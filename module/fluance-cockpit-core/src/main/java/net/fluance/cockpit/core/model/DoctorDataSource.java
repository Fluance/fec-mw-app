package net.fluance.cockpit.core.model;

/**
 * Enum for the DoctorService Types
 *
 */
public enum DoctorDataSource {

	Physician("Physician"), 
	ResourcePersonnel("ResourcePersonnel");

	private String value;

	private DoctorDataSource(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
