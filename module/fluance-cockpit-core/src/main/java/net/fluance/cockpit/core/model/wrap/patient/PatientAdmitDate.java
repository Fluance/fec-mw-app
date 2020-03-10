package net.fluance.cockpit.core.model.wrap.patient;

/**
 * Wrap Class to return Admit Date and the Patients Quantity with a visit this Date
 *
 */
public class PatientAdmitDate {

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
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PatientAdmitDate)) {
			return false;
		}
		
		PatientAdmitDate pad = (PatientAdmitDate) obj;
		
		if(this.count==null){
			return false;
		}
		
		if(this.admitdt==null){
			return false;
		}
		
		if(this.count.equals(pad.getCount()) && this.admitdt.equals(pad.getAdmitdt())){
			return true;
		}
		return false;
	}

	
	

	
}
