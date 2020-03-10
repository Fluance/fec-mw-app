package net.fluance.cockpit.app.domain.log;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogCreator;
import net.fluance.app.log.model.LogModel;
import net.fluance.cockpit.app.service.log.PatientAccessLogService;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.PatientAccessLogModel;
import net.fluance.cockpit.core.model.jdbc.patientfiles.PatientFile;
import net.fluance.cockpit.core.repository.jdbc.patientfiles.PatientFileRepository;
import net.fluance.cockpit.core.util.PatientAccessLogUtils;
import net.fluance.commons.lang.FluancePrintingMap;

public class PatientAccessLogCreatorPatientFile extends LogCreator {
	
	@Autowired
	private PatientFileRepository patientFileRepository;

	@Override
	public LogModel getLogModel() {
		super.init();
		PatientFile patientFile = patientFileRepository.findOne(Long.valueOf(params.getMap().get(PatientAccessLogService.RESOURCE_ID)[0]));
		PatientAccessLogModel log = new PatientAccessLogModel();
		log.setResourceId(params.getMap().get(PatientAccessLogService.RESOURCE_ID)[0]);
		log.setResourceType(resourceType.getResourceType());
		log.setDisplayName(patientFile.displayName());
		log.setHttpMethod(this.httpMethod);
		log.setParentPid(params.getMap().get(PatientAccessLogService.PID_KEY)[0]);
		log.setUri(this.uri);
		log.setParameters(this.params.toString());
		
		PatientAccessLogUtils.fillUserInformation(user, log);
		
		return log;
	}

	public PatientAccessLogCreatorPatientFile(MwAppResourceType resourceType, Object payload, Map<String, String[]> params, User user, String httpMethod, String uri) {
		this.resourceType = resourceType;
		this.payload = payload;
		this.params = new FluancePrintingMap<>(params);
		this.user = user;
		this.httpMethod = httpMethod;
		this.uri = uri;
	}
}
