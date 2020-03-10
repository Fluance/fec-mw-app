package net.fluance.cockpit.app.domain.log;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogCreator;
import net.fluance.app.log.model.LogModel;
import net.fluance.cockpit.app.service.log.PatientAccessLogService;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.PatientAccessLogModel;
import net.fluance.cockpit.core.model.PayloadDisplayLogName;
import net.fluance.cockpit.core.model.jdbc.guarantor.GuarantorDetail;
import net.fluance.cockpit.core.model.jdbc.visit.VisitDetail;
import net.fluance.cockpit.core.model.jdbc.visit.VisitPolicyDetail;
import net.fluance.cockpit.core.repository.jdbc.guarantor.GuarantorRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitDetailRepository;
import net.fluance.cockpit.core.util.PatientAccessLogUtils;
import net.fluance.commons.lang.FluancePrintingMap;

public class PatientAccessLogCreatorVisitNb extends LogCreator {

	@Autowired
	private VisitDetailRepository visitDetailRepository;

	@Autowired
	private GuarantorRepository guarantorRepository;

	@Override
	public LogModel getLogModel() {
		super.init();
		VisitDetail v = visitDetailRepository.findOne(Long.valueOf(params.getMap().get(PatientAccessLogService.VISITNB_KEY)[0]));
		PatientAccessLogModel log = new PatientAccessLogModel();
		log.setResourceType(resourceType.getResourceType());
		log.setDisplayName("");
		log.setHttpMethod(this.httpMethod);
		log.setParentPid(v.getPatientId().toString());
		log.setParentVisitNb(params.getMap().get(PatientAccessLogService.VISITNB_KEY)[0]);
		if(payload !=null && payload instanceof Persistable){
			log.setResourceId(((Persistable) payload).getId().toString());
		}
		if(payload !=null && payload instanceof PayloadDisplayLogName){
			log.setDisplayName(((PayloadDisplayLogName) payload).displayName());
		}
		if (resourceType == MwAppResourceType.POLICY_DETAIL && payload instanceof VisitPolicyDetail) {
			GuarantorDetail guarantor = guarantorRepository.findOne(((VisitPolicyDetail) payload).getGuarantor_id());
			log.setDisplayName(guarantor.displayName() + "/" + ((PayloadDisplayLogName) payload).displayName());
		}
		log.setUri(this.uri);
		log.setParameters(this.params.toString());
		
		PatientAccessLogUtils.fillUserInformation(user, log);
		
		return log;
	}

	public PatientAccessLogCreatorVisitNb(MwAppResourceType resourceType, Object payload, Map<String, String[]> params, User user, String httpMethod, String uri){
		this.resourceType = resourceType;
		this.payload = payload;
		this.params = new FluancePrintingMap<>(params);
		this.user = user;
		this.httpMethod = httpMethod;
		this.uri = uri;
	}
}
