package net.fluance.cockpit.core.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.fluance.commons.json.JsonDateTimeSerializer;

public class AppointmentReference implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private PatientVisitReference patient;
	private DoctorReference doctor;
	private String type;
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date startDate;
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date endDate;
	private String serviceName;
	private String subject;
	private String room;
	
	public AppointmentReference(String id, PatientVisitReference patient, DoctorReference doctor, String type, Date startDate, Date endDate, String serviceName, String subject, String room) {
		super();
		this.id = id;
		this.patient = patient;
		this.doctor = doctor;
		this.type = type;
		this.startDate = startDate;
		this.endDate = endDate;
		this.serviceName = serviceName;
		this.subject = subject;
		this.room = room;
	}

	
	public String getId() {
		return id;
	}

	
	public void setId(String id) {
		this.id = id;
	}

	
	public PatientVisitReference getPatient() {
		return patient;
	}

	
	public void setPatient(PatientVisitReference patient) {
		this.patient = patient;
	}

	
	public DoctorReference getDoctor() {
		return doctor;
	}

	
	public void setDoctor(DoctorReference doctor) {
		this.doctor = doctor;
	}

	
	public String getType() {
		return type;
	}

	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getServiceName() {
		return serviceName;
	}

	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	
	public String getSubject() {
		return subject;
	}

	
	public void setSubject(String subject) {
		this.subject = subject;
	}

	
	public String getRoom() {
		return room;
	}

	
	public void setRoom(String room) {
		this.room = room;
	}


	
	public Date getStartDate() {
		return startDate;
	}


	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	
	public Date getEndDate() {
		return endDate;
	}


	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
	
}
