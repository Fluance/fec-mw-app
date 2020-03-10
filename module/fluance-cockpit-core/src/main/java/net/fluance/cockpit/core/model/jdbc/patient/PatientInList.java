package net.fluance.cockpit.core.model.jdbc.patient;

import java.util.Date;
import java.sql.Timestamp;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateTimeSerializer;

@SuppressWarnings("serial")
public class PatientInList implements Persistable<Long> {

	private Integer nbRecords;
	// Personal info
	private Long pid;
	private String firstName;
	private String lastName;
	private String maidenName;
	private Date birthDate;
	private String sex;
	private String address;
	private String locality;
	private String postCode;
	private boolean death;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date deathdt;
	// Last visit info 
	private Long lastVisitnumber;
	@JsonIgnore
	private Long lastVisitCompanyId;
	private Timestamp lastVisitAdmitDate;
	private Timestamp lastVisitDischargeDate;
	private String lastVisitPatientClass;
	private String lastVisitPatientClassDesc;
	private String lastVisitPatientType;
	private String lastVisitPatientTypeDesc;
	private String lastVisitPatientCase;
	private String lastVisitPatientCaseDesc;
	private String lastVisitHospService;
	private String lastVisitHospServiceDesc;
	private String lastVisitAdmissionType;
	private String lastVisitAdmissionTypeDesc;
	private String lastVisitPatientUnit;
	private String lastVisitPatientUnitDesc;
	private String lastVisitPatientRoom;
	private String lastVisitPatientBed;
	private String lastVisitFinancialClass;
	private String lastVisitFinancialClassDesc;

	public PatientInList() {}

	public PatientInList(Integer nbRecords, Long pid, String firstname, String lastname, String maidenName,
			Date birthDate, String sex, boolean death, Date deathdt, String address, String locality, String postCode, Long nb, Long companyId,  Timestamp admitDate, Timestamp dischargeDate, 
			String patientClass, String patientClassDesc, String patientType, String patientTypeDesc, String patientCase, String patientCaseDesc,
			String hospService, String hospServiceDesc, String admissionType, String admissionTypeDesc,
			String patientUnit, String patientUnitDesc, String patientRoom, String patientBed, String financialClass,
			String financialClassDesc) {
		this.nbRecords = nbRecords;
		this.pid = pid;
		this.firstName = firstname;
		this.lastName = lastname;
		this.maidenName = maidenName;
		this.birthDate = birthDate;
		this.sex = sex;
		this.death = death;
		this.deathdt = deathdt;
		this.address = address;
		this.locality = locality;
		this.postCode = postCode;
		this.lastVisitnumber = nb;
		this.lastVisitCompanyId = companyId;
		this.lastVisitAdmitDate = admitDate;
		this.lastVisitDischargeDate = dischargeDate;
		this.lastVisitPatientClass = patientClass;
		this.lastVisitPatientClassDesc = patientClassDesc;
		this.lastVisitPatientType = patientType;
		this.lastVisitPatientTypeDesc = patientTypeDesc;
		this.lastVisitPatientCase = patientCase;
		this.lastVisitPatientCaseDesc = patientCaseDesc;
		this.lastVisitHospService = hospService;
		this.lastVisitHospServiceDesc = hospServiceDesc;
		this.lastVisitAdmissionType = admissionType;
		this.lastVisitAdmissionTypeDesc = admissionTypeDesc;
		this.lastVisitPatientUnit = patientUnit;
		this.lastVisitPatientUnitDesc = patientUnitDesc;
		this.lastVisitPatientRoom = patientRoom;
		this.lastVisitPatientBed = patientBed;
		this.lastVisitFinancialClass = financialClass;
		this.lastVisitFinancialClassDesc = financialClassDesc;
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
	 * @return the lastVisitnumber
	 */
	public Long getLastVisitnumber() {
		return lastVisitnumber;
	}

	/**
	 * @param lastVisitnumber the lastVisitnumber to set
	 */
	public void setLastVisitnumber(Long lastVisitnumber) {
		this.lastVisitnumber = lastVisitnumber;
	}

	/**
	 * @return the lastVisitCompanyId
	 */
	public Long getLastVisitCompanyId() {
		return lastVisitCompanyId;
	}

	/**
	 * @param lastVisitCompanyId the lastVisitCompanyId to set
	 */
	public void setLastVisitCompanyId(Long lastVisitCompanyId) {
		this.lastVisitCompanyId = lastVisitCompanyId;
	}

	/**
	 * @return the lastVisitAdmitDate
	 */
	public Timestamp getLastVisitAdmitDate() {
		return lastVisitAdmitDate;
	}

	/**
	 * @param lastVisitAdmitDate the lastVisitAdmitDate to set
	 */
	public void setLastVisitAdmitDate(Timestamp lastVisitAdmitDate) {
		this.lastVisitAdmitDate = lastVisitAdmitDate;
	}

	/**
	 * @return the lastVisitDischargeDate
	 */
	public Timestamp getLastVisitDischargeDate() {
		return lastVisitDischargeDate;
	}

	/**
	 * @param lastVisitDischargeDate the lastVisitDischargeDate to set
	 */
	public void setLastVisitDischargeDate(Timestamp lastVisitDischargeDate) {
		this.lastVisitDischargeDate = lastVisitDischargeDate;
	}

	/**
	 * @return the lastVisitPatientClass
	 */
	public String getLastVisitPatientClass() {
		return lastVisitPatientClass;
	}

	/**
	 * @param lastVisitPatientClass the lastVisitPatientClass to set
	 */
	public void setLastVisitPatientClass(String lastVisitPatientClass) {
		this.lastVisitPatientClass = lastVisitPatientClass;
	}

	/**
	 * @return the lastVisitPatientClassDesc
	 */
	public String getLastVisitPatientClassDesc() {
		return lastVisitPatientClassDesc;
	}

	/**
	 * @param lastVisitPatientClassDesc the lastVisitPatientClassDesc to set
	 */
	public void setLastVisitPatientClassDesc(String lastVisitPatientClassDesc) {
		this.lastVisitPatientClassDesc = lastVisitPatientClassDesc;
	}

	/**
	 * @return the lastVisitPatientType
	 */
	public String getLastVisitPatientType() {
		return lastVisitPatientType;
	}

	/**
	 * @param lastVisitPatientType the lastVisitPatientType to set
	 */
	public void setLastVisitPatientType(String lastVisitPatientType) {
		this.lastVisitPatientType = lastVisitPatientType;
	}

	/**
	 * @return the lastVisitPatientTypeDesc
	 */
	public String getLastVisitPatientTypeDesc() {
		return lastVisitPatientTypeDesc;
	}

	/**
	 * @param lastVisitPatientTypeDesc the lastVisitPatientTypeDesc to set
	 */
	public void setLastVisitPatientTypeDesc(String lastVisitPatientTypeDesc) {
		this.lastVisitPatientTypeDesc = lastVisitPatientTypeDesc;
	}

	/**
	 * @return the lastVisitPatientCase
	 */
	public String getLastVisitPatientCase() {
		return lastVisitPatientCase;
	}

	/**
	 * @param lastVisitPatientCase the lastVisitPatientCase to set
	 */
	public void setLastVisitPatientCase(String lastVisitPatientCase) {
		this.lastVisitPatientCase = lastVisitPatientCase;
	}

	/**
	 * @return the lastVisitPatientCaseDesc
	 */
	public String getLastVisitPatientCaseDesc() {
		return lastVisitPatientCaseDesc;
	}

	/**
	 * @param lastVisitPatientCaseDesc the lastVisitPatientCaseDesc to set
	 */
	public void setLastVisitPatientCaseDesc(String lastVisitPatientCaseDesc) {
		this.lastVisitPatientCaseDesc = lastVisitPatientCaseDesc;
	}

	/**
	 * @return the lastVisitHospService
	 */
	public String getLastVisitHospService() {
		return lastVisitHospService;
	}

	/**
	 * @param lastVisitHospService the lastVisitHospService to set
	 */
	public void setLastVisitHospService(String lastVisitHospService) {
		this.lastVisitHospService = lastVisitHospService;
	}

	/**
	 * @return the lastVisitHospServiceDesc
	 */
	public String getLastVisitHospServiceDesc() {
		return lastVisitHospServiceDesc;
	}

	/**
	 * @param lastVisitHospServiceDesc the lastVisitHospServiceDesc to set
	 */
	public void setLastVisitHospServiceDesc(String lastVisitHospServiceDesc) {
		this.lastVisitHospServiceDesc = lastVisitHospServiceDesc;
	}

	/**
	 * @return the lastVisitAdmissionType
	 */
	public String getLastVisitAdmissionType() {
		return lastVisitAdmissionType;
	}

	/**
	 * @param lastVisitAdmissionType the lastVisitAdmissionType to set
	 */
	public void setLastVisitAdmissionType(String lastVisitAdmissionType) {
		this.lastVisitAdmissionType = lastVisitAdmissionType;
	}

	/**
	 * @return the lastVisitAdmissionTypeDesc
	 */
	public String getLastVisitAdmissionTypeDesc() {
		return lastVisitAdmissionTypeDesc;
	}

	/**
	 * @param lastVisitAdmissionTypeDesc the lastVisitAdmissionTypeDesc to set
	 */
	public void setLastVisitAdmissionTypeDesc(String lastVisitAdmissionTypeDesc) {
		this.lastVisitAdmissionTypeDesc = lastVisitAdmissionTypeDesc;
	}

	/**
	 * @return the lastVisitPatientUnit
	 */
	public String getLastVisitPatientUnit() {
		return lastVisitPatientUnit;
	}

	/**
	 * @param lastVisitPatientUnit the lastVisitPatientUnit to set
	 */
	public void setLastVisitPatientUnit(String lastVisitPatientUnit) {
		this.lastVisitPatientUnit = lastVisitPatientUnit;
	}

	/**
	 * @return the lastVisitPatientUnitDesc
	 */
	public String getLastVisitPatientUnitDesc() {
		return lastVisitPatientUnitDesc;
	}

	/**
	 * @param lastVisitPatientUnitDesc the lastVisitPatientUnitDesc to set
	 */
	public void setLastVisitPatientUnitDesc(String lastVisitPatientUnitDesc) {
		this.lastVisitPatientUnitDesc = lastVisitPatientUnitDesc;
	}

	/**
	 * @return the lastVisitPatientRoom
	 */
	public String getLastVisitPatientRoom() {
		return lastVisitPatientRoom;
	}

	/**
	 * @param lastVisitPatientRoom the lastVisitPatientRoom to set
	 */
	public void setLastVisitPatientRoom(String lastVisitPatientRoom) {
		this.lastVisitPatientRoom = lastVisitPatientRoom;
	}

	/**
	 * @return the lastVisitPatientBed
	 */
	public String getLastVisitPatientBed() {
		return lastVisitPatientBed;
	}

	/**
	 * @param lastVisitPatientBed the lastVisitPatientBed to set
	 */
	public void setLastVisitPatientBed(String lastVisitPatientBed) {
		this.lastVisitPatientBed = lastVisitPatientBed;
	}

	/**
	 * @return the lastVisitFinancialClass
	 */
	public String getLastVisitFinancialClass() {
		return lastVisitFinancialClass;
	}

	/**
	 * @param lastVisitFinancialClass the lastVisitFinancialClass to set
	 */
	public void setLastVisitFinancialClass(String lastVisitFinancialClass) {
		this.lastVisitFinancialClass = lastVisitFinancialClass;
	}

	/**
	 * @return the lastVisitFinancialClassDesc
	 */
	public String getLastVisitFinancialClassDesc() {
		return lastVisitFinancialClassDesc;
	}

	/**
	 * @param lastVisitFinancialClassDesc the lastVisitFinancialClassDesc to set
	 */
	public void setLastVisitFinancialClassDesc(String lastVisitFinancialClassDesc) {
		this.lastVisitFinancialClassDesc = lastVisitFinancialClassDesc;
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
