package net.fluance.cockpit.app.service.domain.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;
import net.fluance.cockpit.core.model.dto.patient.NoteDto;
import net.fluance.cockpit.core.repository.jpa.patient.PatientNoteDao;
import net.fluance.cockpit.core.util.PageUtils;

@Service
public class PatientNoteService {
	
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;

	@Autowired
	PatientNoteDao patientNoteDao;

	public Page<NoteDto> getNotes(int page) {
		if(page < 0) {
			throw new IllegalArgumentException("Page value not valid");
		}
		
		PageRequest pageRequest = PageUtils.getPage(page, defaultResultLimit);
		return patientNoteDao.getNotes(pageRequest);
	}

	public NoteDto saveNote(NoteDto note) {
		//a note is never shifted from beginning
		note.setShifted(false);
		return patientNoteDao.save(note);
	}

	public NoteDto getNoteById(Long id) throws NotFoundException {
		if(id == null) {
			throw new IllegalArgumentException("id can't be null");		
		}
		
		NoteDto noteDto = patientNoteDao.findById(id);
		
		if(noteDto == null) {
			throw new NotFoundException("Note not found");
		}
		
		return patientNoteDao.findById(id);
	}

	public NoteDto updateNote(Long id, NoteDto note) {		
		if(id == null) {
			throw new IllegalArgumentException("id can't be null");		
		} else {			
			if(!id.equals(note.getId())) {
				throw new IllegalArgumentException("Invalid id");	
			}	
		}
		return patientNoteDao.save(note);
	}

	public void deleteNote(Long id) {
		if(id == null) {
			throw new IllegalArgumentException("id can't be null");
		}
		patientNoteDao.delete(id);
	}
}
