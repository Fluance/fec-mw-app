package net.fluance.cockpit.core.model.jdbc.patient.byroom;

import org.springframework.data.domain.Persistable;

public class PatientsInRoomCount implements Persistable<Integer> {

	private static final long serialVersionUID = 1L;
	
	Integer roomCount;
	String patientRoom;

	public PatientsInRoomCount(Integer roomCount, String patientRoom) {
		this.roomCount = roomCount;
		this.patientRoom = patientRoom;
	}

	public Integer getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(Integer roomCount) {
		this.roomCount = roomCount;
	}

	public String getPatientRoom() {
		return patientRoom;
	}

	public void setPatientRoom(String patientRoom) {
		this.patientRoom = patientRoom;
	}

	@Override
	public Integer getId() {
		return null;
	}

	@Override
	public boolean isNew() {
		return false;
	}
}
