package net.fluance.cockpit.app.service.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.app.web.controller.DoctorController;
import net.fluance.cockpit.core.model.DoctorDataSource;
import net.fluance.cockpit.core.model.DoctorReference;
import net.fluance.cockpit.core.model.jdbc.physician.Physician;
import net.fluance.cockpit.core.model.jdbc.resourcepersonnel.ResourcePersonnel;
import net.fluance.cockpit.core.repository.jdbc.doctor.DoctorRepository;
import net.fluance.cockpit.core.repository.jdbc.physician.PhysicianRepository;
import net.fluance.cockpit.core.repository.jdbc.resourcepersonnel.ResourcePersonnelRepository;

@Service
public class DoctorService {
	private static final Logger LOGGER = LogManager.getLogger(DoctorController.class);

	@Autowired
	DoctorRepository doctorRepository;

	@Autowired
	PhysicianRepository physicianRepository;

	@Autowired
	ResourcePersonnelRepository resourcePersonnelRepository;

	public void deleteAssignement(Long doctorId, Long patientId) {
		if (Objects.isNull(doctorId)) {
			deletePatientFromMyself(patientId);
		} else {
			// TODO: delete
		}

	}

	private void deletePatientFromMyself(Long patientId) {
		// TODO: delete
	}

	public void addAssignement(Long doctorId, Long patientId) {
		if (Objects.isNull(doctorId)) {
			addPatientFromMyself(patientId);
		} else {
			// TODO: add
		}
	}

	private void addPatientFromMyself(Long patientId) {
		// TODO: add
	}

	/**
	 * The value which is in the field we have to search both in firstName, lastName
	 * and speciality
	 * 
	 * @param field
	 * @return
	 */
	public List<DoctorReference> getDoctorsFilterByField(String field, Integer limit, Integer offset){
		List<DoctorReference> doctors = new ArrayList<DoctorReference>();
		
		//First SQL Query to obtain the Physicians
		List<Physician> physicians = physicianRepository.getDoctorsFilterByField(field, limit, offset);
		if(physicians != null) {
			for (Physician physician : physicians) {
				DoctorReference doctor = physicianToDoctorReference(physician);
				doctors.add(doctor);
			}
		}
		
		//Second SQL Query to obtain the ResourcePersonnel
		List<ResourcePersonnel> resourcePersonnels = resourcePersonnelRepository.getDoctorsFilterByField(field, limit, offset);
		if(resourcePersonnels != null) {
			for (ResourcePersonnel resourcePersonnel : resourcePersonnels) {
				DoctorReference doctor = resourcePersonnelToDoctorReference(resourcePersonnel);
				doctors.add(doctor);
			}
		}
		
		return doctors;
	}
	
	/**
	 * Returns a {@link List} of {@link DoctorReference} which match with the param different to null
	 * @param companyId
	 * @param firstName
	 * @param lastName
	 * @param speciality
	 * @return
	 */
	public List<DoctorReference> getDoctorsFilterByField(Long companyId, String firstName, String lastName, String speciality, Integer limit, Integer offset) {
		
		List<DoctorReference> doctors = new ArrayList<DoctorReference>();
		
		//First SQL Query to obtain the Physicians
		List<Physician> physicians = physicianRepository.getDoctorsFilterByField(companyId, firstName, lastName, speciality, limit, offset);
		
		if(physicians != null) {
			for (Physician physician : physicians) {
				DoctorReference doctor = physicianToDoctorReference(physician);
				doctors.add(doctor);
			}
		}
		
		//Second SQL Query to obtain the ResourcePersonnel
		List<ResourcePersonnel> resourcePersonnels = resourcePersonnelRepository.getDoctorsFilterByField(companyId, firstName, lastName, speciality, limit, offset);
		if(resourcePersonnels != null) {
			for (ResourcePersonnel resourcePersonnel : resourcePersonnels) {
				DoctorReference doctor = resourcePersonnelToDoctorReference(resourcePersonnel);
				doctors.add(doctor);
			}
		}

		return doctors;
	}

	public List<DoctorReference> getDoctorsById(DoctorDataSource doctorDataSource, Long companyId, Long staffId) {
		List<DoctorReference> doctors = new ArrayList<DoctorReference>();
		switch (doctorDataSource) {
		case ResourcePersonnel:
			LOGGER.debug("Getting Data from resourcepersonnel");
			List<ResourcePersonnel> resourcePersonnels = physicianRepository.getDoctorsByIdFromFResourcePersonell(companyId, staffId.toString());
			if(resourcePersonnels != null) {
				for (ResourcePersonnel resourcePersonnel : resourcePersonnels) {
					DoctorReference doctor = resourcePersonnelToDoctorReference(resourcePersonnel);
					doctors.add(doctor);
				}
			}
			return doctors;
		case Physician:
			LOGGER.debug("Getting Data from physician");
			List<Physician> physicians = physicianRepository.getDoctorsByIdFromPhysician(companyId, staffId);
			if(physicians != null) {
				for (Physician physician : physicians) {
					DoctorReference doctor = physicianToDoctorReference(physician);
					doctors.add(doctor);
				}
			}
			return doctors;
		default :
			throw new IllegalArgumentException("Not supported source :" + doctorDataSource.name());
		}
	}

	private DoctorReference resourcePersonnelToDoctorReference(ResourcePersonnel resourcePersonnel) {
		DoctorReference doctor = new DoctorReference();
		doctor.setId(DoctorDataSource.ResourcePersonnel.getValue() + "/" + resourcePersonnel.getCompany_id() + "/" + resourcePersonnel.getStaffid());
		doctor.setFirstName(resourcePersonnel.getName());
		doctor.setInternalPhoneNumber(resourcePersonnel.getInternalphone());
		doctor.setExternalPhoneNumberOne(resourcePersonnel.getPrivatephone());
		doctor.setExternalPhoneNumbertwo(resourcePersonnel.getAltphone());
		doctor.setSpeciality(resourcePersonnel.getRole());
		return doctor;
	}
	
	private DoctorReference physicianToDoctorReference(Physician physician) {
		DoctorReference doctor = new DoctorReference();
		doctor.setId(DoctorDataSource.Physician.getValue() + "/" + physician.getCompanyId() + "/" + physician.getStaffId());
		doctor.setFirstName(physician.getFirstname());
		doctor.setLastName(physician.getLastname());
		doctor.setSpeciality(physician.getPhySpecialityDesc());
		return doctor;
	}
}

