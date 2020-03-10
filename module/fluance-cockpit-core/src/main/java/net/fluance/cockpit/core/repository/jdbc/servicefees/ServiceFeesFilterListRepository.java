package net.fluance.cockpit.core.repository.jdbc.servicefees;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.servicefees.ServiceFeesFilterList;
import net.fluance.cockpit.core.model.jdbc.servicefees.ServiceFeesList;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class ServiceFeesFilterListRepository extends JdbcRepository<ServiceFeesFilterList,Long> {

	@Value("${net.fluance.cockpit.core.model.repository.defaultResultServiceFeesFilterListOrderBy}")
	private String defaultResultServiceFeesFilterListOrderBy;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultServiceFeesFilterListSortOrder}")
	private String defaultResultServiceFeesFilterListSortOrder;
	
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private Integer defaultResultServiceFeesFilterListLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private Integer defaultResultServiceFeesFilterListOffset;
	
	public ServiceFeesFilterListRepository() {
		this(MappingsConfig.TABLE_NAMES.get("bmv_visit_benefit_list"));
	}

	public ServiceFeesFilterListRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "appointment_id"));
	}

	private static final RowUnmapper<ServiceFeesFilterList> ROW_UNMAPPER = new RowUnmapper<ServiceFeesFilterList>() {
		public Map<String, Object> mapColumns(ServiceFeesFilterList groupingList) {
			return null;
		}
	};

	public static final RowMapper<ServiceFeesFilterList> ROW_MAPPER = new RowMapper<ServiceFeesFilterList>() {
		public ServiceFeesFilterList mapRow(ResultSet rs, int rowNum) throws SQLException {

			String filter = rs.getString("filter");
			return new ServiceFeesFilterList(filter);

		};
	};

	public Integer getFilterValuesCount(Long visitNb, String filterBy) {
		if (filterBy == null || filterBy.isEmpty()) { filterBy = defaultResultServiceFeesFilterListOrderBy; }
		String filterColumn = null;

		switch (filterBy) {
		case "department":
			filterColumn = "actingdptdesc";
			break;
		case "date":
			filterColumn = "benefitdt::DATE";
			break;
		default:
			throw new IllegalArgumentException("Unsupported value for filterby : " + filterBy);
		}		
		// Injects grouping column and sorting order
		String query = String.format(SQLStatements.FIND_BENEFIT_FILTER_GROUPED_LIST_BY_VISIT_NB_COUNT, filterColumn);
		
		// Pass visit number as parameter and execute query
		return getJdbcOperations().queryForObject(query, Integer.class, visitNb);
	}
	
	public List<ServiceFeesFilterList> getFilterValues(Long visitNb, String filterBy, String sortOrder, Integer limit, Integer offset){

		if (filterBy == null || filterBy.isEmpty()) { filterBy = defaultResultServiceFeesFilterListOrderBy; }
		if (sortOrder == null) { sortOrder = defaultResultServiceFeesFilterListSortOrder; }
		if (limit == null) { limit = defaultResultServiceFeesFilterListLimit; }
		if (offset == null) { offset = defaultResultServiceFeesFilterListOffset; }
		
		
		String filterColumn = null;

		switch (filterBy) {
		case "department":
			filterColumn = "actingdptdesc";
			break;
		case "date":
			filterColumn = "benefitdt::DATE";
			break;
		default:
			throw new IllegalArgumentException("Unsupported value for filterby : " + filterBy);
		}

		// Injects grouping column and sorting order
		String query = String.format(SQLStatements.FIND_BENEFIT_FILTER_GROUPED_LIST_BY_VISIT_NB, filterColumn, sortOrder, limit, offset);
		
		// Pass visit number as parameter and execute query
		return getJdbcOperations().query(query, ROW_MAPPER, visitNb);
	}

}
