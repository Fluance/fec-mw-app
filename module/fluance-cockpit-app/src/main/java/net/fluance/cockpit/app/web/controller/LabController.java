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
import net.fluance.cockpit.core.model.jdbc.lab.Groupname;
import net.fluance.cockpit.core.model.jdbc.lab.LabData;
import net.fluance.cockpit.core.repository.jdbc.lab.GroupnameRepository;
import net.fluance.cockpit.core.repository.jdbc.lab.LabDataRepository;

@RestController
@RequestMapping(WebConfig.API_MAIN_URI_LAB)
public class LabController extends AbstractRestController {
	
	public static final String PATH_GROUPNAME_LIST = "/groupname/list";
	public static final String PATH_DATA = "/data";

	private static Logger LOGGER = LogManager.getLogger(LabController.class);

	@Autowired
	private GroupnameRepository groupNameRepository;
	
	@Autowired
	private LabDataRepository labDataRepository;
	
	@Autowired
	private LogService patientAccessLogService;
	
	@ApiOperation(value = "", response = Groupname.class, responseContainer = "List", tags="Lab API")
	@RequestMapping(value = PATH_GROUPNAME_LIST, method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getGroupNamesByPid(@RequestParam Long pid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Retrieving group name list for patient " + pid + "...");
		getLogger().debug("Patient ID = " + pid);
		try{
			List<Groupname> groupNameList = groupNameRepository.findByPid(pid);
			patientAccessLogService.log(MwAppResourceType.LAB, groupNameList, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), pid,null, request.getRequestURI(), null);
			return new ResponseEntity<>(groupNameList, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "", response = LabData.class, responseContainer = "List", tags="Lab API")
	@RequestMapping(value = "/data", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getDataByPidAndGroupName(@RequestParam Long pid, @RequestParam(name="groupname") String groupName, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Retrieving lab data for patient " + pid + " and group name " + groupName + "...");
		try{
			List<LabData> labDataList = labDataRepository.findByPidAndGroupName(pid, groupName);
			patientAccessLogService.log(MwAppResourceType.LAB, labDataList, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), pid,null, request.getRequestURI(), null);
			return new ResponseEntity<>(labDataList, HttpStatus.OK);
		} catch (Exception e){
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
