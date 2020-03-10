package net.fluance.cockpit.core.model.jdbc.appointment;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateTimeSerializer;

public class Personnel implements Serializable{
	private String name;
	private String role;
	private String staffId;
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date beginDate;
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date endDate;
	private String occupationCode;

	public Personnel() {
		//EMPTY
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getCodeOccupation() {
		return occupationCode;
	}

	public void setOccupationCode(String occupationCode) {
		this.occupationCode = occupationCode;
	}
}