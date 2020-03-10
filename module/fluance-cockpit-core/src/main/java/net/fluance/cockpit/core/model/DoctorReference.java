package net.fluance.cockpit.core.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateDeserializer;
import net.fluance.commons.json.JsonDateSerializer;

public class DoctorReference {

	private String id;
	private String firstName;
	private String lastName;
	private String gender;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date birthDate;
	private String title;
	private String speciality;
	private String googleToken;
	private String linkedinToken;
	private String email;
	private String externalPhoneNumberOne;
	private String externalPhoneNumbertwo;
	private String internalPhoneNumber;
	private boolean showExternalPhoneNumber;
	private String latitude;
	private String longitude;
	private String pictureUri;
	private String username;
	
	public DoctorReference(){}
	
	public DoctorReference(String gender, Date birthDate, String title, String speciality, String googleToken, String linkedinToken, String email, String externalPhoneNumberOne, String externalPhoneNumbertwo, String latitude,
			String longitude, String pictureUri) {
		this.gender = gender;
		this.birthDate = birthDate;
		this.title = title;
		this.speciality = speciality;
		this.googleToken = googleToken;
		this.linkedinToken = linkedinToken;
		this.email = email;
		this.externalPhoneNumberOne = externalPhoneNumberOne;
		this.externalPhoneNumbertwo = externalPhoneNumbertwo;
		this.latitude = latitude;
		this.longitude = longitude;
		this.pictureUri = pictureUri;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getGoogleToken() {
		return googleToken;
	}

	public void setGoogleToken(String googleToken) {
		this.googleToken = googleToken;
	}

	public String getLinkedinToken() {
		return linkedinToken;
	}

	public void setLinkedinToken(String linkedinToken) {
		this.linkedinToken = linkedinToken;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getExternalPhoneNumberOne() {
		return externalPhoneNumberOne;
	}

	public void setExternalPhoneNumberOne(String externalPhoneNumberOne) {
		this.externalPhoneNumberOne = externalPhoneNumberOne;
	}

	public String getExternalPhoneNumbertwo() {
		return externalPhoneNumbertwo;
	}

	public void setExternalPhoneNumbertwo(String externalPhoneNumbertwo) {
		this.externalPhoneNumbertwo = externalPhoneNumbertwo;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getPictureUri() {
		return pictureUri;
	}

	public void setPictureUri(String pictureUri) {
		this.pictureUri = pictureUri;
	}

	public boolean isShowExternalPhoneNumber() {
		return showExternalPhoneNumber;
	}

	public void setShowExternalPhoneNumber(boolean showExternalPhoneNumber) {
		this.showExternalPhoneNumber = showExternalPhoneNumber;
	}

	
	public String getId() {
		return id;
	}

	
	public void setId(String id) {
		this.id = id;
	}

	public String getInternalPhoneNumber() {
		return internalPhoneNumber;
	}

	public void setInternalPhoneNumber(String internalPhoneNumber) {
		this.internalPhoneNumber = internalPhoneNumber;
	}

	
	public String getUsername() {
		return username;
	}

	
	public void setUsername(String username) {
		this.username = username;
	}
	
	

}
