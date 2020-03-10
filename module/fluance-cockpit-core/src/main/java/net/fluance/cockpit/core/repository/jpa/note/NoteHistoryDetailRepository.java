package net.fluance.cockpit.core.repository.jpa.note;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import net.fluance.cockpit.core.model.jpa.note.NoteHistoryDetail;

@Repository
public interface NoteHistoryDetailRepository extends CrudRepository<NoteHistoryDetail, Long>{}
