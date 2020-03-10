/**
 * 
 */
package net.fluance.cockpit.core.model.jdbc.physician;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;


@SuppressWarnings("serial")
public class PhysicianByStaffid implements Persistable<Integer> {
	
	private String firstname;
	private String lastname;
	private Integer staffid;
	private Integer id;
	
    public PhysicianByStaffid(){}
	public PhysicianByStaffid(String firstname, String lastname, Integer staffid, Integer id) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.staffid = staffid;
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getLastname() {
		return lastname;
	}


	public void setLastname(String lastname) {
		this.lastname = lastname;
	}


	public Integer getStaffid() {
		return staffid;
	}


	public void setStaffid(Integer staffid) {
		this.staffid = staffid;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}
}