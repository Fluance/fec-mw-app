package net.fluance.cockpit.core.model.jdbc.patient.byroom;

import java.io.Serializable;
import java.util.List;

import net.fluance.cockpit.core.model.wrap.patient.PatientInList;

public class RoomWithPatientsDetails implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String patientRoom;
	private Integer nbPatients;
	
	private List<PatientInList> patients;
	
	public RoomWithPatientsDetails(String roomId, Integer nbPatients, List<PatientInList> patientReferences) {
		this.patientRoom = roomId;
		this.nbPatients = nbPatients;
		this.patients = patientReferences;
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

	public List<PatientInList> getPatients() {
		return patients;
	}

	public void setPatients(List<PatientInList> patientReferences) {
		this.patients = patientReferences;
	}
}
