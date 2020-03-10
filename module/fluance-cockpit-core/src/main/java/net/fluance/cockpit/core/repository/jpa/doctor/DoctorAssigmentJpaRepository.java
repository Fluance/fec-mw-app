package net.fluance.cockpit.core.repository.jpa.doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.fluance.cockpit.core.model.jpa.doctor.DoctorAssigmentEntity;
import net.fluance.cockpit.core.model.jpa.doctor.DoctorAssigmentMapper;

public interface DoctorAssigmentJpaRepository extends JpaRepository<DoctorAssigmentEntity, Long> {

	@Query(nativeQuery=true,value="SELECT * FROM " + DoctorAssigmentMapper.TABLE_NAME + " WHERE " + DoctorAssigmentMapper.STAFF_ID
			+ "=?1 AND " + DoctorAssigmentMapper.PATIENT_ID + "=?2 AND " + DoctorAssigmentMapper.SOURCE + "=?3 AND "+DoctorAssigmentMapper.COMPANY_ID+"=?4")
	DoctorAssigmentEntity findAssigment(Long doctorId, Long patientId, String source, Long companyId);

}
