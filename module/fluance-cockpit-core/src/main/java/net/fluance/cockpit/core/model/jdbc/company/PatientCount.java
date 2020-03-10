package net.fluance.cockpit.core.model.jdbc.company;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PatientCount implements Persistable<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer patientCount;
	private String patientRoom;

	
	public PatientCount(Integer patientCount, String patientRoom) {
		this.patientCount = patientCount;
		this.patientRoom = patientRoom;
	}
	
	

	public Integer getPatientCount() {
		return patientCount;
	}



	public void setPatientCount(Integer patientCount) {
		this.patientCount = patientCount;
	}



	public String getPatientRoom() {
		return patientRoom;
	}



	public void setPatientRoom(String patientRoom) {
		this.patientRoom = patientRoom;
	}



	@JsonIgnore
	@Override
	public Integer getId() {
		return 0;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}

}
