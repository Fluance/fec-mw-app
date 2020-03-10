package net.fluance.cockpit.core.model.jdbc.resourcepersonnel;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResourcePersonnel implements Persistable<String> {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private Integer company_id;
	private Integer rsid;
	private String staffid;
	private String role;
	private String name;
	private String address;
	private String address2;
	private String postcode;
	private String locality;
	private String internalphone;
	private String privatephone;
	private String altphone;
	private String fax;
	private String source;
	private String sourceid;
		
	public ResourcePersonnel(String id, Integer company_id, Integer rsid, String staffid, String role, String name,
			String address, String address2, String postcode, String locality, String internalphone,
			String privatephone, String altphone, String fax, String source, String sourceid) {
		//TODO: remove this
		this.id = id;
		this.company_id = company_id;
		this.rsid = rsid;
		this.staffid = staffid;
		this.role = role;
		this.name = name;
		this.address = address;
		this.address2 = address2;
		this.postcode = postcode;
		this.locality = locality;
		this.internalphone = internalphone;
		this.privatephone = privatephone;
		this.altphone = altphone;
		this.fax = fax;
		this.source = source;
		this.sourceid = sourceid;
	}

	public ResourcePersonnel() {
		//we need this for cleaner code
	}

	public Integer getCompany_id() {
		return company_id;
	}

	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}

	public Integer getRsid() {
		return rsid;
	}

	public void setRsid(Integer rsid) {
		this.rsid = rsid;
	}

	public String getStaffid() {
		return staffid;
	}

	public void setStaffid(String staffid) {
		this.staffid = staffid;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getInternalphone() {
		return internalphone;
	}

	public void setInternalphone(String internalphone) {
		this.internalphone = internalphone;
	}

	public String getPrivatephone() {
		return privatephone;
	}

	public void setPrivatephone(String privatephone) {
		this.privatephone = privatephone;
	}

	public String getAltphone() {
		return altphone;
	}

	public void setAltphone(String altphone) {
		this.altphone = altphone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceid() {
		return sourceid;
	}

	public void setSourceid(String sourceid) {
		this.sourceid = sourceid;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public String getId() {
		return id;
	}

}
