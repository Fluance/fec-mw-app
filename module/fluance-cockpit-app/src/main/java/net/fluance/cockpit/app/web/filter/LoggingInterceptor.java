package net.fluance.cockpit.app.web.filter;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoggingInterceptor implements HandlerInterceptor {

	private static Logger LOGGER = LogManager.getLogger(LoggingInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String fullyQualifiedAuthorizedUsername = (String) request.getAttribute("user");
		String ip = request.getRemoteAddr();
		LOGGER.info(request.getServletPath() + " | User : " + fullyQualifiedAuthorizedUsername + " | IP Address : " + ip + " | Processing request...");
		String parameters = "{";
		List<String> requestParameterNames = Collections.list((Enumeration<String>)request.getParameterNames());
		for (String parameterName : requestParameterNames){
			parameters += parameterName + "=" +request.getParameter(parameterName) + ",";
		}
		parameters += "}";
		LOGGER.info(request.getServletPath() + " | User : " + fullyQualifiedAuthorizedUsername + " | Parameters : " + parameters);
		long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String fullyQualifiedAuthorizedUsername = (String) request.getAttribute("user");
		LOGGER.info(request.getServletPath() + " | User : " + fullyQualifiedAuthorizedUsername + " | Done.");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		long startTime = (Long) request.getAttribute("startTime");
		String fullyQualifiedAuthorizedUsername = (String) request.getAttribute("user");
		LOGGER.debug(request.getServletPath().toString() + " | User : " + fullyQualifiedAuthorizedUsername +  " | Time = " + (System.currentTimeMillis() - startTime + " ms"));
	}
}
