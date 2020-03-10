package net.fluance.cockpit.core.model.jdbc.appointment;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Type;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.fluance.cockpit.core.model.CompanyReference;
import net.fluance.cockpit.core.model.PatientReference;
import net.fluance.cockpit.core.model.PayloadDisplayLogName;
import net.fluance.cockpit.core.model.VisitReference;
import net.fluance.commons.json.JsonDateTimeSerializer;

@SuppressWarnings("serial")
public class AppointmentDetail implements Persistable<Long>, PayloadDisplayLogName{
	private Integer nbRecords;
	private Long appointmentId;
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Timestamp beginDate;
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Timestamp endDate;
	private Integer duration;
	@JsonIgnore // used for translation
	private String appointkindcode;
	@JsonIgnore// used for translation
	private String appointkinddescription;
	private String description;
	private String appointmentType;
	private Integer companyId;
	private PatientReference patient;
	private VisitReference visit;
	private List<Personnel> personnels;
	@Type(type = "json")
	private List<Location> locations;
	@Type(type = "json")
	private List<Device> devices;
	private CompanyReference company;
	private String anesthesia;
	private String patientPosition;

	public AppointmentDetail(Integer nbRecords, Long appointmentId, Timestamp beginDate, Timestamp endDate,
			String appointmentType, Integer duration, String description, String appointkindcode, String appointkinddescription, Integer companyId, String anesthesia, String patientPosition, PatientReference patient, VisitReference visit,
			List<Personnel> personnels, List<Location> locations, List<Device> devices) {
		this.nbRecords = nbRecords;
		this.appointmentId = appointmentId;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.appointmentType = appointmentType;
		this.duration = duration;
		this.description = description;
		this.appointkindcode = appointkindcode;
		this.appointkinddescription = appointkinddescription;
		this.companyId = companyId;
		this.anesthesia = anesthesia;
		this.patientPosition = patientPosition;
		this.patient = patient;
		this.visit = visit;
		this.personnels = personnels;
		this.locations = locations;
		this.devices = devices;
	}

	public Integer getNbRecords() {
		return nbRecords;
	}

	public void setNbRecords(Integer nbRecords) {
		this.nbRecords = nbRecords;
	}

	public Long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Timestamp beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) { this.description = description; }

	public String getAppointkindcode() { return appointkindcode; }

	public void setAppointkindcode(String appointkindcode) {  this.appointkindcode = appointkindcode; }

	public String getAppointkinddescription() { return appointkinddescription; }

	public void setAppointkinddescription(String appointkinddescription) {  this.appointkinddescription = appointkinddescription;  }

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public PatientReference getPatient() {
		return patient;
	}

	public void setPatient(PatientReference patient) {
		this.patient = patient;
	}

	public VisitReference getVisit() {
		return visit;
	}

	public void setVisit(VisitReference visit) {
		this.visit = visit;
	}

	public List<Personnel> getPersonnels() {
		return personnels;
	}

	public void setPersonnels(List<Personnel> personnels) {
		this.personnels = personnels;
	}

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}
	
	public CompanyReference getCompany() {
		return company;
	}

	public void setCompany(CompanyReference company) {
		this.company = company;
	}
	
	public String getAnesthesia() {
		return anesthesia;
	}
	
	public void setAnesthesia(String anesthesia) {
		this.anesthesia = anesthesia;
	}
	
	public String getPatientPosition() {
		return patientPosition;
	}

	public void setPatientPosition(String patientPosition) {
		this.patientPosition = patientPosition;
	}

	@JsonIgnore
	@Override
	public Long getId() {
		return null;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public String displayName() {
		return this.appointmentId.toString();
	}
}
