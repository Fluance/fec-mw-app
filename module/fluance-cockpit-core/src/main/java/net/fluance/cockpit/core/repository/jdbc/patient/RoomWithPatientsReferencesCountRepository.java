package net.fluance.cockpit.core.repository.jdbc.patient;

import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.patient.byroom.PatientsInRoomCount;
import net.fluance.cockpit.core.model.jdbc.patient.byroom.RoomWithPatientsReferencesCount;

@Repository
@Component
public class RoomWithPatientsReferencesCountRepository extends JdbcRepository<PatientsInRoomCount, Integer> {
	
	public RoomWithPatientsReferencesCountRepository() {
		super(ROW_MAPPER, ROW_UNMAPPER, MappingsConfig.TABLE_NAMES.get("PatientList"));
	}

	public static final RowMapper<PatientsInRoomCount> ROW_MAPPER = new RowMapper<PatientsInRoomCount>() {
		public PatientsInRoomCount mapRow(ResultSet rs, int rowNum) throws SQLException {
			Integer roomCount = rs.getInt("room_count");
			String patientRoom = rs.getString("patientroom");
			
			return new PatientsInRoomCount(roomCount, patientRoom);
		}
	};

	private static final RowUnmapper<PatientsInRoomCount> ROW_UNMAPPER = new RowUnmapper<PatientsInRoomCount>() {
		public Map<String, Object> mapColumns(PatientsInRoomCount patient) {
			return null;
		}
	};
	
	public List<RoomWithPatientsReferencesCount> getRoomWithPatientsReferencesCount(Integer companyId, String hospService, String unit, String hl7code, String sortOrder) throws InvalidParameterException {
		if (companyId == null) {
			throw new InvalidParameterException();
		}
		
		List<Object> params = new ArrayList<Object>();
		
		params.add(companyId);
	
		String queryFragment = initQueryParams(hospService, unit, hl7code, params);
	
		String query = String.format(SQLStatements.FIND_ROOMS_WITH_PATIENTS_REFERENCES_COUNT,
				queryFragment,
				sortOrder);
	
		List<PatientsInRoomCount> rows = getJdbcOperations().query(query, ROW_MAPPER, params.toArray());
		
		List<RoomWithPatientsReferencesCount> rooms = new ArrayList<RoomWithPatientsReferencesCount>();
		
		for (PatientsInRoomCount row : rows) {
			rooms.add(new RoomWithPatientsReferencesCount(row.getPatientRoom(), row.getRoomCount()));			
		}
		
		return rooms;
	}
	
	/**
	 * Return the query fragment for the parameters and add it to @param params
	 * 
	 * @param hospService
	 * @param unit
	 * @param hl7code
	 * @param params
	 * @return
	 */
	private String initQueryParams(String hospService, String unit, String hl7code, List<Object> params) {
		StringBuffer query = new StringBuffer(60);
		
		if(unit != null) {
			query.append("AND patientunit=? ");
			params.add(unit);
		}
		
		if(hospService != null) {
			query.append("AND hospservice=? ");
			params.add(hospService);
		}

		if(hl7code != null) {
			query.append("AND hl7code=? ");
			params.add(hl7code);
		}
		
		return query.toString();
	}
}