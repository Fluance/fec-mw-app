package net.fluance.cockpit.core.repository.jdbc.company;

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
import net.fluance.cockpit.core.model.jdbc.company.CompanyMetadata;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class CompanyMetadataRepository extends JdbcRepository<CompanyMetadata, Integer>{

	public CompanyMetadataRepository() {
		this(MappingsConfig.TABLE_NAMES.get("company_metadata"));
	}

	public CompanyMetadataRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "id"));
	}

	public static final RowMapper<CompanyMetadata> ROW_MAPPER = new RowMapper<CompanyMetadata>() {
		public CompanyMetadata mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new CompanyMetadata(SqlUtils.getInt(true, rs, "id"), 
					SqlUtils.getInt(true, rs, "company_id"),
					rs.getString("type"), 
					rs.getString("title"),
					rs.getString("name"),
					rs.getString("location"));
		}
	};

	private static final RowUnmapper<CompanyMetadata> ROW_UNMAPPER = new RowUnmapper<CompanyMetadata>() {
		public Map<String, Object> mapColumns(CompanyMetadata companyMetadata) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("id", companyMetadata.getId());
			mapping.put("company_id", companyMetadata.getCompanyId());
			mapping.put("type", companyMetadata.getType());
			mapping.put("title", companyMetadata.getTitle());
			mapping.put("name", companyMetadata.getName());
			mapping.put("location", companyMetadata.getLocation());
			
			return mapping;
		}
	};
	
	/**
	 * Given a Company ID and a Metadata Title, it returns the first {@link CompanyMetadata} which arguments matchs or null is there are not any coincidence 
	 * @param companyId
	 * @param title
	 * @return
	 */
	public CompanyMetadata findCompanyMetadataByIdAndTitle(Integer companyId, String title) {
		List<CompanyMetadata> companyMetadatas = getJdbcOperations().query(SQLStatements.FIND_COMPANY_METADATA_BY_COMPANY_ID_AND_TITLE , ROW_MAPPER, companyId, title);
		return companyMetadatas.stream().findFirst().orElse(null);
	}

}
