package net.fluance.cockpit.core.model.jdbc.note;

import java.io.Serializable;

public class NoteCategory implements Serializable{
	
	private int id;
	private String name;
	private int priority;
	
	public NoteCategory(){}
	public NoteCategory(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public NoteCategory(int id, String name, int priority) {
		this.id = id;
		this.name = name;
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
