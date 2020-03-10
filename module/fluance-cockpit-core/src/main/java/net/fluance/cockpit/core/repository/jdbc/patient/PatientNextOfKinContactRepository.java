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
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.patient.PatientNextOfKinContact;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class PatientNextOfKinContactRepository extends JdbcRepository<PatientNextOfKinContact, Object[]> {

	public PatientNextOfKinContactRepository() {
		this(MappingsConfig.TABLE_NAMES.get("PatientNextOfKinContact"));
	}
	
	public PatientNextOfKinContactRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER,
				new TableDescription(tableName, null, "id", "nbtype", "equipment"));
	}

	public static final RowMapper<PatientNextOfKinContact> ROW_MAPPER = new RowMapper<PatientNextOfKinContact>() {
		public PatientNextOfKinContact mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long id = SqlUtils.getLong(true, rs, "id");
			String nbType = rs.getString("nbtype");
			String equipment = rs.getString("equipment");
			String data = rs.getString("data");
			PatientNextOfKinContact nokContact = new PatientNextOfKinContact(id, nbType, equipment, data);
			return nokContact;
		}
	};

	private static final RowUnmapper<PatientNextOfKinContact> ROW_UNMAPPER = new RowUnmapper<PatientNextOfKinContact>() {
		public Map<String, Object> mapColumns(PatientNextOfKinContact contact) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("id", contact.getId());
			mapping.put("nbtype", contact.getNbType());
			mapping.put("equipment", contact.getEquipment());
			mapping.put("data", contact.getData());
			return mapping;
		}
	};
	
	/**
	 * 
	 * @param nokId
	 * @return
	 */
	public List<PatientNextOfKinContact> findByNokId(Long nokId) {
		List<PatientNextOfKinContact> patientsNokContacts = getJdbcOperations().query(SQLStatements.FIND_PATIENT_NEXTOFKIN_CONTACTS, ROW_MAPPER, nokId);
		return patientsNokContacts;
	}
	
}
