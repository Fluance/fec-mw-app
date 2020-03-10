package net.fluance.cockpit.app.service.log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.log.LogCreator;
import net.fluance.app.log.LogCreatorFactory;
import net.fluance.app.log.LogService;
import net.fluance.app.log.ResourceType;
import net.fluance.app.log.model.LogModel;
import net.fluance.app.web.service.CommonSystemAccessLogService;
import net.fluance.cockpit.core.domain.MwAppResourceType;
import net.fluance.cockpit.core.model.PatientAccessLogModel;
import net.fluance.cockpit.core.repository.jdbc.accesslog.PALIgnoredUsersDao;

/**
 * Service that implement the LogService to save on DB the logs
 */
@Service
public class PatientAccessLogService implements LogService {

	public static String PID_KEY = "pid";
	public static String VISITNB_KEY = "vnb";
	public static String RESOURCE_ID = "resourceId";

	private Logger patientAccessLogger = LogManager.getLogger();
	private Logger systemAccessLogger = LogManager.getLogger(CommonSystemAccessLogService.class);
	private Logger rootLogger = LogManager.getRootLogger();

	@Autowired
	private LogCreatorFactory patientAccessLogCreatorFactory;

	@Autowired
	private PALIgnoredUsersDao patientAccessLogIgnoredUsersDao;

	@Async
	public void log(ResourceType resourceType, Object payload, Map<String, String[]> params, User user,
			String httpMethod, String uri, String resourceId) {
		log(resourceType, payload, params, user, httpMethod, null, null, uri, resourceId);
	}

	@Async
	public Future<String> log(ResourceType resourceType, Object payload, Map<String, String[]> parameters, User user,
			String httpMethod, Long parentPid, Long parentVn, String uri, String resourceId) {
		Map<String, String[]> notLockedparams = new HashMap<String, String[]>(parameters);
		if (parentPid != null) {
			notLockedparams.put(PID_KEY, new String[] { parentPid.toString() });
		}
		if (parentVn != null) {
			notLockedparams.put(VISITNB_KEY, new String[] { parentVn.toString() });
		}
		if (resourceId != null) {
			notLockedparams.put(RESOURCE_ID, new String[] { resourceId });
		}

		if (resourceType == null || resourceType.getResourceType() == null
				|| resourceType.getResourceType().equals(MwAppResourceType.UNKNOWN.getResourceType())) {
			throw new IllegalArgumentException("The Resource is Unknown");
		}

		try {
			LogModel log = buildLogModel(resourceType, payload, notLockedparams, user, httpMethod, uri, resourceId);
			initializeLog4jThreadContext((PatientAccessLogModel) log);
			if (userIsIgnored(user)) {
				rootLogger
						.info(user.getDomain() + "/" + user.getUsername() + " is Ignored by the Patient Access Logger");
				systemAccessLogger.info("Saving System Access Log into Database");
			} else {
				patientAccessLogger.info("Saving Patient Access Log into Database");
			}
		} catch (Exception e) {
			rootLogger.error("Unable to save Patient Access Log", e);
		}

		return new AsyncResult<String>("Log Done");

	}

	public LogModel buildLogModel(ResourceType resourceType, Object payload, Map<String, String[]> params, User user,
			String httpMethod, String uri, String resourceId) throws UnsupportedOperationException {
		LogCreator logCreator = patientAccessLogCreatorFactory.instanciateLogCreator(resourceType, payload, params,
				user, httpMethod, uri);
		
		return logCreator.getLogModel();
	}

	private void initializeLog4jThreadContext(PatientAccessLogModel log) {
		ThreadContext.put("username", log.getUserName());
		ThreadContext.put("firstName", log.getFirstName());
		ThreadContext.put("lastName", log.getLastName());
		ThreadContext.put("resourceId", log.getResourceId());
		ThreadContext.put("resourceType", log.getResourceType());
		ThreadContext.put("httpMethod", log.getHttpMethod());
		ThreadContext.put("displayName", log.getDisplayName());
		if (log.getParentVisitNb() != null) {
			ThreadContext.put("parentVisitNb", log.getParentVisitNb());
		}
		if (log.getParentPid() != null) {
			ThreadContext.put("parentPid", log.getParentPid());
		}
		ThreadContext.put("uri", log.getUri());
		ThreadContext.put("parameters", log.getParameters());
		
		if(log.getActualUserName() != null) {
			ThreadContext.put("actualUserName", log.getActualUserName());	
		}
		if(log.getActualFirstName() != null) {
			ThreadContext.put("actualFirstName", log.getActualFirstName());	
		}
		if(log.getActualLastName() != null) {
			ThreadContext.put("actualLastName", log.getActualLastName());	
		}		
		if(log.getActualEmail() != null) {
			ThreadContext.put("actualEmail", log.getActualEmail());	
		}
	}

	private boolean userIsIgnored(User user) {
		List<String> ignoredUsers = patientAccessLogIgnoredUsersDao.getAll();
		String appuser = user.getDomain() + "/" + user.getUsername();
		return ignoredUsers.contains(appuser);
	}
}
