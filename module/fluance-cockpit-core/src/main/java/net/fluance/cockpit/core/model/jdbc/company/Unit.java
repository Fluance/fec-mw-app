package net.fluance.cockpit.core.model.jdbc.company;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
public class Unit implements Persistable<Integer>{

	@JsonProperty("patientUnit")
	private String patientunit;
	@JsonProperty("codeDesc")
	private String codedesc;
	@JsonProperty("nbPatients")
	private Integer nb_patients;

	public Unit(String patientunit, String codedesc) {
		this.patientunit = patientunit;
		this.codedesc = codedesc;
	}
	
	public Unit(String patientunit, String codedesc, Integer nb_patients) {
		this.patientunit = patientunit;
		this.codedesc = codedesc;
		this.nb_patients = nb_patients;
	}

	public Unit() {}

	public String getPatientunit() {
		return patientunit;
	}

	public void setPatientunit(String patientunit) {
		this.patientunit = patientunit;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String getCodedesc() {
		return codedesc;
	}

	public void setPatientunitdesc(String patientunitdesc) {
		this.codedesc = patientunitdesc;
	}
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public Integer getNb_patients() {
		return nb_patients;
	}

	public void setNb_patients(Integer nb_patients) {
		this.nb_patients = nb_patients;
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
