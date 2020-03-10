/**
 * 
 */
package net.fluance.cockpit.core.model.wrap.patient;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateTimeSerializer;

/**
 * DTO for the info of a {@link net.fluance.cockpit.core.model.jdbc.patient.Patient}
 *
 */
public class PersonalInfo {

	private Long pid;
	private String language;
	private String courtesy;
	private String firstName;
	private String lastName;
	private String maidenName;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using=JsonDateTimeSerializer.class)
	private Date birthDate;
	private String avsNumber;
	private String nationality;
	private String sex;
	private boolean death;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date deathdt;
	
	private String maritalStatus;
	
	/**
	 * 
	 */
	public PersonalInfo() {}

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
	 * @param maritalStatus
	 */
	public PersonalInfo(Long pid, String language, String courtesy, String firstname, String lastname, String maidenName,
			Date birthDate, String avsNumber, String nationality, String sex, boolean death, Date deathdt, String maritalStatus) {
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
		this.death = death;
		this.deathdt = deathdt;
		this.maritalStatus = maritalStatus;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
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
	
}
