package net.fluance.cockpit.core.util.sql;

public enum BenefitListOrderByEnum {
	UNKNOWN(""),
	CODE("code"),
	BENEFITDT("benefitdt");
	
	private String value;

	private BenefitListOrderByEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static BenefitListOrderByEnum permissiveValueOf(String value){
		if (value == null){
			return BenefitListOrderByEnum.UNKNOWN;
		} else {
			BenefitListOrderByEnum orderby;
			try {
				orderby = valueOf(value.toUpperCase());
			} catch (IllegalArgumentException exception) {
				return BenefitListOrderByEnum.UNKNOWN;
			}
			return orderby;
		}
	}
}
