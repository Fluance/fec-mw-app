package net.fluance.cockpit.core.repository.jdbc.catalog;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.catalog.CatalogMapper;
import net.fluance.cockpit.core.model.jpa.catalog.Catalog;
import net.fluance.cockpit.core.model.jpa.catalog.CatalogDTO;
import net.fluance.cockpit.core.model.jpa.catalog.CatalogPK;

@Repository
@Component
public class CatalogRepository extends JdbcRepository<Catalog, CatalogPK> {
	
	public CatalogRepository() {
		this(MappingsConfig.TABLE_NAMES.get("whiteboard_catalog"));
	}

	public CatalogRepository(String tableName) {
		 super(CatalogMapper.ROW_MAPPER, CatalogMapper.ROW_UNMAPPER, new TableDescription(tableName, null, ""));
	}
	
	public List<CatalogDTO> getCatalogs(String companyCode, String type, String language) {
		List<Catalog> catalogs = new ArrayList();
		if(!StringUtils.isEmpty(language)) {
			catalogs = getJdbcOperations().query(SQLStatements.FIND_CATALOG_LIST_BY_COMPANYID_AND_TYPE + SQLStatements.AND_LANG, CatalogMapper.ROW_MAPPER, companyCode, type, language);
		}else {
			catalogs = getJdbcOperations().query(SQLStatements.FIND_CATALOG_LIST_BY_COMPANYID_AND_TYPE, CatalogMapper.ROW_MAPPER, companyCode, type);
		}
		
		List<CatalogDTO> results = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(catalogs)) {
			for (Catalog catalog : catalogs) {
				results.add(CatalogMapper.toModel(catalog));
			}
		}
		return results;
	}

	public CatalogDTO getCatalogByPK(String companyCode, String code, String language, String type) {
		return CatalogMapper.toModel(getJdbcOperations().queryForObject(SQLStatements.CATALOG_BY_PK, CatalogMapper.ROW_MAPPER, companyCode, language, type, code));
	}
}
