package net.fluance.cockpit.core.repository.jdbc.appointment;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.app.data.criteria.sql.PostgresqlCriteria;
import net.fluance.app.data.criteria.sql.SQL99Criteria;
import net.fluance.app.data.criteria.sql.SQLCriteriaClause;
import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.PatientReference;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.VisitReference;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentDetail;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentSpecific;
import net.fluance.cockpit.core.model.jdbc.appointment.Device;
import net.fluance.cockpit.core.model.jdbc.appointment.Location;
import net.fluance.cockpit.core.model.jdbc.appointment.Personnel;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class AppointmentDetailRepository extends JdbcRepository<AppointmentDetail,Long>{
	
	//Paramter names for findByCriteria methods 
	public static final String PARAMETER_COMPANY_ID = "company_id";
	public static final String PARAMETER_PATIENT_ID = "patient_id";
	public static final String PARAMETER_NB = "nb";
	public static final String PARAMETER_LIMIT = "limit";
	public static final String PARAMETER_OFFSET = "offset";
	public static final String PARAMETER_PATIENT_UNIT = "patientunit";
	public static final String PARAMETER_HOSP_SERVICE = "hospservice";
	public static final String PARAMETER_INCLUDE_ACTIVE = "includeactive";
	public static final String PARAMETER_FROM = "from";
	public static final String PARAMETER_TO = "to";
	public static final String PARAMETER_TYPE = "type";
	public static final String PARAMETER_PATIENT_ROOM = "patientroom";
	public static final String PARAMETER_PATIENT_LOCATION_NAMES = "locationnames";
	
	private static final String SPECIFIC_ANESTHESIA = "Anesthesia";
	private static final String SPECIFIC_POSITION = "Position";

	@SuppressWarnings("unused")
	public AppointmentDetailRepository() {
		this(MappingsConfig.TABLE_NAMES.get("appointment_detail"));
	}

	public AppointmentDetailRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "appointment_id"));
	}

	public static final RowMapper<AppointmentDetail> ROW_MAPPER = (rs, rowNum) -> {

		try {
			ObjectMapper mapper = new ObjectMapper();
			List<Personnel> personnels = null;
			List<Location> locations = null;
			List<Device> devices = null;

			List<AppointmentSpecific> appointmentSpecifics;

			if(rs.getString("personnels") != null && !rs.getString("personnels").isEmpty()){
				personnels = toObjectPersonnel(mapper.readTree(rs.getString("personnels")));
			}
			if(rs.getString("locations") != null && !rs.getString("locations").isEmpty()){
				locations = toObjectLocation(mapper.readTree(rs.getString("locations")));
			}
			if(rs.getString("devices")!=null && !rs.getString("devices").isEmpty()){
				devices = toObjectDevice(mapper.readTree(rs.getString("devices")));
			}

			String anesthesia = null;
			String position = null;
			if(rs.getString("specifics")!=null && !rs.getString("specifics").isEmpty()){
				appointmentSpecifics = toListOfAppointmentSpecific(mapper.readTree(rs.getString("specifics")));

				anesthesia = getValueFromSpecifics(appointmentSpecifics, SPECIFIC_ANESTHESIA);
				position = getValueFromSpecifics(appointmentSpecifics, SPECIFIC_POSITION);
			}

			return new AppointmentDetail(rs.getInt("nb_records"), rs.getLong("appointment_id"), rs.getTimestamp("begindt"), rs.getTimestamp("enddt"), rs.getString("type"),
					rs.getInt("duration"), rs.getString("description"), rs.getString("appointkindcode"), rs.getString("appointkinddescription"), rs.getInt("company_id"),
					anesthesia,
					position,
					new PatientReference(SqlUtils.getLong(true, rs, "patient_id"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("maidenname"), rs.getTimestamp("birthdate")),
					new VisitReference(SqlUtils.getLong(true, rs, "nb"),rs.getString("patientunit"), rs.getString("hospservice"), rs.getString("patientroom")),
					personnels,locations,devices);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	};
	
	/**
	 * Maps the JSON object personnels to a list of {@link Personnel} objects
	 * 
	 * @param personnels
	 * @return
	 */
	private static List<Personnel> toObjectPersonnel(JsonNode personnels){
		List<Personnel> personnelList = new ArrayList<>();
		for (JsonNode node : personnels) {
			String name = node.get("name").asText();
			String role = node.get("role").asText();
			String staffid = node.get("staffid").asText();
			Date begindt = convertStringToTimestamp(node.get("begindt").asText());
			Date enddt = convertStringToTimestamp(node.get("enddt").asText());
			String occupationCode = "";
			if(!StringUtils.isEmpty(node.get("occupationcode"))) {
				occupationCode = node.get("occupationcode").asText();
			}
			
			Personnel personnel = new Personnel();
			personnel.setName(name);
			personnel.setRole(role);
			personnel.setStaffId(staffid);
			personnel.setBeginDate(begindt);
			personnel.setEndDate(enddt);
			personnel.setOccupationCode(occupationCode);			
			
			personnelList.add(personnel);
		}
		return personnelList;
	}
	
	/**
	 * Maps the JSON object locations to a list of {@link Location} objects
	 * 
	 * @param locations
	 * @return
	 */
	private static List<Location> toObjectLocation(JsonNode locations){
		List<Location> locationList = new ArrayList<>();
		for (JsonNode node : locations) {
			String name = node.get("name").asText();
			Date begindt = convertStringToTimestamp(node.get("begindt").asText());
			Date enddt = convertStringToTimestamp(node.get("enddt").asText());
			String type = node.get("type").asText();
			locationList.add(new Location(name, begindt, enddt, type));
		}
		return locationList;
	}
	
	/**
	 * Maps the JSON object devices to a list of {@link Device} objects
	 * 
	 * @param devices
	 * @return
	 */
	private static List<Device> toObjectDevice(JsonNode devices){
		List<Device> deviceList = new ArrayList<>();
		for (JsonNode node : devices) {
			String name = node.get("name").asText();
			Date begindt = convertStringToTimestamp(node.get("begindt").asText());
			Date enddt = convertStringToTimestamp(node.get("enddt").asText());
			String type = node.get("type").asText();
			deviceList.add(new Device(name, begindt, enddt, type));
		}
		return deviceList;
	}
	
	/**
	 * Utility method for convert the given String to a {@link Date)<br>
	 * The expected format is "yyyy-MM-dd'T'HH:mm:ss"
	 * 
	 * @param stringDate
	 * @return
	 */
	private static Date convertStringToTimestamp(String stringDate) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		    Date date = formatter.parse(stringDate);
			return new Timestamp(date.getTime());
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * Converts the JsonNode to a list of objects of {@link AppointmentSpecific} 
	 * 
	 * @param devices
	 * @return
	 */
	private static List<AppointmentSpecific> toListOfAppointmentSpecific(JsonNode devices){
		List<AppointmentSpecific> AppointmentSpecifics = new ArrayList<>();
		AppointmentSpecific appointmentSpecific;
		for (JsonNode node : devices) {
			appointmentSpecific = new AppointmentSpecific();
			String procedure = node.get("procedure").asText();
			String description = node.get("description").asText();
			
			if(procedure != null) {
				appointmentSpecific.setProcedure(procedure);
				appointmentSpecific.setDescription(description);
				AppointmentSpecifics.add(appointmentSpecific);
			}
		}
		return AppointmentSpecifics;
	}
	
	/**
	 * Finds into the given list the procedure with the given name and return the description as result.<br>
	 * If it is not possible to find null is returned.
	 * 
	 * @param appointmentSpecifics
	 * @param procedureName
	 * @return {@link String} can be null
	 */
	private static String getValueFromSpecifics(List<AppointmentSpecific> appointmentSpecifics, String procedureName) {
		String value = null;
		if(appointmentSpecifics != null && appointmentSpecifics.size() > 0 && procedureName != null && procedureName.length() > 0) {
			AppointmentSpecific appointmentSpecific = appointmentSpecifics.stream()
					  .filter(appointmentSpecificFiltered -> procedureName.equals(appointmentSpecificFiltered.getProcedure()))
					  .findAny()
					  .orElse(null);
			if(appointmentSpecific != null) {
				value = appointmentSpecific.getDescription();
			}
		}
		
		return value;
	}
	
	/**
	 * Always return null because no create, update or delete is expected with this repository  
	 */
	private static final RowUnmapper<AppointmentDetail> ROW_UNMAPPER = appointment -> null;

	public AppointmentDetail findByAppointmentId(Long appointmentid){
		return getJdbcOperations().queryForObject(SQLStatements.FIND_BY_APPOINTMENT_ID, ROW_MAPPER, appointmentid);
	}

	/**
	 * Returns the list of {@link AppointmentDetail} for the query generated with the criteria based in the given parameters, <b>all the parameters are optional</b><br>
	 * It's possible to paginate the query with limit and offset<br>
	 * <b>The method don't validate the values or the format of the query statement.</b> 
	 * 
	 * @param companyId		Integer
	 * @param pid			Long	patient id
	 * @param visitNumber	Long
	 * @param patientUnits	{@link List} of {@link String}
	 * @param hospServices	{@link List} of {@link String}
	 * @param type			String
	 * @param rooms			{@link List} of {@link String}
	 * @param locationNames {@link List} of {@link String}
	 * @param from			{@link LocalDateTime} if null set to <b>now</b>
	 * @param to 			{@link LocalDateTime}
	 * @param orderBy		String	correct syntax for the order by, example "patient_id desc"
	 * @param limit 		String	if null set to default
	 * @param offset		String 	if null set to default
	 * @return
	 */
	public List<AppointmentDetail> findByCriteriaShortedAndPaginated(Integer companyId, Long pid, Long visitNumber, List<String> patientUnits, List<String> hospServices, String type, List<String> rooms, List<String> locationNames, boolean includeActive, LocalDate from, LocalDate to, String orderBy, Integer limit, Integer offset) {
		String queryBase = SQLStatements.FIND_DETAILED_APPOINTMENTS_BY_COMPANYId_PATIENTUNIT_HOSPSERVICE_BASE;
		Map<String, Object> params = new HashMap<>();
		if(companyId != null){
			params.put(PARAMETER_COMPANY_ID, companyId);
		}
		if(pid != null){
			params.put(PARAMETER_PATIENT_ID, pid);
		}
		if(visitNumber != null){
			params.put(PARAMETER_NB, visitNumber);
		}
		if(limit !=null && limit > 0){
			params.put(PARAMETER_LIMIT, limit);
		}
		if(offset !=null && offset > 0){
			params.put(PARAMETER_OFFSET, offset);
		}
		if (patientUnits != null && patientUnits.size() > 0){
			params.put(PARAMETER_PATIENT_UNIT, patientUnits);
		} 
		if (hospServices != null && hospServices.size() > 0){
			params.put(PARAMETER_HOSP_SERVICE, hospServices);
		}
		
		params.put(PARAMETER_INCLUDE_ACTIVE, includeActive);
		
		if (from != null ){
			params.put(PARAMETER_FROM, from);
		}
		if (to != null ){
			params.put(PARAMETER_TO, to);
		}		
		if (type != null ){
			params.put(PARAMETER_TYPE, type);
		}
		if (rooms != null && rooms.size() > 0){
			params.put(PARAMETER_PATIENT_ROOM, rooms);
		}
		if (locationNames != null && locationNames.size() > 0){
			params.put(PARAMETER_PATIENT_LOCATION_NAMES, locationNames);
		}

		String searchCriteria = findByCriteriaCreateCriteria(params);
		String paginationPart = paginationPart(params);
		String query = queryBase + " " + searchCriteria + " ORDER BY " + orderBy+ " " + paginationPart;
		return getJdbcOperations().query(query, ROW_MAPPER);
	}

	public List<AppointmentDetail> findByStaffidAndCompanyid(String staffid, Integer companyid, String orderby, Integer limit, Integer offset) {
		String criteria = singleStaffIdCriteria(staffid, companyid);
		return getJdbcOperations().query(SQLStatements.FIND_DETAILED_APPOINTMENTS_SINGLE_STAFF_ID + criteria + SQLStatements.FIND_DETAILED_APPOINTMENTS_WITHOUT_DATE_PARAM + " ORDER BY " +orderby +" LIMIT " +limit + " OFFSET " +offset, ROW_MAPPER);
	}

	public List<AppointmentDetail> findByStaffidAndCompanyidAndBegindt(String staffid, Integer companyid, String date, String orderby, Integer limit, Integer offset) {
		String criteria = singleStaffIdCriteria(staffid, companyid);
		return getJdbcOperations().query(SQLStatements.FIND_DETAILED_APPOINTMENTS_SINGLE_STAFF_ID + criteria + SQLStatements.FIND_DETAILED_APPOINTMENTS_DATE_PARAM.replaceAll("dateParam", date) + " ORDER BY " +orderby +" LIMIT " +limit + " OFFSET " +offset, ROW_MAPPER);
	}

	public List<AppointmentDetail> findByMultipleStaffids(Map<Integer, String> companyStaffIds, String date, String orderby, Integer limit, Integer offset){
		String queryBase = SQLStatements.FIND_DETAILED_APPOINTMENTS_MULTIPLE_STAFFID_BASE;
		String searchCriteria = multipleStaffIdCriteria(companyStaffIds);
		String query;
		if(date!=null && !date.isEmpty()){
			query = queryBase + " " + searchCriteria + SQLStatements.FIND_DETAILED_APPOINTMENTS_DATE_PARAM.replaceAll("dateParam", date) + " ORDER BY " +orderby +" LIMIT " +limit + " OFFSET " +offset;   
		}
		else{
			query = queryBase + " " + searchCriteria + SQLStatements.FIND_DETAILED_APPOINTMENTS_WITHOUT_DATE_PARAM + " ORDER BY " +orderby +" LIMIT " +limit + " OFFSET " +offset;
		}
		return getJdbcOperations().query(query, ROW_MAPPER);
	}
	
	public Integer findByMultipleStaffidsCount(Map<Integer, String> companyStaffIds, String date){
		String queryBase = SQLStatements.FIND_DETAILED_APPOINTMENTS_BASE_COUNT;
		String searchCriteria = multipleStaffIdCriteria(companyStaffIds);
		String query;
		if(date!=null && !date.isEmpty()){
			query = queryBase + " " + searchCriteria + SQLStatements.FIND_DETAILED_APPOINTMENTS_DATE_PARAM.replaceAll("dateParam", date);   
		}
		else{
			query = queryBase + " " + searchCriteria + SQLStatements.FIND_DETAILED_APPOINTMENTS_WITHOUT_DATE_PARAM;
		}
		return getJdbcOperations().queryForObject(query, Integer.class);
	}

	public Integer findBySingleStaffidCount(String staffid, Integer companyid, String date){
		String criteria = singleStaffIdCriteria(staffid, companyid);
		if (date != null && !date.isEmpty()){
			return getJdbcOperations().queryForObject(SQLStatements.FIND_DETAILED_APPOINTMENTS_BASE_COUNT + criteria + SQLStatements.FIND_DETAILED_APPOINTMENTS_DATE_PARAM.replaceAll("dateParam", date), Integer.class);
		} else {
			return getJdbcOperations().queryForObject(SQLStatements.FIND_DETAILED_APPOINTMENTS_BASE_COUNT + criteria + SQLStatements.FIND_DETAILED_APPOINTMENTS_WITHOUT_DATE_PARAM, Integer.class);
		}
	}
	
	/**
	 * Returns the number of results of the query generated with the criteria created based in the given parameters, <b>all the parameters are optional</b><br>
	 * <b>The method don't validate the values or the format of the query statement.</b>
	 * 
	 * @param companyId		Integer
	 * @param pid			Long patient id
	 * @param visitNumber	Long
	 * @param patientUnits	{@link List} of {@link String}
	 * @param hospServices	{@link List} of {@link String}
	 * @param type			String
	 * @param rooms			{@link List} of {@link String}
	 * @param locationNames {@link List} of {@link String}
	 * @param from			{@link LocalDateTime}
	 * @param to 			{@link LocalDateTime}
	 * @return
	 */
	public Integer findByCriteriaCount(Integer companyId, Long pid, Long visitNumber, List<String> patientUnits, List<String> hospServices, String type, List<String> rooms, List<String> locationNames, boolean includeActive, LocalDate from, LocalDate to){
		String queryBase = SQLStatements.FIND_DETAILED_APPOINTMENTS_UNIT_SERVICE_PID_BASE_COUNT;
		Map<String, Object> params = new HashMap<>();
		
		if(companyId != null){
			params.put(PARAMETER_COMPANY_ID, companyId);
		}
		if(pid != null){
			params.put(PARAMETER_PATIENT_ID, pid);
		}
		if(visitNumber != null){
			params.put(PARAMETER_NB, visitNumber);
		}
		if (patientUnits != null && patientUnits.size() > 0){
			params.put(PARAMETER_PATIENT_UNIT, patientUnits);
		} 
		if (hospServices != null && hospServices.size() > 0){
			params.put(PARAMETER_HOSP_SERVICE, hospServices);
		} 
	
		params.put(PARAMETER_INCLUDE_ACTIVE, includeActive);
		
		if (from != null) {
			params.put(PARAMETER_FROM, from);
		} 
		if (to != null) {
			params.put(PARAMETER_TO, to);
		}
		if (type != null) {
			params.put(PARAMETER_TYPE, type);
		}
		if (rooms != null && rooms.size() > 0){
			params.put(PARAMETER_PATIENT_ROOM, rooms);
		}
		if (locationNames != null && locationNames.size() > 0){
			params.put(PARAMETER_PATIENT_LOCATION_NAMES, locationNames);
		}
		
		String searchCriteria = findByCriteriaCreateCriteria(params);
		String query = queryBase + " " + searchCriteria;
		return getJdbcOperations().queryForObject(query, Integer.class);
	}
	
	/**
	 * Creates the criteria part for the select
	 * 
	 * @param params
	 * @return String with the criteria for the select
	 */
	@SuppressWarnings("unchecked")
	private String findByCriteriaCreateCriteria(Map<String, Object> params) {
		SQL99Criteria criteria = new PostgresqlCriteria();
		criteria = criteria.where();

		if (params.containsKey(PARAMETER_COMPANY_ID)){
			and(criteria);
			criteria = criteria.equals(PARAMETER_COMPANY_ID, params.get(PARAMETER_COMPANY_ID));
		}
		if (params.containsKey(PARAMETER_PATIENT_ID)){
			and(criteria);
			criteria = criteria.equals(PARAMETER_PATIENT_ID, params.get(PARAMETER_PATIENT_ID));
		}
		if (params.containsKey(PARAMETER_NB)){
			and(criteria);
			criteria = criteria.equals(PARAMETER_NB, params.get(PARAMETER_NB));
		}
		if (params.containsKey(PARAMETER_PATIENT_UNIT) && params.containsKey(PARAMETER_HOSP_SERVICE)){
			and(criteria);
			SQL99Criteria subCriteria = new SQL99Criteria();
			subCriteria = subCriteria.custom("(");
			subCriteria = subCriteria.in(PARAMETER_PATIENT_UNIT, ((List<String>)params.get(PARAMETER_PATIENT_UNIT)).toArray());
			subCriteria = subCriteria.and();
			subCriteria = subCriteria.in(PARAMETER_HOSP_SERVICE, ((List<String>)params.get(PARAMETER_HOSP_SERVICE)).toArray());
			subCriteria = subCriteria.custom(")");
			criteria.custom(subCriteria.toString());
		} else if (params.containsKey(PARAMETER_PATIENT_UNIT)){
			and(criteria);
			criteria = criteria.in(PARAMETER_PATIENT_UNIT, ((List<String>)params.get(PARAMETER_PATIENT_UNIT)).toArray());
		} else if (params.containsKey(PARAMETER_HOSP_SERVICE)){
			and(criteria);
			criteria = criteria.in(PARAMETER_HOSP_SERVICE, ((List<String>)params.get(PARAMETER_HOSP_SERVICE)).toArray());
		}
		
		if(params.containsKey(PARAMETER_PATIENT_LOCATION_NAMES)) {
			and(criteria);
			criteria = criteria.custom(locationNamesCriteria((List<String>)params.get(PARAMETER_PATIENT_LOCATION_NAMES)));
		}
		
		boolean includeActive = (boolean) params.get(PARAMETER_INCLUDE_ACTIVE);
		
		if(params.containsKey(PARAMETER_FROM)) {
			if(includeActive) {
				
				SQL99Criteria subCriteria = new SQL99Criteria();
				subCriteria = subCriteria.and();
				subCriteria = subCriteria.custom("(");
				subCriteria = subCriteria.ge("DATE(begindt)", params.get(PARAMETER_FROM).toString());
				
				subCriteria = subCriteria.or();
				
				subCriteria = subCriteria.custom("(");
				subCriteria = subCriteria.lt("DATE(begindt)", params.get(PARAMETER_FROM).toString());
				subCriteria = subCriteria.and();
				subCriteria = subCriteria.ge("DATE(enddt)", params.get(PARAMETER_FROM).toString());
				subCriteria = subCriteria.custom(")");
				
				subCriteria = subCriteria.custom(")");
				criteria.custom(subCriteria.toString());
			} else {
				and(criteria);
				criteria = criteria.ge("DATE(begindt)", params.get(PARAMETER_FROM).toString());
			}
		}
		
		if(params.containsKey(PARAMETER_TO)) {			
			and(criteria);
			criteria = criteria.le("DATE(begindt)", params.get(PARAMETER_TO).toString());	
		}

		if(params.containsKey(PARAMETER_PATIENT_ROOM)) {
			and(criteria);
			criteria = criteria.in(PARAMETER_PATIENT_ROOM, ((List<String>)params.get(PARAMETER_PATIENT_ROOM)).toArray());
		}

		Set<String> paramNames = params.keySet();
		for(String paramName : paramNames) {
			if(!isOrderParam(paramName) && !isSpecialParam(paramName)) {
				and(criteria);
				criteria = criteria.equals(paramName, params.get(paramName));
			} 
		}
		return (SQLCriteriaClause.WHERE.getName().equals(criteria.toString()) || SQLCriteriaClause.HAVING.getName().equals(criteria.toString())) ? "" : criteria.toString();
	}
	
	/**
	 * If return true the paramName is in the list it should be treat in special way, for example as a list or with the comparation different than equal.
	 * 
	 * @param paramName
	 * @return
	 */
	private boolean isSpecialParam(String paramName) {
		return Arrays.asList(PARAMETER_COMPANY_ID, PARAMETER_PATIENT_ID, PARAMETER_NB, PARAMETER_PATIENT_UNIT, PARAMETER_HOSP_SERVICE, PARAMETER_FROM, PARAMETER_TO, PARAMETER_PATIENT_ROOM, PARAMETER_INCLUDE_ACTIVE, PARAMETER_PATIENT_LOCATION_NAMES).contains(paramName);
	}

	/**
	 * If return true the paramName will be use for the order
	 * 
	 * @param paramName
	 * @return
	 */
	private boolean isOrderParam(String paramName) {
		return Arrays.asList("orderby", "sortorder", PARAMETER_LIMIT, PARAMETER_OFFSET).contains(paramName);
	}

	/**
	 * Appends and clause to the criteria
	 * 
	 * @param criteria
	 */
	private void and(SQL99Criteria criteria) {
		if (criteria.getFilteringExpressions() > 0){
			criteria.and();
		}
	}
	
	/**
	 * Creates the Limit and offset clause<br>
	 * The method expects PARAMETER_LIMIT and PARAMETER_OFFSET in the params map.  
	 * 
	 * @param params
	 * @return
	 */
	private String paginationPart(Map<String, Object> params) {
		StringBuilder paginationPart = new StringBuilder();
		Set<String> paramNames = params.keySet();
		for(String paramName : paramNames) {
			if(PARAMETER_LIMIT.equals(paramName)) {
				paginationPart.append(" LIMIT ").append(params.get(paramName));
			} else if(PARAMETER_OFFSET.equals(paramName)) {
				paginationPart.append(" OFFSET ").append(params.get(paramName));
			}
		}
		return paginationPart.toString();
	}

	/**
	 * Returns the clause for searching by at least one staffid in the personnels field.<br>
	 * All the staffId will be concatenated by OR operator.<br>
	 * Personnels field is a JSON object.
	 * 
	 * @param companyStaffIds
	 * @return String representing the a clause 
	 */
	private String multipleStaffIdCriteria(Map<Integer, String> companyStaffIds) {
		StringBuilder criteria = new StringBuilder("(");
		Iterator<Entry<Integer, String>> iterator = companyStaffIds.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer,String> entry = iterator.next();
			criteria.append("(company_id=").append(entry.getKey()).append(" AND personnels @> '[{\"staffid\": \"").append(entry.getValue()).append("\"}]')");
			if(iterator.hasNext()){
				criteria.append(" OR ");
			}
		}
		criteria.append(")");
		return criteria.toString();
	}

	/**
	 * Returns the clause for searching by staffid in the personnels field.<br>
	 * Personnels field is a JSON object.
	 * 
	 * @param staffid
	 * @param companyid
	 * @return
	 */
	private String singleStaffIdCriteria(String staffid, Integer companyid){
		return " (company_id=" + companyid + " AND personnels @> '[{\"staffid\": \""+staffid+"\"}]') ";
	}
	
	private String locationNamesCriteria(List<String> locationNames) {
		return "(" + locationNames.stream()
	    	.map(this::singleLocationNameCriteria)
	    	.collect(Collectors.joining(" OR ") ) + ")";
	}
	
	private String singleLocationNameCriteria(String locationName) {
		return " (locations @> '[{\"name\": \""+locationName+"\"}]') ";
	}
	
}
