package net.fluance.cockpit.core.model.jdbc.company;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Capacity implements Persistable<Integer>{
	
	private static final long serialVersionUID = 1L;
	
	private String roomnumber;
	private Integer nbbed;
	
	public Capacity(String roomnumber, Integer nbbed) {
		super();
		this.roomnumber = roomnumber;
		this.nbbed = nbbed;
	}

	public String getRoomnumber() {
		return roomnumber;
	}
	
	public void setRoomnumber(String roomnumber) {
		this.roomnumber = roomnumber;
	}
	
	public Integer getNbbed() {
		return nbbed;
	}
	
	public void setNbbed(Integer nbbed) {
		this.nbbed = nbbed;
	}

	@JsonIgnore
	@Override
	public Integer getId() {
		return 0;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}
	
	
}
