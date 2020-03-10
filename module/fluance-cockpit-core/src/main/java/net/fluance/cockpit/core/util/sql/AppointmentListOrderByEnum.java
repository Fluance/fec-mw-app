package net.fluance.cockpit.core.util.sql;

public enum AppointmentListOrderByEnum {
	UNKNOWN(""), APPOINT_ID("appoint_id"), BEGINDT("begindt"), PID("pid"), NB("nb"), PATIENTROOM("patientroom"),
	LASTNAME("lastname"), FIRSTNAME("firstname"), MAIDENNAME("maidenname");

	private String value;

	private AppointmentListOrderByEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	/**
	 * 
	 * @param value
	 * @return AppointmentListOrderByEnum corresponding to the parameter value, if
	 *         not found returns AppointmentListOrderByEnum.UNKNOWN
	 */
	public static AppointmentListOrderByEnum permissiveValueOf(String value) {
		if (value == null) {
			return AppointmentListOrderByEnum.UNKNOWN;
		} else {
			AppointmentListOrderByEnum orderby;
			try {
				orderby = valueOf(value.toUpperCase());
			} catch (IllegalArgumentException exception) {
				return AppointmentListOrderByEnum.UNKNOWN;
			} 
			return orderby;
		}
	}
}
