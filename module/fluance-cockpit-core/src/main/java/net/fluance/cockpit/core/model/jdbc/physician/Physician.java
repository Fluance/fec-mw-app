/**
 * 
 */
package net.fluance.cockpit.core.model.jdbc.physician;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
public class Physician implements Persistable<Integer> {
	
	private Integer id;
	private String firstname;
	private String lastname;
	private String prefix;
	private Integer staffId;
	private String alternateId;
	private String alternateIdName;
	private String speciality;
	private String address;
	private String locality;
	private String postCode;
	private String canton;
	private String country;
	private String complement;
	private String language;
	private Integer companyId;
	private String phySpecialityDesc;
	
	public Physician(Integer id, String firstname, String lastname, String prefix, Integer staffId, String alternateId, String alternateIdName, String speciality, String address, String locality, String postCode, String canton,
			String country, String complement, String language, Integer companyId, String phySpecialityDesc) {
		//TODO: remove this
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.prefix = prefix;
		this.staffId = staffId;
		this.alternateId = alternateId;
		this.alternateIdName = alternateIdName;
		this.speciality = speciality;
		this.address = address;
		this.locality = locality;
		this.postCode = postCode;
		this.canton = canton;
		this.country = country;
		this.complement = complement;
		this.language = language;
		this.companyId = companyId;
		this.phySpecialityDesc = phySpecialityDesc;
	}

	
	public String getFirstname() {
		return firstname;
	}

	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	
	public String getLastname() {
		return lastname;
	}

	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	
	public String getPrefix() {
		return prefix;
	}

	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	
	public Integer getStaffId() {
		return staffId;
	}

	
	public void setStaffId(Integer staffId) {
		this.staffId = staffId;
	}

	
	public String getAlternateId() {
		return alternateId;
	}

	
	public void setAlternateId(String alternateId) {
		this.alternateId = alternateId;
	}

	
	public String getAlternateIdName() {
		return alternateIdName;
	}

	
	public void setAlternateIdName(String alternateIdName) {
		this.alternateIdName = alternateIdName;
	}

	
	public String getSpeciality() {
		return speciality;
	}

	
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
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

	
	public String getComplement() {
		return complement;
	}

	
	public void setComplement(String complement) {
		this.complement = complement;
	}

	
	public String getLanguage() {
		return language;
	}

	
	public void setLanguage(String language) {
		this.language = language;
	}

	
	public Integer getCompanyId() {
		return companyId;
	}

	
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	
	public String getPhySpecialityDesc() {
		return phySpecialityDesc;
	}

	
	public void setPhySpecialityDesc(String phySpecialityDesc) {
		this.phySpecialityDesc = phySpecialityDesc;
	}

	
	public void setId(Integer id) {
		this.id = id;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public Integer getId() {
		return id;
	}
}