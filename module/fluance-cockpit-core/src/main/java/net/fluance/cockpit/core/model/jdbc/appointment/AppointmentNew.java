package net.fluance.cockpit.core.model.jdbc.appointment;



import java.util.Date;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateTimeSerializer;

@SuppressWarnings("serial")
public class AppointmentNew implements Persistable<Integer>{

	public AppointmentNew(){}

	@JsonProperty("nbRecords")
	private Integer nb_records;
	@JsonProperty("appointmentId")
	private Long appoint_id;
	private Long pid;
	@JsonProperty("patientRoom")
	private String patientroom;
	@JsonProperty("visitNb")
	private Long visit_nb;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonProperty("beginDt")
	private Date begindt;
	private String description;
	@JsonProperty("firstName")
	private String firstname;
	@JsonProperty("lastName")
	private String lastname;
	@JsonProperty("maindenName")
	private String maidenname;

	public AppointmentNew(Integer nb_records, Long appoint_id, Long pid, String patientroom, Long visit_nb,  
			String firstname, String lastname,String maidenname,
			Date begindt,String description){
		this.nb_records = nb_records;
		this.appoint_id = appoint_id;
		this.pid = pid;
		this.patientroom = patientroom;
		this.visit_nb = visit_nb;
		this.firstname = firstname;
		this.lastname = lastname;
		this.maidenname = maidenname == null ? "" : maidenname;
		this.begindt = begindt;
		this.description = description;
	}

	public Integer getNb_records() {
		return nb_records;
	}

	public void setNb_records(Integer nb_records) {
		this.nb_records = nb_records;
	}

	public Long getVisit_nb() {
		return visit_nb;
	}

	public void setVisit_nb(Long visit_nb) {
		this.visit_nb = visit_nb;
	}

	public Date getBegindt() {
		return begindt;
	}

	public void setBegindt(Date begindt) {
		this.begindt = begindt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getMaidenname() {
		return maidenname;
	}

	public void setMaidenname(String maidenname) {
		this.maidenname = maidenname;
	}
	
	public Long getAppoint_id() {
		return appoint_id;
	}

	public void setAppoint_id(Long appoint_id) {
		this.appoint_id = appoint_id;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getPatientroom() {
		return patientroom;
	}

	public void setPatientroom(String patientroom) {
		this.patientroom = patientroom;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}
	
	@JsonIgnore
	@Override
	public Integer getId() {
		return 0;
	}
}
