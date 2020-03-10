package net.fluance.cockpit.core.util.sql;

public enum PolicyListOrderByEnum {
	UNKNOWN(""),
	ID("id"),
	CODE("code"),
	NAME("name"),
	PRIORITY("priority"),
	SUBPRIORITY("subpriority"),
	ACCIDENTDATE("accidentdate"),
	BEGINDATE("begindate"),
	ENDDATE("enddate");
	
	private String value;

	private PolicyListOrderByEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @param value
	 * @return InvoiceListOrderByEnum corresponding to the parameter value, if not found returns InvoiceListOrderByEnum.UNKNOWN
	 */
	public static PolicyListOrderByEnum permissiveValueOf(String value){
		if (value == null){
			return PolicyListOrderByEnum.UNKNOWN;
		} else {
			PolicyListOrderByEnum orderby = valueOf(value.toUpperCase());
			if (orderby == null){
				return PolicyListOrderByEnum.UNKNOWN;
			}
			return orderby;
		}
	}
}
