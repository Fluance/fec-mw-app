package net.fluance.cockpit.core.model.jpa.visit;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "visit_detail")
@Table(name = "BMV_VISIT_DETAIL")
public class VisitDetailEntity {
	@Id
	@Column(name = "nb")
	private Long visitNumber;

	@Column(name = "patient_id")
	private Long patientId;

	@Column(name = "company_id")
	private Integer companyId;

	@Column(name = "patientclass")
	private String patientClass;

	@Column(name = "patientunit")
	private String patientUnit;

	@Column(name = "patientroom")
	private String patientRoom;

	@Column(name = "patientbed")
	private String patientBed;

	@Column(name = "patientcase")
	private String patientCase;

	@Column(name = "admissiontype")
	private String admissionType;

	@Column(name = "financialclass")
	private String financialClass;

	@Column(name = "priorunit")
	private String priorUnit;

	@Column(name = "priorroom")
	private String priorRoom;

	@Column(name = "priorbed")
	private String priorBed;

	@Column(name = "hospservice")
	private String hospService;

	@Column(name = "admitsource")
	private String admitSource;

	@Column(name = "patienttype")
	private String patientType;

	@Column(name = "admitdt")
	private Timestamp admitDate;

	@Column(name = "dischargedt")
	private Timestamp dischargeDate;

	@Column(name = "expdischargedt")
	private Timestamp expiredDischargeDate;

	@Column(name = "patientclassdesc")
	private String patientClassDesc;

	@Column(name = "patienttypedesc")
	private String patientTypeDesc;

	@Column(name = "patientcasedesc")
	private String patientCaseDesc;

	@Column(name = "admissiontypedesc")
	private String admissionTypeDesc;

	@Column(name = "financialclassdesc")
	private String financialClassDesc;

	@Column(name = "patientunitdesc")
	private String patientUnitDesc;

	@Column(name = "hospservicedesc")
	private String hospServiceDesc;

	@Column(name = "priorunitdesc")
	private String priorUnitDesc;

	@Column(name = "admitsourcedesc")
	private String admitSourceDesc;

	@Column(name = "hl7code")
	private String hl7Code;

	
	public Long getVisitNumber() {
		return visitNumber;
	}

	
	public void setVisitNumber(Long visitNumber) {
		this.visitNumber = visitNumber;
	}

	
	public Long getPatientId() {
		return patientId;
	}

	
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	
	public Integer getCompanyId() {
		return companyId;
	}

	
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	
	public String getPatientClass() {
		return patientClass;
	}

	
	public void setPatientClass(String patientClass) {
		this.patientClass = patientClass;
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

	
	public String getPatientCase() {
		return patientCase;
	}

	
	public void setPatientCase(String patientCase) {
		this.patientCase = patientCase;
	}

	
	public String getAdmissionType() {
		return admissionType;
	}

	
	public void setAdmissionType(String admissionType) {
		this.admissionType = admissionType;
	}

	
	public String getFinancialClass() {
		return financialClass;
	}

	
	public void setFinancialClass(String financialClass) {
		this.financialClass = financialClass;
	}

	
	public String getPriorUnit() {
		return priorUnit;
	}

	
	public void setPriorUnit(String priorUnit) {
		this.priorUnit = priorUnit;
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

	
	public String getHospService() {
		return hospService;
	}

	
	public void setHospService(String hospService) {
		this.hospService = hospService;
	}

	
	public String getAdmitSource() {
		return admitSource;
	}

	
	public void setAdmitSource(String admitSource) {
		this.admitSource = admitSource;
	}

	
	public String getPatientType() {
		return patientType;
	}

	
	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}

	
	public Timestamp getAdmitDate() {
		return admitDate;
	}

	
	public void setAdmitDate(Timestamp admitDate) {
		this.admitDate = admitDate;
	}

	
	public Timestamp getDischargeDate() {
		return dischargeDate;
	}

	
	public void setDischargeDate(Timestamp dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	
	public Timestamp getExpiredDischargeDate() {
		return expiredDischargeDate;
	}

	
	public void setExpectedDischargeDate(Timestamp expiredDischargeDate) {
		this.expiredDischargeDate = expiredDischargeDate;
	}

	
	public String getPatientClassDesc() {
		return patientClassDesc;
	}

	
	public void setPatientClassDesc(String patientClassDesc) {
		this.patientClassDesc = patientClassDesc;
	}

	
	public String getPatientTypeDesc() {
		return patientTypeDesc;
	}

	
	public void setPatientTypeDesc(String patientTypeDesc) {
		this.patientTypeDesc = patientTypeDesc;
	}

	
	public String getPatientCaseDesc() {
		return patientCaseDesc;
	}

	
	public void setPatientCaseDesc(String patientCaseDesc) {
		this.patientCaseDesc = patientCaseDesc;
	}

	
	public String getAdmissionTypeDesc() {
		return admissionTypeDesc;
	}

	
	public void setAdmissionTypeDesc(String admissionTypeDesc) {
		this.admissionTypeDesc = admissionTypeDesc;
	}

	
	public String getFinancialClassDesc() {
		return financialClassDesc;
	}

	
	public void setFinancialClassDesc(String financialClassDesc) {
		this.financialClassDesc = financialClassDesc;
	}

	
	public String getPatientUnitDesc() {
		return patientUnitDesc;
	}

	
	public void setPatientUnitDesc(String patientUnitDesc) {
		this.patientUnitDesc = patientUnitDesc;
	}

	
	public String getHospServiceDesc() {
		return hospServiceDesc;
	}

	
	public void setHospServiceDesc(String hospServiceDesc) {
		this.hospServiceDesc = hospServiceDesc;
	}

	
	public String getPriorUnitDesc() {
		return priorUnitDesc;
	}

	
	public void setPriorUnitDesc(String priorUnitDesc) {
		this.priorUnitDesc = priorUnitDesc;
	}

	
	public String getAdmitSourceDesc() {
		return admitSourceDesc;
	}

	
	public void setAdmitSourceDesc(String admitSourceDesc) {
		this.admitSourceDesc = admitSourceDesc;
	}

	
	public String getHl7Code() {
		return hl7Code;
	}

	
	public void setHl7Code(String hl7Code) {
		this.hl7Code = hl7Code;
	}
}
