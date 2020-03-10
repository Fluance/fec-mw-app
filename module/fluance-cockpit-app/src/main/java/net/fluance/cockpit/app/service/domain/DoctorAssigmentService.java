package net.fluance.cockpit.app.service.domain;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.DoctorDataSource;
import net.fluance.cockpit.core.model.jpa.doctor.DoctorAssigmentEntity;
import net.fluance.cockpit.core.model.jpa.doctor.DoctorSource;
import net.fluance.cockpit.core.repository.jpa.doctor.DoctorAssigmentJpaRepository;

@Service
public class DoctorAssigmentService {
	
	private static final Logger LOGGER = LogManager.getLogger(DoctorAssigmentService.class);
	@Autowired
	DoctorAssigmentJpaRepository doctorAssigmentJpaRepository;

	public void deleteAssignement(Long doctorId, Long patientId, DoctorDataSource source, Long companyId) {
		DoctorAssigmentEntity doctorAssigment = getAssigment(doctorId, patientId, source,companyId);
		LOGGER.info("is null :"+(doctorAssigment==null));
		if (Objects.nonNull(doctorAssigment)) {
			doctorAssigmentJpaRepository.delete(doctorAssigment);
		}
	}

	public DoctorAssigmentEntity saveAssignement(Long doctorId, Long patientId, DoctorDataSource source, Long companyId) {
		return doctorAssigmentJpaRepository.save(generateEntity(doctorId, patientId, source,companyId));
	}

	private Long getMyDoctorId() {
		return 1L;
	}

	private DoctorAssigmentEntity getAssigment(Long doctorId, Long patientId, DoctorDataSource source, Long companyId) {
		if (Objects.isNull(doctorId)) {
			doctorId = getMyDoctorId();
		}
		return doctorAssigmentJpaRepository.findAssigment(doctorId, patientId, source.name(),companyId);
	}

	protected DoctorAssigmentEntity generateEntity(Long staffId, Long patientId, DoctorDataSource source, Long companyId) {
		DoctorAssigmentEntity doctorAssigmentEntity = new DoctorAssigmentEntity();
		doctorAssigmentEntity.setPatientId(patientId);
		doctorAssigmentEntity.setSource(source.name());
		doctorAssigmentEntity.setCompanyId(companyId);
		if (Objects.isNull(staffId)) {
			staffId = getMyDoctorId();
		}
		doctorAssigmentEntity.setStaffId(staffId);
		return doctorAssigmentEntity;
	}
}
