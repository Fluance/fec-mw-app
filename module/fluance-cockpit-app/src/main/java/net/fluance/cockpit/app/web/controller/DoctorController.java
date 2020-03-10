package net.fluance.cockpit.app.web.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.data.model.identity.EhProfile;
import net.fluance.app.data.model.identity.User;
import net.fluance.app.security.service.UserProfileLoader;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.app.web.support.payload.response.GenericResponsePayload;
import net.fluance.cockpit.app.config.WebConfig;
import net.fluance.cockpit.app.service.domain.DoctorAssigmentService;
import net.fluance.cockpit.app.service.domain.DoctorService;
import net.fluance.cockpit.core.model.DoctorDataSource;
import net.fluance.cockpit.core.model.DoctorReference;

import net.fluance.cockpit.core.model.DoctorReferenceId;

@RestController
@RequestMapping(WebConfig.API_MAIN_URI_DOCTORS)
public class DoctorController extends AbstractRestController {

	private static final Logger LOGGER = LogManager.getLogger(DoctorController.class);
	
	private static final String MINIMUM_PARAM = "It is necessary to indicate one of the three parameters: First Name, Last Name or Speciality";

	@Autowired
	DoctorService doctorService;

	@Autowired
	DoctorAssigmentService doctorAssigmentService;
	
	@Autowired
	UserProfileLoader userProfileLoader;
	
	/**
	 * Returns a {@link List} of {@link DoctorReference} which match with the param different to null
	 * @param companyId
	 * @param firstName
	 * @param lastName
	 * @param speciality
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "Look for doctors with filters", response = DoctorReference.class, responseContainer="List", tags="Doctor API")
	@GetMapping(value = "/bycriteria")
	public ResponseEntity<?> getDoctorsWithFilters(
			@RequestParam("companyid") Long companyId,
			@RequestParam(value="firstname", required=false) String firstName,
			@RequestParam(value="lastname", required=false) String lastName,
			@RequestParam(value="speciality", required=false) String speciality,
			@RequestParam(value="limit", required=false, defaultValue="50") Integer limit,
			@RequestParam(value="offset", required=false, defaultValue="0") Integer offset,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			
			if(firstName == null && lastName == null && speciality == null){
				throw new IllegalArgumentException(MINIMUM_PARAM);
			}
			
			List<DoctorReference> doctors = doctorService.getDoctorsFilterByField(companyId, firstName, lastName, speciality, limit, offset);
			linkWithProfiles(request, doctors);
			return new ResponseEntity<>(doctors, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Show all matching doctors for Source, CompanyID & StaffID", response = DoctorReference.class, responseContainer="List", tags="Doctor API")
	@GetMapping(value = "/{source}/{companyId}/{staffId}")
	public ResponseEntity<?> getDoctorsById(
			@PathVariable("source") DoctorDataSource physicianSource,
			@PathVariable("companyId") Long companyId,
			@PathVariable("staffId") Long staffId,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			List<DoctorReference> doctors = doctorService.getDoctorsById(physicianSource,companyId,staffId);
			linkWithProfiles(request, doctors);
			return new ResponseEntity<>(doctors, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "/assign", response = String.class, responseContainer = "HttpStatus", tags = "Doctor API")
	@PostMapping(path = "/assign", produces = "application/json")
	public ResponseEntity addAssignement(@RequestParam(required = true) Long patientId,
			@RequestParam(required = true) Long companyId,
			@RequestParam(required = true) DoctorDataSource source, @RequestParam(required = false) Long staffId,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		try {
			getLogger().info("Delete Docotor assignement. Staff [{}] Patient [{}] Source[{}], Company [{}]", staffId, patientId,
					source,companyId);
			return new ResponseEntity<>(doctorAssigmentService.saveAssignement(staffId, patientId, source,companyId),
					HttpStatus.OK);
		} catch (Exception exception) {
			return handleException(exception);
		}
	}
 
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "/assign", response = String.class, responseContainer = "HttpStatus", tags = "Doctor API")
	@DeleteMapping(path = "/assign", produces = "application/json")
	public ResponseEntity deleteAssignement(@RequestParam(required = true) Long patientId,
			@RequestParam(required = true) Long companyId,
			@RequestParam(required = true) DoctorDataSource source, @RequestParam(required = true) Long staffId,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		try {
			getLogger().info("Add Docotor assignement. Staff [{}] Patient [{}] Source[{}], Company [{}]", staffId, patientId,
					source,companyId);
			doctorAssigmentService.deleteAssignement(staffId, patientId, source,companyId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception exception) {
			return handleException(exception);
		}
	}
	
	@ApiOperation(value = "Look for doctors by field", response = DoctorReference.class, responseContainer="List", tags="Doctor API")
	@GetMapping(value = "/search")
	public ResponseEntity<?> getDoctorsFilterByField(
			@RequestParam(value="field") String field,
			@RequestParam(required=false, defaultValue="50") Integer limit,
			@RequestParam(required=false, defaultValue="0" ) Integer offset,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			List<DoctorReference> doctors = doctorService.getDoctorsFilterByField(field, limit, offset);
			linkWithProfiles(request, doctors);
			return new ResponseEntity<>(doctors, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	private void linkWithProfiles(HttpServletRequest request, List<DoctorReference> doctors) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, URISyntaxException, HttpException, IOException {
		User user = (User) request.getAttribute(User.USER_KEY);
		for(DoctorReference doctor : doctors) {
			DoctorReferenceId id = doctorReferenceIdMapped(doctor.getId());
			if (id != null) {
				EhProfile ehProfile = userProfileLoader.getProfileByStaffIds(id.getStaffId(), id.getCompanyId(), id.getResourceId(), user.getAccessToken());
				if(ehProfile!=null){
					doctor.setUsername(ehProfile.getUsername());
				}
			} else {
				LOGGER.error("Cannot transform the id : " + doctor.getId() + " to a regular DoctorReferenceId");
			}
		}
	}
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	@Override
	public Object handleDataException(DataException dataException) {
		GenericResponsePayload genericResponsePayload = new GenericResponsePayload();
		String message = DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE;
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		genericResponsePayload.setMessage(message);
		return new ResponseEntity<>(genericResponsePayload, httpStatus);
	}
	
	private DoctorReferenceId doctorReferenceIdMapped(String id){
		if(id==null || id.isEmpty() || !id.matches("([a-zA-Z])+/(\\d)+/(\\d)+")){
			return null;
		}
		List<String> idSplitted = Arrays.asList(id.split("/"));
		return new DoctorReferenceId(idSplitted.get(2), Integer.parseInt(idSplitted.get(1)), idSplitted.get(0).equals("Physician")?1:2);
	}
}
