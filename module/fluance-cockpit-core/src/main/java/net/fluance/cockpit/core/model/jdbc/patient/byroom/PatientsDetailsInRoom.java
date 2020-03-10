package net.fluance.cockpit.core.model.jdbc.patient.byroom;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateTimeSerializer;

public class PatientsDetailsInRoom implements Persistable<Integer> {

	private static final long serialVersionUID = 1L;
	Integer roomCount;
	String patientRoom;

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
	@ApiModelProperty(dataType = "java.lang.String")
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

	public PatientsDetailsInRoom(Integer roomCount, String patientRoom, Long pid, String firstname, String lastname, String maidenName, Date birthDate, String sex, boolean death, Date deathdt, String address, String locality,
			String postCode, Long nb, Long companyId, Timestamp admitDate, Timestamp dischargeDate, String patientClass, String patientClassDesc, String patientType, String patientTypeDesc, String patientCase, String patientCaseDesc,
			String hospService, String hospServiceDesc, String admissionType, String admissionTypeDesc, String patientUnit, String patientUnitDesc, String patientBed, String financialClass, String financialClassDesc) {
		this.roomCount = roomCount;
		this.patientRoom = patientRoom;
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

	public Integer getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(Integer roomCount) {
		this.roomCount = roomCount;
	}

	public String getPatientRoom() {
		return patientRoom;
	}

	public void setPatientRoom(String patientRoom) {
		this.patientRoom = patientRoom;
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

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public Long getLastVisitnumber() {
		return lastVisitnumber;
	}

	public void setLastVisitnumber(Long lastVisitnumber) {
		this.lastVisitnumber = lastVisitnumber;
	}

	public Long getLastVisitCompanyId() {
		return lastVisitCompanyId;
	}

	public void setLastVisitCompanyId(Long lastVisitCompanyId) {
		this.lastVisitCompanyId = lastVisitCompanyId;
	}

	public Timestamp getLastVisitAdmitDate() {
		return lastVisitAdmitDate;
	}

	public void setLastVisitAdmitDate(Timestamp lastVisitAdmitDate) {
		this.lastVisitAdmitDate = lastVisitAdmitDate;
	}

	public Timestamp getLastVisitDischargeDate() {
		return lastVisitDischargeDate;
	}

	public void setLastVisitDischargeDate(Timestamp lastVisitDischargeDate) {
		this.lastVisitDischargeDate = lastVisitDischargeDate;
	}

	public String getLastVisitPatientClass() {
		return lastVisitPatientClass;
	}

	public void setLastVisitPatientClass(String lastVisitPatientClass) {
		this.lastVisitPatientClass = lastVisitPatientClass;
	}

	public String getLastVisitPatientClassDesc() {
		return lastVisitPatientClassDesc;
	}

	public void setLastVisitPatientClassDesc(String lastVisitPatientClassDesc) {
		this.lastVisitPatientClassDesc = lastVisitPatientClassDesc;
	}

	public String getLastVisitPatientType() {
		return lastVisitPatientType;
	}

	public void setLastVisitPatientType(String lastVisitPatientType) {
		this.lastVisitPatientType = lastVisitPatientType;
	}

	public String getLastVisitPatientTypeDesc() {
		return lastVisitPatientTypeDesc;
	}

	public void setLastVisitPatientTypeDesc(String lastVisitPatientTypeDesc) {
		this.lastVisitPatientTypeDesc = lastVisitPatientTypeDesc;
	}

	public String getLastVisitPatientCase() {
		return lastVisitPatientCase;
	}

	public void setLastVisitPatientCase(String lastVisitPatientCase) {
		this.lastVisitPatientCase = lastVisitPatientCase;
	}

	public String getLastVisitPatientCaseDesc() {
		return lastVisitPatientCaseDesc;
	}

	public void setLastVisitPatientCaseDesc(String lastVisitPatientCaseDesc) {
		this.lastVisitPatientCaseDesc = lastVisitPatientCaseDesc;
	}

	public String getLastVisitHospService() {
		return lastVisitHospService;
	}

	public void setLastVisitHospService(String lastVisitHospService) {
		this.lastVisitHospService = lastVisitHospService;
	}

	public String getLastVisitHospServiceDesc() {
		return lastVisitHospServiceDesc;
	}

	public void setLastVisitHospServiceDesc(String lastVisitHospServiceDesc) {
		this.lastVisitHospServiceDesc = lastVisitHospServiceDesc;
	}

	public String getLastVisitAdmissionType() {
		return lastVisitAdmissionType;
	}

	public void setLastVisitAdmissionType(String lastVisitAdmissionType) {
		this.lastVisitAdmissionType = lastVisitAdmissionType;
	}

	public String getLastVisitAdmissionTypeDesc() {
		return lastVisitAdmissionTypeDesc;
	}

	public void setLastVisitAdmissionTypeDesc(String lastVisitAdmissionTypeDesc) {
		this.lastVisitAdmissionTypeDesc = lastVisitAdmissionTypeDesc;
	}

	public String getLastVisitPatientUnit() {
		return lastVisitPatientUnit;
	}

	public void setLastVisitPatientUnit(String lastVisitPatientUnit) {
		this.lastVisitPatientUnit = lastVisitPatientUnit;
	}

	public String getLastVisitPatientUnitDesc() {
		return lastVisitPatientUnitDesc;
	}

	public void setLastVisitPatientUnitDesc(String lastVisitPatientUnitDesc) {
		this.lastVisitPatientUnitDesc = lastVisitPatientUnitDesc;
	}

	public String getLastVisitPatientRoom() {
		return lastVisitPatientRoom;
	}

	public void setLastVisitPatientRoom(String lastVisitPatientRoom) {
		this.lastVisitPatientRoom = lastVisitPatientRoom;
	}

	public String getLastVisitPatientBed() {
		return lastVisitPatientBed;
	}

	public void setLastVisitPatientBed(String lastVisitPatientBed) {
		this.lastVisitPatientBed = lastVisitPatientBed;
	}

	public String getLastVisitFinancialClass() {
		return lastVisitFinancialClass;
	}

	public void setLastVisitFinancialClass(String lastVisitFinancialClass) {
		this.lastVisitFinancialClass = lastVisitFinancialClass;
	}

	public String getLastVisitFinancialClassDesc() {
		return lastVisitFinancialClassDesc;
	}

	public void setLastVisitFinancialClassDesc(String lastVisitFinancialClassDesc) {
		this.lastVisitFinancialClassDesc = lastVisitFinancialClassDesc;
	}

	@Override
	public Integer getId() {
		return null;
	}

	@Override
	public boolean isNew() {
		return false;
	}
}
