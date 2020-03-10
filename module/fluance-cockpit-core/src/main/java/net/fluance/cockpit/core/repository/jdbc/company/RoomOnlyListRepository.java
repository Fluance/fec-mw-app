/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import net.fluance.cockpit.core.model.jdbc.company.RoomOnlyList;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class RoomOnlyListRepository extends JdbcRepository<RoomOnlyList, Integer> {

	public RoomOnlyListRepository() {
		this(MappingsConfig.TABLE_NAMES.get("m_bmv_patientroom"));
	}

	public RoomOnlyListRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, ""));
	}

	public static final RowMapper<RoomOnlyList> ROW_MAPPER = new RowMapper<RoomOnlyList>() {
		public RoomOnlyList mapRow(ResultSet rs, int rowNum) throws SQLException{
			return new RoomOnlyList(rs.getString("patientroom"));
		}
	};

	public static final RowUnmapper<RoomOnlyList> ROW_UNMAPPER = new RowUnmapper<RoomOnlyList>() {
		public Map<String, Object> mapColumns(RoomOnlyList patientroom) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("patientroom", patientroom.getRoom());
			return mapping;
		}
	};

	public List<RoomOnlyList> findByCompanyIdAndPatientunit(int companyId, String patientunit, List<String> excluderooms) {
		List<RoomOnlyList> roomOnlyList= new ArrayList<RoomOnlyList>();
		
		if(excluderooms != null && !excluderooms.isEmpty()){
			String query = addExcludeRoomsCriteria(SQLStatements.FIND_ROOM_LIST_BY_COMPANYID_PATIENTUNIT_AND_EXCLUDEROOMS, excluderooms);
			return getJdbcOperations().query(query, ROW_MAPPER, companyId, patientunit);
		} else {
			roomOnlyList = getJdbcOperations().query(SQLStatements.FIND_SERVICE_ROOM_LIST_BY_COMPANYID_AND_PATIENTUNIT, ROW_MAPPER, companyId, patientunit);
		}
		return roomOnlyList;
	}

	public List<RoomOnlyList> findByCompanyIdAndHospservice(int companyId, String hospservice, List<String> excluderooms) {
		List<RoomOnlyList> roomOnlyList = new ArrayList<RoomOnlyList>(); 
		
		if(excluderooms != null && !excluderooms.isEmpty()){
			String query = addExcludeRoomsCriteria(SQLStatements.FIND_ROOM_LIST_BY_COMPANYID_HOSPSERVICE_AND_EXCLUDEROOMS, excluderooms);
			return getJdbcOperations().query(query, ROW_MAPPER, companyId, hospservice);			
		} else {
			roomOnlyList = getJdbcOperations().query(SQLStatements.FIND_SERVICE_ROOM_LIST_BY_COMPANYID_AND_HOSPSERVICE, ROW_MAPPER, companyId, hospservice);			
		}
		
		return roomOnlyList;
	}

	public List<RoomOnlyList> findByCompanyIdAndPatientunitAndHospservice(int companyId, String patientunit, String hospservice, List<String> excluderooms) {
		if(excluderooms != null && !excluderooms.isEmpty()){
			String query = addExcludeRoomsCriteria(SQLStatements.FIND_ROOM_LIST_BY_COMPANYID_AND_PATIENTUNIT_HOSPSERVICE_AND_EXCLUDEROOMS, excluderooms);
			return getJdbcOperations().query(query, ROW_MAPPER, companyId, patientunit, hospservice);
		} else {
			return getJdbcOperations().query(SQLStatements.FIND_SERVICE_ROOM_LIST_BY_COMPANYID_AND_PATIENTUNIT_AND_HOSPSERVICE, ROW_MAPPER, companyId, patientunit, hospservice);			
		}
	}

	private String addExcludeRoomsCriteria(String query, List<String> rooms){
		String roomsToExclude = SqlUtils.toIn(rooms);
		return query.replace("(?)", roomsToExclude);
	}
}
