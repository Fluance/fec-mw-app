package net.fluance.cockpit.app.service.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import net.fluance.cockpit.core.model.jdbc.company.Capacity;
import net.fluance.cockpit.core.model.jdbc.company.CompanyDetails;
import net.fluance.cockpit.core.model.jdbc.whiteboard.WhiteBoardMapper;
import net.fluance.cockpit.core.model.jpa.catalog.CatalogDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewEntity;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteboardRoomsConfigurationEntity;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteboardRoomsConfigurationID;
import net.fluance.cockpit.core.repository.jdbc.company.CompanyDetailsRepository;
import net.fluance.cockpit.core.repository.jpa.whiteboard.WhiteboardRoomConfigurationRepository;

@Service
public class WhiteBoardCapacityService {
	
	private static final Logger LOGGER = LogManager.getLogger(WhiteBoardCapacityService.class);
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	CatalogService catalogService;
	
	@Autowired
	CompanyDetailsRepository companyDetailsRepository;
	
	@Autowired
	WhiteboardRoomConfigurationRepository whiteboardRoomConfigurationRepository;
		
	private static final String OCCUPANCY_PATIENTS = "patients";
	private static final String OCCUPANCY_AVAILABILITIES = "availabilities";
	
	private static final String ROOM_TYPE = "room_type";
	private static final String DEFAULT_LANGUAGE = "EN";
	private static final String ROOM_CAPACITY = "room_capacity";
	private static final String PRESIDENTIAL_SUITE = "PRS";
	private static final String MONT_BLANC_SUITE = "MBS";
	private static final String MEDICAL_DESK = "MED";
	private static final String GUEST_PATIENT_CLASS = "ACC";
	private static final String VIP_PATIENT_CLASS = "VIP";
	private static final String BED1 = "1";
	private static final String BED2 = "2";
	private static final String BED0 = "0";
	private static final String ROOM_TYPE_1BED = "1BED";
	private static final String ROOM_TYPE_1BED_JUNIOR_SUITE = "1BJS";
	
	/**
	 * Query the DB to get the capacities for a company, that is all the rooms that a company has in the DB defined.<br>
	 * After it matches the rooms with the configuration for a room for a company at the DB.<br>
	 * Finally it fills the map of rooms and empty {@link WhiteBoardViewEntity}
	 * 
	 * @param companyId
	 * @param serviceId
	 * @param limit
	 * @param offset
	 * @return Return a map of rooms and {@link WhiteBoardViewEntity}
	 */
	public Map<String, WhiteBoardViewEntity> generateWhiteBoardViewEntitiesBasedOnCompanyCapacity(Long companyId, String serviceId, Integer limit,
			Integer offset) {		
		CompanyDetails companyDetails = companyDetailsRepository.findOne(companyId.intValue());
		WhiteboardRoomsConfigurationEntity whiteboardRoomsConfigurationEntity;
		
		//FEHC-4072 Calculate the empty beds by room and capacity
		List<Capacity> capacities = companyService.getCapacity(companyId.intValue(), null, serviceId, limit, offset);		
		Map<String, WhiteBoardViewEntity> emptyRooms = new HashMap<String, WhiteBoardViewEntity>();		
		for (Capacity capacity : capacities) {
			
			whiteboardRoomsConfigurationEntity = whiteboardRoomConfigurationRepository.findOne(new WhiteboardRoomsConfigurationID(companyDetails.getCode(), serviceId, capacity.getRoomnumber()));
			
			calculateCapacityForEmptyRoomsMap(companyId, whiteboardRoomsConfigurationEntity, capacity);
			
			generateEmptyRoomsMap(whiteboardRoomsConfigurationEntity, emptyRooms, capacity); 
		}
		return emptyRooms;
	}

	private void calculateCapacityForEmptyRoomsMap(Long companyId, WhiteboardRoomsConfigurationEntity whiteboardRoomsConfigurationEntity, Capacity capacity) {
		if(whiteboardRoomsConfigurationEntity != null && !StringUtils.isEmpty(whiteboardRoomsConfigurationEntity.getRoomType())) {
			if(PRESIDENTIAL_SUITE.equalsIgnoreCase(whiteboardRoomsConfigurationEntity.getRoomType()) || MONT_BLANC_SUITE.equalsIgnoreCase(whiteboardRoomsConfigurationEntity.getRoomType())) {
				//Room is "Presidential Suite" (capacity = many)
				capacity.setNbbed(1);
			}else {
				//Others: Room is "Medical Desk" (capacity = 0) or Room is "2 Bed" (capacity = 2)  or Room is "1 Bed" (capacity = 1) 
				CatalogDTO catalogDTO = catalogService.getCatalogByPK(companyId, whiteboardRoomsConfigurationEntity.getRoomType(), DEFAULT_LANGUAGE, ROOM_TYPE);
				if(catalogDTO != null && catalogDTO.getExtra() != null && catalogDTO.getExtra().containsKey(ROOM_CAPACITY) && !StringUtils.isEmpty(catalogDTO.getExtra().containsKey(ROOM_CAPACITY))) {
					capacity.setNbbed(Integer.valueOf(catalogDTO.getExtra().get(ROOM_CAPACITY)));
				}
			}				
		}
	}
	
	private void generateEmptyRoomsMap(WhiteboardRoomsConfigurationEntity whiteboardRoomsConfigurationEntity,
			Map<String, WhiteBoardViewEntity> emptyRooms, Capacity capacity) {
		
		//remove rooms with display == false
		if(whiteboardRoomsConfigurationEntity == null || 
				(whiteboardRoomsConfigurationEntity!=null && whiteboardRoomsConfigurationEntity.isDisplay())) {
			
			String roomType = null;
			
			if(whiteboardRoomsConfigurationEntity !=null && !StringUtils.isEmpty(whiteboardRoomsConfigurationEntity.getRoomType())) {
				roomType = whiteboardRoomsConfigurationEntity.getRoomType();
			}
			
			if(capacity.getNbbed() == 0) {
				createDefaultViewEntity(emptyRooms, capacity, capacity.getNbbed(), roomType);
			} 
			
			if(capacity.getNbbed() > 0) {
				for(int i=1; i <= capacity.getNbbed(); i++) {
					createDefaultViewEntity(emptyRooms, capacity, i, roomType);
				}
			}
		}
	}

	private void createDefaultViewEntity(Map<String, WhiteBoardViewEntity> emptyRooms, Capacity capacity, int i, String roomType) {
		WhiteBoardViewEntity whiteBoardViewEntityDefault = new WhiteBoardViewEntity();
		whiteBoardViewEntityDefault.setRoom(capacity.getRoomnumber());
		whiteBoardViewEntityDefault.setPatientBed(String.valueOf(i));
		whiteBoardViewEntityDefault.setCapacity(capacity.getNbbed());
		whiteBoardViewEntityDefault.setRoomType(roomType);
		emptyRooms.put(capacity.getRoomnumber() + "." + i, whiteBoardViewEntityDefault);
	}
	
	/**
	 * Set the capacity for every room and show as empty room some special rooms like type 'MED'
	 * 
	 * @param whiteBoardViewDtos
	 */
	public void manageRoomsCapacities(List<WhiteBoardViewDTO> whiteBoardViewDtos) {
		
		Map<String, WhiteBoardViewDTO> companyWhiteBoardViewDTOs = new HashMap<String, WhiteBoardViewDTO>();
		for (WhiteBoardViewDTO whiteBoardViewDto : whiteBoardViewDtos) {
			companyWhiteBoardViewDTOs.put(whiteBoardViewDto.getRoom() + "." + whiteBoardViewDto.getPatientBed(), whiteBoardViewDto);
		}
		
		for (WhiteBoardViewDTO whiteBoardViewDto : whiteBoardViewDtos) {			
			//Take the original or conf capacity
			whiteBoardViewDto.setCapacity(calculateCapacity(whiteBoardViewDto)); 
			
			//if room type = MEDICAL_DESK --> show empty room
			if(!StringUtils.isEmpty(whiteBoardViewDto.getRoomType()) && MEDICAL_DESK.equalsIgnoreCase(whiteBoardViewDto.getRoomType())){
				whiteBoardViewDto = createWhiteboardViewDto(whiteBoardViewDto.getRoom(), BED0, whiteBoardViewDto.getCapacity());	
			}else if(whiteBoardViewDto.getCapacity() != null && whiteBoardViewDto.getCapacity() == 0) {
				//Capacity = 0 --> show the room empty
				whiteBoardViewDto = createWhiteboardViewDto(whiteBoardViewDto.getRoom(), whiteBoardViewDto.getPatientBed(), whiteBoardViewDto.getCapacity());			
			}
		}
	}

	private Integer calculateCapacity(WhiteBoardViewDTO whiteBoardViewDto) {
		if(PRESIDENTIAL_SUITE.equalsIgnoreCase(whiteBoardViewDto.getRoomType()) || MONT_BLANC_SUITE.equalsIgnoreCase(whiteBoardViewDto.getRoomType())) {
			//Room is "Presidential Suite" (capacity = many)
			return 1;
		} else if(whiteBoardViewDto.getConfCapacity() != null) {
				return whiteBoardViewDto.getConfCapacity();
		} else {
			return whiteBoardViewDto.getOriginalCapacity();
		}
	}

	private WhiteBoardViewDTO createWhiteboardViewDto(String room, String patientBed, Integer capacity) {
		WhiteBoardViewDTO whiteBoardViewDto = new WhiteBoardViewDTO();
		whiteBoardViewDto.setRoom(room);
		whiteBoardViewDto.setPatientBed(patientBed);	
		whiteBoardViewDto.setCapacity(capacity);
		return whiteBoardViewDto;
	}
	
	/**
	 * Mix the entities with the map of empty rooms<br>
	 * The method manages the bed of a room append '.' + 'patient bed' to the key in the result map.
	 * 
	 * @param entities
	 * @param emptyRooms
	 * @return
	 */
	public List<WhiteBoardViewDTO> generateWhiteBoardViewDTOsWithEntitiesAndCompanyRooms(List<WhiteBoardViewEntity> entities, Map<String, WhiteBoardViewEntity> emptyRooms) {
		for (WhiteBoardViewEntity entity : entities) {
			if(!StringUtils.isEmpty(entity.getRoom())) {
				WhiteBoardViewEntity auxEntity = emptyRooms.get(entity.getRoom() + "." + entity.getPatientBed());
				if(auxEntity != null) {
					entity.setCapacity(auxEntity.getCapacity());
				}
				if(entity.getRoomType() == null || (!StringUtils.isEmpty(entity.getRoomType()) && !MEDICAL_DESK.equalsIgnoreCase(entity.getRoomType()))){
					if(entity.getConfCapacity() != null && (entity.getConfCapacity() < Integer.valueOf(entity.getPatientBed())) && VIP_PATIENT_CLASS.equalsIgnoreCase(entity.getInsurance())) {
						WhiteBoardViewEntity existingEntity = emptyRooms.get(entity.getRoom() + "." + entity.getConfCapacity());
						if(existingEntity != null && GUEST_PATIENT_CLASS.equalsIgnoreCase(existingEntity.getPatientClass())) {
							entity.setPatientBed(entity.getConfCapacity().toString());
						}
					}
					emptyRooms.put(entity.getRoom() + "." + entity.getPatientBed(), entity);
				}
			}
		}
		
		return WhiteBoardMapper.toModel(new ArrayList<WhiteBoardViewEntity>(emptyRooms.values()));
	}
	
	/**
	 * Apply the filter for ocupancy.
	 * The values can be:
	 * <ul>
	 * <li>'patients', see only rooms with patients</li>
	 * <li>'availabilities', see only empty rooms</li>
	 * <li>null or any other, all entries</li>
	 * </ul>
	 * 
	 * @param rooms
	 * @param occupancy
	 * @return
	 */
	public List<WhiteBoardViewDTO> applyOccupancyFilter(List<WhiteBoardViewDTO> rooms, String occupancy) {		
		if(!StringUtils.isEmpty(occupancy) && OCCUPANCY_PATIENTS.equalsIgnoreCase(occupancy)) {
			return getOnlyBedsWithPatients(rooms);			
		} else if(!StringUtils.isEmpty(occupancy) && OCCUPANCY_AVAILABILITIES.equalsIgnoreCase(occupancy)) {
			return getOnlyFreeBeds(rooms);
		} else {
			return rooms;
		}
	}

	private List<WhiteBoardViewDTO> getOnlyFreeBeds(List<WhiteBoardViewDTO> rooms) {		
		List<WhiteBoardViewDTO> emptyBeds = new ArrayList<>();
		
		for (WhiteBoardViewDTO whiteBoardViewDTO : rooms) {
			if(whiteBoardViewDTO.getPatientId() == null) {
				emptyBeds.add(whiteBoardViewDTO);
			}
		}
		
		return emptyBeds;
	}
	
	private List<WhiteBoardViewDTO> getOnlyBedsWithPatients(List<WhiteBoardViewDTO> rooms) {
		List<WhiteBoardViewDTO> bedsWithPatient = new ArrayList<>();
		
		for (WhiteBoardViewDTO whiteBoardViewDTO : rooms) {
			if(whiteBoardViewDTO.getPatientId() != null) {
				if(whiteBoardViewDTO.getInsurance() != null && !GUEST_PATIENT_CLASS.equalsIgnoreCase(whiteBoardViewDTO.getInsurance())) {
					bedsWithPatient.add(whiteBoardViewDTO);
				}
			}
		}
		
		return bedsWithPatient;
	}
	
	/**
	 * Adjusts the capacity for Presidential Suite, Mont-Blanc Suite & 1 Bed Room<br>
	 * The method will remove to the list GUESTS for some kinds of rooms.
	 * 
	 * @param whiteBoardViewDtos
	 * @return
	 */
	public List<WhiteBoardViewDTO> adjustSuitesAnd1BedCapacity(List<WhiteBoardViewDTO> whiteBoardViewDtos) {
		
		Map<String, WhiteBoardViewDTO> companyWhiteBoardViewDTOs = whiteBoardViewDtoListToMap(whiteBoardViewDtos);
		
		for (WhiteBoardViewDTO whiteBoardViewDto : whiteBoardViewDtos) {
			if(PRESIDENTIAL_SUITE.equalsIgnoreCase(whiteBoardViewDto.getRoomType()) 
					|| MONT_BLANC_SUITE.equalsIgnoreCase(whiteBoardViewDto.getRoomType()) 
					|| ROOM_TYPE_1BED.equalsIgnoreCase(whiteBoardViewDto.getRoomType()) 
					|| ROOM_TYPE_1BED_JUNIOR_SUITE.equalsIgnoreCase(whiteBoardViewDto.getRoomType()) ) {
				Boolean haveVipAndGuest = haveVipAndGuestPatients(companyWhiteBoardViewDTOs, whiteBoardViewDto);
				//Remove the guest if the Suite room have VIP also
				if(haveVipAndGuest) {
					removeGuests(companyWhiteBoardViewDTOs, whiteBoardViewDto);
					
					updatePatientVipBed(companyWhiteBoardViewDTOs, whiteBoardViewDto);
				} else if(BED2.equals(whiteBoardViewDto.getPatientBed())) {
					updatePatientBedIfPossible(companyWhiteBoardViewDTOs, whiteBoardViewDto);
				}
			} 
		}
		return new ArrayList<WhiteBoardViewDTO>(companyWhiteBoardViewDTOs.values());		
	}
	
	private void updatePatientBedIfPossible(Map<String, WhiteBoardViewDTO> companyWhiteBoardViewDTOs, WhiteBoardViewDTO whiteBoardViewDto) {
		if(BED2.equals(whiteBoardViewDto.getPatientBed())){
			if(whiteBoardViewDto.getPatientId() != null) {						
				//Switch and remove if there is a bed1 and it's empty
				WhiteBoardViewDTO whiteBoardViewDtoForBed1 = companyWhiteBoardViewDTOs.get(whiteBoardViewDto.getRoom() + "." + BED1);
				if(whiteBoardViewDtoForBed1 != null && whiteBoardViewDtoForBed1.getPatientId() == null) {
					whiteBoardViewDto.setPatientBed(BED1);	
					companyWhiteBoardViewDTOs.put(whiteBoardViewDto.getRoom() + "." + BED1 , whiteBoardViewDto);
					
					companyWhiteBoardViewDTOs.remove(whiteBoardViewDto.getRoom() + "." + BED2);
				} else {
					if(whiteBoardViewDtoForBed1 != null) {
						//There is a bed1 and it's filled, log as warning
						LOGGER.warn(String.format("Company: %s, Service: %s, Room: %s. Is configured like 1 bed room and it has 2 patients set", whiteBoardViewDtoForBed1.getCompanyId(), whiteBoardViewDtoForBed1.getServiceId(),
								whiteBoardViewDtoForBed1.getRoom()));
					}
				}
			} else {
				companyWhiteBoardViewDTOs.remove(whiteBoardViewDto.getRoom() + "." + whiteBoardViewDto.getPatientBed());
			}
		}
	}
	
	private void updatePatientVipBed(Map<String, WhiteBoardViewDTO> companyWhiteBoardViewDTOs, WhiteBoardViewDTO whiteBoardViewDto) {
		if(whiteBoardViewDto != null && !StringUtils.isEmpty(whiteBoardViewDto.getInsurance()) && VIP_PATIENT_CLASS.equalsIgnoreCase(whiteBoardViewDto.getInsurance()) && BED2.equals(whiteBoardViewDto.getPatientBed())){
			
			WhiteBoardViewDTO whiteBoardViewDtoForBed1 = companyWhiteBoardViewDTOs.get(whiteBoardViewDto.getRoom() + "." + BED1);
			
			if(whiteBoardViewDtoForBed1!=null) {
				if(!StringUtils.isEmpty(whiteBoardViewDtoForBed1.getInsurance()) && GUEST_PATIENT_CLASS.equalsIgnoreCase(whiteBoardViewDtoForBed1.getInsurance())){
					whiteBoardViewDto.setPatientBed(BED1);
					companyWhiteBoardViewDTOs.put(whiteBoardViewDto.getRoom() + "." + BED1, whiteBoardViewDto);
					
					companyWhiteBoardViewDTOs.remove(whiteBoardViewDtoForBed1.getRoom() + "." + BED2);
				}
			}		
		}
	}
	
	private void removeGuests(Map<String, WhiteBoardViewDTO> companyWhiteBoardViewDTOs, WhiteBoardViewDTO whiteBoardViewDto) {		
		if(whiteBoardViewDto != null && !StringUtils.isEmpty(whiteBoardViewDto.getInsurance()) && GUEST_PATIENT_CLASS.equalsIgnoreCase(whiteBoardViewDto.getInsurance()) && BED2.equals(whiteBoardViewDto.getPatientBed())){
			companyWhiteBoardViewDTOs.remove(whiteBoardViewDto.getRoom() + "." + whiteBoardViewDto.getPatientBed());
		}
	}
	
	private Integer getCapacity(WhiteBoardViewDTO whiteBoardViewDto) {
		Integer capacity = 1; 
		if(whiteBoardViewDto.getCapacity() != null) {
			capacity = whiteBoardViewDto.getCapacity();
		} else if (whiteBoardViewDto.getOriginalCapacity() != null) {
			capacity = whiteBoardViewDto.getOriginalCapacity();
		}
		return capacity;
	}
	
	private Map<String, WhiteBoardViewDTO> whiteBoardViewDtoListToMap(List<WhiteBoardViewDTO> whiteBoardViewDtos) {
		Map<String, WhiteBoardViewDTO> companyWhiteBoardViewDTOs = new HashMap<String, WhiteBoardViewDTO>();
		for (WhiteBoardViewDTO whiteBoardViewDto : whiteBoardViewDtos) {
			companyWhiteBoardViewDTOs.put(whiteBoardViewDto.getRoom() + "." + whiteBoardViewDto.getPatientBed(), whiteBoardViewDto);
		}
		return companyWhiteBoardViewDTOs;
	}

	private Boolean haveVipAndGuestPatients(Map<String, WhiteBoardViewDTO> whiteBoardViewMap, WhiteBoardViewDTO whiteBoardViewDto) {
		Integer capacity = getCapacity(whiteBoardViewDto);
		boolean hasVip = false;
		boolean hasGuest = false;		
		
		for(int i=1; i <= capacity; i++) {
			WhiteBoardViewDTO auxWhiteBoardViewDto = whiteBoardViewMap.get(whiteBoardViewDto.getRoom() + "." + i);
			if(auxWhiteBoardViewDto != null && !StringUtils.isEmpty(auxWhiteBoardViewDto.getInsurance())) {
				if(VIP_PATIENT_CLASS.equalsIgnoreCase(auxWhiteBoardViewDto.getInsurance())) {
					hasVip = true;
				}
				
				if(GUEST_PATIENT_CLASS.equalsIgnoreCase(auxWhiteBoardViewDto.getInsurance())) {
					hasGuest = true;
				}
			}
		}
		return hasVip && hasGuest;
	}
}
