package net.fluance.cockpit.core.util.patient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import net.fluance.cockpit.core.model.dto.patient.NoteDto;
import net.fluance.cockpit.core.model.jpa.patient.Note;

/**
 * Generates the Mock data for Note entity, the methods include mock Page data and a method for the NoteDto
 */
public class NoteMock {
	private NoteMock() {}
	
	public static Note getOneNote(Long id) {
		Note note = new Note();
		
		note.setId(id);
		note.setCreator("User creator " + id);
		note.setContent("Mock content " + id);
		note.setEditedDate(new Date());
		note.setReferenceDate(new Date());
		note.setEditor("User editor " + id);
		note.setPatientId(id + 10000);
		note.setShifted(false);
		note.setSource("MockSource");
		note.setTitle("Mock note " + id);
		
		return note;
	}
	
	public static List<Note> getNotes(int numberOfNotes) {
		List<Note> notes = new ArrayList<>();
		
		do {
			notes.add(getOneNote(new Long(numberOfNotes)));
			numberOfNotes--;
		}while(numberOfNotes > 0);
		
		return notes;
	}
	
	public static Page<Note> getNotesAsPage(int numberOfNotes) {
		Page<Note> page = new PageImpl<Note>(getNotes(numberOfNotes));
		
		return page;
	}
	
	public static Page<Note> getEmptyPage() {
		Page<Note> page = new PageImpl<Note>(new ArrayList<Note>());
		
		return page;
	}
	
	public static NoteDto getOneNoteDto(Long id) {
		NoteDto noteDto = new NoteDto();
		
		noteDto.setId(id);
		noteDto.setCreator("User creator " + id);
		noteDto.setContent("Mock content " + id);
		noteDto.setEditedDate(new Date());
		noteDto.setReferenceDate(new Date());
		noteDto.setEditor("User editor " + id);
		noteDto.setPatientId(id + 10000);
		noteDto.setShifted(false);
		noteDto.setSource("MockSource");
		noteDto.setTitle("Mock note " + id);
		
		return noteDto;
	}
}
