package net.fluance.cockpit.app.service.domain.impl;

import java.security.InvalidParameterException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import net.fluance.cockpit.app.service.domain.AccessLogService;
import net.fluance.cockpit.core.dao.AccessLogDao;
import net.fluance.cockpit.core.model.AccessLog;
import net.fluance.cockpit.core.model.AccessLogShort;
import net.fluance.cockpit.core.model.AccessLogUser;
import net.fluance.cockpit.core.model.Count;

@Service
public class AccessLogServiceImpl implements AccessLogService {
	String LOG_DETAIL_URL = "/users/%s";
	String LOG_DETAIL_URL_WITH_ACTUAL_USERNAME = "/users/%s/actualusername/%s";
	
	@Autowired
	private AccessLogDao accessLogDao;

	@Override
	public List<AccessLogShort> getPatientActions(String pid, String orderBy, String sortOrder, Integer limit, Integer offset) {
		if (pid == null || pid.isEmpty()) {
			throw new InvalidParameterException("Patient ID can't be null");
		}
		validateSortOrder(sortOrder);

		return applyDetailUrlToAccessLogs(accessLogDao.findUsersWithAccessLogForPatient(pid, orderBy, sortOrder, limit, offset));
	}

	@Override
	public Count getPatientActionsCount(String pid) {
		if (pid == null || pid.isEmpty()) {
			throw new InvalidParameterException("Patient ID can't be null");
		}

		return accessLogDao.findUsersWithAccessLogForPatientCount(pid);
	}

	@Override
	public List<AccessLog> getPatientActionsByUser(String pid, String domain, String username, String orderBy, String sortOrder, Integer limit, Integer offset) throws IllegalArgumentException {
		String appuser = setAppUser(domain, username);
		if (pid == null || pid.isEmpty()) {
			throw new InvalidParameterException("Patient ID can't be null");
		}
		
		return accessLogDao.findAccessLogForPatientByPidAndUser(pid, appuser, orderBy, sortOrder, limit, offset);
	}
	
	@Override
	public List<AccessLog> getPatientActionsByUserAndActualUsername(String pid, String domain, String username, String actualUsername, String orderBy, String sortOrder, Integer limit, Integer offset) {
		String appuser = setAppUser(domain, username);
		if (pid == null || pid.isEmpty()) {
			throw new InvalidParameterException("Patient ID can't be null");
		}
		if (actualUsername == null || actualUsername.isEmpty()) {
			throw new InvalidParameterException("ActualUsername can't be null");
		}
		
		
		return accessLogDao.findAccessLogForPatientByPidAndUserAndActualUsername(pid, appuser, actualUsername, orderBy, sortOrder, limit, offset);
	}

	@Override
	public Count getPatientActionsByUserCount(String pid, String domain, String username) {
		String appuser = setAppUser(domain, username);
		if (pid == null || pid.isEmpty()) {
			throw new InvalidParameterException("Patient ID can't be null");
		}
		
		return accessLogDao.findAccessLogForPatientByPidAndUserCount(pid, appuser);
	}
	
	@Override
	public Count getPatientActionsByUserAndActualUsernameCount(String pid, String domain, String username, String actualUsername) {
		String appuser = setAppUser(domain, username);
		if (pid == null || pid.isEmpty()) {
			throw new InvalidParameterException("Patient ID can't be null");
		}
		if (actualUsername == null || actualUsername.isEmpty()) {
			throw new InvalidParameterException("ActualUsername can't be null");
		}
		
		return accessLogDao.findAccessLogForPatientByPidAndUserAndActualUsernameCount(pid, appuser, actualUsername);
	}

	/**
	 * Validates the values for the short order, only ASC and DESC are correct
	 * 
	 * @param sortOrder
	 */
	private void validateSortOrder(String sortOrder) {
		if(!StringUtils.isEmpty(sortOrder) && !("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder))) {
			throw new InvalidParameterException("The sortOrder haven't the correct values ASC or DESC");
		}
	}

	/**
	 * Creates a <b>appuser</b> from a domain and a username.
	 * 
	 * @param domain
	 * @param username
	 * @return The string <b>domain/username</b>
	 * @throws InvalidParameterException
	 *             The two parameters are null or empty
	 */
	private String setAppUser(String domain, String username) throws InvalidParameterException {
		if (domain == null || username == null) {
			throw new InvalidParameterException("Domain and username can't be null");
		}

		return domain + "/" + username;
	}
	
	
	/**
	 * Apply the detailUrl for evety log entry
	 * 
	 * @param accessLogShorts
	 * @return
	 */
	private List<AccessLogShort> applyDetailUrlToAccessLogs(List<AccessLogShort> accessLogShorts) {
		accessLogShorts.forEach(log -> log.setDetailUrl(getDetailUrlFromAccessLogUser(log.getUser())));
		
		return accessLogShorts;
	}
	
	/**
	 * Generates the url for the detail of the logs for the user<br>
	 * /users/{DOMAIN}/{USER}<br>
	 * /users/{DOMAIN}/{USER}/actualusername/{actualusername} if the value is present
	 * 
	 * @param user
	 * @return
	 */
	private String getDetailUrlFromAccessLogUser(AccessLogUser user) {
		if(user !=null && !StringUtils.isEmpty(user.getUsername())) {
			if(StringUtils.isEmpty(user.getActualUsername())) {
				return String.format(LOG_DETAIL_URL, user.getUsername());
			} else {
				return String.format(LOG_DETAIL_URL_WITH_ACTUAL_USERNAME, user.getUsername(), user.getActualUsername());
			}
		}
		return "";
	}
}