package net.fluance.cockpit.core.domain.intervention;

import java.sql.Timestamp;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateTimeSerializer;

public class Intervention implements Persistable<Long>{

	private Long visitNb;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Timestamp interventionDate;
	private String data;
    private String type;
	private Integer rank;
    
	public Intervention(Long visitNb, String data, String type, Integer rank, Timestamp interventiondt) {
		this.visitNb = visitNb;
		this.data = data;
		this.type = type;
		this.rank = rank;
		this.interventionDate = interventiondt;
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

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@JsonIgnore
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonIgnore
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
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
