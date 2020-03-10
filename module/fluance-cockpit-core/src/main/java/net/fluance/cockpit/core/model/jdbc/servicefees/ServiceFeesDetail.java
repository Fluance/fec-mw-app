package net.fluance.cockpit.core.model.jdbc.servicefees;

import java.sql.Timestamp;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateTimeSerializer;

public class ServiceFeesDetail implements Persistable<Long>{

	private Long benefitId;
	private Long visitNb;
	private String code;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Timestamp benefitDate;
	private double quantity;
	private String side;
	private String actingDeptDescription;
	private String note;
	@ApiModelProperty( dataType="java.lang.String")
	private String description;
	private String descLanguage;
	private Integer paidPhysicianId;
	private String paidPhysicianFirstName;
	private String paidPhysicianLastName;
	private Integer operatingPhysicianId;
	private String operatingPhysicianFirstName;
	private String operatingPhysicianLastName;
	private Integer leadPhysicianId;
	private String leadPhysicianFirstName;
	private String leadPhysicianLastName;
	
	public ServiceFeesDetail(Long benefitId, Long visitNb, String code, Timestamp benefitDate, double quantity, String side,
			String actingDeptDescription, String note, String description, String descLanguage,
			Integer paidPhysicianId, String paidPhysicianFirstName, String paidPhysicianLastName,
			Integer operatingPhysicianId, String operatingPhysicianFirstName, String operatingPhysicianLastName,
			Integer leadPhysicianId, String leadPhysicianFirstName, String leadPhysicianLastName) {
		this.benefitId = benefitId;
		this.visitNb = visitNb;
		this.code = code;
		this.benefitDate = benefitDate;
		this.quantity = quantity;
		this.side = side;
		this.actingDeptDescription = actingDeptDescription;
		this.note = note;
		this.description = description;
		this.descLanguage = descLanguage;
		this.paidPhysicianId = paidPhysicianId;
		this.paidPhysicianFirstName = paidPhysicianFirstName;
		this.paidPhysicianLastName = paidPhysicianLastName;
		this.operatingPhysicianId = operatingPhysicianId;
		this.operatingPhysicianFirstName = operatingPhysicianFirstName;
		this.operatingPhysicianLastName = operatingPhysicianLastName;
		this.leadPhysicianId = leadPhysicianId;
		this.leadPhysicianFirstName = leadPhysicianFirstName;
		this.leadPhysicianLastName = leadPhysicianLastName;
	}
	public Long getBenefitId() {
		return benefitId;
	}
	public void setBenefitId(Long benefitId) {
		this.benefitId = benefitId;
	}
	public Long getVisitNb() {
		return visitNb;
	}
	public void setVisitNb(Long visitNb) {
		this.visitNb = visitNb;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Timestamp getBenefitDate() {
		return benefitDate;
	}
	public void setBenefitdate(Timestamp benefitDate) {
		this.benefitDate = benefitDate;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public String getSide() {
		return side;
	}
	public void setSide(String side) {
		this.side = side;
	}
	public String getActingDeptDescription() {
		return actingDeptDescription;
	}
	public void setActingDeptDescription(String actingDeptDescription) {
		this.actingDeptDescription = actingDeptDescription;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescLanguage() {
		return descLanguage;
	}
	public void setDescLanguage(String descLanguage) {
		this.descLanguage = descLanguage;
	}
	public Integer getPaidPhysicianId() {
		return paidPhysicianId;
	}
	public void setPaidPhysicianId(Integer paidPhysicianId) {
		this.paidPhysicianId = paidPhysicianId;
	}
	public String getPaidPhysicianFirstName() {
		return paidPhysicianFirstName;
	}
	public void setPaidPhysicianFirstName(String paidPhysicianFirstName) {
		this.paidPhysicianFirstName = paidPhysicianFirstName;
	}
	public String getPaidPhysicianLastName() {
		return paidPhysicianLastName;
	}
	public void setPaidPhysicianLastName(String paidPhysicianLastName) {
		this.paidPhysicianLastName = paidPhysicianLastName;
	}
	public Integer getOperatingPhysicianId() {
		return operatingPhysicianId;
	}
	public void setOperatingPhysicianId(Integer operatingPhysicianId) {
		this.operatingPhysicianId = operatingPhysicianId;
	}
	public String getOperatingPhysicianFirstName() {
		return operatingPhysicianFirstName;
	}
	public void setOperatingPhysicianFirstName(String operatingPhysicianFirstName) {
		this.operatingPhysicianFirstName = operatingPhysicianFirstName;
	}
	public String getOperatingPhysicianLastName() {
		return operatingPhysicianLastName;
	}
	public void setOperatingPhysicianLastName(String operatingPhysicianLastName) {
		this.operatingPhysicianLastName = operatingPhysicianLastName;
	}
	public Integer getLeadPhysicianId() {
		return leadPhysicianId;
	}
	public void setLeadPhysicianId(Integer leadPhysicianId) {
		this.leadPhysicianId = leadPhysicianId;
	}
	public String getLeadPhysicianFirstName() {
		return leadPhysicianFirstName;
	}
	public void setLeadPhysicianFirstName(String leadPhysicianFirstName) {
		this.leadPhysicianFirstName = leadPhysicianFirstName;
	}
	public String getLeadPhysicianLastName() {
		return leadPhysicianLastName;
	}
	public void setLeadPhysicianLastName(String leadPhysicianLastName) {
		this.leadPhysicianLastName = leadPhysicianLastName;
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

}
