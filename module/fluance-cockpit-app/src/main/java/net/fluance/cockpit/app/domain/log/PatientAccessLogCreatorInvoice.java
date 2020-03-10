package net.fluance.cockpit.app.domain.log;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogCreator;
import net.fluance.app.log.model.LogModel;
import net.fluance.cockpit.app.service.log.PatientAccessLogService;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.PatientAccessLogModel;
import net.fluance.cockpit.core.model.PayloadDisplayLogName;
import net.fluance.cockpit.core.model.jdbc.invoice.Invoice;
import net.fluance.cockpit.core.model.jdbc.visit.VisitDetail;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitDetailRepository;
import net.fluance.cockpit.core.util.PatientAccessLogUtils;
import net.fluance.commons.lang.FluancePrintingMap;

public class PatientAccessLogCreatorInvoice extends LogCreator {

	@Autowired
	private VisitDetailRepository visitDetailRepository;

	@Override
	public LogModel getLogModel() {
		super.init();
		PatientAccessLogModel log = new PatientAccessLogModel();
		Long visitNb = null;
		if(resourceType == MwAppResourceType.INVOICE && payload instanceof Invoice){
			visitNb = ((Invoice)payload).getVisit_nb();
			log.setResourceId(((Invoice)payload).getId().toString());
			log.setDisplayName(((PayloadDisplayLogName)payload).displayName());
		} else if (resourceType == MwAppResourceType.INVOICE_LIST_BY_GUARANTOR && payload instanceof List){
			visitNb = Long.valueOf(params.getMap().get(PatientAccessLogService.VISITNB_KEY)[0]);
			if(params.getMap().get(PatientAccessLogService.RESOURCE_ID) != null) {
				log.setResourceId(params.getMap().get(PatientAccessLogService.RESOURCE_ID)[0]);
			}
		}
		VisitDetail v = visitDetailRepository.findOne(visitNb);
		log.setResourceType(resourceType.getResourceType());
		log.setHttpMethod(this.httpMethod);
		
		String patientId = new String();
		if(v != null && v.getPatientId() != null){
			patientId = v.getPatientId().toString();
		}		
		log.setParentPid(patientId);
		
		log.setParentVisitNb(visitNb.toString());
		log.setUri(this.uri);
		log.setParameters(this.params.toString());
		
		PatientAccessLogUtils.fillUserInformation(user, log);
		
		return log;
	}

	public PatientAccessLogCreatorInvoice(MwAppResourceType resourceType, Object payload, Map<String, String[]> params, User user, String httpMethod, String uri){
		this.resourceType = resourceType;
		this.payload = payload;
		this.params = new FluancePrintingMap<>(params);
		this.user = user;
		this.httpMethod = httpMethod;
		this.uri = uri;
	}
}
