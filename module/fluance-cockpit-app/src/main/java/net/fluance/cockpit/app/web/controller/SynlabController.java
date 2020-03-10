package net.fluance.cockpit.app.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.fluance.app.data.exception.DataException;
import net.fluance.app.data.model.identity.UserProfile;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.app.web.support.payload.response.GenericResponsePayload;
import net.fluance.cockpit.app.config.WebConfig;
import net.fluance.cockpit.core.domain.synlab.Url;
import net.fluance.cockpit.app.service.domain.SynlabService;

@RestController
@RequestMapping(WebConfig.API_MAIN_URI_SYNLAB)
public class SynlabController extends AbstractRestController{

	private static Logger LOGGER = LogManager.getLogger(SynlabController.class);
	
	@Autowired
	private SynlabService service;
	
	@RequestMapping(value = "/{visitnb}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getLabInformation(@PathVariable Long visitnb, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{
			UserProfile profile = (UserProfile)request.getAttribute(UserProfile.USER_PROFILE_KEY);
			String url = service.getSynlabEndpointURL(visitnb, profile);
			return new ResponseEntity<>(new Url(url), HttpStatus.OK);
		}catch (SOAPException soapException){
			GenericResponsePayload grp = new GenericResponsePayload();
			grp.setMessage("An error occured while connecting to remote Prescription server.");
			LOGGER.error(soapException.getMessage());
			return new ResponseEntity<>(grp,HttpStatus.BAD_GATEWAY);
		} 
		catch (Exception e){
			return handleException(e);
		}
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	@Override
	public Object handleDataException(DataException exc) {
		GenericResponsePayload grp = new GenericResponsePayload();
		String msg = DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE;
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		grp.setMessage(msg);
		return new ResponseEntity<>(grp, status);
	}
}
