package net.fluance.cockpit.core.util.sql;

public enum NoteListOrderByEnum {
	UNKNOWN(""),
	REFERENCEDT("date"),
	EDITOR("editor"),
	CREATOR("creator"),
	EDITEDDT("editeddate"),
	TITLE("title"),
	CATEGORYID("category");
	
	private final String value;

	
	private NoteListOrderByEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public static NoteListOrderByEnum permissiveValueOf(String valueToCheck){
		
		for ( NoteListOrderByEnum enumeration : NoteListOrderByEnum.values() ) {
			if (enumeration.getValue().equalsIgnoreCase(valueToCheck)) {
				return enumeration;
			}
		}
		return null;
	}
}
