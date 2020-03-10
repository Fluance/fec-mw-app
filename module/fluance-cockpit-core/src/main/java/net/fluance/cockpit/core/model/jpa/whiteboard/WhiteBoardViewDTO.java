package net.fluance.cockpit.core.model.jpa.whiteboard;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonFullDateDeserializer;
import net.fluance.commons.json.JsonFullDateSerializer;

public class WhiteBoardViewDTO implements Comparable<WhiteBoardViewDTO>{

	private Long id;

	private Long visitNumber;

	private String room;

	private String sex;

	private String firstname;

	private String lastname;

	private Date entreeDate;

	private Date expireDate;
	
	private Date editedExpireDate;
	
	private Date birthDate;

	private String insurance;

	private List<String> physician;

	private String nurseName;

	private List<String> diet;

	private Date operationDate;
	
	private Date editedOperationDate;

	private String isolationType;

	private List<String> reason;

	private List<String> editedReason;

	private String comment;

	private Long companyId;

	private String serviceId;

	private String hl7Code;

	private Long appointmentId;

	private Long patientId;

	private String patientBed;
	
	@JsonIgnore(value = true)
	private String patientClass;
	
	private List<String> editedPhysician;
	
	private Integer capacity;

	private Integer originalCapacity;
	
	private Integer confCapacity;
	
	private String roomType;
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonProperty("visitId")
	public Long getVisitNumber() {
		return this.visitNumber;
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

	@JsonProperty("admitDate")
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonFullDateSerializer.class)
	@JsonDeserialize(using = JsonFullDateDeserializer.class)
	public Date getEntreeDate() {
		return entreeDate;
	}

	public void setEntreeDate(Date entreeDate) {
		this.entreeDate = entreeDate;
	}
	
	@JsonProperty("dischargeDate")
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonFullDateSerializer.class)
	@JsonDeserialize(using = JsonFullDateDeserializer.class)
	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	
	@JsonProperty("editedDischargeDate")
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonFullDateSerializer.class)
	@JsonDeserialize(using = JsonFullDateDeserializer.class)
	public Date getEditedExpireDate() {
		return editedExpireDate;
	}

	public void setEditedExpireDate(Date editedExpireDate) {
		this.editedExpireDate = editedExpireDate;
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

	public String getIsolationType() {
		return isolationType;
	}

	public void setIsolationType(String isolationType) {
		this.isolationType = isolationType;
	}
	
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonFullDateSerializer.class)
	@JsonDeserialize(using = JsonFullDateDeserializer.class)
	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}
	
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonFullDateSerializer.class)
	@JsonDeserialize(using = JsonFullDateDeserializer.class)
	public Date getEditedOperationDate() {
		return editedOperationDate;
	}

	public void setEditedOperationDate(Date editedOperationDate) {
		this.editedOperationDate = editedOperationDate;
	}

	public List<String> getReason() {
		return reason;
	}

	public void setReason(List<String> reason) {
		this.reason = reason;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@JsonProperty("companyId")
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@JsonProperty("hospService")
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	@JsonProperty("patientClass")
	public String getHl7Code() {
		return hl7Code;
	}

	public void setHl7Code(String hl7Code) {
		this.hl7Code = hl7Code;
	}

	@JsonProperty("appointmentId")
	public Long getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	@JsonProperty("patientId")
	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public List<String> getPhysician() {
		return physician;
	}

	public void setPhysician(List<String> physician) {
		this.physician = physician;
	}

	public String getNurseName() {
		return nurseName;
	}

	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}

	public String getPatientBed() {
		return patientBed;
	}

	public void setPatientBed(String patientBed) {
		this.patientBed = patientBed;
	}
	@JsonProperty("birthDate")
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonFullDateSerializer.class)
	@JsonDeserialize(using = JsonFullDateDeserializer.class)
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public List<String> getEditedPhysician() {
		return editedPhysician;
	}

	public void setEditedPhysician(List<String> editedPhysician) {
		this.editedPhysician = editedPhysician;
	}

	public String getPatientClass() {
		return patientClass;
	}

	public void setPatientClass(String patientClass) {
		this.patientClass = patientClass;
	}

	public List<String> getEditedReason() {
		return editedReason;
	}

	public void setEditedReason(List<String> editedReason) {
		this.editedReason = editedReason;
	}
	
	public List<String> getDiet() {
		return diet;
	}

	public void setDiet(List<String> diet) {
		this.diet = diet;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public Integer getOriginalCapacity() {
		return originalCapacity;
	}

	public void setOriginalCapacity(Integer originalCapacity) {
		this.originalCapacity = originalCapacity;
	}

	public Integer getConfCapacity() {
		return confCapacity;
	}

	public void setConfCapacity(Integer confCapacity) {
		this.confCapacity = confCapacity;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	
	@Override
	public int compareTo(WhiteBoardViewDTO o) {
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
