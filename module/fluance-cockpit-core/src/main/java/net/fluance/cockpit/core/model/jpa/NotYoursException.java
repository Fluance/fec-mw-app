package net.fluance.cockpit.core.model.jpa;

import net.fluance.app.web.util.exceptions.ForbiddenException;

public class NotYoursException extends ForbiddenException {

	private Lock lock;

	public NotYoursException(Lock lock) {
		this.lock = lock;
	}

	public Lock getLock() {
		return lock;
	}
}