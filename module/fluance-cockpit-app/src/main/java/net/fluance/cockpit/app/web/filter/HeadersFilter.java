package net.fluance.cockpit.app.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import net.fluance.app.web.util.RequestHelper;

@Component
public class HeadersFilter extends GenericFilterBean {

	public static final String GENERATE_PDF_REGEX = "\\/patients\\/(\\d*)\\/pdf\\/generate";
	public static final String GENERATE_WHITEBOARD_PDF_REGEX = "\\/whiteboards\\/companies\\/(\\d+)\\/services\\/([a-zA-Z0-9]+)\\/print";
	
	@Autowired
	private RequestHelper requestHelper;
	
	@Value("${app.version}")
	private String apiVersion;

	@Value("${app.header.version}")
	private String headerVersionName;

	@Value("${app.header.from-public-network}")
	private String fromPublicNetworkHeader;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		((HttpServletResponse) response).addHeader(headerVersionName, apiVersion);
		processRequestForwardedFor((HttpServletRequest)request, (HttpServletResponse)response);
		processAccessControlExpose((HttpServletRequest)request, (HttpServletResponse)response);
		chain.doFilter(request, response);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	private void processRequestForwardedFor(HttpServletRequest request, HttpServletResponse response) {
		boolean isForwardedForHeaderPresent = requestHelper.hasHeader(request, RequestHelper.FORWARDED_FOR_HEADER);
		if(isForwardedForHeaderPresent) {
			response.addHeader(fromPublicNetworkHeader, request.getHeader(RequestHelper.FORWARDED_FOR_HEADER));
		}
	}
	
	/**
	 * Change the <code>Access-Control-Expose-Headers</code> in the Response when the Request is a PDF solicitude to <code>Content-Disposition</code>
	 * 
	 * <ul>
	 * <li><b>Generata Data Patient PDF:</b> /patients/{pid}/pdf/generate</li>
	 * </ul>
	 * @param request
	 * @param response
	 */
	private void processAccessControlExpose(HttpServletRequest request, HttpServletResponse response) {
		if(request.getServletPath().matches(GENERATE_PDF_REGEX) || request.getServletPath().matches(GENERATE_WHITEBOARD_PDF_REGEX)) {
			response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
		}		
	}

}
