package net.fluance.cockpit.app.web.util.note;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import net.fluance.app.security.service.Person;
import net.fluance.cockpit.app.web.util.patient.PatientMock;
import net.fluance.cockpit.core.model.jdbc.note.NoteDetails;
import net.fluance.cockpit.core.model.jpa.note.Note;

public class NoteMock {
	
	private NoteMock() {}
	
	public static Note generateNote() {
		Note note = new Note();

		note.setId(1L);
		note.setCategoryId(2);
		note.setPatientId(10000L);
		note.setVisitNb(100L);
		note.setTitle("Foo note");
		note.setDescription("Foo description");
		note.setDeleted(false);
		note.setShift(false);
		
		LocalDateTime.now();		
		note.setReferenceDate(Date.from(LocalDateTime.now().minusDays(5).atZone(ZoneId.systemDefault()).toInstant()));		
		note.setEditedDate(Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant()));
		
		note.setCreator("ADMIN/foo");
		note.setEditor("ADMIN/foo");
		
		return note;
	}
	
	public static NoteDetails generateNoteDetails() {
		NoteDetails noteDetails = new NoteDetails();
		
		noteDetails.setId(1L);		
		noteDetails.setVisitNb(100L);
		noteDetails.setTitle("Foo note");
		noteDetails.setDescription("Foo description");
		noteDetails.setDeleted(false);
		noteDetails.setShift(false);
		
		LocalDateTime.now();		
		noteDetails.setReferenceDate(Date.from(LocalDateTime.now().minusDays(5).atZone(ZoneId.systemDefault()).toInstant()));		
		noteDetails.setEditedDate(Date.from(LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant()));
		
		noteDetails.setCategory(NoteCategoryMock.getNoteCategory());		
		noteDetails.setPatient(PatientMock.getPatientReference());
		
		noteDetails.setCreator(new Person("foo"));
		noteDetails.setEditor(new Person("foo"));
		
		return noteDetails;
	}
}
