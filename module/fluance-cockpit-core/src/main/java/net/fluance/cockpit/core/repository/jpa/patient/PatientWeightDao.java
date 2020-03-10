package net.fluance.cockpit.core.repository.jpa.patient;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.dto.patient.WeightDto;
import net.fluance.cockpit.core.model.jpa.patient.Weight;
import net.fluance.cockpit.core.model.jpa.patient.WeightMapper;

@Service
public class PatientWeightDao {

	@Autowired
	PatientWeightRepository patientWeightRepository;

	public Page<WeightDto> getWeightsForPatient(Long pid, LocalDate from, LocalDate to, PageRequest page) {
		
		Page<Weight> weights;
		
		if(from != null && to != null) {
			weights = patientWeightRepository.findByPatientIdFromTo(pid, Timestamp.valueOf(from.atStartOfDay()), Timestamp.valueOf(to.atTime(LocalTime.MAX)), page);
		}
		else if(from != null) {
			weights = patientWeightRepository.findByPatientIdFrom(pid, Timestamp.valueOf(from.atStartOfDay()), page);
		}
		else if(to != null) {
			weights = patientWeightRepository.findByPatientIdTo(pid, Timestamp.valueOf(to.atTime(LocalTime.MAX)), page);
		} 
		else {		
			weights = patientWeightRepository.findByPatientId(pid, page);
		}
		
		return WeightMapper.toModel(weights);
	}

	public WeightDto save(WeightDto weight) {
		return WeightMapper.toModel(patientWeightRepository.save(WeightMapper.toEntity(weight)));
	}
	
	public void delete(Long weightId) {
		patientWeightRepository.delete(weightId);
	}
}
