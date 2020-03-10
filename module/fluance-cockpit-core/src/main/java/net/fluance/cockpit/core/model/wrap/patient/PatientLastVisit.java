package net.fluance.cockpit.core.model.wrap.patient;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.fluance.commons.json.JsonDateTimeSerializer;

public class PatientLastVisit {

	private Long nb;
	private Long companyId;
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date admitdt;
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date dischargedt;
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
	private String financialClass;
	private String financialClassDesc;
	
	public PatientLastVisit(){}

	/**
	 * @param nb
	 * @param admitDate
	 * @param patientClass
	 * @param patientType
	 * @param patientTypeDesc
	 * @param patientCase
	 * @param patientCaseDesc
	 * @param hospService
	 * @param hospServiceDesc
	 * @param admissionType
	 * @param admissionTypeDesc
	 * @param patientUnit
	 * @param patientUnitDesc
	 * @param patientRoom
	 * @param patientBed
	 * @param financialClass
	 * @param financialClassDesc
	 */
	public PatientLastVisit(Long nb, Long companyId, Date admitDate, Date dischargeDate, String patientClass, String patientClassDesc, String patientType,
			String patientTypeDesc, String patientCase, String patientCaseDesc, String hospService,
			String hospServiceDesc, String admissionType, String admissionTypeDesc, String patientUnit,
			String patientUnitDesc, String patientRoom, String patientBed, String financialClass,
			String financialClassDesc) {
		super();
		this.nb = nb;
		this.companyId = companyId;
		this.admitdt = admitDate;
		this.dischargedt = dischargeDate;
		this.patientClass = patientClass;
		this.patientClassDesc = patientClassDesc;
		this.patientType = patientType;
		this.patientTypeDesc = patientTypeDesc;
		this.patientCase = patientCase;
		this.patientCaseDesc = patientCaseDesc;
		this.hospService = hospService;
		this.hospServiceDesc = hospServiceDesc;
		this.admissionType = admissionType;
		this.admissionTypeDesc = admissionTypeDesc;
		this.patientUnit = patientUnit;
		this.patientUnitDesc = patientUnitDesc;
		this.patientRoom = patientRoom;
		this.patientBed = patientBed;
		this.financialClass = financialClass;
		this.financialClassDesc = financialClassDesc;
	}
	
	/**
	 * @return the nb
	 */
	public Long getNb() {
		return nb;
	}

	/**
	 * @param nb the nb to set
	 */
	public void setNb(Long nb) {
		this.nb = nb;
	}

	/**
	 * @return the companyId
	 */
	public Long getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Date getAdmitdt() {
		return admitdt;
	}

	public void setAdmitdt(Date admitdt) {
		this.admitdt = admitdt;
	}

	public Date getDischargedt() {
		return dischargedt;
	}

	public void setDischargedt(Date dischargedt) {
		this.dischargedt = dischargedt;
	}

	/**
	 * @return the patientClass
	 */
	public String getPatientclass() {
		return patientClass;
	}

	/**
	 * @param patientClass the patientClass to set
	 */
	public void setPatientclass(String patientClass) {
		this.patientClass = patientClass;
	}

	/**
	 * @return the patientClassDesc
	 */
	public String getPatientclassdesc() {
		return patientClassDesc;
	}

	/**
	 * @param patientClassDesc the patientClassDesc to set
	 */
	public void setPatientClassDesc(String patientClassDesc) {
		this.patientClassDesc = patientClassDesc;
	}

	/**
	 * @return the patientType
	 */
	public String getPatienttype() {
		return patientType;
	}

	/**
	 * @param patientType the patientType to set
	 */
	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}

	/**
	 * @return the patientTypeDesc
	 */
	public String getPatienttypedesc() {
		return patientTypeDesc;
	}

	/**
	 * @param patientTypeDesc the patientTypeDesc to set
	 */
	public void setPatientTypeDesc(String patientTypeDesc) {
		this.patientTypeDesc = patientTypeDesc;
	}

	/**
	 * @return the patientCase
	 */
	public String getPatientcase() {
		return patientCase;
	}

	/**
	 * @param patientCase the patientCase to set
	 */
	public void setPatientCase(String patientCase) {
		this.patientCase = patientCase;
	}

	/**
	 * @return the patientCaseDesc
	 */
	public String getPatientcasedesc() {
		return patientCaseDesc;
	}

	/**
	 * @param patientCaseDesc the patientCaseDesc to set
	 */
	public void setPatientCaseDesc(String patientCaseDesc) {
		this.patientCaseDesc = patientCaseDesc;
	}

	/**
	 * @return the hospService
	 */
	public String getHospservice() {
		return hospService;
	}

	/**
	 * @param hospService the hospService to set
	 */
	public void setHospService(String hospService) {
		this.hospService = hospService;
	}

	/**
	 * @return the hospServiceDesc
	 */
	public String getHospservicedesc() {
		return hospServiceDesc;
	}

	/**
	 * @param hospServiceDesc the hospServiceDesc to set
	 */
	public void setHospServiceDesc(String hospServiceDesc) {
		this.hospServiceDesc = hospServiceDesc;
	}

	/**
	 * @return the admissionType
	 */
	public String getAdmissiontype() {
		return admissionType;
	}

	/**
	 * @param admissionType the admissionType to set
	 */
	public void setAdmissionType(String admissionType) {
		this.admissionType = admissionType;
	}

	/**
	 * @return the admissionTypeDesc
	 */
	public String getAdmissiontypedesc() {
		return admissionTypeDesc;
	}

	/**
	 * @param admissionTypeDesc the admissionTypeDesc to set
	 */
	public void setAdmissionTypeDesc(String admissionTypeDesc) {
		this.admissionTypeDesc = admissionTypeDesc;
	}

	/**
	 * @return the patientUnit
	 */
	public String getPatientunit() {
		return patientUnit;
	}

	/**
	 * @param patientUnit the patientUnit to set
	 */
	public void setPatientUnit(String patientUnit) {
		this.patientUnit = patientUnit;
	}

	/**
	 * @return the patientUnitDesc
	 */
	public String getPatientunitdesc() {
		return patientUnitDesc;
	}

	/**
	 * @param patientUnitDesc the patientUnitDesc to set
	 */
	public void setPatientUnitDesc(String patientUnitDesc) {
		this.patientUnitDesc = patientUnitDesc;
	}

	/**
	 * @return the patientRoom
	 */
	public String getPatientroom() {
		return patientRoom;
	}

	/**
	 * @param patientRoom the patientRoom to set
	 */
	public void setPatientRoom(String patientRoom) {
		this.patientRoom = patientRoom;
	}

	/**
	 * @return the patientBed
	 */
	public String getPatientbed() {
		return patientBed;
	}

	/**
	 * @param patientBed the patientBed to set
	 */
	public void setPatientbed(String patientBed) {
		this.patientBed = patientBed;
	}

	/**
	 * @return the financialClass
	 */
	public String getFinancialclass() {
		return financialClass;
	}

	/**
	 * @param financialClass the financialClass to set
	 */
	public void setFinancialClass(String financialClass) {
		this.financialClass = financialClass;
	}

	/**
	 * @return the financialClassDesc
	 */
	public String getFinancialclassdesc() {
		return financialClassDesc;
	}

	/**
	 * @param financialClassDesc the financialClassDesc to set
	 */
	public void setFinancialClassDesc(String financialClassDesc) {
		this.financialClassDesc = financialClassDesc;
	}

}