/**
 * 
 */
package net.fluance.cockpit.core.model.jdbc.patient;


public enum PatientViewEnum {
	DAY("day"),WEEK("week");
	private String key;
	private PatientViewEnum(String key){
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
    
}
