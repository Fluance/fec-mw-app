package net.fluance.cockpit.app.service.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.web.util.exceptions.ConflictException;
import net.fluance.app.web.util.exceptions.ForbiddenException;
import net.fluance.app.web.util.exceptions.LockedException;
import net.fluance.app.web.util.exceptions.MustRequestLockException;
import net.fluance.app.web.util.exceptions.NotFoundException;
import net.fluance.cockpit.core.model.LightServiceDescription;
import net.fluance.cockpit.core.model.jdbc.company.CompaniesList;
import net.fluance.cockpit.core.model.jdbc.company.CompanyDetails;
import net.fluance.cockpit.core.model.jdbc.whiteboard.PatientListRulesMapper;
import net.fluance.cockpit.core.model.jdbc.whiteboard.WhiteBoardDao;
import net.fluance.cockpit.core.model.jdbc.whiteboard.WhiteBoardMapper;
import net.fluance.cockpit.core.model.jpa.catalog.CatalogDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardEntryDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewEntity;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteboardRoomsConfigurationDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteboardRoomsConfigurationEntity;
import net.fluance.cockpit.core.repository.jdbc.company.CompaniesListRepository;
import net.fluance.cockpit.core.repository.jdbc.company.CompanyDetailsRepository;
import net.fluance.cockpit.core.repository.jpa.whiteboard.WhiteboardRoomConfigurationRepository;
import net.fluance.commons.net.MapUtils;

@Service
public class WhiteBoardService {

	private static final Logger LOGGER = LogManager.getLogger();

	@Value("#{${whiteboard.config.featureEnabledFor}}")
	private Map<Integer, List<String>> companyUnits;
	@Value("#{${whiteboard.config.roomConfigEnabledFor}}")
	protected Map<Integer, List<String>> roomConfigEnabledFor;
	@Value("#{${whiteboard.config.patientListRules}}")
	private Map<Long, String> patientListRules;

	public static final String LOCK_RESOURCE_TYPE_WHITEBOARD = "whiteboard";
	private static final String DEFAULT_LANGUAGE = "EN";
	private static final String DIET = "Diet";
	private static final String ROOM_TYPE = "room_type";
	private final String SORT_ORDER_ASC = "ASC";
	private static final String DEFAULT_SORT_ORDER = "ASC";
	private static final String DEFAULT_ORDER_BY = "patientroom,patientbed";
	private static final String DEFAULT_OCCUPANCY = "patients";

	@Autowired
	WhiteBoardDao whiteBoardDao;

	@Autowired
	CatalogService catalogService;

	@Autowired
	CompanyDetailsRepository companyDetailsRepository;

	@Autowired
	CompaniesListRepository companiesListRepository;

	@Autowired
	LockService lockService;

	@Autowired
	WhiteboardRoomConfigurationRepository whiteboardRoomConfigurationRepository;

	@Autowired
	WhiteBoardCapacityService whiteBoardCapacityService;

	/**
	 * The method return the Object that represents a full whiteboard payload, but with the entries that are expected due to the paramters
	 * 
	 * @param companyId
	 * @param serviceId
	 * @param admitDate
	 * @param display
	 * @param confCapacity
	 * @param originalCapacity
	 * @param occupancy
	 * @param limit
	 * @param offset
	 * @param orderBy
	 * @param sortOrder
	 * @return {@link WhiteBoardDTO}
	 */
	public WhiteBoardDTO getAWhiteBoard(Long companyId, String serviceId, LocalDate admitDate, Boolean display,
			Integer confCapacity, Integer originalCapacity, String occupancy, Integer limit, Integer offset,
			String orderBy, String sortOrder) {
		// Protection
		checkFeauture(companyId, serviceId);
		WhiteBoardDTO whiteBoardDto = new WhiteBoardDTO();
		whiteBoardDto.setCompanyId(companyId);
		whiteBoardDto.setServiceId(serviceId);
		whiteBoardDto.setEntries(getAllWhiteboardViewEntries(companyId, serviceId, admitDate, display, confCapacity,
				originalCapacity, occupancy, limit, offset, orderBy, sortOrder));
		return whiteBoardDto;
	}

	/**
	 * Get the list of whiteboard entities.<br>
	 * The method queries the DB to get the data but do different operation with it to get as final result a self generated data set,
	 * that can not match the DB results.<br>
	 * The list of process is:
	 * <ul>
	 * <li>1. Query the DB to get the entities</li>
	 * <li>2. Get all the possible entities that a company can have, represent all the rooms</li>
	 * <li>3. Generate only one list of rooms with the patients on every rooms, manages the room types</li>
	 * <li>4. Apply the rules for the company, if there are some</li>
	 * <li>5. Manage the capacity for the rooms, Adjusts for Presidential Suite, Mont-Blanc Suite & 1 Bed Room</li>
	 * <li>6. Manage the ocupancy of the room, remove patients if need</li>
	 * <li>7. Short the final result</li>
	 * </ul>
	 * 
	 * @param companyId
	 * @param serviceId
	 * @param admitDate
	 * @param display
	 * @param confCapacity
	 * @param originalCapacity
	 * @param occupancy
	 * @param limit
	 * @param offset
	 * @param orderBy
	 * @param sortOrder
	 * @return
	 */
	private List<WhiteBoardViewDTO> getAllWhiteboardViewEntries(Long companyId, String serviceId, LocalDate admitDate,
			Boolean display, Integer confCapacity, Integer originalCapacity, String occupancy, Integer limit,
			Integer offset, String orderBy, String sortOrder) {

		if (companyId == null || serviceId == null) {
			throw new IllegalArgumentException("ServiceId and CompanyId should not be null");
		}

		List<WhiteBoardViewEntity> whiteBoardViewEntities = whiteBoardDao.getAllWhiteboardEntries(companyId, serviceId,
				admitDate, display, confCapacity, originalCapacity, limit, offset, orderBy, sortOrder);

		Map<String, WhiteBoardViewEntity> companyWhiteBoardViewEntities = whiteBoardCapacityService
				.generateWhiteBoardViewEntitiesBasedOnCompanyCapacity(companyId, serviceId, limit, offset);

		List<WhiteBoardViewDTO> whiteBoardViewDtos = whiteBoardCapacityService
				.generateWhiteBoardViewDTOsWithEntitiesAndCompanyRooms(whiteBoardViewEntities,
						companyWhiteBoardViewEntities);

		applyPatientRules(companyId, whiteBoardViewDtos);

		// Adjusts for Presidential Suite, Mont-Blanc Suite & 1 Bed Room
		whiteBoardViewDtos = whiteBoardCapacityService.adjustSuitesAnd1BedCapacity(whiteBoardViewDtos);

		whiteBoardCapacityService.manageRoomsCapacities(whiteBoardViewDtos);

		whiteBoardViewDtos = whiteBoardCapacityService.applyOccupancyFilter(whiteBoardViewDtos, occupancy);

		// Sort data, default is ascendent
		Collections.sort(whiteBoardViewDtos);
		if (StringUtils.isEmpty(sortOrder) && !SORT_ORDER_ASC.equals(sortOrder)) {
			Collections.reverse(whiteBoardViewDtos);
		}

		return whiteBoardViewDtos;
	}

	/**
	 * Apply the patient rules defined at the property 'whiteboard.config.patientListRules'
	 * 
	 * @param companyId
	 * @param whiteBoardViewDtos
	 */
	private void applyPatientRules(Long companyId, List<WhiteBoardViewDTO> whiteBoardViewDtos) {
		PatientListRulesMapper patientListRulesMapper = new PatientListRulesMapper(patientListRules, companyId);
		boolean areThereRulesForTheCompany = patientListRulesMapper.hasRules();
		if (areThereRulesForTheCompany) {
			for (WhiteBoardViewDTO whiteBoardViewDto : whiteBoardViewDtos) {
				patientListRulesMapper.updateInsuranceByRules(whiteBoardViewDto);
			}
		}
	}

	public List<WhiteBoardEntryDTO> getAllWhiteboardEntries(Long companyId, String serviceId, Integer limit,
			Integer offset, String orderBy, String sortOrder) {
		if (companyId == null || serviceId == null) {
			throw new IllegalArgumentException("ServiceId and CompanyId should not be null");
		}
		// Protection
		checkFeauture(companyId, serviceId);
		List<WhiteBoardViewEntity> whiteBoardViewEntities = whiteBoardDao.getAllWhiteboardEntries(companyId, serviceId,
				null, null, null, null, limit, offset, orderBy, sortOrder);
		return WhiteBoardMapper.toEntryModel(whiteBoardViewEntities);
	}

	public WhiteBoardDTO getWhiteBoardIfItChanged(WhiteBoardDTO whiteboard, Integer limit, Integer offset) {
		WhiteBoardDTO newOne = getAWhiteBoard(whiteboard.getCompanyId(), whiteboard.getServiceId(), null, null, null,
				null, null, limit, offset, null, null);

		int newhash = getWhiteBoardHash(newOne);
		int oldhash = getWhiteBoardHash(whiteboard);
		if (newhash != oldhash) {
			return newOne;
		} else {
			return null;
		}
	}

	public int getWhiteBoardHash(WhiteBoardDTO whiteboard) {
		Gson gson = new Gson();
		String jsonInString = gson.toJson(whiteboard);
		return jsonInString.hashCode();
	}

	public List<String> getNurses(Long companyId, String serviceId, Integer limit, Integer offset) {
		return whiteBoardDao.getNurses(companyId, serviceId, limit, offset);
	}

	public List<String> getPhysicians(Long companyId, String serviceId, Integer limit, Integer offset) {
		return whiteBoardDao.getPhysicians(companyId, serviceId, limit, offset);
	}

	public WhiteBoardViewDTO update(WhiteBoardViewDTO whiteBoard, Long companyId, String serviceId, User user)
			throws NotFoundException {
		if (companyId == null || serviceId == null || whiteBoard.getVisitNumber() == null) {
			throw new IllegalArgumentException("ServiceId[" + serviceId + "], CompanyId[" + companyId
					+ "] and visitNumber[" + whiteBoard.getVisitNumber() + "] should not be null");
		}
		// Protection
		checkFeauture(companyId, serviceId);

		ensureWhiteBoardId(whiteBoard, companyId, serviceId);

		WhiteBoardViewEntity whiteBoardEntity = WhiteBoardMapper.toEntity(whiteBoard);

		getLockForWhiteBoardEntity(whiteBoardEntity, user);

		LOGGER.debug("Updating whiteBoard item ID: " + whiteBoard.getId());
		try {
			WhiteBoardViewEntity whiteBoardEntryEntity = whiteBoardDao.update(whiteBoardEntity);
			return getOneEntry(whiteBoardEntryEntity.getId(), companyId, serviceId);
		} catch (JpaSystemException e) {
			if (e.getCause().getCause().getMessage().contains("out of date")) {
				throw new ConflictException(e.getCause().getCause().getMessage());
			} else {
				throw e;
			}
		}
	}

	private void ensureWhiteBoardId(WhiteBoardViewDTO whiteBoard, Long companyId, String serviceId) {
		if (whiteBoard.getId() == null) {
			Boolean exist = existVisitNumber(whiteBoard.getVisitNumber());
			Boolean existVisitNumberOnView = existVisitNumberOnView(whiteBoard.getVisitNumber(), companyId, serviceId);
			if (!existVisitNumberOnView) {
				throw new ConflictException("This visitNumber is not valid");
			}
			if (exist) {
				whiteBoard.setId(getOneEntryByVisitNumber(whiteBoard.getVisitNumber()).getId());
			}
		}
	}

	private void getLockForWhiteBoardEntity(WhiteBoardViewEntity whiteBoardEntity, User user) {
		/** FEHC-3499: Enforce lock on resource when updating a whiteboard item */
		if (whiteBoardEntity != null && whiteBoardEntity.getId() != null && whiteBoardEntity.getId() > 0) {
			LOGGER.info(
					"The user:" + user.getUsername() + " Tries to LOCK whiteboard item:" + whiteBoardEntity.getId());
			if (!lockService.isLockedByUser(whiteBoardEntity.getId(), LOCK_RESOURCE_TYPE_WHITEBOARD, user.getUsername(),
					user.getDomain())) {
				if (lockService.verify(whiteBoardEntity.getId(), LOCK_RESOURCE_TYPE_WHITEBOARD).isLocked()) {
					throw new LockedException();
				} else {
					throw new MustRequestLockException(MustRequestLockException.RESOURCE_NOT_LOCKED);
				}
			}
		} else {
			LOGGER.error("The given Whiteboard item ID doesn't exist: " + whiteBoardEntity.getId());
			throw new NotFoundException();
		}
	}

	private Boolean existVisitNumberOnView(Long visitNumber, Long companyId, String serviceId) {
		return whiteBoardDao.existVisitNumberOnView(visitNumber, companyId, serviceId);
	}

	public WhiteBoardViewDTO getOneEntry(Long whiteboardId, Long companyId, String serviceId) {
		// Protection
		checkFeauture(companyId, serviceId);
		PatientListRulesMapper patientListRulesMapper = new PatientListRulesMapper(patientListRules, companyId);
		WhiteBoardViewDTO whiteBoardViewDto = WhiteBoardMapper.toModel(whiteBoardDao.getOneEntry(whiteboardId));
		boolean areThereRulesForTheCompany = patientListRulesMapper.hasRules();

		if (areThereRulesForTheCompany) {
			patientListRulesMapper.updateInsuranceByRules(whiteBoardViewDto);
		}

		return whiteBoardViewDto;
	}

	private WhiteBoardViewDTO getOneEntryByVisitNumber(Long visitNumber) {
		return WhiteBoardMapper.toModel(whiteBoardDao.getOneEntryByVisitNummer(visitNumber));
	}

	private Boolean existVisitNumber(Long visitNumber) {
		return whiteBoardDao.existVisitNumber(visitNumber);
	}

	public boolean isFeatureEnabled(int companyId, String unit) {
		return MapUtils.isValueAvaiableForKey(companyUnits, companyId, unit);
	}

	public boolean isRoomsConfigEnabled(int companyId, String serviceId) {
		return MapUtils.isValueAvaiableForKey(roomConfigEnabledFor, companyId, serviceId);
	}

	public void checkFeauture(Long companyId, String serviceId) {
		if (!isFeatureEnabled(companyId.intValue(), serviceId)) {
			throw new ForbiddenException(
					"Feature not available for companyId : " + companyId + " , serviceId :" + serviceId);
		}
	}

	public List<CatalogDTO> getDiets(Long companyId, String language) {
		return catalogService.getCatalogs(companyId, DIET, language);
	}

	public CatalogDTO getDietByPK(Long companyId, String code, String language, String type) {
		if (StringUtils.isEmpty(language)) {
			language = DEFAULT_LANGUAGE;
		}
		if (StringUtils.isEmpty(type)) {
			type = DIET;
		}
		return catalogService.getCatalogByPK(companyId, code, language, type);
	}

	public List<CatalogDTO> getRoomTypes(Long companyId, String language) {
		return catalogService.getCatalogs(companyId, ROOM_TYPE, language);
	}

	public CatalogDTO getRoomTypeByPK(Long companyId, String code, String language, String type) {
		if (StringUtils.isEmpty(language)) {
			language = DEFAULT_LANGUAGE;
		}
		if (StringUtils.isEmpty(type)) {
			type = ROOM_TYPE;
		}
		return catalogService.getCatalogByPK(companyId, code, language, type);
	}

	public List<WhiteboardRoomsConfigurationDTO> getRoomsConfiguration(Long companyId, String serviceId)
			throws ConflictException {
		CompanyDetails companyDetails = companyDetailsRepository.findOne(companyId.intValue());
		if (companyDetails == null) {
			throw new ConflictException("Company ID : " + companyId + " not managed in our System");
		}
		List<WhiteboardRoomsConfigurationEntity> whiteboardRoomsConfigurationEntities = (List<WhiteboardRoomsConfigurationEntity>) whiteboardRoomConfigurationRepository
				.findByCompanyAndService(companyDetails.getCode(), serviceId);
		List<WhiteboardRoomsConfigurationDTO> whiteboardRoomsConfigurations = whiteboardRoomsConfigurationEntities
				.stream().map(whiteboardRoomsConfigurationEntity -> new WhiteboardRoomsConfigurationDTO(
						whiteboardRoomsConfigurationEntity))
				.collect(Collectors.toList());
		return whiteboardRoomsConfigurations;
	}

	public List<WhiteboardRoomsConfigurationDTO> updateRoomConfiguration(
			List<WhiteboardRoomsConfigurationDTO> whiteboardRoomsConfigurationList) throws NotFoundException {
		// Save
		List<WhiteboardRoomsConfigurationEntity> whiteboardRoomsConfigurationEntities = whiteboardRoomsConfigurationList
				.stream().map(whiteboardRoomsConfigurationDTO -> new WhiteboardRoomsConfigurationEntity(
						whiteboardRoomsConfigurationDTO))
				.collect(Collectors.toList());
		Iterable<WhiteboardRoomsConfigurationEntity> savedEntitiesIterable = whiteboardRoomConfigurationRepository
				.save(whiteboardRoomsConfigurationEntities);

		// Return Result well formatted
		List<WhiteboardRoomsConfigurationEntity> savedEntities = StreamSupport
				.stream(savedEntitiesIterable.spliterator(), false).collect(Collectors.toList());
		List<WhiteboardRoomsConfigurationDTO> whiteboardRoomsConfigurations = savedEntities.stream()
				.map(whiteboardRoomsConfigurationEntity -> new WhiteboardRoomsConfigurationDTO(
						whiteboardRoomsConfigurationEntity))
				.collect(Collectors.toList());
		return whiteboardRoomsConfigurations;
	}

	/**
	 * Return a list of a special payload objects {@link LightServiceDescription} for the given companyId
	 * 
	 * @param givenCompanyId
	 * @return
	 */
	public List<LightServiceDescription> enabledServicesByCompany(Long givenCompanyId) {
		List<String> emptyList = new ArrayList<>();
		List<LightServiceDescription> enabledUnitsOrServicesForGivenCompany = new ArrayList<>();
		HashMap<String, String> unitsOrServicesDescriptions = new HashMap<String, String>();

		List<String> enabledUnitsOrServicesByConfigurationForGivenCompany = companyUnits.getOrDefault(givenCompanyId.intValue(),
				emptyList);

		// Filtering the whole list of companies, because findOne doesn't work...
		Optional<CompaniesList> givenCompanyOptional = companiesListRepository.findAll().stream()
				.filter(company -> new Long(company.getId()).equals(givenCompanyId)).findFirst();

		// Can't find the company -> bye bye!
		if (!givenCompanyOptional.isPresent())
			throw new IllegalArgumentException(
					"Impossible to find a suitable company for companyId : " + givenCompanyId);

		// Unwrap optional type
		CompaniesList givenCompany = givenCompanyOptional.get();

		// Get services of the given company
		givenCompany.getHospServices().stream().forEach(service -> {
			unitsOrServicesDescriptions.put(service.getHospService(), service.getHospServiceDesc());
		});

		// Get units of the given company
		givenCompany.getUnits().stream().forEach(unit -> {
			unitsOrServicesDescriptions.put(unit.getPatientunit(), unit.getCodedesc());
		});

		// Retrieve units|services descriptions
		enabledUnitsOrServicesByConfigurationForGivenCompany.stream().forEach(serviceCode -> {
			LightServiceDescription enabledServiceDescription = new LightServiceDescription();
			enabledServiceDescription.setCode(serviceCode);
			enabledServiceDescription.setCodeDescription(unitsOrServicesDescriptions.get(serviceCode));
			
			enabledServiceDescription.setNumberOfPatients(countAllWhiteboardViewEntriesByCompanyIdAndServiceIdForNow(givenCompanyId, serviceCode) );
			
			enabledUnitsOrServicesForGivenCompany.add(enabledServiceDescription);
		});
		
		return enabledUnitsOrServicesForGivenCompany;
	}
	
	/**
	 * Returns the number of rooms with patients, occupancy set to 'patients'<br>
	 * For testing reasons the method is package scope
	 * 
	 * @param companyId
	 * @param serviceId
	 * @return
	 */
	int countAllWhiteboardViewEntriesByCompanyIdAndServiceIdForNow(Long companyId, String serviceId) {
		return getAllWhiteboardViewEntriesByCompanyIdAndServiceIdForNow(companyId, serviceId).size();
	}
	
	/**
	 * Return the list of all the {@link WhiteBoardViewDTO} with the occupancy set to 'patients', so all the rooms returned will have patients<br>
	 * For testing reasons the method is package scope
	 * 
	 * @param companyId
	 * @param serviceId
	 * @return
	 */
	List<WhiteBoardViewDTO> getAllWhiteboardViewEntriesByCompanyIdAndServiceIdForNow(Long companyId, String serviceId) {
		if (companyId == null || serviceId == null) {
			throw new IllegalArgumentException("ServiceId and CompanyId should not be null");
		}
				
		List<WhiteBoardViewEntity> whiteBoardViewEntities = whiteBoardDao.getAllWhiteboardEntries(companyId, serviceId, null, null, null, null, 100000, 0, DEFAULT_ORDER_BY, DEFAULT_SORT_ORDER);
		if(whiteBoardViewEntities.size()>0) {
			return getAllWhiteboardViewEntries(companyId, serviceId, null, false, null, null, DEFAULT_OCCUPANCY, 100000, 0, DEFAULT_ORDER_BY, DEFAULT_SORT_ORDER);
		}
		
		return new ArrayList<WhiteBoardViewDTO>();
	}
}
