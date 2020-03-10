package net.fluance.cockpit.core.dao;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.AccessLog;
import net.fluance.cockpit.core.model.AccessLogShort;
import net.fluance.cockpit.core.model.AccessLogUser;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.jdbc.accesslog.PatientAccessLog;
import net.fluance.cockpit.core.model.jdbc.accesslog.PatientAccessLogShort;
import net.fluance.cockpit.core.repository.jdbc.accesslog.PatientAccessLogRepository;
import net.fluance.cockpit.core.repository.jdbc.accesslog.PatientAccessLogShortRepository;
import net.fluance.cockpit.core.util.sql.PatientAccessLogOrderByEnum;
import net.fluance.cockpit.core.util.sql.PatientAccessLogShortOrderEnum;

/**
 * Data access service for the AccessLog, the exposed methods should return {@link AccessLog} or {@link AccessLogShort}
 */
@Service
public class AccessLogDao {
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultAccessLogListOrderBy}")
	private String defaultResultAccessLogListOrderBy;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultAccessLogListSortOrder}")
	private String defaultResultAccessLogListSortOrder;	
	
	@Autowired
	private PatientAccessLogRepository patientAccessLogRepository;

	@Autowired
	private PatientAccessLogShortRepository patientAccessLogShortRepository;
	
	/**
	 * Returns a list of {@link AccessLogShort} the represent the list of users with access logs to the given patient
	 * 
	 * @param pid
	 * @param orderBy
	 * @param sortOrder
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<AccessLogShort> findUsersWithAccessLogForPatient(String pid, String orderBy, String sortOrder, Integer limit, Integer offset) {
		if (pid == null || pid.isEmpty()) {
			throw new InvalidParameterException("Patient ID can't be null");
		}

		//validate order and limit
		orderBy = setOrderBy(orderBy);
		sortOrder = setShortOrder(sortOrder);		
		if (limit == null) {
			limit = defaultResultLimit;
		}		
		if (offset == null) {
			offset = defaultResultOffset;
		}
			
		return patientAccessLogShortListToAccessLogShortDtoList(patientAccessLogShortRepository.findByPid(pid, orderBy, sortOrder, limit, offset));
	}

	/**
	 * Returns the total amount of users with access logs to the given patient
	 * 
	 * @param pid
	 * @return
	 */
	public Count findUsersWithAccessLogForPatientCount(String pid) {
		if (pid == null || pid.isEmpty()) {
			throw new InvalidParameterException("Patient ID can't be null");
		}
		
		return new Count(patientAccessLogShortRepository.findByPidCount(pid));
	}

	/**
	 * Returns the access log for a Patient for a given user
	 * 
	 * @param pid {@link String}
	 * @param appuser {@link String} with format USERNAME/DOMAIN
	 * @param orderBy {@link String}
	 * @param sortOrder {@link String}
	 * @param limit {@link Integer}
	 * @param offset {@link Integer}
	 * @return
	 * @throws IllegalArgumentException
	 */
	public List<AccessLog> findAccessLogForPatientByPidAndUser(String pid, String appuser, String orderBy, String sortOrder, Integer limit, Integer offset) throws IllegalArgumentException {
		if (pid == null || pid.isEmpty()) {
			throw new InvalidParameterException("Patient ID can't be null");
		}
		
		if (appuser == null || appuser.isEmpty()) {
			throw new InvalidParameterException("Appuser can't be null");
		}
		
		//validate order and limit
		orderBy = setOrderBy(orderBy);
		sortOrder = setShortOrder(sortOrder);		
		if (limit == null) {
			limit = defaultResultLimit;
		}		
		if (offset == null) {
			offset = defaultResultOffset;
		}
		
		return patientAccessLogListToAccessLogDtoList(patientAccessLogRepository.findByPidAndAppuser(pid, appuser, orderBy, sortOrder, limit, offset));
	}
	
	/**
	 * Returns the access log for a Patient for a given user with actualUserName
	 * 
	 * @param pid {@link String}
	 * @param appuser {@link String} with format USERNAME/DOMAIN
	 * @param atualUserName {@link String}
	 * @param orderBy {@link String}
	 * @param sortOrder {@link String}
	 * @param limit {@link Integer}
	 * @param offset {@link Integer}
	 * @return
	 * @throws IllegalArgumentException
	 */
	public List<AccessLog> findAccessLogForPatientByPidAndUserAndActualUsername(String pid, String appuser, String actualUsername, String orderBy, String sortOrder, Integer limit, Integer offset) throws IllegalArgumentException {
		if (pid == null || pid.isEmpty()) {
			throw new InvalidParameterException("Patient ID can't be null");
		}
		
		if (appuser == null || appuser.isEmpty()) {
			throw new InvalidParameterException("Appuser can't be null");
		}
		
		if (actualUsername == null || actualUsername.isEmpty()) {
			throw new InvalidParameterException("actualUsername can't be null");
		}
		
		//validate order and limit
		orderBy = setOrderBy(orderBy);
		sortOrder = setShortOrder(sortOrder);		
		if (limit == null) {
			limit = defaultResultLimit;
		}		
		if (offset == null) {
			offset = defaultResultOffset;
		}
		
		return patientAccessLogListToAccessLogDtoList(patientAccessLogRepository.findByPidAndAppuserAndExternalUser(pid, appuser, actualUsername, orderBy, sortOrder, limit, offset));
	}

	/**
	 * Counts the access log for a Patient for a given user
	 * 
	 * @param pid
	 * @param appuser
	 * @return
	 */
	public Count findAccessLogForPatientByPidAndUserCount(String pid, String appuser) {
		if (pid == null || pid.isEmpty()) {
			throw new InvalidParameterException("Patient ID can't be null");
		}
		
		if (appuser == null || appuser.isEmpty()) {
			throw new InvalidParameterException("Appuser can't be null");
		}
		
		return patientAccessLogRepository.findByPidAndAppuserCount(pid, appuser);
	}
	
	/**
	 * Counts the access log for a Patient for a given user with atualUsername
	 * 
	 * @param pid
	 * @param appuser
	 * @param atualUsername
	 * @return
	 */
	public Count findAccessLogForPatientByPidAndUserAndActualUsernameCount(String pid, String appuser, String actualUsername) {
		if (pid == null || pid.isEmpty()) {
			throw new InvalidParameterException("Patient ID can't be null");
		}
		
		if (appuser == null || appuser.isEmpty()) {
			throw new InvalidParameterException("Appuser can't be null");
		}
		
		if (actualUsername == null || actualUsername.isEmpty()) {
			throw new InvalidParameterException("actualUsername can't be null");
		}
		
		return patientAccessLogRepository.findByPidAndAppuserAndExternalUserCount(pid, appuser, actualUsername);
	}

	/**
	 * Converts a {@link PatientAccessLog} to {@link AccessLog}
	 * 
	 * @param accessLog
	 * @return
	 */
	private AccessLog patientAccessLogToAccessLogDto(PatientAccessLog patientAccessLog) {
		AccessLog accessLog = new AccessLog();

		if (patientAccessLog != null) {
			accessLog.setLogDate(patientAccessLog.getLogDate());
			accessLog.setHttpMethod(patientAccessLog.getHttpMethod());
			accessLog.setObjectType(patientAccessLog.getObjectType());
			accessLog.setObjectId(patientAccessLog.getObjectId());
			accessLog.setDisplayName(patientAccessLog.getDisplayName());
			accessLog.setParentPid(patientAccessLog.getParentPid());
			accessLog.setParentVisitNb(patientAccessLog.getParentVisitNb());

			AccessLogUser user = new AccessLogUser();
			user.setUsername(patientAccessLog.getAppUser());
			user.setFirstName(patientAccessLog.getFirstName());
			user.setLastName(patientAccessLog.getLastName());
			user.setActualUsername(patientAccessLog.getExternalUser());
			user.setActualFirstName(patientAccessLog.getExternalFirstName());
			user.setActualLastName(patientAccessLog.getExternalLastName());
			user.setActualEmail(patientAccessLog.getExternalEmail());
			
			accessLog.setUser(user);
		}

		return accessLog;
	}

	/**
	 * Converts a list of {@link PatientAccessLog} to a list of {@link AccessLog}
	 * 
	 * @param patientAccessLogList
	 * @return
	 */
	private List<AccessLog> patientAccessLogListToAccessLogDtoList(List<PatientAccessLog> patientAccessLogList) {
		List<AccessLog> accessLogList = new ArrayList<AccessLog>();

		if (patientAccessLogList != null) {
			for (PatientAccessLog patientActionDto : patientAccessLogList) {
				accessLogList.add(patientAccessLogToAccessLogDto(patientActionDto));
			}
		}

		return accessLogList;
	}

	/**
	 * Converts a {@link PatientAccessLogShort} to {@link AccessLogShort}
	 * 
	 * @param accessLog
	 * @return
	 */
	private AccessLogShort patientAccessLogShortToAccessLogShortDto(PatientAccessLogShort patientAccessLog) {
		AccessLogShort accessLogShortDto = new AccessLogShort();

		if (patientAccessLog != null) {
			accessLogShortDto.setLogDate(patientAccessLog.getLogDate());

			AccessLogUser user = new AccessLogUser();
			user.setUsername(patientAccessLog.getAppUser());
			user.setFirstName(patientAccessLog.getFirstName());
			user.setLastName(patientAccessLog.getLastName());
			user.setActualUsername(patientAccessLog.getExternalUser());
			user.setActualFirstName(patientAccessLog.getExternalFirstName());
			user.setActualLastName(patientAccessLog.getExternalLastName());
			user.setActualEmail(patientAccessLog.getExternalEmail());
			
			accessLogShortDto.setUser(user);
		}
		return accessLogShortDto;
	}
	

	/**
	 * Converts a list of {@link PatientAccessLogShort} to a list of {@link AccessLogShort}
	 * 
	 * @param patientAccessLogList
	 * @return
	 */
	private List<AccessLogShort> patientAccessLogShortListToAccessLogShortDtoList(List<PatientAccessLogShort> patientAccessLogList) {
		List<AccessLogShort> lAccessLogShortDto = new ArrayList<AccessLogShort>();

		if (patientAccessLogList != null) {
			for (PatientAccessLogShort patientAccessLog : patientAccessLogList) {
				lAccessLogShortDto.add(patientAccessLogShortToAccessLogShortDto(patientAccessLog));
			}
		}

		return lAccessLogShortDto;
	}
	
	/**
	 * Validates the orderBy value over {@link PatientAccessLogOrderByEnum}, 
	 * if the value don't exist set the default that is defined on property 
	 * ${net.fluance.cockpit.core.model.repository.defaultResultAccessLogListOrderBy}
	 * 
	 * @param orderBy
	 * @return
	 */
	private String setOrderBy(String orderBy) {
		if(!StringUtils.isEmpty(orderBy)) {
			PatientAccessLogOrderByEnum orderbyEnum = PatientAccessLogOrderByEnum.permissiveValueOf(orderBy);
			if(orderbyEnum == PatientAccessLogOrderByEnum.UNKNOWN) {
				return defaultResultAccessLogListOrderBy;
			}
			
			return orderBy;
		} else {
			return defaultResultAccessLogListOrderBy;
		}
	}
	
	/**
	 * Validates the sortOrder value over {@link PatientAccessLogShortOrderEnum}<br>
	 * If the value don't exist set the default that is defined on property 
	 * ${net.fluance.cockpit.core.model.repository.defaultResultAccessLogListOrderBy}
	 * 
	 * @param sortOrder
	 * @return
	 */
	private String setShortOrder(String sortOrder){
		if(!StringUtils.isEmpty(sortOrder)) {
			PatientAccessLogShortOrderEnum sortOrderEnum = PatientAccessLogShortOrderEnum.permissiveValueOf(sortOrder);
			if (sortOrderEnum == PatientAccessLogShortOrderEnum.UNKNOWN) {
				return defaultResultAccessLogListSortOrder;
			}
			
			return sortOrder;
		} else {
			return defaultResultAccessLogListSortOrder;
		}
	}
}
