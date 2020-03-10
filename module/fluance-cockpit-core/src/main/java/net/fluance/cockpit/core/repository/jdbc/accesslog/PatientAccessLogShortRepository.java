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

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.accesslog.PatientAccessLogShort;
import net.fluance.cockpit.core.util.sql.PatientAccessLogOrderByEnum;
import net.fluance.cockpit.core.util.sql.PatientAccessLogShortOrderEnum;

/**
 * This repository request the table ehealth.bmv_user_access_list always grouping by appuser and logdt. The expected result is only a row for user and day.<br>
 * Will map the results in an object {@link PatientAccessLogShort}
 *
 */
@Repository
@Component
public class PatientAccessLogShortRepository extends JdbcRepository<PatientAccessLogShort, Integer> {

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

	public PatientAccessLogShortRepository() {
		this(MappingsConfig.TABLE_NAMES.get("UserAccess"));
	}

	public PatientAccessLogShortRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "id"));
	}

	public static final RowMapper<PatientAccessLogShort> ROW_MAPPER = new RowMapper<PatientAccessLogShort>() {

		public PatientAccessLogShort mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new PatientAccessLogShort(rs.getTimestamp("logdt"), rs.getString("appuser"),rs.getString("firstname"), rs.getString("lastname"),
					rs.getString("externaluser"), rs.getString("externalfirstname"), rs.getString("externallastname"), rs.getString("externalemail"));
		}
	};

	public static final RowUnmapper<PatientAccessLogShort> ROW_UNMAPPER = new RowUnmapper<PatientAccessLogShort>() {

		public Map<String, Object> mapColumns(PatientAccessLogShort accessLog) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("logdt", accessLog.getLogDate());
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
	 * Find the last log from a user for a patient. <b>Only one row by user</b>
	 * 
	 * @param pid
	 * @param orderby
	 * @param sortorder
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<PatientAccessLogShort> findByPid(String pid, String orderby, String sortorder, Integer limit, Integer offset) {

		if (pid == null || pid.isEmpty()) {
			throw new InvalidParameterException("Patient ID can't be null");
		}

		String validSortOrder = PatientAccessLogShortOrderEnum.permissiveValueOf(sortorder).getValue();
		if (validSortOrder.isEmpty()) {
			validSortOrder = defaultResultAccessLogListSortOrder;
		}		
		
		PatientAccessLogOrderByEnum orderbyEnum = PatientAccessLogOrderByEnum.permissiveValueOf(orderby);
		String orderbyValue = orderbyEnum.getValue();
		if (orderbyValue == null || orderbyValue.isEmpty()) {
			orderby = defaultResultAccessLogListOrderBy + " " + validSortOrder;
		} else {
			orderby = orderbyEnum.getValueWithSortOrder(validSortOrder);
		}
		
		if (limit == null) {
			limit = defaultResultLimit;
		}
				
		if (offset == null) {
			offset = defaultResultOffset;
		}

		List<PatientAccessLogShort> logs = getJdbcOperations().query(SQLStatements.FIND_PATIENT_ACCESS_LOGS_BY_PID.replace("order by ?", "order by " + orderby), ROW_MAPPER, pid, limit, offset);

		return logs;
	}

	/**
	 * Count the last log from a user for a patient
	 * 
	 * @param pid
	 * @return
	 */
	public Integer findByPidCount(String pid) {

		if (pid == null || pid.isEmpty()) {
			throw new InvalidParameterException("Patient ID can't be null");
		}

		return getJdbcOperations().queryForObject(SQLStatements.FIND_PATIENT_ACCESS_LOGS_BY_PID_COUNT, Integer.class, pid);
	}

	@Override
	protected JdbcOperations getJdbcOperations() {
		return new JdbcTemplate(ehlogdataSource);
	}
}
