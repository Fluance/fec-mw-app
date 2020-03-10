package net.fluance.cockpit.core.util.sql;

/**
 * Enum for possible values for the appointments search order by
 */
public enum DoctorAppointmentListOrderByEnum {
	UNKNOWN(""),
	PID("patient_id, appointment_id"),
	APPOINT_ID("appointment_id"),
	BEGINDT("begindt"),
	NB("nb, appointment_id");
	
	private String value;

	private DoctorAppointmentListOrderByEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	/**
	 * Finds the value in the Enum for the given value, if not found returns AppointmentListOrderByEnum.UNKNOWN
	 * 
	 * @param value
	 * @return AppointmentListOrderByEnum corresponding to the parameter value, if not found returns AppointmentListOrderByEnum.UNKNOWN
	 */
	public static DoctorAppointmentListOrderByEnum permissiveValueOf(String value){
		if (value == null){
			return DoctorAppointmentListOrderByEnum.UNKNOWN;
		} else {
			DoctorAppointmentListOrderByEnum orderby;
			try {
				orderby = valueOf(value.toUpperCase());
			} catch (IllegalArgumentException exception) {
				return DoctorAppointmentListOrderByEnum.UNKNOWN;
			}
			return orderby;
		}
	}
}