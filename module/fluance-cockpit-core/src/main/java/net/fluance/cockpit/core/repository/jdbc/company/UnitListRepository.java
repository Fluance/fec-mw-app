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

import com.google.gson.Gson;
import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.company.Unit;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class UnitListRepository extends JdbcRepository<Unit, Integer> {

	public UnitListRepository() {
		this(MappingsConfig.TABLE_NAMES.get("m_bmv_patientunit"));
	}

	public UnitListRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, ""));
	}

	public static final RowMapper<Unit> ROW_MAPPER = new RowMapper<Unit>() {
		public Unit mapRow(ResultSet rs, int rowNum) throws SQLException{
			return new Unit(rs.getString("patientunit"), rs.getString("patientunitdesc"));

		}
	};
	
	public static final RowMapper<Unit> ROW_MAPPER_WITHCOUNT = new RowMapper<Unit>() {
		public Unit mapRow(ResultSet rs, int rowNum) throws SQLException{
			return new Unit(rs.getString("patientunit"), rs.getString("patientunitdesc"), SqlUtils.getInt(true, rs, "nb_patients"));
		}
	};

	public static final RowUnmapper<Unit> ROW_UNMAPPER = new RowUnmapper<Unit>() {
		public Map<String, Object> mapColumns(Unit unit) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("patientunit", unit.getPatientunit());
			mapping.put("patientunitdesc", unit.getCodedesc());
			return mapping;
		}
	};

	public List<Unit> findByCompanyId(int companyId) {
		List<Unit> u= getJdbcOperations().query(SQLStatements.FIND_LIST_UNITS_BY_ID, ROW_MAPPER, companyId);
		return u;
	}
	
	public List<Unit> findByCompanyIdWithCount(int companyId, List<String> patientUnits) {
		
		String units = new Gson().toJson(patientUnits);

		List<Unit> u= getJdbcOperations().query(SQLStatements.FIND_LIST_UNITS_BY_ID_WITH_COUNT, ROW_MAPPER_WITHCOUNT, units, companyId);
		return u;
	}

}
