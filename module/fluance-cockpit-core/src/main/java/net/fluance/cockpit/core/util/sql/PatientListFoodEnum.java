package net.fluance.cockpit.core.util.sql;

import java.util.ArrayList;
import java.util.List;

public enum PatientListFoodEnum {
	UNKNOWN(""), NÜ("nü"), WK("WK"), DD("DD");

	private String value;

	private PatientListFoodEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static PatientListFoodEnum permissiveValueOf(String value) {
		if (value == null) {
			return PatientListFoodEnum.UNKNOWN;
		} else {
			PatientListFoodEnum orderby;
			try {
				orderby = valueOf(value.toUpperCase());
			} catch (IllegalArgumentException exception) {
				return PatientListFoodEnum.UNKNOWN;
			} 
			return orderby;
		}
	}

	public static List<String> getValues() {
		List<String> values = new ArrayList<>();
		for (PatientListFoodEnum patientListFoodEnum : PatientListFoodEnum.values()) {
			values.add(patientListFoodEnum.value);
		}
		return values;
	}
}
