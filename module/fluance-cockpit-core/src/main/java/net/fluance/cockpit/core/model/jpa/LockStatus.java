package net.fluance.cockpit.core.model.jpa;

import java.io.Serializable;

//TODO : add LOCK object as attribute
public class LockStatus implements Serializable {

	private boolean isLocked;
	
	public LockStatus(boolean isLocked){
		this.isLocked = isLocked;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
	}
}
