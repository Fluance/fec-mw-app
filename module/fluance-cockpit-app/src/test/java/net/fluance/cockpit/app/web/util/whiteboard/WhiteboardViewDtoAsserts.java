package net.fluance.cockpit.app.web.util.whiteboard;


public class WhiteboardViewDtoAsserts {
	public WhiteboardViewDtoAsserts(String room, String patientBed, Long patientId, String insurance) {
		this.room = room;
		this.patientBed = patientBed;
		this.patientId = patientId;
		this.insurance = insurance;
	}
			
	private String room;
	private String patientBed;
	private Long patientId;
	private String insurance;
	
	public String getRoom() {
		return room;
	}
	
	public String getPatientBed() {
		return patientBed;
	}
	
	public Long getPatientId() {
		return patientId;
	}
	
	public String getInsurance() {
		return insurance;
	}
	
	public String getKey(){
		return room+"."+patientBed;
	}
}
