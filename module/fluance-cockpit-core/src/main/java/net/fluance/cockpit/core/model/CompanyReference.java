package net.fluance.cockpit.core.model;

import java.io.Serializable;

public class CompanyReference implements Serializable{

	private Integer companyId;
	private String name;
	private String code;
	
	public CompanyReference(Integer companyId, String name, String code) {
		super();
		this.companyId = companyId;
		this.name = name;
		this.code = code;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
