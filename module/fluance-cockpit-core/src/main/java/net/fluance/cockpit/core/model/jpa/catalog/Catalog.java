package net.fluance.cockpit.core.model.jpa.catalog;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.fluance.cockpit.core.model.jdbc.catalog.CatalogMapper;

public class Catalog implements Persistable<CatalogPK>, Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private CatalogPK id;
	
	@Column(name = CatalogMapper.CODE_DESC)
	private String codeDesc;
	
	@Column(name = CatalogMapper.CATEGORY)
	private String category;

	@Column(name = CatalogMapper.EXTRA_1)
	private Map<String, String> extra;
	
	public Catalog() {
		//EMPTY
	}

	public String getCodeDesc() {
		return codeDesc;
	}
	
	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}
	
	public String getCategory() {
		return category;
	}

	
	public void setCategory(String category) {
		this.category = category;
	}

	
	public void setId(CatalogPK id) {
		this.id = id;
	}

	public Map<String, String> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, String> extra) {
		this.extra = extra;
	}
	
	@JsonIgnore
	@Override
	public CatalogPK getId() {
		return id;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}
		
}
