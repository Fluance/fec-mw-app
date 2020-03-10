package net.fluance.cockpit.app.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.BooleanUtils;
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
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.app.web.support.payload.response.GenericResponsePayload;
import net.fluance.cockpit.app.config.WebConfig;
import net.fluance.cockpit.app.service.domain.CompanyService;
import net.fluance.cockpit.app.service.domain.VisitService;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.Capacities;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.jdbc.company.BedList;
import net.fluance.cockpit.core.model.jdbc.company.Capacity;
import net.fluance.cockpit.core.model.jdbc.company.CompaniesList;
import net.fluance.cockpit.core.model.jdbc.company.CompanyDetails;
import net.fluance.cockpit.core.model.jdbc.company.RoomList;
import net.fluance.cockpit.core.model.jdbc.company.RoomOnlyList;
import net.fluance.cockpit.core.model.jdbc.company.ServiceList;
import net.fluance.cockpit.core.model.jdbc.company.Unit;
import net.fluance.cockpit.core.model.swagger.Error;
import net.fluance.cockpit.core.repository.jdbc.company.CompaniesListRepository;
import net.fluance.cockpit.core.repository.jdbc.company.CompanyDetailsRepository;
import net.fluance.cockpit.core.repository.jdbc.company.UnitListRepository;

@RestController
@RequestMapping(WebConfig.API_MAIN_URI_COMPANIES)
public class CompanyController extends AbstractRestController {

	private static Logger LOGGER = LogManager.getLogger(CompanyController.class);
	
	private static final String DATE_FORMAT = "dd-MM-yyyy";

	@Autowired
	private CompaniesListRepository companyListRepo;

	@Autowired
	private CompanyDetailsRepository companyDetailsRepository;

	@Autowired
	private UnitListRepository unitListRepository;

	@Autowired
	private CompanyService service;
	
	@Autowired
	private VisitService visitService;
	
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "Company List", response = CompaniesList.class, responseContainer = "List", tags="Company API")
	@ApiResponses(value = {@ApiResponse(code=org.apache.http.HttpStatus.SC_NO_CONTENT, response = Error.class, message="No Content"),
			@ApiResponse(code=org.apache.http.HttpStatus.SC_BAD_REQUEST, response = Error.class, message="Bad Request"),
			@ApiResponse(code=org.apache.http.HttpStatus.SC_UNAUTHORIZED, response = Error.class, message="Unauthorized"),
			@ApiResponse(code=org.apache.http.HttpStatus.SC_FORBIDDEN, response = Error.class, message="Forbidden"),
			@ApiResponse(code=org.apache.http.HttpStatus.SC_NOT_FOUND, response = Error.class, message="Not Found"),
			@ApiResponse(code=org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, response = Error.class, message="Internal Server Error")}
			)
	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity getCompanyList(HttpServletRequest request, HttpServletResponse response){
		getLogger().info("Processing getCompanyList...");
		try{
			List<CompaniesList> companiesLists = companyListRepo.findAll();
			super.systemLog(request, MwAppResourceType.CompanyList);
			return new ResponseEntity<>(companiesLists, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "Company Detail", response = CompanyDetails.class, tags="Company API")
	@RequestMapping(value = "/{companyid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity getCompanyDetail(@PathVariable Integer companyid, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Processing getCompanyDetail...");
		getLogger().debug("Parameters : company ID = " + companyid);
		try{
			CompanyDetails company = companyDetailsRepository.findOne(companyid);
			company = service.setCompanyLogoPath(company);
			
			super.systemLog(request, MwAppResourceType.CompanyDetail, companyid.toString());
			return new ResponseEntity<>(company, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "Unit List", response = Unit.class, responseContainer = "List", tags="Company API")
	@RequestMapping(value = "/{companyid}/units", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity getUnitList(@PathVariable Integer companyid, @RequestParam(value="patientunit", required=false) List<String> patientunits, HttpServletRequest request, HttpServletResponse response){
		getLogger().info("Processing getUnitList...");
		getLogger().debug("Parameters : company ID = " + companyid);
		try{
			if(patientunits != null && !patientunits.isEmpty()){
				List<Unit> unitList = unitListRepository.findByCompanyIdWithCount(companyid, patientunits);
				super.systemLog(request, MwAppResourceType.UnitList);
				return new ResponseEntity<>(unitList, HttpStatus.OK);
			} else {
				List<Unit> unitList = unitListRepository.findByCompanyId(companyid);
				super.systemLog(request, MwAppResourceType.UnitList);
				return new ResponseEntity<>(unitList, HttpStatus.OK);
			}
		} catch (Exception e){
			return handleException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "Service List", response = ServiceList.class, responseContainer = "List", tags="Company API")
	@RequestMapping(value = "/{companyid}/services", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity getServiceList(@PathVariable Integer companyid, @RequestParam(required=false) String patientunit, @RequestParam(value="hospservice", required=false) List<String> hospServices, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Processing getServiceList...");
		getLogger().debug("Parameters : company ID = " + companyid + ",  patientunit = " + patientunit);
		try{
			List<ServiceList> servicesList = service.getServiceList(companyid, patientunit, hospServices);
			super.systemLog(request, MwAppResourceType.ServiceList);
			return new ResponseEntity<>(servicesList, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "Room List", response = RoomList.class, responseContainer = "List", tags="Company API")
	@RequestMapping(value = "/{companyid}/rooms", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity getRoomList(@PathVariable Integer companyid, @RequestParam(required=false) String patientunit, @RequestParam(required=false) String patientclass, @RequestParam(required=false) List<String> excluderooms, @RequestParam(required=false) String hospservice, @RequestParam(required=false) Boolean occupancy, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Processing getRoomList...");
		try{
			if(BooleanUtils.isFalse(occupancy)){
				List<RoomOnlyList> roomList = service.getRoomOnlyList(companyid, patientunit, hospservice, excluderooms);
				super.systemLog(request, MwAppResourceType.RoomList);
				return new ResponseEntity<>(roomList, HttpStatus.OK);
			}
			else{
				List<RoomList> roomList = service.getRoomList(companyid, patientunit, patientclass, hospservice, occupancy, excluderooms);
				super.systemLog(request, MwAppResourceType.RoomList);
				return new ResponseEntity<>(roomList, HttpStatus.OK);
			}
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@ApiOperation(value = "Room Capacities", response = Capacity.class, responseContainer = "List", tags="Company API", notes="Returns a lists with the Beds available in each Room")
	@GetMapping(value = "/{companyid}/capacity", produces = "application/json")
	public ResponseEntity<?> getCapacity(
		@PathVariable(value = "companyid") Integer companyId,
		@RequestParam(required = false, value = "patientunit") String patientUnit,
		@RequestParam(required = false, value = "hospservice") String hospService,
		@RequestParam(required = false, value = "limit", defaultValue="50") Integer limit,
		@RequestParam(required = false, value = "offset", defaultValue="0") Integer offset,
		HttpServletRequest request, HttpServletResponse response)
	{
		getLogger().info("Getting Capacities");
		try{
			Capacities capacities = new Capacities(service.getCapacity(companyId, patientUnit, hospService, limit, offset));
			super.systemLog(request, MwAppResourceType.CAPACITIES);
			return new ResponseEntity<>(capacities, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "Bed List", response = BedList.class, responseContainer = "List", tags="Company API")
	@RequestMapping(value = "/{companyid}/beds", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity getBedList(@PathVariable Integer companyid, @RequestParam String patientroom, @RequestParam(required=false) String patientunit, @RequestParam(required=false) String hospservice, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Processing getBedList...");
		try{
			List<BedList> bedList = service.getBedList(companyid, patientroom, patientunit, hospservice);
			super.systemLog(request, MwAppResourceType.BedList);
			return new ResponseEntity<>(bedList, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}
	
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "Count visits of clinic", response = Count.class, tags="Company API", notes="The expected date format is DATE_FORMAT='"+DATE_FORMAT+"'")
	@RequestMapping(value = "/{companyid}/countVisits", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity visitCountByDate(@PathVariable Integer companyid, @RequestParam @DateTimeFormat(pattern=DATE_FORMAT) Date date, @RequestParam(required = false, defaultValue = "true", name = "ishosp") Boolean isHosp, @RequestParam(required = false, defaultValue = "true", name = "isamb") Boolean isAmb, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Processing visitCountByDate...");
		try{
			Count count = new Count(visitService.visitCountByDate(companyid, date, isHosp, isAmb));
			super.systemLog(request, MwAppResourceType.VISIT_COUNT);
			return new ResponseEntity<>(count, HttpStatus.OK);
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
		GenericResponsePayload grp = new GenericResponsePayload();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		grp.setMessage(DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE);
		return new ResponseEntity<>(grp, status);
	}
}