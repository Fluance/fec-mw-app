package net.fluance.cockpit.core.model.jpa.doctor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = DoctorAssigmentMapper.TABLE_NAME)
@IdClass(DoctorAssigmentKey.class)
public class DoctorAssigmentEntity implements Serializable {

	private static final long serialVersionUID = 7173911046373676178L;

	@Id
	@Column(name = DoctorAssigmentMapper.STAFF_ID)
	private Long staffId;
	
	@Id
	@Column(name = DoctorAssigmentMapper.COMPANY_ID)
	private Long companyId;

	@Id
	@Column(name = DoctorAssigmentMapper.PATIENT_ID)
	private Long patientId;

	@Id
	@Column(name = DoctorAssigmentMapper.SOURCE) // ENUM => OPALE || POLYPOINT
	private String source;

	public Long getStaffId() {
		return staffId;
	}

	public void setStaffId(Long staffId2) {
		this.staffId = staffId2;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
