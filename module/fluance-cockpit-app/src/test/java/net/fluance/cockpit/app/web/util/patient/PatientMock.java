package net.fluance.cockpit.app.web.util.patient;

import net.fluance.cockpit.core.model.PatientReference;

public class PatientMock {
	private PatientMock() {}
	
	public static PatientReference getPatientReference() {
		PatientReference patientReference = new PatientReference();
		
		patientReference.setPid(1000L);
		patientReference.setFirstName("FOO");
		patientReference.setLastName("Lastname");
		patientReference.setMaidenName("MR.");
		
		return patientReference;
	}

}
