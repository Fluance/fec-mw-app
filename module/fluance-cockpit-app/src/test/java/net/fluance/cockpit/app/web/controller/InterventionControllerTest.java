package net.fluance.cockpit.app.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.HttpException;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.xml.sax.SAXException;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import io.swagger.models.Swagger;
import net.fluance.app.test.AbstractWebIntegrationTest;
import net.fluance.app.web.util.swagger.SwaggerSpecUtils;
import net.fluance.cockpit.Application;
import net.fluance.cockpit.app.service.domain.InterventionService;
import net.fluance.cockpit.core.model.jdbc.intervention.Diagnosis;
import net.fluance.cockpit.core.model.jdbc.intervention.Intervention;
import net.fluance.cockpit.core.model.jdbc.intervention.Operation;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class InterventionControllerTest extends AbstractWebIntegrationTest{
	
private static Logger LOGGER = LogManager.getLogger(InterventionControllerTest.class);
	
	private String url;
	private Swagger swaggerYamlController;
	private String okUserToken;
	private String notAllowedToken;
	private String pathIntervention;
	
	@Value("${test.swagger.generated}")
	private String swaggerGeneratedPath;
	
	@Value("${test.host.local}")
	private String host;
	@Value("${net.fluance.cockpit.app.web.company.userUsername}")
	private String companyUserUsername;
	@Value("${net.fluance.cockpit.app.web.company.userPassword}")
	private String companyUserPassword;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserUsername}")
	private String companyNotAllowedUserUsername;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserPassword}")
	private String companyNotAllowedUserPassword;
	private String domain = "PRIMARY";
	
	@Autowired
    protected WebApplicationContext wac;
	
	@Autowired 
	private InterventionService interventionService;
	
	@Override
	public void setUp() throws Exception {
		url = host + serverPort;
		swaggerYamlController = SwaggerSpecUtils.load(url + swaggerGeneratedPath);
		baseUrl = "http://localhost:" + serverPort;
		pathIntervention = "/intervention";
		
		okUserToken = getAccessToken(companyUserUsername, domain);
		notAllowedToken = getAccessToken(companyNotAllowedUserUsername, domain);
		
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void testInterventionDetailControllerOk() throws Exception {
		List<Diagnosis> diagnosis = new ArrayList<>();
		List<Operation> operations =  new ArrayList<>();
		Intervention intervention = new Intervention((long)123, new java.sql.Timestamp((long)123), operations, diagnosis);
		when(interventionService.getByVisitnb((long)anyInt())).thenReturn(intervention);

		CloseableHttpResponse response = sendRequest(url + pathIntervention +"?visitnb=123", okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathIntervention +"?visitnb=123", HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathIntervention +"?visitnb=123", HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}
	
	@Test
	public void testInterventionDetailControllerUnAuthorized() throws Exception {
		CloseableHttpResponse response = sendRequest(url + pathIntervention +"?visitnb=123", "");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
		
		// INVALID ACCES_TOKEN
		response = sendRequest(url + pathIntervention +"?visitnb=123", "FAKE_ACCESS_TOKEN");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testInterventionDetailControllerBadRequest() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathIntervention +"?visitnb=123", okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testInterventionDetailControllerForbidden() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathIntervention +"?visitnb=123" , notAllowedToken);
		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusLine().getStatusCode());
	}
	
	@Override
	public void tearDown() {
	}

	@Override
	protected boolean checkOAuth2Authorization(Object... params) {
		return false;
	}

	@Override
	public void checkOk(Object... params) throws KeyManagementException, UnsupportedOperationException,
			NoSuchAlgorithmException, KeyStoreException, IOException, HttpException, URISyntaxException,
			ProcessingException, NumberFormatException, ParseException, XPathExpressionException,
			ParserConfigurationException, SAXException, TransformerFactoryConfigurationError, TransformerException {
		
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}
}
