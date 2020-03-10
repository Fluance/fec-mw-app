package net.fluance.cockpit.core.model.jpa.whiteboard;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.fluance.cockpit.core.model.jdbc.whiteboard.WhiteBoardMapper;

@SuppressWarnings("serial")
@Entity(name = "whiteboard")
public class WhiteBoardViewEntity implements Persistable<Long>, Serializable {

	@Id
	@Column(name = WhiteBoardMapper.ID)
	private Long id;

	@Column(name = WhiteBoardMapper.VISIT_NUMBER)
	private Long visitNumber;

	@Transient
	private String room;

	@Transient
	private String sex;

	@Transient
	private String firstname;

	@Transient
	private String lastname;

	@Transient
	private Date entreeDate;

	@Transient
	private Date expireDate;

	@Column(name = WhiteBoardMapper.EXPIRE_DATE)
	private Date dischargeDate;

	@Transient
	private String insurance;

	@Transient
	private Date birthDate;

	@Transient
	private String physician;

	@Column(name = WhiteBoardMapper.NURSE)
	private String nurseName;

	@Column(name = WhiteBoardMapper.DIET)
	private String diet;

	@Transient
	private Date operationDate;

	@Column(name = WhiteBoardMapper.EDITED_OPERATION_DATE)
	private Date editedOperationDate;

	@Column(name = WhiteBoardMapper.ISOLATION_TYPE)
	private String isolationType;

	@Transient
	private String reason;
	
	@Column(name = WhiteBoardMapper.EDITED_REASON_WHITEBOAD_TABLE)
	private String editedReason;

	@Column(name = WhiteBoardMapper.COMMENT)
	private String comment;

	@Transient
	private Long companyId;

	@Transient
	private String serviceId;

	@Transient
	private String hl7Code;

	@Transient
	private Long appointmentId;

	@Transient
	private Long patientId;

	@Transient
	private String patientBed;
	
	@Transient
	private String patientClass;
	
	@Transient
	private Integer capacity;
	
	@Transient
	private Integer originalCapacity; 

	@Transient
	private Integer confCapacity; 
	
	@Transient
	private String roomType;

	@Column(name = WhiteBoardMapper.EDITED_PHYSICIAN_WHITEBOAD_TABLE)
	private String editedPhysician;

	@Override
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVisitNumber() {
		return this.visitNumber;
	}

	public void setVisitNumber(Long visitNumber) {
		this.visitNumber = visitNumber;
	}

	@Override
	@JsonIgnore
	public boolean isNew() {
		return false;
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

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public String getPhysician() {
		return physician;
	}

	public void setPhysician(String phisician) {
		this.physician = phisician;
	}

	public String getNurseName() {
		return nurseName;
	}

	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
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

	public Date getOperationDate() {
		return operationDate;
	}

	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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
	
	public Date getDischargeDate() {
		return dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	public Date getEditedOperationDate() {
		return editedOperationDate;
	}

	public void setEditedOperationDate(Date editedOperationDate) {
		this.editedOperationDate = editedOperationDate;
	}

	public String getEditedPhysician() {
		return editedPhysician;
	}

	public void setEditedPhysician(String editedPhysician) {
		this.editedPhysician = editedPhysician;
	}
	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getPatientClass() {
		return patientClass;
	}

	public void setPatientClass(String patientClass) {
		this.patientClass = patientClass;
	}

	public String getEditedReason() {
		return editedReason;
	}

	public void setEditedReason(String editedReason) {
		this.editedReason = editedReason;
	}
	
	public String getDiet() {
		return diet;
	}

	public void setDiet(String diet) {
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

}
