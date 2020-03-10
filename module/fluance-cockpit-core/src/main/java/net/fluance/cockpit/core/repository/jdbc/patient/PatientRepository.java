/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.patient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.jdbc.patient.Patient;
import net.fluance.commons.sql.SqlUtils;

/**
 * Repository for {@link Patient}
 *
 *
 */
@Repository
@Component
public class PatientRepository extends JdbcRepository<Patient, Long> {

	public PatientRepository() {
		super(ROW_MAPPER, ROW_UNMAPPER, MappingsConfig.TABLE_NAMES.get("Patient"));
	}

	public static final RowMapper<Patient> ROW_MAPPER = new RowMapper<Patient>() {
		public Patient mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long pid = SqlUtils.getLong(true, rs, "id");
			String language = rs.getString("language");
			String courtesy = rs.getString("courtesy");
			String firstname = rs.getString("firstname");
			String lastname = rs.getString("lastname");
			String maidenName = rs.getString("maidenname");
			Date birthDate = rs.getDate("birthdate");
			String avsNumber = rs.getString("avsnb");
			String nationality = rs.getString("nationality");
			String sex = rs.getString("sex");
			String address = rs.getString("address");
			String address2 = rs.getString("address2");
			String locality = rs.getString("locality");
			String postCode = rs.getString("postcode");
			String subPostCode = rs.getString("subpostcode");
			String adressComplement = rs.getString("complement");
			String careOf = rs.getString("careof");
			String canton = rs.getString("canton");
			String country = rs.getString("country");
			Boolean death = rs.getBoolean("death");
			java.sql.Timestamp deathdt = rs.getTimestamp("deathdt");
			String maritalStatus = rs.getString("maritalstatus");
			Patient patient = new Patient(pid, language, courtesy, firstname, lastname, maidenName, birthDate, avsNumber,
					nationality, sex, address, address2, locality, postCode, subPostCode, adressComplement,
					careOf, canton, country, death, deathdt, maritalStatus);
			return patient;
		}
	};

	private static final RowUnmapper<Patient> ROW_UNMAPPER = new RowUnmapper<Patient>() {
		public Map<String, Object> mapColumns(Patient patient) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("id", patient.getPid());
			mapping.put("language", patient.getLanguage());
			mapping.put("firstname", patient.getFirstName());
			mapping.put("lastname", patient.getLastName());
			mapping.put("maidenname", patient.getMaidenName());
			mapping.put("birthdate", patient.getBirthDate());
			mapping.put("avsnb", patient.getAvsNumber());
			mapping.put("nationality", patient.getNationality());
			mapping.put("sex", patient.getSex());
			mapping.put("address", patient.getAddress());
			mapping.put("address2", patient.getAddress2());
			mapping.put("locality", patient.getLocality());
			mapping.put("postcode", patient.getPostCode());
			mapping.put("subpostcode", patient.getSubPostCode());
			mapping.put("complement", patient);
			mapping.put("careOf", patient.getCareOf());
			mapping.put("canton", patient.getCanton());
			mapping.put("country", patient.getCountry());
			mapping.put("death", patient.isDeath());
			mapping.put("deathdt", patient.getDeathdt());
			mapping.put("maritalstatus", patient.getMaritalStatus());
			return mapping;
		}
	};
	
}
