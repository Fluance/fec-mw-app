/**
 * 
 */
package net.fluance.cockpit.core.model.wrap.patient;

import net.fluance.cockpit.core.model.SQLStatements;

public enum AdmissionStatusEnum {

	UNKNOWN("",""),
	PREADMITTED("preadmitted",SQLStatements.PREADMITTED_PATIENTS_CRITERIA),
	PREADMISSION("preadmission",SQLStatements.PREADMITTED_PATIENTS_CRITERIA),
	CURRENTADMISSION("currentadmission",SQLStatements.INHOUSE_PATIENTS_CRITERIA);

	private String value;
	private String sql;

	private AdmissionStatusEnum(String value, String sql) {
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
	public static AdmissionStatusEnum permissiveValueOf(String value){
		if (value == null){
			return AdmissionStatusEnum.UNKNOWN;
		} else {
			AdmissionStatusEnum admissionStatus = valueOf(value.toUpperCase());
			if (admissionStatus == null){
				return AdmissionStatusEnum.UNKNOWN;
			}
			return admissionStatus;
		}
	}
}
