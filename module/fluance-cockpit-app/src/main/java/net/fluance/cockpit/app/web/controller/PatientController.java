package net.fluance.cockpit.app.web.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.data.model.identity.EhProfile;
import net.fluance.app.data.model.identity.User;
import net.fluance.app.data.model.identity.UserProfile;
import net.fluance.app.log.LogService;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.app.web.support.payload.response.GenericResponsePayload;
import net.fluance.app.web.util.exceptions.ForbiddenException;
import net.fluance.cockpit.app.config.ProviderConfig;
import net.fluance.cockpit.app.config.WebConfig;
import net.fluance.cockpit.app.service.domain.PatientNextOfKinService;
import net.fluance.cockpit.app.service.domain.PatientService;
import net.fluance.cockpit.app.service.domain.patient.PatientExerciseService;
import net.fluance.cockpit.app.service.domain.patient.PatientWeightService;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.PatientVisitReference;
import net.fluance.cockpit.core.model.dto.PatientNextOfKinDto;
import net.fluance.cockpit.core.model.dto.patient.ExerciseDto;
import net.fluance.cockpit.core.model.dto.patient.WeightDto;
import net.fluance.cockpit.core.model.jdbc.lab.Groupname;
import net.fluance.cockpit.core.model.jdbc.lab.LabData;
import net.fluance.cockpit.core.model.jdbc.patient.PatientNextOfKin;
import net.fluance.cockpit.core.model.jdbc.patient.PatientNextOfKinContact;
import net.fluance.cockpit.core.model.jdbc.patient.byroom.RoomWithPatientsReferences;
import net.fluance.cockpit.core.model.jdbc.patient.byroom.RoomWithPatientsReferencesCount;
import net.fluance.cockpit.core.model.jdbc.radiology.Radiology;
import net.fluance.cockpit.core.model.wrap.patient.Patient;
import net.fluance.cockpit.core.model.wrap.patient.PatientAdmitDate;
import net.fluance.cockpit.core.model.wrap.patient.PatientInList;
import net.fluance.cockpit.core.model.wrap.patient.PatientsCount;
import net.fluance.cockpit.core.repository.jdbc.lab.GroupnameRepository;
import net.fluance.cockpit.core.repository.jdbc.lab.LabDataRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientNextOfKinContactRepository;
import net.fluance.cockpit.core.repository.jdbc.radiology.RadiologyRepository;
import net.fluance.cockpit.core.util.DateUtils;

@RestController
@RequestMapping(WebConfig.API_MAIN_URI_PATIENTS)
public class PatientController extends AbstractRestController {

	private static Logger LOGGER = LogManager.getLogger(PatientController.class);
	@Autowired
	private PatientService patientService;
	@Autowired
	private PatientNextOfKinContactRepository patientNextOfKinContactRepository;
	@Autowired
	private GroupnameRepository groupNameRepository;
	@Autowired
	private LabDataRepository labDataRepository;
	@Autowired
	private RadiologyRepository radiologyRepository;
	@Autowired
	private ProviderConfig providerConfig;
	@Autowired
	private LogService patientAccessLogService;
	@Autowired
	PatientExerciseService patientExceriseService;
	@Autowired
	PatientWeightService patientWeightService;
	@Autowired
	PatientNextOfKinService patientNextOfKinService;

	@ApiOperation(value = "Patient Details", response = Patient.class, responseContainer = "List", tags = "Patient API")
	@RequestMapping(value = "/{pid}", method = RequestMethod.GET)
	public ResponseEntity<?> getPatientDetails(@PathVariable Long pid, HttpServletRequest request, HttpServletResponse response) {
		getLogger().debug("Parameters : pid = " + pid);
		try {
			Patient patientDetails = patientService.patientDetail(pid);
			if (patientDetails == null) {
				GenericResponsePayload grp = new GenericResponsePayload();
				grp.setMessage("Not Found");
				return new ResponseEntity<>(grp, HttpStatus.NOT_FOUND);
			}
			patientAccessLogService.log(MwAppResourceType.PATIENT_DETAIL, patientDetails, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), pid.toString());
			return new ResponseEntity<>(patientDetails, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Patient Details", response = Patient.class, responseContainer = "List", tags = "Patient API")
	@RequestMapping(value = "/multiple", method = RequestMethod.GET)
	public ResponseEntity<?> getMultipleVisits(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "pids") String pids) {
		List<Patient> patients = patientService.getPatients(pids);
		for (Patient patient : patients) {
			patientAccessLogService.log(MwAppResourceType.PATIENT_DETAIL, patient, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(),
					patient.getPatientInfo().getPid().toString());
		}
		return new ResponseEntity<>(patients, HttpStatus.OK);
	}

	@RequestMapping(value = "/{pid}/overview", method = RequestMethod.GET)
	public ResponseEntity<?> getPatientOverview(@PathVariable Long pid, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Processing getPatientOverview...");
		getLogger().debug("Parameters : pid = " + pid);
		try {
			List<?> patients = patientService.patientsByCriteria(false, null, null, null, null, pid, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
			patientAccessLogService.log(MwAppResourceType.PATIENT_DETAIL, patients, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), pid.toString());
			return new ResponseEntity<>(patients, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Next of Kin List", response = PatientNextOfKin.class, responseContainer = "List", tags = "Patient API", notes = "Returns a List of the Kins of the Patient of the given Patient ID")
	@RequestMapping(value = "{pid}/noks", method = RequestMethod.GET)
	public ResponseEntity<?> getNoksByPid(@PathVariable("pid") Long pid, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving next of kin list for patient " + pid + "...");
		try {
			List<PatientNextOfKinDto> patientNextOfKins = patientNextOfKinService.getNoksByPid(pid);
			super.systemLog(request, MwAppResourceType.NoksByPid);
			return new ResponseEntity<>(patientNextOfKins, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	/**
	 * @deprecated the PathVariable are required and mandatory, but we never use it, please use the new method with only
	 *             one PathVariable "nokid"
	 */
	@Deprecated
	@ApiOperation(value = "Next of Kin Contact", response = PatientNextOfKinContact.class, responseContainer = "List", tags = "Patient API")
	@RequestMapping(value = "{pid}/noks/{nokid}/contacts", method = RequestMethod.GET)
	public ResponseEntity<?> getNoksContacts(@PathVariable("pid") Long pid, @PathVariable("nokid") Long nokId, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving contacts of next of kin " + nokId + "...");
		try {
			List<PatientNextOfKinContact> nokContactList = patientNextOfKinContactRepository.findByNokId(nokId);
			super.systemLog(request, MwAppResourceType.NoksContacts, nokId.toString());
			return new ResponseEntity<>(nokContactList, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Next of Kin Contact", response = PatientNextOfKinContact.class, responseContainer = "List", tags = "Patient API")
	@RequestMapping(value = "noks/{nokid}/contacts", method = RequestMethod.GET)
	public ResponseEntity<?> getNoksContacts(@PathVariable("nokid") Long nokId, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving contacts of next of kin " + nokId + "...");
		try {
			List<PatientNextOfKinContact> nokContactList = patientNextOfKinContactRepository.findByNokId(nokId);
			super.systemLog(request, MwAppResourceType.NoksContacts, nokId.toString());
			return new ResponseEntity<>(nokContactList, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Patient List", response = PatientInList.class, responseContainer = "List", tags = "Patient API")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getPatientsByCriteria(@RequestParam(required = false) boolean byphysician, @RequestParam(required = false) String patientview, @RequestParam(required = false) Long pid,
			@RequestParam(required = false) Integer companyid, @RequestParam(required = false) Integer visitnb, @RequestParam(required = false) String name, @RequestParam(required = false) String firstname,
			@RequestParam(required = false) String sex, @RequestParam(required = false) String admitdt, @RequestParam(required = false) String birthdate, @RequestParam(required = false) String patientunit,
			@RequestParam(required = false) String hospservice, @RequestParam(required = false) String patientroom, @RequestParam(required = false) String patientbed, @RequestParam(required = false) Integer limit,
			@RequestParam(required = false) Integer offset, @RequestParam(required = false) String orderby, @RequestParam(required = false) String sortorder, @RequestParam(required = false) String admissionstatus,
			@RequestParam(required = false) String patientclass,@RequestParam(required = false) String telephone, @RequestParam(value = "email", required = false) String email, HttpServletRequest request, HttpServletResponse response) {
		try {
			String staffid = "";
			List<String> staffIds = new ArrayList<>();
			List<Integer> companyIds = new ArrayList<>();
			if (byphysician) {
				UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
				EhProfile profile = userProfile.getProfile();
				if (companyid != null) {
					staffid = profile.getStaffId(companyid, providerConfig.getProviderOpalId());
				} else {
					staffIds = profile.getStaffIds(providerConfig.getProviderOpalId());
					companyIds = profile.getGrantedCompanies(providerConfig.getProviderOpalId());
				}
				if ((staffid == null || staffid.isEmpty()) && (staffIds == null || staffIds.isEmpty())) {
					throw new ForbiddenException(ForbiddenException.NO_STAFF_ID);
				}
			}
			List<?> patients = patientService.patientsByCriteria(byphysician, staffid, staffIds, companyIds, patientview, pid, companyid, visitnb, name, firstname, sex, admitdt, birthdate, patientunit, hospservice, patientroom,
					patientbed, limit, offset, orderby, sortorder, admissionstatus, patientclass, telephone, email);
			super.systemLog(request, MwAppResourceType.PATIENT_LIST);
			return new ResponseEntity<>(patients, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "", response = Groupname.class, responseContainer = "List", tags = "Lab API")
	@RequestMapping(value = "/{pid}/lab/groupnames", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getLabGroupNames(@PathVariable Long pid, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving group name list for patient " + pid + "...");
		getLogger().debug("Patient ID = " + pid);
		try {
			List<Groupname> groupNameList = groupNameRepository.findByPid(pid);
			super.systemLog(request, MwAppResourceType.LabGroupNames);
			return new ResponseEntity<>(groupNameList, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Lab Data", response = LabData.class, responseContainer = "List", tags = "Lab API")
	@RequestMapping(value = "/{pid}/lab", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getDataByPidAndGroupName(@PathVariable Long pid, @RequestParam(name = "groupname") String groupName, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving lab data for patient " + pid + " and group name " + groupName + "...");
		try {
			List<LabData> labDataList = labDataRepository.findByPidAndGroupName(pid, groupName);
			patientAccessLogService.log(MwAppResourceType.LAB, labDataList, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), pid, null, request.getRequestURI(), null);
			return new ResponseEntity<>(labDataList, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Lab Data Count", response = Count.class, tags = "Lab API")
	@RequestMapping(value = "/{pid}/lab/count", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getDataByPidCount(@PathVariable Long pid, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving lab data COUNT for patient " + pid);
		try {
			Integer count = labDataRepository.findByPidCount(pid);
			return new ResponseEntity<>(new Count(count), HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Radiology List", response = Radiology.class, responseContainer = "list", tags = "Radiology API")
	@RequestMapping(value = "/{pid}/radiology", method = RequestMethod.GET)
	public ResponseEntity<?> getExamsByPid(@PathVariable Long pid, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving exam list for patient " + pid + "...");
		try {
			List<Radiology> examList = radiologyRepository.findByPatientId(pid);
			patientAccessLogService.log(MwAppResourceType.RADIOLOGY_EXAMS, examList, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), pid, null, request.getRequestURI(), null);
			return new ResponseEntity<>(examList, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Radiology List Count", response = Count.class, tags = "Radiology API")
	@RequestMapping(value = "/{pid}/radiology/count", method = RequestMethod.GET)
	public ResponseEntity<?> getExamsByPidCount(@PathVariable Long pid, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving exam list COUNT for patient " + pid + "...");
		try {
			Integer count = radiologyRepository.findByPatientIdCount(pid);
			return new ResponseEntity<>(new Count(count), HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Patients by Rooms", response = RoomWithPatientsReferences.class, responseContainer = "List", tags = "Patient API", notes = "Set patientdetails=true changes the payload for every patient and return patientinfo and visit")
	@RequestMapping(value = "/byrooms", method = RequestMethod.GET)
	public ResponseEntity<?> getPatientsByRooms(@RequestParam Integer companyid, @RequestParam(required = false) String hospservice, @RequestParam(required = false) String patientunit, @RequestParam(required = false) String patientclass,
			@RequestParam(required = false, defaultValue = "5") Integer maxpatientsbyroom, @RequestParam(required = false) String sortorder, @RequestParam(required = false, defaultValue = "50") Integer limit,
			@RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(required = false, defaultValue = "false") Boolean patientdetails, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Processing getPatientsByRooms...");
		getLogger().debug("Parameters : companyId = " + companyid);
		try {
			List<?> roomsWithPatients;
			if (patientdetails) {
				roomsWithPatients = patientService.roomsWithPatientsDetails(companyid, hospservice, patientclass, patientunit, sortorder, limit, offset, maxpatientsbyroom);
			} else {
				roomsWithPatients = patientService.roomsWithPatientsReferences(companyid, hospservice, patientclass, patientunit, sortorder, limit, offset, maxpatientsbyroom);
			}
			return new ResponseEntity<>(roomsWithPatients, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Patients by Rooms count", response = RoomWithPatientsReferencesCount.class, responseContainer = "List", tags = "Patient API")
	@RequestMapping(value = "/byrooms/count", method = RequestMethod.GET)
	public ResponseEntity<?> getPatientsByRoomsCount(@RequestParam Integer companyid, @RequestParam(required = false) String hospservice, @RequestParam(required = false) String patientunit,
			@RequestParam(required = false) String patientclass, @RequestParam(required = false) String sortorder, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Processing getPatientsByRoomsCount...");
		getLogger().debug("Parameters : companyId = " + companyid);
		try {
			List<RoomWithPatientsReferencesCount> roomsWithPatientsReferences = patientService.roomsWithPatientsReferencesCount(companyid, hospservice, patientclass, patientunit, sortorder);
			return new ResponseEntity<>(roomsWithPatientsReferences, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Patient List Count By Name", response = PatientInList.class, responseContainer = "List", tags = "Patient API")
	@RequestMapping(value = "/countbyname", method = RequestMethod.GET)
	public ResponseEntity<?> getPatientsCount(@RequestParam(required = false) boolean byphysician, @RequestParam(required = false) String patientview, @RequestParam(required = false) Long pid,
			@RequestParam(required = false) Integer companyid, @RequestParam(required = false) Integer visitnb, @RequestParam(required = false) String name, @RequestParam(required = false) String firstname,
			@RequestParam(required = false) String sex, @RequestParam(required = false) String admitdt, @RequestParam(required = false) String birthdate, @RequestParam(required = false) String patientunit,
			@RequestParam(required = false) String hospservice, @RequestParam(required = false) String patientroom, @RequestParam(required = false) String patientbed, @RequestParam(required = false) String admissionstatus,
			@RequestParam(required = false) String patientclass, @RequestParam(value = "email", required = false) String email, HttpServletRequest request, HttpServletResponse response) {
		try {
			List<PatientsCount> patientsCount = patientService.patientsCount(patientview, pid, companyid, visitnb, name, firstname, sex, admitdt, birthdate, patientunit, hospservice, patientroom, patientbed, admissionstatus, patientclass, email);
			super.systemLog(request, MwAppResourceType.PATIENTS_COUNT);
			return new ResponseEntity<>(patientsCount, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Patient Count By Admit Date", response = PatientAdmitDate.class, responseContainer = "List", tags = "Patient API")
	@RequestMapping(value = "/{companyid}/countbyadmitdate", method = RequestMethod.GET)
	public ResponseEntity<?> getPatientsCountByDate(@PathVariable Integer companyid, @RequestParam(required = false) String patientunit, @RequestParam(required = false) String hospservice,
			@RequestParam(required = false) String patientclass, HttpServletRequest request, HttpServletResponse response) {
		try {
			List<PatientAdmitDate> patientsByAdminDate = patientService.patientsCountByAdmitDate(companyid, patientunit, hospservice, patientclass);
			super.systemLog(request, MwAppResourceType.PATIENTS_COUNT_BY_ADMIN_DATE);
			return new ResponseEntity<>(patientsByAdminDate, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Get Patients for current doctor", response = PatientVisitReference.class, responseContainer = "List", tags = "Patient API", notes = "By default all type of patients for the doctor requesting. \n Based on Staffid(s) for the given clinic (Attending, admiting, consulting or referring). \n If patientclass=I then only In Patients, if patientclass=O then only Out Patients")
	@RequestMapping(value = "/ofdoctor", method = RequestMethod.GET)
	public ResponseEntity<?> getPatientsForCurrentDoctor(@RequestParam Integer companyid, @RequestParam(required = false) String patientclass, @RequestParam(required = false, defaultValue = "50") Integer limit,
			@RequestParam(required = false, defaultValue = "0") Integer offset, HttpServletRequest request, HttpServletResponse response) {
		try {
			String staffid = "";
			List<String> staffIds = new ArrayList<>();
			List<Integer> companyIds = new ArrayList<>();
			UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			EhProfile profile = userProfile.getProfile();
			if (companyid != null) {
				staffid = profile.getStaffId(companyid, providerConfig.getProviderOpalId());
			} else {
				staffIds = profile.getStaffIds(providerConfig.getProviderOpalId());
				companyIds = profile.getGrantedCompanies(providerConfig.getProviderOpalId());
			}
			if ((staffid == null || staffid.isEmpty()) && (staffIds == null || staffIds.isEmpty())) {
				throw new ForbiddenException(ForbiddenException.NO_STAFF_ID);
			}
			List<PatientVisitReference> patients = patientService.patientsForDoctor(staffid, staffIds, companyIds, companyid, patientclass, limit, offset);
			return new ResponseEntity<>(patients, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Get exercises for patient", response = ExerciseDto.class, responseContainer = "List", tags = "Patient API")
	@RequestMapping(value = { "/{pid}/exercises" }, method = RequestMethod.GET)
	public ResponseEntity<?> getExercisesForPatient(
			@PathVariable(value = "pid") Long pid,
			@RequestParam(value = "from", required=false) @DateTimeFormat(pattern=DateUtils.DATE_FORMAT) LocalDate from,
			@RequestParam(value = "to", required=false) @DateTimeFormat(pattern=DateUtils.DATE_FORMAT) LocalDate to,
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			HttpServletRequest request, HttpServletResponse response
	) {
		try {
			Page<ExerciseDto> exercises = patientExceriseService.getExercisesForPatient(pid, from, to, page);
			return new ResponseEntity<>(exercises, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Save exercises for patient", response = ExerciseDto.class, tags = "Patient API" )
	@RequestMapping(value = { "/{pid}/exercises" }, method = RequestMethod.POST)
	public ResponseEntity<?> saveExerciseForPatient(
			@PathVariable("pid") Long pid,
			@RequestBody ExerciseDto exercise,
			HttpServletRequest request, HttpServletResponse response
	) {
		try {
			ExerciseDto exerciseDto = patientExceriseService.saveExerciseForPatient(pid, exercise);
			return new ResponseEntity<>(exerciseDto, HttpStatus.CREATED);
		} catch (org.springframework.dao.DataIntegrityViolationException de) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.TEXT_PLAIN).body("Patient '" + pid + "' does not exist");
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Delete exercise for a patient", tags = "Patient API")
	@DeleteMapping(value = { "/{pid}/exercises/{exercise_id}" })
	public ResponseEntity<?> deleteExerciseForPatient(
			@PathVariable(value = "pid") Long pid,
			@PathVariable(value = "exercise_id") Long exerciseId,
			HttpServletRequest request, HttpServletResponse response
	) {
		try {
			patientExceriseService.delete(exerciseId);
			patientAccessLogService.log(MwAppResourceType.PATIENT_EXERCISE, null, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), exerciseId.toString());
			return new ResponseEntity<GenericResponsePayload>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Get weight for patient", response = WeightDto.class, responseContainer = "List", tags = "Patient API")
	@RequestMapping(value = "/{pid}/weights", method = RequestMethod.GET)
	public ResponseEntity<?> getWeightsForPatient(
			@PathVariable("pid") Long pid,
			@RequestParam(value = "from", required=false) @DateTimeFormat(pattern=DateUtils.DATE_FORMAT) LocalDate from,
			@RequestParam(value = "to", required=false) @DateTimeFormat(pattern=DateUtils.DATE_FORMAT) LocalDate to,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			HttpServletRequest request, HttpServletResponse response
	) {
		try {
			Page<WeightDto> weights = patientWeightService.getWeightsForPatient(pid, from, to, page);
			return new ResponseEntity<>(weights, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Save weight for patient", response = WeightDto.class, tags = "Patient API")
	@RequestMapping(value = "/{pid}/weights", method = RequestMethod.POST)
	public ResponseEntity<?> saveWeightsForPatient(
			@PathVariable("pid") Long pid, 
			@RequestBody WeightDto weight,
			HttpServletRequest request, HttpServletResponse response
	) {
		try {
			WeightDto weightDto = patientWeightService.saveWeightForPatient(pid, weight);
			return new ResponseEntity<>(weightDto, HttpStatus.CREATED);
		} catch (org.springframework.dao.DataIntegrityViolationException de) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.TEXT_PLAIN).body("Patient '" + pid + "' does not exist");
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Delete weight for a patient", tags = "Patient API")
	@DeleteMapping(value = { "/{pid}/weights/{weight_id}" })
	public ResponseEntity<?> deleteWeightForPatient(
			@PathVariable(value = "pid") Long pid,
			@PathVariable(value = "weight_id") Long weightId,
			HttpServletRequest request, HttpServletResponse response
	) {
		try {
			patientWeightService.delete(weightId);
			patientAccessLogService.log(MwAppResourceType.PATIENT_WEIGHT, null, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), weightId.toString());
			return new ResponseEntity<GenericResponsePayload>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	public Logger getLogger() {
		return LOGGER;
	}

	@Override
	public Object handleDataException(DataException exc) {
		GenericResponsePayload grp = new GenericResponsePayload();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		grp.setMessage(DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE);
		return new ResponseEntity<>(grp, status);
	}
}
