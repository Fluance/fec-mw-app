package net.fluance.cockpit.core.model.jdbc.catalog;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.springframework.jdbc.core.RowMapper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nurkiewicz.jdbcrepository.RowUnmapper;

import net.fluance.cockpit.core.model.jpa.catalog.Catalog;
import net.fluance.cockpit.core.model.jpa.catalog.CatalogDTO;
import net.fluance.cockpit.core.model.jpa.catalog.CatalogPK;
import net.fluance.commons.sql.SqlUtils;

public class CatalogMapper {
	
	private static final Logger LOGGER = LogManager.getLogger(CatalogMapper.class);
	
	public static final String COMPANY_CODE = "company_code";
	public static final String CODE = "code";
	public static final String CODE_DESC = "codedesc";
	public static final String LANG = "lang";
	public static final String CATEGORY = "category";
	public static final String TYPE = "type";
	public static final String EXTRA_1 = "extra_1";
	
	private CatalogMapper() {
		throw new IllegalStateException("Utility class");
	}
	
	public static CatalogDTO toModel(Catalog catalog) {
		CatalogDTO model = null;
		if (catalog != null) {
			model = new CatalogDTO();
			model.setCategory(catalog.getCategory());
			model.setCodeDesc(catalog.getCodeDesc());
			model.setExtra(catalog.getExtra());
			
			//This four attributes are the PK
			if(catalog.getId() != null) {
				model.setCode(catalog.getId().getCode());
				model.setCompanyCode(catalog.getId().getCompanyCode());
				model.setLang(catalog.getId().getLang());
				model.setType(catalog.getId().getType());
			}
		}		
		return model;
	}
		
	public static Catalog toEntity(CatalogDTO model) {
		Catalog catalog = null;
		if(model != null) {
			catalog = new Catalog();
			CatalogPK pk = new CatalogPK();
			pk.setCode(model.getCode());
			pk.setCompanyCode(model.getCompanyCode());
			pk.setLang(model.getLang());
			pk.setType(model.getType());
			catalog.setId(pk);
			catalog.setCategory(model.getCategory());
			catalog.setCodeDesc(model.getCodeDesc());
			catalog.setExtra(model.getExtra());
		}
		return catalog;
	}
	
	
	public static final RowMapper<Catalog> ROW_MAPPER = (resultSet, rowNumber) -> {

	    if (resultSet == null) {
	      LOGGER.info("ResultSet is null");
	      return null;
	    }
	    Catalog catalog = new Catalog();
	    
	    CatalogPK pk = new CatalogPK();
		pk.setCode(SqlUtils.getString(true, resultSet, CODE));
		pk.setCompanyCode(SqlUtils.getString(true, resultSet, COMPANY_CODE));
		pk.setLang(SqlUtils.getString(true, resultSet, LANG));
		pk.setType(SqlUtils.getString(true, resultSet, TYPE));
	    
		catalog.setId(pk);
	    catalog.setCodeDesc(SqlUtils.getString(true, resultSet, CODE_DESC));
	    catalog.setCategory(SqlUtils.getString(true, resultSet, CATEGORY));
	    
	    try {
	    	catalog.setExtra(toMap(SqlUtils.getString(true, resultSet, EXTRA_1)));
		} catch (JSONException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error when buildint extra content");
		}
	    
	    return catalog;		
	};
	
	private static Map<String, String> toMap(String jsonString) throws JsonParseException, JsonMappingException, JSONException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		if(jsonString!=null && !jsonString.isEmpty()){
			return mapper.readValue(jsonString, mapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));
		} else {
			return new HashMap();
		}
	}

	public static final RowUnmapper<Catalog> ROW_UNMAPPER = catalog -> {
	    Map<String, Object> mapping = new LinkedHashMap<>();
	    if(catalog != null && catalog.getId() != null) {
		    mapping.put(COMPANY_CODE, catalog.getId().getCompanyCode());
		    mapping.put(CODE, catalog.getId().getCode());
		    mapping.put(LANG, catalog.getId().getLang());
		    mapping.put(TYPE, catalog.getId().getType());
		    mapping.put(CATEGORY, catalog.getCategory());
		    mapping.put(CODE_DESC, catalog.getCodeDesc());
		    mapping.put(EXTRA_1, catalog.getExtra());
	    }	    
	    return mapping;
	};
}
