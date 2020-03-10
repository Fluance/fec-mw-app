package net.fluance.cockpit.app.domain.log;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogCreator;
import net.fluance.app.log.model.LogModel;
import net.fluance.cockpit.app.service.log.PatientAccessLogService;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.PatientAccessLogModel;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewEntity;
import net.fluance.cockpit.core.repository.jdbc.whiteboard.WhiteBoardRepository;
import net.fluance.cockpit.core.util.PatientAccessLogUtils;
import net.fluance.commons.lang.FluancePrintingMap;

public class PatientAccessLogCreatorWhiteBoardDetail extends LogCreator {
	
	@Autowired
	private WhiteBoardRepository whiteBoardRepository;

	@Override
	public LogModel getLogModel() {
		super.init();
		
		PatientAccessLogModel log = new PatientAccessLogModel();
		
		if(params.getMap() != null && !params.getMap().isEmpty() && params.getMap().get(PatientAccessLogService.RESOURCE_ID).length > 0){
			WhiteBoardViewEntity whiteBoardView = whiteBoardRepository.findOneEntry(Long.valueOf(params.getMap().get(PatientAccessLogService.RESOURCE_ID)[0]));
			if(whiteBoardView != null) {
				log.setDisplayName(whiteBoardView.getFirstname() + " " + whiteBoardView.getLastname());
				log.setResourceId(whiteBoardView.getId().toString());
				log.setParentPid(whiteBoardView.getPatientId().toString());
				log.setParentVisitNb(whiteBoardView.getVisitNumber().toString());
			}
		}
		else {
			log.setDisplayName(null);
			log.setResourceId(null);
			log.setParentPid(null);
			log.setParentVisitNb(null);
		}
		
		log.setResourceType(resourceType.getResourceType());
		
		log.setHttpMethod(this.httpMethod);		
		log.setUri(this.uri);
		log.setParameters(this.params.toString());
		
		PatientAccessLogUtils.fillUserInformation(user, log);
		
		return log;
	}

	public PatientAccessLogCreatorWhiteBoardDetail(MwAppResourceType resourceType, Object payload, Map<String, String[]> params, User user, String httpMethod, String uri) {
		this.resourceType = resourceType;
		this.payload = payload;
		this.params = new FluancePrintingMap<>(params);
		this.user = user;
		this.httpMethod = httpMethod;
		this.uri = uri;
	}
}
