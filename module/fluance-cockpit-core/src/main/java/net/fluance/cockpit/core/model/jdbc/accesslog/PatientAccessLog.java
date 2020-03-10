package net.fluance.cockpit.core.model.jdbc.accesslog;

import java.util.Date;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.fluance.commons.json.JsonDateTimeSerializer;
@SuppressWarnings("serial")
public class PatientAccessLog implements Persistable<Integer>{

	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date logDate;
	private String objectType;
	private String objectId;
	private String displayName;
	private String parentPid;
	private String parentVisitNb;
	private String httpMethod;
	private String appUser;
	private String firstName;
	private String lastName;
	private String externalUser;
	private String externalFirstName;
	private String externalLastName;
	private String externalEmail;
	
	public PatientAccessLog(Date logDate, String objectType, String objectId, String displayName, String parentPid,
			String parentVisitNb, String httpMethod, String appUser, String firstName, String lastName,
			String externalUser, String externalFirstName, String externalLastName, String externalEmail) {
		super();
		this.logDate = logDate;
		this.objectType = objectType;
		this.objectId = objectId;
		this.displayName = displayName;
		this.parentPid = parentPid;
		this.parentVisitNb = parentVisitNb;
		this.httpMethod = httpMethod;
		this.appUser = appUser;
		this.firstName = firstName;
		this.lastName = lastName;
		this.externalUser = externalUser;
		this.externalFirstName =externalFirstName;
		this.externalLastName = externalLastName;
		this.externalEmail = externalEmail;
	}

	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
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

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getAppUser() {
		return appUser;
	}

	public void setAppUser(String appUser) {
		this.appUser = appUser;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getExternalUser() {
		return externalUser;
	}
	
	public String getExternalFirstName() {
		return externalFirstName;
	}
	
	public String getExternalLastName() {
		return externalLastName;
	}

	public String getExternalEmail() {
		return externalEmail;
	}
	
	public void setExternalUser(String externalUser) {
		this.externalUser = externalUser;
	}

	public void setExternalFirstName(String externalFirstName) {
		this.externalFirstName = externalFirstName;
	}

	public void setExternalLastName(String externalLastName) {
		this.externalLastName = externalLastName;
	}
	
	public void setExternalEmail(String externalEmail) {
		this.externalEmail = externalEmail;
	}

	@Override
	@JsonIgnore
	public Integer getId() {
		return null;
	}

	@Override
	@JsonIgnore
	public boolean isNew() {
		return false;
	}

}
