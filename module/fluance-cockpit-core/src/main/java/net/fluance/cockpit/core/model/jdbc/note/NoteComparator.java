package net.fluance.cockpit.core.model.jdbc.note;

import java.util.Comparator;

public class NoteComparator implements Comparator<NoteDetails>{

	@Override
	public int compare(NoteDetails note1, NoteDetails note2) {
		return note1.getCategory().getPriority() - note2.getCategory().getPriority();
	}

}
