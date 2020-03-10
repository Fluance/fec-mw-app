package net.fluance.cockpit.core.model.jdbc.patient.byroom;

import java.io.Serializable;

public class RoomWithPatientsReferencesCount implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String patientRoom;
	private Integer nbPatients;
	
	public RoomWithPatientsReferencesCount(String roomId, Integer nbPatients) {
		this.patientRoom = roomId;
		this.nbPatients = nbPatients;
	}
	
	public String getPatientRoom() {
		return this.patientRoom;
	}
	
	public void setPatientRoom(String patientRoom) {
		this.patientRoom = patientRoom;
	}
	
	public Integer getNbPatients() {
		return this.nbPatients;
	}
	
	public void setNbPatients(Integer nbPatients) {
		this.nbPatients = nbPatients;
	}
}
