package net.fluance.cockpit.core.model;


public class DoctorReferenceId {
	
	private String staffId;
	private Integer companyId;
	private Integer resourceId;
	
	public DoctorReferenceId(String staffId, Integer companyId, Integer resourceId) {
		super();
		this.staffId = staffId;
		this.companyId = companyId;
		this.resourceId = resourceId;
	}

	public String getStaffId() {
		return staffId;
	}
	
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	
	public Integer getCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	public Integer getResourceId() {
		return resourceId;
	}
	
	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
	
}
