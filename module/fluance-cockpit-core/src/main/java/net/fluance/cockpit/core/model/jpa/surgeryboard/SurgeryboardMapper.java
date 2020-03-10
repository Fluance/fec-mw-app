package net.fluance.cockpit.core.model.jpa.surgeryboard;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.dto.surgeryboard.SurgeryboardDto;

@Service
public class SurgeryboardMapper {

	public SurgeryboardDto toModel(Surgeryboard entity) {
		SurgeryboardDto model = new SurgeryboardDto();
		if (entity == null) {
			return null;
		}
		model.setCompanyId(entity.getId().getCompanyId());
		model.setNoteDate(entity.getId().getNoteDate());
		model.setNote(entity.getNote());
		return model;
	}	

	public Surgeryboard toEntity(SurgeryboardDto model) {
		Surgeryboard entity = new Surgeryboard();
		if (model == null) {
			return null;
		}
		entity.getId().setCompanyId(model.getCompanyId());
		entity.getId().setNoteDate(model.getNoteDate());
		entity.setNote(model.getNote());		
		return entity;
	}

	public Page<SurgeryboardDto> toModel(Page<Surgeryboard> entities) {
		if (entities == null) {
			return null;
		}
		return entities.map(this::toModel);
	}
}
