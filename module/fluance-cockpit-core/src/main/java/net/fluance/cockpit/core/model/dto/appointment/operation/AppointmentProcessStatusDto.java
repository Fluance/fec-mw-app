package net.fluance.cockpit.core.model.dto.appointment.operation;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.fluance.cockpit.core.util.serializer.LocalDateTimeSerializer;

public class AppointmentProcessStatusDto implements Comparable<AppointmentProcessStatusDto>{
	@JsonIgnore
	private Long appointmentId;
	@JsonIgnore
	private Integer companyId;
	@JsonIgnore
	private String companyCode;
	@JsonIgnore
	private String processStatusCode;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime eventDate;
	
	private Integer stepNumber;
	
	private Integer processStatusId;
	
	private String processStatusDescription;
	
	private boolean live;
	
	private boolean operationLive;
	
	public String getProcessStatusDescription() {
		return processStatusDescription;
	}
	
	public void setProcessStatusDescription(String processStatusDescription) {
		this.processStatusDescription = processStatusDescription;
	}

	public Long getAppointmentId() {
		return appointmentId;
	}
	
	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Integer getProcessStatusId() {
		return processStatusId;
	}
	
	public void setProcessStatusId(Integer processStatusId) {
		this.processStatusId = processStatusId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public LocalDateTime getEventDate() {
		return eventDate;
	}
	
	public void setEventDate(LocalDateTime eventDate) {
		this.eventDate = eventDate;
	}

	public Integer getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(Integer stepNumber) {
		this.stepNumber = stepNumber;
	}
	
	public boolean isLive() {
		return live;
	}
	
	public void setLive(boolean live) {
		this.live = live;
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
	
	public boolean isOperationLive() {
		return operationLive;
	}

	public void setOperationLive(boolean operationLive) {
		this.operationLive = operationLive;
	}

	@JsonIgnore
	@Override
	public int compareTo(AppointmentProcessStatusDto o) {
		if(o != null) {
			if(o.getStepNumber() != null) {
				if(this.stepNumber == null) {
					return 1;
				} else {
					return this.stepNumber.compareTo(o.getStepNumber());
				}
			} else {
				return -1;
			}
		} else {
			return 1;
		}
	}
}
