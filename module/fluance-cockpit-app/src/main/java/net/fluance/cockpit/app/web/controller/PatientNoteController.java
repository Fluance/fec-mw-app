package net.fluance.cockpit.app.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.app.web.support.payload.response.GenericResponsePayload;
import net.fluance.cockpit.app.config.WebConfig;
import net.fluance.cockpit.app.service.domain.patient.PatientNoteService;
import net.fluance.cockpit.core.model.dto.patient.NoteDto;

@RestController
@RequestMapping(WebConfig.API_MAIN_PATIENT_NOTES)
public class PatientNoteController extends AbstractRestController {
	
	private static Logger LOGGER = LogManager.getLogger(NoteController.class);

	@Autowired
	PatientNoteService patientNoteService;

	@ApiOperation(value = "Note List", response = NoteDto.class, responseContainer = "list", tags = "PatientAPP Notes")
	@GetMapping(value = "")
	public ResponseEntity<?> getNotes(@RequestParam(value = "page", defaultValue = "0") int page, HttpServletResponse response, HttpServletRequest request) {
		try {
			return new ResponseEntity<>(patientNoteService.getNotes(page), HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Note", response = NoteDto.class, tags = "PatientAPP Notes")
	@PostMapping(value = "")
	public ResponseEntity<?> insertNote(@RequestBody NoteDto note, HttpServletResponse response) {
		try {
			return new ResponseEntity<>(patientNoteService.saveNote(note), HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Notes", response = NoteDto.class, tags = "PatientAPP Notes")
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> getNote(@PathVariable Long id, HttpServletResponse response) {
		try {
			return new ResponseEntity<>(patientNoteService.getNoteById(id), HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Notes", response = NoteDto.class, tags = "PatientAPP Notes")
	@PutMapping(value = "/{id}")
	public ResponseEntity<?> updateNote(@PathVariable Long id, @RequestBody NoteDto note, HttpServletResponse response) {
		try {
			return new ResponseEntity<>(patientNoteService.updateNote(id, note), HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Notes", response = NoteDto.class, tags = "PatientAPP Notes")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deleteNote(@PathVariable Long id, HttpServletResponse response) {
		try {
			patientNoteService.deleteNote(id);
			return new ResponseEntity<>(null, HttpStatus.OK);
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
		LOGGER.error("Data error on Patient notes {}", exc.getMessage());
		
		GenericResponsePayload grp = new GenericResponsePayload();
		
		String msg = DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE;
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		grp.setMessage(msg);
		
		return new ResponseEntity<>(grp, status);
	}
}
