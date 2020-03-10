package net.fluance.cockpit.core.model.wrap.patient;

public enum PatientCourtesyEnum {
	UNKNOWN("I"),
	MONSIEUR("1"),
	MADAME("2"),
	MADEMOISELLE("3");

	private String value;

	private PatientCourtesyEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static String permissiveValueOf(String value){
		if (value == null || value.isEmpty()){
			return PatientSexEnum.UNKNOWN.getValue();
		}
		switch(value){
			case "M": case "Mr": case "Hr": return PatientCourtesyEnum.MONSIEUR.getValue();
			case "Mrs": case "Fr": case "Mme": return PatientCourtesyEnum.MADAME.getValue();
			case "Mle": return PatientCourtesyEnum.MADEMOISELLE.getValue();
		}
		return PatientCourtesyEnum.UNKNOWN.getValue();
	}
}