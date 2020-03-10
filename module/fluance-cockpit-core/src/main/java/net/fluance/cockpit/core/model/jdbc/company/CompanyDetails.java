/**
 * 
 */
package net.fluance.cockpit.core.model.jdbc.company;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
public class CompanyDetails implements Persistable<Integer>{

	private Integer id;
	private String code;
	private String name;
	private String address;
	private String canton;
	private String country;
	private String email;
	private String fax;
	private String locality;
	private String phone;
	private Integer postcode;
	private String preflang;
	private String logo;
	
	public CompanyDetails(Integer id, String code, String name, String address, String canton, String country, String email,
			String fax, String locality, String phone, Integer postcode, String preflang) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.address = address;
		this.canton = canton;
		this.country = country;
		this.email = email;
		this.fax = fax;
		this.locality = locality;
		this.phone = phone;
		this.postcode = postcode;
		this.preflang = preflang;
	}
	
	public CompanyDetails() {}
	
	@Override
	public String toString() {
		return "Company [id=" + id + ", code=" + code + ", name=" + name + ", address=" + address + ", canton=" + canton
				+ ", country=" + country + ", email=" + email + ", fax=" + fax + ", locality=" + locality + ", phone="
				+ phone + ", postcode=" + postcode + ", preflang=" + preflang + "]";
	}

	@Override
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
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


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getFax() {
		return fax;
	}


	public void setFax(String fax) {
		this.fax = fax;
	}


	public String getLocality() {
		return locality;
	}


	public void setLocality(String locality) {
		this.locality = locality;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public Integer getPostcode() {
		return postcode;
	}


	public void setPostcode(Integer postcode) {
		this.postcode = postcode;
	}


	public String getPreflang() {
		return preflang;
	}


	public void setPreflang(String preflang) {
		this.preflang = preflang;
	}

	
	public String getLogo() {
		return logo;
	}

	
	public void setLogo(String logo) {
		this.logo = logo;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return id == null;
	}
}
