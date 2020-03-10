package net.fluance.cockpit.core.repository.jdbc.patient;

import java.util.Map;
import java.util.Set;

import net.fluance.app.data.criteria.sql.PostgresqlCriteria;
import net.fluance.app.data.criteria.sql.SQL99Criteria;
import net.fluance.app.data.criteria.sql.SQLCriteriaClause;
import net.fluance.cockpit.core.model.wrap.patient.AdmissionStatusEnum;
import net.fluance.cockpit.core.model.wrap.patient.PatientSexEnum;

/**
 * Different utility methods specially focused on avoid duplicates in the repositories
 */
public class PatientJdbcRepositoriesUtils {
	
	private PatientJdbcRepositoriesUtils() {}
	
	/**
	 * Completes all the where part of the query if some of the parameters match or there is patientSex or admissionStatus
	 * 
	 * @param params {@link Map} of {@link String},{@link Object} The parameters names must match exactly the column names
	 * @param patientSex
	 * @param admissionStatus
	 * @return String representing all the where part of the query, can be empty
	 */
	public static String createWhereForByCriteriaSearch(Map<String, Object> params, PatientSexEnum patientSex, AdmissionStatusEnum admissionStatus) {
		SQL99Criteria criteria = new PostgresqlCriteria();
		criteria = criteria.where();
		Set<String> paramNames = params.keySet();
		for(String paramName : paramNames) {
			boolean ignore = isOrderParam(paramName) || isPaginationParam(paramName);

			if("lastname".equals(paramName) || "maidenname".equals(paramName) || "firstname".equals(paramName)) {
				criteria = ((PostgresqlCriteria)appendAnd(criteria)).startsWithCaseInsensitive(paramName, (String) params.get(paramName));
			}else if("admitdt".equals(paramName)){
				criteria = ((PostgresqlCriteria)appendAnd(criteria)).equals("date("+paramName+")", params.get(paramName));
			}
			else if("email".equals(paramName)) {
				criteria = ((PostgresqlCriteria)appendAnd(criteria)).equalsWithCaseInsensitive("pcl.data", (String) params.get(paramName)).and().equals("pcl.nbtype", "email_address");
			}
			else if(!ignore) {
				criteria = ((PostgresqlCriteria)appendAnd(criteria)).equals(paramName, params.get(paramName));
			} 
		}

		if(patientSex != null && patientSex.getSql().length() > 0) {			
			criteria = ((PostgresqlCriteria)appendAnd(criteria)).custom(patientSex.getSql());
		}
		
		if(admissionStatus != null && admissionStatus.getSql().length() > 0) {
			criteria = ((PostgresqlCriteria)appendAnd(criteria)).custom(admissionStatus.getSql());
		}
		
		return (SQLCriteriaClause.WHERE.getName().equals(criteria.toString()) || SQLCriteriaClause.HAVING.getName().equals(criteria.toString())) ? "" : criteria.toString();
	}
	
	/**
	 * Determine if a parameter because its name is part of the order expected parameters
	 * 
	 * @param paramName
	 * @return
	 */
	public static boolean isOrderParam(String paramName) {
		return "orderby".equals(paramName) || "sortorder".equals(paramName);
	}
	
	/**
	 * Determine if a parameter because its name is part of the pagination expected paramters
	 * 
	 * @param paramName
	 * @return
	 */
	public static boolean isPaginationParam(String paramName) {
		return "limit".equals(paramName) || "offset".equals(paramName);
	}
	
	/**
	 * Append the "AND" to the criteria if it is not empty
	 * 
	 * @param criteria
	 * @return
	 */
	public static SQL99Criteria appendAnd(SQL99Criteria criteria) {
		if(criteria.getFilteringExpressions() > 0) {
			return criteria = criteria.and();
		} else {
			return criteria;
		}
	}
}
