package net.fluance.cockpit.app.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.exc.UnrecognizedPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import net.fluance.app.data.exception.DataException;
import net.fluance.app.data.model.identity.User;
import net.fluance.app.data.model.identity.UserProfile;
import net.fluance.app.log.LogService;
import net.fluance.app.web.servlet.controller.AbstractRestController;
import net.fluance.cockpit.app.config.WebConfig;
import net.fluance.cockpit.app.service.domain.PictureService;
import net.fluance.cockpit.core.domain.AdditionalMethods;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.PictureType;
import net.fluance.cockpit.core.model.jdbc.picture.PictureDetail;
import net.fluance.cockpit.core.model.jpa.picture.Picture;
import net.fluance.cockpit.core.model.jpa.picture.PictureHistoryDetail;

@RestController
public class PictureController extends AbstractRestController {

	@Autowired
	PictureService pictureService;

	@Autowired
	private LogService patientAccessLogService;

	@ApiOperation(value = "Pictures List", response = PictureDetail.class, responseContainer = "list", tags = "Notes & Pictures API")
	@RequestMapping(value = WebConfig.API_MAIN_URI_NOTES + "/pictures", method = RequestMethod.GET)
	public ResponseEntity<?> picturesbyPatient(@RequestParam(required = false) Long noteid, @RequestParam(required = false) Long pid, @RequestParam(required = false) Long visitNb, @RequestParam(required = false) String creator,
			@RequestParam(required = false) Integer categoryid, @RequestParam(required = false) Boolean read, @RequestParam(required = false) Boolean deleted, @RequestParam(required = false) Integer limit,
			@RequestParam(required = false) Integer offset, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);

			List<PictureDetail> picturesList = pictureService.getPicturesList(noteid, pid, visitNb, creator, categoryid, read, deleted, userProfile, limit, offset);

			patientAccessLogService.log(MwAppResourceType.PICTURE_LIST, picturesList, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(),
					(noteid != null ? noteid.toString() : null));
			return new ResponseEntity<>(picturesList, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Pictures For Shift Notes", response = PictureDetail.class, responseContainer = "list", tags = "Notes & Pictures API")
	@RequestMapping(value = WebConfig.API_MAIN_URI_NOTES + "/pictures/shift", method = RequestMethod.GET)
	public ResponseEntity<?> picturesbyPatientForShiftNotes(@RequestParam(required = false) Long noteid, @RequestParam(required = false) Long pid, @RequestParam(required = false) Long visitNb, @RequestParam(required = false) String creator,
			@RequestParam(required = false) Integer categoryid, @RequestParam(required = false) Boolean read, @RequestParam(required = false) Boolean deleted, @RequestParam(required = false, defaultValue = "0") Integer shiftNb,
			@RequestParam(required = false) Integer limit, @RequestParam(required = false) Integer offset, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			List<PictureDetail> picturesList = pictureService.getPicturesListForShiftNotes(noteid, pid, visitNb, creator, categoryid, read, deleted, shiftNb, userProfile, limit, offset);
			patientAccessLogService.log(MwAppResourceType.PICTURE_LIST, picturesList, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(),
					(noteid != null ? noteid.toString() : null));
			return new ResponseEntity<>(picturesList, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Pictures For Shift Notes Count", response = Count.class, tags = "Notes & Pictures API")
	@RequestMapping(value = WebConfig.API_MAIN_URI_NOTES + "/pictures/shift/count", method = RequestMethod.GET)
	public ResponseEntity<?> picturesbyPatientForShiftNotesCount(@RequestParam(required = false) Long noteid, @RequestParam(required = false) Long pid, @RequestParam(required = false) Long visitNb,
			@RequestParam(required = false) String creator, @RequestParam(required = false) Integer categoryid, @RequestParam(required = false) Boolean read, @RequestParam(required = false) Boolean deleted,
			@RequestParam(required = false, defaultValue = "0") Integer shiftNb, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			Count picturesCount = pictureService.getPicturesListForShiftNotesCount(noteid, pid, visitNb, creator, categoryid, read, deleted, shiftNb, userProfile);
			return new ResponseEntity<>(picturesCount, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Pictures List By Patient Count", response = Count.class, tags = "Notes & Pictures API")
	@RequestMapping(value = WebConfig.API_MAIN_URI_NOTES + "/pictures/count", method = RequestMethod.GET)
	public ResponseEntity<?> picturesbyPatientCount(@RequestParam(required = false) Long noteid, @RequestParam(required = false) Long pid, @RequestParam(required = false) Long visitNb, @RequestParam(required = false) String creator,
			@RequestParam(required = false) Integer categoryid, @RequestParam(required = false) Boolean read, @RequestParam(required = false) Boolean deleted, @RequestParam(required = false) Integer limit,
			@RequestParam(required = false) Integer offset, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			Count picturesCount = pictureService.getPicturesListCount(noteid, pid, creator, visitNb, categoryid, read, deleted, userProfile);
			return new ResponseEntity<>(picturesCount, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Picture Ordering", response = Picture.class, responseContainer = "list", tags = "Notes & Pictures API")
	@RequestMapping(value = WebConfig.API_MAIN_URI_NOTES + "/pictures/order", method = RequestMethod.PUT)
	public ResponseEntity<?> orderPicturesByNoteId(@RequestParam Long noteid, @RequestBody List<Long> pictureIds, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			User user = (User) request.getAttribute(User.USER_KEY);
			List<Picture> pictures = pictureService.orderPicturesOfNote(noteid, pictureIds, user);
			pictureService.insertIntoNoteHistory(noteid, pictureIds);
			patientAccessLogService.log(MwAppResourceType.NOTE, pictures, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), AdditionalMethods.REORDER_PICTURES.getValue(), request.getRequestURI(), noteid.toString());
			return new ResponseEntity<>(pictures, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Picture Detail", response = PictureDetail.class, responseContainer = "list", tags = "Notes & Pictures API")
	@RequestMapping(value = WebConfig.API_MAIN_URI_NOTES + "/pictures/{pictureid}", method = RequestMethod.GET)
	public ResponseEntity<?> picturesById(@PathVariable Long pictureid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			PictureDetail picture = pictureService.getPictureById(pictureid);
			patientAccessLogService.log(MwAppResourceType.PICTURE, picture, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), null);
			return new ResponseEntity<>(picture, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Picture Restore", tags = "Notes & Pictures API")
	@RequestMapping(value = WebConfig.API_MAIN_URI_NOTES + "/pictures/{pictureid}/restore", method = RequestMethod.PUT)
	public ResponseEntity<?> restorePicture(@PathVariable Long pictureid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			pictureService.setPictureAsDeleted(pictureid, userProfile, false);
			patientAccessLogService.log(MwAppResourceType.PICTURE, null, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), AdditionalMethods.RESTORE.getValue(), request.getRequestURI(), pictureid.toString());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Delete Picture", response = PictureDetail.class, responseContainer = "list", tags = "Notes & Pictures API")
	@RequestMapping(value = WebConfig.API_MAIN_URI_NOTES + "/pictures/{pictureid}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePictureById(@PathVariable Long pictureid, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			UserProfile userprofile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			pictureService.setPictureAsDeleted(pictureid, userprofile, true);
			patientAccessLogService.log(MwAppResourceType.PICTURE, null, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), pictureid.toString());
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Create Picture", response = PictureDetail.class, tags = "Notes & Pictures API")
	@RequestMapping(value = WebConfig.API_MAIN_URI_NOTES + "/pictures", method = RequestMethod.POST)
	public ResponseEntity<?> createPicture(@RequestParam Long noteid, @RequestPart("file") MultipartFile file, @RequestParam String picture, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			User user = (User) request.getAttribute(User.USER_KEY);
			pictureService.checkIfNoteLocked(noteid, user);
			Picture pictureObj = new Picture();
			try {
				pictureObj = new ObjectMapper().readValue(picture, Picture.class);
			} catch (UnrecognizedPropertyException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
			pictureService.createPicture(pictureObj, user);
			PictureDetail savedPicture = pictureService.getPictureById(pictureObj.getPictureId());
			pictureService.savePictureFile(file, savedPicture);
			patientAccessLogService.log(MwAppResourceType.PICTURE, savedPicture, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), null);
			return new ResponseEntity<>(savedPicture, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "Update Picture", tags = "Notes & Pictures API")
	@RequestMapping(value = WebConfig.API_MAIN_URI_NOTES + "/pictures/{pictureId}", method = RequestMethod.PUT)
	public ResponseEntity<?> updatePicture(@PathVariable Long pictureId, @RequestBody Picture picture, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			if (picture != null && !picture.getPictureId().equals(pictureId)) {
				throw new IllegalArgumentException("Path variable pictureId and Request Body pictureId are not the same");
			}
			User user = (User) request.getAttribute(User.USER_KEY);
			pictureService.checkIfNoteLocked(picture.getNoteId(), user);
			pictureService.updatePicture(picture, user);
			PictureDetail updatedPicture = pictureService.getPictureById(picture.getPictureId());
			patientAccessLogService.log(MwAppResourceType.PICTURE, updatedPicture, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), null);
			return new ResponseEntity<>(updatedPicture, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "GET Picture History Detail", response = PictureHistoryDetail.class, tags = "Notes & Pictures API")
	@RequestMapping(value = WebConfig.API_MAIN_URI_PICTURES + "/history/{historyId}", method = RequestMethod.GET)
	public ResponseEntity<?> getPictureHistoryDetail(@PathVariable Long historyId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			UserProfile userProfile = (UserProfile) request.getAttribute(UserProfile.USER_PROFILE_KEY);
			PictureDetail pictureHistory = pictureService.getPictureHistoryDetail(historyId, userProfile);
			return new ResponseEntity<>(pictureHistory, HttpStatus.OK);
		} catch (Exception e) {
			return handleException(e);
		}
	}

	@ApiOperation(value = "GET Picture Binary File", tags = "Picture API", produces = "application/octet-stream")
	@RequestMapping(value = WebConfig.API_MAIN_URI_PICTURES + "/{pictureId}", method = RequestMethod.GET, produces = "application/octet-stream")
	@ResponseBody
	public ResponseEntity<?> readPicture(@PathVariable Long pictureId, HttpServletRequest request) throws NotFoundException {
		PictureDetail pictureDetail = pictureService.getPictureById(pictureId);
		if (pictureDetail != null) {
			Resource file = pictureService.getPictureFile(pictureDetail, PictureType.ORIGINAL);
			patientAccessLogService.log(MwAppResourceType.PICTURE_FILE, null, request.getParameterMap(), (User) request.getAttribute(User.USER_KEY), request.getMethod(), request.getRequestURI(), pictureId.toString());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pictureDetail.getFileName() + "\"").body(file);
		} else {
			throw new NotFoundException("Picture ID : " + pictureId + " not found");
		}
	}

	@ApiOperation(value = "GET Thumbnail of the Picture", tags = "Picture API", produces = "application/octet-stream")
	@RequestMapping(value = WebConfig.API_MAIN_URI_PICTURES + "/{pictureId}/thumbnail", method = RequestMethod.GET, produces = "application/octet-stream")
	@ResponseBody
	public ResponseEntity<?> readPictureThumbnail(@PathVariable Long pictureId) throws NotFoundException, IOException {
		PictureDetail pictureDetail = pictureService.getPictureById(pictureId);
		if (pictureDetail != null) {
			Resource file = pictureService.getPictureFile(pictureDetail, PictureType.THUMBNAIL);
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pictureDetail.getFileName() + "\"").body(file);
		} else {
			throw new NotFoundException("Thumbnail of the Picture ID : " + pictureId + "not found");
		}
	}

	@Override
	public Logger getLogger() {
		return LogManager.getLogger();
	}

	@Override
	public Object handleDataException(DataException exc) {
		return null;
	}
}
