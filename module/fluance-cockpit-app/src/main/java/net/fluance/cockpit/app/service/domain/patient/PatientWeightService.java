package net.fluance.cockpit.app.service.domain.patient;

import java.time.LocalDate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.dto.patient.WeightDto;
import net.fluance.cockpit.core.repository.jpa.patient.PatientWeightDao;

@Service
public class PatientWeightService {
	
	@Value("${net.fluance.cockpit.core.model.repository.defaultPageSize}")
	private Integer pageSize;
	
	@Autowired
	PatientWeightDao patientWeightDao;

	public Page<WeightDto> getWeightsForPatient(Long pid, LocalDate from, LocalDate to, Integer page) {
		if(page < 0) {
			page = 0;
		}
		PageRequest pageRequest = new PageRequest(page, pageSize);
		return patientWeightDao.getWeightsForPatient(pid, from, to, pageRequest);
	}

	public WeightDto saveWeightForPatient(Long pid, WeightDto weight) {
		weight.setPatientId(validatePatientId(pid));
		return patientWeightDao.save(validateWeightDto(weight));
	}
	
	public void delete(Long weightId) {
		patientWeightDao.delete(weightId);
	}

	private Long validatePatientId(Long patientId) {
		if (patientId == null) {
			throw new IllegalArgumentException("patientId(pid) is mandatory");
		}
		return patientId;
	}

	private WeightDto validateWeightDto(WeightDto weightDto) {
		if (StringUtils.isEmpty(weightDto.getSource()) || StringUtils.isEmpty(weightDto.getSourceId()) || weightDto.getWeightDate() == null) {
			throw  new IllegalArgumentException("source, sourceId and WeightDate are mandatory");
		}
		return weightDto;
	}
}
