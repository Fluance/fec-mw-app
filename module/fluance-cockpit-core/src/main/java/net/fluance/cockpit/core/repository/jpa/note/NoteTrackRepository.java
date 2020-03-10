package net.fluance.cockpit.core.repository.jpa.note;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.fluance.cockpit.core.model.jpa.note.NoteTrack;

@Repository
public interface NoteTrackRepository extends CrudRepository<NoteTrack, ProfileNoteTrackPK>{

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO notesandpictures.profile_note_track (username, note_id, readdt) VALUES (?1, ?2, NOW())", nativeQuery = true)
	public Integer markNoteAsRead(String username, Long noteId);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM notesandpictures.profile_note_track p where p.username=?1 and p.note_id=?2", nativeQuery = true)
	public Integer markNoteAsUnRead(String username, Long noteId);
	
	@Query(value = "SELECT * FROM notesandpictures.profile_note_track p where p.username=?1 and p.note_id=?2", nativeQuery = true)
	public NoteTrack findByUsernameAndNoteId(String username, Long noteId);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM notesandpictures.profile_note_track p WHERE p.note_id=?1", nativeQuery = true)
	public Integer markNoteAsUnReadForAllUsers(Long noteId);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM notesandpictures.profile_note_track p WHERE p.note_id=?1 and p.username<>?2", nativeQuery = true)
	public Integer markNoteAsUnReadForOtherUsers(Long noteId, String excludedUsername);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM notesandpictures.profile_note_track p WHERE p.note_id=?1", nativeQuery = true)
	public Integer markNoteAsUnReadForDeletedNote(Long noteId);
	
}
