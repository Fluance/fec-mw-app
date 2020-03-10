package net.fluance.cockpit.core.model.jdbc.company;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
public class RoomOnlyList implements Persistable<Integer> {

	private String room;

	public RoomOnlyList(String room) {
		this.room = room;
	}

	public RoomOnlyList() {}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
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
