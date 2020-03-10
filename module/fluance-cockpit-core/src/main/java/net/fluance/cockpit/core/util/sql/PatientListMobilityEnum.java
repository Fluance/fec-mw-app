package net.fluance.cockpit.core.util.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum PatientListMobilityEnum {
	UNKNOWN(""),
	SS("s/s"),
	MH("m/H"),
	WH("w/H");
	
	private String value;

	private PatientListMobilityEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static PatientListMobilityEnum permissiveValueOf(String value){
		if (value == null){
			return PatientListMobilityEnum.UNKNOWN;
		} else {
			PatientListMobilityEnum orderby = valueOf(value.toUpperCase().replaceAll("/", ""));
			if (orderby == null){
				return PatientListMobilityEnum.UNKNOWN;
			}
			return orderby;
		}
	}
	
	public static List<String> getValues(){
		return new ArrayList<String>(Arrays.asList(PatientListMobilityEnum.UNKNOWN.value, PatientListMobilityEnum.SS.value, PatientListMobilityEnum.MH.value, PatientListMobilityEnum.WH.value));
	}
}
