package net.fluance.cockpit.app.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.web.wrapper.ResponseWrapper;
import net.fluance.cockpit.app.service.security.SinglePatientService;

@Component
public class DomainPermissionFilter extends GenericFilterBean {

	private Logger logger = LogManager.getLogger();
	
	@Autowired
	private SinglePatientService singlePatientService;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		User user = (User) request.getAttribute(User.USER_KEY);
		if(user.isPatient() && !checkPidPermission(user, (HttpServletRequest) request)){
			ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);
			responseWrapper.setContentType("application/json");
			responseWrapper.setStatus(HttpServletResponse.SC_FORBIDDEN);
			responseWrapper.getWriter().write("PID : " + user.getPid() + " is not Allowed to access to this Resource.");
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * Apply PID verification
	 * @param authzUser
	 * @return is permitted or not
	 */
	private boolean checkPidPermission(User authzUser, HttpServletRequest request) {
		try {
			boolean patientPermission = singlePatientService.isPatientPermitted(request, authzUser);
			logger.info("Patient PID = " + authzUser.getPid() + " is " + (patientPermission ? "" : "NOT") + " allowed to perform a " + request.getMethod() + " at " + request.getRequestURI());
			return patientPermission;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("", e);
			return false;
		}
	}
}
