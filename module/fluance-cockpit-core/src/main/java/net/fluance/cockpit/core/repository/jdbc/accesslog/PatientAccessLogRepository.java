package net.fluance.cockpit.core.repository.jdbc.accesslog;

import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.accesslog.PatientAccessLog;
import net.fluance.cockpit.core.util.sql.PatientAccessLogOrderByEnum;
import net.fluance.cockpit.core.util.sql.PatientAccessLogShortOrderEnum;

/**
 * REpository for request ehealth.bmv_user_access_list the results will be stored in objects of {@link PatientAccessLog}
 */
@Repository
@Component
public class PatientAccessLogRepository extends JdbcRepository<PatientAccessLog, Integer> {

	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultAccessLogListOrderBy}")
	private String defaultResultAccessLogListOrderBy;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultAccessLogListSortOrder}")
	private String defaultResultAccessLogListSortOrder;

	@Autowired
	@Qualifier("ehlogdataSource")
	private DataSource ehlogdataSource;

	public PatientAccessLogRepository() {
		this(MappingsConfig.TABLE_NAMES.get("UserAccess"));
	}

	public PatientAccessLogRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "id"));
	}

	public static final RowMapper<PatientAccessLog> ROW_MAPPER = new RowMapper<PatientAccessLog>() {

		public PatientAccessLog mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new PatientAccessLog(rs.getTimestamp("logdt"), rs.getString("objecttype"), rs.getString("objectid"), rs.getString("displayname"), rs.getString("parentpid"), rs.getString("parentvn"), rs.getString("httpmethod"),
					rs.getString("appuser"), rs.getString("firstname"), rs.getString("lastname"), rs.getString("externaluser"), rs.getString("externalfirstname"), rs.getString("externallastname"), rs.getString("externalemail"));
		}
	};

	public static final RowUnmapper<PatientAccessLog> ROW_UNMAPPER = new RowUnmapper<PatientAccessLog>() {

		public Map<String, Object> mapColumns(PatientAccessLog accessLog) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("logdt", accessLog.getLogDate());
			mapping.put("objecttype", accessLog.getObjectType());
			mapping.put("objectid", accessLog.getObjectId());
			mapping.put("displayname", accessLog.getDisplayName());
			mapping.put("parentpid", accessLog.getParentPid());
			mapping.put("parentvn", accessLog.getParentVisitNb());
			mapping.put("httpmethod", accessLog.getHttpMethod());
			mapping.put("appuser", accessLog.getAppUser());
			mapping.put("firstname", accessLog.getFirstName());
			mapping.put("lastname", accessLog.getLastName());
			mapping.put("externaluser", accessLog.getExternalUser());			
			mapping.put("externalfirstname", accessLog.getExternalFirstName());
			mapping.put("externallastname", accessLog.getExternalLastName());
			mapping.put("externalemail", accessLog.getExternalEmail());
			return mapping;
		}
	};

	/**
	 * Returns the actions made by a <b>username</b> over the <b>patient</b>
	 * 
	 * @param pid
	 *            Mandatory
	 * @param appuser
	 *            Mandatory
	 * @param orderBy
	 *            logdt as default
	 * @param sortOrder
	 *            ASC as default
	 * @param limit
	 *            50 as default
	 * @param offset
	 *            0 as default
	 * @return A {@link List} with the Actions
	 * @throws InvalidParameterException
	 *             If pid or username are null or empty
	 */
	public List<PatientAccessLog> findByPidAndAppuser(String pid, String appuser, String orderBy, String sortOrder, Integer limit, Integer offset) throws InvalidParameterException {

		if (pid == null || pid.isEmpty() || appuser == null || appuser.isEmpty()) {
			throw new InvalidParameterException();
		}

		String validOrder = PatientAccessLogOrderByEnum.permissiveValueOf(orderBy).getValue();
		if (validOrder.isEmpty()) {
			validOrder = defaultResultAccessLogListOrderBy;
		}

		String validSortOrder = PatientAccessLogShortOrderEnum.permissiveValueOf(sortOrder).getValue();
		if (validSortOrder.isEmpty()) {
			validSortOrder = defaultResultAccessLogListSortOrder;
		}

		if (limit == null || limit < 0) {
			limit = defaultResultLimit;
		}

		if (offset == null || offset < 0) {
			offset = defaultResultOffset;
		}

		List<PatientAccessLog> logs = getJdbcOperations().query(SQLStatements.FIND_PATIENT_ACTIONS_LOG_BY_USERNAME.replace("ORDER BY ?", "ORDER BY " + validOrder + " " + validSortOrder), ROW_MAPPER, pid, appuser, limit, offset);

		return logs;
	}
	
	/**
	 * Returns the actions made by a <b>username</b> with <b>externalUser</b> over the <b>patient</b>
	 * 
	 * @param pid
	 * @param appuser
	 * @param externalUser
	 * @param orderBy
	 * @param sortOrder
	 * @param limit
	 * @param offset
	 * @return
	 * @throws InvalidParameterException
	 */
	public List<PatientAccessLog> findByPidAndAppuserAndExternalUser(String pid, String appuser, String externalUser, String orderBy, String sortOrder, Integer limit, Integer offset) throws InvalidParameterException {

		if (pid == null || pid.isEmpty() || appuser == null || appuser.isEmpty() || externalUser == null || externalUser.isEmpty()) {
			throw new InvalidParameterException();
		}

		String validOrder = PatientAccessLogOrderByEnum.permissiveValueOf(orderBy).getValue();
		if (validOrder.isEmpty()) {
			validOrder = defaultResultAccessLogListOrderBy;
		}

		String validSortOrder = PatientAccessLogShortOrderEnum.permissiveValueOf(sortOrder).getValue();
		if (validSortOrder.isEmpty()) {
			validSortOrder = defaultResultAccessLogListSortOrder;
		}

		if (limit == null || limit < 0) {
			limit = defaultResultLimit;
		}

		if (offset == null || offset < 0) {
			offset = defaultResultOffset;
		}

		List<PatientAccessLog> logs = getJdbcOperations().query(SQLStatements.FIND_PATIENT_ACTIONS_LOG_BY_USERNAME_AND_EXTERNALUSER.replace("ORDER BY ?", "ORDER BY " + validOrder + " " + validSortOrder), ROW_MAPPER, pid, appuser, externalUser, limit, offset);

		return logs;
	}

	/**
	 * Returns the number of actions made by a <b>username</b> over the <b>patients</b> elements
	 * 
	 * @param pid
	 * @param appuser
	 * @return
	 * @throws InvalidParameterException
	 *             If pid or username are null or empty
	 */
	public Count findByPidAndAppuserCount(String pid, String appuser) throws InvalidParameterException {

		if (pid == null || pid.isEmpty() || appuser == null || appuser.isEmpty() ) {
			throw new InvalidParameterException();
		}

		Integer count = getJdbcOperations().queryForObject(SQLStatements.FIND_PATIENT_ACTIONS_LOG_BY_USERNAME_COUNT, Integer.class, pid, appuser);

		return new Count(count);
	}
	
	/**
	 * Returns the number of actions made by a <b>username</b> with <b>externalUser</b> over the <b>patients</b> elements
	 * 
	 * @param pid
	 * @param appuser
	 * @param externalUser
	 * @return
	 * @throws InvalidParameterException
	 */
	public Count findByPidAndAppuserAndExternalUserCount(String pid, String appuser, String externalUser) throws InvalidParameterException {

		if (pid == null || pid.isEmpty() || appuser == null || appuser.isEmpty() || externalUser == null || externalUser.isEmpty()) {
			throw new InvalidParameterException();
		}

		Integer count = getJdbcOperations().queryForObject(SQLStatements.FIND_PATIENT_ACTIONS_LOG_BY_USERNAME_AND_EXTERNALUSER_COUNT, Integer.class, pid, appuser, externalUser);

		return new Count(count);
	}

	@Override
	protected JdbcOperations getJdbcOperations() {
		return new JdbcTemplate(ehlogdataSource);
	}
}
