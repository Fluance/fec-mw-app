package net.fluance.cockpit.core.model.jdbc.company;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
public class BedList implements Persistable<Integer>{

	@JsonProperty("patientBed")
	private Integer patientbed;

	public BedList(Integer patientbed) {
		this.patientbed = patientbed;
	}
	
	public BedList(){}

	public Integer getPatientbed() {
		return patientbed;
	}

	public void setPatientbed(int patientbed) {
		this.patientbed = patientbed;
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
