package net.fluance.cockpit.core.util;

import net.fluance.app.data.model.identity.User;
import net.fluance.cockpit.core.model.PatientAccessLogModel;

/**
 * Utilities for work with the Patient Access Log, specially {@link PatientAccessLogModel} objects
 */
public class PatientAccessLogUtils {
	private PatientAccessLogUtils() {}
	
	/**
	 * Fill the user information to the given {@link PatientAccessLogModel}. The method manages the Third party user information if it exists.
	 * 
	 * @param user
	 * @param log
	 */
	public static void fillUserInformation(User user, PatientAccessLogModel log) {
		if(user != null && log != null) {
			String userName = "";
			
			if (user.isPatient()){
				userName = userName.concat(user.getUsername());
			} else {
				userName = userName.concat(user.getDomain());
			}
		
			userName = userName.concat("/");
			
			if (user.isPatient()){
				userName = userName.concat(user.getEvitaUserName());
			} else {
				userName = userName.concat(user.getUsername());
			}
			
			log.setUserName(userName);
			log.setFirstName(user.getFirstName());
			log.setLastName(user.getLastName());
			
			if(user.getThirdPartyUser() != null && user.getThirdPartyUser().getActualUserName() != null && user.getThirdPartyUser().getActualUserName().length()>0) {
				log.setActualUserName(user.getThirdPartyUser().getActualUserName());
				log.setActualFirstName(user.getThirdPartyUser().getActualFirstName());
				log.setActualLastName(user.getThirdPartyUser().getActualLastName());
				log.setActualEmail(user.getThirdPartyUser().getActualEmail());
			}
		}
	}

}
