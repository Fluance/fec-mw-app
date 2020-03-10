package net.fluance.cockpit.core.model;

import java.io.Serializable;

public class VisitReference implements Serializable{
	private Long visitNb;
	private String patientUnit;
	private String hospService;
	private String patientRoom;

	public VisitReference(Long visitNb, String patientUnit, String hospService, String patientRoom) {
		super();
		this.visitNb = visitNb;
		this.patientUnit = patientUnit;
		this.hospService = hospService;
		this.patientRoom = patientRoom;
	}

	public Long getVisitNb() {
		return visitNb;
	}

	public void setVisitNb(Long visitNb) {
		this.visitNb = visitNb;
	}

	public String getPatientUnit() {
		return patientUnit;
	}

	public void setPatientUnit(String patientUnit) {
		this.patientUnit = patientUnit;
	}

	public String getHospService() {
		return hospService;
	}

	public void setHospService(String hospService) {
		this.hospService = hospService;
	}

	public String getPatientRoom() {
		return patientRoom;
	}

	public void setPatientRoom(String patientRoom) {
		this.patientRoom = patientRoom;
	}
}
