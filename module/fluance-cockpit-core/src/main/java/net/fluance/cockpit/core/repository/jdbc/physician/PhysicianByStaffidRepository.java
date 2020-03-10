package net.fluance.cockpit.core.repository.jdbc.physician;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.physician.PhysicianByStaffid;
import net.fluance.commons.sql.SqlUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

@Repository
@Component
public class PhysicianByStaffidRepository extends JdbcRepository<PhysicianByStaffid, Integer> {
	
	
	public PhysicianByStaffidRepository() {
		this(MappingsConfig.TABLE_NAMES.get("Physician"));
	}
	
	/**
	 * 
	 * @param tableName
	 */
	public PhysicianByStaffidRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER,
				new TableDescription(tableName, null, "code", "sequence", "type", "description"));
	}

	public static final RowMapper<PhysicianByStaffid> ROW_MAPPER = new RowMapper<PhysicianByStaffid>() {
		public PhysicianByStaffid mapRow(ResultSet rs, int rowNum) throws SQLException {
			PhysicianByStaffid PhysicianByStaffid = new PhysicianByStaffid(rs.getString("firstname"), rs.getString("lastname"), SqlUtils.getInt(true, rs, "staffid"), SqlUtils.getInt(true, rs, "id"));
			return PhysicianByStaffid;
		}
	};
	
	private static final RowUnmapper<PhysicianByStaffid> ROW_UNMAPPER = new RowUnmapper<PhysicianByStaffid>() {
		public Map<String, Object> mapColumns(PhysicianByStaffid PhysicianByStaffid) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("firstname", PhysicianByStaffid.getFirstname());
			mapping.put("lastname", PhysicianByStaffid.getLastname());
			mapping.put("staffid", PhysicianByStaffid.getStaffid());
			mapping.put("id", PhysicianByStaffid.getId());
			return mapping;
		}
	};
	
	public List<PhysicianByStaffid> findPhysicianByStaffid(Integer staffid, Integer companyid) {

		List<PhysicianByStaffid> PhysicianByStaffid = getJdbcOperations().query(SQLStatements.PHYSICIAN_BY_STAFFID, ROW_MAPPER, staffid, companyid);
		return PhysicianByStaffid;
	}

}
