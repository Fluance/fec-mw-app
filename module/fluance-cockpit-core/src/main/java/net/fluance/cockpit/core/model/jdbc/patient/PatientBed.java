package net.fluance.cockpit.core.model.jdbc.patient;

public enum PatientBed {

	WINDOW_BED("window_bed"),
	DOOR_BED("door_bed");

	private String value;

	private PatientBed(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

}
