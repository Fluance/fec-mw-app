package net.fluance.cockpit.core.repository.jdbc.visit;

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
import net.fluance.cockpit.core.model.jdbc.visit.VisitPolicyDetail;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class VisitPolicyDetailRepository extends JdbcRepository<VisitPolicyDetail, String> {
	
	public static final RowMapper<VisitPolicyDetail> ROW_MAPPER = new RowMapper<VisitPolicyDetail>() {
		public VisitPolicyDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			VisitPolicyDetail policyDetail = new VisitPolicyDetail(SqlUtils.getInt(true, rs, "id"), rs.getString("name"),
					rs.getString("code"), SqlUtils.getInt(true, rs, "priority"), SqlUtils.getInt(true, rs, "subpriority"), rs.getString("hospclass"),
					rs.getBoolean("inactive"), rs.getString("policynb"), rs.getDouble("covercardnb"),
					rs.getString("accidentnb"), rs.getString("accidentDate"));
			return policyDetail;
		}
	};

	private static final RowUnmapper<VisitPolicyDetail> ROW_UNMAPPER = new RowUnmapper<VisitPolicyDetail>() {
		public Map<String, Object> mapColumns(VisitPolicyDetail policyDetail) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			return null;
		}
	};

	public VisitPolicyDetailRepository() {
		this(MappingsConfig.TABLE_NAMES.get("bmv_visit_guarantors_list"));
	}

	public VisitPolicyDetailRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER,
				new TableDescription(tableName, null, ""));
	}

	/**
	 * 
	 * @param visitNb The visit number of the patient.
	 * @param orderBy
	 * @param limit The maximum number of treatments to return
	 * @param offset
	 * @return list of Policies
	 */
	/**
	 * 
	 */
	public VisitPolicyDetail findByVisitNbAndGuarantorIdAndPriorityAndSubPriority(int visitNb, int guarantorId, int priority, int subpriority){
		VisitPolicyDetail policyDetail = getJdbcOperations().queryForObject(SQLStatements.VISIT_POLICY_DETAIL, ROW_MAPPER, visitNb, guarantorId, priority, subpriority);
		return policyDetail;
	}
	
	/**
	 * Get a {@link VisitPolicyDetail} as of its Visit Number 
	 * @param visitNb
	 * @return
	 */
	public List<VisitPolicyDetail> findByVisit(Long visitNb){
		
		if(visitNb == null){
			throw new IllegalArgumentException("Value cannot be null");
		}
		
		List<VisitPolicyDetail> policiesDetail = getJdbcOperations().query(SQLStatements.FIND_VISIT_POLICIES, ROW_MAPPER, visitNb);
		
		return policiesDetail;
	}
}
