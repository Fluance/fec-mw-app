package net.fluance.cockpit.core.repository.jpa.note;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProfileNoteTrackPK implements Serializable{

	@Column(name = "username")
	private String userName;
	 
	@Column(name = "note_id")
	private Long noteId;
	
}
