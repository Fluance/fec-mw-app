/**
 * 
 */
package net.fluance.cockpit.core.model.jdbc.appointment;

import java.util.Date;
import java.sql.Timestamp;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateTimeSerializer;

@SuppressWarnings("serial")
public class Appointment extends AppointmentNew implements Persistable<Integer>{

	private String duration;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonProperty("endDate")
	private Date endt;
	@JsonProperty("appointmentType")
	private String appointment_type;
	private String status;
	@JsonProperty("staffId")
	private Integer staffid;
	@JsonProperty("birthDate")
	private String birthdate;
	@JsonProperty("hospService")
	private String hospservice;
	@JsonProperty("hospserviceDesc")
	private String hospservicedesc;
	@JsonProperty("patientUnit")
	private String patientunit;
	@JsonProperty("patientUnitDesc")
	private String patientunitdesc;
	@JsonProperty("patientClass")
	private String patientclass;
	@JsonProperty("patientClassDesc")
	private String patientclassdesc;
	@JsonProperty("companyId")
	private Integer company_id;

	
	public Appointment(){}
	
	public Appointment(Integer nb_records, Long appoint_id, Long pid, String patientroom, String status, Long visit_nb,  
			String firstname, String lastname,String maidenname,
			Date begindt,String description){
		super(nb_records, appoint_id, pid, patientroom, visit_nb, firstname, lastname, maidenname, begindt, description);
	}
	
	public Appointment(Integer nb_records, Long appoint_id,
			Long visit_nb, String duration, Date begindt, Date endt,
			String appointment_type, String description, String status,
			Integer staffid, Long pid, String firstname, String lastname,
			String maidenname, String birthdate, String hospservice,
			String hospservicedesc, String patientunit, String patientunitdesc,
			String patientroom, String patientclass, String patientclassdesc,
			Integer company_id) {
		super(nb_records, appoint_id, pid, patientroom, visit_nb, firstname, lastname, maidenname, begindt, description);
		this.duration = duration;
		this.endt = endt;
		this.appointment_type = appointment_type;
		this.staffid = staffid;
		this.status = status;
		this.birthdate = birthdate;
		this.hospservice = hospservice;
		this.hospservicedesc = hospservicedesc;
		this.patientunit = patientunit;
		this.patientunitdesc = patientunitdesc;
		this.patientclass = patientclass;
		this.patientclassdesc = patientclassdesc;
		this.company_id = company_id;
	}

	/**
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}


	/**
	 * @return the endt
	 */
	public Date getEndt() {
		return endt;
	}

	/**
	 * @param endt the endt to set
	 */
	public void setEndt(Date endt) {
		this.endt = endt;
	}

	/**
	 * @return the appointment_type
	 */
	public String getAppointment_type() {
		return appointment_type;
	}

	/**
	 * @param appointment_type the appointment_type to set
	 */
	public void setAppointment_type(String appointment_type) {
		this.appointment_type = appointment_type;
	}

	/**
	 * @return the staffid
	 */
	public Integer getStaffid() {
		return staffid;
	}

	/**
	 * @param staffid the staffid to set
	 */
	public void setStaffid(Integer staffid) {
		this.staffid = staffid;
	}

	/**
	 * @return the birthdate
	 */
	public String getBirthdate() {
		return birthdate;
	}

	/**
	 * @param birthdate the birthdate to set
	 */
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	/**
	 * @return the hospservice
	 */
	public String getHospservice() {
		return hospservice;
	}

	/**
	 * @param hospservice the hospservice to set
	 */
	public void setHospservice(String hospservice) {
		this.hospservice = hospservice;
	}

	/**
	 * @return the hospservicedesc
	 */
	public String getHospservicedesc() {
		return hospservicedesc;
	}

	/**
	 * @param hospservicedesc the hospservicedesc to set
	 */
	public void setHospservicedesc(String hospservicedesc) {
		this.hospservicedesc = hospservicedesc;
	}

	/**
	 * @return the patientunit
	 */
	public String getPatientunit() {
		return patientunit;
	}

	/**
	 * @param patientunit the patientunit to set
	 */
	public void setPatientunit(String patientunit) {
		this.patientunit = patientunit;
	}

	/**
	 * @return the patientunitdesc
	 */
	public String getPatientunitdesc() {
		return patientunitdesc;
	}

	/**
	 * @param patientunitdesc the patientunitdesc to set
	 */
	public void setPatientunitdesc(String patientunitdesc) {
		this.patientunitdesc = patientunitdesc;
	}

	/**
	 * @return the patientclass
	 */
	public String getPatientclass() {
		return patientclass;
	}

	/**
	 * @param patientclass the patientclass to set
	 */
	public void setPatientclass(String patientclass) {
		this.patientclass = patientclass;
	}

	/**
	 * @return the patientclassdesc
	 */
	public String getPatientclassdesc() {
		return patientclassdesc;
	}

	/**
	 * @param patientclassdesc the patientclassdesc to set
	 */
	public void setPatientclassdesc(String patientclassdesc) {
		this.patientclassdesc = patientclassdesc;
	}

	/**
	 * @return the company_id
	 */
	public Integer getCompany_id() {
		return company_id;
	}

	/**
	 * @param company_id the company_id to set
	 */
	public void setCompany_id(Integer company_id) {
		this.company_id = company_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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