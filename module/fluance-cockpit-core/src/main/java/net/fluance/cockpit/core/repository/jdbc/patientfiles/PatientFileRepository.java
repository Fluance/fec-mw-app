package net.fluance.cockpit.core.repository.jdbc.patientfiles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.CompanyReference;
import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.jdbc.patientfiles.PatientFile;

@Repository
public class PatientFileRepository extends JdbcRepository<PatientFile, Long> {
	
	private static final String FILES_BY_PID = "SELECT * FROM " + MappingsConfig.TABLE_NAMES.get("patientfile")+" WHERE patient_id = ? order by ? LIMIT ? OFFSET ?";
	private static final String FILES_BY_PID_BY_COMPANY = "SELECT * FROM " + MappingsConfig.TABLE_NAMES.get("patientfile")+" WHERE patient_id = ? AND company_id = ? order by ? LIMIT ? OFFSET ?";
	private static final String FILES_BY_PID_COUNT = "SELECT COUNT(*) FROM " + MappingsConfig.TABLE_NAMES.get("patientfile")+" WHERE patient_id = ? ";
	private static final String FILES_BY_PID_BY_COMPANY_COUNT = "SELECT COUNT(*) FROM " + MappingsConfig.TABLE_NAMES.get("patientfile")+" WHERE patient_id = ? AND company_id = ? ";
	private static final String COMPANIES_PATIENT_FILES = "select distinct(company_id), code, name from files.bmv_icp_detail where patient_id = ? group by company_id, code, name";
	
	public PatientFileRepository() {
		this(MappingsConfig.TABLE_NAMES.get("patientfile"));
	}

	public static final RowMapper<PatientFile> ROW_MAPPER = new RowMapper<PatientFile>() {

		public PatientFile mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long id = rs.getLong("id");
			Long patientId = rs.getLong("patient_id");
			String fileName = rs.getString("filename");
			String path = rs.getString("path");
			Date creationDate = rs.getTimestamp("creationdt");
			Integer companyId = rs.getInt("company_id");
			String companyName = rs.getString("name");
			String companyCode = rs.getString("code");
			return new PatientFile(id, patientId, fileName, path, creationDate, new CompanyReference(companyId, companyName, companyCode));
		}
	};
	
	private static final RowUnmapper<PatientFile> ROW_UNMAPPER = new RowUnmapper<PatientFile>() {

		@Override
		public Map<String, Object> mapColumns(PatientFile t) {
			return null;
		}
	};

	public PatientFileRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "id"));
	}
	
	public List<PatientFile> getPatientFilesByPid(long pid, String orderBy, String sortOrder, int limit, int offset){
		orderBy = orderBy + " " + sortOrder;
		return getJdbcOperations().query(FILES_BY_PID.replace("order by ?", "order by " + orderBy), ROW_MAPPER, pid, limit, offset);
	}
	
	public List<PatientFile> getPatientFilesByPidAndCompanyId(long pid, int companyId, String orderBy, String sortOrder, int limit, int offset){
		orderBy = orderBy + " " + sortOrder;
		return getJdbcOperations().query(FILES_BY_PID_BY_COMPANY.replace("order by ?", "order by " + orderBy), ROW_MAPPER, pid, companyId, limit, offset);
	}
	
	public int getPatientFilesByPidCount(long pid){
		return getJdbcOperations().queryForObject(FILES_BY_PID_COUNT, Integer.class, pid);
	}
	
	public int getPatientFilesByPidAndCompanyIdCount(long pid, int companyId){
		return getJdbcOperations().queryForObject(FILES_BY_PID_BY_COMPANY_COUNT, Integer.class, pid, companyId);
	}

	
	public static final RowMapper<CompanyReference> COMPANY_REF_MAPPER = new RowMapper<CompanyReference>() {
		public CompanyReference mapRow(ResultSet rs, int rowNum) throws SQLException {
			Integer companyId = rs.getInt("company_id");
			String companyName = rs.getString("name");
			String companyCode = rs.getString("code");
			return new CompanyReference(companyId, companyName, companyCode);
		}
	};
	
	public List<CompanyReference> getCompaniesPatientFiles(Long pid){
		return getJdbcOperations().query(COMPANIES_PATIENT_FILES, COMPANY_REF_MAPPER, pid);
	}
}
