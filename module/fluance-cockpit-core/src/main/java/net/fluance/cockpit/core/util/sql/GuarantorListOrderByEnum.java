package net.fluance.cockpit.core.util.sql;

public enum GuarantorListOrderByEnum {
	UNKNOWN(""),
	ID("id"),
	CODE("code"),
	NAME("name"),
	BEGINDATE("begindate"),
	ENDDATE("enddate");

	private String value;

	private GuarantorListOrderByEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	/**
	 * 
	 * @param value
	 * @return AppointmentListOrderByEnum corresponding to the parameter value, if not found returns AppointmentListOrderByEnum.UNKNOWN
	 */
	public static GuarantorListOrderByEnum permissiveValueOf(String value){
		if (value == null){
			return GuarantorListOrderByEnum.UNKNOWN;
		} else {
			GuarantorListOrderByEnum orderby;
			try {
				orderby = valueOf(value.toUpperCase());
			} catch (IllegalArgumentException exception) {
				return GuarantorListOrderByEnum.UNKNOWN;
			} 
			return orderby;
		}
	}
}
