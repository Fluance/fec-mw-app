package net.fluance.cockpit.core.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateDeserializer;
import net.fluance.commons.json.JsonDateTimeSerializer;

/**
 * This patient representation should be included into any other object in order
 * to make a basic representation of a patient, if more informations are needed
 * then use the Patient Api with the ID. 
 * HATEOAS
 *
 *
 */
public class PatientReference implements Serializable{

	private Long pid;
	private String firstName;
	private String lastName;
	private String maidenName;
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date birthDate;

	public PatientReference(){}
	public PatientReference(Long pid, String firstName, String lastName, String maidenName, Date birthDate) {
		super();
		this.pid = pid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.maidenName = maidenName;
		this.birthDate = birthDate;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMaidenName() {
		return maidenName;
	}

	public void setMaidenName(String maidenName) {
		this.maidenName = maidenName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

}
