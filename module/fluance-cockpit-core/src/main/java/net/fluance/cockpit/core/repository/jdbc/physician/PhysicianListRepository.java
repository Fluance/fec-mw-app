package net.fluance.cockpit.core.repository.jdbc.physician;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.physician.PhysicianList;
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
public class PhysicianListRepository extends JdbcRepository<PhysicianList, Integer> {
	
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	
	public PhysicianListRepository() {
		this(MappingsConfig.TABLE_NAMES.get("Physician"));
	}
	
	/**
	 * 
	 * @param tableName
	 */
	public PhysicianListRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER,
				new TableDescription(tableName, null, "code", "sequence", "type", "description"));
	}

	public static final RowMapper<PhysicianList> ROW_MAPPER = new RowMapper<PhysicianList>() {
		public PhysicianList mapRow(ResultSet rs, int rowNum) throws SQLException {
			PhysicianList PhysicianList = new PhysicianList(SqlUtils.getInt(true, rs, "attending_id"), rs.getString("attending_firstname"), rs.getString("attending_lastname"),
					SqlUtils.getInt(true, rs, "attending_staffid"), SqlUtils.getInt(true, rs, "referring_id"), rs.getString("referring_firstname"), rs.getString("referring_lastname"),
					SqlUtils.getInt(true, rs, "referring_staffid"), SqlUtils.getInt(true, rs, "consulting_id"), rs.getString("consulting_firstname"), rs.getString("consulting_lastname"),
					SqlUtils.getInt(true, rs, "consulting_staffid"), SqlUtils.getInt(true, rs, "admitting_id"), rs.getString("admitting_firstname"), rs.getString("admitting_lastname"),
					SqlUtils.getInt(true, rs, "admitting_staffid"));
			return PhysicianList;
		}
	};
	
	private static final RowUnmapper<PhysicianList> ROW_UNMAPPER = new RowUnmapper<PhysicianList>() {
		public Map<String, Object> mapColumns(PhysicianList PhysicianList) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("attendingPhysicianId", PhysicianList.getAttendingPhysicianId());
			mapping.put("attendingPhysicianFirstname", PhysicianList.getAttendingPhysicianFirstname());
			mapping.put("attendingPhysicianLastname", PhysicianList.getAttendingPhysicianLastname());
			mapping.put("attendingPhysicianStaffid", PhysicianList.getAttendingPhysicianStaffid());
			mapping.put("referringPhysicianId", PhysicianList.getReferringPhysicianId());
			mapping.put("referringPhysicianFirstname", PhysicianList.getReferringPhysicianFirstname());
			mapping.put("referringPhysicianLastname", PhysicianList.getReferringPhysicianLastname());
			mapping.put("referringPhysicianStaffid", PhysicianList.getReferringPhysicianStaffid());
			mapping.put("consultingPhysicianFirstname", PhysicianList.getConsultingPhysicianFirstname());
			mapping.put("consultingPhysicianLastname", PhysicianList.getConsultingPhysicianLastname());
			mapping.put("consultingPhysicianStaffid", PhysicianList.getConsultingPhysicianStaffid());
			mapping.put("admittingPhysicianId", PhysicianList.getAdmittingPhysicianId());
			mapping.put("admittingPhysicianFirstname", PhysicianList.getAdmittingPhysicianFirstname());
			mapping.put("admittingPhysicianLastname", PhysicianList.getAdmittingPhysicianLastname());
			mapping.put("admittingPhysicianStaffid", PhysicianList.getAdmittingPhysicianStaffid());
			return mapping;
		}
	};
	
	public List<PhysicianList> findPhysicianByVisitnb(long visitNb) {

		List<PhysicianList> PhysicianList = getJdbcOperations().query(SQLStatements.PHYSICIAN_BY_VISIT_NB, ROW_MAPPER, visitNb);
		return PhysicianList;
	}

}
