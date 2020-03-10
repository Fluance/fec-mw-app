package net.fluance.cockpit.core.model.jdbc.company;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
public class RoomList implements Persistable<Integer> {

	@JsonProperty("patientRoom")
	private String patientroom;
	@JsonProperty("nbPatients")
	private Integer nb_patients;

	public RoomList(String patientroom, Integer nb_patients) {
		this.nb_patients = nb_patients;
		this.patientroom = patientroom;
	}

	public Integer getNb_patients() {
		return nb_patients;
	}

	public void setNb_patients(Integer nb_patients) {
		this.nb_patients = nb_patients;
	}

	public RoomList(String patientroom) {
		this.patientroom = patientroom;
	}

	public RoomList() {}

	public String getPatientroom() {
		return patientroom;
	}

	public void setPatientroom(String patientroom) {
		this.patientroom = patientroom;
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
