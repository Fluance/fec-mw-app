package net.fluance.cockpit.app.domain.log;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogCreator;
import net.fluance.app.log.LogCreatorFactory;
import net.fluance.app.log.ResourceType;
import net.fluance.cockpit.core.domain.MwAppResourceType;

@Component
public class PatientAccessLogCreatorFactory implements LogCreatorFactory {

	private @Autowired AutowireCapableBeanFactory beanFactory;

	@Override
	public LogCreator instanciateLogCreator(ResourceType resourceType, Object payload, Map<String, String[]> params,
			User user, String httpMethod, String uri) {
		switch ((MwAppResourceType) resourceType) {
		case PATIENT_DETAIL:
			LogCreator patientAccessLogCreatorPatientDetail = (LogCreator) new PatientAccessLogCreatorPatientDetail((MwAppResourceType)resourceType, payload, params, user, httpMethod, uri);
			beanFactory.autowireBean(patientAccessLogCreatorPatientDetail);
			return patientAccessLogCreatorPatientDetail;
		case LAB:
		case RADIOLOGY_EXAMS:
		case VISIT_LIST:
		case LOGS_PATIENT:
		case AppointmentList:
		case Oxygen:
		case PATIENT_FILES_LIST:
			LogCreator logCreator = new PatientAccessLogCreatorPID((MwAppResourceType) resourceType, payload, params, user, httpMethod, uri);
			beanFactory.autowireBean(logCreator);
			return logCreator;
		case PATIENT_EXERCISE:
			LogCreator logCreatorExercise = new PatientAccessLogCreatorExercise((MwAppResourceType) resourceType, payload, params, user, httpMethod, uri);
			beanFactory.autowireBean(logCreatorExercise);
			return logCreatorExercise;
		case PATIENT_WEIGHT:
			LogCreator logCreatorWeight = new PatientAccessLogCreatorWeight((MwAppResourceType) resourceType, payload, params, user, httpMethod, uri);
			beanFactory.autowireBean(logCreatorWeight);
			return logCreatorWeight;
		case DIAGNOSIS_TREATMENTS:
		case GUARANTOR:
		case GUARANTOR_LIST:
		case POLICIES:
		case POLICY_DETAIL:
		case VISIT:
		case INTERVENTION:
		case Synlab:
			LogCreator logCreatorVisit = new PatientAccessLogCreatorVisitNb((MwAppResourceType) resourceType, payload, params, user, httpMethod, uri);
			beanFactory.autowireBean(logCreatorVisit);
			return logCreatorVisit;
		case INVOICE_LIST_BY_GUARANTOR:
		case INVOICE:
			LogCreator logCreatorInvoice = new PatientAccessLogCreatorInvoice((MwAppResourceType) resourceType, payload, params, user, httpMethod, uri);
			beanFactory.autowireBean(logCreatorInvoice);
			return logCreatorInvoice;
		case AppointmentDetail:
			LogCreator logCreatorAppointment = new PatientAccessLogCreatorAppointment((MwAppResourceType) resourceType, payload, params, user, httpMethod, uri);
			beanFactory.autowireBean(logCreatorAppointment);
			return logCreatorAppointment;
		case NOTE:
			LogCreator noteLogCreator = new PatientAccessLogCreatorNote((MwAppResourceType) resourceType, payload, params, user, httpMethod, uri);
			beanFactory.autowireBean(noteLogCreator);
			return noteLogCreator;
		case NOTE_LIST:
			LogCreator noteListLogCreator = new PatientAccessLogCreatorNoteList((MwAppResourceType) resourceType, payload, params, user, httpMethod, uri);
			beanFactory.autowireBean(noteListLogCreator);
			return noteListLogCreator;

		case PICTURE:
			LogCreator pictureLogCreator = new PatientAccessLogCreatorPicture((MwAppResourceType) resourceType, payload, params, user, httpMethod, uri);
			beanFactory.autowireBean(pictureLogCreator);
			return pictureLogCreator;
		case PICTURE_LIST:
			LogCreator pictureListLogCreator = new PatientAccessLogCreatorPictureList((MwAppResourceType) resourceType, payload, params, user, httpMethod, uri);
			beanFactory.autowireBean(pictureListLogCreator);
			return pictureListLogCreator;
		case PATIENT_FILE:
			LogCreator patientFileLogCreator = (LogCreator) new PatientAccessLogCreatorPatientFile((MwAppResourceType)resourceType, payload, params, user, httpMethod, uri);
			beanFactory.autowireBean(patientFileLogCreator);
			return patientFileLogCreator;
		case WHITEBOARD_ENTRY:
		case WHITEBOARD_LIST:
			LogCreator whiteBoardDetailLogCreator = (LogCreator) new PatientAccessLogCreatorWhiteBoardDetail((MwAppResourceType)resourceType, payload, params, user, httpMethod, uri);
			beanFactory.autowireBean(whiteBoardDetailLogCreator);
			return whiteBoardDetailLogCreator;
		default:
			break;
		}
		return null;
	}

}
