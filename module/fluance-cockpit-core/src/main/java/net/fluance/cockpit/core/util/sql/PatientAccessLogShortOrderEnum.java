package net.fluance.cockpit.core.util.sql;

public enum PatientAccessLogShortOrderEnum {
	UNKNOWN(""),
	ASC("asc"),
	DESC("desc");
	
	private final String value;

	private PatientAccessLogShortOrderEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static PatientAccessLogShortOrderEnum permissiveValueOf(String valueToCheck){		
		if (valueToCheck == null) {
			return PatientAccessLogShortOrderEnum.UNKNOWN;
		} else {
			PatientAccessLogShortOrderEnum orderby;
			try {
				orderby = valueOf(valueToCheck.toUpperCase());
			} catch (IllegalArgumentException exception) {
				return PatientAccessLogShortOrderEnum.UNKNOWN;
			} 
			return orderby;
		}
	}
}
