package net.fluance.cockpit.core.util.sql;

public enum VisitListOrderByEnum {
	UNKNOWN(""),
	NB("nb"),
	ADMITDT("admitdt"),
	DISCHARGEDT("dischargedt"),
	HOSPSERVICE("hospservice"),
	PATIENTUNIT("patientunit"),
	PATIENTROOM("patientroom"),
    PATIENTBED("patientbed");
	
	private String value;

	private VisitListOrderByEnum(String value) {
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
	public static VisitListOrderByEnum permissiveValueOf(String value){
		if (value == null){
			return VisitListOrderByEnum.UNKNOWN;
		} else {
			VisitListOrderByEnum orderby = valueOf(value.toUpperCase());
			if (orderby == null){
				return VisitListOrderByEnum.UNKNOWN;
			}
			return orderby;
		}
	}
}
