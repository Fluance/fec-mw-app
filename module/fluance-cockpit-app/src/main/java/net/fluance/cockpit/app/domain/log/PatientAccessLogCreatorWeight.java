package net.fluance.cockpit.app.domain.log;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogCreator;
import net.fluance.app.log.model.LogModel;
import net.fluance.cockpit.app.service.log.PatientAccessLogService;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.PatientAccessLogModel;
import net.fluance.cockpit.core.model.jpa.patient.Weight;
import net.fluance.cockpit.core.repository.jpa.patient.PatientWeightRepository;
import net.fluance.cockpit.core.util.PatientAccessLogUtils;
import net.fluance.commons.lang.FluancePrintingMap;

public class PatientAccessLogCreatorWeight extends LogCreator {
	
	@Autowired
	PatientWeightRepository patientWeightRepository;

	@Override
	public LogModel getLogModel() {
		super.init();
		PatientAccessLogModel log = new PatientAccessLogModel();
		
		Weight weight = new Weight();
		if (payload instanceof Weight && payload != null) {
			weight = (Weight) payload;
		} else if (params.getMap().get(PatientAccessLogService.RESOURCE_ID) != null) {
			weight = patientWeightRepository.findOne(Long.valueOf(params.getMap().get(PatientAccessLogService.RESOURCE_ID)[0]));
		}
		log.setResourceId(weight!=null? weight.getId().toString() : params.getMap().get(PatientAccessLogService.RESOURCE_ID)[0]);
		
		log.setResourceType(resourceType.getResourceType());
		log.setHttpMethod(this.httpMethod);
		log.setUri(this.uri);
		log.setParameters(this.params.toString());
		
		PatientAccessLogUtils.fillUserInformation(user, log);
		
		return log;
	}

	/**
	 * Builds PatientAccessLogCreatorWeight Creator Based on the {@link Weight} payload
	 * @param resourceType
	 * @param payload
	 * @param params
	 * @param user
	 * @param httpMethod
	 * @throws UnsupportedOperationException
	 */
	public PatientAccessLogCreatorWeight(MwAppResourceType resourceType, Object payload, Map<String, String[]> params, User user, String httpMethod, String uri){
		this.resourceType = resourceType;
		this.payload = payload;
		this.params = new FluancePrintingMap<>(params);
		this.user = user;
		this.httpMethod = httpMethod;
		this.uri = uri;
	}
}