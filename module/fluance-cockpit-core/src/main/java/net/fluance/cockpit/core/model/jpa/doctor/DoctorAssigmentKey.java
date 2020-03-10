package net.fluance.cockpit.core.model.jpa.doctor;

import java.io.Serializable;

public class DoctorAssigmentKey implements Serializable{
	private Long patientId;
	private String source;
	private Long companyId;
	private Long staffId;
}
