/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.patient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.patient.Patient;
import net.fluance.cockpit.core.model.jdbc.patient.PatientContact;
import net.fluance.cockpit.core.model.jdbc.patient.PatientNextOfKin;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class PatientContactRepository extends JdbcRepository<PatientContact, Object[]> {

	public PatientContactRepository() {
		this(MappingsConfig.TABLE_NAMES.get("PatientContact"));
	}

	public PatientContactRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "id", "nbtype", "equipment"));
	}

	public static final RowMapper<PatientContact> ROW_MAPPER = new RowMapper<PatientContact>() {

		public PatientContact mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long pid = SqlUtils.getLong(true, rs, "id");
			String nbType = rs.getString("nbtype");
			String equipment = rs.getString("equipment");
			String data = rs.getString("data");
			PatientContact contact = new PatientContact(pid, nbType, equipment, data);
			return contact;
		}
	};
	private static final RowUnmapper<PatientContact> ROW_UNMAPPER = new RowUnmapper<PatientContact>() {

		public Map<String, Object> mapColumns(PatientContact contact) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("id", contact.getId());
			mapping.put("nbtype", contact.getNbType());
			mapping.put("equipment", contact.getEquipment());
			mapping.put("data", contact.getData());
			return mapping;
		}
	};

	public List<PatientContact> findByPid(Long pid) {
		List<PatientContact> patientContacts = getJdbcOperations().query(SQLStatements.FIND_PATIENT_CONTACTS, ROW_MAPPER, pid);
		return patientContacts;
	}

	public List<PatientContact> findByContact(String contact, String newPhone) {
		return getJdbcOperations().query(
				"SELECT * FROM " + MappingsConfig.TABLE_NAMES.get("PatientContact") + " WHERE data LIKE ? OR data Like ? OR regexp_replace(data, '[^0-9]+', '', 'g') LIKE ? OR regexp_replace(data, '[^0-9]+', '', 'g') LIKE ?", ROW_MAPPER,
				contact, newPhone,contact, newPhone);
	}
}
