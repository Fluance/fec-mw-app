package net.fluance.cockpit.app.domain.log;

import java.util.Map;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogCreator;
import net.fluance.app.log.model.LogModel;
import net.fluance.cockpit.app.service.log.PatientAccessLogService;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.PatientAccessLogModel;
import net.fluance.cockpit.core.util.PatientAccessLogUtils;
import net.fluance.commons.lang.FluancePrintingMap;


public class PatientAccessLogCreatorPictureList extends LogCreator {

	@Override
	@SuppressWarnings("unchecked")
	public LogModel getLogModel() {
		super.init();
		PatientAccessLogModel log = new PatientAccessLogModel();

//		set noteId like ResourceId or parent parent PID or parent visitNb
		if (params.getMap().get("visitNb") != null) { // search by visitNb
			log.setParentVisitNb(params.getMap().get("visitNb")[0]);
		} 
		if (params.getMap().get("pid") != null) { // search by patientId
			log.setParentPid(params.getMap().get("pid")[0]);
		}
		if (params.getMap().get(PatientAccessLogService.RESOURCE_ID) != null) {
			// search by noteId
			log.setResourceId(params.getMap().get(PatientAccessLogService.RESOURCE_ID)[0]);
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
	 * 
	 * @param resourceType
	 * @param payload
	 * @param params
	 * @param user
	 * @param httpMethod
	 * @throws UnsupportedOperationException
	 */
	public PatientAccessLogCreatorPictureList(MwAppResourceType resourceType, Object payload, Map<String, String[]> params, User user, String httpMethod, String uri) {
		this.resourceType = resourceType;
		this.payload = payload;
		this.params = new FluancePrintingMap<>(params);
		this.user = user;
		this.httpMethod = httpMethod;
		this.uri = uri;
	}
}
