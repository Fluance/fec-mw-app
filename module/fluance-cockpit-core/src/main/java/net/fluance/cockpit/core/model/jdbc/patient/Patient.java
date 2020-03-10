package net.fluance.cockpit.core.model.jdbc.patient;

import java.util.Date;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateTimeSerializer;

/**
 * {@link Persistable} which maps the <b>bmv_patient_detail</b> Database View
 *
 *
 */
@SuppressWarnings("serial")
public class Patient implements Persistable<Long> {

	// Personal info
	private Long pid;
	private String language;
	private String courtesy;
	private String firstName;
	private String lastName;
	private String maidenName;
	private Date birthDate;
	private String avsNumber;
	private String nationality;
	private String sex;
	// Address info
	private String address;
	private String address2;
	private String locality;
	private String postCode;
	private String subPostCode;
	private String adressComplement;
	private String careOf;
	private String canton;
	private String country;
	private boolean death;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date deathdt;
	
	private String maritalStatus;
	
	public Patient(){}

	/**
	 * 
	 * @param pid
	 * @param language
	 * @param firstname
	 * @param lastname
	 * @param maidenName
	 * @param birthDate
	 * @param avsNumber
	 * @param nationality
	 * @param sex
	 * @param addressLine
	 * @param addressLine2
	 * @param locality
	 * @param postCode
	 * @param subPostCode
	 * @param adressComplement
	 * @param careOf
	 * @param canton
	 * @param country
	 * @param death
	 * @param deathdt
	 * @param maritalstatus
	 */
	public Patient(Long pid, String language, String courtesy, String firstname, String lastname, String maidenName,
			Date birthDate, String avsNumber, String nationality, String sex, String address, String address2, String locality, String postCode, String subPostCode,
			String adressComplement, String careOf, String canton, String country, boolean death, Date deathdt, String maritalStatus) {
		this.pid = pid;
		this.language = language;
		this.courtesy = courtesy;
		this.firstName = firstname;
		this.lastName = lastname;
		this.maidenName = maidenName;
		this.birthDate = birthDate;
		this.avsNumber = avsNumber;
		this.nationality = nationality;
		this.sex = sex;
		this.address = address;
		this.address2 = address2;
		this.locality = locality;
		this.postCode = postCode;
		this.subPostCode = subPostCode;
		this.adressComplement = adressComplement;
		this.careOf = careOf;
		this.canton = canton;
		this.country = country;
		this.death = death;
		this.deathdt = deathdt;
		this.maritalStatus = maritalStatus;
	}

	/**
	 * @return the pid
	 */
	public Long getPid() {
		return pid;
	}

	/**
	 * @param pid the pid to set
	 */
	public void setPid(Long pid) {
		this.pid = pid;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCourtesy() {
		return courtesy;
	}

	public void setCourtesy(String courtesy) {
		this.courtesy = courtesy;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the maidenName
	 */
	public String getMaidenName() {
		return maidenName;
	}

	/**
	 * @param maidenName the maidenName to set
	 */
	public void setMaidenName(String maidenName) {
		this.maidenName = maidenName;
	}

	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the avsNumber
	 */
	public String getAvsNumber() {
		return avsNumber;
	}

	/**
	 * @param avsNumber the avsNumber to set
	 */
	public void setAvsNumber(String avsNumber) {
		this.avsNumber = avsNumber;
	}

	/**
	 * @return the nationality
	 */
	public String getNationality() {
		return nationality;
	}

	/**
	 * @param nationality the nationality to set
	 */
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * @return the address2
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param address2 the address2 to set
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the locality
	 */
	public String getLocality() {
		return locality;
	}

	/**
	 * @param locality the locality to set
	 */
	public void setLocality(String locality) {
		this.locality = locality;
	}

	/**
	 * @return the postCode
	 */
	public String getPostCode() {
		return postCode;
	}

	/**
	 * @param postCode the postCode to set
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	/**
	 * @return the subPostCode
	 */
	public String getSubPostCode() {
		return subPostCode;
	}

	/**
	 * @param subPostCode the subPostCode to set
	 */
	public void setSubPostCode(String subPostCode) {
		this.subPostCode = subPostCode;
	}

	/**
	 * @return the adressComplement
	 */
	public String getAdressComplement() {
		return adressComplement;
	}

	/**
	 * @param adressComplement the adressComplement to set
	 */
	public void setAdressComplement(String adressComplement) {
		this.adressComplement = adressComplement;
	}

	/**
	 * @return the careOf
	 */
	public String getCareOf() {
		return careOf;
	}

	/**
	 * @param careOf the careOf to set
	 */
	public void setCareOf(String careOf) {
		this.careOf = careOf;
	}

	/**
	 * @return the canton
	 */
	public String getCanton() {
		return canton;
	}

	/**
	 * @param canton the canton to set
	 */
	public void setCanton(String canton) {
		this.canton = canton;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the death
	 */
	public boolean isDeath() {
		return death;
	}

	/**
	 * @param death the death to set
	 */
	public void setDeath(boolean death) {
		this.death = death;
	}

	/**
	 * @return the deathdt
	 */
	public Date getDeathdt() {
		return deathdt;
	}

	/**
	 * @param deathdt the deathdt to set
	 */
	public void setDeathdt(Date deathdt) {
		this.deathdt = deathdt;
	}
	
	public String getMaritalStatus() {
		return maritalStatus;
	}
	
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	@JsonIgnore
	@Override
	public Long getId() {
		return pid;
	}
	
	@JsonIgnore
	@Override
	public boolean isNew() {
		// id must be null for a new (unsaved) entity
		// when the id is auto-generated
		return pid == null;
	}
	
}

