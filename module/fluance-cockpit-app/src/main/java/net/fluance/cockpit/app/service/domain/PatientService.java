/**
 * 
 */
package net.fluance.cockpit.app.service.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import net.fluance.app.web.util.exceptions.ForbiddenException;
import net.fluance.cockpit.core.model.PatientVisitReference;
import net.fluance.cockpit.core.model.VisitReference;
import net.fluance.cockpit.core.model.jdbc.patient.PatientContact;
import net.fluance.cockpit.core.model.jdbc.patient.PatientViewEnum;
import net.fluance.cockpit.core.model.jdbc.patient.byroom.RoomWithPatientsDetails;
import net.fluance.cockpit.core.model.jdbc.patient.byroom.RoomWithPatientsReferences;
import net.fluance.cockpit.core.model.jdbc.patient.byroom.RoomWithPatientsReferencesCount;
import net.fluance.cockpit.core.model.jdbc.visit.PatientClassEnum;
import net.fluance.cockpit.core.model.wrap.patient.AdmissionStatusEnum;
import net.fluance.cockpit.core.model.wrap.patient.Patient;
import net.fluance.cockpit.core.model.wrap.patient.PatientAdmitDate;
import net.fluance.cockpit.core.model.wrap.patient.PatientInList;
import net.fluance.cockpit.core.model.wrap.patient.PatientSexEnum;
import net.fluance.cockpit.core.model.wrap.patient.PatientsCount;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientContactRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientInListRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientsCountByAdminDateRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientsCountRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.RoomWithPatientsDetailsRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.RoomWithPatientsReferencesCountRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.RoomWithPatientsReferencesRepository;
import net.fluance.cockpit.core.util.sql.PatientListOrderByEnum;

@Service
public class PatientService {

	private static final String MINIMUM_ONE_SERVICE = "At least patientunit or hospservice expected";
	private static final String PATIENT_SPLIT_SEPERATOR = ",";

	@Autowired
	private RoomWithPatientsReferencesRepository roomWithPatientsReferencesRepository;
	@Autowired
	private RoomWithPatientsDetailsRepository roomWithPatientsDetailsRepository;
	@Autowired
	private RoomWithPatientsReferencesCountRepository roomWithPatientsReferencesCountRepository;	
	@Autowired
	private PatientRepository patientRepository;
	@Autowired
	private PatientInListRepository patientInListRepository;
	@Autowired
	private PatientsCountRepository patientsCountRepository;
	@Autowired
	private PatientsCountByAdminDateRepository patientsCountByAdminDateRepository;
	@Autowired
	private PatientContactRepository patientContactRepository;
	
	@Autowired
	PatientContactService patientContactService;

	/**
	 * Get all the patient that match the given values
	 * 
	 * @param byphysician
	 * @param staffid
	 * @param staffIds
	 * @param companyIds
	 * @param patientview
	 * @param pid
	 * @param companyid
	 * @param visitnb
	 * @param lastname
	 * @param firstname
	 * @param sex must be a valid value of {link PatientSexEnum}
	 * @param admitdate
	 * @param birthdate
	 * @param patientunit
	 * @param hospservice
	 * @param patientroom
	 * @param patientbed
	 * @param limit
	 * @param offset
	 * @param orderby
	 * @param sortorder
	 * @param admissionstatus sex must be a valid value of {link AdmissionStatusEnum}
	 * @param patientclass
	 * @param telephone
	 * @param email
	 * @return {@link List} of {@link PatientInList}
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public List<?> patientsByCriteria(boolean byphysician, String staffid, List<String> staffIds, List<Integer> companyIds, String patientview, Long pid, Integer companyid, Integer visitnb, String lastname,
			String firstname, String sex, String admitdate, String birthdate, String patientunit,
			String hospservice, String patientroom, String patientbed, Integer limit, Integer offset, String orderby, String sortorder,
			String admissionstatus, String patientclass, String telephone, String email) throws JsonProcessingException, IOException {
		
		if(telephone != null) {
			//this ugly stuff, is because we have so much legacy code, and we remove the full patient => microservice will come soon
			// we must add the patient to a general list
			return patientContactService.getPatientByTelephone(telephone);
		}
		
		
		PatientClassEnum patientClassEnum = PatientClassEnum.withPatientShortClassCode(patientclass);
		patientclass = (patientClassEnum  == null) ? null : patientClassEnum.getPatientClassShortCode();

		PatientListOrderByEnum orderByEnum=PatientListOrderByEnum.permissiveValueOf(orderby);
		orderby=orderByEnum.getValue();

		if(byphysician==true){
			if(patientview!=null && !PatientViewEnum.WEEK.getKey().equals(patientview)){
				throw new IllegalArgumentException("Invalid parameters");
			}
			if(staffid != null && companyid != null){
				return patientsByAttendingPhysician(companyid, Integer.valueOf(staffid), patientview, orderby, sortorder, limit, offset);
			}else if (staffIds != null && !staffIds.isEmpty()){
				List<Integer> staffIdsInt = new ArrayList<>();
				for (int i = 0; i < staffIds.size(); i++) {
					staffIdsInt.add(Integer.valueOf(staffIds.get(i)));
				}
				return patientsByAttendingPhysicianMultipleStaffId(staffIdsInt, companyIds, patientview, orderby, sortorder, limit, offset);
			}
		}

		AdmissionStatusEnum admissionStatusEnum = validaAdmissionStatusValue(admissionstatus);
		
		PatientSexEnum patientSexEnum = validaPatientSexValue(sex);

		Map<String, Object> params = initParams(pid, companyid, visitnb, lastname, firstname, admitdate, birthdate, patientunit, hospservice, patientroom, patientbed, limit, offset, patientclass, email);

		if(isMyUnitRequest(params)){
			return patientsByMyUnit(companyid, patientunit, limit, offset, orderby, sortorder);
		}

		return patientsByCriteria(params, patientSexEnum, admissionStatusEnum, orderby, sortorder);
	}
	
	/**
	 * The list of {@link PatientsCount} that will be returned is grouped by the first lastname letter
	 * 
	 * @param patientview
	 * @param pid
	 * @param companyid
	 * @param visitnb
	 * @param lastname
	 * @param firstname
	 * @param sex must be a valid value of {link PatientSexEnum}
	 * @param admitdate
	 * @param birthdate
	 * @param patientunit
	 * @param hospservice
	 * @param patientroom
	 * @param patientbed
	 * @param admissionstatus sex must be a valid value of {link AdmissionStatusEnum}
	 * @param patientclass
	 * @param email
	 * @return {@link List} of {@link PatientsCount}
	 */
	public List<PatientsCount> patientsCount(String patientview, Long pid, Integer companyid, Integer visitnb, String lastname,
			String firstname, String sex, String admitdate, String birthdate, String patientunit,
			String hospservice, String patientroom, String patientbed, String admissionstatus, String patientclass, String email) {
		
		PatientClassEnum patientClassEnum = PatientClassEnum.withPatientShortClassCode(patientclass);
		patientclass = (patientClassEnum  == null) ? null : patientClassEnum.getPatientClassShortCode();
		
		AdmissionStatusEnum admissionStatusEnum = validaAdmissionStatusValue(admissionstatus);
		
		PatientSexEnum patientSexEnum = validaPatientSexValue(sex);

		Map<String, Object> params = initParams(pid, companyid, visitnb, lastname, firstname, admitdate, birthdate, patientunit, hospservice, patientroom, patientbed, null, null, patientclass, email);

		return patientsCountByCriteria(params, patientSexEnum, admissionStatusEnum);
	}
	
	/**
	 * Get the value from {@link PatientSexEnum}
	 * 
	 * @param sex
	 * @return {@link PatientSexEnum}
	 */
	private PatientSexEnum validaPatientSexValue(String sex) {
		try {
			return PatientSexEnum.permissiveValueOf(sex);
		}catch(IllegalArgumentException illegalArgumentException) {
			throw new IllegalArgumentException("Invalid value for sex");
		}
	}
	
	/**
	 * Get the value from {@link AdmissionStatusEnum}
	 * 
	 * @param admissionStatus
	 * @return {@link AdmissionStatusEnum}
	 */
	private AdmissionStatusEnum validaAdmissionStatusValue(String admissionStatus) {
		try {
			return AdmissionStatusEnum.permissiveValueOf(admissionStatus);
		}catch(IllegalArgumentException illegalArgumentException) {
			throw new IllegalArgumentException("Invalid value for admission status");
		}
	}
	
	/**
	 * Create a List with the Visit Admit Dates and the number of patients of each date, based in the params
	 * @param companyid	Company Code of the Visits
	 * @param patientunit	Patient Unit Code of the Visits
	 * @param hospservice	Hospital Service of the Visits
	 * @param patientclass	Patient Class of the Visit
	 * @return	A {@link List} of {@link PatientAdminDate} 
	 */
	public List<PatientAdmitDate> patientsCountByAdmitDate(Integer companyid, String patientunit, String hospservice, String patientclass) {
		if((patientunit == null || patientunit.isEmpty()) && (hospservice == null || hospservice.isEmpty())) {
			throw new IllegalArgumentException("PatientUnit or HospService params needs a value");
		}
		if((patientunit != null && !patientunit.isEmpty()) && (hospservice != null && !hospservice.isEmpty())) {
			throw new IllegalArgumentException("PatientUnit and HospService params cannot be with value in the same request");
		}
		PatientClassEnum patientClassEnum = PatientClassEnum.withPatientShortClassCode(patientclass);
		patientclass = (patientClassEnum  == null) ? null : patientClassEnum.getPatientClassShortCode();
		Map<String, Object> params = initParams(null, companyid, null, null, null, null, null, patientunit, hospservice, null, null, null, null, patientclass, null);
		return patientsCountByAdminDateCriteria(params);
	}
	
	/**
	 * Test if the request is candidate to be a My Unit request<br>
	 * Valid combinations
	 * <ul>
	 * 	<li>Parameters: "company_id" && "patientunit"</li>
	 * 	<li>Parameters: "company_id" && "patientunit" && ("limit" || "offset")</li>
	 * 	<li>Parameters: "company_id" && "patientunit" && "limit" && "offset"</li>
	 * </ul>
	 * 
	 * @param params
	 * @return
	 */
	private boolean isMyUnitRequest(Map<String, Object> params){
		int tableSize=params.size();
		boolean isMyUnitRequest=false;
		if(tableSize>4 || tableSize<2){
			return false;
		}
		switch(tableSize){
		case 2: 
			isMyUnitRequest=params.containsKey("company_id") && params.containsKey("patientunit");
			break;
		case 3: 
			isMyUnitRequest=params.containsKey("company_id") && params.containsKey("patientunit") && (params.containsKey("limit") || params.containsKey("offset"));
			break;
		case 4: 
			isMyUnitRequest=params.containsKey("company_id") && params.containsKey("patientunit") && params.containsKey("limit") && params.containsKey("offset");
			break;
		}

		return isMyUnitRequest;
	}

	/**
	 * 
	 * @param params
	 * @return List<PatientInList> List of patients
	 */
	private List<PatientInList> patientsByAttendingPhysician(Integer companyid, Integer staffid, String patientview, String orderby, String sortorder, Integer limit, Integer offset) {
		List<PatientInList> patients = new ArrayList<>();
		List<net.fluance.cockpit.core.model.jdbc.patient.PatientInList> rawPatients = new ArrayList<>();
		if (patientview==null){
			rawPatients = patientInListRepository.findPatientsByAttendingphysician(companyid, staffid, orderby, limit, offset);
		}
		else if(patientview.equals(PatientViewEnum.WEEK.getKey())){
			rawPatients = patientInListRepository.findPatientsByAttendingphysicianWeek(companyid, staffid, orderby, limit, offset);
		}
		
		if(rawPatients != null && !rawPatients.isEmpty()) {
			for (net.fluance.cockpit.core.model.jdbc.patient.PatientInList rawPatient : rawPatients) {
				PatientInList patient = new PatientInList(rawPatient.getNbRecords(), rawPatient.getId(),
						rawPatient.getFirstName(), rawPatient.getLastName(), rawPatient.getMaidenName(),
						rawPatient.getBirthDate(), rawPatient.getSex(), rawPatient.isDeath(), rawPatient.getDeathdt(), rawPatient.getAddress(), rawPatient.getLocality(),
						rawPatient.getPostCode(), rawPatient.getLastVisitnumber(), rawPatient.getLastVisitCompanyId(),
						rawPatient.getLastVisitAdmitDate(), rawPatient.getLastVisitDischargeDate(),
						rawPatient.getLastVisitPatientClass(), rawPatient.getLastVisitPatientClassDesc(),
						rawPatient.getLastVisitPatientType(), rawPatient.getLastVisitPatientTypeDesc(),
						rawPatient.getLastVisitPatientCase(), rawPatient.getLastVisitPatientCaseDesc(),
						rawPatient.getLastVisitHospService(), rawPatient.getLastVisitHospServiceDesc(),
						rawPatient.getLastVisitAdmissionType(), rawPatient.getLastVisitAdmissionTypeDesc(),
						rawPatient.getLastVisitPatientUnit(), rawPatient.getLastVisitPatientUnitDesc(),
						rawPatient.getLastVisitPatientRoom(), rawPatient.getLastVisitPatientBed(),
						rawPatient.getLastVisitFinancialClass(), rawPatient.getLastVisitFinancialClassDesc());
				patients.add(patient);
			}
		}
		return patients;
	}

	private List<PatientInList> patientsByAttendingPhysicianMultipleStaffId(List<Integer> staffids, List<Integer> companyids, String patientview, String orderby, String sortorder, Integer limit, Integer offset) {
		List<PatientInList> patients = new ArrayList<>();
		List<net.fluance.cockpit.core.model.jdbc.patient.PatientInList> rawPatients = new ArrayList<>();
		if (patientview==null){
			rawPatients = patientInListRepository.findPatientsByAttendingphysicianMultipleStaffId(staffids, companyids, orderby, limit, offset);
		}
		else if(patientview.equals(PatientViewEnum.WEEK.getKey())){
			rawPatients = patientInListRepository.findPatientsByAttendingphysicianWeekMultipleStaffId(staffids, companyids, orderby, limit, offset);
		}

		for (net.fluance.cockpit.core.model.jdbc.patient.PatientInList rawPatient : rawPatients) {
			PatientInList patient = new PatientInList(rawPatient.getNbRecords(), rawPatient.getId(),
					rawPatient.getFirstName(), rawPatient.getLastName(), rawPatient.getMaidenName(),
					rawPatient.getBirthDate(), rawPatient.getSex(), rawPatient.isDeath(), rawPatient.getDeathdt(),rawPatient.getAddress(), rawPatient.getLocality(),
					rawPatient.getPostCode(), rawPatient.getLastVisitnumber(), rawPatient.getLastVisitCompanyId(),
					rawPatient.getLastVisitAdmitDate(), rawPatient.getLastVisitDischargeDate(),
					rawPatient.getLastVisitPatientClass(), rawPatient.getLastVisitPatientClassDesc(),
					rawPatient.getLastVisitPatientType(), rawPatient.getLastVisitPatientTypeDesc(),
					rawPatient.getLastVisitPatientCase(), rawPatient.getLastVisitPatientCaseDesc(),
					rawPatient.getLastVisitHospService(), rawPatient.getLastVisitHospServiceDesc(),
					rawPatient.getLastVisitAdmissionType(), rawPatient.getLastVisitAdmissionTypeDesc(),
					rawPatient.getLastVisitPatientUnit(), rawPatient.getLastVisitPatientUnitDesc(),
					rawPatient.getLastVisitPatientRoom(), rawPatient.getLastVisitPatientBed(),
					rawPatient.getLastVisitFinancialClass(), rawPatient.getLastVisitFinancialClassDesc());
			patients.add(patient);
		}
		return patients;

	}

	private List<PatientInList> patientsByCriteria(Map<String, Object> params, PatientSexEnum patientSex, AdmissionStatusEnum admissionStatus, String orderby, String sortorder) {
		List<PatientInList> patients = new ArrayList<>();
		List<net.fluance.cockpit.core.model.jdbc.patient.PatientInList> rawPatients = patientInListRepository
				.findByCriteria(params, patientSex, admissionStatus, orderby, sortorder);
		for (net.fluance.cockpit.core.model.jdbc.patient.PatientInList rawPatient : rawPatients) {
			PatientInList patient = new PatientInList(rawPatient.getNbRecords(), rawPatient.getId(),
					rawPatient.getFirstName(), rawPatient.getLastName(), rawPatient.getMaidenName(),
					rawPatient.getBirthDate(), rawPatient.getSex(), rawPatient.isDeath(), rawPatient.getDeathdt(), rawPatient.getAddress(), rawPatient.getLocality(),
					rawPatient.getPostCode(), rawPatient.getLastVisitnumber(), rawPatient.getLastVisitCompanyId(),
					rawPatient.getLastVisitAdmitDate(), rawPatient.getLastVisitDischargeDate(),
					rawPatient.getLastVisitPatientClass(), rawPatient.getLastVisitPatientClassDesc(),
					rawPatient.getLastVisitPatientType(), rawPatient.getLastVisitPatientTypeDesc(),
					rawPatient.getLastVisitPatientCase(), rawPatient.getLastVisitPatientCaseDesc(),
					rawPatient.getLastVisitHospService(), rawPatient.getLastVisitHospServiceDesc(),
					rawPatient.getLastVisitAdmissionType(), rawPatient.getLastVisitAdmissionTypeDesc(),
					rawPatient.getLastVisitPatientUnit(), rawPatient.getLastVisitPatientUnitDesc(),
					rawPatient.getLastVisitPatientRoom(), rawPatient.getLastVisitPatientBed(),
					rawPatient.getLastVisitFinancialClass(), rawPatient.getLastVisitFinancialClassDesc());
			patients.add(patient);
		}
		return patients;
	}
	
	private List<PatientsCount> patientsCountByCriteria(Map<String, Object> params, PatientSexEnum patientSex, AdmissionStatusEnum admissionStatus) {
		List<PatientsCount> patientsCount = new ArrayList<>();
		List<net.fluance.cockpit.core.model.jdbc.patient.PatientsCount> rawPatients = patientsCountRepository
				.patientsCountByCriteria(params, patientSex, admissionStatus);
		for (net.fluance.cockpit.core.model.jdbc.patient.PatientsCount rawPatient : rawPatients) {
			PatientsCount patientCount = new PatientsCount(rawPatient.getNb_records(), rawPatient.getSubstr());
			patientsCount.add(patientCount);
		}
		return patientsCount;
	}
	
	/**
	 * Create a List with the Visit Admit Dates and the number of patients of each date, based in the params without duplicated values
	 * @param params
	 * @return	A {@link List} of {@link PatientAdminDate}
	 */
	private List<PatientAdmitDate> patientsCountByAdminDateCriteria(Map<String, Object> params) {
		List<PatientAdmitDate> patientsByAdminDate = new ArrayList<>();
		List<net.fluance.cockpit.core.model.jdbc.patient.PatientAdmitDate> rawPatientsByAdminDate = patientsCountByAdminDateRepository
				.patientsCountByAdminDateCriteria(params);
		for (net.fluance.cockpit.core.model.jdbc.patient.PatientAdmitDate rawPatient : rawPatientsByAdminDate) {
			PatientAdmitDate patientByAdmitDate = new PatientAdmitDate(rawPatient.getCount(), rawPatient.getAdmitdt());
			if(!patientsByAdminDate.contains(patientByAdmitDate)){
				patientsByAdminDate.add(patientByAdmitDate);
			}
		}
		return patientsByAdminDate;
	}

	/**
	 * 
	 * @param sortorder 
	 * @param orderby 
	 * @param params
	 * @return List<PatientInList> list of patients
	 */
	private List<PatientInList> patientsByMyUnit(int companyid, String patientunit, Integer limit, Integer offset, String orderby, String sortorder) {
		List<PatientInList> patients = new ArrayList<>();
		List<net.fluance.cockpit.core.model.jdbc.patient.PatientInList> rawPatients = patientInListRepository
				.findByMyUnit(companyid, patientunit, limit, offset, orderby, sortorder);
		for (net.fluance.cockpit.core.model.jdbc.patient.PatientInList rawPatient : rawPatients) {
			PatientInList patient = new PatientInList(rawPatient.getNbRecords(), rawPatient.getId(),
					rawPatient.getFirstName(), rawPatient.getLastName(), rawPatient.getMaidenName(),
					rawPatient.getBirthDate(), rawPatient.getSex(), rawPatient.isDeath(), rawPatient.getDeathdt(), rawPatient.getAddress(), rawPatient.getLocality(),
					rawPatient.getPostCode(), rawPatient.getLastVisitnumber(), rawPatient.getLastVisitCompanyId(),
					rawPatient.getLastVisitAdmitDate(), rawPatient.getLastVisitDischargeDate(),
					rawPatient.getLastVisitPatientClass(), rawPatient.getLastVisitPatientClassDesc(),
					rawPatient.getLastVisitPatientType(), rawPatient.getLastVisitPatientTypeDesc(),
					rawPatient.getLastVisitPatientCase(), rawPatient.getLastVisitPatientCaseDesc(),
					rawPatient.getLastVisitHospService(), rawPatient.getLastVisitHospServiceDesc(),
					rawPatient.getLastVisitAdmissionType(), rawPatient.getLastVisitAdmissionTypeDesc(),
					rawPatient.getLastVisitPatientUnit(), rawPatient.getLastVisitPatientUnitDesc(),
					rawPatient.getLastVisitPatientRoom(), rawPatient.getLastVisitPatientBed(),
					rawPatient.getLastVisitFinancialClass(), rawPatient.getLastVisitFinancialClassDesc());
			patients.add(patient);
		}
		return patients;
	}

	/**
	 * Given a Patient ID, returns the {@link Patient} info
	 * @param pid
	 * @return <ul><li>The Patient details if the ID exists</li><li>A null value if the Patient is not found</li></ul>
	 */
	public Patient patientDetail(long pid) {
		net.fluance.cockpit.core.model.jdbc.patient.Patient rawPatient = patientRepository.findOne(pid);
		if(rawPatient!=null){
			Patient patient = new Patient(rawPatient.getId(), rawPatient.getLanguage(), rawPatient.getCourtesy(), rawPatient.getFirstName(),
					rawPatient.getLastName(), rawPatient.getMaidenName(), rawPatient.getBirthDate(),
					rawPatient.getAvsNumber(), rawPatient.getNationality(), rawPatient.getSex(), rawPatient.isDeath(), rawPatient.getDeathdt(), rawPatient.getAddress(),
					rawPatient.getAddress2(), rawPatient.getLocality(), rawPatient.getPostCode(),
					rawPatient.getSubPostCode(), rawPatient.getAdressComplement(), rawPatient.getCareOf(),
					rawPatient.getCanton(), rawPatient.getCountry(), rawPatient.getMaritalStatus());

			List<PatientContact> patientContacts = patientContactRepository.findByPid(pid);
			patient.setContacts(patientContacts);
			return patient;
		}
		return null;
	}
	
	/**
	 * Lists the rooms with every patients reference until @param maxPatientsByRoom 
	 * 
	 * @param companyId
	 * @param hospService
	 * @param patientClass
	 * @param unit
	 * @param limit
	 * @param offset
	 * @param maxPatientsByRoom
	 * @return
	 */
	public List<RoomWithPatientsReferences> roomsWithPatientsReferences(Integer companyId, String hospService, String patientClass, String unit, String sortOrder, Integer limit, Integer offset, Integer maxPatientsByRoom) {
		if(unit == null && hospService == null){
			throw new IllegalArgumentException(MINIMUM_ONE_SERVICE);
		}
		List<RoomWithPatientsReferences> roomsWithPatientsReferences = roomWithPatientsReferencesRepository.getRoomWithPatientsReferences(companyId, hospService, unit, patientClass, sortOrder, limit, offset, maxPatientsByRoom);
		return roomsWithPatientsReferences;
	}
	
	/**
	 * Lists the rooms with every patients details until @param maxPatientsByRoom 
	 * 
	 * @param companyId
	 * @param hospService
	 * @param patientClass
	 * @param unit
	 * @param limit
	 * @param offset
	 * @param maxPatientsByRoom
	 * @return
	 */
	public List<RoomWithPatientsDetails> roomsWithPatientsDetails(Integer companyId, String hospService, String patientClass, String unit, String sortOrder, Integer limit, Integer offset, Integer maxPatientsByRoom) {
		if(unit == null && hospService == null){
			throw new IllegalArgumentException(MINIMUM_ONE_SERVICE);
		}
		List<RoomWithPatientsDetails> roomsWithPatientsReferences = roomWithPatientsDetailsRepository.getRoomWithPatientsDetails(companyId, hospService, unit, patientClass, sortOrder, limit, offset, maxPatientsByRoom);
		return roomsWithPatientsReferences;
	}
	
	/**
	 * Lists the rooms and returns the number of patients for every room
	 * 
	 * @param companyId
	 * @param hospService
	 * @param patientClass
	 * @param unit
	 * @return
	 */
	public List<RoomWithPatientsReferencesCount> roomsWithPatientsReferencesCount(Integer companyId, String hospService, String patientClass, String unit, String sortOrder) {
		if(unit == null && hospService == null){
			throw new IllegalArgumentException(MINIMUM_ONE_SERVICE);
		}
		
		List<RoomWithPatientsReferencesCount> roomsWithPatientsReferences = roomWithPatientsReferencesCountRepository.getRoomWithPatientsReferencesCount(companyId, hospService, unit, patientClass, sortOrder);
		return roomsWithPatientsReferences;
	}
	
	private Map<String, Object> initParams(Long pid, Integer companyid, Integer visitnb, String lastname, String firstname, String admitdate, String birthdate, String patientunit, String hospservice, String patientroom, String patientbed,
			Integer limit, Integer offset, String patientclass, String email) {
		Map<String, Object> params = new HashMap<>();
		if (companyid != null) {
			params.put("company_id", companyid);
		}
		if (visitnb != null) {
			params.put("nb", visitnb);
		}
		if (pid != null) {
			params.put("patient_id", pid);
		}
		if (lastname != null) {
			params.put("lastname", lastname);
		}
		if (firstname != null) {
			params.put("firstname", firstname);
		}
		if (admitdate != null) {
			params.put("admitdt", admitdate);
		}
		if (birthdate != null) {
			params.put("birthdate", birthdate);
		}
		if (patientunit != null) {
			params.put("patientunit", patientunit);
		}
		if (hospservice != null) {
			params.put("hospservice", hospservice);
		}
		if (patientroom != null) {
			params.put("patientroom", patientroom);
		}
		if (patientbed != null) {
			params.put("patientbed", patientbed);
		}
		if (patientclass != null) {
			params.put("hl7code", patientclass);
		}
		if (email != null) {
			params.put("email", email);
		}
		if (limit != null) {
			params.put("limit", limit);
		}
		if (offset != null) {
			params.put("offset", offset);
		}
		
		return params;
	}

	public List<PatientVisitReference> patientsForDoctor(String staffid, List<String> staffIds, List<Integer> companyIds, Integer companyid, String patientclass, Integer limit, Integer offset) {
		if(staffid != null && companyid != null){
			return findAnyPatientsForPhysician(companyid, Integer.valueOf(staffid), patientclass, limit, offset);
		} else if (staffIds != null && !staffIds.isEmpty()){
			List<Integer> staffIdsInt = new ArrayList<>();
			for (int i = 0; i < staffIds.size(); i++) {
				staffIdsInt.add(Integer.valueOf(staffIds.get(i)));
			}
			return findAnyPatientsForPhysicianMultipleStaffId(staffIdsInt, companyIds, patientclass, limit, offset);
		} else {
			throw new ForbiddenException(ForbiddenException.NO_STAFF_ID);
		}
	}
	
	/**
	 * 
	 * @param params
	 * @return List<PatientInList> List of patients
	 */
	private List<PatientVisitReference> findAnyPatientsForPhysician(Integer companyid, Integer staffid, String patientClass, Integer limit, Integer offset) {
		List<PatientVisitReference> patients = new ArrayList<>();
		PatientClassEnum patientClassEnum = PatientClassEnum.withPatientShortClassCode(patientClass);
		String cleanPatientClassCode = null;
		if(patientClassEnum != null){
			cleanPatientClassCode = patientClassEnum.getPatientClassShortCode();
		}
		List<net.fluance.cockpit.core.model.jdbc.patient.PatientInList> rawPatients = new ArrayList<>();
		rawPatients = patientInListRepository.findPatientsAnyForPhysician(companyid, staffid, cleanPatientClassCode, limit, offset);
		if(rawPatients != null && !rawPatients.isEmpty()) {
			for (net.fluance.cockpit.core.model.jdbc.patient.PatientInList rawPatient : rawPatients) {
				PatientVisitReference patient = new PatientVisitReference(rawPatient.getId(), rawPatient.getFirstName(), rawPatient.getLastName(), rawPatient.getBirthDate(), rawPatient.getSex());
				VisitReference visit = new VisitReference(rawPatient.getLastVisitnumber(), rawPatient.getLastVisitPatientUnit(), rawPatient.getLastVisitHospService(), rawPatient.getLastVisitPatientRoom());
				patient.setVisit(visit);
				patients.add(patient);
			}
		}
		return patients;
	}
	
	private List<PatientVisitReference> findAnyPatientsForPhysicianMultipleStaffId(List<Integer> staffids, List<Integer> companyids, String patientClass, Integer limit, Integer offset) {
		List<PatientVisitReference> patients = new ArrayList<>();
		List<net.fluance.cockpit.core.model.jdbc.patient.PatientInList> rawPatients = new ArrayList<>();
		PatientClassEnum patientClassEnum = PatientClassEnum.withPatientShortClassCode(patientClass);
		String cleanPatientClassCode = null;
		if(patientClassEnum != null){
			cleanPatientClassCode = patientClassEnum.getPatientClassShortCode();
		}
		rawPatients = patientInListRepository.findPatientsForAnyPhysicianMultipleStaffId(staffids, companyids, cleanPatientClassCode, limit, offset);

		if(rawPatients != null && !rawPatients.isEmpty()) {
			for (net.fluance.cockpit.core.model.jdbc.patient.PatientInList rawPatient : rawPatients) {
				PatientVisitReference patient = new PatientVisitReference(rawPatient.getId(), rawPatient.getFirstName(), rawPatient.getLastName(), rawPatient.getBirthDate(), rawPatient.getSex());
				VisitReference visit = new VisitReference(rawPatient.getLastVisitnumber(), rawPatient.getLastVisitPatientUnit(), rawPatient.getLastVisitHospService(), rawPatient.getLastVisitPatientRoom());
				patient.setVisit(visit);
				patients.add(patient);
			}
		}
		
		return patients;
	}

	public List<Patient> getPatients(String pids) {
		List<Patient> patients = new ArrayList<>();
		List<Long> patientList = getPatientIds(pids);
		for(Long pid: patientList) {
			patients.add(this.patientDetail(pid));
		}
		return patients;
	}
	
	protected List<Long> getPatientIds(String patients) {
		if (patients == null || patients.isEmpty()) {
			return new ArrayList<>();
		}
		List<Long> patientIds = new ArrayList<>();
		for (String visit : patients.split(PATIENT_SPLIT_SEPERATOR)) {
			try {
				Long patientId = Long.parseLong(visit);
				if (!patientIds.contains(patientId)) {
					patientIds.add(patientId);
				}
			} catch (NumberFormatException e) {
				java.util.logging.Logger.getLogger(this.getClass().getName()).warning("Input is not a number [" + visit + "]");
			}
		}
		return patientIds;
	}
}
