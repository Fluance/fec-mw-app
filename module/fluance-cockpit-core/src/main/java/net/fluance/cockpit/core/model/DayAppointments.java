package net.fluance.cockpit.core.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.fluance.commons.json.JsonDateDeserializer;
import net.fluance.commons.json.JsonDateSerializer;

public class DayAppointments implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date date;
	private List<AppointmentReference> appointments;	
	
	public DayAppointments() {
		super();
	}

	public DayAppointments(Date date, List<AppointmentReference> appointments) {
		super();
		this.date = date;
		this.appointments = appointments;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	
	public List<AppointmentReference> getAppointments() {
		return appointments;
	}

	
	public void setAppointments(List<AppointmentReference> appointments) {
		this.appointments = appointments;
	}
	
	
	
	
}
