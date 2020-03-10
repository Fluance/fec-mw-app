package net.fluance.cockpit.app.service.domain.patient;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.dto.patient.ExerciseDto;
import net.fluance.cockpit.core.repository.jpa.patient.PatientExerciseDao;

@Service
public class PatientExerciseService {

	@Value("${net.fluance.cockpit.core.model.repository.defaultPageSize}")
	private Integer pageSize;
	
	@Autowired
	PatientExerciseDao patientExerciseDao;
	
	public Page<ExerciseDto> getExercisesForPatient(Long pid, LocalDate from, LocalDate to, Integer page) {
		if(page < 0) {
			page = 0;
		}
		PageRequest pageRequest = new PageRequest(page, pageSize);
		return patientExerciseDao.getExercisesForPatient(pid, from, to, pageRequest);
	}

	public ExerciseDto saveExerciseForPatient(Long patientId, ExerciseDto exerciseDto) {
		exerciseDto.setPatientId(validatePatientId(patientId));
		return patientExerciseDao.save(validateExerciseDto(exerciseDto));
	}
	
	public void delete(Long exerciseId) {
		patientExerciseDao.delete(exerciseId);
	}

	private Long validatePatientId(Long patientId) {
		if (patientId == null) {
			throw new IllegalArgumentException("patientId(pid) is mandatory");
		}
		return patientId;
	}

	private ExerciseDto validateExerciseDto(ExerciseDto exerciseDto) {
		if (StringUtils.isEmpty(exerciseDto.getSource()) || StringUtils.isEmpty(exerciseDto.getSourceId()) || exerciseDto.getExerciseDate() == null) {
			throw  new IllegalArgumentException("source, sourceId and ExcerciseDate are mandatory");
		}
		return exerciseDto;
	}

}
