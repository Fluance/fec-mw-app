package net.fluance.cockpit.app.web.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogService;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.app.web.support.payload.response.GenericResponsePayload;
import net.fluance.cockpit.app.config.WebConfig;
import net.fluance.cockpit.app.service.domain.WhiteBoardFilesService;
import net.fluance.cockpit.app.service.domain.WhiteBoardService;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.jpa.catalog.CatalogDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardEntryDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteboardRoomsConfigurationDTO;
import net.fluance.cockpit.core.util.DateUtils;

@RestController
@RequestMapping(WebConfig.API_MAIN_URI_WHITEBOARD + "/companies/{companyId}/services")
public class WhiteBoardController extends AbstractRestController {
	public static final String DEFAULT_LIMIT = "10";

	private static final Logger LOGGER = LogManager.getLogger(WhiteBoardController.class);

	private static final String DEFAULT_SORT_ORDER = "ASC";
	private static final String DEFAULT_ORDER_BY = "patientroom,patientbed";

	@Autowired
	WhiteBoardService whiteBoardService;

	@Autowired
	private LogService patientAccessLogService;

	@Autowired
	private WhiteBoardFilesService whiteBoardFilesService;

	@ApiOperation(value = "Check if Whiteboard Feature is enabled", response = Boolean.class, tags = "Whiteboard API")
	@GetMapping(value = "/{serviceId}/enabled")
	public ResponseEntity<?> checkPatientListEnabled(@PathVariable("companyId") Long companyId,
			@PathVariable("serviceId") String serviceId, HttpServletRequest request, HttpServletResponse response) {
		super.systemLog(request, MwAppResourceType.WHITEBOARD);
		try {
			return new ResponseEntity<>(whiteBoardService.isFeatureEnabled(companyId.intValue(), serviceId),
					HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Returns a collection of service where the Whiteboard feature is enabled for a given company", tags = "Whiteboard API")
	@GetMapping(value = "")
	public ResponseEntity<?> getEnabledServicesByCompany(@PathVariable("companyId") Long companyId,
			HttpServletRequest request, HttpServletResponse response) {
		super.systemLog(request, MwAppResourceType.WHITEBOARD);

		try {
			return new ResponseEntity<>(whiteBoardService.enabledServicesByCompany(companyId),
					HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Check if Room Config is enabled", response = Boolean.class, tags = "Whiteboard API")
	@GetMapping(value = "/{serviceId}/rooms/configuration/enabled")
	public ResponseEntity<?> checkRoomsConfigurationEnable(@PathVariable("companyId") Long companyId,
			@PathVariable("serviceId") String serviceId, HttpServletRequest request, HttpServletResponse response) {
		super.systemLog(request, MwAppResourceType.WHITEBOARD);
		try {
			return new ResponseEntity<>(whiteBoardService.isRoomsConfigEnabled(companyId.intValue(), serviceId),
					HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Get Whiteboard", response = WhiteBoardDTO.class, tags = "Whiteboard API")
	@GetMapping(path = "/{serviceId}", produces = "application/json")
	public ResponseEntity<?> getWhiteboard(@PathVariable("companyId") Long companyId,
			@PathVariable("serviceId") String serviceId,
			@RequestParam(required = false, value = "date") @DateTimeFormat(pattern = DateUtils.DATE_FORMAT) LocalDate admitDate,
			@RequestParam(required = false, defaultValue = "true") Boolean display,
			@RequestParam(required = false) Integer confCapacity,
			@RequestParam(required = false) Integer originalCapacity, @RequestParam(required = false) String occupancy,
			@RequestParam(required = false, defaultValue = DEFAULT_LIMIT) Integer limit,
			@RequestParam(required = false, defaultValue = "0") Integer offset,
			@RequestParam(required = false, defaultValue = DEFAULT_ORDER_BY) String orderBy,
			@RequestParam(required = false, defaultValue = DEFAULT_SORT_ORDER) String sortOrder,
			HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving a Whiteboard. [limit={},offset={}] For company [comapnyId={}, serviceId={}]",
				limit, offset, companyId, serviceId);
		super.systemLog(request, MwAppResourceType.WHITEBOARD_ENTRY);
		try {
			return new ResponseEntity<>(whiteBoardService.getAWhiteBoard(companyId, serviceId, admitDate, display,
					confCapacity, originalCapacity, occupancy, limit, offset, orderBy, sortOrder), HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Print Whiteboard", tags = "Whiteboard API")
	@GetMapping(value = "/{serviceId}/print", produces = MediaType.APPLICATION_PDF_VALUE)
	@ResponseBody
	public ResponseEntity<?> print(@PathVariable("companyId") Long companyId,
			@PathVariable("serviceId") String serviceId,
			@RequestParam(required = false, value = "date") @DateTimeFormat(pattern = DateUtils.DATE_FORMAT) LocalDate admitDate,
			@RequestParam(required = false, defaultValue = "true") Boolean display,
			@RequestParam(required = false) Integer confCapacity,
			@RequestParam(required = false) Integer originalCapacity, @RequestParam(required = false) String occupancy,
			@RequestParam(required = false, defaultValue = DEFAULT_ORDER_BY) String orderBy,
			@RequestParam(required = false, defaultValue = DEFAULT_SORT_ORDER) String sortOrder,
			@RequestParam(required = false, value = "language") String language, HttpServletRequest request,
			HttpServletResponse response) {
		getLogger().info("Generating for company [comapnyId={}, serviceId={}]", companyId, serviceId);
		super.systemLog(request, MwAppResourceType.WHITEBOARD_FILES);
		try {
			Resource file = whiteBoardFilesService.generatePdfList(companyId, serviceId, admitDate, display,
					confCapacity, originalCapacity, occupancy, orderBy, sortOrder, language);
			String fileName = whiteBoardFilesService.generatePdfListName(companyId, serviceId);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(file);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Returns diets", response = List.class, responseContainer = "Whiteboard", tags = "Whiteboard API")
	@GetMapping(value = "/{serviceId}/diets")
	@ResponseBody
	public ResponseEntity<?> getDiets(@PathVariable("companyId") Long companyId,
			@PathVariable("serviceId") String serviceId,
			@RequestParam(required = false, value = "lang") String language, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			getLogger().info("Retrieving list of Diets for a Company and a language");
			List<CatalogDTO> diets = whiteBoardService.getDiets(companyId, language);
			return new ResponseEntity<>(diets, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Returns room types", response = List.class, responseContainer = "Whiteboard", tags = "Whiteboard API")
	@GetMapping(value = "/{serviceId}/room-types")
	@ResponseBody
	public ResponseEntity<?> getRoomTypes(@PathVariable("companyId") Long companyId,
			@PathVariable("serviceId") String serviceId,
			@RequestParam(required = false, value = "lang") String language, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			getLogger().info("Retrieving list of Room types for a Company and a language");
			List<CatalogDTO> diets = whiteBoardService.getRoomTypes(companyId, language);
			return new ResponseEntity<>(diets, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Returns rooms configurations", response = WhiteboardRoomsConfigurationDTO.class, responseContainer = "List", tags = "Whiteboard API", notes = "Endpoint to get the list of the whiteboard rooms configurations for a given Clinic and given Service.")
	@GetMapping(value = "/{serviceId}/rooms-config", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> getRoomsConfigurations(@PathVariable("companyId") Long companyId,
			@PathVariable("serviceId") String serviceId, HttpServletRequest request, HttpServletResponse response) {
		try {
			getLogger().info("Retrieving Whiteboard Rooms Configurations...");
			super.systemLog(request, MwAppResourceType.WHITEBOARD_ROOMS_CONFIGURATION);
			return new ResponseEntity<>(whiteBoardService.getRoomsConfiguration(companyId, serviceId), HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Update rooms configuration", response = WhiteboardRoomsConfigurationDTO.class, tags = "Whiteboard API", notes = "Endpoint to update the configuration of a room. Returns the saved Entity")
	@PutMapping(value = "/{serviceId}/rooms-config", produces = "application/json")
	@ResponseBody
	public ResponseEntity<?> updateRoomConfiguration(@PathVariable("companyId") Long companyId,
			@PathVariable("serviceId") String serviceId,
			@RequestBody List<WhiteboardRoomsConfigurationDTO> whiteboardRoomsConfigurationList,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			getLogger().info("Updating Whiteboard Room Configuration...");
			List<WhiteboardRoomsConfigurationDTO> savedEntities = whiteBoardService
					.updateRoomConfiguration(whiteboardRoomsConfigurationList);
			super.systemLog(request, MwAppResourceType.WHITEBOARD_ROOMS_CONFIGURATION);
			return new ResponseEntity<>(savedEntities, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Get Whiteboard Entries", response = WhiteBoardEntryDTO.class, responseContainer = "List", tags = "Whiteboard API")
	@GetMapping(path = "/{serviceId}/entries", produces = "application/json")
	public ResponseEntity<?> getEntries(@PathVariable("companyId") Long companyId,
			@PathVariable("serviceId") String serviceId,
			@RequestParam(required = false, defaultValue = DEFAULT_LIMIT) Integer limit,
			@RequestParam(required = false, defaultValue = "0") Integer offset,
			@RequestParam(required = false, defaultValue = DEFAULT_ORDER_BY) String orderBy,
			@RequestParam(required = false, defaultValue = DEFAULT_SORT_ORDER) String sortOrder,
			HttpServletRequest request, HttpServletResponse response) {
		getLogger().info(
				"Retrieving list of Whiteboard entries. [limit={},offset={}] For company [comapnyId={}, serviceId={}]",
				limit, offset, companyId, serviceId);
		super.systemLog(request, MwAppResourceType.WHITEBOARD_LIST);
		try {
			return new ResponseEntity<>(
					whiteBoardService.getAllWhiteboardEntries(companyId, serviceId, limit, offset, orderBy, sortOrder),
					HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "/entries/{whiteboardEntryId}", response = WhiteBoardViewDTO.class, tags = "Whiteboard API")
	@GetMapping(path = "/{serviceId}/entries/{whiteboardEntryId}", produces = "application/json")
	public ResponseEntity<?> getOne(@PathVariable("companyId") Long companyId,
			@PathVariable("serviceId") String serviceId, @PathVariable("whiteboardEntryId") Long whiteboardEntryId,
			HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving the entry [id={}] from the Whiteboard", whiteboardEntryId);
		try {
			WhiteBoardViewDTO whiteBoardViewDto = whiteBoardService.getOneEntry(whiteboardEntryId, companyId,
					serviceId);
			if (whiteBoardViewDto != null) {
				patientAccessLogService.log(MwAppResourceType.WHITEBOARD_ENTRY, whiteBoardViewDto,
						request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(),
						request.getRequestURI(), whiteBoardViewDto.getId().toString());
			}
			return new ResponseEntity<>(whiteBoardViewDto, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "/nurses", response = String.class, responseContainer = "List", tags = "Whiteboard API")
	@GetMapping(path = "/{serviceId}/nurses", produces = "application/json")
	public ResponseEntity<?> getNurses(@PathVariable("companyId") Long companyId,
			@PathVariable("serviceId") String serviceId,
			@RequestParam(required = false, defaultValue = DEFAULT_LIMIT) Integer limit,
			@RequestParam(required = false, defaultValue = "0") Integer offset, HttpServletRequest request,
			HttpServletResponse response) {
		getLogger().info("Retrieving list of nurses. [limit={},offset={}] For company [comapnyId={}, serviceId={}]",
				limit, offset, companyId, serviceId);
		super.systemLog(request, MwAppResourceType.WHITEBOARD_NURSES);
		try {
			return new ResponseEntity<>(whiteBoardService.getNurses(companyId, serviceId, limit, offset),
					HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "/physicians", response = String.class, responseContainer = "List", tags = "Whiteboard API")
	@GetMapping(path = "/{serviceId}/physicians", produces = "application/json")
	public ResponseEntity<?> getPhysicians(@PathVariable("companyId") Long companyId,
			@PathVariable("serviceId") String serviceId,
			@RequestParam(required = false, defaultValue = DEFAULT_LIMIT) Integer limit,
			@RequestParam(required = false, defaultValue = "0") Integer offset, HttpServletRequest request,
			HttpServletResponse response) {
		getLogger().info("Retrieving list of physicians. [limit={},offset={}] For company [comapnyId={}, serviceId={}]",
				limit, offset, companyId, serviceId);
		super.systemLog(request, MwAppResourceType.WHITEBOARD_PHYSICIANS);
		try {
			return new ResponseEntity<>(whiteBoardService.getPhysicians(companyId, serviceId, limit, offset),
					HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "/", response = WhiteBoardViewDTO.class, tags = "Whiteboard API")
	@PutMapping(value = "/{serviceId}", produces = "application/json")
	public ResponseEntity<?> update(@PathVariable("companyId") Long companyId,
			@PathVariable("serviceId") String serviceId, @RequestBody WhiteBoardViewDTO whiteBoard,
			HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Updating the whiteboard entry  For company [comapnyId={}, serviceId={}]", companyId,
				serviceId);
		try {
			WhiteBoardViewDTO whiteBoardViewDTO = whiteBoardService.update(whiteBoard, companyId, serviceId,
					(User) request.getAttribute(User.USER_KEY));
			patientAccessLogService.log(MwAppResourceType.WHITEBOARD_ENTRY, whiteBoard, request.getParameterMap(),
					(User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(),
					whiteBoard.getId().toString());
			return new ResponseEntity<>(whiteBoardViewDTO, HttpStatus.OK);
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
