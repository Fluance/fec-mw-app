package net.fluance.cockpit.app.domain.log;

import java.util.List;
import java.util.Map;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogCreator;
import net.fluance.app.log.model.LogModel;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.PatientAccessLogModel;
import net.fluance.cockpit.core.model.wrap.patient.Patient;
import net.fluance.cockpit.core.model.wrap.patient.PatientInList;
import net.fluance.cockpit.core.util.PatientAccessLogUtils;
import net.fluance.commons.lang.FluancePrintingMap;

public class PatientAccessLogCreatorPatientDetail extends LogCreator {

	@Override
	@SuppressWarnings("unchecked")
	public LogModel getLogModel() {
		super.init();
		PatientAccessLogModel log = new PatientAccessLogModel();
		if(payload instanceof Patient && payload != null ){
			Patient p = (Patient) payload;
			log.setResourceId(p.getPatientInfo().getPid().toString());
			log.setDisplayName(p.displayName());
			log.setParentPid(p.getPatientInfo().getPid().toString());
		} else if (payload instanceof List && payload !=null && ((List<PatientInList>)payload).size() > 0){
			PatientInList p = ((List<PatientInList>)payload).get(0);
			log.setResourceId(p.getPatientInfo().getPid().toString());
			log.setDisplayName(p.displayName());
			log.setParentPid(p.getPatientInfo().getPid().toString());
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
	public PatientAccessLogCreatorPatientDetail(MwAppResourceType resourceType, Object payload, Map<String, String[]> params, User user, String httpMethod, String uri){
		this.resourceType = resourceType;
		this.payload = payload;
		this.params = new FluancePrintingMap<>(params);
		this.user = user;
		this.httpMethod = httpMethod;
		this.uri = uri;
	}
}
