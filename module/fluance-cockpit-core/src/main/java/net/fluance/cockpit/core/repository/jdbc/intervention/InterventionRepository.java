package net.fluance.cockpit.core.repository.jdbc.intervention;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.domain.intervention.Intervention;
import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;

@Repository
@Component
public class InterventionRepository extends JdbcRepository<Intervention,Long>{

	public InterventionRepository() {
		this(MappingsConfig.TABLE_NAMES.get("bmv_visit_intervention_data"));
	}

	public InterventionRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "visit_nb"));
	}

	private static final RowUnmapper<Intervention> ROW_UNMAPPER = new RowUnmapper<Intervention>() {
		public Map<String, Object> mapColumns(Intervention intervention) {
			return null;
		}
	};

	public static final RowMapper<Intervention> ROW_MAPPER = new RowMapper<Intervention>() {
		public Intervention mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Intervention(rs.getLong("visit_nb"), rs.getString("data"), rs.getString("type"), rs.getInt("rank"), rs.getTimestamp("interventiondt")); 
		}
	};
	
	public List<Intervention> getByVisitnb(Long visitnb){
		List<Intervention> interventions = getJdbcOperations().query(SQLStatements.FIND_INTERVENTION_BY_VISIT_NB, ROW_MAPPER, visitnb);
	    return interventions;
	}
}
