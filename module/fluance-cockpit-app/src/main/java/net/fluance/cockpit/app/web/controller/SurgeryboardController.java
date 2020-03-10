
package net.fluance.cockpit.app.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.data.model.identity.User;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.app.web.support.payload.response.GenericResponsePayload;
import net.fluance.cockpit.app.config.WebConfig;
import net.fluance.cockpit.app.service.domain.SurgeryboardService;
import net.fluance.cockpit.core.model.dto.surgeryboard.SurgeryboardDto;
import net.fluance.cockpit.core.util.DateUtils;

@RestController
@RequestMapping(WebConfig.API_MAIN_URI_SURGERYBOARD)
public class SurgeryboardController extends AbstractRestController{

	private static final Logger LOGGER = LogManager.getLogger(SurgeryboardController.class);
	
	@Autowired
	SurgeryboardService surgeryboardService;
	
	@ApiOperation(value = "Get Surgeryboard", response = SurgeryboardDto.class, tags = "Surgeryboard API")
	@GetMapping(path = "/{companyId}", produces = "application/json")
	public ResponseEntity<?> getOne(@PathVariable("companyId") Integer companyId,
			@RequestParam(required = true, value = "noteDate") @DateTimeFormat(pattern = DateUtils.DATE_FORMAT) Date noteDate,
			HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving the entry [id={}] from the Surgeryboard", companyId);
		try {
			SurgeryboardDto surgeryboardDto = surgeryboardService.getOneEntry(companyId, noteDate);			
			return new ResponseEntity<>(surgeryboardDto, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Update Surgeryboard", response = SurgeryboardDto.class, tags = "Surgeryboard API")
	@RequestMapping(value = "/{companyId}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateNote(@PathVariable("companyId") Integer companyId,
			@RequestParam(required = true, value = "noteDate") @DateTimeFormat(pattern = DateUtils.DATE_FORMAT) Date noteDate,
			@RequestParam(required = false, value = "note") String note,
			HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Writting the entry [id={}] from the Surgeryboard", companyId);
		try {
			SurgeryboardDto surgeryboardDto = surgeryboardService.update(companyId, noteDate, note, (User) request.getAttribute(User.USER_KEY));			
			return new ResponseEntity<>(surgeryboardDto, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	@Override
	public Object handleDataException(DataException arg0) {
		GenericResponsePayload genericResponsePayload = new GenericResponsePayload();
		String message = DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE;
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		genericResponsePayload.setMessage(message);
		return new ResponseEntity<>(genericResponsePayload, status);
	}

}
