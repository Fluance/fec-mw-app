package net.fluance.cockpit.core.repository.jdbc.patientenliste;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.jdbc.patientenliste.BedConfig;

@Repository
public class BedConfigRepository extends JdbcRepository<BedConfig, Integer> {

	public BedConfigRepository() {
		this(MappingsConfig.TABLE_NAMES.get("patientslistconfig"));
	}

	/**
	 * 
	 * @param tableName
	 */
	public BedConfigRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "company_id", "unit", "room", "bed", "sortorder"));
	}

	public static final RowMapper<BedConfig> ROW_MAPPER = new RowMapper<BedConfig>() {

		public BedConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new BedConfig(rs.getInt("company_id"), rs.getString("unit"), rs.getString("room"), rs.getString("bed"), rs.getInt("sortorder"));
		}
	};
	private static final RowUnmapper<BedConfig> ROW_UNMAPPER = new RowUnmapper<BedConfig>() {

		public Map<String, Object> mapColumns(BedConfig bedConfig) {
			return null;
		}
	};
	
	public List<BedConfig> findByCompanyIdAndUnit(int companyId, String unit){
		return getJdbcOperations().query("select * from ehealth.patientslistconfig where company_id = ? and unit = ? order by sortorder asc", ROW_MAPPER, companyId, unit);
	}
}
