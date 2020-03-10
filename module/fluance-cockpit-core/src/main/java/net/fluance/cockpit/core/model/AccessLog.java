package net.fluance.cockpit.core.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.fluance.cockpit.core.model.jdbc.accesslog.PatientAccessLog;
import net.fluance.commons.json.JsonDateTimeSerializer;

/**
 * DTO which maps the Repository class {@link PatientAccessLog}
 *
 */
public class AccessLog {

	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date logDate;
	
	@JsonInclude
	private String objectType;
	
	@JsonInclude
	private String httpMethod;
	
	@JsonInclude
	private String objectId;
	
	@JsonInclude
	private String displayName;
	
	@JsonInclude
	private String parentPid;
	
	@JsonInclude
	private String parentVisitNb;

	@JsonInclude
	private AccessLogUser user;

	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	
	public AccessLogUser getUser() {
		return user;
	}
	
	public void setUser(AccessLogUser user) {
		this.user = user;
	}
	
	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getParentPid() {
		return parentPid;
	}

	public void setParentPid(String parentPid) {
		this.parentPid = parentPid;
	}

	public String getParentVisitNb() {
		return parentVisitNb;
	}

	public void setParentVisitNb(String parentVisitNb) {
		this.parentVisitNb = parentVisitNb;
	}
	
}
