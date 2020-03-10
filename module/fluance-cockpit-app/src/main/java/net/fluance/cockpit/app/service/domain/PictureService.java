package net.fluance.cockpit.app.service.domain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javassist.NotFoundException;
import net.fluance.app.data.model.identity.User;
import net.fluance.app.data.model.identity.UserProfile;
import net.fluance.app.security.service.Person;
import net.fluance.app.security.service.UserProfileLoader;
import net.fluance.app.web.util.exceptions.ConflictException;
import net.fluance.app.web.util.exceptions.LockedException;
import net.fluance.app.web.util.exceptions.MustRequestLockException;
import net.fluance.cockpit.app.service.domain.picture.ThumbnailService;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.PictureType;
import net.fluance.cockpit.core.model.jdbc.note.NoteDetails;
import net.fluance.cockpit.core.model.jdbc.note.ShiftNote;
import net.fluance.cockpit.core.model.jdbc.picture.PictureComparator;
import net.fluance.cockpit.core.model.jdbc.picture.PictureDetail;
import net.fluance.cockpit.core.model.jpa.note.Note;
import net.fluance.cockpit.core.model.jpa.note.NoteHistoryDetail;
import net.fluance.cockpit.core.model.jpa.picture.Picture;
import net.fluance.cockpit.core.model.jpa.picture.PictureHistoryDetail;
import net.fluance.cockpit.core.repository.jdbc.picture.PictureDetailRepository;
import net.fluance.cockpit.core.repository.jpa.note.NoteHistoryDetailRepository;
import net.fluance.cockpit.core.repository.jpa.note.NoteRepository;
import net.fluance.cockpit.core.repository.jpa.note.NoteTrackRepository;
import net.fluance.cockpit.core.repository.jpa.picture.PictureHistoryDetailRepository;
import net.fluance.cockpit.core.repository.jpa.picture.PictureRepository;

@Service
public class PictureService {

	private static Logger LOGGER = LogManager.getLogger();
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;
	@Value("${notes.pictures.baseDirectory}")
	private String baseDirectory;
	@Value("${notes.pictures.thumbnail.squareSize}")
	private int thumbnailSquareSize;
	@Autowired
	PictureDetailRepository pictureDetailRepository;
	@Autowired
	NoteTrackRepository noteTrackRepository;
	@Autowired
	PictureRepository pictureRepository;
	@Autowired
	NoteHistoryDetailRepository noteHistoryDetailRepository;
	@Autowired
	PictureHistoryDetailRepository pictureHistoryDetailRepository;
	@Autowired
	NotesService noteService;
	@Autowired
	LockService lockService;
	@Autowired
	NoteRepository noteRepository;
	@Autowired
	UserProfileLoader profileLoader;
	@Autowired
	ShiftService shiftService;
	@Autowired
	ThumbnailService thumbnailService;

	public List<PictureDetail> getPicturesList(Long noteid, Long pid, Long visitNb, String creator, Integer categoryid, Boolean read, Boolean deleted, UserProfile userProfile, Integer limit, Integer offset) {
		List<NoteDetails> notes = new ArrayList<>();
		List<PictureDetail> allPictures = new ArrayList<>();
		if (noteid != null && pid == null && categoryid == null && read == null) {
			allPictures = pictureDetailRepository.findPicturesByNoteId(noteid, deleted, Integer.MAX_VALUE, 0);
			return applyLimitOffset(allPictures, limit, offset);
		} else if ((pid != null || visitNb != null) && read != null && noteid == null) {
			notes = noteService.getNotesListReadUnread(pid, visitNb, creator, categoryid, read, false, false, null, null, Integer.MAX_VALUE, 0, userProfile);
		} else if ((pid != null || visitNb != null) && read == null && noteid == null) {
			notes = noteService.getNotesList(pid, visitNb, creator, categoryid, false, false, null, null, Integer.MAX_VALUE, 0, userProfile);
		} else {
			throw new IllegalArgumentException("The provided parameters are invalid. The list of pictures can be obtained " + "either by the noteid or the patientid along with optional parameters categoryid and read.");
		}
		for (NoteDetails note : notes) {
			List<PictureDetail> pictures = pictureDetailRepository.findPicturesByNoteId(note.getId(), deleted, Integer.MAX_VALUE, 0);
			Collections.sort(pictures, new PictureComparator());
			allPictures.addAll(pictures);
		}
		return applyLimitOffset(allPictures, limit, offset);
	}

	public Count getPicturesListCount(Long noteid, Long pid, String creator, Long visitNb, Integer categoryid, Boolean read, Boolean deleted, UserProfile userProfile) {
		List<NoteDetails> notes = new ArrayList<>();
		List<PictureDetail> allPictures = new ArrayList<>();
		if (noteid != null && pid == null && categoryid == null) {
			return pictureDetailRepository.findPicturesByNoteIdCount(noteid, deleted);
		} else if ((pid != null || visitNb != null) && read != null && noteid == null) {
			notes = noteService.getNotesListReadUnread(pid, visitNb, creator, categoryid, read, false, false, null, null, Integer.MAX_VALUE, 0, userProfile);
		} else if ((pid != null || visitNb != null) && read == null && noteid == null) {
			notes = noteService.getNotesList(pid, visitNb, creator, categoryid, false, false, null, null, Integer.MAX_VALUE, 0, userProfile);
		} else {
			throw new IllegalArgumentException("The provided parameters are invalid. The list of pictures can be obtained " + "either by the noteid or the patientid along with optional parameters categoryid and read.");
		}
		for (NoteDetails note : notes) {
			List<PictureDetail> pictures = pictureDetailRepository.findPicturesByNoteId(note.getId(), deleted, Integer.MAX_VALUE, 0);
			allPictures.addAll(pictures);
		}
		return new Count(allPictures.size());
	}

	public List<PictureDetail> getPicturesListForShiftNotes(Long noteid, Long pid, Long visitNb, String creator, Integer categoryid, Boolean read, Boolean deleted, Integer shiftNb, UserProfile userProfile, Integer limit, Integer offset) {
		List<NoteDetails> notes = new ArrayList<>();
		List<PictureDetail> allPictures = new ArrayList<>();
		if (noteid != null && pid == null && categoryid == null && read == null) {
			allPictures = pictureDetailRepository.findAllPicturesByShiftNoteId(noteid, deleted);
			return applyLimitOffset(allPictures, limit, offset);
		} else if ((pid != null || visitNb != null) && read != null && noteid == null) {
			ShiftNote shiftNote = noteService.getShiftNotesListReadUnread(pid, visitNb, creator, categoryid, read, false, shiftNb, false, null, null, Integer.MAX_VALUE, 0, userProfile);
			notes = shiftNote.getNotes();
		} else if ((pid != null || visitNb != null) && read == null && noteid == null) {
			ShiftNote shiftNote = noteService.getShiftNotesList(pid, visitNb, creator, categoryid, false, shiftNb, false, null, null, Integer.MAX_VALUE, 0, userProfile);
			notes = shiftNote.getNotes();
		} else {
			throw new IllegalArgumentException("The provided parameters are invalid. The list of pictures can be obtained " + "either by the noteid or the patientid along with optional parameters categoryid and read.");
		}
		for (NoteDetails note : notes) {
			List<PictureDetail> pictures = pictureDetailRepository.findPicturesByNoteId(note.getId(), deleted, Integer.MAX_VALUE, 0);
			Collections.sort(pictures, new PictureComparator());
			allPictures.addAll(pictures);
		}
		return applyLimitOffset(allPictures, limit, offset);
	}

	public Count getPicturesListForShiftNotesCount(Long noteid, Long pid, Long visitNb, String creator, Integer categoryid, Boolean read, Boolean deleted, Integer shiftNb, UserProfile userProfile) {
		List<NoteDetails> notes = new ArrayList<>();
		List<PictureDetail> allPictures = new ArrayList<>();
		if (noteid != null && pid == null && categoryid == null && read == null) {
			return pictureDetailRepository.findAllPicturesByShiftNoteIdCount(noteid, deleted);
		} else if ((pid != null || visitNb != null) && read != null && noteid == null) {
			ShiftNote shiftNote = noteService.getShiftNotesListReadUnread(pid, visitNb, creator, categoryid, read, false, shiftNb, false, null, null, Integer.MAX_VALUE, 0, userProfile);
			notes = shiftNote.getNotes();
		} else if ((pid != null || visitNb != null) && read == null && noteid == null) {
			ShiftNote shiftNote = noteService.getShiftNotesList(pid, visitNb, creator, categoryid, false, shiftNb, false, null, null, Integer.MAX_VALUE, 0, userProfile);
			notes = shiftNote.getNotes();
		} else {
			throw new IllegalArgumentException("The provided parameters are invalid. The list of pictures can be obtained " + "either by the noteid or the patientid along with optional parameters categoryid and read.");
		}
		for (NoteDetails note : notes) {
			List<PictureDetail> pictures = pictureDetailRepository.findPicturesByNoteId(note.getId(), deleted, Integer.MAX_VALUE, 0);
			allPictures.addAll(pictures);
		}
		return new Count(allPictures.size());
	}

	@Transactional
	public List<Picture> orderPicturesOfNote(Long noteId, List<Long> pictureIds, User user) {
		Set<Long> set = new HashSet<Long>(pictureIds);
		if (set.size() < pictureIds.size()) {
			throw new IllegalArgumentException("Picture Ids must be unique in the list");
		}
		List<Picture> pictures = new ArrayList<>();
		for (int i = 0; i < pictureIds.size(); i++) {
			Picture picture = pictureRepository.findOne(pictureIds.get(i));
			if (picture == null) {
				throw new IllegalArgumentException("Picture with Id does not exist: " + pictureIds.get(i));
			}
			picture.setEditor(user.getDomain() + "/" + user.getUsername());
			picture.setOrder(i + 1);
			pictureRepository.save(picture);
			pictures.add(picture);
		}
		return pictures;
	}

	public void insertIntoNoteHistory(Long noteId, List<Long> pictureIds) {
		Note note = noteRepository.findOne(noteId);
		if (note != null) {
			NoteHistoryDetail historyDetail = new NoteHistoryDetail();
			historyDetail.setCategoryId(note.getCategoryId());
			historyDetail.setDeleted(note.isDeleted());
			historyDetail.setDescription(note.getDescription());
			historyDetail.setEditedDate(note.getEditedDate());
			historyDetail.setEditor(note.getEditor());
			historyDetail.setNoteId(noteId);
			historyDetail.setPatientId(note.getPatientId());
			historyDetail.setPicturesOrder(pictureIds.toString());
			historyDetail.setReferenceDate(note.getReferenceDate());
			historyDetail.setTitle(note.getTitle());
			historyDetail.setVisitNb(note.getVisitNb());
			historyDetail.setCreator(note.getEditor());
			noteHistoryDetailRepository.save(historyDetail);
		}
	}

	public PictureDetail getPictureById(Long pictureId) {
		return pictureDetailRepository.findPictureById(pictureId);
	}

	public void setPictureAsDeleted(Long pictureId, UserProfile user, boolean delete) {
		String editor = user.getDomain() + "/" + user.getUsername();
		Picture picture = pictureRepository.findOne(pictureId);
		NoteDetails note = noteService.getNoteDetail(picture.getNoteId(), user); // if user doesn't have access to this
																					// note, forbidden Exception is
																					// thrown
		if (checkIfNoteLocked(note.getId(), user) && picture != null) {
			picture.setDeleted(delete);
		}
		pictureRepository.save(picture);
		noteTrackRepository.markNoteAsUnReadForOtherUsers(picture.getNoteId(), editor);
	}

	public void setDeletedForAllPictures(Long noteId, User user, boolean deleted) {
		List<PictureDetail> pictures = pictureDetailRepository.findPicturesByNoteId(noteId, false, Integer.MAX_VALUE, 0);
		if (pictures != null && !pictures.isEmpty()) {
			for (int i = 0; i < pictures.size(); i++) {
				Picture picture = pictureRepository.findOne(pictures.get(i).getPictureId());
				picture.setDeleted(deleted);
				pictureRepository.save(picture);
			}
		}
	}

	public void createPicture(Picture picture, User user) {
		String editor = user.getDomain() + "/" + user.getUsername();
		picture.setEditor(editor);
		LOGGER.info("Creating a picture");
		pictureRepository.save(picture);
		
		noteTrackRepository.markNoteAsUnReadForOtherUsers(picture.getNoteId(), editor);
	}

	public Picture updatePicture(Picture picture, User user) throws NotFoundException {
		Picture oldPicture = pictureRepository.findOne(picture.getPictureId());
		if (oldPicture != null && oldPicture.getPictureId() != null && oldPicture.getPictureId() > 0) {
			LOGGER.debug("Updating picture with Id : " + picture.getPictureId());
			String editor = user.getDomain() + "/" + user.getUsername();
			picture.setEditor(editor);
			try {
				pictureRepository.save(picture);
				noteTrackRepository.markNoteAsUnReadForOtherUsers(picture.getNoteId(), editor);
				return picture;
			} catch (JpaSystemException e) {
				if (e.getCause().getCause().getMessage().contains("out of date")) {
					throw new ConflictException(e.getCause().getCause().getMessage());
				} else {
					throw e;
				}
			}
		} else {
			throw new NotFoundException("The given Picture doesn't exist pictureId : " + picture.getPictureId());
		}
	}

	public Resource getPictureFile(PictureDetail pictureDetail, PictureType pictureType) {
		String pictureLocation = buildPictureLocation(pictureDetail, pictureType, true);
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		LOGGER.info("Reading picture : " + pictureLocation);
		Resource file = resourceLoader.getResource(pictureLocation);
		return file;
	}

	public void savePictureFile(MultipartFile file, PictureDetail picture) throws IllegalStateException, IOException {
		initNoteDirectory(picture);
		String extension = FilenameUtils.getExtension(picture.getFileName());//getPictureExtension(file);
		String pictureLocation = buildPictureLocation(picture, PictureType.ORIGINAL, false);
		LOGGER.info("Saving picture : " + pictureLocation);
		File originalFile = new File(pictureLocation);
		file.transferTo(originalFile);
		String thumbnailLocation = buildPictureLocation(picture, PictureType.THUMBNAIL, false);
		LOGGER.info("Generating and Saving Thumbnail : " + thumbnailLocation);
		originalFile = new File(pictureLocation);
		writeThumbnail(originalFile, thumbnailLocation, extension);
	}

	public boolean checkIfNoteLocked(Long noteId, User user) throws LockedException, MustRequestLockException {
		if (lockService.isLockedByUser(noteId, "note", user.getUsername(), user.getDomain())) {
			return true;
		} else {
			if (lockService.verify(noteId, "note").isLocked()) {
				throw new LockedException();
			} else {
				throw new MustRequestLockException(MustRequestLockException.RESOURCE_NOT_LOCKED);
			}
		}
	}

	public PictureDetail getPictureHistoryDetail(Long historyId, UserProfile userProfile) throws NotFoundException {
		PictureHistoryDetail pictureHistory = pictureHistoryDetailRepository.findOne(historyId);
		if (pictureHistory != null) {
			noteService.getNoteDetail(pictureHistory.getNoteId(), userProfile); // If the User doesn't have access to
																				// the note, an exception is thrown.
			Person editor = new Person(pictureHistory.getEditor());
			editor = profileLoader.buildFullName(editor);
			return new PictureDetail(pictureHistory.getPictureId(), pictureHistory.getNoteId(), pictureHistory.getFilename(), pictureHistory.getEditedDate(), pictureHistory.getReferenceDate(), pictureHistory.getAnnotation(), editor,
					pictureHistory.getSortOrder(), pictureHistory.isDeleted());
		}
		throw new NotFoundException("No picture history found for Id: " + historyId);
	}

	private void initNoteDirectory(PictureDetail picture) {
		File noteDirectory = new File(baseDirectory + "/notes/" + picture.getNoteId());
		if (!noteDirectory.exists()) {
			noteDirectory.mkdirs();
		}
	}
	@SuppressWarnings("This doesnt works")
	private String getPictureExtension(MultipartFile file) throws IOException {
		Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(file.getInputStream());
		if (imageReaders.hasNext()) {
			ImageReader reader = (ImageReader) imageReaders.next();
			return reader.getFormatName();
		} else {
			return "jpg";
		}
	}

	private String buildPictureLocation(PictureDetail pictureDetail, PictureType pictureType, boolean isReadOperation) {
		LOGGER.debug("Preparing Picture Location of the Picture :" + pictureDetail.getFileName() + ", ID : " + pictureDetail.getPictureId());
		Long noteId = pictureDetail.getNoteId();
		String location = "";
		switch (pictureType) {
			case ORIGINAL:
				location = baseDirectory + "/notes/" + noteId + "/" + pictureDetail.getPictureId();
				break;
			case THUMBNAIL:
				location = baseDirectory + "/notes/" + noteId + "/thumbnail_" + pictureDetail.getPictureId();
				break;
			default:
				break;
		}
		if (isReadOperation) {
			location = "file:" + location;
		}
		LOGGER.debug("Picture Location :" + location);
		return location;
	}

	private void writeThumbnail(File originalFile, String thumbnailLocation, String extension) throws IOException {
		BufferedImage generatedThumbnail = thumbnailService.createThumbnail(originalFile, thumbnailSquareSize);
		ImageIO.write(generatedThumbnail, extension, new File(thumbnailLocation));
	}

	private List<PictureDetail> applyLimitOffset(List<PictureDetail> pictures, Integer limit, Integer offset) {
		if (limit == null) {
			limit = defaultResultLimit;
		}
		if (offset == null) {
			offset = defaultResultOffset;
		}
		if (offset > 0) {
			if (offset >= pictures.size()) {
				return pictures.subList(0, 0); // return empty.
			}
			if (limit > -1) {
				// apply offset and limit
				return pictures.subList(offset, Math.min(offset + limit, pictures.size()));
			} else {
				// apply just offset
				return pictures.subList(offset, pictures.size());
			}
		} else if (limit > -1) {
			// apply just limit
			return pictures.subList(0, Math.min(limit, pictures.size()));
		} else {
			return pictures.subList(0, pictures.size());
		}
	}
}
