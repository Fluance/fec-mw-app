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
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class GuarantorListRepository extends JdbcRepository<GuarantorList, Integer> {
	
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultGuarantorListOrderBy}")
	private String defaultResultGuarantorListOrderBy;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultGuarantorListSortOrder}")
	private String defaultResultGuarantorListSortOrder;


	public static final RowMapper<GuarantorList> ROW_MAPPER = new RowMapper<GuarantorList>() {
		public GuarantorList mapRow(ResultSet rs, int rowNum) throws SQLException {
			GuarantorList GuarantorListReponse = new GuarantorList(rs.getLong("nb_records"), SqlUtils.getInt(true, rs, "id"), rs.getString("name"),
					rs.getString("code"), rs.getString("address"), rs.getString("address2"), rs.getString("locality"),
					rs.getString("postcode"), rs.getString("canton"), rs.getString("country"),
					rs.getString("complement"), rs.getString("begindate"), rs.getString("enddate"),
					rs.getBoolean("occasional"));
			return GuarantorListReponse;
		}
	};

	private static final RowUnmapper<GuarantorList> ROW_UNMAPPER = new RowUnmapper<GuarantorList>() {
		public Map<String, Object> mapColumns(GuarantorList guarantorListReponse) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			return null;
		}
	};

	public GuarantorListRepository() {
		this(MappingsConfig.TABLE_NAMES.get("bmv_guarantor_detail"));
	}

	/**
	 * 
	 * @param tableName
	 */
	public GuarantorListRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER,
				new TableDescription(tableName, null, ""));
	}

	/**
	 * 
	 * @param visitNb The visit number of the patient.
	 * @param orderBy
	 * @param limit The maximum number of treatments to return
	 * @param offset
	 * @return list of guarantors
	 */
	/**
	 * 
	 */
	public List<GuarantorList> findByVisitNb(long visitNb, String orderBy, String sortorder, Integer limit, Integer offset){
		if (limit == null){
			limit = defaultResultLimit;
		}
		if (offset == null){
			offset = defaultResultOffset;
		}
		if (orderBy == null || orderBy.isEmpty())
		{
			orderBy = defaultResultGuarantorListOrderBy;
		}
		if (sortorder == null)
		{
			sortorder=defaultResultGuarantorListSortOrder;
		}
		orderBy=orderBy.concat(" "+sortorder);
		List<GuarantorList> guarantorListReponses = getJdbcOperations().query(SQLStatements.VISIT_GUARANTORS.replace("order by ?", "order by "+orderBy), ROW_MAPPER, visitNb, limit, offset);
		return guarantorListReponses;
	}
	
	public Integer findByVisitNbCount(long visitNb){
		return getJdbcOperations().queryForObject(SQLStatements.VISIT_GUARANTORS_COUNT, Integer.class, visitNb);
	}
}
