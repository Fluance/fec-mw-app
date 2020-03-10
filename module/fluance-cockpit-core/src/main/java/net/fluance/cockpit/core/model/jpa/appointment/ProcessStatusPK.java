package net.fluance.cockpit.core.model.jpa.appointment;

import java.io.Serializable;

import javax.persistence.Column;

public class ProcessStatusPK implements Serializable {	
	private static final long serialVersionUID = 6376583849388383756L;

	@Column(name="id")
	private Integer id;
	
	@Column(name="psid")
	private Integer processStatusId;
	
	@Column(name="company_id")
	private Integer companyId;

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProcessStatusId() {
		return processStatusId;
	}

	public void setProcessStatusId(Integer processStatusId) {
		this.processStatusId = processStatusId;
	}
	
	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
