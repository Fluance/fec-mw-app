package net.fluance.cockpit.app.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.data.model.identity.User;
import net.fluance.app.data.model.identity.UserProfile;
import net.fluance.app.log.LogService;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.app.web.support.payload.response.GenericResponsePayload;
import net.fluance.cockpit.app.service.domain.NotesService;
import net.fluance.cockpit.app.service.security.NotesRolesCategoriesService;
import net.fluance.cockpit.core.domain.AdditionalMethods;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.jdbc.note.NoteCategory;
import net.fluance.cockpit.core.model.jdbc.note.NoteDetails;
import net.fluance.cockpit.core.model.jdbc.note.ShiftNote;
import net.fluance.cockpit.core.model.jpa.note.History;
import net.fluance.cockpit.core.model.jpa.note.Note;
import net.fluance.cockpit.core.model.jpa.note.NoteHistoryDetail;

@RestController
@RequestMapping("/notes")
public class NoteController extends AbstractRestController {

	private static Logger LOGGER = LogManager.getLogger(NoteController.class);

	@Autowired
	private NotesService notesService;

	@Autowired
	private NotesRolesCategoriesService notesRolesCategoriesService;

	@Autowired
	private LogService patientAccessLogService;

	@ApiOperation(value = "Notes List", response = NoteDetails.class, responseContainer = "list", tags = "Notes & Pictures API")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getNotes(@RequestParam(required=false) Long pid, @RequestParam(required=false) Long visitNb, @RequestParam(required=false) Integer categoryid, @RequestParam(required=false) String creator, @RequestParam(required=false) Boolean read,
			@RequestParam(required=false, defaultValue = "false") Boolean deleted, @RequestParam(required=false, defaultValue = "false") Boolean haspictures, @RequestParam(required = false) String orderby, @RequestParam(required = false) String sortorder, @RequestParam(required=false) Integer limit,  @RequestParam(required=false) Integer offset, 
			HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving notes list... ");
		try{
			UserProfile userProfile = (UserProfile)request.getAttribute(UserProfile.USER_PROFILE_KEY);
			
			List<NoteDetails> notes;
			if(read != null){
				notes = notesService.getNotesListReadUnread(pid, visitNb, creator, categoryid, read, deleted, haspictures, orderby, sortorder, limit, offset, userProfile);
			}else{
				notes = notesService.getNotesList(pid, visitNb, creator, categoryid, deleted, haspictures, orderby, sortorder, limit, offset, userProfile);
			}
			if(notes != null && !notes.isEmpty()){
				patientAccessLogService.log(MwAppResourceType.NOTE_LIST, notes, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), null);
			}else{
				super.systemLog(request, MwAppResourceType.NOTE_LIST);
			}
			return new ResponseEntity<>(notes, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Shift Notes", response = ShiftNote.class, tags = "Notes & Pictures API")
	@RequestMapping(value = "/shift", method = RequestMethod.GET)
	public ResponseEntity<?> getShiftNotes(@RequestParam(required=false) Long pid, @RequestParam(required=false) Long visitNb, @RequestParam(required=false) Integer categoryid, 
			@RequestParam(required=false) String creator, @RequestParam(required=false) Boolean read, @RequestParam(required=false, defaultValue = "false") Boolean deleted, 
			@RequestParam(required=false, defaultValue = "0") Integer shiftNb, @RequestParam(required=false, defaultValue = "false") Boolean haspictures, @RequestParam(required = false) String orderby, @RequestParam(required = false) String sortorder, 
			@RequestParam(required=false) Integer limit,  @RequestParam(required=false) Integer offset, 
			HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving notes list... ");
		try{
			UserProfile userProfile = (UserProfile)request.getAttribute(UserProfile.USER_PROFILE_KEY);
			ShiftNote shiftNote;
			if(read != null){
				shiftNote = notesService.getShiftNotesListReadUnread(pid, visitNb, creator, categoryid, read, deleted, shiftNb, haspictures, orderby, sortorder, limit, offset, userProfile);
			}else{
				shiftNote = notesService.getShiftNotesList(pid, visitNb, creator, categoryid, deleted, shiftNb, haspictures, orderby, sortorder, limit, offset, userProfile);
			}
			if(shiftNote.getNotes()!=null && !shiftNote.getNotes().isEmpty()){
				patientAccessLogService.log(MwAppResourceType.NOTE_LIST, shiftNote.getNotes(), request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), null);
			}else{
				super.systemLog(request, MwAppResourceType.NOTE_LIST);
			}
			return new ResponseEntity<>(shiftNote, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Shift Notes Count", response = Count.class, tags = "Notes & Pictures API")
	@RequestMapping(value = "/shift/count", method = RequestMethod.GET)
	public ResponseEntity<?> getShiftNotesCount(@RequestParam(required=false) Long pid, @RequestParam(required=false) Long visitNb, @RequestParam(required=false) Integer categoryid, 
			@RequestParam(required=false) String creator, @RequestParam(required=false) Boolean read, @RequestParam(required=false, defaultValue = "false") Boolean deleted, 
			@RequestParam(required=false, defaultValue = "0") Integer shiftNb, @RequestParam(required=false, defaultValue = "false") Boolean haspictures, @RequestParam(required=false) Integer limit,  @RequestParam(required=false) Integer offset, 
			HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving notes list... ");
		try{
			UserProfile userProfile = (UserProfile)request.getAttribute(UserProfile.USER_PROFILE_KEY);
			Count notesCount;
			if(read != null){
				notesCount = notesService.getShiftNotesListReadUnreadCount(pid, visitNb, creator, categoryid, read, deleted, shiftNb, haspictures, userProfile);
			}else{
				notesCount = notesService.getShiftNotesListCount(pid, visitNb, creator, categoryid, deleted, shiftNb, haspictures, userProfile);
			}
			return new ResponseEntity<>(notesCount, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Notes List Count", response = Count.class, tags = "Notes & Pictures API")
	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public ResponseEntity<?> getNotesCount(@RequestParam(required=false) Long pid, @RequestParam(required=false) Long visitNb, @RequestParam(required=false) Boolean read,
			@RequestParam(required=false, defaultValue = "false") Boolean deleted, @RequestParam(required=false) Integer categoryid, @RequestParam(required=false, defaultValue = "false") Boolean haspictures,@RequestParam(required=false) String creator,
			HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving notes list count ... ");
		try{
			UserProfile userProfile = (UserProfile)request.getAttribute(UserProfile.USER_PROFILE_KEY);
			Count notesCount;
			if(read!=null){
				notesCount = notesService.getNotesListReadUnreadCount(pid, visitNb, categoryid,  creator, read, deleted, haspictures, userProfile);
			}else{
				notesCount = notesService.getNotesListCount(pid, visitNb, creator, categoryid, deleted, haspictures, userProfile);
			}
			return new ResponseEntity<>(notesCount, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Notes Detail", response = NoteDetails.class, tags = "Notes & Pictures API")
	@RequestMapping(value = "/{noteId}", method = RequestMethod.GET)
	public ResponseEntity<?> getNote(@PathVariable Long noteId, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving note by id : " + noteId + "...");
		try{
			UserProfile userProfile = (UserProfile)request.getAttribute(UserProfile.USER_PROFILE_KEY);
			NoteDetails note = notesService.getNoteDetail(noteId, userProfile);
			patientAccessLogService.log(MwAppResourceType.NOTE, note, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), null);
			return new ResponseEntity<>(note, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Categories", response = NoteCategory.class, responseContainer = "list", tags = "Notes & Pictures API")
	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public ResponseEntity<?> getCategories(HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving Note Categories ...");
		try{
			UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			List<NoteCategory> categories = notesRolesCategoriesService.getAccessibleCategories(userProfile);
			return new ResponseEntity<>(categories, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Category Detail", response = NoteCategory.class, tags = "Notes & Pictures API")
	@RequestMapping(value = "/categories/{categoryId}", method = RequestMethod.GET)
	public ResponseEntity<?> getCategoryDetail(@PathVariable int categoryId, HttpServletRequest request, HttpServletResponse response) {
		getLogger().info("Retrieving Note Category Detail...");
		try{
			UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			NoteCategory categoryDetail = notesService.getCategoryById(categoryId, userProfile);
			if(categoryDetail == null){
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(categoryDetail, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Create Note", response = NoteDetails.class, tags = "Notes & Pictures API")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> createNote(
			@RequestBody Note note,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			notesService.create(note, userProfile);		
			NoteDetails savedNote = notesService.getNoteDetail(note.getId(), userProfile);
			patientAccessLogService.log(MwAppResourceType.NOTE, savedNote, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), (savedNote.getPatient().getPid()!=null ? savedNote.getPatient().getPid().toString() : ""));
			return new ResponseEntity<>(savedNote, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Note Update", response = NoteDetails.class, tags = "Notes & Pictures API", notes="Here you can modify a Note <br> IMPORTANT : a NOTE must be locked by THE USER before any modification.")
	@ApiResponses(value = {@ApiResponse(code=org.apache.http.HttpStatus.SC_LOCKED,  response = Error.class, message="Ressource is locked by another user")})
	@RequestMapping(value = "/{noteId}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateNote(@PathVariable Long noteId,
			@RequestBody Note note,
			HttpServletRequest request, HttpServletResponse response) {
		try{
			if (!note.getId().equals(noteId)){
				throw new IllegalArgumentException("Path variable NoteID and Request Body NoteID are not the same");
			}
			User user = (User) request.getAttribute(User.USER_KEY);
			notesService.update(note, user);
			UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			NoteDetails savedNote = notesService.getNoteDetail(note.getId(), userProfile);
			patientAccessLogService.log(MwAppResourceType.NOTE, savedNote, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), savedNote.getId().toString());
			return new ResponseEntity<>(savedNote, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Notes Delete", response = NoteDetails.class, tags = "Notes & Pictures API")
	@RequestMapping(value = "/{noteId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteNote(
			@PathVariable Long noteId, HttpServletRequest request, HttpServletResponse response) {
		try{
			User user = (User) request.getAttribute(User.USER_KEY);
			notesService.setDeletedForNote(noteId, user, true);
			patientAccessLogService.log(MwAppResourceType.NOTE, null, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), noteId.toString());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Notes Restore", response = NoteDetails.class, tags = "Notes & Pictures API")
	@RequestMapping(value = "/{noteId}/restore", method = RequestMethod.PUT)
	public ResponseEntity<?> restoreNote(
			@PathVariable Long noteId, HttpServletRequest request, HttpServletResponse response) {
		try{
			User user = (User) request.getAttribute(User.USER_KEY);
			notesService.setDeletedForNote(noteId, user, false);
			patientAccessLogService.log(MwAppResourceType.NOTE, null, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), AdditionalMethods.RESTORE.getValue(), request.getRequestURI(), noteId.toString());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Notes Mark as read", response = NoteDetails.class, tags = "Notes & Pictures API")
	@RequestMapping(value = "/read", method = RequestMethod.PUT)
	public ResponseEntity<?> markNoteAsRead(@RequestParam(required=false) Long pid, @RequestParam(required=false) Long visitNb, @RequestParam(required=false) String creator,
			@RequestParam(required=false) Integer categoryid, @RequestParam(required=false) List<Long> noteIds, @RequestParam(required=false, defaultValue = "false") Boolean shift,
			@RequestParam(required=false, defaultValue = "0") Integer shiftNb, HttpServletRequest request, HttpServletResponse response) {
		try{
			UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			notesService.markNoteAsReadOrUnRead(pid, visitNb, creator, categoryid, noteIds, true, shift, shiftNb, userProfile);
			patientAccessLogService.log(MwAppResourceType.NOTE_LIST, null, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), AdditionalMethods.SET_READ.getValue(), request.getRequestURI(), null);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "Notes mark as UnRead", response = NoteDetails.class, tags = "Notes & Pictures API")
	@RequestMapping(value = "/unread", method = RequestMethod.PUT)
	public ResponseEntity<?> markNoteAsUnRead(@RequestParam(required=false) Long pid, @RequestParam(required=false) Long visitNb, @RequestParam(required=false) String creator,
			@RequestParam(required=false) Integer categoryid, @RequestParam(required=false) List<Long> noteIds, @RequestParam(required=false, defaultValue = "false") Boolean shift,
			@RequestParam(required=false, defaultValue = "0") Integer shiftNb, HttpServletRequest request, HttpServletResponse response) {
		try{
			UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			notesService.markNoteAsReadOrUnRead(pid, visitNb, creator, categoryid, noteIds, false, shift, shiftNb, userProfile);
			patientAccessLogService.log(MwAppResourceType.NOTE_LIST, null, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), AdditionalMethods.SET_UNREAD.getValue(), request.getRequestURI(), null);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "GET Note's History", response = History.class, responseContainer = "list", tags = "Notes & Pictures API")
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public ResponseEntity<?> getNoteHistory(@RequestParam Long noteid, @RequestParam(required=false) String resourcetype, @RequestParam(required=false) Long resourceid, @RequestParam(required=false) String beforedate, @RequestParam(required=false, defaultValue="50") Integer limit,  @RequestParam(required=false, defaultValue="0") Integer offset, HttpServletRequest request, HttpServletResponse response) {
		try{
			UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			List<History> noteHistory = notesService.getNoteHistory(noteid, resourcetype, resourceid, beforedate, userProfile, limit, offset);
			return new ResponseEntity<>(noteHistory, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "GET Note's History Count", response = Count.class, tags = "Notes & Pictures API")
	@RequestMapping(value = "/history/count", method = RequestMethod.GET)
	public ResponseEntity<?> getNoteHistoryCount(@RequestParam Long noteid, @RequestParam(required=false) String resourcetype, @RequestParam(required=false) Long resourceid, @RequestParam(required=false) String beforedate, HttpServletRequest request, HttpServletResponse response) {
		try{
			UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			Count noteHistoryCount = notesService.getNoteHistoryCount(noteid, resourcetype, resourceid, beforedate, userProfile);
			return new ResponseEntity<>(noteHistoryCount, HttpStatus.OK);
		} catch (Exception e){
			return handleException(e);
		}
	}

	@ApiOperation(value = "GET Note History Detail", response = NoteHistoryDetail.class, tags = "Notes & Pictures API")
	@RequestMapping(value = "/history/{historyId}", method = RequestMethod.GET)
	public ResponseEntity<?> getNoteHistoryDetail(@PathVariable Long historyId, HttpServletRequest request, HttpServletResponse response) {
		try{
			UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			NoteDetails noteHistoryDetail = notesService.getNoteHistoryDetail(historyId, userProfile);
			return new ResponseEntity<>(noteHistoryDetail, HttpStatus.OK);
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
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		grp.setMessage(DEFAULT_INTERNAL_SERVER_ERROR_MESSAGE);
		return new ResponseEntity<>(grp, status);
	}
	
}
