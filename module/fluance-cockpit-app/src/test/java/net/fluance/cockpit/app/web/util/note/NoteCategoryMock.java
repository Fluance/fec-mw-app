package net.fluance.cockpit.app.web.util.note;

import net.fluance.cockpit.core.model.jdbc.note.NoteCategory;

public class NoteCategoryMock {
	private NoteCategoryMock() {}
	
	public static NoteCategory getNoteCategory() {
		NoteCategory noteCategory = new NoteCategory();
		
		noteCategory.setId(1);
		noteCategory.setName("Note category");
		noteCategory.setPriority(1);
		
		return noteCategory;
	}

}
