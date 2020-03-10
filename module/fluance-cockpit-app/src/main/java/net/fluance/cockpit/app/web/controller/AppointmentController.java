package net.fluance.cockpit.app.web.controller;

import java.time.LocalDate;
import java.util.List;

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
import net.fluance.app.data.model.identity.UserProfile;
import net.fluance.app.log.LogService;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.app.web.support.payload.response.GenericResponsePayload;
import net.fluance.cockpit.app.config.WebConfig;
import net.fluance.cockpit.app.service.domain.AppointmentOperationService;
import net.fluance.cockpit.app.service.domain.AppointmentService;
import net.fluance.cockpit.app.service.domain.appointment.operation.RoomOperationsByStatusService;
import net.fluance.cockpit.app.service.domain.model.RoomOperationsByStatus;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.DayAppointments;
import net.fluance.cockpit.core.model.dto.appointment.operation.AppointmentOperationNoteDto;
import net.fluance.cockpit.core.model.dto.appointment.operation.AppointmentProcessStatusDto;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentDetail;
import net.fluance.cockpit.core.repository.jdbc.doctor.DoctorRepository;

@RestController
@RequestMapping(WebConfig.API_MAIN_URI_APPOINTMENTS)
public class AppointmentController extends AbstractRestController{

	private static Logger LOGGER = LogManager.getLogger(AppointmentController.class);
	
	private static final String DATE_FORMAT = "yyyy-MM-dd";

	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	private LogService patientAccessLogService;
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private AppointmentOperationService appointmentOperationService;
	
	@Autowired
	private RoomOperationsByStatusService roomOperationsByStatusService;

	@ApiOperation(value = "Appointment List", response = AppointmentDetail.class, responseContainer = "List", tags="Appointment API", notes="Get doctor's appointment based on StaffID(s) assiciated in the profile")
	@RequestMapping(value = "/mypatients", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getDetailedAppointmentPhysician(@RequestParam(required=false) Integer companyid, @RequestParam(required=false) String date,
			@RequestParam(required=false) String type, @RequestParam(required=false) List<String> rooms,
			@RequestParam(required=false) String orderby, @RequestParam(required=false) String sortorder,			
			@RequestParam(required=false) Integer limit, @RequestParam(required=false) Integer offset, @RequestParam(required=false) String language, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Processing getDetailedAppointmentPhysician...");
		try{
			UserProfile profile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			List<AppointmentDetail> appointments = appointmentService.getAppointmentsPhysician(companyid, profile, date, orderby, sortorder, limit, offset, language);
			super.systemLog(request, MwAppResourceType.AppointmentList);
			return new ResponseEntity<>(appointments, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Appointment List Count", response = Count.class, tags="Appointment API", notes="Count doctor's appointment based on StaffID(s) assiciated in the profile")
	@RequestMapping(value = "/mypatients/count", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getDetailedAppointmentPhysicianCount(@RequestParam(required=false) Integer companyid, @RequestParam(required=false) String date,
			@RequestParam(required=false) String type, @RequestParam(required=false) List<String> rooms,
			HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Processing getDetailedAppointmentPhysicianCount...");
		try{
			UserProfile profile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			Integer count = appointmentService.getAppointmentsPhysicianCount(companyid, profile, date);
			return new ResponseEntity<>(new Count(count), HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Appointment List", response = AppointmentDetail.class, responseContainer = "List", tags="Appointment API", notes="Get Appointments By Unit/Service/PID")
	@RequestMapping(value = "", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getDetailedAppointmentDynamicSearch(
			@RequestParam(value="companyid", required=false) Integer companyId, 
			@RequestParam(value="patientunit", required=false) List<String> patientUnits, 
			@RequestParam(value="hospservice", required=false) List<String> hospServices, 
			@RequestParam(required=false) Long pid,
			@RequestParam(value="visitnb", required=false) Long visitNumber,
			@RequestParam(required=false) String type,
			@RequestParam(required=false) List<String> rooms,
			@RequestParam(required=false, value="locationnames") List<String> locationNames,
			@RequestParam(value="from", required=false) @DateTimeFormat(pattern=DATE_FORMAT) LocalDate from, 
			@RequestParam(value="to", required=false) @DateTimeFormat(pattern=DATE_FORMAT) LocalDate to,
			@RequestParam(value="includeactive", required=false, defaultValue="false") boolean includeActive,
			@RequestParam(value="orderby", required=false) String orderBy, 
			@RequestParam(value="sortorder", required=false) String sortOrder, 
			@RequestParam(required=false) Integer limit, 
			@RequestParam(required=false) Integer offset,
			@RequestParam(required=false) String language,
			HttpServletRequest request, 
			HttpServletResponse response) {
		getLogger().info("Processing getDetailedAppointmentDynamicSearch...");
		try{
			List<AppointmentDetail> appointments = appointmentService.getAppointmentDetailsByCriteriaShortedAndPaginated(companyId, pid, visitNumber, patientUnits, hospServices, type, rooms, locationNames, includeActive, from, to, orderBy, sortOrder, limit, offset, language);
			
			if(pid != null){				
				patientAccessLogService.log(MwAppResourceType.AppointmentList, appointments, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), pid, visitNumber, request.getRequestURI(), null);
			} else if(visitNumber != null){
				super.systemLog(request, MwAppResourceType.AppointmentList);
			}
			
			return new ResponseEntity<>(appointments, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Appointment List Count", response = Count.class, tags="Appointment API", notes="Count Appointments By Unit/Service/PID")
	@RequestMapping(value = "/count", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getDetailedAppointmentDynamicSearchCount(
			@RequestParam(value="companyid", required=false) Integer companyId, 
			@RequestParam(value="patientunit", required=false) List<String> patientUnits, 
			@RequestParam(value="hospservice", required=false) List<String> hospServices, 
			@RequestParam(required=false) Long pid,
			@RequestParam(value="visitnb", required=false) Long visitNumber,
			@RequestParam(required=false) String type,
			@RequestParam(required=false) List<String> rooms,
			@RequestParam(required=false, value="locationnames") List<String> locationNames,
			@RequestParam(value="includeactive", required=false, defaultValue="false") boolean includeActive,
			@RequestParam(value="from", required=false) @DateTimeFormat(pattern=DATE_FORMAT) LocalDate from, 
			@RequestParam(value="to", required=false) @DateTimeFormat(pattern=DATE_FORMAT) LocalDate to,
			HttpServletRequest request, 
			HttpServletResponse response) {
		getLogger().info("Processing getDetailedAppointmentDynamicSearchCount...");
		try{
			Integer count = appointmentService.getAppointmentDetailsByCriteriaCount(companyId, pid, visitNumber, patientUnits, hospServices, type, rooms, locationNames, includeActive, from, to);
			return new ResponseEntity<>(new Count(count), HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Appointment Detail", response = AppointmentDetail.class, tags="Appointment API", notes="Appointment Detail By ID")
	@RequestMapping(value = "/{appointmentid}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getDetailByAppointmentId(@PathVariable Long appointmentid, @RequestParam(required=false) String language,
													  HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Processing getDetailedAppointment...");
		try{
			AppointmentDetail appointment = appointmentService.getAppointmentDetail(appointmentid, language);
			patientAccessLogService.log(MwAppResourceType.AppointmentDetail, appointment, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), null);
			return new ResponseEntity<>(appointment, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Get visits/consultations for current doctor", response = DayAppointments.class, responseContainer="List", tags="Appointment API", notes="Prototype. This Endpoint returns Mocked Data")
	@GetMapping(value = WebConfig.API_MAIN_URI_VISITS + "/doctors")
	public ResponseEntity<?> getVisitsForCurrentDoctor(@RequestParam(value="companyid", required=false) Long companyId, 
			@RequestParam(value="from", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate from, 
			@RequestParam(value="to", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate to, 
			HttpServletRequest request, HttpServletResponse response) {
		try {
			
			if(from == null){
				from = LocalDate.now();
			}
			
			if(to == null){
				to = LocalDate.now();
			}
			
			List<DayAppointments> dayAppointments = doctorRepository.getVisitsForCurrentDoctor(companyId, from, to);
						
			return new ResponseEntity<>(dayAppointments, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Appointment Operation status", response = AppointmentDetail.class, tags="Appointment API", notes="Appointment operation status")
	@RequestMapping(value = "/{appointmentId}/operation/status", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<List<AppointmentProcessStatusDto>> getAppointmentOperationProcessStatus(@PathVariable Long appointmentId, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Processing getAppointmentOperationProcessStatus...");
		
		return new ResponseEntity<>(appointmentOperationService.getOperationProcessStatusByAppointmentId(appointmentId), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get all the notes related with the appointment", response = AppointmentOperationNoteDto.class, tags="Appointment API", notes="Appointment operation note")
	@RequestMapping(value = "/{appointmentId}/operation/note", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<AppointmentOperationNoteDto> getAppointmentOperationNotes(@PathVariable Long appointmentId, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Processing getAppointmentOperationNotes...");
		
		return new ResponseEntity<>(appointmentOperationService.getOperationNoteByAppointmentId(appointmentId), HttpStatus.OK);
	}	
	
	@ApiOperation(value = "Update Operation Note related with the appointment", response = AppointmentOperationNoteDto.class, tags="Appointment API", notes="Appointment operation note")
	@RequestMapping(value = "/{appointmentId}/operation/note", method = RequestMethod.PUT)
	public ResponseEntity<?> updateNoteByAppointmentId(@PathVariable("appointmentId") Long appointmentId,
			@RequestParam(required = false, value = "note") String note,
			HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Writting the entry [appointmentId={}] from the Operation Notes", appointmentId);
		try {
			AppointmentOperationNoteDto appointmentOperationNoteDto = appointmentOperationService.updateOperationNoteByAppointmentId(appointmentId, note, (User) request.getAttribute(User.USER_KEY));			
			return new ResponseEntity<>(appointmentOperationNoteDto, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Operation gruped by status for each room", response = RoomOperationsByStatus.class, responseContainer = "List", tags="Appointment API", notes="Gets the appointments of type operation for today for every room provided grouped in \"in progress\", \"operation live\", \"upcoming\"")
	@RequestMapping(value = "operation/statusByRoom", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getOperationAppointmentsByRoomAndStatus(
			@RequestParam(value="companyid", required=false) Integer companyId, 
			@RequestParam(required=false, value="rooms") List<String> rooms, 
			HttpServletRequest request, 
			HttpServletResponse response) {
		getLogger().info("Processing getOperationAppointmentsByRoomAndStatus...");
		try{
			List<RoomOperationsByStatus> RoomsOperationsByStatus = roomOperationsByStatusService.getRoomsOperationsByStatus(companyId, rooms);
			
			return new ResponseEntity<>(RoomsOperationsByStatus, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@Override
	public Object handleDataException(DataException exc) {
		GenericResponsePayload grp = new GenericResponsePayload();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		grp.setMessage(DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE);
		return new ResponseEntity<>(grp, status);
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}

}
