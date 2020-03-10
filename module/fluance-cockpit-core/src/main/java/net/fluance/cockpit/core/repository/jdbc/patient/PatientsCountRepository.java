package net.fluance.cockpit.core.repository.jdbc.patient;

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

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.patient.PatientsCount;
import net.fluance.cockpit.core.model.wrap.patient.AdmissionStatusEnum;
import net.fluance.cockpit.core.model.wrap.patient.PatientSexEnum;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class PatientsCountRepository extends JdbcRepository<PatientsCount, String> {

	public PatientsCountRepository() {
		super(ROW_MAPPER, ROW_UNMAPPER, MappingsConfig.TABLE_NAMES.get("PatientList"));
	}
	
	public static final RowMapper<PatientsCount> ROW_MAPPER = new RowMapper<PatientsCount>() {
		public PatientsCount mapRow(ResultSet rs, int rowNum) throws SQLException {
			Integer nbRecords = SqlUtils.getInt(true, rs, "nb_records");
			String substr = rs.getString("substr");
			PatientsCount patientsCount = new PatientsCount(nbRecords, substr);

			return patientsCount;
		}
	};
	
	private static final RowUnmapper<PatientsCount> ROW_UNMAPPER = new RowUnmapper<PatientsCount>() {
		public Map<String, Object> mapColumns(PatientsCount patient) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("nb_records", patient.getNb_records());
			mapping.put("substr", patient.getSubstr());
			return mapping;
		}
	};
	
	/**
	 * The list of {@link PatientsCount} that will returned group but the first lastname letter all the patients that match the criterias  
	 * 
	 * @param params {@link Map} of {@link String},{@link Object} The parameters names must match exactly the column names
	 * @param patientSex {@link PatientSexEnum}
	 * @param admissionStatus {@link AdmissionStatusEnum}
	 * @return {@link List} of {@link PatientsCount} 
	 */
	public List<PatientsCount> patientsCountByCriteria(Map<String, Object> params, PatientSexEnum patientSex, AdmissionStatusEnum admissionStatus) {
		
		List<PatientsCount> patients = new ArrayList<PatientsCount>();
		List<Object> queryParams = new ArrayList<>();
		
		Integer substrSize = 1;
		if (params.get("lastname") != null){
			String lastname = (String) params.get("lastname");
			substrSize = lastname.length();
		}
		queryParams.add(substrSize);
		
		String queryBase = SQLStatements.PATIENTS_COUNT_BY_CRITERIA_BASE;
		if(params.containsKey("email")) {
			queryBase = queryBase.concat(SQLStatements.INNER_JOIN_PATIENT_CONTACT_LIST);
		}
		
		String criteriaString = PatientJdbcRepositoriesUtils.createWhereForByCriteriaSearch(params, patientSex, admissionStatus);
		
		String groupBy = " GROUP BY substr";
		String orderBy = " ORDER BY substr";
		
		String query = queryBase + " " + criteriaString + ") bpl" + groupBy + orderBy;
		patients = getJdbcOperations().query(query, ROW_MAPPER, queryParams.toArray());
		
		return patients;
	}
}
