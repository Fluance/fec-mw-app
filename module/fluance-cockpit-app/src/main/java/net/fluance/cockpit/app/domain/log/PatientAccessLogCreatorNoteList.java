package net.fluance.cockpit.app.domain.log;

import java.util.List;
import java.util.Map;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogCreator;
import net.fluance.app.log.model.LogModel;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.PatientAccessLogModel;
import net.fluance.cockpit.core.util.PatientAccessLogUtils;
import net.fluance.commons.lang.FluancePrintingMap;

public class PatientAccessLogCreatorNoteList extends LogCreator {
	
	@Override
	@SuppressWarnings("unchecked")
	public LogModel getLogModel() {
		super.init();
		PatientAccessLogModel log = new PatientAccessLogModel();
		if (payload instanceof List && payload !=null){
			if(params.getMap().get("noteid") != null){ // search by category
				log.setResourceId(params.getMap().get("noteid")[0]);
				log.setDisplayName("note");
			} else if(params.getMap().get("visitNb") != null ) { // search by visitNb
				log.setResourceId(params.getMap().get("visitNb")[0]);
				log.setDisplayName("visitNb");
			} else if(params.getMap().get("pid") != null ) { // search by patientId
				log.setResourceId(params.getMap().get("pid")[0]);
				log.setDisplayName("pid");
			}
			// set parent parent PID or parent visitNb
			if(params.getMap().get("visitNb") != null ) { // search by visitNb
				log.setParentVisitNb(params.getMap().get("visitNb")[0]);
			} else if(params.getMap().get("pid") != null ) { // search by patientId
				log.setParentPid(params.getMap().get("pid")[0]);
			}
		} else {
			throw new IllegalArgumentException("Unable to build Patient Access Logs of Note List for :" + payload);
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
	public PatientAccessLogCreatorNoteList(MwAppResourceType resourceType, Object payload, Map<String, String[]> params, User user, String httpMethod, String uri){
		this.resourceType = resourceType;
		this.payload = payload;
		this.params = new FluancePrintingMap<>(params);
		this.user = user;
		this.httpMethod = httpMethod;
		this.uri = uri;
	}
}
