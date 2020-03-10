package net.fluance.cockpit.core.model.jdbc.patientfiles;

public enum PatientFilesOrderEnum {

	filename("filename"),
	id("id"),
	company_id("companyid"),
	creationdt("creationdate");

	private String value;

	private PatientFilesOrderEnum(String value) {
			this.value = value;
		}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static PatientFilesOrderEnum permissiveValueOf(String value) {
		PatientFilesOrderEnum[] orders = PatientFilesOrderEnum.values();
		for (int i = 0; i < orders.length; i++) {
			if (orders[i].getValue().equalsIgnoreCase(value))
				return orders[i];
		}
		return null;
	}

}
