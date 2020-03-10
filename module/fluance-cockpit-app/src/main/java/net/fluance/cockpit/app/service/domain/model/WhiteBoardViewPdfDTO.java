package net.fluance.cockpit.app.service.domain.model;

import java.util.Date;


public class WhiteBoardViewPdfDTO implements Comparable<WhiteBoardViewPdfDTO> {
	
	private Long id;

	private Long visitNumber;

	private String room;

	private String sex;

	private String firstname;

	private String lastname;

	private Date entreeDate;

	private Date birthDate;

	private String insurance;

	private String nurseName;	

	private String isolationType;

	private String comment;

	private Long companyId;

	private String serviceId;

	private String hl7Code;

	private Long appointmentId;

	private Long patientId;

	private String patientBed;
	
	private String patientClass;
	
	private Integer capacity;
	
	private Date expireDate;
	
	private Date operationDate;
	
	private String diet;
	
	private String physician;
	
	private String reason;

	
	public Long getId() {
		return id;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	
	public Long getVisitNumber() {
		return visitNumber;
	}

	
	public void setVisitNumber(Long visitNumber) {
		this.visitNumber = visitNumber;
	}

	
	public String getRoom() {
		return room;
	}

	
	public void setRoom(String room) {
		this.room = room;
	}

	
	public String getSex() {
		return sex;
	}

	
	public void setSex(String sex) {
		this.sex = sex;
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

	
	public Date getEntreeDate() {
		return entreeDate;
	}

	
	public void setEntreeDate(Date entreeDate) {
		this.entreeDate = entreeDate;
	}

	
	public Date getExpireDate() {
		return expireDate;
	}

	
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}

	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	
	public String getInsurance() {
		return insurance;
	}

	
	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	
	public String getNurseName() {
		return nurseName;
	}

	
	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}

	
	public Date getOperationDate() {
		return operationDate;
	}

	
	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}


	public String getIsolationType() {
		return isolationType;
	}

	
	public void setIsolationType(String isolationType) {
		this.isolationType = isolationType;
	}

	
	public String getComment() {
		return comment;
	}

	
	public void setComment(String comment) {
		this.comment = comment;
	}

	
	public Long getCompanyId() {
		return companyId;
	}

	
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	
	public String getServiceId() {
		return serviceId;
	}

	
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	
	public String getHl7Code() {
		return hl7Code;
	}

	
	public void setHl7Code(String hl7Code) {
		this.hl7Code = hl7Code;
	}

	
	public Long getAppointmentId() {
		return appointmentId;
	}

	
	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	
	public Long getPatientId() {
		return patientId;
	}

	
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	
	public String getPatientBed() {
		return patientBed;
	}

	
	public void setPatientBed(String patientBed) {
		this.patientBed = patientBed;
	}

	
	public String getPatientClass() {
		return patientClass;
	}

	
	public void setPatientClass(String patientClass) {
		this.patientClass = patientClass;
	}

	
	public Integer getCapacity() {
		return capacity;
	}

	
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	
	public String getDiet() {
		return diet;
	}

	
	public void setDiet(String diet) {
		this.diet = diet;
	}

	
	public String getPhysician() {
		return physician;
	}

	
	public void setPhysician(String physician) {
		this.physician = physician;
	}

	
	public String getReason() {
		return reason;
	}

	
	public void setReason(String reason) {
		this.reason = reason;
	}


	@Override
	public int compareTo(WhiteBoardViewPdfDTO o) {
		if(this.room  != null  && o.getRoom() != null) {			
			if(this.room.compareTo(o.getRoom()) == 0) {
				//Same room test patien Bed
				if(this.patientBed != null  && o.getPatientBed() != null) {
					return this.patientBed.compareTo(o.getPatientBed());
				} else if(this.patientBed != null  && o.getPatientBed() == null) {
					return 1;
				} else if(this.patientBed == null  && o.getPatientBed() != null) {
					return -1;
				} else {
					return 0;
				}
			} else {
				return this.room.compareTo(o.getRoom());
			}			
		} else if(this.room != null  && o.getRoom() == null) {
			return 1;
		} else if(this.room == null  && o.getRoom() != null) {
			return -1;
		} else {
			return 0;
		}
	}
	
}
