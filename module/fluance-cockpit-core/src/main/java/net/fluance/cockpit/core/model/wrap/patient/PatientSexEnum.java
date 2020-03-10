package net.fluance.cockpit.core.model.wrap.patient;

import net.fluance.cockpit.core.model.SQLStatements;

public enum PatientSexEnum {
	UNKNOWN("",""),
	UNDETERMINED("undetermined",SQLStatements.UNDETERMINED_GENDER_PATIENTS_CRITERIA),
	MALE("male",SQLStatements.MALE_PATIENTS_CRITERIA),
	FEMALE("female",SQLStatements.FEMALE_PATIENTS_CRITERIA),
	U("U",SQLStatements.UNDETERMINED_GENDER_PATIENTS_CRITERIA),
	M("M",SQLStatements.MALE_PATIENTS_CRITERIA),
	F("F",SQLStatements.FEMALE_PATIENTS_CRITERIA);

	private String value;
	private String sql;

	private PatientSexEnum(String value, String sql) {
		this.value = value;
		this.sql = sql;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * 
	 * @param value
	 * @return AdmissionStatusEnum corresponding to the parameter value, if not found returns AdmissionStatusEnum.UNKNOWN
	 */
	public static PatientSexEnum permissiveValueOf(String value){
		if (value == null || value.isEmpty()){
			return PatientSexEnum.UNKNOWN;
		} else {
			PatientSexEnum gender = valueOf(value.toUpperCase());
			if (gender == null){
				return PatientSexEnum.UNKNOWN;
			}
			return gender;
		}
	}

	public static String getCode(String value){
		if(value != null){
			switch(value){
				case "weiblich": case "Femminile": case "Féminin": 
					return permissiveValueOf("female").getValue();
				case "Masculin": case "männlich": case "Maschile":
					return permissiveValueOf("male").getValue();
			}
		}
		return PatientSexEnum.UNKNOWN.getValue();
	}
}
