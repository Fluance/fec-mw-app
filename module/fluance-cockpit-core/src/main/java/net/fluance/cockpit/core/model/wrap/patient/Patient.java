package net.fluance.cockpit.core.model.wrap.patient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.fluance.cockpit.core.model.PayloadDisplayLogName;
import net.fluance.cockpit.core.model.jdbc.patient.PatientContact;

public class Patient implements PayloadDisplayLogName{

	private PersonalInfo patientInfo;
	private PatientAddress address;
	private List<PatientContact> contacts = new ArrayList<PatientContact>();
	
	public Patient(){}

	/**
	 * @param personalInfo
	 * @param address
	 * @param patientContacts
	 */
	public Patient(PersonalInfo personalInfo, PatientAddress address, List<PatientContact> patientContacts) {
		super();
		this.patientInfo = personalInfo;
		this.address = address;
		// Better to have an empty list than a null
		if (patientContacts != null){
			this.contacts = patientContacts;
		}
	}
	
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
	 * @param maritalStatus
	 */
	public Patient(Long pid, String language, String courtesy, String firstname, String lastname, String maidenName,
			Date birthDate, String avsNumber, String nationality, String sex, boolean death, Date deathdt, String address, String address2,String locality, String postCode, String subPostCode,
			String adressComplement, String careOf, String canton, String country, String maritalStatus) {
		this(new PersonalInfo(pid, language, courtesy, firstname, lastname, maidenName, birthDate, avsNumber, nationality, sex, death, deathdt, maritalStatus), new PatientAddress(address, address2, locality, postCode, subPostCode, adressComplement, careOf, canton, country), null);
	}

	public PersonalInfo getPatientInfo() {
		return patientInfo;
	}

	public void setPatientInfo(PersonalInfo patientInfo) {
		this.patientInfo = patientInfo;
	}

	/**
	 * @return the address
	 */
	public PatientAddress getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(PatientAddress address) {
		this.address = address;
	}

	public List<PatientContact> getContacts() {
		return contacts;
	}

	public void setContacts(List<PatientContact> contactsList) {
		this.contacts = contactsList;
	}

	@JsonIgnore
	@Override
	public String displayName() {
		return this.getPatientInfo().getFirstName() + " " + this.getPatientInfo().getLastName();
	}
}

