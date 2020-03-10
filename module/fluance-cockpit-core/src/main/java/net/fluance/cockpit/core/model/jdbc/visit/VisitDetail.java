package net.fluance.cockpit.core.model.jdbc.visit;

import java.util.Date;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.cockpit.core.model.CompanyReference;
import net.fluance.cockpit.core.model.PayloadDisplayLogName;
import net.fluance.commons.json.JsonDateTimeSerializer;

@SuppressWarnings("serial")
public class VisitDetail implements Persistable<Long>, PayloadDisplayLogName {

	private Long number;
	private Long patientId;
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
	private String patientClassIOU;
	private CompanyReference company;
	
	public VisitDetail(long nb, long patient_id, Date admitdt, Date dischargedt, Date expdischargedt, String patientclass,
			String patientclassdesc, String patienttype, String patienttypedesc, String patientcase,
			String patientcasedesc, String hospservice, String hospservicedesc, String admissiontype,
			String admissiontypedesc, String patientunit, String patientunitdesc, String patientroom, String patientbed,
			String priorroom, String priorbed, String priorunit, String priorunitdesc, String admitsource, String admitsourcedesc, String financialclass, String financialclassdesc, String patientClassIOU, CompanyReference company) {
		this.number = nb;
		this.patientId = patient_id;
		this.admitDate = admitdt;
		this.dischargeDate = dischargedt;
		this.expDischargeDate = expdischargedt;
		this.patientClass = patientclass;
		this.patientClassDesc = patientclassdesc;
		this.patientType = patienttype;
		this.patientTypeDesc = patienttypedesc;
		this.patientCase = patientcase;
		this.patientCaseDesc = patientcasedesc;
		this.hospService = hospservice;
		this.hospServiceDesc = hospservicedesc;
		this.admissionType = admissiontype;
		this.admissionTypeDesc = admissiontypedesc;
		this.patientUnit = patientunit;
		this.patientUnitDesc = patientunitdesc;
		this.patientRoom = patientroom;
		this.patientBed = patientbed;
		this.priorRoom = priorroom;
		this.priorBed = priorbed;
		this.priorUnit = priorunit;
		this.priorUnitDesc = priorunitdesc;
		this.admitSource = admitsource;
		this.admitSourceDesc = admitsourcedesc;
		this.financialClass = financialclass;
		this.financialClassDesc = financialclassdesc;
		this.patientClassIOU = patientClassIOU;
		this.company = company;
	}
	
	public VisitDetail(){}

	@JsonIgnore
	@Override
	public Long getId() {
		return number;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return true;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
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

	public String getPatientClassIOU() {
		return patientClassIOU;
	}

	public void setPatientClassIOU(String patientClassIOU) {
		this.patientClassIOU = patientClassIOU;
	}

	public CompanyReference getCompany() {
		return company;
	}

	public void setCompany(CompanyReference company) {
		this.company = company;
	}

	@Override
	public String displayName() {
		return this.number.toString();
	}
}
