package net.fluance.cockpit.core.model.jdbc.accesslog;

import java.util.Date;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.fluance.commons.json.JsonDateTimeSerializer;

/**
 * Short object version for {@link PatientAccessLog} only with the data that is required for the list of access by
 * patient group by user
 *
 */
@SuppressWarnings("serial")
public class PatientAccessLogShort implements Persistable<Integer> {

	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date logDate;
	private String appUser;	
	private String firstName;
	private String lastName;
	private String externalUser;
	private String externalFirstName;
	private String externalLastName;
	private String externalEmail;

	public PatientAccessLogShort(Date logDate, String appUser, String firstName, String lastName, String externalUser, String externalFirstName, String externalLastName, String externalEmail) {
		super();
		this.logDate = logDate;
		this.appUser = appUser;
		this.firstName = firstName;
		this.lastName = lastName;
		this.externalUser = externalUser;
		this.externalFirstName = externalFirstName;
		this.externalLastName = externalLastName;
		this.externalEmail = externalEmail;
	}

	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
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

	public void setExternalUser(String externalUser) {
		this.externalUser = externalUser;
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
