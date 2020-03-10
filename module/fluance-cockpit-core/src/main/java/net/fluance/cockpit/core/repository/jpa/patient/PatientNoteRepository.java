package net.fluance.cockpit.core.repository.jpa.patient;

import org.springframework.data.repository.PagingAndSortingRepository;

import net.fluance.cockpit.core.model.jpa.patient.Note;

public interface PatientNoteRepository extends PagingAndSortingRepository<Note, Long> {
	
}
