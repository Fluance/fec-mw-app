package net.fluance.cockpit.core.model.jdbc.patient;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JDBC from the <b>bmv_nextofkin_contacts_list</b> and <b>bmv_patient_nextofkins_list</b> Database Views
 * 
 *
 */
@SuppressWarnings("serial")
public class PatientNextOfKin implements Persistable<Long> {
	
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
	private String equipment;
	private String data;

	public PatientNextOfKin() {}

	public PatientNextOfKin(Integer nbRecords, Long id, String firstName, String lastName, Long patientId,
			String addressType, String address, String address2, String complement, String locality, String postCode,
			String canton, String country, String type, String careOf, String equipment, String data) {
		super();
		this.nbRecords = nbRecords;
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.patientId = patientId;
		this.addressType = addressType;
		this.address = address;
		this.address2 = address2;
		this.complement = complement;
		this.locality = locality;
		this.postCode = postCode;
		this.canton = canton;
		this.country = country;
		this.type = type;
		this.careOf = careOf;
		this.equipment = equipment;
		this.data = data;
	}

	/**
	 * @return the nbRecords
	 */
	public Integer getNbRecords() {
		return nbRecords;
	}

	/**
	 * @param nbRecords the nbRecords to set
	 */
	public void setNbRecords(Integer nbRecords) {
		this.nbRecords = nbRecords;
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
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		// id must be null for a new (unsaved) entity
		// when the id is auto-generated
		return id == null;
	}

	@JsonIgnore
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * @return the patientId
	 */
	public Long getPatientId() {
		return patientId;
	}

	/**
	 * @param patientId the patientId to set
	 */
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	/**
	 * @return the addressType
	 */
	public String getAddressType() {
		return addressType;
	}

	/**
	 * @param addressType the addressType to set
	 */
	public void setAddressType(String addressType) {
		this.addressType = addressType;
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the complement
	 */
	public String getComplement() {
		return complement;
	}

	/**
	 * @param complement the complement to set
	 */
	public void setComplement(String complement) {
		this.complement = complement;
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

	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
}