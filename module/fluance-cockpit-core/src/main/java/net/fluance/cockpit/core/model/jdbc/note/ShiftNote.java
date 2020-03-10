package net.fluance.cockpit.core.model.jdbc.note;

import java.util.List;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.fluance.cockpit.core.model.PayloadDisplayLogName;
import net.fluance.cockpit.core.model.Shift;

public class ShiftNote implements Persistable<Long>, PayloadDisplayLogName{

	private List<NoteDetails> notes;
	private Shift shift;
	
	public ShiftNote(List<NoteDetails> notes, Shift shift) {
		this.notes = notes;
		this.shift = shift;
	}
	
	public List<NoteDetails> getNotes() {
		return notes;
	}

	public void setNotes(List<NoteDetails> notes) {
		this.notes = notes;
	}

	public Shift getShift() {
		return shift;
	}
	
	public void setShift(Shift shift) {
		this.shift = shift;
	}

	@Override
	public String displayName() {
		return "ShiftNote"; 
	}

	@JsonIgnore
	@Override
	public Long getId() {
		return null;
	}
	
	@JsonIgnore
	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	} }
