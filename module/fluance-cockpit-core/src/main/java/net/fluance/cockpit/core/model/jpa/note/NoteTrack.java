package net.fluance.cockpit.core.model.jpa.note;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="profile_note_track")
public class NoteTrack implements Serializable{
	
	@Column(name = "username")
	private String userName;
	@Id
	@Column(name = "note_id")
	private Long noteId;
	private Date readdt;

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getNoteId() {
		return noteId;
	}
	public void setNoteId(Long noteId) {
		this.noteId = noteId;
	}
	public Date getReaddt() {
		return readdt;
	}
	public void setReaddt(Date readdt) {
		this.readdt = readdt;
	}
	
}
