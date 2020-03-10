/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.company.RoomList;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class RoomListRepository extends JdbcRepository<RoomList, Integer> {

	public RoomListRepository() {
		this(MappingsConfig.TABLE_NAMES.get("m_bmv_patientroom"));
	}

	public RoomListRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, ""));
	}

	public static final RowMapper<RoomList> ROW_MAPPER = new RowMapper<RoomList>() {
		public RoomList mapRow(ResultSet rs, int rowNum) throws SQLException{
			return new RoomList(rs.getString("patientroom"));
		}
	};

	public static final RowMapper<RoomList> ROW_MAPPER_WITHCOUNT = new RowMapper<RoomList>() {
		public RoomList mapRow(ResultSet rs, int rowNum) throws SQLException{
			return new RoomList(rs.getString("patientroom"), SqlUtils.getInt(true, rs, "nb_patients"));
		}
	};

	public static final RowUnmapper<RoomList> ROW_UNMAPPER = new RowUnmapper<RoomList>() {
		public Map<String, Object> mapColumns(RoomList patientroom) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("patientroom", patientroom.getPatientroom());
			return mapping;
		}
	};

	public List<RoomList> findByCompanyIdAndPatientunit(int companyId, String patientunit, List<String> excluderooms) {
		if(excluderooms != null && !excluderooms.isEmpty()){
			String query = addExcludeRoomsCriteria(SQLStatements.FIND_SERVICE_ROOM_LIST_BY_COMPANYID_AND_PATIENTUNIT_AND_EXCLUDEROOMS, excluderooms);
			return getJdbcOperations().query(query, ROW_MAPPER, companyId, patientunit);
		}
		else return getJdbcOperations().query(SQLStatements.FIND_SERVICE_ROOM_LIST_BY_COMPANYID_AND_PATIENTUNIT, ROW_MAPPER, companyId, patientunit);
	}

	public List<RoomList> findByCompanyIdAndHospservice(int companyId, String hospservice, List<String> excluderooms) {
		if(excluderooms != null && !excluderooms.isEmpty()){
			String query = addExcludeRoomsCriteria(SQLStatements.FIND_SERVICE_ROOM_LIST_BY_COMPANYID_AND_HOSPSERVICE_AND_EXCLUDEROOMS, excluderooms);
			return getJdbcOperations().query(query, ROW_MAPPER, companyId, hospservice);
		}
		else return getJdbcOperations().query(SQLStatements.FIND_SERVICE_ROOM_LIST_BY_COMPANYID_AND_HOSPSERVICE, ROW_MAPPER, companyId, hospservice);
	}

	public List<RoomList> findByCompanyIdAndPatientunitAndHospservice(int companyId, String patientunit, String hospservice, List<String> excluderooms) {
		if(excluderooms != null && !excluderooms.isEmpty()){
			String query = addExcludeRoomsCriteria(SQLStatements.FIND_SERVICE_ROOM_LIST_BY_COMPANYID_AND_PATIENTUNIT_AND_HOSPSERVICE_AND_EXCLUDEROOMS, excluderooms);
			return getJdbcOperations().query(query, ROW_MAPPER, companyId, patientunit, hospservice);
		}
		else return getJdbcOperations().query(SQLStatements.FIND_SERVICE_ROOM_LIST_BY_COMPANYID_AND_PATIENTUNIT_AND_HOSPSERVICE, ROW_MAPPER, companyId, patientunit, hospservice);
	}

	public List<RoomList> findByCompanyIdAndPatientunitIncludeCount(int companyId, String patientunit, String patientclass, List<String> excluderooms) {
		if(excluderooms != null && !excluderooms.isEmpty()){
			if(patientclass != null && !patientclass.isEmpty()) {
				String query = addExcludeRoomsCriteria(SQLStatements.FIND_PATIENT_COUNT_BY_COMPANYID_AND_PATIENTUNIT_AND_EXCLUDEROOMS_INOUT, excluderooms);
				return getJdbcOperations().query(query, ROW_MAPPER_WITHCOUNT, patientunit, patientclass, companyId);
			} else {
				String query = addExcludeRoomsCriteria(SQLStatements.FIND_PATIENT_COUNT_BY_COMPANYID_AND_PATIENTUNIT_AND_EXCLUDEROOMS, excluderooms);
				return getJdbcOperations().query(query, ROW_MAPPER_WITHCOUNT, patientunit, companyId);
			}
		} else {
			if(patientclass != null && !patientclass.isEmpty()) {
				return getJdbcOperations().query(SQLStatements.FIND_PATIENT_COUNT_BY_COMPANYID_AND_PATIENTUNIT_INOUT, ROW_MAPPER_WITHCOUNT, patientunit, patientclass, companyId);
			} else {
				return getJdbcOperations().query(SQLStatements.FIND_PATIENT_COUNT_BY_COMPANYID_AND_PATIENTUNIT, ROW_MAPPER_WITHCOUNT, patientunit, companyId);
			}
		}
	}

	public List<RoomList> findByCompanyIdAndHospserviceIncludeCount(int companyId, String hospservice, String patientclass, List<String> excluderooms) {
		if(excluderooms != null && !excluderooms.isEmpty()){
			if(patientclass != null && !patientclass.isEmpty()) {
				String query = addExcludeRoomsCriteria(SQLStatements.FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE_AND_EXCLUDEROOMS_INOUT, excluderooms);
				return getJdbcOperations().query(query, ROW_MAPPER_WITHCOUNT, hospservice, patientclass, companyId);
			} else {
				String query = addExcludeRoomsCriteria(SQLStatements.FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE_AND_EXCLUDEROOMS, excluderooms);
				return getJdbcOperations().query(query, ROW_MAPPER_WITHCOUNT, hospservice, companyId);
			}
			
		} else {
			if(patientclass != null && !patientclass.isEmpty()) {
				return getJdbcOperations().query(SQLStatements.FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE_INOUT, ROW_MAPPER_WITHCOUNT, hospservice, patientclass, companyId);
			} else {
				return getJdbcOperations().query(SQLStatements.FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE, ROW_MAPPER_WITHCOUNT, hospservice, companyId);
			}
		}
	}

	public List<RoomList> findByCompanyIdAndPatientunitAndHospserviceIncludeCount(int companyId, String patientunit, String patientclass, String hospservice, List<String> excluderooms) {
		if(excluderooms != null && !excluderooms.isEmpty()){
			String query = "";
			if(patientclass != null && !patientclass.isEmpty()) {
				query = addExcludeRoomsCriteria(SQLStatements.FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE_AND_PATIENTUNIT_AND_EXCLUDEROOMS_INOUT, excluderooms);
				return getJdbcOperations().query(query, ROW_MAPPER_WITHCOUNT, hospservice, patientunit, patientclass, companyId);
			} else {
				query = addExcludeRoomsCriteria(SQLStatements.FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE_AND_PATIENTUNIT_AND_EXCLUDEROOMS, excluderooms);
				return getJdbcOperations().query(query, ROW_MAPPER_WITHCOUNT, hospservice, patientunit, companyId);				
			}
		} else {
			if(patientclass != null && !patientclass.isEmpty()) {
				return getJdbcOperations().query(SQLStatements.FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE_AND_PATIENTUNIT_INOUT, ROW_MAPPER_WITHCOUNT, hospservice, patientunit, patientclass, companyId);
			} else {
				return getJdbcOperations().query(SQLStatements.FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE_AND_PATIENTUNIT, ROW_MAPPER_WITHCOUNT, hospservice, patientunit, companyId);
			}
		}
	}

	private String addExcludeRoomsCriteria(String query, List<String> rooms){
		String roomsToExclude = SqlUtils.toIn(rooms);
		return query.replace("(?)", roomsToExclude);
	}
}
