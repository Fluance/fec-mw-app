package net.fluance.cockpit.app.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogService;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.app.web.support.payload.response.GenericResponsePayload;
import net.fluance.cockpit.app.config.WebConfig;
import net.fluance.cockpit.app.service.domain.AccessLogService;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.AccessLog;
import net.fluance.cockpit.core.model.AccessLogShort;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.jdbc.accesslog.PatientAccessLog;

@RestController
// @ComponentScan("net.fluance.cockpit.core.repository.jdbc")
@RequestMapping(WebConfig.API_MAIN_URI_ACCESSLOGGER)
public class AccessLogController extends AbstractRestController {

	private static Logger LOGGER = LogManager.getLogger(AccessLogController.class);

	@Autowired
	private LogService patientAccessLogService;

	@Autowired
	private AccessLogService accessLogService;

	@ApiOperation(value = "Patient AccessLogs", response = AccessLogShort.class, responseContainer = "List", tags = "Patient Access Logs API", notes = "Gets the Patient access logs, orderby possible values : LOGDATE, USERNAME which is : \"lastname, firstname, username\", FIRSTNAME, LASTNAME, DISPLAYNAME")
	@RequestMapping(value = "/patient/{pid}", method = RequestMethod.GET)
	public ResponseEntity<?> getPatientAccessLogs(@PathVariable String pid, @RequestParam(required = false, name = "orderby") String orderBy, @RequestParam(required = false, name = "sortorder") String sortOrder,
			@RequestParam(required = false) Integer limit, @RequestParam(required = false) Integer offset, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Processing getPatientAccessLog...");
		getLogger().debug("Parameters : pid = " + pid);
		try {
			List<AccessLogShort> lAccessLogShortDto = accessLogService.getPatientActions(pid, orderBy, sortOrder, limit, offset);

			patientAccessLogService.log(MwAppResourceType.LOGS_PATIENT, lAccessLogShortDto, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), new Long(pid), null, request.getRequestURI(), null);
			return new ResponseEntity<List<AccessLogShort>>(lAccessLogShortDto, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Patient AccessLog Count", response = Count.class, tags = "Patient Access Logs API")
	@RequestMapping(value = "/patient/{pid}/count", method = RequestMethod.GET)
	public ResponseEntity<?> getPatientAccessLogsCount(@PathVariable String pid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing getPatientAccessLogCount...");
		getLogger().debug("Parameters : pid = " + pid);
		try {
			Count count = accessLogService.getPatientActionsCount(pid);
			return new ResponseEntity<Count>(count, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Patient AccessLogs", tags = "Patient Access Logs API", notes = "Save a Patient Access Log", code = 201)
	@RequestMapping(value = "/patient", method = RequestMethod.POST)
	public ResponseEntity<?> savePatientAccessLogs(@RequestParam(required = false) Long pid, @RequestParam String objectType, @RequestParam(required = false) Long visitNb, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		getLogger().info("Processing savePatientAccessLogs...");
		try {
			MwAppResourceType resourceType = MwAppResourceType.permissiveValueOf(objectType);

			Future<String> asynResult = patientAccessLogService.log(resourceType, null, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), pid, visitNb, request.getRequestURI(), null);

			asynResult.get();

			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Patient Access Log Detail", response = PatientAccessLog.class, responseContainer = "List", tags = "Patient Access Logs API", notes = "Gets the Patient Actions Log, orderby possible values : LOGDATE, USERNAME which is : \"lastname, firstname, username\", FIRSTNAME, LASTNAME, DISPLAYNAME")
	@RequestMapping(value = "/patient/{pid}/users/{domain}/{username}", method = RequestMethod.GET)
	public ResponseEntity<?> getPatientActionsLog(@PathVariable String pid, @PathVariable String domain, @PathVariable String username, @RequestParam(required = false, name = "orderby") String orderBy,
			@RequestParam(required = false, name = "sortorder") String sortOrder, @RequestParam(required = false) Integer limit, @RequestParam(required = false) Integer offset, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		getLogger().info("Processing getPatientActionsLog...");
		getLogger().debug("Parameters : pid = " + pid);
		getLogger().debug("Parameters : username = " + username);
		try {
			List<AccessLog> lAccessLogDto = accessLogService.getPatientActionsByUser(pid, domain, username, orderBy, sortOrder, limit, offset);
			return new ResponseEntity<List<AccessLog>>(lAccessLogDto, HttpStatus.OK);
		} catch (Exception e) {
			// If the error comes from the async method, the real cause must be extracted
			if (e instanceof ExecutionException) {
				e = (Exception) e.getCause();
			}
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Patient Access Log Detail", response = PatientAccessLog.class, responseContainer = "List", tags = "Patient Access Logs API", notes = "Gets the Patient Actions Log, orderby possible values : LOGDATE, USERNAME which is : \"lastname, firstname, username\", FIRSTNAME, LASTNAME, DISPLAYNAME")
	@RequestMapping(value = "/patient/{pid}/users/{domain}/{username}/actualusername/{actualUsername}", method = RequestMethod.GET)
	public ResponseEntity<?> getPatientActionsLogByActualUsername(@PathVariable String pid, @PathVariable String domain, @PathVariable String username, @PathVariable String actualUsername,
			@RequestParam(required = false, name = "orderby") String orderBy,
			@RequestParam(required = false, name = "sortorder") String sortOrder,
			@RequestParam(required = false) Integer limit,
			@RequestParam(required = false) Integer offset,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		getLogger().info("Processing getPatientActionsLog...");
		getLogger().debug("Parameters : pid = " + pid);
		getLogger().debug("Parameters : username = " + username);
		getLogger().debug("Parameters : actualUsername = " + actualUsername);
		try {
			List<AccessLog> lAccessLogDto = accessLogService.getPatientActionsByUserAndActualUsername(pid, domain, username, actualUsername, orderBy, sortOrder, limit, offset);
			return new ResponseEntity<List<AccessLog>>(lAccessLogDto, HttpStatus.OK);
		} catch (Exception e) {
			// If the error comes from the async method, the real cause must be extracted
			if (e instanceof ExecutionException) {
				e = (Exception) e.getCause();
			}
			return handleException(e);
		}
	}

	@ApiOperation(value = "Patient Access Log Detail Count", response = Count.class, tags = "Patient Access Logs API")
	@RequestMapping(value = "/patient/{pid}/users/{domain}/{username}/count", method = RequestMethod.GET)
	public ResponseEntity<?> getPatientActionsLogCount(@PathVariable String pid, @PathVariable String domain, @PathVariable String username, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing getPatientActionsLogCount...");
		getLogger().debug("Parameters : pid = " + pid);
		getLogger().debug("Parameters : username = " + username);
		try {
			Count total = accessLogService.getPatientActionsByUserCount(pid, domain, username);
			return new ResponseEntity<Count>(total, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Patient Access Log Detail Count", response = Count.class, tags = "Patient Access Logs API")
	@RequestMapping(value = "/patient/{pid}/users/{domain}/{username}/actualusername/{actualUsername}/count", method = RequestMethod.GET)
	public ResponseEntity<?> getPatientActionsLogByActualUsernameCount(@PathVariable String pid, @PathVariable String domain, @PathVariable String username, @PathVariable String actualUsername,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing getPatientActionsLogCount...");
		getLogger().debug("Parameters : pid = " + pid);
		getLogger().debug("Parameters : username = " + username);
		getLogger().debug("Parameters : actualUsername = " + actualUsername);
		try {
			Count total = accessLogService.getPatientActionsByUserAndActualUsernameCount(pid, domain, username, actualUsername);
			return new ResponseEntity<Count>(total, HttpStatus.OK);
		} catch (Exception e) {
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
