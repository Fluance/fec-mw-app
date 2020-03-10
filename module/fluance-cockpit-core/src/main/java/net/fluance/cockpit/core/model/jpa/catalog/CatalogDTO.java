package net.fluance.cockpit.core.model.jpa.catalog;

import java.util.Map;

public class CatalogDTO {
	
	private String companyCode;
	private String code;
	private String lang;
	private String type;
	private String codeDesc;
	private String category;
	private Map<String, String> extra;
	
	public CatalogDTO() {
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

	public Map<String, String> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, String> extra) {
		this.extra = extra;
	}	
	
}

