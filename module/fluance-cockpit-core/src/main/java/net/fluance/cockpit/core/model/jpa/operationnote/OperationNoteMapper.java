package net.fluance.cockpit.core.model.jpa.operationnote;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.dto.appointment.operation.AppointmentOperationNoteDto;
import net.fluance.cockpit.core.model.dto.patient.ExerciseDto;
import net.fluance.cockpit.core.model.jpa.patient.Exercise;

@Service
public class OperationNoteMapper {

	public AppointmentOperationNoteDto toModel(OperationNote entity) {
		AppointmentOperationNoteDto model = new AppointmentOperationNoteDto();
		if (entity == null) {
			return null;
		}
		model.setAppointmentId(entity.getAppointmentId());
		model.setId(entity.getId());
		model.setNote(entity.getNote());
		return model;
	}	

	public OperationNote toEntity(AppointmentOperationNoteDto model) {
		OperationNote entity = new OperationNote();
		if (model == null) {
			return null;
		}
		entity.setAppointmentId(model.getAppointmentId());
		entity.setId(model.getId());
		entity.setNote(model.getNote());		
		return entity;
	}

	public Page<AppointmentOperationNoteDto> toModel(Page<OperationNote> entities) {
		if (entities == null) {
			return null;
		}
		return entities.map(this::toModel);
	}
	
	public List<AppointmentOperationNoteDto> toModel(List<OperationNote> entities) {
		List<AppointmentOperationNoteDto> models = new ArrayList<>();
		for (OperationNote entity : entities) {
			models.add(toModel(entity));
		}
		return models;
	}
}