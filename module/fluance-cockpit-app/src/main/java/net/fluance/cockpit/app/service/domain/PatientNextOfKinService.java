package net.fluance.cockpit.app.service.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.dto.PatientNextOfKinDto;
import net.fluance.cockpit.core.model.jdbc.patient.PatientNextOfKin;
import net.fluance.cockpit.core.model.mapper.PatientNextOfKinMapper;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientNextOfKinRepository;

@Service
public class PatientNextOfKinService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PatientNextOfKinService.class);

	@Autowired
	PatientNextOfKinRepository patientNextOfKinRepository;
	
	/**
	 * <ol>
	 * <li> Recover a {@code List} of {@code PatientNextOfKin} which belong to the Patient ID</li>
	 * <li> The {@code PatientNextOfKin}'s are grouped in a {@code PatientNextOfKinDto} if the Contact ID is the same</li>
	 * <li> The email's and telephone's are grouped in list's</li>
	 * <li> Returns the {@code PatientNextOfKinDto} {@code Lists}</li>
	 * </ol> 
	 * @param patientId The Patient ID
	 * @return The List of Kins of a the Patient
	 */
	public List<PatientNextOfKinDto> getNoksByPid(Long patientId) {
		
		LOGGER.debug("Getting Kins by Patient ID " + patientId + " ...");
		
		List<PatientNextOfKinDto> patientKinsDto = new ArrayList<>();
		
		List<PatientNextOfKin> patientKins = patientNextOfKinRepository.findByPid(patientId);
		
		for(PatientNextOfKin patientNextOfKin : patientKins) {
			
			Optional<PatientNextOfKinDto> kin = patientKinsDto.stream().filter(
					patientKinDto -> patientNextOfKin.getId().equals(patientKinDto.getId())
			).findFirst();
			
			if(!kin.isPresent()) {
				PatientNextOfKinDto patientNextOfKinDto = PatientNextOfKinMapper.toModel(patientNextOfKin);
				patientKinsDto.add(patientNextOfKinDto);
			}
			else {
				if(patientNextOfKin.getEquipment().equals("internet_address")) {
					kin.get().getEmails().add(patientNextOfKin.getData());
				}
				if(patientNextOfKin.getEquipment().equals("telephone") || patientNextOfKin.getEquipment().equals("cellular_phone")) {
					kin.get().getTelephones().add(patientNextOfKin.getData());
				}
			}
		}
		
		LOGGER.debug("Got Kins by Patient ID " + patientId + " ...");
		
		return patientKinsDto;
	}
}
