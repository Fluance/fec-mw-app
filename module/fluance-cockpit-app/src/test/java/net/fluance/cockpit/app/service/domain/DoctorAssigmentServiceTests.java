package net.fluance.cockpit.app.service.domain;

import org.junit.Assert;
import org.junit.Test;

import net.fluance.cockpit.core.model.DoctorDataSource;
import net.fluance.cockpit.core.model.jpa.doctor.DoctorAssigmentEntity;

public class DoctorAssigmentServiceTests {
	@Test
	public void generateEntity_works() throws Exception {
		Long staffId = 34234L;
		Long patientId = 242352L;
		Long companyId = 2L;
		DoctorDataSource source = DoctorDataSource.Physician;

		DoctorAssigmentService doctorAssigmentService = new DoctorAssigmentService();
		DoctorAssigmentEntity doctorAssigmentEntity = doctorAssigmentService.generateEntity(staffId, patientId,
				source,companyId);

		Assert.assertTrue("Is staffId mapped?", doctorAssigmentEntity.getStaffId().equals(staffId));
		Assert.assertTrue("Is patientId mapped?", doctorAssigmentEntity.getPatientId().equals(patientId));
		Assert.assertTrue("Is source mapped?", doctorAssigmentEntity.getSource().equals(source.name()));
		Assert.assertTrue("Is companyId mapped?", doctorAssigmentEntity.getCompanyId().equals(companyId));
	}
}
