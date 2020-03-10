package net.fluance.cockpit.core.util.sql;

public enum PatientAccessLogOrderByEnum {
	UNKNOWN(""), LOGDATE("logdt"), APPUSER("appuser"), FIRSTNAME("firstname"), LASTNAME("lastname"), DISPLAYNAME("displayname");

	private String value;

	PatientAccessLogOrderByEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	/**
	 * Get the value for the sort and treats the special cases
	 * 
	 * @param sortOrder
	 * @return literal for add before "sort by "
	 */
	public String getValueWithSortOrder(String sortOrder) {

		if (this.equals(APPUSER)) {
			return "UPPER(lastname) COLLATE \"C\" " + sortOrder + ", UPPER(firstname) COLLATE \"C\" " + sortOrder + ", UPPER(appuser) " + sortOrder;
		} else if (this.equals(LOGDATE)) {
			return "logdt " + sortOrder;
		} else {
			return this.getValue() + " " + sortOrder;
		}
	}

	/**
	 * 
	 * @param value
	 * @return AppointmentListOrderByEnum corresponding to the parameter value, if not found returns
	 *         AppointmentListOrderByEnum.UNKNOWN
	 */
	public static PatientAccessLogOrderByEnum permissiveValueOf(String value) {
		if (value == null) {
			return PatientAccessLogOrderByEnum.UNKNOWN;
		} else {
			try {
				return valueOf(value.toUpperCase());
			} catch (IllegalArgumentException illegalArgumentException) {
				return PatientAccessLogOrderByEnum.UNKNOWN;
			}
		}
	}
}
