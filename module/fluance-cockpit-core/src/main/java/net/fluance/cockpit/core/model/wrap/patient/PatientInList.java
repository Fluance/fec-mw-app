package net.fluance.cockpit.core.model.wrap.patient;

import java.sql.Timestamp;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.fluance.cockpit.core.model.PayloadDisplayLogName;

public class PatientInList  implements PayloadDisplayLogName{
	@JsonProperty("nbRecords")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer nb_records;
	private PatientInListPersonalInfo patientInfo;
	private PatientInListAddress address;
	private PatientLastVisit lastVisit;

	public PatientInList() {}

	/**
	 * @param personalInfo
	 * @param lastVisit
	 */
	public PatientInList(Integer nbRecords, PatientInListPersonalInfo personalInfo, PatientInListAddress address, PatientLastVisit lastVisit) {
		this.nb_records = nbRecords;
		this.patientInfo = personalInfo;
		this.address = address;
		if (address != null & patientInfo != null){
			patientInfo.setAddress(address.getAddress());
			patientInfo.setPostcode(address.getPostCode());
			patientInfo.setLocality(address.getLocality());
		}
		this.lastVisit = lastVisit;
	}

	public PatientInList(Integer nbRecords, Long pid, String firstname, String lastname, String maidenName,
			Date birthDate, String sex,  boolean death, Date deathdt, String address, String locality, String postCode, Long nb, Long companyId,  Timestamp admitDate, Timestamp dischargeDate, 
			String patientClass, String patientClassDesc, String patientType, String patientTypeDesc, String patientCase, String patientCaseDesc,
			String hospService, String hospServiceDesc, String admissionType, String admissionTypeDesc,
			String patientUnit, String patientUnitDesc, String patientRoom, String patientBed, String financialClass,
			String financialClassDesc) {
		this(nbRecords, new PatientInListPersonalInfo(pid, firstname, lastname, maidenName, birthDate, sex, death, deathdt), 
				new PatientInListAddress(address, locality, postCode),
				new PatientLastVisit(nb, companyId, admitDate, dischargeDate, patientClass, patientClassDesc, patientType, patientTypeDesc, patientCase,
						patientCaseDesc, hospService, hospServiceDesc, admissionType, admissionTypeDesc, patientUnit,
						patientUnitDesc, patientRoom, patientBed, financialClass, financialClassDesc));
	}

	public Integer getNb_records() {
		return nb_records;
	}

	public void setNb_records(Integer nb_records) {
		this.nb_records = nb_records;
	}

	/**
	 * @return the personalInfo
	 */
	public PatientInListPersonalInfo getPatientInfo() {
		return patientInfo;
	}

	/**
	 * @param personalInfo
	 *            the personalInfo to set
	 */
	public void setPersonalInfo(PatientInListPersonalInfo personalInfo) {
		this.patientInfo = personalInfo;
	}
	
	/**
	 * @return the address
	 */
	@JsonIgnore
	public PatientInListAddress getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(PatientInListAddress address) {
		this.address = address;
	}

	/**
	 * @return the lastVisit
	 */
	public PatientLastVisit getLastVisit() {
		return lastVisit;
	}

	/**
	 * @param lastVisit
	 *            the lastVisit to set
	 */
	public void setLastVisit(PatientLastVisit lastVisit) {
		this.lastVisit = lastVisit;
	}

	@JsonIgnore
	@Override
	public String displayName() {
		return this.getPatientInfo().getFirstName() + " " + this.getPatientInfo().getLastName();
	}
}
