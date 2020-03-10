package net.fluance.cockpit.app.web.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogService;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.app.web.util.exceptions.NotFoundException;
import net.fluance.cockpit.app.config.WebConfig;
import net.fluance.cockpit.app.service.domain.CompanyService;
import net.fluance.cockpit.app.service.domain.PatientFilesService;
import net.fluance.cockpit.app.service.domain.PatientService;
import net.fluance.cockpit.app.service.domain.PhysicianListService;
import net.fluance.cockpit.app.service.domain.VisitService;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.CompanyReference;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.FileTemplate;
import net.fluance.cockpit.core.model.jdbc.company.CompanyDetails;
import net.fluance.cockpit.core.model.jdbc.patientfiles.PatientFile;
import net.fluance.cockpit.core.model.jdbc.physician.PhysicianContact;
import net.fluance.cockpit.core.model.jdbc.physician.PhysicianList;
import net.fluance.cockpit.core.model.jdbc.visit.VisitDetail;
import net.fluance.cockpit.core.model.jdbc.visit.VisitPolicyDetail;
import net.fluance.cockpit.core.model.wrap.patient.Patient;
import net.fluance.cockpit.core.repository.jdbc.patientfiles.PatientFileRepository;
import net.fluance.cockpit.core.repository.jdbc.physician.PhysicianContactRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitPolicyDetailRepository;

@RestController
@RequestMapping(WebConfig.API_MAIN_URI_PATIENTS)
public class PatientFilesController extends AbstractRestController {

	private static Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private PatientFileRepository patientFileRepository;
	@Autowired
	private LogService patientAccessLogService;
	@Autowired
	private PatientFilesService patientFilesService;
	@Autowired
	private PhysicianContactRepository physicianContactRepository; 
	
	@Autowired
	PatientService patientService;
	@Autowired
	CompanyService companyService;
	@Autowired
	VisitService visitService;
	@Autowired
	PhysicianListService physicianListService;
	
	@Autowired
	VisitPolicyDetailRepository visitPolicyDetailRepository;	
	
	@Value("${patientfiles.datamatrix.workDirectory}")
	private String patientFilesTmpDirectory;
	

	@ApiOperation(value = "Get Patient Files", response = PatientFile.class, responseContainer = "List", tags = "Patient Files")
	@RequestMapping(value = "/{pid}/files", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getFiles(@PathVariable Long pid, @RequestParam(required = false) Integer companyid, @RequestParam(required = false, defaultValue = "creationdate") String orderby,
			@RequestParam(required = false, defaultValue = "desc") String sortorder, @RequestParam(required = false, defaultValue = "50") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset,
			HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("Fetching Files of PID = " + pid);
		try {
			List<PatientFile> files = patientFilesService.getPatientFiles(pid, companyid, orderby, sortorder, limit, offset);
			patientAccessLogService.log(MwAppResourceType.PATIENT_FILES_LIST, null, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), pid, null, request.getRequestURI(), null);
			return new ResponseEntity<>(files, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Get Patient Files COUNT", response = Count.class, tags = "Patient Files")
	@RequestMapping(value = "/{pid}/files/count", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getFilesCount(@PathVariable Long pid, @RequestParam(required = false) Integer companyid, HttpServletRequest request, HttpServletResponse response){
		LOGGER.info("Counting Files of PID = " + pid);
		try {
			int count = patientFilesService.getPatientFilesCount(pid, companyid);
			return new ResponseEntity<>(new Count(count), HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Get Companies feature enabled for", response = CompanyReference.class, responseContainer = "List", tags = "Patient Files")
	@RequestMapping(value = "/files-companies", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getCompanies(@RequestParam(required = false) Long pid, HttpServletRequest request, HttpServletResponse response) {
		LOGGER.info("Fetching companies those feature is enabled for (patientFiles)");
		try {
			List<CompanyReference> companies = patientFilesService.getPatientFilesCompanies(pid);
			return new ResponseEntity<>(companies, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Get Binary Patient File", tags = "Patient Files", produces=MediaType.APPLICATION_PDF_VALUE)
	@RequestMapping(value = "/{pid}/files/{fileId}", method = RequestMethod.GET, produces=MediaType.APPLICATION_PDF_VALUE)
	@ResponseBody
	public ResponseEntity<?> getFile(@PathVariable Long pid, @PathVariable Long fileId, HttpServletRequest request){
		try {
			PatientFile patientFile = patientFileRepository.findOne(fileId);
			//TODO: move this in the service
			if (patientFile == null){
				throw new NotFoundException();
			} else if (pid != patientFile.getPid()){
				throw new IllegalArgumentException("Patient ID : " + pid + " doesn't match the Patient associated with the requested File, PID = " + patientFile.getPid());
			} else {
				Resource file = patientFilesService.getPatientFileBinary(patientFile);
				patientAccessLogService.log(MwAppResourceType.PATIENT_FILE, null, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), pid, null, request.getRequestURI(), fileId.toString());
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + patientFile.getFileName() + "\"").body(file);
			}
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	/**
	 * Returns a PDF file with the Patient Data
	 * @param pid
	 * @param companyid
	 * @param templateid
	 * @param withdatamatrix
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "Generata Data Patient PDF", produces=MediaType.APPLICATION_PDF_VALUE, tags = "Patient Files")
	@GetMapping(value = "/{pid}/pdf/generate")
	public @ResponseBody ResponseEntity<?> generatePDF(
		@PathVariable Long pid, 
		@RequestParam Long companyid,
		@RequestParam Long visitnb, 
		@RequestParam Integer templateid, 
		@RequestParam(required=false, defaultValue="true") Boolean withdatamatrix, 
		HttpServletRequest request, 
		HttpServletResponse response
	) throws Exception {
		//TODO: move this in the service
		try{
			if(withdatamatrix == null) {
				return new ResponseEntity<>("Include Matrix parameter cannot be null", HttpStatus.BAD_REQUEST);
			}
			
			Patient patient = patientService.patientDetail(pid);
			if(patient == null || patient.getPatientInfo() == null) {
				throw new SQLException("Not Patient with this ID");
			}
			
			CompanyDetails companyDetails = companyService.find(companyid);
			if(companyDetails == null || companyDetails.getId() == null) {
				throw new SQLException("Not Company with this ID");
			}
			
			VisitDetail visitDetail = new VisitDetail();
			visitDetail = visitService.findVisitDetail(visitnb);
			if(!visitDetail.getPatientId().equals(pid)){
				throw new IllegalArgumentException("The Patient is not the consistent for the Visit");
			}
			if(!visitDetail.getCompany().getCompanyId().equals(new Integer(companyid.intValue()))){
				throw new IllegalArgumentException("The Company is not the consistent for the Visit");
			}
			
			List<PhysicianList> physicianList = new ArrayList<PhysicianList>();
			physicianList = physicianListService.findPhysicianByVisitNb(visitnb);
			if(physicianList == null || physicianList.isEmpty()){
				throw new IllegalArgumentException("The Visit has not any Physician associated");
			}
			
			List<PhysicianContact> physicianContactList = new ArrayList<PhysicianContact>();
			physicianContactList = physicianContactRepository.findTelephonesById(physicianList.get(0).getAdmittingPhysicianId());
			if(physicianContactList == null || physicianContactList.isEmpty()){
				physicianContactList.add(new PhysicianContact(null, "telephone", ""));
			}
			
			List<VisitPolicyDetail> visitPoliciesDetail = visitPolicyDetailRepository.findByVisit(visitnb);
			if(visitPoliciesDetail == null || visitPoliciesDetail.isEmpty()) {
				throw new IllegalArgumentException("The Visit has not any Policy associated");
			}
			
			String finalFileName = patientFilesService.generatePdf(patient, companyDetails, visitDetail, physicianList.get(0), physicianContactList.get(0), visitPoliciesDetail, templateid, withdatamatrix);
			ResourceLoader resourceLoader = new DefaultResourceLoader();
			Resource file = resourceLoader.getResource("file:" + patientFilesTmpDirectory + finalFileName);
			
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+finalFileName+"\"").contentType(MediaType.APPLICATION_PDF).body(file);
			
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	/**
	 * Get a {@link List} with all the available Templates with are assigned to the Company ID
	 * @param request
	 * @param response
	 * @param companyId
	 * @return The {@link List} of available {@link FileTemplate}
	 * @throws IOException
	 */
	@ApiOperation(value = "Get Files Templates", response = List.class, tags = "Patient API")
	@RequestMapping(value = "/files-templates", method = RequestMethod.GET)
	public ResponseEntity<?> getFilesTemplate(
		HttpServletRequest request, 
		HttpServletResponse response, 
		@RequestParam(value="companyid") Long companyId
	) throws IOException {
		
		getLogger().info("$> " + "Recovering the Templates");
		
		try{
			List<FileTemplate> fileTemplates = patientFilesService.getFileTemplates(companyId);
			
			return new ResponseEntity<>(fileTemplates, HttpStatus.OK);
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
