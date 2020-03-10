package net.fluance.cockpit.core.model.jdbc.patient.byroom;

import java.io.Serializable;
import java.util.ArrayList;
import net.fluance.cockpit.core.model.PatientReference;

public class RoomWithPatientsReferences implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String patientRoom;
	private Integer nbPatients;
	
	private ArrayList<PatientReference> patients;
	
	public RoomWithPatientsReferences(String roomId, Integer nbPatients, ArrayList<PatientReference> patientReferences) {
		this.patientRoom = roomId;
		this.nbPatients = nbPatients;
		this.setPatients(patientReferences);
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

	public ArrayList<PatientReference> getPatients() {
		return patients;
	}

	public void setPatients(ArrayList<PatientReference> patientReferences) {
		this.patients = patientReferences;
	}
}
