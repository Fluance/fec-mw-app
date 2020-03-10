/**
 * 
 */
package net.fluance.cockpit.app.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.fluance.app.security.service.support.entitlement.EntitlementDecision;
import net.fluance.app.security.service.support.entitlement.PermissionEvaluateRequestBody;
import net.fluance.app.security.service.support.entitlement.PreparedPermissionEvaluateRequestBody;
import net.fluance.app.test.mock.AuthorizationServerMock;
import net.fluance.app.web.support.payload.response.GenericResponsePayload;

/**
 * @author Yves
 *
 */
@RestController
@PropertySource({"classpath:test.properties"})
@ComponentScan(basePackages = {"net.fluance.app.test"})
public class PermissionController {

	@Autowired
	protected AuthorizationServerMock authorizationServerMock;
	protected static final Map<String, String> DOMAIN_MAPPINGS = new HashMap<String, String>();
	private static final Logger LOGGER = LogManager.getLogger(PermissionController.class);
	@Value("${user.domain}")
	private String domain;
	@Value("${net.fluance.cockpit.app.web.company.userUsername}")
	private String allowedUsername;
	@Value("${net.fluance.cockpit.app.web.company.userPassword}")
	private String allowedPassword;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserUsername}")
	private String notAllowedUserUsername;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserPassword}")
	private String notAllowedPassword;
	
	private static final String FLUANCE_USERNAME = "fluance";
	private static final String PRIMARY = "PRIMARY";

	private List<String> allowedUsernames;
	
	static {
		DOMAIN_MAPPINGS.put("local", "PRIMARY");
		DOMAIN_MAPPINGS.put("fluance", "PRIMARY");
	}
	
	@PostConstruct
	public void init() {
		allowedUsernames = new ArrayList<>();
		allowedUsernames.add(PRIMARY + "/" + allowedUsername);
		allowedUsernames.add(PRIMARY + "/" + FLUANCE_USERNAME);
	}

	@RequestMapping(value = "/xacml/evaluate", method = RequestMethod.GET)
	public ResponseEntity<?> evaluate(@RequestBody(required = false) String payload, @RequestParam(required = false) String resource, @RequestParam(required = false) String username, @RequestParam(required = false) String domain, @RequestParam(required = false) String action, @RequestParam(name = "user_roles", required = false) List<String> roles, HttpServletRequest request,
			HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException {
		boolean isBodyContentRequest = (payload != null) && (resource == null) && (username == null) && (domain == null) && (action == null) && (roles == null);
		if(!isBodyContentRequest) {
			return evaluate(resource, username, domain, action, null);
		} else {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode payloadNode = mapper.readTree(payload);
			if(payloadNode == null || !payloadNode.has("resource") || !payloadNode.has("username") || !payloadNode.has("domain") || !payloadNode.has("action")) {
				return new ResponseEntity<GenericResponsePayload>(new GenericResponsePayload("resource, username, domain and action are mandatory"), HttpStatus.BAD_REQUEST);
			}
			PermissionEvaluateRequestBody body = null;
			if(payloadNode.has("user_roles")){
				body = mapper.readValue(payload, PreparedPermissionEvaluateRequestBody.class);
			} else {
				body = mapper.readValue(payload, PermissionEvaluateRequestBody.class);
			}
			return evaluate(body);
		}
	}

	/**
	 * 
	 * @param payload
	 * @return
	 */
	public ResponseEntity<?> evaluate(PermissionEvaluateRequestBody payload) {
		return evaluate(payload.getResource(), payload.getUsername(), payload.getDomain(), payload.getAction(), ((payload instanceof PreparedPermissionEvaluateRequestBody) ? ((PreparedPermissionEvaluateRequestBody)payload).getUserRoles() : null));
	}

	/**
	 * 
	 * @param resource
	 * @param username
	 * @param domain
	 * @param action
	 * @param roles
	 * @return
	 */
	private ResponseEntity<?> evaluate(String resource, String username, String domain, String action, List<String> roles) {
		
		if(username == null || username.isEmpty()) {
			return new ResponseEntity<EntitlementDecision>(EntitlementDecision.DENY, HttpStatus.FORBIDDEN);
		}
		
		String patternString = ".*http://localhost:8080/.*";
        Pattern pattern = Pattern.compile(patternString);

        Matcher matcher = pattern.matcher(resource);
        boolean resourceMatches = matcher.matches();
        
		boolean allowed = ("GET".equalsIgnoreCase(action) && resourceMatches && allowedUsernames.contains(((domain != null) ? domain : "") + "/" +username));

		if(allowed) {
			return new ResponseEntity<EntitlementDecision>(EntitlementDecision.PERMIT, HttpStatus.OK);
		} else {
			return new ResponseEntity<EntitlementDecision>(EntitlementDecision.DENY, HttpStatus.FORBIDDEN);
		}
	}
	
}
