package net.fluance.cockpit.core.model.jdbc.patient;

import org.springframework.data.domain.Persistable;

public class PatientsCount implements Persistable<String> {

	private static final long serialVersionUID = 1L;

	private Integer nb_records;
	private String substr;
	
	public PatientsCount(Integer nb_records, String substr) {
		super();
		this.nb_records = nb_records;
		this.substr = substr;
	}
	
	public Integer getNb_records() {
		return nb_records;
	}
	
	public void setNb_records(Integer nb_records) {
		this.nb_records = nb_records;
	}
	
	public String getSubstr() {
		return substr;
	}
	
	public void setSubstr(String substr) {
		this.substr = substr;
	}

	@Override
	public String getId() {
		return substr;
	}

	@Override
	public boolean isNew() {
		return substr == null;
	}
}
