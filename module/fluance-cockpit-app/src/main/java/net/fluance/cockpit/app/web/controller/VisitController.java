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
import org.springframework.web.bind.annotation.GetMapping;
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
import net.fluance.cockpit.app.service.domain.InterventionService;
import net.fluance.cockpit.app.service.domain.VisitService;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.VisitDetailed;
import net.fluance.cockpit.core.model.jdbc.guarantor.GuarantorDetail;
import net.fluance.cockpit.core.model.jdbc.intervention.Intervention;
import net.fluance.cockpit.core.model.jdbc.physician.PhysicianList;
import net.fluance.cockpit.core.model.jdbc.visit.Diagnosis;
import net.fluance.cockpit.core.model.jdbc.visit.GuarantorList;
import net.fluance.cockpit.core.model.jdbc.visit.Treatment;
import net.fluance.cockpit.core.model.jdbc.visit.VisitDetail;
import net.fluance.cockpit.core.model.jdbc.visit.VisitList;
import net.fluance.cockpit.core.model.jdbc.visit.VisitPolicyDetail;
import net.fluance.cockpit.core.model.jdbc.visit.VisitPolicyList;
import net.fluance.cockpit.core.repository.jdbc.guarantor.GuarantorRepository;
import net.fluance.cockpit.core.repository.jdbc.physician.PhysicianListRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.DiagnosisRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.TreatmentRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitPolicyDetailRepository;

@RestController
@RequestMapping(WebConfig.API_MAIN_URI_VISITS)
public class VisitController extends AbstractRestController {

	private static Logger LOGGER = LogManager.getLogger(VisitController.class);
	
	@Autowired
	private VisitService visitService;
	
	@Autowired
	private InterventionService interventionService;

	@Autowired
	private TreatmentRepository treatmentRepository;
	
	@Autowired
	private DiagnosisRepository diagnosisRepository;
	
	@Autowired
	private VisitDetailRepository visitDetailRepository;
	
	@Autowired
	private VisitPolicyDetailRepository visitPolicyDetailRepository;
	
	@Autowired
	PhysicianListRepository physicianListRepository;
	
	@Autowired
	private GuarantorRepository guarantorRepository;
	
	@Autowired
	private LogService patientAccessLogService;
	
	@ApiOperation(value = "Treatment List", response = Treatment.class,  responseContainer = "list", tags = "Visit API")
	@RequestMapping(value = "/{visitid}/treatments", method = RequestMethod.GET)
	public ResponseEntity<?> getTreatmentListByVisitid(@PathVariable Integer visitid, @RequestParam(required=false) String lang, @RequestParam(required=false) Integer limit, @RequestParam(required=false, defaultValue="0") Integer offset, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing getTreatmentListByvisitid...");
		getLogger().debug("Parameters : visitid = " + visitid);
		try{
			List<Treatment> treatments = treatmentRepository.findTreatments(visitid, lang, limit, offset);
			patientAccessLogService.log(MwAppResourceType.DIAGNOSIS_TREATMENTS, treatments, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), null, new Long(visitid.toString()), request.getRequestURI(), null);
			return new ResponseEntity<>(treatments, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Treatment List Count", response = Count.class, tags = "Visit API")
	@RequestMapping(value = "/{visitid}/treatments/count", method = RequestMethod.GET)
	public ResponseEntity<?> getTreatmentListByVisitidCount(@PathVariable Integer visitid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing getTreatmentListByvisitid...");
		getLogger().debug("Parameters : visitid = " + visitid);
		try{
			Integer count = treatmentRepository.findByVisitNbCount(visitid);
			return new ResponseEntity<>(new Count(count), HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Diagnosis List", response = Diagnosis.class,  responseContainer = "list", tags = "Visit API")
	@RequestMapping(value = "/{visitid}/diagnosis", method = RequestMethod.GET)
	public ResponseEntity<?> getDiagnosisListByVisitid(@PathVariable Integer visitid, @RequestParam(required=false) String lang, @RequestParam(required=false) Integer limit, @RequestParam(required=false, defaultValue="0") Integer offset, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing getDiagnosisListByvisitid...");
		getLogger().debug("Parameters : visitid = " + visitid);
		try {
			List<Diagnosis> diagnosis = diagnosisRepository.findDiagnosis(visitid, lang, limit, offset);
			patientAccessLogService.log(MwAppResourceType.DIAGNOSIS_TREATMENTS, diagnosis, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), null, new Long(visitid.toString()), request.getRequestURI(), null);
			return new ResponseEntity<>(diagnosis, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Diagnosis List Count", response = Count.class, tags = "Visit API")
	@RequestMapping(value = "/{visitid}/diagnosis/count", method = RequestMethod.GET)
	public ResponseEntity<?> getDiagnosisListByVisitidCount(@PathVariable Integer visitid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing getDiagnosisListByvisitid...");
		getLogger().debug("Parameters : visitid = " + visitid);
		try {
			Integer count = diagnosisRepository.findByVisitNbCount(visitid);
			return new ResponseEntity<>(new Count(count), HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Guarantor List", response = GuarantorList.class,  responseContainer = "list", tags = "Visit API")
	@RequestMapping(value = "/{visitid}/guarantors", method = RequestMethod.GET)
	public ResponseEntity<?> getGuarantorListByVisitid(@PathVariable Integer visitid, @RequestParam(required=false) String orderby, @RequestParam(required=false) String sortorder, @RequestParam(required=false) Integer limit, @RequestParam(required=false) Integer offset, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing getGuarantorListByvisitid...");
		getLogger().debug("Parameters : visitid = " + visitid);
		try {
			if (visitid == null){
				throw new IllegalArgumentException("visitid is required");
			}
			List<GuarantorList> guarantors = visitService.findGuarantorsByVisitNb(visitid, orderby, sortorder, limit, offset);
			patientAccessLogService.log(MwAppResourceType.GUARANTOR_LIST, guarantors, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), null, new Long(visitid.toString()), request.getRequestURI(), null);
			return new ResponseEntity<>(guarantors, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Guarantors List Count", response = Count.class, tags = "Visit API")
	@RequestMapping(value = "/{visitid}/guarantors/count", method = RequestMethod.GET)
	public ResponseEntity<?> getGuarantorsListByVisitidCount(@PathVariable Long visitid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing getGuarantorListByvisitidCount...");
		getLogger().debug("Parameters : visitid = " + visitid);
		try {
			Integer count = visitService.findGuarantorsByVisitNbCount(visitid);
			return new ResponseEntity<>(new Count(count), HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Policy List", response = VisitPolicyList.class,  responseContainer = "list", tags = "Visit API")
	@RequestMapping(value = "/{visitid}/policies", method = RequestMethod.GET)
	public ResponseEntity<?> getPolicyListByVisitid(@PathVariable Integer visitid, @RequestParam(required=false) String orderby, @RequestParam(required=false) String sortorder, @RequestParam(required=false) Integer limit, @RequestParam(required=false) Integer offset, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing getPolicyListByvisitid...");
		getLogger().debug("Parameters : visitid = " + visitid);
		try {
			List<VisitPolicyList> policies = visitService.findPoliciesByVisitNb(visitid, orderby, sortorder, limit, offset);
			patientAccessLogService.log(MwAppResourceType.POLICIES, policies, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), null, new Long(visitid.toString()), request.getRequestURI(), null);
			return new ResponseEntity<>(policies, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Policies List Count", response = Count.class, tags = "Visit API")
	@RequestMapping(value = "/{visitid}/policies/count", method = RequestMethod.GET)
	public ResponseEntity<?> getPoliciesListByVisitidCount(@PathVariable Long visitid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing getPoliciesListByvisitidCount...");
		getLogger().debug("Parameters : visitid = " + visitid);
		try {
			Integer count = visitService.findGuarantorsByVisitNbCount(visitid);
			return new ResponseEntity<>(new Count(count), HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Policy Detail", response = VisitPolicyDetail.class, tags="Visit API")
	@RequestMapping(value = "/{visitid}/policies/{guarantorid}/{priority}/{subpriority}", method = RequestMethod.GET)
	public ResponseEntity<?> getPolicyDetail(@PathVariable Integer visitid, @PathVariable Integer guarantorid, @PathVariable Integer priority, @PathVariable Integer subpriority, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing getPolicyDetail...");
		getLogger().debug("Parameters : visitid = " + visitid + ", guarantorId = "+ guarantorid +", priority = "+ priority+ ", subpriority = " + subpriority);
		try {
			VisitPolicyDetail policyDetail = visitPolicyDetailRepository.findByVisitNbAndGuarantorIdAndPriorityAndSubPriority(visitid, guarantorid, priority, subpriority);
			patientAccessLogService.log(MwAppResourceType.POLICY_DETAIL, policyDetail, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), null, new Long(visitid.toString()), request.getRequestURI(), null);
			return new ResponseEntity<>(policyDetail, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Doctor List", response = PhysicianList.class,  responseContainer = "list", tags = "Visit API")
	@RequestMapping(value = "/{visitid}/doctors", method = RequestMethod.GET)
	public ResponseEntity<?> getDoctorListByVisitid(@PathVariable Integer visitid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing DoctorListByvisitid..."); 
		getLogger().debug("Parameters : visitid = " + visitid);
		try {
			List<PhysicianList> physicianLists = physicianListRepository.findPhysicianByVisitnb(visitid);
			super.systemLog(request, MwAppResourceType.DoctorListByVisitid);
			return new ResponseEntity<>(physicianLists, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Visit List", response = VisitList.class,  responseContainer = "list", tags = "Visit API")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getPatientVisitList(@RequestParam(required=false) Integer companyid, @RequestParam Integer pid, @RequestParam(required=false) boolean openvisits, @RequestParam(required=false) String orderby, @RequestParam(required=false) String sortorder, @RequestParam(required=false) Integer limit, @RequestParam(required=false) Integer offset, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing getPatientVisitList...");
		getLogger().debug("Parameters : companyId = " + companyid + ", patientId = "+ pid);
		try {
			List<VisitList> visitList = visitService.findVisitList(companyid, pid, openvisits, orderby, sortorder, limit, offset);
			patientAccessLogService.log(MwAppResourceType.VISIT_LIST, visitList, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), new Long(pid.toString()), null, request.getRequestURI(), null);
			return new ResponseEntity<>(visitList, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Visit List", response = VisitDetail.class,  responseContainer = "list", tags = "Visit API")
	@RequestMapping(value = "multiple", method = RequestMethod.GET)
	public ResponseEntity<?> getMultipleVisits(HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "visits") String visits) {
		List<VisitDetail> visitDetails = visitService.getMultipleVisits(visits);
		for(VisitDetail visitDetail : visitDetails) {
			patientAccessLogService.log(MwAppResourceType.VISIT, visitDetail, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), null,visitDetail.getNumber(), request.getRequestURI(), visitDetail.getNumber().toString());
		}
		return new ResponseEntity<>(visitDetails, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Visit Detail", response = VisitDetail.class, tags="Visit API")
	@RequestMapping(value = "/{visitid}", method = RequestMethod.GET)
	public ResponseEntity<?> getVisitDetail(@PathVariable Long visitid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing getVisitDetail...");
		getLogger().debug("Parameters : visitid = " + visitid);
		try {
			VisitDetail visitDetail = visitDetailRepository.findByNb(visitid);
			patientAccessLogService.log(MwAppResourceType.VISIT, visitDetail, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), null, new Long(visitid.toString()), request.getRequestURI(), visitid.toString());
			return new ResponseEntity<>(visitDetail, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Guarantor Details", response = GuarantorDetail.class, tags="Visit API")
	@RequestMapping(value = "/{visitid}/guarantors/{guarantorid}", method = RequestMethod.GET)
	public ResponseEntity<?> getGuarantorDetail(@PathVariable long visitid, @PathVariable Integer guarantorid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		getLogger().info("Processing getGuarantorDetail...");
		getLogger().debug("Parameters : id = " + guarantorid);
		try {
			GuarantorDetail guarantor = guarantorRepository.findOne(guarantorid);
			patientAccessLogService.log(MwAppResourceType.GUARANTOR, guarantor, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), null, visitid, request.getRequestURI(), guarantorid.toString());
			return new ResponseEntity<>(guarantor, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Intervention Detail", response = Intervention.class, tags="Intervention API", notes="Returns the Intervention related to a givent VisitNb")
	@RequestMapping(value = "/{visitnb}/intervention", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<?> getInterventionDetail(@PathVariable Long visitnb, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try{
			Intervention intervention = interventionService.getByVisitnb(visitnb);
			patientAccessLogService.log(MwAppResourceType.INTERVENTION, intervention, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), null, visitnb, request.getRequestURI(), null);
			return new ResponseEntity<>(intervention, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Get Visits By Criteria", response = VisitDetailed.class, tags="Visit API")
	@GetMapping(value = "/bycriteria")
	public ResponseEntity<?> getVisitByCriteria(
			@RequestParam(value="companyid") Integer companyId,
			@RequestParam List<String> beds,
			@RequestParam(required=false, defaultValue="0") Integer limit,
			@RequestParam(required=false, defaultValue="10") Integer offset,
			HttpServletRequest request, 
			HttpServletResponse response
	) throws IOException {
		getLogger().info("Processing getVisitByCriteria...");
		try {
			List<VisitDetailed> visitsDetaileds = visitService.getVisitByCriteria(companyId, beds, limit, offset);
			super.systemLog(request, MwAppResourceType.VISIT_DETAILED_LIST);
			return new ResponseEntity<>(visitsDetaileds, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Get Count Visits By Criteria", response = Long.class, tags="Visit API")
	@GetMapping(value = "/bycriteria/count")
	public ResponseEntity<?> getVisitByCriteriaCount(
			@RequestParam(value="companyid") Integer companyId,
			@RequestParam List<String> beds,
			HttpServletRequest request, 
			HttpServletResponse response
	) throws IOException {
		getLogger().info("Processing getVisitByCriteriaCount...");
		try {
			Long count = visitService.getVisitByCriteriaCount(companyId, beds);
			return new ResponseEntity<>(count, HttpStatus.OK);
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