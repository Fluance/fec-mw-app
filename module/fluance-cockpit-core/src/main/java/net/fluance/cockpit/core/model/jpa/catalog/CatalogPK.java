package net.fluance.cockpit.core.model.jpa.catalog;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import net.fluance.cockpit.core.model.jdbc.catalog.CatalogMapper;

@Embeddable
public class CatalogPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = CatalogMapper.COMPANY_CODE)
	private String companyCode;
	
	@Column(name = CatalogMapper.CODE)
	private String code;
	
	@Column(name = CatalogMapper.LANG)
	private String lang;
	
	@Column(name = CatalogMapper.TYPE)
	private String type;

	
	public CatalogPK() {
		//EMPTY
	}


	public String getCompanyCode() {
		return companyCode;
	}

	
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	
	public String getCode() {
		return code;
	}

	
	public void setCode(String code) {
		this.code = code;
	}

	
	public String getLang() {
		return lang;
	}

	
	public void setLang(String lang) {
		this.lang = lang;
	}

	
	public String getType() {
		return type;
	}

	
	public void setType(String type) {
		this.type = type;
	}
	
	
}
