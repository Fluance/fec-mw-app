package net.fluance.cockpit.core.model.wrap.patient;

/**
 * Wrap created to can map the Patients Count endpoint
 *
 *
 */
public class PatientsCount {

	private Integer count;
	private String lastName;

	public PatientsCount(Integer nb_records, String substr) {
		super();
		this.count = nb_records;
		this.lastName = substr;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
