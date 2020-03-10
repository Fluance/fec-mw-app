package net.fluance.cockpit.core.model.jpa.appointment;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.domain.Persistable;

@Entity(name="AppointmentProcessStatus")
@Table(name = "bmv_appointment_process_status", schema = "ehealth")
public class AppointmentProcessStatus implements Persistable<AppointmentProcessStatusPK>, Serializable{
	private static final long serialVersionUID = -4628276281042897199L;

	@EmbeddedId
	private AppointmentProcessStatusPK id;

	@Column(name = "eventdt")
	private Date eventDate;
	
	@Column(name = "company_code")
	private String companyCode;
	
	@Column(name = "ps_code")
	private String processStatusCode;
	
	public AppointmentProcessStatusPK getId() {
		return id;
	}
	
	public void setId(AppointmentProcessStatusPK id) {
		this.id = id;
	}
	
	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	public String getProcessStatusCode() {
		return processStatusCode;
	}
	
	public void setProcessStatusCode(String processStatusCode) {
		this.processStatusCode = processStatusCode;
	}

	@Override
	public boolean isNew() {
		return false;
	}
}
