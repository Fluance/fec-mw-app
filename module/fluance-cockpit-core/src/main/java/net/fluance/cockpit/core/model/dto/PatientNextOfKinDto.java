package net.fluance.cockpit.core.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO to manage the info returned as {@code PatientNextOfKin}
 * @author jcs
 *
 */
public class PatientNextOfKinDto {

	private Integer nbRecords;
	@JsonProperty("nextOfKinId")
	private Long id;
	private String firstName;
	private String lastName;
	private Long patientId;
	private String addressType;
	private String address;
	private String address2;
	private String complement;
	private String locality;
	private String postCode;
	private String canton;
	private String country;
	private String type;
	private String careOf;
	private List<String> emails;
	private List<String> telephones;
	
	public Integer getNbRecords() {
		return nbRecords;
	}
	public void setNbRecords(Integer nbRecords) {
		this.nbRecords = nbRecords;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Long getPatientId() {
		return patientId;
	}
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}
	public String getAddressType() {
		return addressType;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
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
	public String getComplement() {
		return complement;
	}
	public void setComplement(String complement) {
		this.complement = complement;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCareOf() {
		return careOf;
	}
	public void setCareOf(String careOf) {
		this.careOf = careOf;
	}
	public List<String> getEmails() {
		return emails;
	}
	public void setEmails(List<String> emails) {
		this.emails = emails;
	}
	public List<String> getTelephones() {
		return telephones;
	}
	public void setTelephones(List<String> telephones) {
		this.telephones = telephones;
	}
}
