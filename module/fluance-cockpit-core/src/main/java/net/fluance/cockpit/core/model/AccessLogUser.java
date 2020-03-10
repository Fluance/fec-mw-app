package net.fluance.cockpit.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class AccessLogUser {
	private String username;
	private String firstName;
	private String lastName;
	
	@JsonInclude(Include.NON_EMPTY)
	private String actualUsername;
	
	@JsonInclude(Include.NON_EMPTY)
	private String actualFirstName;
	
	@JsonInclude(Include.NON_EMPTY)
	private String actualLastName;
	
	@JsonIgnore
	private String actualEmail;
	
	public String getUsername() {
		return username;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getActualUsername() {
		return actualUsername;
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

	public void setActualUsername(String actualUsername) {
		this.actualUsername = actualUsername;
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
