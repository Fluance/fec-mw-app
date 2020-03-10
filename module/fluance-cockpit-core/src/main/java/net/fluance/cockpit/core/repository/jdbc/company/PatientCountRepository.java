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
import net.fluance.cockpit.core.model.jdbc.company.PatientCount;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class PatientCountRepository extends JdbcRepository<PatientCount, Integer> {

	public PatientCountRepository() {
		this(MappingsConfig.TABLE_NAMES.get("m_bmv_patientbed"));
	}

	public PatientCountRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, ""));
	}

	public static final RowMapper<PatientCount> ROW_MAPPER = new RowMapper<PatientCount>() {
		public PatientCount mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new PatientCount(SqlUtils.getInt(true, rs, "nb_patients"), rs.getString("patientroom"));
		}
	};

	public static final RowUnmapper<PatientCount> ROW_UNMAPPER = new RowUnmapper<PatientCount>() {
		public Map<String, Object> mapColumns(PatientCount patientbed) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("patientroom", patientbed.getPatientRoom());
			mapping.put("nb_patients", patientbed.getPatientCount());
			return mapping;
		}
	};

	public List<PatientCount> findPatientunitAndCompanyId(String patientunit, int idCompany) {
		List<PatientCount> lPatientCount = getJdbcOperations().query(
				SQLStatements.FIND_PATIENT_COUNT_BY_COMPANYID_AND_PATIENTUNIT, ROW_MAPPER, patientunit, idCompany);
		return lPatientCount;
	}

	public List<PatientCount> findByHospserviceAndCompanyId(String hospservice, int companyId) {
		List<PatientCount> lPatientCount = getJdbcOperations().query(
				SQLStatements.FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE, ROW_MAPPER, hospservice, companyId);
		return lPatientCount;
	}

	public List<PatientCount> findByPatientunitAndHospserviceAndCompanyId(String patientunit, String hospservice, int companyId) {
		List<PatientCount> lPatientBed = getJdbcOperations().query(
				SQLStatements.FIND_PATIENT_COUNT_BY_COMPANYID_AND_HOSPSERVICE_AND_PATIENTUNIT, ROW_MAPPER, hospservice,
				patientunit, companyId);
		return lPatientBed;
	}

}
