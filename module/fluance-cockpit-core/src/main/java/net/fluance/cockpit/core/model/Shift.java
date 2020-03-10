package net.fluance.cockpit.core.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonFullDateDeserializer;
import net.fluance.commons.json.JsonFullDateSerializer;


public class Shift implements Serializable {
	
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonFullDateSerializer.class)
	@JsonDeserialize(using = JsonFullDateDeserializer.class)
	private Date startDate;
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonFullDateSerializer.class)
	@JsonDeserialize(using = JsonFullDateDeserializer.class)
	private Date endDate;
	
	public Shift(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Override
	public String toString(){
		return "startDate : " + startDate + " , endDate = " +endDate ;
	}
}
