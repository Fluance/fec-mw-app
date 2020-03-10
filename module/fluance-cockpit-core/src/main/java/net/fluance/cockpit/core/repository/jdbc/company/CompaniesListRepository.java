/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.company;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.company.CompaniesList;
import net.fluance.cockpit.core.model.jdbc.company.ServiceList;
import net.fluance.cockpit.core.model.jdbc.company.Unit;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class CompaniesListRepository extends JdbcRepository<CompaniesList, Integer> {

	public CompaniesListRepository() {
		this(MappingsConfig.TABLE_NAMES.get("Company"));
	}

	public CompaniesListRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "id"));
	}

	public static final RowMapper<CompaniesList> ROW_MAPPER = new RowMapper<CompaniesList>() {
		public CompaniesList mapRow(ResultSet rs, int rowNum) throws SQLException {
			try {
				return new CompaniesList(SqlUtils.getInt(true, rs, "id"), rs.getString("code"), rs.getString("name"), toUnits(rs.getString("units")), toServices(rs.getString("hospservices")));
			} catch (JSONException | IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Error when buildint Units or Hospservices");
			}
		}
		
		private List<Unit> toUnits(String jsonString)
				throws JsonParseException, JsonMappingException, JSONException, IOException {
			ObjectMapper mapper = new ObjectMapper();
			if(jsonString!=null && !jsonString.isEmpty()){
				List<Unit> units = mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(List.class, Unit.class));
				return units;
			} else {
				return new ArrayList<>();
			}
		}
		
		private List<ServiceList> toServices(String jsonString)
				throws JsonParseException, JsonMappingException, JSONException, IOException {
			ObjectMapper mapper = new ObjectMapper();
			if(jsonString!=null && !jsonString.isEmpty()){
				List<ServiceList> services = mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(List.class, ServiceList.class));
				return services;
			} else {
				return new ArrayList<>();
			}
		}

	};

	private static final RowUnmapper<CompaniesList> ROW_UNMAPPER = new RowUnmapper<CompaniesList>() {
		public Map<String, Object> mapColumns(CompaniesList company) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("id", company.getId());
			mapping.put("code", company.getCode());
			mapping.put("name", company.getName());
			return mapping;
		}
	};

	@Cacheable("companyList")
	public List<CompaniesList> findAll() {
		List<CompaniesList> c = getJdbcOperations().query(SQLStatements.FIND_LIST_COMPANIES, ROW_MAPPER);
		return c;
	}

}
