/**
 * 
 */
package net.fluance.cockpit.core.model.wrap.patient;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateTimeSerializer;


public class PatientInListPersonalInfo {

	private Long pid;
	private String firstName;
	private String lastName;
	private String maidenName;
	private Date birthDate;
	private String sex;
	private String address;
	private String postcode;
	private String locality;
	private boolean death;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date deathdt;
	
	/**
	 * 
	 */
	public PatientInListPersonalInfo() {}

	/**
	 * @param pid
	 * @param language
	 * @param firstname
	 * @param lastname
	 * @param maidenName
	 * @param birthDate
	 * @param avsNumber
	 * @param nationality
	 * @param sex
	 */
	public PatientInListPersonalInfo(Long pid, String firstname, String lastname, String maidenName,
			Date birthDate, String sex, boolean death, Date deathdt ) {
		this.pid = pid;
		this.firstName = firstname;
		this.lastName = lastname;
		this.maidenName = maidenName;
		this.birthDate = birthDate;
		this.sex = sex;
		this.death = death;
		this.deathdt = deathdt;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstName(String firstname) {
		this.firstName = firstname;
	}
	
	/**
	 * @return the lastname
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * @param lastname the lastname to set
	 */
	public void setLastName(String lastname) {
		this.lastName = lastname;
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

	public boolean isDeath() {
		return death;
	}

	public void setDeath(boolean death) {
		this.death = death;
	}

	public Date getDeathdt() {
		return deathdt;
	}

	public void setDeathdt(Date deathdt) {
		this.deathdt = deathdt;
	}
	
}
