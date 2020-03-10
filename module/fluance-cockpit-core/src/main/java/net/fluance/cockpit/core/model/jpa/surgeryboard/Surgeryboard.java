package net.fluance.cockpit.core.model.jpa.surgeryboard;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="Surgeryboard")
@Table(name = "surgeryboard", schema = "ehealth")
public class Surgeryboard implements Persistable<SurgeryboardPK>, Serializable{
	
	private static final long serialVersionUID = -8802070591246097527L;

	@EmbeddedId
	private SurgeryboardPK id;
	
	@Column(name="note")
	private String note;	
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}	
	
	@JsonIgnore
	@Override
	public SurgeryboardPK getId() {
		return id;
	}
	
	public void setId(SurgeryboardPK id) {
		this.id = id;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}
	
}
