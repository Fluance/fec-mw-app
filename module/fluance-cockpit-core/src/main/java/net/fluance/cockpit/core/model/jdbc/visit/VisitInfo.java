package net.fluance.cockpit.core.model.jdbc.visit;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.fluance.commons.json.JsonDateTimeSerializer;

public class VisitInfo implements Serializable {

	private static final long serialVersionUID = -7141937845545117229L;
	
	@JsonProperty("number")
	private Long nb;
	private String name;
	private String code;
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date admitDate;
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date dischargeDate;
	private String patientclass;
	private String patientclassdesc;
	private String patienttype;
	private String patienttypedesc;
	private String patientcase;
	private String patientcasedesc;
	private String hospservice;
	private String hospservicedesc;
	private String admissiontype;
	private String admissiontypedesc;
	private String patientunit;
	private String patientunitdesc;
	private String financialclass;
	private String financialclassdesc;
	private Integer attendingPhysicianId;
	private String attendingPhysicianLastname;
	private String attendingPhysicianFirstname;

	public VisitInfo(){}

	public Long getNb() {
		return nb;
	}

	public void setNb(Long nb) {
		this.nb = nb;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getPatientclass() {
		return patientclass;
	}

	public void setPatientclass(String patientclass) {
		this.patientclass = patientclass;
	}

	public String getPatientclassdesc() {
		return patientclassdesc;
	}

	public void setPatientclassdesc(String patientclassdesc) {
		this.patientclassdesc = patientclassdesc;
	}

	public String getPatienttype() {
		return patienttype;
	}

	public void setPatienttype(String patienttype) {
		this.patienttype = patienttype;
	}

	public String getPatienttypedesc() {
		return patienttypedesc;
	}

	public void setPatienttypedesc(String patienttypedesc) {
		this.patienttypedesc = patienttypedesc;
	}

	public String getPatientcase() {
		return patientcase;
	}

	public void setPatientcase(String patientcase) {
		this.patientcase = patientcase;
	}

	public String getPatientcasedesc() {
		return patientcasedesc;
	}

	public void setPatientcasedesc(String patientcasedesc) {
		this.patientcasedesc = patientcasedesc;
	}

	public String getHospservice() {
		return hospservice;
	}

	public void setHospservice(String hospservice) {
		this.hospservice = hospservice;
	}

	public String getHospservicedesc() {
		return hospservicedesc;
	}

	public void setHospservicedesc(String hospservicedesc) {
		this.hospservicedesc = hospservicedesc;
	}

	public String getAdmissiontype() {
		return admissiontype;
	}

	public void setAdmissiontype(String admissiontype) {
		this.admissiontype = admissiontype;
	}

	public String getAdmissiontypedesc() {
		return admissiontypedesc;
	}

	public void setAdmissiontypedesc(String admissiontypedesc) {
		this.admissiontypedesc = admissiontypedesc;
	}

	public String getPatientunit() {
		return patientunit;
	}

	public void setPatientunit(String patientunit) {
		this.patientunit = patientunit;
	}

	public String getPatientunitdesc() {
		return patientunitdesc;
	}

	public void setPatientunitdesc(String patientunitdesc) {
		this.patientunitdesc = patientunitdesc;
	}

	public String getFinancialclass() {
		return financialclass;
	}

	public void setFinancialclass(String financialclass) {
		this.financialclass = financialclass;
	}

	public String getFinancialclassdesc() {
		return financialclassdesc;
	}

	public void setFinancialclassdesc(String financialclassdesc) {
		this.financialclassdesc = financialclassdesc;
	}

	@JsonIgnore
	public Integer getAttendingPhysicianId() {
		return attendingPhysicianId;
	}

	public void setAttendingPhysicianId(Integer attendingPhysicianId) {
		this.attendingPhysicianId = attendingPhysicianId;
	}

	public String getAttendingPhysicianLastname() {
		return attendingPhysicianLastname;
	}

	public void setAttendingPhysicianLastname(String attendingPhysicianLastname) {
		this.attendingPhysicianLastname = attendingPhysicianLastname;
	}

	public String getAttendingPhysicianFirstname() {
		return attendingPhysicianFirstname;
	}

	public void setAttendingPhysicianFirstname(String attendingPhysicianFirstname) {
		this.attendingPhysicianFirstname = attendingPhysicianFirstname;
	}

}
