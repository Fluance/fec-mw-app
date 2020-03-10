package net.fluance.cockpit.app.service.domain;

import java.util.List;

import net.fluance.cockpit.core.model.AccessLog;
import net.fluance.cockpit.core.model.AccessLogShort;
import net.fluance.cockpit.core.model.Count;

public interface AccessLogService {

	/**
	 * Gets the list of {@link AccessLogShort} for a patient ID. The results objects only will have logDate and user
	 * 
	 * @param pid
	 * @param orderBy
	 * @param sortOrder
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<AccessLogShort> getPatientActions(String pid, String orderBy, String sortOrder, Integer limit, Integer offset);

	/**
	 * Gets the count for a list of {@link PatientActionDto} for a patient ID.
	 * 
	 * @param pid
	 * @return
	 */
	public Count getPatientActionsCount(String pid);

	/**
	 * Returns the actions made by a <b>username</b> over the <b>patientis</b> elements
	 * 
	 * @param pid	Patient ID
	 * @param domain
	 * @param username
	 * @param orderBy
	 * @param sortOrder
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<AccessLog> getPatientActionsByUser(String pid, String domain, String username, String orderBy, String sortOrder, Integer limit, Integer offset);
	
	/**
	 * Returns the actions made by a <b>username</b> with <b>actualUserName</b> over the <b>patientis</b> elements
	 * 
	 * @param pid
	 * @param domain
	 * @param username
	 * @param actualUserName
	 * @param orderBy
	 * @param sortOrder
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<AccessLog> getPatientActionsByUserAndActualUsername(String pid, String domain, String username, String actualUsername, String orderBy, String sortOrder, Integer limit, Integer offset);

	/**
	 * Returns the number of actions made by a <b>username</b> over the <b>patientis</b> elements
	 * 
	 * @param pid Patient ID
	 * @param domain
	 * @param username
	 * @return
	 */
	public Count getPatientActionsByUserCount(String pid, String domain, String username);
	
	/**
	 * Returns the number of actions made by a <b>username</b> with <b>actualUserName</b over the <b>patientis</b> elements
	 * 
	 * @param pid
	 * @param domain
	 * @param username
	 * @param actualUsername
	 * @return
	 */
	public Count getPatientActionsByUserAndActualUsernameCount(String pid, String domain, String username, String actualUsername);

}
