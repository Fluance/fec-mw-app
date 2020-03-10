package net.fluance.cockpit.core.model;

import net.fluance.app.log.model.LogModel;

public class PatientAccessLogModel extends LogModel {
	
	private String firstName;
	private String lastName;
	private String displayName;
	private String actualUserName;
	private String actualFirstName;
	private String actualLastName;
	private String actualEmail;
	
	public PatientAccessLogModel() {
		super();
	}

	public PatientAccessLogModel(String userName, String resourceId, String httpMethod, String parentPid,
			String parentVisitNb, String uri, String parameters, String resourceType) {
		super(userName, resourceId, httpMethod, parentPid, parentVisitNb, uri, parameters, resourceType);
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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getActualUserName() {
		return actualUserName;
	}
	
	public String getActualFirstName() {
		return actualFirstName;
	}
	
	public String getActualLastName() {
		return actualLastName;
	}
	
	public String getActualEmail() {
		return actualEmail;
	}

	public void setActualUserName(String actualUserName) {
		this.actualUserName = actualUserName;
	}
	
	public void setActualFirstName(String actualFirstName) {
		this.actualFirstName = actualFirstName;
	}
	
	public void setActualLastName(String actualLastName) {
		this.actualLastName = actualLastName;
	}
	
	public void setActualEmail(String actualEmail) {
		this.actualEmail = actualEmail;
	}
}
