/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.lab;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.lab.LabData;

@Repository
@Component
public class LabDataRepository extends JdbcRepository<LabData, Integer> {

	public LabDataRepository() {
		this(MappingsConfig.TABLE_NAMES.get("labdata_list"));
	}

	public LabDataRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, ""));
	}

	public static String Json(String jsontoMerge) {
		String msg = null;
		if (jsontoMerge != null) {
			JSONObject jsonObj = new JSONObject(jsontoMerge);

			int i = 0;

			while (i < jsonObj.length()) {
				msg += jsonObj.get(Integer.toString(i + 1));
				i++;
			}
		}
		return msg;
	}

	public static final RowMapper<LabData> ROW_MAPPER = new RowMapper<LabData>() {
		public LabData mapRow(ResultSet rs, int rowNum) throws SQLException {
			try {
				return new LabData(rs.getLong("patient_id"), rs.getString("groupname"), rs.getTimestamp("observationdt"),
						rs.getString("analysisname"), rs.getString("value"), rs.getString("valuetype"),
						rs.getString("unit"), rs.getString("refrange"), rs.getString("abnormalflag"),
						rs.getString("abnormalflagdesc"), rs.getString("resultstatus"), rs.getString("resultstatusdesc"),
						(rs.getString("comments") != null) ? new ObjectMapper().readTree(rs.getString("comments")) : null);
			} catch (JsonProcessingException e) {
				throw new SQLException("Could not map " + rs.getString("comments") + " to JsonNode");
			} catch (IOException e) {
				throw new SQLException("Could not map " + rs.getString("comments") + " to JsonNode");
			}
		}
	};

	public static final RowUnmapper<LabData> ROW_UNMAPPER = new RowUnmapper<LabData>() {
		public Map<String, Object> mapColumns(LabData labdata) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("patient_id", labdata.getPatientId());
			mapping.put("groupname", labdata.getGroupName());
			mapping.put("observationdt", labdata.getObservationdt());
			mapping.put("analysisname", labdata.getAnalysisname());
			mapping.put("value", labdata.getValue());
			mapping.put("valuetype", labdata.getValuetype());
			mapping.put("unit", labdata.getUnit());
			mapping.put("refrange", labdata.getRefrange());
			mapping.put("abnormalflag", labdata.getAbnormalflag());
			mapping.put("abnormalflagdesc", labdata.getAbnormalflagdesc());
			mapping.put("resultstatus", labdata.getResultstatus());
			mapping.put("resultstatusdesc", labdata.getResultstatusdesc());
			// mapping.put("comments", labdata.getComments());

			return mapping;
		}
	};

	public List<LabData> findByPidAndGroupName(long patientid, String groupname) {
		List<LabData> lLabdata = getJdbcOperations().query(SQLStatements.FIND_LABDATA_BY_GROUPNAME_AND_PATIENTID,
				ROW_MAPPER, patientid, groupname);
		return lLabdata;
	}

	public Integer findByPidCount(long patientid) {
		return getJdbcOperations().queryForObject(SQLStatements.FIND_LABDATA_BY_PATIENTID_COUNT, Integer.class, patientid);
	}

}
