package net.fluance.cockpit.core.domain.visit;

import java.util.Date;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateTimeSerializer;

@SuppressWarnings("serial")
public class PatientList implements Persistable<Long>{
	private long visitNb;
	private long companyId;
	private String firstName;
	private String lastName;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date birthDate;
	private String sex;
	private String patientUnit;
	private String patientRoom;
	private String patientBed;
	private String physicianPrefix;
	private String physicianFirstName;
	private String physicianLastName;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date admissionDate;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date dischargeDate;
	private String financialClass;
	private String food;
	private String mobility;

	public PatientList(long visitNb, long companyId, String firstName, String lastName, Date birthDate, String sex, String patientUnit, String patientRoom, String patientBed, String physicianPrefix, String physicianFirstName,
			String physicianLastName, Date admissionDate, Date dischargeDate, String financialClass, String food, String mobility) {
		this.visitNb = visitNb;
		this.companyId = companyId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.sex = sex;
		this.patientUnit = patientUnit;
		this.patientRoom = patientRoom;
		this.patientBed = patientBed;
		this.physicianPrefix = physicianPrefix;
		this.physicianFirstName = physicianFirstName;
		this.physicianLastName = physicianLastName;
		this.admissionDate = admissionDate;
		this.dischargeDate = dischargeDate;
		this.financialClass = financialClass;
		this.food = food;
		this.mobility = mobility;
	}

	
	public long getVisitNb() {
		return visitNb;
	}

	
	public void setVisitNb(long visitNb) {
		this.visitNb = visitNb;
	}

	
	public long getCompanyId() {
		return companyId;
	}

	
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
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

	
	public String getPatientUnit() {
		return patientUnit;
	}

	
	public void setPatientUnit(String patientUnit) {
		this.patientUnit = patientUnit;
	}

	
	public String getPatientRoom() {
		return patientRoom;
	}

	
	public void setPatientRoom(String patientRoom) {
		this.patientRoom = patientRoom;
	}

	
	public String getPatientBed() {
		return patientBed;
	}

	
	public void setPatientBed(String patientBed) {
		this.patientBed = patientBed;
	}

	
	public String getPhysicianPrefix() {
		return physicianPrefix;
	}

	
	public void setPhysicianPrefix(String physicianPrefix) {
		this.physicianPrefix = physicianPrefix;
	}

	
	public String getPhysicianFirstName() {
		return physicianFirstName;
	}

	
	public void setPhysicianFirstName(String physicianFirstName) {
		this.physicianFirstName = physicianFirstName;
	}

	
	public String getPhysicianLastName() {
		return physicianLastName;
	}

	
	public void setPhysicianLastName(String physicianLastName) {
		this.physicianLastName = physicianLastName;
	}

	
	public Date getAdmissionDate() {
		return admissionDate;
	}

	
	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	
	public Date getDischargeDate() {
		return dischargeDate;
	}

	
	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	
	public String getFinancialClass() {
		return financialClass;
	}

	
	public void setFinancialClass(String financialClass) {
		this.financialClass = financialClass;
	}

	
	public String getFood() {
		return food;
	}

	
	public void setFood(String food) {
		this.food = food;
	}

	
	public String getMobility() {
		return mobility;
	}

	
	public void setMobility(String mobility) {
		this.mobility = mobility;
	}

	@JsonIgnore
	@Override
	public Long getId() {
		return null;
	}


	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}	
}