package net.fluance.cockpit.core.repository.jpa.note;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.fluance.cockpit.core.model.jpa.note.Note;

@Repository
public interface NoteRepository extends CrudRepository<Note, Long> {

}