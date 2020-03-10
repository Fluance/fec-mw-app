package net.fluance.cockpit.core.repository.jdbc.servicefees;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.servicefees.ServiceFeesList;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class ServiceFeesListRepository extends JdbcRepository<ServiceFeesList,Long>{

	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultBenefitListOrderBy}")
	private String defaultResultBenefitListOrderBy;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultBenefitListSortOrder}")
	private String defaultResultBenefitListSortOrder;
	
	public ServiceFeesListRepository() {
		this(MappingsConfig.TABLE_NAMES.get("bmv_visit_benefit_list"));
	}

	public ServiceFeesListRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "appointment_id"));
	}

	private static final RowUnmapper<ServiceFeesList> ROW_UNMAPPER = new RowUnmapper<ServiceFeesList>() {
		public Map<String, Object> mapColumns(ServiceFeesList benefitList) {
			return null;
		}
	};

	public static final RowMapper<ServiceFeesList> ROW_MAPPER = new RowMapper<ServiceFeesList>() {
		public ServiceFeesList mapRow(ResultSet rs, int rowNum) throws SQLException {

			Integer nbRecords = rs.getInt("nb_records");
			Long id = SqlUtils.getLong(true, rs, "id");
			Long visitNumber = rs.getLong("visit_nb");
			String code = rs.getString("code");
			Timestamp benefitDate = rs.getTimestamp("benefitdt");
			double quantity = rs.getDouble("quantity");
			String side = rs.getString("side");
			String actingDeptDesc = rs.getString("actingdptdesc");
			String note = rs.getString("note");
			String description = rs.getString("description");
			String language = rs.getString("lang");
			Boolean hasPhysician = rs.getBoolean("hasPhysician");
			
			if(language != null && !language.isEmpty()){
				language = rs.getString("lang").trim();
			}
			return new ServiceFeesList(nbRecords, id, visitNumber, code, benefitDate, quantity, side, actingDeptDesc, 
					note, description, language, hasPhysician);
		}
	};

	public List<ServiceFeesList> findBenefits(Long visitNumber, String language, String orderBy, String sortOrder, Integer limit, Integer offset){
		if(visitNumber != null && language != null && !language.isEmpty()){
			return findByVisitNbAndLanguage(visitNumber, language, orderBy, sortOrder, limit, offset);
		} else if(visitNumber != null){
			return findByVisitNb(visitNumber, orderBy, sortOrder, limit, offset);
		}
		return null;
	}
	
	public List<ServiceFeesList> findServiceFeesUsingFilter(Long visitNumber, String filterBy, String filterValue, String orderBy, String sortOrder, Integer limit, Integer offset) {
		if (limit == null) limit = defaultResultLimit;
		if (offset == null) offset = defaultResultOffset;
		if (orderBy == null || orderBy.isEmpty()) orderBy = defaultResultBenefitListOrderBy;
		if (sortOrder == null) sortOrder = defaultResultBenefitListSortOrder;
		
		String filterColumn = null;

		switch (filterBy) {
		case "department":
			filterColumn = "actingdptdesc";
			break;
		case "date":
			filterColumn = "benefitdt::DATE::VARCHAR";
			break;
		default:
			throw new IllegalArgumentException("Unsupported value for filterby : " + filterBy);
		}
		
		// Handles fees counting with filtering
		if (!StringUtils.isEmpty(filterBy) && !StringUtils.isEmpty(filterValue)) {
			String query = String.format(SQLStatements.FIND_BENEFIT_FILTERED_LIST_BY_VISIT_NB, filterColumn, orderBy, sortOrder, limit, offset);	
			return getJdbcOperations().query(query, ROW_MAPPER, visitNumber, filterValue);
		} else {
			return findByVisitNb(visitNumber, orderBy, sortOrder, limit, offset);
		}
	}
	
	private List<ServiceFeesList> findByVisitNb(Long visitNumber, String orderBy, String sortOrder, Integer limit, Integer offset){
		if (limit == null) limit = defaultResultLimit;
		if (offset == null) offset = defaultResultOffset;
		if (orderBy == null || orderBy.isEmpty()) orderBy = defaultResultBenefitListOrderBy;
		if (sortOrder == null) sortOrder = defaultResultBenefitListSortOrder;
		
		orderBy=orderBy.concat(" "+sortOrder);
		
		String query = SQLStatements.FIND_BENEFIT_LIST_BY_VISIT_NB + " ORDER BY " + orderBy +" LIMIT " +limit + " OFFSET " +offset;
		return getJdbcOperations().query(query, ROW_MAPPER, visitNumber);
	}

	private List<ServiceFeesList> findByVisitNbAndLanguage(Long visitNumber, String language, String orderBy, String sortOrder, Integer limit, Integer offset){
		if (limit == null) limit = defaultResultLimit;
		if (offset == null) offset = defaultResultOffset;
		if (orderBy == null || orderBy.isEmpty()) orderBy = defaultResultBenefitListOrderBy;
		if (sortOrder == null) sortOrder = defaultResultBenefitListSortOrder;
		
		orderBy=orderBy.concat(" "+sortOrder);
		String query = SQLStatements.FIND_BENEFIT_LIST_BY_VISIT_NB_AND_LANG + " ORDER BY " +orderBy +" LIMIT " +limit + " OFFSET " +offset;
		return getJdbcOperations().query(query, ROW_MAPPER, language, visitNumber);
	} 
	
	private Integer findServiceFeesUsingFilterCount(Long visitNumber, String filterBy, String filterValue) {
		
		String filterColumn = null;

		switch (filterBy) {
		case "department":
			filterColumn = "actingdptdesc";
			break;
		case "date":
			filterColumn = "benefitdt::DATE::VARCHAR";
			break;
		default:
			throw new IllegalArgumentException("Unsupported value for filterby : " + filterBy);
		}
		
		String query = String.format(SQLStatements.FIND_BENEFIT_FILTERED_LIST_BY_VISIT_NB_COUNT, filterColumn);				
				
		return getJdbcOperations().queryForObject(query, Integer.class, visitNumber, filterValue);
	}
	
	private Integer findByVisitNbCount(Long visitNumber){
		return getJdbcOperations().queryForObject(SQLStatements.FIND_BENEFIT_LIST_BY_VISIT_NB_COUNT, Integer.class, visitNumber);
	}
	
	private Integer findByVisitNbAndLanguageCount(Long visitNumber, String language){
		return getJdbcOperations().queryForObject(SQLStatements.FIND_BENEFIT_LIST_BY_VISIT_NB_AND_LANG_COUNT, Integer.class, language, visitNumber);
	}
	
	public Integer getBenefitListCount(Long visitNumber, String language, String filterBy, String filterValue) { 
		// No visit number => bye bye
		if (visitNumber == null) { 
			return null; 
		}
		
		// Handles fees counting with filtering
		if (!StringUtils.isEmpty(filterBy) && !StringUtils.isEmpty(filterValue)) { 
			return findServiceFeesUsingFilterCount(visitNumber, filterBy, filterValue);  
		}
		
		// Handles fees counting with language
		if (language != null && !language.isEmpty()) { return findByVisitNbAndLanguageCount(visitNumber, language); }
		
		// Default fees counting for a visit number
		return findByVisitNbCount(visitNumber);
	}
}
