package net.fluance.cockpit.core.model.jdbc.lab;

import org.springframework.data.domain.Persistable;

@SuppressWarnings("serial")
public class Comment implements Persistable<Integer>{
	
	private int idComment;
	private String message;
	
	public Comment(int idComment, String message) {
		super();
		this.idComment = idComment;
		this.message = message;
	}
	public int getIdComment() {
		return idComment;
	}
	public void setIdComment(int idComment) {
		this.idComment = idComment;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
