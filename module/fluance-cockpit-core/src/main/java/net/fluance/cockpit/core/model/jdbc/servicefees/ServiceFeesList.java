package net.fluance.cockpit.core.model.jdbc.servicefees;

import java.sql.Timestamp;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateTimeSerializer;

public class ServiceFeesList implements Persistable<Long>{

	private Integer nbRecords;
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
	private String description;
	private String descLanguage;
	private Boolean hasPhysician;
	
	public ServiceFeesList(Integer nbRecords, Long benefitId, Long visitNb, String code, Timestamp benefitDate, double quantity, String side,
			String actingDeptDescription, String note, String description, String descLanguage, Boolean hasPhysician) {
		this.nbRecords = nbRecords;
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
		this.hasPhysician = hasPhysician;
	}
	public Integer getNbRecords() {
		return nbRecords;
	}

	public void setNbRecords(Integer nbRecords) {
		this.nbRecords = nbRecords;
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
	public Boolean getHasPhysician() {
		return hasPhysician;
	}
	public void setHasPhysician(Boolean hasPhysician) {
		this.hasPhysician = hasPhysician;
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
