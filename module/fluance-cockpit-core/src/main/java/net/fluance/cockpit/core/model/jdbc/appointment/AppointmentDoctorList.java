package net.fluance.cockpit.core.model.jdbc.appointment;

import java.util.Date;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.cockpit.core.model.PatientReference;
import net.fluance.commons.json.JsonDateTimeSerializer;

public class AppointmentDoctorList implements Persistable<Long> {

	private Integer nbRecords;
	private Long id;
	private String description;
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date beginDate;
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date endDate;
	private PatientReference patient;
	private Long visitNb;

	public AppointmentDoctorList(Integer nbRecords, Long id, String description, Date beginDate, Date endDate,
			PatientReference patient, Long visitNb) {
		super();
		this.nbRecords = nbRecords;
		this.id = id;
		this.description = description;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.patient = patient;
		this.visitNb = visitNb;
	}

	public Integer getNbRecords() {
		return nbRecords;
	}

	public void setNbRecords(Integer nbRecords) {
		this.nbRecords = nbRecords;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public PatientReference getPatient() {
		return patient;
	}

	public void setPatient(PatientReference patient) {
		this.patient = patient;
	}

	public Long getVisitNb() {
		return visitNb;
	}

	public void setVisitNb(Long visitNb) {
		this.visitNb = visitNb;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

}
