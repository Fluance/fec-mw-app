package net.fluance.cockpit.app.web.controller;

import java.io.IOException;
import java.net.URI;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Contact;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Url;
import net.fortuna.ical4j.model.property.Version;

@RestController
public class ICalControlller extends AbstractRestController{
	
	private static final String URL_TEXT = "If this link doesn't work, you can also copy and paste this link into your browser:\n";
	
	@ApiOperation(value = "ICal",  tags = "ICal API", produces = "text/calendar")
	@RequestMapping(value = "/ical", method = RequestMethod.GET, produces = "text/calendar")
	public ResponseEntity<?> getIcal(
			@RequestParam(required=false) String eventName, 
			@RequestParam(required=false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date eventStart, 
			@RequestParam(required=false) @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date eventEnd,
			@RequestParam(required=false) String url,
			@RequestParam(required=false) String location,
			@RequestParam(required=false) String patient,
			@RequestParam(required=false) String responsible,
			HttpServletRequest request,
			HttpServletResponse response
	) throws IOException {
		try{
			Calendar calendar = new Calendar();
			calendar.getProperties().add(Version.VERSION_2_0);
			calendar.getProperties().add(CalScale.GREGORIAN);
			DateTime start = null;
			if(eventStart != null){
				start = new DateTime(eventStart.getTime());
			}
			DateTime end = null;
			if(eventEnd != null){
				end = new DateTime(eventEnd.getTime());
			}
			VEvent event = new VEvent(start, end, eventName);
			
			// Location
			if(location != null && !location.isEmpty()){
				event.getProperties().add(new Location(location));
			}
			
			Description description = new Description();
			event.getProperties().add(description);
			
			// Patient
			if(patient != null && !patient.isEmpty()){
				Attendee atendee = new Attendee();
				atendee.getParameters().add(new Cn(patient));
				event.getProperties().add(atendee);
				
				updateDescription(event, "Patient: " + patient + "\n");
			}
			
			// Responsible
			if(responsible != null && !responsible.isEmpty()){
				Contact iCalContact = new Contact(responsible);
				event.getProperties().add(iCalContact);
				
				updateDescription(event, "Responsible: " + responsible + "\n");
			}
						
			// URL
			if(url != null && !url.isEmpty()){
				URI uri = new URI(url);
				event.getProperties().add(new Url(uri));
				
				updateDescription(event, URL_TEXT + url + "\n");
			}
			
			calendar.getComponents().add(event);
			final HttpHeaders responseHeaders= new HttpHeaders();
			responseHeaders.add("Content-Type", "text/calendar");
			return new ResponseEntity<>(calendar.toString(), responseHeaders, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	/**
	 * Given a {@link VEvent} add a String to the existing content in the {@link Description}
	 * @param event	iCal Event which contains the Description
	 * @param newValue	String to add to the Description
	 */
	private void updateDescription(VEvent event, String newValue){
		
		Description auxDesc = (Description) event.getProperty(Property.DESCRIPTION);
		
		if(auxDesc==null){
			return;
		}
		
		String oldValue = auxDesc.getValue();
		auxDesc.setValue((oldValue!=null) ? oldValue + newValue : newValue);
	}
	
	@Override
	public Logger getLogger() {
		return null;
	}

	@Override
	public Object handleDataException(DataException exc) {
		return null;
	}
}
