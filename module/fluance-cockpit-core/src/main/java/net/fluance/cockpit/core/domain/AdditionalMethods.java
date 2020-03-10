package net.fluance.cockpit.core.domain;

public enum AdditionalMethods {
	RESTORE("RESTORE"), SET_READ("SET_READ"), SET_UNREAD("SET_UNREAD"), REORDER_PICTURES("REORDER_PICTURES");

	private String value;

	private AdditionalMethods(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
