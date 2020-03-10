package net.fluance.cockpit.app.service.domain;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.data.model.identity.UserProfile;
import net.fluance.app.web.util.exceptions.NotFoundException;
import net.fluance.cockpit.core.model.jpa.Lock;
import net.fluance.cockpit.core.model.jpa.LockStatus;
import net.fluance.cockpit.core.model.jpa.NotYoursException;
import net.fluance.cockpit.core.repository.jpa.LockRepository;

@Service
public class LockService {

	private static Logger LOGGER = LogManager.getLogger(LockService.class);

	@Autowired
	private LockRepository lockRepository;

	@Value("${lock.timeout}")
	private int lockTimeOutHours;

	public Lock lock(Long resourceId, String resourceType, HttpServletRequest request){
		UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
		Lock existingLock = getLock(resourceId, resourceType);
		if (existingLock != null){
			if (existingLock.getUserName().equals(userProfile.getUsername()) && existingLock.getDomain().equals(userProfile.getDomain())){
				return refresh(resourceId, resourceType, request);
			} else if(existingLock.getExpirationDate().before(new Date())){
				unLock(resourceId, resourceType, request);
				return createLock(resourceId, resourceType, userProfile);
			} else {
				throw new NotYoursException(existingLock);
			}
		} else {
			return createLock(resourceId, resourceType, userProfile);
		}
	}

	private Lock createLock(Long resourceId, String resourceType, User user){
		Lock lock = new Lock(resourceId, resourceType, user.getUsername(), user.getDomain(), initNewExpirationDate());
		lockRepository.save(lock);
		return lock;
	}

	public void unLock(Long resourceId, String resourceType, HttpServletRequest request){
		UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
		try{
			Lock existingLock = getLock(resourceId, resourceType, userProfile.getUsername(), userProfile.getDomain());
			lockRepository.delete(existingLock);
		} catch (NotFoundException e){
			Lock existingLock = getLock(resourceId, resourceType);
			if (existingLock != null){
				if(existingLock.getExpirationDate().before(new Date())){
					lockRepository.delete(existingLock);
				} else {
					throw new NotYoursException(existingLock);
				}
			}
			else
				throw e;
		}
	}

	public LockStatus verify(Long resourceId, String resourceType){
		Lock existingLock = getLock(resourceId, resourceType);
		boolean result = isLockExpirationValid(existingLock) ;
		return new LockStatus(result);
	}

	public boolean isLockedByUser(Long resourceId, String resourceType, String username, String domain){
		try{
			return isLockExpirationValid(getLock(resourceId, resourceType, username, domain));
		} catch (NotFoundException e){
			LOGGER.info("The " + resourceType + " - id = : " + resourceId + " is not locked by the User : " + username);
			return false;
		}
	}

	/**
	 * refresh a lock. We search only the USER'S lock
	 * @param resourceId	Long
	 * @param resourceType	String
	 * @param request		HttpServletRequest
	 * @return Lock
	 */
	public Lock refresh(Long resourceId, String resourceType, HttpServletRequest request){
		UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
		Lock lock = getLock(resourceId, resourceType, userProfile.getUsername(), userProfile.getDomain());
		lock.setExpirationDate(initNewExpirationDate());
		lockRepository.save(lock);
		return lock;
	}

	private Lock getLock(Long resourceId, String resourceType, String username, String domain) {
		Lock lock = lockRepository.findByResourceIdAndResourceTypeAndProfile(resourceId, resourceType, username, domain);
		if (lock == null){
			throw new NotFoundException();
		} else {
			return lock;
		}
	}

	private Lock getLock(Long resourceId, String resourceType) {
		return lockRepository.findByResourceIdAndResourceType(resourceId, resourceType);
	}

	private Date initNewExpirationDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR, lockTimeOutHours);
		return cal.getTime();
	}

	private boolean isLockExpirationValid(Lock existingLock) {
		if(existingLock != null){
			boolean result = existingLock.getExpirationDate().compareTo(new Date()) > 0; 
			LOGGER.debug(result ? "Lock is found and valid" : "but not valid , Expiration Date = " + existingLock.getExpirationDate());
			return result;
		}
		LOGGER.debug( "Lock value found to be null");
		return false;
	}
}
