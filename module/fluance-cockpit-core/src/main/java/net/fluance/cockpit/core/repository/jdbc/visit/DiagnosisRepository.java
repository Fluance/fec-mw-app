package net.fluance.cockpit.core.repository.jdbc.visit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.visit.Diagnosis;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class DiagnosisRepository extends JdbcRepository<Diagnosis, Object[]> {

	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;

	public DiagnosisRepository() {
		this(MappingsConfig.TABLE_NAMES.get("Diagnosis"));
	}

	/**
	 * 
	 * @param tableName
	 */
	public DiagnosisRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER,
				new TableDescription(tableName, null, "data", "rank", "type", "description", "language"));
	}

	public static final RowMapper<Diagnosis> ROW_MAPPER = new RowMapper<Diagnosis>() {
		public Diagnosis mapRow(ResultSet rs, int rowNum) throws SQLException {
			String lang = SqlUtils.getString(true, rs, "lang");
			if(lang != null) {
				lang = lang.trim();
			}
			
			Diagnosis diagnosis = new Diagnosis(rs.getLong("nb_records"), rs.getString("data"), SqlUtils.getInt(true, rs, "rank"), rs.getString("type"),
					rs.getString("description"), lang);
			return diagnosis;
		}
	};

	private static final RowUnmapper<Diagnosis> ROW_UNMAPPER = new RowUnmapper<Diagnosis>() {
		public Map<String, Object> mapColumns(Diagnosis diagnosis) {
			String lang = diagnosis.getDescLanguage();
			if(lang != null) {
				lang = lang.trim();
			}
			
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("nb_records", diagnosis.getNbRecords());
			mapping.put("data", diagnosis.getData());
			mapping.put("rank", diagnosis.getRank());
			mapping.put("type", diagnosis.getType());
			mapping.put("description", diagnosis.getDescription());
			mapping.put("lang", lang);
			return mapping;
		}
	};

	/**
	 * 
	 * @param visitNb
	 *            The visit number of the patient.
	 * @return list of diagnosis of a patient for a given visitnb
	 */
	public List<Diagnosis> findByVisitNb(int visitNb) {
		return findByVisitNb(visitNb, defaultResultLimit, defaultResultOffset);
	}

	public List<Diagnosis> findDiagnosis(long visitNb, String language, Integer limit, Integer offset) {
		if (limit == null)
			limit = defaultResultLimit;
		if(language!=null && !language.isEmpty()){
			return findByVisitNbAndLanguage(visitNb, language, limit, offset);
		}else{
			return findByVisitNb(visitNb, limit, offset);
		}
	}

	public List<Diagnosis> findByVisitNb(long visitNb, Integer limit, Integer offset) {
		if (limit == null)
			limit = defaultResultLimit;
		List<Diagnosis> diagnosis = getJdbcOperations().query(SQLStatements.FIND_DIAGNOSIS_BY_VISIT_NB, ROW_MAPPER, visitNb, limit, offset);
		return diagnosis;
	}

	public List<Diagnosis> findByVisitNbAndLanguage(long visitNb, String language, Integer limit, Integer offset) {
		if (limit == null)
			limit = defaultResultLimit;

		List<Diagnosis> diagnosis = getJdbcOperations().query(SQLStatements.FIND_DIAGNOSIS_BY_VISIT_NB_AND_LANGUAGE, ROW_MAPPER, language, visitNb, visitNb, limit, offset);
		return diagnosis;
	}

	public Integer findByVisitNbCount(Integer visitid) {
		return getJdbcOperations().queryForObject(SQLStatements.FIND_DIAGNOSIS_BY_VISIT_NB_COUNT, Integer.class, visitid);
	}

}
