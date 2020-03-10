/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.jdbc.company.CompanyDetails;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class CompanyDetailsRepository extends JdbcRepository<CompanyDetails, Integer> {

	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;

	public CompanyDetailsRepository() {
		this(MappingsConfig.TABLE_NAMES.get("Company"));
	}

	public CompanyDetailsRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "id"));
	}

	public static final RowMapper<CompanyDetails> ROW_MAPPER = new RowMapper<CompanyDetails>() {
		public CompanyDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new CompanyDetails(SqlUtils.getInt(true, rs, "id"), rs.getString("code"), rs.getString("name"),
					rs.getString("address"), rs.getString("canton"), rs.getString("country"), rs.getString("email"),
					rs.getString("fax"), rs.getString("locality"), rs.getString("phone"), SqlUtils.getInt(true, rs, "postcode"),
					rs.getString("preflang"));
		}
	};

	private static final RowUnmapper<CompanyDetails> ROW_UNMAPPER = new RowUnmapper<CompanyDetails>() {
		public Map<String, Object> mapColumns(CompanyDetails company) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("id", company.getId());
			mapping.put("code", company.getCode());
			mapping.put("name", company.getName());
			mapping.put("address", company.getAddress());
			mapping.put("postcode", company.getPostcode());
			mapping.put("locality", company.getLocality());
			mapping.put("canton", company.getCanton());
			mapping.put("country", company.getCountry());
			mapping.put("email", company.getEmail());
			mapping.put("phone", company.getPhone());
			mapping.put("preflang", company.getPreflang());
			mapping.put("fax", company.getFax());
			return mapping;
		}
	};
	
	@Override
	@Cacheable("companyDetails")
	public CompanyDetails findOne(Integer companyId) {
		CompanyDetails companyDetails = super.findOne(companyId);
		return companyDetails;
	}

}
