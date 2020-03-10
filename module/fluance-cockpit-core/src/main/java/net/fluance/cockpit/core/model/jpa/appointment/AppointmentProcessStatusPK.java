package net.fluance.cockpit.core.model.jpa.appointment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AppointmentProcessStatusPK implements Serializable {
	private static final long serialVersionUID = 62268803620754886L;

	@Column(name="appoint_id")
	private Long appointmentId;
	
	@Column(name="psid")
	private Integer processStatusId;
	
	@Column(name="company_id")
	private Integer companyId;

	public Long getAppointmentId() {
		return appointmentId;
	}

	public Integer getProcessStatusId() {
		return processStatusId;
	}
	
	public Integer getCompanyId() {
		return companyId;
	}
	
	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public void setProcessStatusId(Integer processStatusId) {
		this.processStatusId = processStatusId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
