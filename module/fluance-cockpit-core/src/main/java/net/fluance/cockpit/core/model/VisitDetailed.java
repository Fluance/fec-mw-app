package net.fluance.cockpit.core.model;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateTimeSerializer;

public class VisitDetailed implements PayloadDisplayLogName {

	private Long visitNb;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date admitDate;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date dischargeDate;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date expDischargeDate;
	private String patientClass;
	private String patientClassDesc;
	private String patientType;
	private String patientTypeDesc;
	private String patientCase;
	private String patientCaseDesc;
	private String hospService;
	private String hospServiceDesc;
	private String admissionType;
	private String admissionTypeDesc;
	private String patientUnit;
	private String patientUnitDesc;
	private String patientRoom;
	private String patientBed;
	private String priorRoom;
	private String priorBed;
	private String priorUnit;
	private String priorUnitDesc;
	private String admitSource;
	private String admitSourceDesc;
	private String financialClass;
	private String financialClassDesc;
	private String hl7code;	
	
	private CompanyReference company;
	private PatientReference patient;

	public Long getVisitNb() {
		return visitNb;
	}
	
	public void setVisitNb(Long visitNb) {
		this.visitNb = visitNb;
	}
	
	public Date getAdmitDate() {
		return admitDate;
	}
	
	public void setAdmitDate(Date admitDate) {
		this.admitDate = admitDate;
	}
	
	public Date getDischargeDate() {
		return dischargeDate;
	}
	
	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public Date getExpDischargeDate() {
		return expDischargeDate;
	}

	public void setExpDischargeDate(Date expDischargeDate) {
		this.expDischargeDate = expDischargeDate;
	}
	
	public String getPatientClass() {
		return patientClass;
	}

	
	public void setPatientClass(String patientClass) {
		this.patientClass = patientClass;
	}

	
	public String getPatientClassDesc() {
		return patientClassDesc;
	}
	
	public void setPatientClassDesc(String patientClassDesc) {
		this.patientClassDesc = patientClassDesc;
	}
	
	public String getPatientType() {
		return patientType;
	}
	
	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}

	public String getPatientTypeDesc() {
		return patientTypeDesc;
	}
	
	public void setPatientTypeDesc(String patientTypeDesc) {
		this.patientTypeDesc = patientTypeDesc;
	}
	
	public String getPatientCase() {
		return patientCase;
	}

	public void setPatientCase(String patientCase) {
		this.patientCase = patientCase;
	}

	public String getPatientCaseDesc() {
		return patientCaseDesc;
	}

	public void setPatientCaseDesc(String patientCaseDesc) {
		this.patientCaseDesc = patientCaseDesc;
	}

	public String getHospService() {
		return hospService;
	}

	public void setHospService(String hospService) {
		this.hospService = hospService;
	}

	public String getHospServiceDesc() {
		return hospServiceDesc;
	}

	public void setHospServiceDesc(String hospServiceDesc) {
		this.hospServiceDesc = hospServiceDesc;
	}
	
	public String getAdmissionType() {
		return admissionType;
	}

	public void setAdmissionType(String admissionType) {
		this.admissionType = admissionType;
	}

	public String getAdmissionTypeDesc() {
		return admissionTypeDesc;
	}

	public void setAdmissionTypeDesc(String admissionTypeDesc) {
		this.admissionTypeDesc = admissionTypeDesc;
	}

	public String getPatientUnit() {
		return patientUnit;
	}

	public void setPatientUnit(String patientUnit) {
		this.patientUnit = patientUnit;
	}

	public String getPatientUnitDesc() {
		return patientUnitDesc;
	}

	public void setPatientUnitDesc(String patientUnitDesc) {
		this.patientUnitDesc = patientUnitDesc;
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

	public String getPriorRoom() {
		return priorRoom;
	}
	
	public void setPriorRoom(String priorRoom) {
		this.priorRoom = priorRoom;
	}
	
	public String getPriorBed() {
		return priorBed;
	}
	
	public void setPriorBed(String priorBed) {
		this.priorBed = priorBed;
	}
	
	public String getPriorUnit() {
		return priorUnit;
	}
	
	public void setPriorUnit(String priorUnit) {
		this.priorUnit = priorUnit;
	}

	public String getPriorUnitDesc() {
		return priorUnitDesc;
	}

	public void setPriorUnitDesc(String priorUnitDesc) {
		this.priorUnitDesc = priorUnitDesc;
	}
	
	public String getAdmitSource() {
		return admitSource;
	}
	
	public void setAdmitSource(String admitSource) {
		this.admitSource = admitSource;
	}
	
	public String getAdmitSourceDesc() {
		return admitSourceDesc;
	}

	public void setAdmitSourceDesc(String admitSourceDesc) {
		this.admitSourceDesc = admitSourceDesc;
	}

	public String getFinancialClass() {
		return financialClass;
	}
	
	public void setFinancialClass(String financialClass) {
		this.financialClass = financialClass;
	}
	
	public String getFinancialClassDesc() {
		return financialClassDesc;
	}
	
	public void setFinancialClassDesc(String financialClassDesc) {
		this.financialClassDesc = financialClassDesc;
	}
	
	public String getHl7code() {
		return hl7code;
	}
	
	public void setHl7code(String hl7code) {
		this.hl7code = hl7code;
	}
	
	public CompanyReference getCompany() {
		return company;
	}

	public void setCompany(CompanyReference company) {
		this.company = company;
	}
	
	public PatientReference getPatient() {
		return patient;
	}
	
	public void setPatient(PatientReference patient) {
		this.patient = patient;
	}

	@Override
	public String displayName() {
		return this.visitNb.toString();
	}
}
