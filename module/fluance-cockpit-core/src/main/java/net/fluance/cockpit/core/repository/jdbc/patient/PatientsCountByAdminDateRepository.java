package net.fluance.cockpit.core.repository.jdbc.patient;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import net.fluance.cockpit.core.model.jdbc.patient.PatientAdmitDate;
import net.fluance.commons.sql.SqlUtils;

/**
 * Repository layer to manage the queries which uses {@link PatientAdmitDate}
 *
 */
@Repository
@Component
public class PatientsCountByAdminDateRepository extends JdbcRepository<PatientAdmitDate, String> {
	
	private final static String DATE_COUNT_COLUMN = "date_count";
	private final static String ADMIT_DATE_COLUMN = "admitdate";
	
	public PatientsCountByAdminDateRepository() {
		super(ROW_MAPPER, ROW_UNMAPPER, MappingsConfig.TABLE_NAMES.get("PatientList"));
	}

	public static final RowMapper<PatientAdmitDate> ROW_MAPPER = new RowMapper<PatientAdmitDate>() {
		public PatientAdmitDate mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			Integer count = SqlUtils.getInt(true, rs, DATE_COUNT_COLUMN);
			Date admitdt = rs.getDate(ADMIT_DATE_COLUMN);
			
			PatientAdmitDate patientsAdmitDate = new PatientAdmitDate(count, admitdt.toString());
			
			return patientsAdmitDate;
		}
	};
	
	private static final RowUnmapper<PatientAdmitDate> ROW_UNMAPPER = new RowUnmapper<PatientAdmitDate>() {
		public Map<String, Object> mapColumns(PatientAdmitDate patient) {
			
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			
			mapping.put(DATE_COUNT_COLUMN, patient.getCount());
			mapping.put(ADMIT_DATE_COLUMN, patient.getAdmitdt());
			
			return mapping;
		}
	};
	
	/**
	 * Create a List with the Visit Admit Dates and the number of patients of each date, based in the params
	 * @param params	Values to build the Query
	 * @return
	 */
	public List<PatientAdmitDate> patientsCountByAdminDateCriteria(Map<String, Object> params) {
		
		List<PatientAdmitDate> patients = new ArrayList<PatientAdmitDate>();
		
		String criteriaString = searchCriteria(params);
		
		String queryBase = SQLStatements.PATIENTS_COUNT_BY_ADMIT_DATE_BASE;
		
		String orderBy = " ORDER BY date_trunc('day', admitdt) ";		
		
		String query = queryBase + " " + criteriaString + " AND admitdt <= LOCALTIMESTAMP AND coalesce(dischargedt, 'infinity') >= LOCALTIMESTAMP ORDER BY patient_id, admitdt DESC) sub1 " + orderBy;
		
		patients = getJdbcOperations().query(query, ROW_MAPPER);
		
		return patients;
	}
	
	/**
	 * Creates the search criteria to apply to the base search. The param names must match exactly the column names
	 * @param params
	 * @param clause
	 * @return
	 */
	public String searchCriteria(Map<String, Object> params) {
		SQL99Criteria criteria = new PostgresqlCriteria();
		criteria = criteria.where();
		Set<String> paramNames = params.keySet();
		for(String paramName : paramNames) {
			Boolean isOrderParam = "orderby".equals(paramName) || "sortorder".equals(paramName) || "limit".equals(paramName) || "offset".equals(paramName);
			if(criteria.getFilteringExpressions() > 0 & !isOrderParam) {
				criteria = criteria.and();
			}
			if("lastname".equals(paramName) || "maidenname".equals(paramName) || "firstname".equals(paramName)) {
				criteria = ((PostgresqlCriteria)criteria).startsWithCaseInsensitive(paramName, (String) params.get(paramName));
			} else if("admitdt".equals(paramName)){
				criteria = ((PostgresqlCriteria)criteria).equals("date("+paramName+")", params.get(paramName));
			} else if(!isOrderParam) {
				criteria = ((PostgresqlCriteria)criteria).equals(paramName, params.get(paramName));
			} 
		}
		return (SQLCriteriaClause.WHERE.getName().equals(criteria.toString()) || SQLCriteriaClause.HAVING.getName().equals(criteria.toString())) ? "" : criteria.toString();
	}
	
}
