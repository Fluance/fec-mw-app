package net.fluance.cockpit.core.repository.jpa.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.dto.patient.NoteDto;
import net.fluance.cockpit.core.model.jpa.patient.NoteMapper;

@Service
public class PatientNoteDao {

	@Autowired
	PatientNoteRepository patientNoteRepository;
	
	@Autowired
	NoteMapper noteMapper;

	public Page<NoteDto> getNotes(PageRequest pageRequest) {		
		return noteMapper.toModel(patientNoteRepository.findAll(pageRequest));
	}

	public NoteDto save(NoteDto note) {
		return noteMapper.toModel(patientNoteRepository.save(noteMapper.toEntity(note)));
	}

	public NoteDto findById(Long id) {
		return noteMapper.toModel(patientNoteRepository.findOne(id));
	}

	public void delete(Long id) {
		patientNoteRepository.delete(id);
	}
}
