package net.fluance.cockpit.core.model.jdbc.patient;

import org.springframework.data.domain.Persistable;

/**
 * Mapping Class for the Query PATIENTS_COUNT_BY_ADMIT_DATE_BASE
 *
 */
public class PatientAdmitDate implements Persistable<String> {

	private static final long serialVersionUID = 1L;

	private Integer count;
	private String admitdt;
	
	public PatientAdmitDate() {
		super();
	}

	public PatientAdmitDate(Integer count, String admitdt) {
		super();
		this.count = count;
		this.admitdt = admitdt;
	}
	
	public Integer getCount() {
		return count;
	}

	
	public void setCount(Integer count) {
		this.count = count;
	}

	
	public String getAdmitdt() {
		return admitdt;
	}

	
	public void setAdmitdt(String admitdt) {
		this.admitdt = admitdt;
	}

	@Override
	public String getId() {
		return admitdt;
	}

	@Override
	public boolean isNew() {
		return admitdt == null;
	}
}
