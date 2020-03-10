package net.fluance.cockpit.app.web.util.patient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import net.fluance.cockpit.core.model.dto.patient.NoteDto;

/**
 * Generates the Mock data for NoteDto, the methods include mock Page data
 */
public class NoteDtoMock {
	private NoteDtoMock() {}
	
	public static Page<NoteDto> getEmptyPage() {
		Page<NoteDto> page = new PageImpl<NoteDto>(new ArrayList<NoteDto>());
		
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
	
	public static List<NoteDto> getNotesDto(int numberOfNotes) {
		List<NoteDto> notes = new ArrayList<>();
		
		do {
			notes.add(getOneNoteDto(new Long(numberOfNotes)));
			numberOfNotes--;
		}while(numberOfNotes > 0);
		
		return notes;
	}
	
	public static Page<NoteDto> getNotesDtoAsPage(int numberOfNotes) {
		Page<NoteDto> page = new PageImpl<NoteDto>(getNotesDto(numberOfNotes));
		
		return page;
	}
}
