package net.fluance.cockpit.core.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.fluance.cockpit.core.model.jdbc.accesslog.PatientAccessLogShort;
import net.fluance.commons.json.JsonDateTimeSerializer;

/**
 * DTO which maps the Repository class {@link PatientAccessLogShort}
 *
 */
public class AccessLogShort {

	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date logDate;
	
	private String detailUrl;

	@JsonInclude
	private AccessLogUser user;

	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	
	public String getDetailUrl() {
		return detailUrl;
	}
	
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	
	public AccessLogUser getUser() {
		return user;
	}
	
	public void setUser(AccessLogUser user) {
		this.user = user;
	}

}
