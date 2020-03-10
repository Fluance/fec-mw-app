package net.fluance.cockpit.app.web.controller;

import java.io.IOException;
import java.util.List;

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
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.jdbc.radiology.Radiology;
import net.fluance.cockpit.core.model.jdbc.radiology.RadiologyReport;
import net.fluance.cockpit.core.repository.jdbc.radiology.RadiologyReportRepository;
import net.fluance.cockpit.core.repository.jdbc.radiology.RadiologyRepository;

@RestController
@RequestMapping(WebConfig.API_MAIN_URI_RADIOLOGY)
public class RadiologyController extends AbstractRestController {
	
	private static Logger LOGGER = LogManager.getLogger(RadiologyController.class);

	@Autowired
	private RadiologyRepository radiologyRepository;
	
	@Autowired
	private RadiologyReportRepository radiologyReportRepository;
	
	@Autowired
	private LogService patientAccessLogService;
	
	@ApiOperation(value = "Radiology List", response = Radiology.class, responseContainer = "list", tags = "Radiology API")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getRadiologyExams(@RequestParam Long pid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{
			List<Radiology> examList = radiologyRepository.findByPatientId(pid);
			patientAccessLogService.log(MwAppResourceType.RADIOLOGY_EXAMS, examList, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), pid,null, request.getRequestURI(), null);
			return new ResponseEntity<>(examList, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Radiology Report List", response = RadiologyReport.class, responseContainer = "list", tags = "Radiology API")
	@RequestMapping(value = "/reports", method = RequestMethod.GET)
	public ResponseEntity<?> getRadiologyReports(@RequestParam Long pid, @RequestParam(required=false)String orderby, @RequestParam(required=false)String sortorder, @RequestParam(required=false)Integer limit, @RequestParam(required=false)Integer offset, HttpServletRequest request,HttpServletResponse response) throws IOException {
		try{
			List<RadiologyReport> reports = radiologyReportRepository.findByPatientId(pid, orderby, sortorder, limit, offset);
			patientAccessLogService.log(MwAppResourceType.RADIOLOGY_EXAMS, reports, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), pid,null, request.getRequestURI(), null);
			return new ResponseEntity<>(reports, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Radiology Report detail", response = RadiologyReport.class, tags = "Radiology API")
	@RequestMapping(value = "/reports/{uid}", method = RequestMethod.GET)
	public ResponseEntity<?> getRadiologyReportsByUid(@PathVariable String uid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{
			RadiologyReport report = radiologyReportRepository.findByStudyUId(uid);
			//patientAccessLogService.log(MwAppResourceType.RADIOLOGY_EXAMS, report, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), null, null, request.getRequestURI(), uid);
    			return new ResponseEntity<RadiologyReport>(report, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Radiology Report Count", response = Count.class, tags = "Radiology API")
	@RequestMapping(value = "/reports/count", method = RequestMethod.GET)
	public ResponseEntity<?> getRadiologyReportsCount(@RequestParam Long pid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing getRadiologyReportsCount...");
		getLogger().debug("Parameters : pid = " + pid);
		try {
			Integer count = radiologyReportRepository.findByPatientIdCount(pid);
			return new ResponseEntity<>(new Count(count), HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	// No DataException is expected here as we are just reading, so just exit with error 500 if happen
	@Override
	public Object handleDataException(DataException exc) {
		GenericResponsePayload grp = new GenericResponsePayload();
		String msg = DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE;
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		grp.setMessage(msg);
		return new ResponseEntity<>(grp, status);
	}
}
