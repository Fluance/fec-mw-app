/**
 * 
 */
package net.fluance.cockpit.app.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.fluance.app.data.model.identity.EhProfile;

@RestController
public class UserProfileController {

	private static final Logger LOGGER = LogManager.getLogger(UserProfileController.class);

	@RequestMapping(value = "/user/profile/{username}/{domain}", method = RequestMethod.GET)
	public ResponseEntity<?> read(@PathVariable String username, @PathVariable String domain) {
		LOGGER.info("Request for user profile: " + domain + "/" + username);
		EhProfile profile = new EhProfile();
		profile.setUsername(username);
		profile.setDomain(domain);
		LOGGER.info("Returning user profile: " + profile.getDomain() + "/" + profile.getUsername());
		return new ResponseEntity<> (profile, HttpStatus.OK);
	}

}
