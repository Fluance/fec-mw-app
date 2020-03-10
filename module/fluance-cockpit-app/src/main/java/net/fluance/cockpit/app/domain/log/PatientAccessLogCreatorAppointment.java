package net.fluance.cockpit.app.domain.log;

import java.util.Map;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogCreator;
import net.fluance.app.log.model.LogModel;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.PatientAccessLogModel;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentDetail;
import net.fluance.cockpit.core.util.PatientAccessLogUtils;
import net.fluance.commons.lang.FluancePrintingMap;

public class PatientAccessLogCreatorAppointment extends LogCreator{

	@Override
	public LogModel getLogModel() {
		super.init();
		PatientAccessLogModel log = new PatientAccessLogModel();
		Long visitNb = null;
		Long pid = null;
		visitNb = ((AppointmentDetail)payload).getVisit().getVisitNb();
		pid = ((AppointmentDetail) payload).getPatient().getPid();
		log.setResourceId(((AppointmentDetail) payload).getAppointmentId().toString());
		log.setResourceType(resourceType.getResourceType());
		log.setDisplayName(((AppointmentDetail)payload).displayName());
		log.setHttpMethod(this.httpMethod);
		log.setParentPid(pid.toString());
		log.setParentVisitNb(visitNb.toString());
		log.setUri(this.uri);
		log.setParameters(this.params.toString());
		
		PatientAccessLogUtils.fillUserInformation(user, log);
		
		return log;
	}

	public PatientAccessLogCreatorAppointment(MwAppResourceType resourceType, Object payload, Map<String, String[]> params, User user, String httpMethod, String uri){
		this.resourceType = resourceType;
		this.payload = payload;
		this.params = new FluancePrintingMap<>(params);
		this.user = user;
		this.httpMethod = httpMethod;
		this.uri = uri;
	}
}
