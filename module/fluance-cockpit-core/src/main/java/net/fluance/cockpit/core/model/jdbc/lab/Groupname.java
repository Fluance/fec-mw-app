package net.fluance.cockpit.core.model.jdbc.lab;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Groupname implements Persistable<Integer> {
	
	@JsonProperty("groupName")
	private String groupname;

	public Groupname() {}
	
	public Groupname(String groupname) {
		super();
		this.groupname = groupname;
	}
	
	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	@JsonIgnore
	@Override
	public Integer getId() {
		return null;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}
}
