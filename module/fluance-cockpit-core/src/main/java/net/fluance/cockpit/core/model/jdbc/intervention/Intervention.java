package net.fluance.cockpit.core.model.jdbc.intervention;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.cockpit.core.model.PayloadDisplayLogName;
import net.fluance.commons.json.JsonDateTimeSerializer;

@SuppressWarnings("serial")
public class Intervention implements Persistable<Long>, PayloadDisplayLogName{

	private Long visitNb;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Timestamp interventionDate;
	private List<Operation> operations;
	private List<Diagnosis> diagnosis;
    
	public Intervention(Long visitNb, Timestamp interventionDate, List<Operation> operations,
			List<Diagnosis> diagnosis) {
		this.visitNb = visitNb;
		this.interventionDate = interventionDate;
		this.operations = operations;
		this.diagnosis = diagnosis;
	}
	
	public Long getVisitNb() {
		return visitNb;
	}

	public void setVisitNb(Long visitNb) {
		this.visitNb = visitNb;
	}

	public Timestamp getInterventionDate() {
		return interventionDate;
	}

	public void setInterventionDate(Timestamp interventionDate) {
		this.interventionDate = interventionDate;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	public List<Diagnosis> getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(List<Diagnosis> diagnosis) {
		this.diagnosis = diagnosis;
	}

	@JsonIgnore
	@Override
	public Long getId() {
		return this.visitNb;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public String displayName() {
		return visitNb.toString();
	}
}
