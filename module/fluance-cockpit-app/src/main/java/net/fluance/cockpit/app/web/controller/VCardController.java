package net.fluance.cockpit.app.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.cockpit.app.service.domain.VCardService;

@RestController
public class VCardController extends AbstractRestController{

	private static Logger LOGGER = LogManager.getLogger(VCardController.class);
	
	@Autowired
	VCardService vcardService;

	@ApiOperation(value = "VCard Contact Patient",  tags = "VCard API", produces = "text/vcard")
	@RequestMapping(value = "/patients/vcard/{pid}", method = RequestMethod.GET)
	public ResponseEntity<?> getVCardContactPatient(@PathVariable Integer pid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{
			final HttpHeaders responseHeaders= new HttpHeaders();
			responseHeaders.add("Content-Type", "text/vcard");
			String contact = vcardService.getPatientContact(pid);
			return new ResponseEntity<>(contact, responseHeaders, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "VCard Contact Physician",  tags = "VCard API", produces = "text/vcard")
	@RequestMapping(value = "/physician/vcard/{id}", method = RequestMethod.GET, produces = "text/vcard")
	public ResponseEntity<?> getVCardContactPhysician(@PathVariable Integer id, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{
			final HttpHeaders responseHeaders= new HttpHeaders();
			responseHeaders.add("Content-Type", "text/vcard");
			String contact = vcardService.getPhysicianContact(id);
			return new ResponseEntity<>(contact, responseHeaders, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	@Override
	public Object handleDataException(DataException exc) {
		return null;
	}
}
