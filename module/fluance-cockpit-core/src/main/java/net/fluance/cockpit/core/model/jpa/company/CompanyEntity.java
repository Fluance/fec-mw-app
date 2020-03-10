package net.fluance.cockpit.core.model.jpa.company;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="company")
@Table(name="COMPANY")
public class CompanyEntity {

	@Id
	@Column(name = "id")
	private Integer companyId;
	
	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "address")
	private String address;

	@Column(name = "locality")
	private String locality;

	@Column(name = "postcode")
	private String postCode;

	@Column(name = "canton")
	private String canton;

	@Column(name = "country")
	private String country;

	@Column(name = "phone")
	private String phone;

	@Column(name = "fax")
	private String fax;

	@Column(name = "email")
	private String email;

	@Column(name = "preflang", columnDefinition = "bpchar")
	private String prefLang;

	
	public Integer getCompanyId() {
		return companyId;
	}

	
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	
	public String getCode() {
		return code;
	}

	
	public void setCode(String code) {
		this.code = code;
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

	
	public String getLocality() {
		return locality;
	}

	
	public void setLocality(String locality) {
		this.locality = locality;
	}

	
	public String getPostCode() {
		return postCode;
	}

	
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	
	public String getCanton() {
		return canton;
	}

	
	public void setCanton(String canton) {
		this.canton = canton;
	}

	
	public String getCountry() {
		return country;
	}

	
	public void setCountry(String country) {
		this.country = country;
	}

	
	public String getPhone() {
		return phone;
	}

	
	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	public String getFax() {
		return fax;
	}

	
	public void setFax(String fax) {
		this.fax = fax;
	}

	
	public String getEmail() {
		return email;
	}

	
	public void setEmail(String email) {
		this.email = email;
	}

	
	public String getPrefLang() {
		return prefLang;
	}

	
	public void setPrefLang(String prefLang) {
		this.prefLang = prefLang;
	}
}
