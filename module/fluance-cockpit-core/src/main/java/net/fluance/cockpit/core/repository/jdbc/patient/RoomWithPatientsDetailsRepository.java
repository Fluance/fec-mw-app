package net.fluance.cockpit.core.repository.jdbc.patient;

import java.security.InvalidParameterException;
import java.sql.Date;
import java.sql.Timestamp;
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
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.patient.byroom.PatientsDetailsInRoom;
import net.fluance.cockpit.core.model.jdbc.patient.byroom.RoomWithPatientsDetails;
import net.fluance.cockpit.core.model.wrap.patient.PatientInList;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class RoomWithPatientsDetailsRepository extends JdbcRepository<PatientsDetailsInRoom, Integer> {

	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;
		
	public RoomWithPatientsDetailsRepository() {
		super(ROW_MAPPER, ROW_UNMAPPER, MappingsConfig.TABLE_NAMES.get("PatientList"));
	}

	public static final RowMapper<PatientsDetailsInRoom > ROW_MAPPER = (resultSet, rowNumber) -> {
			Integer roomCount = resultSet.getInt("room_count");
			String patientRoom = resultSet.getString("patientroom");
			Long pid = SqlUtils.getLong(true, resultSet, "patient_id");
			String firstname = resultSet.getString("firstname");
			String lastname = resultSet.getString("lastname");
			String maidenName = resultSet.getString("maidenname");
			Date birthDate = resultSet.getDate("birthdate");
			String sex = resultSet.getString("sex");
			Boolean death = resultSet.getBoolean("death");
			Date deathdt = resultSet.getDate("deathdt");
			String address = resultSet.getString("address");
			String locality = resultSet.getString("locality");
			String postCode = resultSet.getString("postcode");
			Long visitNumber = SqlUtils.getLong(true, resultSet, "nb");
			Long companyId = SqlUtils.getLong(true, resultSet, "company_id");
			Timestamp admitDate = resultSet.getTimestamp("admitdt");
			Timestamp dischargeDate = resultSet.getTimestamp("dischargedt");
			String patientClass = resultSet.getString("patientclass");
			String patientType = resultSet.getString("patienttype");
			String patientCase = resultSet.getString("patientcase");
			String hospService = resultSet.getString("hospservice");
			String admissionType = resultSet.getString("admissiontype");
			String financialClass = resultSet.getString("financialclass");
			String patientUnit = resultSet.getString("patientunit");
			String patientBed = resultSet.getString("patientbed");
			String patientClassDesc = resultSet.getString("patientclassdesc");
			String patientTypeDesc = resultSet.getString("patienttypedesc");
			String patientCaseDesc = resultSet.getString("patientcasedesc");
			String hospServiceDesc = resultSet.getString("hospservicedesc");
			String admissionTypeDesc = resultSet.getString("admissiontypedesc");
			String financialClassDesc = resultSet.getString("financialclassdesc");
			String patientUnitDesc = resultSet.getString("patientunitdesc");
			
			return new PatientsDetailsInRoom(roomCount, patientRoom, pid, firstname, lastname, maidenName, birthDate, sex, death, deathdt, address, locality, postCode, visitNumber, companyId, admitDate, dischargeDate, patientClass, patientClassDesc, patientType, patientTypeDesc,
					patientCase, patientCaseDesc, hospService, hospServiceDesc, admissionType, admissionTypeDesc,
					patientUnit, patientUnitDesc, patientBed, financialClass, financialClassDesc);
		};

	private static final RowUnmapper<PatientsDetailsInRoom> ROW_UNMAPPER = patient -> null;
	
	public List<RoomWithPatientsDetails> getRoomWithPatientsDetails(Integer companyId, String hospService, String unit, String hl7code, String sortOrder, Integer limit, Integer offset, Integer maxPatientsByRoom) throws InvalidParameterException {
		
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
		
		//TreeMap class is sorted by itself so depending on the sort direction the sort order is set
		Map<String, RoomWithPatientsDetails> patientReferencesByRooms = new TreeMap<String, RoomWithPatientsDetails>();
		
		if(sortOrder !=null &&  "desc".equals(sortOrder)) {
			patientReferencesByRooms = new TreeMap<String, RoomWithPatientsDetails>(Collections.reverseOrder());
		} else {
			patientReferencesByRooms = new TreeMap<String, RoomWithPatientsDetails>();
		}
		
		List<Object> params = new ArrayList<Object>();
		
		params.add(companyId);
	
		String queryFragment = initQueryParams(hospService, unit, hl7code, params);
		
		String query = String.format(SQLStatements.FIND_ROOMS_WITH_PATIENTS_DETAILS,
				queryFragment,
				sortOrder,
				limit,
				offset);
		
		params.add(maxPatientsByRoom);
		
		List<PatientsDetailsInRoom> rows = getJdbcOperations().query(query, ROW_MAPPER, params.toArray());		
		
		for (PatientsDetailsInRoom row : rows) {
			// Create a new PatientInList wrap that has the patient detail information
			PatientInList currentPatientReference = new PatientInList(null, row.getPid(), row.getFirstName(), row.getLastName(), row.getMaidenName(), row.getBirthDate(),		
					row.getSex(), row.isDeath(), row.getDeathdt(), row.getAddress(), row.getLocality(), row.getPostCode(), row.getLastVisitnumber(), row.getLastVisitCompanyId(),
					row.getLastVisitAdmitDate(), row.getLastVisitDischargeDate(), row.getLastVisitPatientClass(), row.getLastVisitPatientClassDesc(), row.getLastVisitPatientType(),
					row.getLastVisitPatientTypeDesc(), row.getLastVisitPatientCase(), row.getLastVisitPatientCaseDesc(), row.getLastVisitHospService(), row.getLastVisitHospServiceDesc(),
					row.getLastVisitAdmissionType(), row.getLastVisitAdmissionTypeDesc(), row.getLastVisitPatientUnit(), row.getLastVisitPatientUnitDesc(), row.getLastVisitPatientRoom(),
					row.getLastVisitPatientBed(), row.getLastVisitFinancialClass(), row.getLastVisitFinancialClassDesc());

			// Find the corresponding entry in the HashMap
			RoomWithPatientsDetails roomWithPatientsReferences = patientReferencesByRooms.get(row.getPatientRoom());
			
			// If not found, then we create a new one
			if (roomWithPatientsReferences == null) {
				roomWithPatientsReferences = new RoomWithPatientsDetails(row.getPatientRoom(), row.getRoomCount(), new ArrayList<PatientInList>());
				patientReferencesByRooms.put(row.getPatientRoom(), roomWithPatientsReferences);
			}
			
			List<PatientInList> currentRoomPatientReferences = roomWithPatientsReferences.getPatients();
			
			currentRoomPatientReferences.add(currentPatientReference);
			
		}
		return new ArrayList<RoomWithPatientsDetails>(patientReferencesByRooms.values());
		
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