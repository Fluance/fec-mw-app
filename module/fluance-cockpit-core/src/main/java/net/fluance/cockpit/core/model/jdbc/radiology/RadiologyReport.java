package net.fluance.cockpit.core.model.jdbc.radiology;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.fluance.commons.json.JsonDateTimeSerializer;

@SuppressWarnings("serial")
public class RadiologyReport implements Persistable<Integer>{

	private String studyUid;
	private Long patientId;
	private Integer companyId;
	private String orderNb;
	private String report;
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date studyDate;
	private String completion;
	private String verification;
	private String referringPhysician;
	private String recordPhysician;
	private String performingPhysician;
	private String readingPhysician;
	private Long rank;
	
	public RadiologyReport(String studyUid, Long patientId, Integer companyId, String orderNb, Timestamp studyDate, String report,
			String completion, String verification, String referringPhysician, String recordPhysician,
			String performingPhysician, String readingPhysician, Long rank) {
		this.studyUid = studyUid;
		this.patientId = patientId;
		this.companyId = companyId;
		this.orderNb = orderNb;
		this.studyDate = studyDate;
		this.report = report;
		this.completion = completion;
		this.verification = verification;
		this.referringPhysician = referringPhysician;
		this.recordPhysician = recordPhysician;
		this.performingPhysician = performingPhysician;
		this.readingPhysician = readingPhysician;
		this.rank = rank;
	}

	public String getStudyUid() {
		return studyUid;
	}

	public void setStudyUid(String studyUid) {
		this.studyUid = studyUid;
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

	public String getOrderNb() {
		return orderNb;
	}

	public void setOrderNb(String orderNb) {
		this.orderNb = orderNb;
	}

	public Date getStudyDate() {
		return studyDate;
	}

	public void setStudyDate(Date studyDate) {
		this.studyDate = studyDate;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getCompletion() {
		return completion;
	}

	public void setCompletion(String completion) {
		this.completion = completion;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	public String getReferringPhysician() {
		return referringPhysician;
	}

	public void setReferringPhysician(String referringPhysician) {
		this.referringPhysician = referringPhysician;
	}

	public String getRecordPhysician() {
		return recordPhysician;
	}

	public void setRecordPhysician(String recordPhysician) {
		this.recordPhysician = recordPhysician;
	}

	public String getPerformingPhysician() {
		return performingPhysician;
	}

	public void setPerformingPhysician(String performingPhysician) {
		this.performingPhysician = performingPhysician;
	}

	public String getReadingPhysician() {
		return readingPhysician;
	}

	public void setReadingPhysician(String readingPhysician) {
		this.readingPhysician = readingPhysician;
	}

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	@JsonIgnore
	@Override
	public Integer getId() {
		return null;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}
	
}
