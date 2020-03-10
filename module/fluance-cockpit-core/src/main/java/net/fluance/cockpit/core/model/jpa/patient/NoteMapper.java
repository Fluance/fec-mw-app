package net.fluance.cockpit.core.model.jpa.patient;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.dto.patient.NoteDto;

@Service
public class NoteMapper {

	public NoteDto toModel(Note entity) {
		NoteDto model = new NoteDto();
		if (entity == null) {
			return null;
		}
		model.setContent(entity.getContent());
		model.setCreator(entity.getCreator());
		model.setPatientId(entity.getPatientId());
		model.setEditedDate(entity.getEditedDate());
		model.setEditor(entity.getEditor());
		model.setId(entity.getId());
		model.setReferenceDate(entity.getReferenceDate());
		model.setShifted(entity.getShifted());
		model.setTitle(entity.getTitle());
		model.setSource(entity.getSource());
		return model;
	}

	public NoteDto toModel(NoteHistory entity) {
		NoteDto model = new NoteDto();
		if (entity == null) {
			return null;
		}
		model.setContent(entity.getContent());
		model.setPatientId(entity.getPatientId());
		model.setCreator(entity.getCreator());
		model.setEditedDate(entity.getEditedDate());
		model.setEditor(entity.getEditor());
		model.setId(entity.getId());
		model.setReferenceDate(entity.getReferenceDate());
		model.setShifted(entity.getShifted());
		model.setTitle(entity.getTitle());
		model.setSource(entity.getSource());
		return model;
	}

	public Note toEntity(NoteDto model) {
		Note entity = new Note();
		if (model == null) {
			return null;
		}
		entity.setContent(model.getContent());
		entity.setCreator(model.getCreator());
		entity.setEditedDate(model.getEditedDate());
		entity.setEditor(model.getEditor());
		entity.setId(model.getId());
		entity.setPatientId(model.getPatientId());
		entity.setReferenceDate(model.getReferenceDate());
		entity.setShifted(model.getShifted());
		entity.setTitle(model.getTitle());
		entity.setSource(model.getSource());
		return entity;
	}

	public Page<NoteDto> toModel(Page<Note> entities) {
		if (entities == null) {
			return null;
		}
		return entities.map(this::toModel);
	}
}
