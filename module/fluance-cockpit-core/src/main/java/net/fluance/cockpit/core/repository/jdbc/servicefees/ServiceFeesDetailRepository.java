package net.fluance.cockpit.core.repository.jdbc.servicefees;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.servicefees.ServiceFeesDetail;

@Repository
@Component
public class ServiceFeesDetailRepository extends JdbcRepository<ServiceFeesDetail,Long>{

	public ServiceFeesDetailRepository() {
		this(MappingsConfig.TABLE_NAMES.get("bmv_visit_benefit_detail"));
	}

	public ServiceFeesDetailRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "id"));
	}

	private static final RowUnmapper<ServiceFeesDetail> ROW_UNMAPPER = new RowUnmapper<ServiceFeesDetail>() {
		public Map<String, Object> mapColumns(ServiceFeesDetail serviceFeesDetail) {
			return null;
		}
	};

	public static final RowMapper<ServiceFeesDetail> ROW_MAPPER = new RowMapper<ServiceFeesDetail>() {
		public ServiceFeesDetail mapRow(ResultSet rs, int rowNum) throws SQLException {

			String lang = rs.getString("lang") == null ? "" : rs.getString("lang").trim();
			return new ServiceFeesDetail(rs.getLong("id"), rs.getLong("visit_nb"), rs.getString("code"), 
					rs.getTimestamp("benefitdt"), rs.getDouble("quantity"), rs.getString("side"), rs.getString("actingdptdesc"), 
					rs.getString("note"), rs.getString("description"), lang,
					rs.getInt("pp_id"), rs.getString("pp_firstname"), rs.getString("pp_lastname"), 
					rs.getInt("op_id"), rs.getString("op_firstname"), rs.getString("op_lastname"), 
					rs.getInt("lp_id"), rs.getString("lp_firstname"), rs.getString("lp_lastname"));
		}
	};

	public ServiceFeesDetail findBenefit(Long id, String language){
		if(id != null && language != null && !language.isEmpty()){
			return findByBenefitIdAndLanguage(id, language);
		} else if(id != null){
			return findByBenefitId(id);
		}
		return null;
	}
	
	private ServiceFeesDetail findByBenefitId(Long id){
		return getJdbcOperations().queryForObject(SQLStatements.FIND_BENEFIT_DETAIL_BY_ID, ROW_MAPPER, id);
	}

	private ServiceFeesDetail findByBenefitIdAndLanguage(Long id, String language){
		return getJdbcOperations().queryForObject(SQLStatements.FIND_BENEFIT_DETAIL_BY_ID_AND_LANG, ROW_MAPPER, language, id);
	} 

}
