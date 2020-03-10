/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.patient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;

import net.fluance.app.data.criteria.sql.PostgresqlCriteria;
import net.fluance.app.data.criteria.sql.SQL99Criteria;
import net.fluance.app.data.criteria.sql.SQLCriteriaClause;
import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.patient.PatientInList;
import net.fluance.cockpit.core.model.wrap.patient.AdmissionStatusEnum;
import net.fluance.cockpit.core.model.wrap.patient.PatientSexEnum;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class PatientInListRepository extends JdbcRepository<PatientInList, Long> {

	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultPatientListOrderBy}")
	private String defaultResultPatientListOrderBy;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultPhysicianPatientListOrderBy}")
	private String defaultResultPhysicianPatientListOrderBy;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultPatientListSortOrder}")
	private String defaultResultPatientListSortOrder;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultPatientListByUnitOrderBy}")
	private String defaultResultPatientListByUnitOrderBy;

	public PatientInListRepository() {
		super(ROW_MAPPER, ROW_UNMAPPER, MappingsConfig.TABLE_NAMES.get("PatientList"));
	}

	public static final RowMapper<PatientInList> ROW_MAPPER = new RowMapper<PatientInList>() {
		public PatientInList mapRow(ResultSet rs, int rowNum) throws SQLException {
			Integer nbRecords = SqlUtils.getInt(true, rs, "nb_records");
			Long pid = SqlUtils.getLong(true, rs, "patient_id");
			String firstname = rs.getString("firstname");
			String lastname = rs.getString("lastname");
			String maidenName = rs.getString("maidenname");
			Date birthDate = rs.getDate("birthdate");
			String sex = rs.getString("sex");
			Boolean death = rs.getBoolean("death");
			Date deathdt = rs.getDate("deathdt");
			String address = rs.getString("address");
			String locality = rs.getString("locality");
			String postCode = rs.getString("postcode");
			Long visitNumber = SqlUtils.getLong(true, rs, "nb");
			Long companyId = SqlUtils.getLong(true, rs, "company_id");
			Timestamp admitDate = rs.getTimestamp("admitdt");
			Timestamp dischargeDate = rs.getTimestamp("dischargedt");
			String patientClass = rs.getString("patientclass");
			String patientType = rs.getString("patienttype");
			String patientCase = rs.getString("patientcase");
			String hospService = rs.getString("hospservice");
			String admissionType = rs.getString("admissiontype");
			String financialClass = rs.getString("financialclass");
			String patientUnit = rs.getString("patientunit");
			String patientRoom = rs.getString("patientroom");
			String patientBed = rs.getString("patientbed");
			String patientClassDesc = rs.getString("patientclassdesc");
			String patientTypeDesc = rs.getString("patienttypedesc");
			String patientCaseDesc = rs.getString("patientcasedesc");
			String hospServiceDesc = rs.getString("hospservicedesc");
			String admissionTypeDesc = rs.getString("admissiontypedesc");
			String financialClassDesc = rs.getString("financialclassdesc");
			String patientUnitDesc = rs.getString("patientunitdesc");

			PatientInList patientInList = new PatientInList(nbRecords, pid, firstname, lastname, maidenName, birthDate, sex, death, deathdt, address, locality, postCode, visitNumber, companyId, admitDate, dischargeDate, patientClass, patientClassDesc, patientType, patientTypeDesc,
					patientCase, patientCaseDesc, hospService, hospServiceDesc, admissionType, admissionTypeDesc,
					patientUnit, patientUnitDesc, patientRoom, patientBed, financialClass, financialClassDesc);

			return patientInList;
		}
	};

	private static final RowUnmapper<PatientInList> ROW_UNMAPPER = new RowUnmapper<PatientInList>() {
		public Map<String, Object> mapColumns(PatientInList patient) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("nb_records", patient.getNbRecords());
			mapping.put("patient_id", patient.getPid());
			mapping.put("firstname", patient.getFirstName());
			mapping.put("lastname", patient.getLastName());
			mapping.put("maidenname", patient.getMaidenName());
			mapping.put("birthdate", patient.getBirthDate());
			mapping.put("sex", patient.getSex());
			mapping.put("address", patient.getAddress());
			mapping.put("locality", patient.getLocality());
			mapping.put("postcode", patient.getPostCode());
			mapping.put("nb", patient.getLastVisitnumber());
			mapping.put("company_id", patient.getLastVisitCompanyId());
			mapping.put("admitdt", patient.getLastVisitAdmitDate());
			mapping.put("dischargedt", patient.getLastVisitDischargeDate());
			mapping.put("patientclass", patient.getLastVisitPatientClass());
			mapping.put("patienttype", patient.getLastVisitPatientType());
			mapping.put("patientcase", patient.getLastVisitPatientCase());
			mapping.put("hospservice", patient.getLastVisitHospService());
			mapping.put("admissiontype", patient.getLastVisitAdmissionType());
			mapping.put("financialclass", patient.getLastVisitFinancialClass());
			mapping.put("patientunit", patient.getLastVisitPatientUnit());
			mapping.put("patientroom", patient.getLastVisitPatientRoom());
			mapping.put("patientbed", patient.getLastVisitPatientBed());
			mapping.put("patientclassdesc", patient.getLastVisitPatientClassDesc());
			mapping.put("patienttypedesc", patient.getLastVisitPatientTypeDesc());
			mapping.put("patientcasedesc", patient.getLastVisitPatientCaseDesc());
			mapping.put("hospservicedesc", patient.getLastVisitHospServiceDesc());
			mapping.put("admissiontypedesc", patient.getLastVisitAdmissionTypeDesc());
			mapping.put("financialclassdesc", patient.getLastVisitFinancialClassDesc());
			mapping.put("patientunitdesc", patient.getLastVisitPatientUnitDesc());

			return mapping;
		}
	};

	public List<PatientInList> findPatientsByAttendingphysician(Integer companyId, Integer staffId, String orderby, Integer limit, Integer offset) {
		
		List<PatientInList> patientsList = new ArrayList<PatientInList>();
		
		if(orderby==null || orderby.isEmpty()){
			orderby=defaultResultPhysicianPatientListOrderBy;
		}
		if (limit == null){
			limit = defaultResultLimit;
		}
		if (offset == null){
			offset = defaultResultOffset;
		}
	
		patientsList = getJdbcOperations().query(SQLStatements.FIND_INHOUSE_PATIENTS_BY_ATTENDINGPHYSICIAN + " LIMIT " +limit + " OFFSET " + offset, ROW_MAPPER, companyId, staffId);
		
		return patientsList;
	}

	public List<PatientInList> findPatientsAnyForPhysician(Integer companyId, Integer staffId, String patientClass, Integer limit, Integer offset) {
		
		List<PatientInList> patientsList = new ArrayList<PatientInList>();
		if (limit == null){
			limit = defaultResultLimit;
		}
		if (offset == null){
			offset = defaultResultOffset;
		}
	
		String finalQuery = PatientsOfDoctorSQL.FIND_PATIENTS_BY_ANY_PHYSICIAN; 
		if (patientClass != null){
			finalQuery = PatientsOfDoctorSQL.FIND_PATIENTS_BY_ANY_PHYSICIAN.replace("PATIENT_CLASS_CLAUSE", "and hl7code='" + patientClass + "'");
		} else {
			finalQuery = PatientsOfDoctorSQL.FIND_PATIENTS_BY_ANY_PHYSICIAN.replace("PATIENT_CLASS_CLAUSE", "");
		}
		patientsList = getJdbcOperations().query(finalQuery + " LIMIT " +limit + " OFFSET " + offset, ROW_MAPPER, companyId, staffId);
		
		return patientsList;
	}

	public List<PatientInList> findPatientsByAttendingphysicianMultipleStaffId(List<Integer> staffIds, List<Integer> companyids, String orderby, Integer limit, Integer offset) {
		if(orderby==null || orderby.isEmpty()){
			orderby=defaultResultPhysicianPatientListOrderBy;
		}
		if (limit == null){
			limit = defaultResultLimit;
		}
		if (offset == null){
			offset = defaultResultOffset;
		}
		String queryBase = SQLStatements.FIND_INHOUSE_PATIENTS_BY_ATTENDINGPHYSICIAN_MULTIPLE_STAFFID_BASE;
		
		Map<String, Object> params = new HashMap<>();
			
		if (staffIds != null && staffIds.size() > 0){
			params.put("staffids", staffIds);
		} 
		if(companyids != null && companyids.size() > 0){
			params.put("companyids", companyids);
		}
		
		String searchCriteria = multipleStaffIdCriteria(params);
		String query = queryBase + searchCriteria + SQLStatements.FIND_INHOUSE_PATIENTS_BY_ATTENDINGPHYSICIAN_MULTIPLE_STAFFID_ORDER_BY +" LIMIT " +limit + " OFFSET " + offset;
		
		List<PatientInList> patientsList = getJdbcOperations().query(query, ROW_MAPPER);
		return patientsList;
	}
	
	public List<PatientInList> findPatientsForAnyPhysicianMultipleStaffId(List<Integer> staffIds, List<Integer> companyids, String patientClass, Integer limit, Integer offset) {
		if (limit == null){
			limit = defaultResultLimit;
		}
		if (offset == null){
			offset = defaultResultOffset;
		}
		String queryBase = PatientsOfDoctorSQL.FIND_PATIENTS_BY_ANY_PHYSICIAN_MULTIPLE_STAFFID_BASE;
		
		Map<String, Object> params = new HashMap<>();
			
		if (staffIds != null && staffIds.size() > 0){
			params.put("staffids", staffIds);
		} 
		if(companyids != null && companyids.size() > 0){
			params.put("companyids", companyids);
		}
		if(patientClass != null && !patientClass.isEmpty()){
			params.put("hl7code", patientClass);
		}
		
		String searchCriteria = multipleStaffIdCriteria(params);
		String query = queryBase + searchCriteria + PatientsOfDoctorSQL.FIND_PATIENTS_BY_ANY_PHYSICIAN_MULTIPLE_STAFFID_ORDER_BY +" LIMIT " +limit + " OFFSET " + offset;
		
		List<PatientInList> patientsList = getJdbcOperations().query(query, ROW_MAPPER);
		return patientsList;
	}

	public List<PatientInList> findPatientsByAttendingphysicianWeek(Integer companyId, Integer staffId, String orderby, Integer limit, Integer offset) {
		
		List<PatientInList> patientsList = new ArrayList<PatientInList>();
		
		if(orderby==null || orderby.isEmpty()){
			orderby=defaultResultPhysicianPatientListOrderBy;
		}
		if (limit == null){
			limit = defaultResultLimit;
		}
		if (offset == null){
			offset = defaultResultOffset;
		}
		
		patientsList = getJdbcOperations().query(SQLStatements.FIND_PATIENTS_BY_ATTENDINGPHYSICIAN_IN_NEXT_DAYS +" LIMIT " +limit + " OFFSET " +offset, ROW_MAPPER, companyId, staffId);
		
		return patientsList;
	}
	
	public List<PatientInList> findPatientsByAttendingphysicianWeekMultipleStaffId(List<Integer> staffIds, List<Integer> companyids, String orderby, Integer limit, Integer offset) {
		if(orderby==null || orderby.isEmpty()){
			orderby=defaultResultPhysicianPatientListOrderBy;
		}
		if (limit == null){
			limit = defaultResultLimit;
		}
		if (offset == null){
			offset = defaultResultOffset;
		}
		String queryBase = SQLStatements.FIND_PATIENTS_BY_ATTENDINGPHYSICIAN_IN_NEXT_DAYS_MULTIPLE_STAFFID_BASE;
		Map<String, Object> params = new HashMap<>();
		if (staffIds != null && staffIds.size() > 0){
			params.put("staffids", staffIds);
		}
		if(companyids != null && companyids.size() > 0){
			params.put("companyids", companyids);
		}
		String searchCriteria = multipleStaffIdCriteria(params);
		String query = queryBase + searchCriteria + SQLStatements.FIND_INHOUSE_PATIENTS_BY_ATTENDINGPHYSICIAN_MULTIPLE_STAFFID_ORDER_BY +" LIMIT " +limit + " OFFSET " +offset;
		List<PatientInList> patientsList = getJdbcOperations().query(query, ROW_MAPPER);
		return patientsList;
	}

	public List<PatientInList> findByMyUnit(int companyId, String patientunit, Integer limit, Integer offset, String orderby, String sortorder) {
		if (limit == null){
			limit = defaultResultLimit;
		}
		if (offset == null){
			offset = defaultResultOffset;
		}
		if (orderby == null || orderby.isEmpty()){
			orderby = defaultResultPatientListByUnitOrderBy;
			sortorder = ""; // sortOrder is included in the default orderBy
		} else if (sortorder == null){
			sortorder = "";
		}
		List<PatientInList> patients = getJdbcOperations().query(SQLStatements.FIND_PATIENTS_BY_COMPANYID_AND_PATIENTUNIT.replace("orderByParam", orderby).replace("sortOrderParam", sortorder), ROW_MAPPER, companyId, patientunit, limit, offset);
		return patients;
	}

	/**
	 * Finds all the patients that match the criterias
	 * 
	 * @param params {@link Map} of {@link String},{@link Object} The parameters names must match exactly the column names
	 * @param patientSex {@link PatientSexEnum}
	 * @param admissionStatus {@link AdmissionStatusEnum}
	 * @param orderby can be null
	 * @param sortorder can be null
	 * @return
	 */
	public List<PatientInList> findByCriteria(Map<String, Object> params, PatientSexEnum patientSex, AdmissionStatusEnum admissionStatus, String orderby, String sortorder) {		
		
		String queryBase = SQLStatements.FIND_PATIENTS_BY_CRITERIA_BASE;
		if(params.containsKey("email")) {
			queryBase = queryBase.concat(SQLStatements.INNER_JOIN_PATIENT_CONTACT_LIST);
		}
		
		String criteriaString = PatientJdbcRepositoriesUtils.createWhereForByCriteriaSearch(params, patientSex, admissionStatus);
				
		String paginationPart = paginationPart(params);
		if(orderby==null || orderby.isEmpty()){
			orderby = defaultResultPatientListOrderBy;
		}
		if(sortorder==null){
			sortorder = defaultResultPatientListSortOrder;
		}
		String query = queryBase + " " + criteriaString + " " + SQLStatements.FIND_PATIENTS_BY_CRITERIA_BASE_ORDER+ orderby + " "+sortorder+" " + paginationPart;
		List<PatientInList> patients = getJdbcOperations().query(query, ROW_MAPPER);
		return patients;
	}

	/**
	 * 
	 * @param params
	 * @return
	 */
	private String paginationPart(Map<String, Object> params) {
		String paginationPart = "";
		String limit = defaultResultLimit + "";
		String offset = defaultResultOffset + "";
		if (params.get("limit") != null){
			limit = params.get("limit") + "";
		}
		if (params.get("offset") != null){
			offset = params.get("offset") + "";
		}
		paginationPart += (" LIMIT " + limit );
		paginationPart += (" OFFSET " + offset );
		return paginationPart;
	}
	
	@SuppressWarnings("unchecked")
	public String multipleStaffIdCriteria(Map<String, Object> params) {
		SQL99Criteria criteria = new PostgresqlCriteria();
		if (params.containsKey("staffids")){
			SQL99Criteria subCriteria = new SQL99Criteria();
			subCriteria = subCriteria.and();
			subCriteria = subCriteria.in("staffid", ((List<Integer>)params.get("staffids")).toArray());
			criteria.custom(subCriteria.toString());
		}
		if (params.containsKey("companyids")){
			SQL99Criteria subCriteria = new SQL99Criteria();
			subCriteria = subCriteria.and();
			subCriteria = subCriteria.in("company_id", ((List<Integer>)params.get("companyids")).toArray());
			criteria.custom(subCriteria.toString());
		}
		if(params.containsKey("hl7code")){
			SQL99Criteria subCriteria = new SQL99Criteria();
			subCriteria = subCriteria.and();
			subCriteria = subCriteria.equals("hl7code", params.get("hl7code"));
			criteria.custom(subCriteria.toString());
		}
		return (SQLCriteriaClause.WHERE.getName().equals(criteria.toString()) || SQLCriteriaClause.HAVING.getName().equals(criteria.toString())) ? "" : criteria.toString();
	}

}
