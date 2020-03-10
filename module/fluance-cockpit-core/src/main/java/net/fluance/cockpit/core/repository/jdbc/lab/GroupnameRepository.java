/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.lab;

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
import net.fluance.cockpit.core.model.jdbc.lab.Groupname;

@Repository
@Component
public class GroupnameRepository extends JdbcRepository<Groupname, Integer> {

	public GroupnameRepository() {
		this(MappingsConfig.TABLE_NAMES.get("groupname_list"));
	}

	public GroupnameRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, ""));
	}

	public static final RowMapper<Groupname> ROW_MAPPER = new RowMapper<Groupname>() {
		public Groupname mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Groupname(rs.getString("groupname"));
		}
	};

	public static final RowUnmapper<Groupname> ROW_UNMAPPER = new RowUnmapper<Groupname>() {
		public Map<String, Object> mapColumns(Groupname groupname) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("groupname", groupname.getGroupname());

			return mapping;
		}
	};

	public List<Groupname> findByPid(long patientid) {
		List<Groupname> lGroupnames = getJdbcOperations().query(
				SQLStatements.FIND_GROUPNAMES_BY_PATIENTID, ROW_MAPPER, patientid);
		return lGroupnames;
	}

}
