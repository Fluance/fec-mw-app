package net.fluance.cockpit.core.model.jdbc.visit;

public enum PatientClassEnum {

	UNKNOWN("U",""),
	INPATIENT("I", "STAT"),
	OUTPATIENT("O", "AMB");

	private String patientShortClassCode;
	private String patientClassCode;

	private PatientClassEnum(String value, String patientClassCode) {
		this.patientShortClassCode = value;
		this.patientClassCode = patientClassCode;
	}

	public String getPatientClassShortCode() {
		return this.patientShortClassCode;
	}
	
	public String getPatientClassCode() {
		return this.patientClassCode;
	}

	
	/**
	 * Return a PatientClassEnum matching the patientClassCode parameter if such one is declared in the enumeration. Returns null if nothing matches.
	 * card
	 * @param patientClassCode
	 * @return the PatientClassEnum matching to the given patientClassCode parameter
	 */
	public static PatientClassEnum withPatientShortClassCode(String patientClassCode) {
		PatientClassEnum patientClassEnum = null;
		
		if (patientClassCode == null) return null;
		
		patientClassCode = patientClassCode.toUpperCase();
		
		// Check if passed value is available in the enumeration ...
		for (PatientClassEnum patientClass : PatientClassEnum.values()) {
			if  (patientClass.getPatientClassShortCode().equals(patientClassCode)) {
				patientClassEnum = patientClass;
				break;
			}
		}
		
		// ... else throw an exception
		if (patientClassEnum == null) {
			throw new IllegalArgumentException("Unknown patient class short code : " + patientClassCode);
		}
		
		return patientClassEnum;
	}
	
}
