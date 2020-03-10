package net.fluance.cockpit.core.util.sql;

public enum InvoiceListOrderByEnum {
	UNKNOWN(""),
	ID("id"),
	INVDT("invdt"),
	GUARANTOR_ID("guarantor_id");

	private String value;

	private InvoiceListOrderByEnum(String value) {
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
	public static InvoiceListOrderByEnum permissiveValueOf(String value){
		if (value == null){
			return InvoiceListOrderByEnum.UNKNOWN;
		} else {
			InvoiceListOrderByEnum orderby;
			try {
				orderby = valueOf(value.toUpperCase());
			} catch (IllegalArgumentException exception) {
				return InvoiceListOrderByEnum.UNKNOWN;
			} 
			return orderby;
		}
	}
}
