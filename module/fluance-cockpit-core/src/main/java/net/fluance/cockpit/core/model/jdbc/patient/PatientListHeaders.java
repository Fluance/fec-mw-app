package net.fluance.cockpit.core.model.jdbc.patient;

public enum PatientListHeaders {

	HEADER_ROOM("h_room"), 
	HEADER_PATIEN("h_patient"), 
	HEADER_ADMISSION("h_admitdt"), 
	HEADER_INSURANCE("h_insurance"), 
	HEADER_DISCHARGE("h_expiredt"), 
	HEADER_PHYSICIAN("h_physician"), 
	HEADER_NURSE("h_nurse"), 
	HEADER_FASTING("h_fasting"), 
	HEADER_DIET("h_diet"), 
	HEADER_ISOLATION("h_isolationtype"), 
	HEADER_OPERATION_DATE("h_operationdt"), 
	HEADER_REASON("h_reason"), 
	HEADER_NOTES("h_notes");

	private String value;

	private PatientListHeaders(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

}
