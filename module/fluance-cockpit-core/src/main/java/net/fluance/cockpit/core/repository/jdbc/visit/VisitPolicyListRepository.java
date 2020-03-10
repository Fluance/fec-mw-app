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
import net.fluance.cockpit.core.model.jdbc.visit.GuarantorList;
import net.fluance.cockpit.core.model.jdbc.visit.VisitPolicyList;
import net.fluance.commons.sql.SqlUtils;
import net.fluance.cockpit.core.model.jdbc.visit.Treatment;

@Repository
@Component
public class VisitPolicyListRepository extends JdbcRepository<VisitPolicyList, Integer> {
	
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultPolicyListOrderBy}")
	private String defaultResultPolicyListOrderBy;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultPolicyListSortorder}")
	private String defaultResultPolicyListSortOrder;

	public static final RowMapper<VisitPolicyList> ROW_MAPPER = new RowMapper<VisitPolicyList>() {
		public VisitPolicyList mapRow(ResultSet rs, int rowNum) throws SQLException {
			VisitPolicyList policy = new VisitPolicyList(rs.getInt("nb_records"), SqlUtils.getInt(true, rs, "id"), rs.getString("name"),
					rs.getString("code"), SqlUtils.getInt(true, rs, "priority"), SqlUtils.getInt(true, rs, "subpriority"), rs.getString("hospclass"));
			return policy;
		}
	};

	private static final RowUnmapper<VisitPolicyList> ROW_UNMAPPER = new RowUnmapper<VisitPolicyList>() {
		public Map<String, Object> mapColumns(VisitPolicyList policyList) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			return null;
		}
	};

	public VisitPolicyListRepository() {
		this(MappingsConfig.TABLE_NAMES.get("bmv_guarantor_detail"));
	}

	public VisitPolicyListRepository(String tableName) {
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
	public List<VisitPolicyList> findByVisitNb(long visitNb, String orderBy, String sortorder, Integer limit, Integer offset){
		if (limit == null){
			limit = defaultResultLimit;
		}
		if (offset == null){
			offset = defaultResultOffset;
		}
		if (orderBy == null || orderBy.isEmpty())
		{
			orderBy = defaultResultPolicyListOrderBy;
		}
		if (sortorder == null)
		{
			sortorder=defaultResultPolicyListSortOrder;
		}
		orderBy=orderBy.concat(" "+sortorder);
		List<VisitPolicyList> policies = getJdbcOperations().query(SQLStatements.VISIT_POLICIES.replace("order by ?", "order by "+orderBy), ROW_MAPPER, visitNb, limit, offset);
		return policies;
	}
	
	public Integer findByVisitNbCount(Long visitNb){
		return getJdbcOperations().queryForObject(SQLStatements.VISIT_POLICIES_COUNT, Integer.class, visitNb);
	}
}
