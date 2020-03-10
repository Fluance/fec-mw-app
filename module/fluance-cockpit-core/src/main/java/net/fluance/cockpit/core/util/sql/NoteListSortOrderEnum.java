package net.fluance.cockpit.core.util.sql;

public enum NoteListSortOrderEnum {
	UNKNOWN(""),
	ASC("asc"),
	DESC("desc");
	
	private final String value;

	private NoteListSortOrderEnum(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public static NoteListSortOrderEnum permissiveValueOf(String valueToCheck){
		
		for ( NoteListSortOrderEnum enumeration : NoteListSortOrderEnum.values() ) {
			if ( enumeration.getValue().equalsIgnoreCase(valueToCheck) ) { return enumeration; };
		}
		
		return null;
	}
}
