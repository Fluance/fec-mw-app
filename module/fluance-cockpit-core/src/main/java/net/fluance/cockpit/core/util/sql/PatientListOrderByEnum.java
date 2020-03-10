package net.fluance.cockpit.core.util.sql;

public enum PatientListOrderByEnum {
	UNKNOWN(""),
	PATIENT_ID("patient_id"),
	FIRSTNAME("firstname"),
	LASTNAME("lastname"),
	MAIDENNAME("maidenname"),
	BIRTHDATE("birthdate"),
	NB("nb"),
	ADMITDT("admitdt"),
	DISCHARGEDT("dischargedt"),
	PATIENTUNIT("patientunit"),
	PATIENTROOM("patientroom"),
	PATIENTBED("patientbed");

	private String value;

	private PatientListOrderByEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	/**
	 * 
	 * @param value
	 * @return PatientListOrderByEnum corresponding to the parameter value, if not found returns PatientListOrderByEnum.UNKNOWN
	 */
	public static PatientListOrderByEnum permissiveValueOf(String value){
		if (value == null || value.isEmpty()){
			return PatientListOrderByEnum.UNKNOWN;
		} else {
			PatientListOrderByEnum orderby;
			try {
				orderby = valueOf(value.toUpperCase());
			} catch (IllegalArgumentException exception) {
				return PatientListOrderByEnum.UNKNOWN;
			}
			return orderby;
		}
	}
}
