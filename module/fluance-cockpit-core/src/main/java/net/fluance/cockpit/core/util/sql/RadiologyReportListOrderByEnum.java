package net.fluance.cockpit.core.util.sql;

public enum RadiologyReportListOrderByEnum {
	UNKNOWN(""),
	PID("patient_id"),
	ORDERNB("ordernb"),
	STUDYDT("studydt");
	
	private String value;

	private RadiologyReportListOrderByEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static RadiologyReportListOrderByEnum permissiveValueOf(String value){
		if (value == null){
			return RadiologyReportListOrderByEnum.UNKNOWN;
		} else {
			RadiologyReportListOrderByEnum orderby = valueOf(value.toUpperCase());
			if (orderby == null){
				return RadiologyReportListOrderByEnum.UNKNOWN;
			}
			return orderby;
		}
	}
}
