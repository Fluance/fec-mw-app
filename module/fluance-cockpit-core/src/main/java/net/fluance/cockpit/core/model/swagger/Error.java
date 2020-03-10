package net.fluance.cockpit.core.model.swagger;

import java.io.Serializable;

public class Error implements Serializable{

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}