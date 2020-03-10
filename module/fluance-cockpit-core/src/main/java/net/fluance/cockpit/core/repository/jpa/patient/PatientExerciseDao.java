package net.fluance.cockpit.core.repository.jpa.patient;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.dto.patient.ExerciseDto;
import net.fluance.cockpit.core.model.jpa.patient.Exercise;
import net.fluance.cockpit.core.model.jpa.patient.ExerciseMapper;

@Service
public class PatientExerciseDao {

	@Autowired
	PatientExerciseRepository patientExerciseRepository;
	
	public Page<ExerciseDto> getExercisesForPatient(Long pid, LocalDate from, LocalDate to, PageRequest page) {
		
		Page<Exercise> exercises;
		
		if(from != null && to != null) {
			exercises = patientExerciseRepository.findByPatientIdFromTo(pid, Timestamp.valueOf(from.atStartOfDay()), Timestamp.valueOf(to.atTime(LocalTime.MAX)), page);
		}
		else if(from != null) {
			exercises = patientExerciseRepository.findByPatientIdFrom(pid, Timestamp.valueOf(from.atStartOfDay()), page);
		}
		else if(to != null) {
			exercises = patientExerciseRepository.findByPatientIdTo(pid, Timestamp.valueOf(to.atTime(LocalTime.MAX)), page);
		} 
		else {		
			exercises = patientExerciseRepository.findByPatientId(pid, page);
		}
		
		return ExerciseMapper.toModel(exercises);
	}

	public ExerciseDto save(ExerciseDto exercise) {
		return ExerciseMapper.toModel(patientExerciseRepository.save(ExerciseMapper.toEntity(exercise)));
	}

	public void delete(Long exerciseId) {
		patientExerciseRepository.delete(exerciseId);
	}
}
