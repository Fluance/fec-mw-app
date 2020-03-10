package net.fluance.cockpit.core.repository.jdbc.patient;

import java.security.InvalidParameterException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.PatientReference;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.patient.byroom.PatientsInRoom;
import net.fluance.cockpit.core.model.jdbc.patient.byroom.RoomWithPatientsReferences;

@Repository
@Component
public class RoomWithPatientsReferencesRepository extends JdbcRepository<PatientsInRoom, Integer> {

	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;
	
	public RoomWithPatientsReferencesRepository() {
		super(ROW_MAPPER, ROW_UNMAPPER, MappingsConfig.TABLE_NAMES.get("PatientList"));
	}

	public static final RowMapper<PatientsInRoom> ROW_MAPPER = new RowMapper<PatientsInRoom>() {
		public PatientsInRoom mapRow(ResultSet rs, int rowNum) throws SQLException {
			Integer roomCount = rs.getInt("room_count");
			String patientRoom = rs.getString("patientroom");
			Long patientId = rs.getLong("patient_id");
			String lastName = rs.getString("lastname");
			String firstName = rs.getString("firstname");
			String maidenName = rs.getString("maidenname");
			Date birthDate = rs.getDate("birthdate");
			
			return new PatientsInRoom(roomCount, patientRoom, patientId, lastName, firstName, maidenName, birthDate);
		}
	};

	private static final RowUnmapper<PatientsInRoom> ROW_UNMAPPER = new RowUnmapper<PatientsInRoom>() {
		public Map<String, Object> mapColumns(PatientsInRoom patient) {
			return null;
		}
	};
	
	public List<RoomWithPatientsReferences> getRoomWithPatientsReferences(Integer companyId, String hospService, String unit, String hl7code, String sortOrder, Integer limit, Integer offset, Integer maxPatientsByRoom) throws InvalidParameterException {	
		
		if (companyId == null) {
			throw new InvalidParameterException();
		}
		
		if (maxPatientsByRoom == null) {
			throw new InvalidParameterException();
		}
		
		if (sortOrder == null || sortOrder.isEmpty() || (!"asc".equals(sortOrder) && !"desc".equals(sortOrder))) {
			sortOrder = "asc";
		}

		if (limit == null || limit < 0) {
			limit = defaultResultLimit;
		}

		if (offset == null || offset < 0) {
			offset = defaultResultOffset;
		}
		
		Map<String, RoomWithPatientsReferences> patientReferencesByRooms;
		
		//TreeMap class is sorted by itself so depending on the sort direction the sort order is set
		if(sortOrder !=null &&  "desc".equals(sortOrder)) {
			patientReferencesByRooms = new TreeMap<String, RoomWithPatientsReferences>(Collections.reverseOrder());
		} else {
			patientReferencesByRooms = new TreeMap<String, RoomWithPatientsReferences>();
		}
		
		List<Object> params = new ArrayList<Object>();
		
		params.add(companyId);
	
		String queryFragment = initQueryParams(hospService, unit, hl7code, params);
		
		String query = String.format(SQLStatements.FIND_ROOMS_WITH_PATIENTS_REFERENCES,
				queryFragment,
				sortOrder,
				limit,
				offset);
		
		params.add(maxPatientsByRoom);
		
		List<PatientsInRoom> rows = getJdbcOperations().query(query, ROW_MAPPER, params.toArray());		
		
		for (PatientsInRoom row : rows) {
			
			// Create a new patientRef
			PatientReference currentPatientReference = new PatientReference(row.getPatientId(), row.getFirstName(), row.getLastName(), row.getMaidenName(), row.getBirthDate());
			
			// Find the corresponding entry in the HashMap
			RoomWithPatientsReferences roomWithPatientsReferences = patientReferencesByRooms.get(row.getPatientRoom());
			
			// If not found, then we create a new one
			if (roomWithPatientsReferences == null) {
				roomWithPatientsReferences = new RoomWithPatientsReferences(row.getPatientRoom(), row.getRoomCount(), new ArrayList<PatientReference>());
				patientReferencesByRooms.put(row.getPatientRoom(), roomWithPatientsReferences);
			}
			
			List<PatientReference> currentRoomPatientReferences = roomWithPatientsReferences.getPatients();
			
			currentRoomPatientReferences.add(currentPatientReference);
			
		}
		return new ArrayList<RoomWithPatientsReferences>(patientReferencesByRooms.values());
		
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