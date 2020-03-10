package net.fluance.cockpit.core.model.jdbc.company;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceList implements Persistable<Integer>{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("hospService")
	private String hospService;
	@JsonProperty("hospServiceDesc")
	private String hospServiceDesc;
	@JsonProperty("nbPatients")
	private Integer nb_patients;

	public ServiceList(String hospservice, String hospservicedesc) {
		this.hospService = hospservice;
		this.hospServiceDesc = hospservicedesc;
	}
	
	public ServiceList(String hospservice, String hospservicedesc, Integer nb_patients) {
		this.hospService = hospservice;
		this.hospServiceDesc = hospservicedesc;
		this.nb_patients = nb_patients;
	}

	public ServiceList() {}

	public String getHospService() {
		return hospService;
	}

	public void setHospservice(String hospService) {
		this.hospService = hospService;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String getHospServiceDesc() {
		return hospServiceDesc;
	}

	public void setHospservicedesc(String hospServiceDesc) {
		this.hospServiceDesc = hospServiceDesc;
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
