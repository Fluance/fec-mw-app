package net.fluance.cockpit.core.repository.jdbc.appointment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.app.data.criteria.sql.PostgresqlCriteria;
import net.fluance.app.data.criteria.sql.SQL99Criteria;
import net.fluance.app.data.criteria.sql.SQLCriteriaClause;
import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.appointment.Appointment;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentNew;

@Repository
@Component
public class AppointmentPatientRepository extends JdbcRepository<Appointment, Integer> {

	private static final String DEFAULT_BEGINDT_CRITERIA = "begindt>=current_date";
	private static final String DEFAULT_CHECKED_STATUS = "Deleted";

	public AppointmentPatientRepository() {
		this(MappingsConfig.TABLE_NAMES.get("bmv_personnel_appointment_list"));
	}

	public AppointmentPatientRepository(String tableName) {
		super(ROW_MAPPER_OLD, ROW_UNMAPPER, new TableDescription(tableName, null, ""));
	}

	public static final RowMapper<AppointmentNew> ROW_MAPPER = (rs, rowNum) -> new AppointmentNew(
			rs.getInt("nb_records"), rs.getLong("appoint_id"),
			rs.getLong("pid"), rs.getString("patientroom"), rs.getLong("nb"),
			rs.getString("firstname"), rs.getString("lastname"),
			rs.getString("maidenname"), rs.getTimestamp("begindt"),  rs.getString("description"));

	public static final RowMapper<Appointment> ROW_MAPPER_OLD = (rs, rowNum) -> new Appointment(
			rs.getInt("nb_records"), rs.getLong("appoint_id"), rs.getLong("visit_nb"),
			rs.getString("duration"), rs.getTimestamp("begindt"), rs.getTimestamp("enddt"),
			rs.getString("appointment_type"), rs.getString("description"), rs.getString("status"),
			rs.getInt("staffid"), rs.getLong("pid"), rs.getString("firstname"), rs.getString("lastname"),
			rs.getString("maidenname"), rs.getString("birthdate"), rs.getString("hospservice"),
			rs.getString("hospservicedesc"), rs.getString("patientunit"), rs.getString("patientunitdesc"),
			rs.getString("patientroom"), rs.getString("patientclass"), rs.getString("patientclassdesc"),
			rs.getInt("company_id"));

	private static final RowUnmapper<Appointment> ROW_UNMAPPER = appointment -> null;

	public List<AppointmentNew> findByCompanyIdAndPatientUnitAndHospService(Integer companyid, List<String> patientunit, List<String> hospservice, String orderby, Integer limit, Integer offset) {
		String queryBase = SQLStatements.FIND_APPOINTMENTS_BY_COMPANYId_PATIENTUNIT_HOSPSERVICE_BASE;
		Map<String, Object> params = new HashMap<>();
		if(companyid != null){
			params.put("company_id", companyid);
		}
		params.put("status", DEFAULT_CHECKED_STATUS);
		if(limit > 0){
			params.put("limit", limit);
		}
		if(offset > 0){
			params.put("offset", offset);
		}
		if (patientunit != null && patientunit.size() > 0){
			params.put("patientunit", patientunit);
		} 
		if (hospservice != null && hospservice.size() > 0){
			params.put("hospservice", hospservice);
		} 
		String searchCriteria = searchCriteria(params);
		String paginationPart = paginationPart(params);
		String query = queryBase + " " + searchCriteria + " ORDER BY " + orderby+ " " + paginationPart;
		return getJdbcOperations().query(query, ROW_MAPPER);
	}

	@SuppressWarnings("unchecked")
	public String searchCriteria(Map<String, Object> params) {
		SQL99Criteria criteria = new PostgresqlCriteria();
		criteria = criteria.where();
		if (params.containsKey("company_id")){
			and(criteria);
			criteria = criteria.equals("company_id", params.get("company_id"));
		}
		if (params.containsKey("status")){
			and(criteria);
			criteria = criteria.notEquals("status", params.get("status"));
		}
		if (params.containsKey("patientunit") && params.containsKey("hospservice")){
			SQL99Criteria subCriteria = new SQL99Criteria();
			subCriteria = subCriteria.and();
			subCriteria = subCriteria.custom("(");
			subCriteria = subCriteria.in("patientunit", ((List<String>)params.get("patientunit")).toArray());
			subCriteria = subCriteria.and();
			subCriteria = subCriteria.in("hospservice", ((List<String>)params.get("hospservice")).toArray());
			subCriteria = subCriteria.custom(")");
			criteria.custom(subCriteria.toString());
		} else if (params.containsKey("patientunit")){
			and(criteria);
			criteria = criteria.in("patientunit", ((List<String>)params.get("patientunit")).toArray());
		} else if (params.containsKey("hospservice")){
			and(criteria);
			criteria = criteria.in("hospservice", ((List<String>)params.get("hospservice")).toArray());
		}

		Set<String> paramNames = params.keySet();
		for(String paramName : paramNames) {
			if(!isOrderParam(paramName) && !isSpecialParam(paramName)) {
				and(criteria);
				criteria = criteria.equals(paramName, params.get(paramName));
			} 
		}
		and(criteria);
		criteria = criteria.custom(DEFAULT_BEGINDT_CRITERIA);
		return (SQLCriteriaClause.WHERE.getName().equals(criteria.toString()) || SQLCriteriaClause.HAVING.getName().equals(criteria.toString())) ? "" : criteria.toString();
	}

	private boolean isSpecialParam(String paramName) {
		return Arrays.asList("company_id", "status", "patientunit", "hospservice").contains(paramName);
	}

	private boolean isOrderParam(String paramName) {
		return Arrays.asList("orderby", "sortorder", "limit", "offset").contains(paramName);
	}

	private void and(SQL99Criteria criteria) {
		if (criteria.getFilteringExpressions() > 0){
			criteria.and();
		}
	}

	/**
	 * 
	 * @param params Map<String, Object>
	 * @return String
	 */
	private String paginationPart(Map<String, Object> params) {
		StringBuilder paginationPart = new StringBuilder();
		Set<String> paramNames = params.keySet();
		for(String paramName : paramNames) {
			if ("limit".equals(paramName)) {
				paginationPart.append(" LIMIT ").append(params.get(paramName));
			} else if("offset".equals(paramName)) {
				paginationPart.append(" OFFSET ").append(params.get(paramName));
			}
		}
		return paginationPart.toString();
	}

	@Deprecated
	// HPRM-910
	public List<AppointmentNew> findByCompanyId(Integer companyid, String orderby, Integer limit, Integer offset) {
		return getJdbcOperations().query(SQLStatements.FIND_APPOINTMENTS_BY_COMPANYId.replace("ORDER BY ?", "ORDER BY "+orderby), ROW_MAPPER, companyid, limit, offset );
	}
	@Deprecated
	// HPRM-910
	public List<AppointmentNew> findByCompanyIdAndPatientUnit(Integer companyid, String patientunit, String orderby, Integer limit, Integer offset) {
		return getJdbcOperations().query(SQLStatements.FIND_APPOINTMENTS_BY_COMPANYId_PATIENTUNIT.replace("ORDER BY ?", "ORDER BY "+orderby), ROW_MAPPER, companyid, patientunit,limit, offset );
	}
	@Deprecated
	// HPRM-910
	public List<AppointmentNew> findByCompanyIdAndHospService(Integer companyid, String hospservice, String orderby, Integer limit, Integer offset) {
		return getJdbcOperations().query(SQLStatements.FIND_APPOINTMENTS_BY_COMPANYId_HOSPSERVICE.replace("ORDER BY ?", "ORDER BY "+orderby), ROW_MAPPER, companyid, hospservice,limit, offset );
	}

	public List<Appointment> findByVisitnb(Integer visitnb) {
		return getJdbcOperations().query(SQLStatements.FIND_APPOINTMENTS_BY_VISIT, ROW_MAPPER_OLD, visitnb);
	}
	public List<Appointment> findByStaffidAndCompanyid(String staffid, Integer companyid, String begindt, String enddt) {
		return getJdbcOperations().query(SQLStatements.FIND_PHYSICIAN_NEXT_DAY_APPOINTMENT, ROW_MAPPER_OLD, staffid, companyid, begindt, enddt);
	}
	public List<Appointment> findByStaffidAndCompanyidAndBegindtAndEnddt(String staffid, Integer companyid, String begindt, String enddt) {
		return getJdbcOperations().query(SQLStatements.FIND_PHYSICIAN_NEXT_WEEK_APPOINTMENT, ROW_MAPPER_OLD, staffid, companyid, begindt, enddt);
	}
}
