package net.fluance.cockpit.app.domain.log;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogCreator;
import net.fluance.app.log.model.LogModel;
import net.fluance.cockpit.app.service.log.PatientAccessLogService;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.PatientAccessLogModel;
import net.fluance.cockpit.core.model.jdbc.note.NoteDetails;
import net.fluance.cockpit.core.model.jdbc.picture.PictureDetail;
import net.fluance.cockpit.core.repository.jdbc.note.NoteDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.picture.PictureDetailRepository;
import net.fluance.cockpit.core.util.PatientAccessLogUtils;
import net.fluance.commons.lang.FluancePrintingMap;

public class PatientAccessLogCreatorPicture extends LogCreator {
	
	@Autowired
	PictureDetailRepository pictureDetailRepository;
	@Autowired
	NoteDetailRepository noteDetailRepository;

	@Override
	public LogModel getLogModel() {
		super.init();
		PatientAccessLogModel log = new PatientAccessLogModel();
		PictureDetail picture = new PictureDetail();
		if (payload instanceof PictureDetail && payload != null) { // pictureDetail is already the payload
			picture = (PictureDetail) payload;
		} else if (params.getMap().get(PatientAccessLogService.RESOURCE_ID) != null) { // Only information is the pictureId
			picture = pictureDetailRepository.findPictureById(Long.valueOf(params.getMap().get(PatientAccessLogService.RESOURCE_ID)[0]));
		}
		log.setResourceId(picture.getPictureId().toString());
		log.setDisplayName(picture.displayName());
		
		NoteDetails note = noteDetailRepository.findByNoteId(picture.getNoteId());
		if (note.getPatient().getPid() != null) {
			log.setParentPid(note.getPatient().getPid().toString());
		} else {
			log.setParentVisitNb(note.getVisitNb().toString());
		}
		log.setResourceType(resourceType.getResourceType());
		log.setHttpMethod(this.httpMethod);
		log.setUri(this.uri);
		log.setParameters(this.params.toString());
		
		PatientAccessLogUtils.fillUserInformation(user, log);
		
		return log;
	}

	/**
	 * Builds PatientAccessLogModel Creator Based on the PatientDetail payload
	 * @param resourceType
	 * @param payload
	 * @param params
	 * @param user
	 * @param httpMethod
	 * @throws UnsupportedOperationException
	 */
	public PatientAccessLogCreatorPicture(MwAppResourceType resourceType, Object payload, Map<String, String[]> params, User user, String httpMethod, String uri){
		this.resourceType = resourceType;
		this.payload = payload;
		this.params = new FluancePrintingMap<>(params);
		this.user = user;
		this.httpMethod = httpMethod;
		this.uri = uri;
	}
}
