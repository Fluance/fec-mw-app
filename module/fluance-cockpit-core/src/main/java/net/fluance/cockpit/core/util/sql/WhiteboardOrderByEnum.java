package net.fluance.cockpit.core.util.sql;

public enum WhiteboardOrderByEnum {
	UNKNOWN(""),
	ROOM("patientroom"),
	PATIENT_BED("patientbed");

	private String value;

	private WhiteboardOrderByEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}	
	/**
	 * 
	 * @param value
	 * @return InvoiceListOrderByEnum corresponding to the parameter value, if not found returns InvoiceListOrderByEnum.UNKNOWN
	 */
	public static WhiteboardOrderByEnum permissiveValueOf(String value){
		if (value == null){
			return WhiteboardOrderByEnum.UNKNOWN;
		} else {
			WhiteboardOrderByEnum orderby;
			try {
				orderby = valueOf(value.toUpperCase());
			} catch (IllegalArgumentException exception) {
				return WhiteboardOrderByEnum.UNKNOWN;
			} 
			return orderby;
		}
	}
}
