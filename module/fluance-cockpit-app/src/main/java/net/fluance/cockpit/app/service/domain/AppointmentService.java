package net.fluance.cockpit.app.service.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import net.fluance.app.data.model.identity.EhProfile;
import net.fluance.app.data.model.identity.UserProfile;
import net.fluance.app.web.util.exceptions.ForbiddenException;
import net.fluance.cockpit.app.config.ProviderConfig;
import net.fluance.cockpit.core.model.CompanyReference;
import net.fluance.cockpit.core.model.jdbc.appointment.Appointment;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentDetail;
import net.fluance.cockpit.core.model.jdbc.company.CompanyDetails;
import net.fluance.cockpit.core.model.jdbc.patient.PatientViewEnum;
import net.fluance.cockpit.core.model.jpa.refdata.LanguageMapper;
import net.fluance.cockpit.core.model.jpa.refdata.RefdataEntity;
import net.fluance.cockpit.core.repository.jdbc.appointment.AppointmentDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.appointment.AppointmentPatientRepository;
import net.fluance.cockpit.core.repository.jdbc.company.CompanyDetailsRepository;
import net.fluance.cockpit.core.repository.jpa.refdata.RefdataRepository;
import net.fluance.cockpit.core.util.sql.DoctorAppointmentListOrderByEnum;

/**
 * Search methods for Appointments
 */
@Component
public class AppointmentService {

	@Autowired
	AppointmentPatientRepository appointmentListRepository;

	@Autowired
	AppointmentDetailRepository appointmentDetailRepository;
	
	@Autowired
	CompanyDetailsRepository companyDetailsRepository;

	@Autowired
	RefdataRepository refDataRepository;

	@Autowired
	private ProviderConfig providerConfig;

	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultAppointmentListOrderBy}")
	private String defaultResultAppointmentListOrderBy;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultAppointmentListSortOrder}")
	private String defaultResultAppointmentListSortOrder;

	@Value("#{${appointment.config.enableTranslationByCompany}}")
	private List<String> enableTranslationByCompany;
	@Value("${refdata.config.src}")
	private String refdataSrcFiled;
	@Value("${refdata.config.srctable}")
	private String refdataSrcTableFiled;

	public List<Appointment> getServiceList(Integer visitNumber, String staffId, Integer companyId, String appointmentStartDate, String patientView) {
		if (patientView == null) {
			patientView = PatientViewEnum.WEEK.toString();
		}
		if (visitNumber != null) {
			return appointmentListRepository.findByVisitnb(visitNumber);
		} else if (companyId != null & appointmentStartDate != null & staffId != null) {
			patientView = patientView.toUpperCase();
			if (patientView.equals(PatientViewEnum.DAY.toString())) {
				return appointmentListRepository.findByStaffidAndCompanyid(staffId, companyId, appointmentStartDate,
						appointmentStartDate);
			} else if (patientView.equals(PatientViewEnum.WEEK.toString())) {
				return appointmentListRepository.findByStaffidAndCompanyidAndBegindtAndEnddt(staffId, companyId,
						appointmentStartDate, appointmentStartDate);
			} else {
				throw new IllegalArgumentException("Invalid parameters");
			}
		} else {
			throw new IllegalArgumentException("Invalid parameters");
		}
	}


	public List<AppointmentDetail> getDetailedDoctorAppointmentsSingleStaffId(String staffId, int companyId, String date, String orderBy, String sortOrder, Integer limit, Integer offset){
		offset = validateOffset (offset);
		String finalOrderBy = getOrderBy(orderBy, sortOrder);
		
		if (date != null && !date.isEmpty()){
			return appointmentDetailRepository.findByStaffidAndCompanyidAndBegindt(staffId, companyId, date, finalOrderBy, limit, offset);
		} else {
			return appointmentDetailRepository.findByStaffidAndCompanyid(staffId, companyId, finalOrderBy, limit, offset);
		}
	}

	public List<AppointmentDetail> getDetailedDoctorAppointmentsMultipleStaffIds(Map<Integer, String> companyStaffIds, String date, String orderBy, String sortOrder, Integer limit, Integer offset){
		offset = validateOffset (offset);
		String finalOrderBy = getOrderBy(orderBy, sortOrder);
		return appointmentDetailRepository.findByMultipleStaffids(companyStaffIds, date, finalOrderBy, limit, offset);
	}

	public List<AppointmentDetail> getDetailedDoctorAppointments(Integer companyId, UserProfile userProfile, String date, String orderBy, String sortOrder, Integer limit, Integer offset, String language) throws ForbiddenException{
		EhProfile profile = userProfile.getProfile();
		
		List<AppointmentDetail> appointmentsDetails;
		
		if(companyId == null){
			Map<Integer, String> companyStaffIds = profile.getCompanyStaffIds(providerConfig.getProviderPolypointId());
			if(companyStaffIds==null || companyStaffIds.isEmpty()){
				throw new ForbiddenException(ForbiddenException.NO_STAFF_ID);
			}
			appointmentsDetails = getDetailedDoctorAppointmentsMultipleStaffIds(companyStaffIds, date, orderBy, sortOrder, limit, offset);
		} else {
			String staffId = profile.getStaffId(companyId, providerConfig.getProviderPolypointId());
			if(staffId==null || staffId.isEmpty()){
				throw new ForbiddenException(ForbiddenException.NO_STAFF_ID);
			}
			appointmentsDetails = getDetailedDoctorAppointmentsSingleStaffId(staffId, companyId, date, orderBy, sortOrder, limit, offset);
		}
					
		addCompanyReferenceAndDescriptionByLanguage(appointmentsDetails, language);
		
		return appointmentsDetails;
	}

	public List<AppointmentDetail> getAppointmentsPhysician(Integer companyId, UserProfile profile, String date, String orderBy, String sortOrder, Integer limit, Integer offset, String language){
		return getDetailedDoctorAppointments(companyId, profile, date, orderBy, sortOrder, limit, offset, language);
	}
	
	public Integer getAppointmentsPhysicianCount(Integer companyId, UserProfile userProfile, String date){
		EhProfile profile = userProfile.getProfile();
		if(companyId == null){
			Map<Integer, String> companyStaffIds = profile.getCompanyStaffIds(providerConfig.getProviderPolypointId());
			if(companyStaffIds==null || companyStaffIds.isEmpty()){
				return 0;
			}
		   return appointmentDetailRepository.findByMultipleStaffidsCount(companyStaffIds, date);
		} else {
			String staffId = profile.getStaffId(companyId, providerConfig.getProviderPolypointId());
			if(staffId==null || staffId.isEmpty()){
				return 0;
			}
			return appointmentDetailRepository.findBySingleStaffidCount(staffId, companyId, date);
		}
	}

	/**
	 * Returns a list of {@link AppointmentDetail} searching by the combination of all the parameters given with <b>value different than null</b>.<br>
	 * At least one of this is need: pid or companyId or services or units or visitNumber.<br>
	 * The data base will be query using {@link AppointmentDetailRepository}
	 * 
	 * @param companyId		Integer
	 * @param pid			Long	patient id
	 * @param visitNumber	Long
	 * @param patientUnits	{@link List} of {@link String}
	 * @param hospServices	{@link List} of {@link String}
	 * @param type			String
	 * @param rooms			{@link List} of {@link String}
	 * @param locationNames {@link List} of {@link String}
	 * @param includeActive	boolean
	 * @param from			{@link LocalDateTime}
	 * @param to 			{@link LocalDateTime}
	 * @param orderBy		String	a value of {@link DoctorAppointmentListOrderByEnum} if null default
	 * @param sortOrder 	String	if null set to default
	 * @param limit 		String	if null set to default
	 * @param offset		String 	if null set to default
	 * @return				{@link List} of {@link AppointmentDetail}
	 */
	public List<AppointmentDetail> getAppointmentDetailsByCriteriaShortedAndPaginated(Integer companyId, Long pid, Long visitNumber, List<String> patientUnits, List<String> hospServices, String type, List<String> rooms, List<String> locationNames, boolean includeActive, LocalDate from,
			LocalDate to, String orderBy, String sortOrder, Integer limit, Integer offset, String language) {

		getAppointmentDetailsByCriteriaValidateParameters(companyId, pid, visitNumber, patientUnits, hospServices);

		offset = validateOffset (offset);
		
		String finalOrderBy = getOrderBy(orderBy, sortOrder);		

		validateFromAndToDates(from, to);

		List<AppointmentDetail> appointmentsDetails = appointmentDetailRepository.findByCriteriaShortedAndPaginated(companyId, pid, visitNumber, patientUnits, hospServices,
				type, rooms, locationNames, includeActive, from, to, finalOrderBy, limit, offset);
		addCompanyReferenceAndDescriptionByLanguage(appointmentsDetails, language);

		return appointmentsDetails;
	}
	
	/**
	 * Sets the default value offset if null
	 * 
	 * @param offset Interger
	 * @return Interger
	 */
	private Integer validateOffset(Integer offset) {
		if (offset == null) {
			return defaultResultOffset;
		} else {
			return offset;
		}
	}
	
	/**
	 * Composes a valid orderBy. If the field for the order or the sort direction are null the default will be use.
	 * 
	 * @param orderBy		String
	 * @param sortOrder		String
	 * @return	String
	 */
	private String getOrderBy(String orderBy, String sortOrder) {
		DoctorAppointmentListOrderByEnum orderByEnum = DoctorAppointmentListOrderByEnum.permissiveValueOf(orderBy);
		String finalOrderBy = orderByEnum.getValue();
		
		if (finalOrderBy == null || finalOrderBy.isEmpty()) {
			finalOrderBy = defaultResultAppointmentListOrderBy;
		}
		if (sortOrder == null) {
			sortOrder = defaultResultAppointmentListSortOrder;
		}

		finalOrderBy = finalOrderBy.concat(" " + sortOrder);
		
		return finalOrderBy;
	}
	
	/**
	 * Returns the maximum number of result that the search will return, the search will be done by the combination of all the parameters given with <b>value different than null</b><br>
	 * The data base will be query but {@link AppointmentDetailRepository}
	 * 
	 * @param companyId		Integer
	 * @param pid			Long	patient id
	 * @param visitNumber	Long
	 * @param patientUnits	{@link List} of {@link String}
	 * @param hospServices	{@link List} of {@link String}
	 * @param type			String
	 * @param rooms			{@link List} of {@link String}
	 * @param locationNames {@link List} of {@link String}
	 * @param from			{@link LocalDateTime}
	 * @param to 			{@link LocalDateTime}
	 * @return 				Interger
	 */
	public Integer getAppointmentDetailsByCriteriaCount(Integer companyId, Long pid, Long visitNumber, List<String> patientUnits, List<String> hospServices, String type, List<String> rooms, List<String> locationNames, boolean includeActive, LocalDate from, LocalDate to) {
		getAppointmentDetailsByCriteriaValidateParameters(companyId, pid, visitNumber, patientUnits, hospServices);
		validateFromAndToDates(from, to);

		return appointmentDetailRepository.findByCriteriaCount(companyId, pid, visitNumber, patientUnits, hospServices, type, rooms, locationNames, includeActive, from, to);
	}

	public AppointmentDetail getAppointmentDetail(Long appointmentid, String language){
		if(appointmentid != null){
			AppointmentDetail appointmentDetail = appointmentDetailRepository.findByAppointmentId(appointmentid);
			addCompanyReferenceAndDescriptionByLanguage(appointmentDetail, language);
			return appointmentDetail;
		}
		return null;
	}

	private void addCompanyReferenceAndDescriptionByLanguage(List<AppointmentDetail> appointmentsDetails, String lang) {
		if (appointmentsDetails != null) {
			String companyCode = null;
			for (AppointmentDetail appointmentDetail : appointmentsDetails) {
				addCompanyReference(appointmentDetail);
				if (StringUtils.isEmpty(companyCode) && appointmentDetail.getCompany() != null) {
					companyCode = appointmentDetail.getCompany().getCode();
				}
			}
			addDescriptionByLanguage(appointmentsDetails, companyCode, lang);
		}
	}

	private void addDescriptionByLanguage(List<AppointmentDetail> appointmentsDetails, String companyCode, String lang) {
		LanguageMapper mapper = validateCompanyTranslation(companyCode, lang);
		if (mapper != null) {

			// get appointmentkindcode (ID for Translation)
			List<String> ids = appointmentsDetails.stream()
												  .filter(appointmentDetail -> ! StringUtils.isEmpty(appointmentDetail.getAppointkindcode()))
												  .map(AppointmentDetail::getAppointkindcode)
												  .collect(Collectors.toList());
			// if empty, no need to add translations
			if (ids.isEmpty()) {
				return;
			}

			// convert translastions to Map
			Map<String, RefdataEntity> map = refDataRepository.findByCompanyCodeAndCodesAndLang(refdataSrcFiled, refdataSrcTableFiled, companyCode, ids, mapper.getLanguageDatabase()).stream().collect(Collectors.toMap(refdataEntity -> refdataEntity.getId().getCode(), refdataEntity -> refdataEntity));
			for (AppointmentDetail ad : appointmentsDetails) {
				if (StringUtils.hasText(ad.getAppointkindcode())) {
					if (map.containsKey(ad.getAppointkindcode())) {
						overrideDescription(ad, map.get(ad.getAppointkindcode()));
					}
				}
			}
		}
	}

	private void addCompanyReferenceAndDescriptionByLanguage(AppointmentDetail appointmentDetail, String lang) {
		if (appointmentDetail != null && appointmentDetail.getCompanyId() != null) {
			addCompanyReference(appointmentDetail);
			addDescriptionByLanguage(appointmentDetail, lang);
		}
	}

	private void addDescriptionByLanguage(AppointmentDetail appointmentDetail, String lang) {
		if (appointmentDetail == null || appointmentDetail.getCompany() == null || appointmentDetail.getCompany().getCode() == null || appointmentDetail.getAppointkindcode() == null) {
			return;
		}

		LanguageMapper mapper = validateCompanyTranslation(appointmentDetail.getCompany().getCode(), lang);
		if (mapper != null) {
			overrideDescription(appointmentDetail, refDataRepository.findByIdCompanyAndIdCodeAndLang(refdataSrcFiled, refdataSrcTableFiled, appointmentDetail.getCompany().getCode(), appointmentDetail.getAppointkindcode(), mapper.getLanguageDatabase()));
		}
	}

	private LanguageMapper validateCompanyTranslation(String companyCode, String lang) {
		return ! StringUtils.isEmpty(companyCode) && (StringUtils.isEmpty(lang) || enableTranslationByCompany.contains(companyCode)) ? LanguageMapper.getLanguageMapper(lang) : null;
	}

	private void  overrideDescription(AppointmentDetail appointmentDetail, RefdataEntity refdata) {
		if(refdata != null && refdata.getId() != null) {
			appointmentDetail.setDescription(refdata.getCodedesc());
		}
	}

	private void addCompanyReference(AppointmentDetail appointmentDetail) {
		if (appointmentDetail == null || appointmentDetail.getCompany() != null) {
			return;
		}

		if (appointmentDetail.getCompanyId() != null) {
			
			CompanyDetails companyDetails;
			CompanyReference companyReference = null;
			
			companyDetails = companyDetailsRepository.findOne(appointmentDetail.getCompanyId());
			
			if(companyDetails != null && companyDetails.getId()!=null) {			
				companyReference = new CompanyReference(companyDetails.getId(), companyDetails.getName(), companyDetails.getCode());
			}
			
			appointmentDetail.setCompany(companyReference);
		}
	}
	
	/**
	 * If fromDate and toDate are not null the method ensures that toDate is equal or greater than fromDate
	 * 
	 * @param fromDate	{@link LocalDateTime}
	 * @param toDate	{@link LocalDateTime}
	 */
	private void validateFromAndToDates(LocalDate fromDate, LocalDate toDate) {
		if (fromDate != null && toDate != null && fromDate.compareTo(toDate) > 0) {
			throw new IllegalArgumentException("The TO date must be greater than the FROM date");
		}
	}
	
	/**
	 * Validate the parameters and throw exception
	 * 
	 * @param companyId		Integer
	 * @param pid			Long Patient ID
	 * @param visitNumber	Long
	 * @param patientUnits	{@link List} of {@link String}
	 * @param hospServices	{@link List} of {@link String}
	 */
	private void getAppointmentDetailsByCriteriaValidateParameters(Integer companyId, Long pid, Long visitNumber, List<String> patientUnits, List<String> hospServices) {
		if ((patientUnits == null || patientUnits.isEmpty()) && (hospServices == null || hospServices.isEmpty()) && pid == null && visitNumber == null && companyId == null) {
			throw new IllegalArgumentException("Invalid parameters. Please provide pid or companyId or services or units or visitNumber.");
		}
	}	
}