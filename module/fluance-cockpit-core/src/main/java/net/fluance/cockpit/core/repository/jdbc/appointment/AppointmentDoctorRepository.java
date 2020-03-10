package net.fluance.cockpit.core.repository.jdbc.appointment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import net.fluance.cockpit.core.model.PatientReference;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentDoctorList;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class AppointmentDoctorRepository extends JdbcRepository<AppointmentDoctorList, Long> {

	public AppointmentDoctorRepository() {
		this(MappingsConfig.TABLE_NAMES.get("bmv_personnel_appointment_list"));
	}

	public AppointmentDoctorRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, ""));
	}

	public static final RowMapper<AppointmentDoctorList> ROW_MAPPER = new RowMapper<AppointmentDoctorList>() {
		public AppointmentDoctorList mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new AppointmentDoctorList(SqlUtils.getInt(true, rs, "nb_records"), SqlUtils.getLong(true, rs, "appointment_id"), rs.getString("description"), rs.getTimestamp("begindt"), rs.getTimestamp("enddt"), new PatientReference(SqlUtils.getLong(true, rs, "patient_id"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("maidenname"), rs.getTimestamp("birthdate")), SqlUtils.getLong(true, rs, "nb"));
		}
	};



	private static final RowUnmapper<AppointmentDoctorList> ROW_UNMAPPER = new RowUnmapper<AppointmentDoctorList>() {
		public Map<String, Object> mapColumns(AppointmentDoctorList appointment) {
			return null;
		}
	};

	public List<AppointmentDoctorList> findByStaffidAndCompanyid(String staffid, Integer companyid, String orderby, Integer limit, Integer offset) {
		List<AppointmentDoctorList> c = getJdbcOperations().query(SQLStatements.FIND_DOCTOR_APPOINTMENTS + SQLStatements.FIND_DOCTOR_APPOINTMENTS_COMPANYID_PARAM + SQLStatements.FIND_DOCTOR_APPOINTMENTS_WITHOUT_DATE_PARAM + " ORDER BY " +orderby +" LIMIT " +limit + " OFFSET " +offset, ROW_MAPPER, staffid, companyid);
		return c;
	}

	public List<AppointmentDoctorList> findByStaffidAndCompanyidAndBegindt(String staffid, Integer companyid, String date, String orderby, Integer limit, Integer offset) {
		List<AppointmentDoctorList> c = getJdbcOperations().query(SQLStatements.FIND_DOCTOR_APPOINTMENTS + SQLStatements.FIND_DOCTOR_APPOINTMENTS_COMPANYID_PARAM + SQLStatements.FIND_DOCTOR_APPOINTMENTS_DATE_PARAM.replaceAll("dateParam", date) + " ORDER BY " +orderby +" LIMIT " +limit + " OFFSET " +offset, ROW_MAPPER, staffid, companyid);
		return c;
	}

	public List<AppointmentDoctorList> findByMultipleStaffids(List<String> staffids, List<Integer> companyids, String date, String orderby, Integer limit, Integer offset){
		String queryBase = SQLStatements.FIND_DOCTOR_APPOINTMENTS_MULTIPLE_STAFFID_BASE;
		Map<String, Object> params = new HashMap<>();
		if (staffids != null && staffids.size() > 0){
			params.put("staffids", staffids);
		}
		if(companyids != null && companyids.size() > 0){
			params.put("companyids", companyids);
		}
		String searchCriteria = searchCriteria(params);
		String query = "";
		if(date!=null && !date.isEmpty()){
			query = queryBase + " " + searchCriteria + SQLStatements.FIND_DOCTOR_APPOINTMENTS_DATE_PARAM.replaceAll("dateParam", date) + " ORDER BY " +orderby +" LIMIT " +limit + " OFFSET " +offset;   
		}
		else{
			query = queryBase + " " + searchCriteria + SQLStatements.FIND_DOCTOR_APPOINTMENTS_WITHOUT_DATE_PARAM + " ORDER BY " +orderby +" LIMIT " +limit + " OFFSET " +offset;
		}
		List<AppointmentDoctorList> c = getJdbcOperations().query(query, ROW_MAPPER);
		return c;
	}

	@SuppressWarnings("unchecked")
	public String searchCriteria(Map<String, Object> params) {
		SQL99Criteria criteria = new PostgresqlCriteria();
		if (params.containsKey("staffids")){
			SQL99Criteria subCriteria = new SQL99Criteria();
			subCriteria = subCriteria.and();
			subCriteria = subCriteria.in("staffid", ((List<String>)params.get("staffids")).toArray());
			criteria.custom(subCriteria.toString());
		}
		if (params.containsKey("companyids")){
			SQL99Criteria subCriteria = new SQL99Criteria();
			subCriteria = subCriteria.and();
			subCriteria = subCriteria.in("company_id", ((List<Integer>)params.get("companyids")).toArray());
			criteria.custom(subCriteria.toString());
		}
		return (SQLCriteriaClause.WHERE.getName().equals(criteria.toString()) || SQLCriteriaClause.HAVING.getName().equals(criteria.toString())) ? "" : criteria.toString();
	}
	
}
