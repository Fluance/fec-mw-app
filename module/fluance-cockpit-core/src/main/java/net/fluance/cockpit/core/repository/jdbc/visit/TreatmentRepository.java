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
import net.fluance.cockpit.core.model.jdbc.visit.Treatment;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class TreatmentRepository extends JdbcRepository<Treatment, Object[]> {

	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;

	public static final RowMapper<Treatment> ROW_MAPPER = new RowMapper<Treatment>() {
		public Treatment mapRow(ResultSet rs, int rowNum) throws SQLException {
			String lang = SqlUtils.getString(true, rs, "lang");
			if(lang != null) {
				lang = lang.trim();
			}
			
			Treatment treatment = new Treatment(rs.getInt("nb_records"), rs.getString("data"), SqlUtils.getInt(true, rs, "rank"), rs.getString("type"),
					rs.getString("description"), lang);
			return treatment;
		}
	};

	private static final RowUnmapper<Treatment> ROW_UNMAPPER = new RowUnmapper<Treatment>() {
		public Map<String, Object> mapColumns(Treatment treatment) {
			String lang = treatment.getDescLanguage();
			if(lang != null) {
				lang = lang.trim();
			}
			
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("nb_records", treatment.getNbRecords());
			mapping.put("code", treatment.getData());
			mapping.put("rank", treatment.getRank());
			mapping.put("type", treatment.getType());
			mapping.put("description", treatment.getDescription());
			mapping.put("lang", lang);
			return mapping;
		}
	};

	public TreatmentRepository() {
		this(MappingsConfig.TABLE_NAMES.get("Treatment"));
	}

	/**
	 * 
	 * @param tableName
	 */
	public TreatmentRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER,
				new TableDescription(tableName, null, "data", "rank", "type", "description"));
	}

	/**
	 * 
	 * @param visitNb The visit number of the patient.
	 * @return list of treatments of a patient for a given visitnb
	 */
	public List<Treatment> findByVisitNb(long visitNb) {
		return findByVisitNb(visitNb, defaultResultLimit, defaultResultOffset);
	}

	public List<Treatment> findTreatments(long visitNb, String language, Integer limit, Integer offset) {
		if (limit == null)
			limit = defaultResultLimit;
		if(language!=null && !language.isEmpty()){
			return findByVisitNbAndLanguage(visitNb, language, limit, offset);
		}else{
			return findByVisitNb(visitNb, limit, offset);
		}
	}

	/**
	 * 
	 * @param visitNb The visit number of the patient.
	 * @param limit The maximum number of treatments to return
	 * @param offset 
	 * @return list of treatments of a patient for a given visitnb
	 */
	public List<Treatment> findByVisitNb(long visitNb, Integer limit, Integer offset) {
		if (limit == null)
			limit = defaultResultLimit;

		List<Treatment> treatments = getJdbcOperations().query(SQLStatements.FIND_TREATMENTS_BY_VISIT_NB, ROW_MAPPER, visitNb, limit, offset);
		return treatments;
	}

	public List<Treatment> findByVisitNbAndLanguage(long visitNb, String language, Integer limit, Integer offset) {
		if (limit == null)
			limit = defaultResultLimit;

		List<Treatment> treatments = getJdbcOperations().query(SQLStatements.FIND_TREATMENTS_BY_VISIT_NB_AND_LANGUAGE, ROW_MAPPER, language, visitNb, visitNb, limit, offset);
		return treatments;
	}

	/**
	 * 
	 * @param visitNb The visit number of the patient.
	 * @return list of treatments of a patient for a given visitnb
	 */
	public Integer findByVisitNbCount(long visitNb) {
		return getJdbcOperations().queryForObject(SQLStatements.FIND_TREATMENTS_BY_VISIT_NB_COUNT, Integer.class, visitNb);
	}
}
