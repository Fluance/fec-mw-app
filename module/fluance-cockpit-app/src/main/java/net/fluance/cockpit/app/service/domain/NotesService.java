package net.fluance.cockpit.app.service.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Component;

import javassist.NotFoundException;
import net.fluance.app.data.model.identity.User;
import net.fluance.app.data.model.identity.UserProfile;
import net.fluance.app.security.service.UserProfileLoader;
import net.fluance.app.web.util.exceptions.ConflictException;
import net.fluance.app.web.util.exceptions.ForbiddenException;
import net.fluance.app.web.util.exceptions.LockedException;
import net.fluance.app.web.util.exceptions.MustRequestLockException;
import net.fluance.cockpit.app.service.security.NotesRolesCategoriesService;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.PatientReference;
import net.fluance.cockpit.core.model.Shift;
import net.fluance.cockpit.core.model.jdbc.note.NoteCategory;
import net.fluance.cockpit.core.model.jdbc.note.NoteComparator;
import net.fluance.cockpit.core.model.jdbc.note.NoteDetails;
import net.fluance.cockpit.core.model.jdbc.note.ShiftNote;
import net.fluance.cockpit.core.model.jpa.note.History;
import net.fluance.cockpit.core.model.jpa.note.Note;
import net.fluance.cockpit.core.model.jpa.note.NoteHistoryDetail;
import net.fluance.cockpit.core.model.jpa.note.NoteTrack;
import net.fluance.cockpit.core.repository.jdbc.note.NoteDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.note.NoteHistoryRepository;
import net.fluance.cockpit.core.repository.jpa.note.NoteHistoryDetailRepository;
import net.fluance.cockpit.core.repository.jpa.note.NoteRepository;
import net.fluance.cockpit.core.repository.jpa.note.NoteTrackRepository;
import net.fluance.cockpit.core.util.sql.NoteListOrderByEnum;
import net.fluance.cockpit.core.util.sql.NoteListSortOrderEnum;

//@ComponentScan(basePackages={"net.fluance.cockpit.core"})
@Component
public class NotesService {

	private static Logger LOGGER = LogManager.getLogger();
	private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private final SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);

	@Autowired
	PictureService pictureService;

	@Autowired
	NoteDetailRepository noteRepository;

	@Autowired
	NoteRepository repository;

	@Autowired
	NoteTrackRepository noteTrackRepository;

	@Autowired
	NoteHistoryDetailRepository noteHistoryDetailRepository;

	@Autowired
	NotesRolesCategoriesService notesRolesCategoriesService;

	@Autowired
	NoteHistoryRepository noteHistoryRepository;

	@Autowired
	LockService lockService;

	@Autowired
	UserProfileLoader profileLoader;

	@Autowired
	ShiftService shiftService;

	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultAppointmentListOrderBy}")
	private String defaultResultAppointmentListOrderBy;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultNoteDetailsListOrderBy}")
	private String defaultResultNoteDetailsListOrderBy;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultNoteDetailsListSortOrder}")
	private String defaultResultNoteDetailsListSortOrder;
	
	public List<NoteDetails> getNotesList(Long pid, Long visitNb, String creator, Integer categoryid, Boolean deleted, boolean hasPictures,
			String orderBy, String sortOrder, Integer limit, Integer offset, UserProfile userProfile) {
		
		boolean orderByClient = orderBy != null && !orderBy.isEmpty();
		boolean sortOrderClient = sortOrder != null && !sortOrder.isEmpty();
		
		validatePidVisitNb(pid, visitNb);
		orderBy = validatedOrderBy(orderBy);
		sortOrder = validatedSortOrder(sortOrder);

		List<Integer> finalCategories = prepareCategories(categoryid, userProfile);
		List<NoteDetails> notes = new ArrayList<>();
		if(pid != null){
			notes = noteRepository.findByPatientId(pid, creator, deleted, finalCategories, hasPictures, orderBy, sortOrder, limit, offset);
		} else {
			notes = noteRepository.findByVisitNb(visitNb, creator, deleted, finalCategories, hasPictures, orderBy, sortOrder, limit, offset);
		}
		String username = userProfile.getDomain() + "/" + userProfile.getUsername();
		for (NoteDetails note : notes) {
			note.setRead(noteRepository.getHasRead(username, creator, note.getId()));
			note.setHasPictures(pictureService.getPicturesListCount(note.getId(), null, null, null, null, null, false, null).getCount() > 0);
		}
		setCategoryPrioritiesInNotes(userProfile, notes); // Since the priorities of notes returned by noteRepository wasn't set
		if ( ! orderByClient && ! sortOrderClient ){
			Collections.sort(notes, new NoteComparator());
		}
		return notes;
	}
	
	public ShiftNote getShiftNotesList(Long pid, Long visitNb, String creator, Integer categoryid, Boolean deleted, Integer shiftNb, boolean hasPictures, 
			String orderBy, String sortOrder, Integer limit, Integer offset, UserProfile userProfile) {
		
		validatePidVisitNb(pid, visitNb);
		orderBy = validatedOrderBy(orderBy);
		sortOrder = validatedSortOrder(sortOrder);
		

		Shift shift = shiftService.getShift(shiftNb);
		List<Integer> finalCategories = prepareCategories(categoryid, userProfile);
		List<NoteDetails> notes = new ArrayList<>();
		if(pid != null){
			notes = noteRepository.findShiftNotesByPatientId(pid, creator, deleted, finalCategories, hasPictures, shift.getStartDate(), shift.getEndDate(), orderBy, sortOrder, limit, offset);
		} else {
			notes = noteRepository.findShiftNotesByVisitNb(visitNb, creator, deleted, finalCategories, hasPictures, shift.getStartDate(), shift.getEndDate(), orderBy, sortOrder, limit, offset);
		}
		String username = userProfile.getDomain() + "/" + userProfile.getUsername();
		setCategoryPrioritiesInNotes(userProfile, notes); // Since the priorities of notes returned by noteRepository wasn't set
		Collections.sort(notes, new NoteComparator());
		for (NoteDetails note : notes) {
			note.setRead(noteRepository.getHasRead(username, creator, note.getId()));
			note.setHasPictures(pictureService.getPicturesListForShiftNotesCount(note.getId(), null, null, null, null, null, false, null, null).getCount() > 0);
		}
		return new ShiftNote(notes, shift);
	}

	public Count getShiftNotesListCount(Long pid, Long visitNb, String creator, Integer categoryid, Boolean deleted, Integer shiftNb, boolean hasPictures, UserProfile userProfile) {
		validatePidVisitNb(pid, visitNb);
		Shift shift = shiftService.getShift(shiftNb);
		List<Integer> finalCategories = prepareCategories(categoryid, userProfile);
		Count notesCount = new Count(0);
		if(pid != null){
			notesCount = noteRepository.findShiftNotesByPatientIdCount(pid, creator, deleted, finalCategories, hasPictures, shift.getStartDate(), shift.getEndDate());
		} else {
			notesCount = noteRepository.findShiftNotesByVisitNbCount(visitNb, creator, deleted, finalCategories, hasPictures, shift.getStartDate(), shift.getEndDate());
		}
		return notesCount;
	}

	public Count getNotesListCount(Long pid, Long visitNb, String creator, Integer categoryid, Boolean deleted, boolean hasPictures, UserProfile userProfile) {
		validatePidVisitNb(pid, visitNb);
		List<Integer> finalCategories = prepareCategories(categoryid, userProfile);
		
		
		Count notesCount = new Count(0);
		if(pid != null){
			notesCount = noteRepository.findByPatientIdCount(pid, creator, deleted, finalCategories, hasPictures);
		} else {
			notesCount = noteRepository.findByVisitNbCount(visitNb, creator, deleted, finalCategories, hasPictures);
		}
		return notesCount;
	}

	public List<NoteDetails> getNotesListReadUnread(Long pid, Long visitNb, String creator, Integer categoryid, Boolean read, Boolean deleted, boolean hasPictures, 
			String orderBy, String sortOrder, Integer limit, Integer offset, UserProfile userProfile) {

		validatePidVisitNb(pid, visitNb);
		orderBy = validatedOrderBy(orderBy);
		sortOrder = validatedSortOrder(sortOrder);
		
		List<Integer> finalCategories = prepareCategories(categoryid, userProfile);
		String userName = userProfile.getDomain() + "/" + userProfile.getUsername();
		List<NoteDetails> notes = new ArrayList<>();
		if(pid != null){
			notes = noteRepository.findByPatientIdAndRead(pid, finalCategories, creator, read, deleted, hasPictures, userName, orderBy, sortOrder, limit, offset);
		} else {
			notes = noteRepository.findByVisitNbAndRead(visitNb, finalCategories, creator, read, deleted, hasPictures, userName, orderBy, sortOrder, limit, offset);
		}
		for (NoteDetails note : notes) {
			note.setRead(read);
			note.setHasPictures(pictureService.getPicturesListCount(note.getId(), null, null, null, null, null, false, null).getCount() > 0);
		}
		setCategoryPrioritiesInNotes(userProfile, notes); // Since the priorities of notes returned by noteRepository wasn't set
		Collections.sort(notes, new NoteComparator());
		return notes;
	}
	
	public ShiftNote getShiftNotesListReadUnread(Long pid, Long visitNb, String creator, Integer categoryid, Boolean read, Boolean deleted, Integer shiftNb, boolean hasPictures,
			String orderBy, String sortOrder, Integer limit, Integer offset, UserProfile userProfile) {

		validatePidVisitNb(pid, visitNb);
		orderBy = validatedOrderBy(orderBy);
		sortOrder = validatedSortOrder(sortOrder);
		
		Shift shift = shiftService.getShift(shiftNb);
		List<Integer> finalCategories = prepareCategories(categoryid, userProfile);
		String userName = userProfile.getDomain() + "/" + userProfile.getUsername();
		List<NoteDetails> notes = new ArrayList<>();
		
		if(pid != null){
			notes = noteRepository.findShiftNotesByPatientIdAndRead(pid, finalCategories, creator, read, deleted, hasPictures, userName, shift.getStartDate(), shift.getEndDate(), orderBy, sortOrder, limit, offset);
		} else {
			notes = noteRepository.findShiftNotesByVisitNbAndRead(visitNb, finalCategories, creator, read, deleted, hasPictures, userName, shift.getStartDate(), shift.getEndDate(), orderBy, sortOrder, limit, offset);
		}
		setCategoryPrioritiesInNotes(userProfile, notes); // Since the priorities of notes returned by noteRepository wasn't set
		Collections.sort(notes, new NoteComparator());
		for (NoteDetails note : notes) {
			note.setRead(read);
			note.setHasPictures(pictureService.getPicturesListForShiftNotesCount(note.getId(), null, null, null, null, null, false, null, null).getCount() > 0);
		}
		return new ShiftNote(notes, shift);
	}
	
	public Count getShiftNotesListReadUnreadCount(Long pid, Long visitNb, String creator, Integer categoryid, Boolean read, Boolean deleted, Integer shiftNb, boolean hasPictures, UserProfile userProfile) {
		validatePidVisitNb(pid, visitNb);
		Shift shift = shiftService.getShift(shiftNb);
		List<Integer> finalCategories = prepareCategories(categoryid, userProfile);
		String userName = userProfile.getDomain() + "/" + userProfile.getUsername();
		Count nbNotes = new Count(0);
		if(pid != null){
			nbNotes = noteRepository.findShiftNotesByPatientIdAndReadCount(pid, finalCategories, creator, read, deleted, hasPictures, userName, shift.getStartDate(), shift.getEndDate());
		} else {
			nbNotes = noteRepository.findShiftNotesByVisitNbAndReadCount(visitNb, finalCategories, creator, read, deleted, hasPictures, userName, shift.getStartDate(), shift.getEndDate());
		}
		return nbNotes;
	}

	public Count getNotesListReadUnreadCount(Long pid, Long visitNb, Integer categoryid, String creator, Boolean read, Boolean deleted, boolean hasPictures, UserProfile userProfile) {
		validatePidVisitNb(pid, visitNb);
		List<Integer> finalCategories = prepareCategories(categoryid, userProfile);
		String userName = userProfile.getDomain() + "/" + userProfile.getUsername();
		Count nbNotes = new Count(0);
		if(pid != null){
			nbNotes = noteRepository.findByPatientIdAndReadCount(pid, creator, finalCategories, read, deleted, hasPictures, userName);
		} else {
			nbNotes = noteRepository.findByVisitNbAndReadCount(visitNb, creator, finalCategories, read, deleted, hasPictures, userName);
		}
		return nbNotes;
	} 

	/**
	 * returns category detail (with priority )
	 * @param categoryId
	 * @param userProfile
	 * @return
	 */
	public NoteCategory getCategoryById(int categoryId, UserProfile userProfile) {
		List<NoteCategory> categoryDetails = notesRolesCategoriesService.getAccessibleCategories(userProfile);
		for(NoteCategory category : categoryDetails){
			if (category.getId() == categoryId)
				return category;
		}
		return null;
	}

	public NoteDetails getNoteDetail(Long noteId, UserProfile userProfile) {
		String username = userProfile.getDomain() +"/"+ userProfile.getUsername();
		NoteDetails note = noteRepository.findByNoteId(noteId);
		note.setRead(noteRepository.getHasRead(username, null, noteId));
		if (notesRolesCategoriesService.hasAccessToCategory(userProfile, note.getCategory().getId())){
			setCategoryPriorityInNote(userProfile, note);
			return note;
		}
		else {
			throw new ForbiddenException();
		}
	}

	public Note create(Note note, User user) throws NotFoundException {
		validatePidVisitNb(note.getPatientId(), note.getVisitNb());
		String editor = user.getDomain() +"/"+ user.getUsername();
		note.setEditor(editor);
		note.setCreator(editor);
		LOGGER.info("The user:" + user.getUsername() + "Tries to CREATE A new Note");
		Note newNote = repository.save(note);
		
		noteTrackRepository.markNoteAsRead(editor, newNote.getId());
		
		return newNote;
	}

	public Note update(Note note, User user) throws NotFoundException {
		String editor = user.getDomain() +"/"+ user.getUsername();
		note.setEditor(editor);
		NoteDetails oldNote = noteRepository.findByNoteId(note.getId());
		if (oldNote!=null && oldNote.getId() != null && oldNote.getId() > 0){
			note.setCreator(oldNote.getCreator().getUsername());
			LOGGER.info("The user:" + user.getUsername() + " Tries to UPDATE note:" + note.getId());
			if(lockService.isLockedByUser(note.getId(), "note", user.getUsername(), user.getDomain())){
				LOGGER.debug("Updating noteId : " + note.getId());
				try {
					repository.save(note);
					noteTrackRepository.markNoteAsUnReadForOtherUsers(note.getId(), editor);					
					return note;
				} catch (JpaSystemException e) {
					if (e.getCause().getCause().getMessage().contains("out of date")){
						throw new ConflictException(e.getCause().getCause().getMessage());
					} else {
						throw e;
					}
				}
			} else {
				if(lockService.verify(note.getId(), "note").isLocked()){
					throw new LockedException();
				}else{
					throw new MustRequestLockException(MustRequestLockException.RESOURCE_NOT_LOCKED);				
				}
			}
		} else {
			throw new NotFoundException("The given NoteId doesn't exist noteId : " + note.getId());
		}
	}

	public void setDeletedForNote(Long noteId, User user, boolean deleted) throws NotFoundException{
		Note note = repository.findOne(noteId);
		note.setDeleted(deleted);
		update(note, user);
	}

	private List<Integer> prepareCategories(Integer categoryid, UserProfile userProfile) {
		List<Integer> finalCategories = new ArrayList<>();
		if (categoryid != null) {
			if (notesRolesCategoriesService.hasAccessToCategory(userProfile, categoryid)) {
				finalCategories.add(categoryid);
			} else {
				throw new ForbiddenException();
			}
		} else {
			List<Integer> accessibleCategories = notesRolesCategoriesService.getAccessibleCategoriesIds(userProfile);
			finalCategories.addAll(accessibleCategories);
		}
		return finalCategories;
	}

	public void markNoteAsReadOrUnRead(Long pid, Long visitNb, String creator, Integer categoryid, List<Long> noteIds, boolean read, boolean shift, Integer shiftNb, UserProfile userProfile) throws NotFoundException{
		validatRequestParametersForReadUnread(pid, visitNb, noteIds);
		String userName = userProfile.getDomain() + "/"+ userProfile.getUsername();
		List<NoteDetails> notes = new ArrayList<>();
		if(pid != null || visitNb != null){
			if(shift==true){
				ShiftNote shiftNote = getShiftNotesList(pid, visitNb, creator, categoryid, false, shiftNb, false, null, null, Integer.MAX_VALUE, 0, userProfile);
				notes = shiftNote.getNotes();
			} else{
				notes = getNotesList(pid, visitNb, creator, categoryid, false, false, null, null, Integer.MAX_VALUE, 0, userProfile);				
			}
		} else if(noteIds != null && !noteIds.isEmpty()){
			for (Long noteId : noteIds) {
				NoteDetails note = noteRepository.findByNoteId(noteId);
				notes.add(note);
			} 
		}
		for (NoteDetails noteDetail : notes){
			markNoteAsReadOrUnRead(userName, noteDetail, read);
		}
	}

	public List<History> getNoteHistory(Long noteId, String resourcetype, Long resourceid, String beforedate, UserProfile userProfile, Integer limit, Integer offset){
		this.getNoteDetail(noteId, userProfile); // If the User doesn't have access to the note, an exception is thrown.
		validateDate(beforedate);
		return noteHistoryRepository.getNoteHistory(noteId, resourcetype, resourceid, beforedate, limit, offset);
	}

	public Count getNoteHistoryCount(Long noteId, String resourcetype, Long resourceid, String beforedate, UserProfile userProfile){
		this.getNoteDetail(noteId, userProfile); // If the User doesn't have access to the note, an exception is thrown.
		validateDate(beforedate);
		return noteHistoryRepository.getNoteHistoryCount(noteId, resourcetype, resourceid, beforedate);
	}
	
	/**
	 * Validate that the provided date from FE is parsable to Date
	 * @param beforedate
	 */
	private void validateDate(String beforedate) {
		try {
			if(beforedate != null && !beforedate.isEmpty() ) {
				if(beforedate.length() <= DEFAULT_DATE_PATTERN.length()){
					dateFormat.parse(beforedate);
				} else {
					throw new IllegalArgumentException("'beforedate' parameter don't have the correct format (" + DEFAULT_DATE_PATTERN + ")");
				}
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("'beforedate' parameter don't have the correct format (" + DEFAULT_DATE_PATTERN + ")", e);
		}
	}

	public NoteDetails getNoteHistoryDetail(Long historyId, UserProfile userProfile) throws NotFoundException{
		NoteHistoryDetail noteHistoryDetail = noteHistoryDetailRepository.findOne(historyId);
		if(noteHistoryDetail != null){
			NoteDetails noteDetail = this.getNoteDetail(noteHistoryDetail.getNoteId(), userProfile); // If the User doesn't have access to the note, an exception is thrown.
			PatientReference patient = noteDetail.getPatient();
			NoteCategory category = getCategoryById(noteHistoryDetail.getCategoryId(), userProfile);
			boolean deleted = noteHistoryDetail.isDeleted();
			String description = noteHistoryDetail.getDescription();
			Date editedDate = noteHistoryDetail.getEditedDate();
			String editor = noteHistoryDetail.getEditor();
			Long noteId = noteHistoryDetail.getNoteId();
			Date referenceDate = noteHistoryDetail.getReferenceDate();
			String title = noteHistoryDetail.getTitle();
			Long visitNb = noteHistoryDetail.getVisitNb();
			String creator = noteHistoryDetail.getCreator();
			Boolean shifted = noteHistoryDetail.isShifted();
			NoteDetails note = new NoteDetails(noteId, title, description, editor, creator, shifted, referenceDate, editedDate, deleted, category, patient, visitNb);
			buildFullNames(note);
			return note;
		}
		throw new NotFoundException("No note history found for Id: " + historyId);
	}

	private void buildFullNames(NoteDetails note) {
		note.setEditor(profileLoader.buildFullName(note.getEditor()));
		note.setCreator(profileLoader.buildFullName(note.getCreator()));
	}

	private void markNoteAsReadOrUnRead(String userName, NoteDetails note, boolean read) throws NotFoundException{
		if(note != null){
			NoteTrack noteTrack = noteTrackRepository.findByUsernameAndNoteId(userName, note.getId());
			if(read == true && noteTrack == null){
				noteTrackRepository.markNoteAsRead(userName, note.getId());
			}else if(read == false && noteTrack != null){
				noteTrackRepository.markNoteAsUnRead(userName, note.getId());
			}
		}
	}

	private void setCategoryPrioritiesInNotes(UserProfile userProfile, List<NoteDetails> notes) {
		for (NoteDetails note : notes) {
			setCategoryPriorityInNote(userProfile, note);
		}
	}

	private void setCategoryPriorityInNote(UserProfile userProfile, NoteDetails note) {
		NoteCategory noteCategory = getCategoryById(note.getCategory().getId(), userProfile);
		if(noteCategory != null){
			note.getCategory().setPriority(noteCategory.getPriority());
		}
	}

	private void validatePidVisitNb(Long pid, Long visitNb) {
		if (pid == null && visitNb == null) {
			throw new IllegalArgumentException("Parameter pid or visitNb is required");
		} else if (pid != null && visitNb != null) {
			throw new IllegalArgumentException("Please, use Only pid or visitNb, not both !");
		}
	}

	private void validatRequestParametersForReadUnread(Long pid, Long visitNb, List<Long> noteIds) {
		if (pid == null && visitNb == null && (noteIds==null || noteIds.isEmpty())) {
			throw new IllegalArgumentException("Parameter pid or visitNb or noteIds is required");
		} else if (pid != null && visitNb != null) {
			throw new IllegalArgumentException("Please, use Only pid or visitNb, not both !");
		} else if(pid != null && (noteIds != null && !noteIds.isEmpty())){
			throw new IllegalArgumentException("Please, use Only pid or noteIds, not both !");
		}else if(visitNb != null && (noteIds != null && !noteIds.isEmpty())){
			throw new IllegalArgumentException("Please, use Only visitNb or noteIds, not both !");
		}else if(visitNb != null && pid != null && (noteIds!=null && !noteIds.isEmpty())){
			throw new IllegalArgumentException("Please, use Only visitNb, pid or noteIds, not all three !");
		}
	}
	
	
	/**
	 * orderBy is not mandatory so if null replace it by the default value, BUT if provided then must be checked.
	 * 
	 * @param orderBy
	 * @return
	 */
	private String validatedOrderBy(String orderBy) {
		if(orderBy == null){
			return null;
		}
		NoteListOrderByEnum orderByEnum = NoteListOrderByEnum.permissiveValueOf(orderBy);
		if (orderByEnum == null) {
			throw new IllegalArgumentException("orderBy value not supported : " + orderBy);
		} else {
			return orderByEnum.name();
		}
	}

	/**
	 * sortOrder is not mandatory so if null replace it by the default value, BUT if provided then must be checked.
	 * @param sortOrder
	 * @return
	 */
	private String validatedSortOrder(String sortOrder) {
		sortOrder = (sortOrder == null) ? defaultResultNoteDetailsListSortOrder : sortOrder;
		NoteListSortOrderEnum sortOrderEnum = NoteListSortOrderEnum.permissiveValueOf(sortOrder);
		if (sortOrderEnum == null){
			throw new IllegalArgumentException("sortOrder value not supported : " + sortOrder);
		}
		return sortOrderEnum.name();
	}
		
}