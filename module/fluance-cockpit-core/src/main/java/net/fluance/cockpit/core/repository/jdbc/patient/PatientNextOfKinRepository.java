/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.patient;

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

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.jdbc.patient.PatientNextOfKin;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class PatientNextOfKinRepository extends JdbcRepository<PatientNextOfKin, Long> {

	/**
	 * 
	 */
	public PatientNextOfKinRepository() {
		super(ROW_MAPPER, ROW_UNMAPPER, MappingsConfig.TABLE_NAMES.get("PatientNextOfKin"));
	}

	/**
	 * 
	 */
	public static final RowMapper<PatientNextOfKin> ROW_MAPPER = new RowMapper<PatientNextOfKin>() {
		public PatientNextOfKin mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long id = SqlUtils.getLong(true, rs, "id");
			Integer nbRecords = SqlUtils.getInt(true, rs, "nb_records");
			String firstName = rs.getString("firstname");
			String lastName = rs.getString("lastname");
			
			Long patientId = SqlUtils.getLong(true, rs, "patient_id");
			String addressType = rs.getString("addresstype");
			String address = rs.getString("address");
			String address2 = rs.getString("address2");
			String complement = rs.getString("complement");
			String locality = rs.getString("locality");
			String postCode = rs.getString("postcode");
			String canton = rs.getString("canton");
			String country = rs.getString("country");
			String type = rs.getString("type");
			String careOf = rs.getString("careof");
			String equipment = rs.getString("equipment");
			String data = rs.getString("data");
			
			PatientNextOfKin nok = new PatientNextOfKin(nbRecords, id, firstName, lastName, patientId,
					addressType, address, address2, complement, locality, postCode,
					canton, country, type, careOf, equipment, data);
			return nok;
		}
	};

	/**
	 * 
	 */
	private static final RowUnmapper<PatientNextOfKin> ROW_UNMAPPER = new RowUnmapper<PatientNextOfKin>() {
		public Map<String, Object> mapColumns(PatientNextOfKin nok) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("id", nok.getId());
			mapping.put("nb_records", nok.getNbRecords());
			mapping.put("firstname", nok.getFirstName());
			mapping.put("lastname", nok.getLastName());
			mapping.put("patient_id", nok.getPatientId());
			mapping.put("addresstype", nok.getAddressType());
			mapping.put("type", nok.getType());
			mapping.put("address", nok.getAddress());
			mapping.put("address2", nok.getAddress2());
			mapping.put("complement", nok.getComplement());
			mapping.put("locality", nok.getLocality());
			mapping.put("postcode", nok.getPostCode());
			mapping.put("canton", nok.getCanton());
			mapping.put("country", nok.getCountry());
			mapping.put("careof", nok.getCareOf());
			mapping.put("equipment", nok.getEquipment());
			mapping.put("data", nok.getData());
			return mapping;
		}
	};
	
	/**
	 * Return a List of Kins of the Patient ID given
	 * @param pid The Patient ID
	 * @return
	 * 	The List of Kins
	 */
	public List<PatientNextOfKin> findByPid(Long pid) {
		List<PatientNextOfKin> patientNoks = getJdbcOperations().query(PatientSQLQueries.FIND_PATIENT_NEXTOFKINS, ROW_MAPPER, pid);
		return patientNoks;
	}
}
