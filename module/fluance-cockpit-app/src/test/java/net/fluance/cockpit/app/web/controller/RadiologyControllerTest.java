package net.fluance.cockpit.app.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
import org.junit.Before;
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
import net.fluance.cockpit.core.model.jdbc.radiology.Radiology;
import net.fluance.cockpit.core.model.jdbc.radiology.RadiologyReport;
import net.fluance.cockpit.core.repository.jdbc.radiology.RadiologyReportRepository;
import net.fluance.cockpit.core.repository.jdbc.radiology.RadiologyRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations={ "classpath:test.properties", "classpath:webapps/conf/mw-app/application.properties"})
public class RadiologyControllerTest extends AbstractWebIntegrationTest{

	private static Logger LOGGER = LogManager.getLogger(RadiologyControllerTest.class);
	
	private String url;
	private String pathSwaggerRadiologies;
	private String pathSwaggerRadiologiesReport;
	private String allowedUserAccessToken;
	private String notallowedUserAccessToken;
	
	@Value("${authorized.username}")
	private String allowedUsername;
	@Value("${authorized.password}")
	private String allowedPassword;
	@Value("${unauthorized.username}")
	private String notAllowedUsername;
	@Value("${unauthorized.password}")
	private String notAllowedPassword;
	
	private String domain = "PRIMARY";

	private Swagger swaggerYamlController;
	@Value("${test.swagger.generated}")
	private String swaggerGeneratedPath;

	// Props
	@Value("${swagger.specs.location}")
	private String specsLocation;
	@Value("${test.host.local}")
	private String host;
	
	@Autowired
	protected WebApplicationContext wac;
	
	@Autowired
	private RadiologyRepository radiologyRepository;
	
	@Autowired
	private RadiologyReportRepository radiologyReportRepository;
	
	@Before
	public void setUp() throws KeyManagementException, UnsupportedOperationException, NoSuchAlgorithmException, KeyStoreException, IOException, HttpException, URISyntaxException {
		url = host + serverPort;
		swaggerYamlController = SwaggerSpecUtils.load(url + swaggerGeneratedPath);
		authBaseUrl = "http://localhost:" + serverPort;
		pathSwaggerRadiologies = "/radiologies";
		pathSwaggerRadiologiesReport = "/radiology/reports";
		allowedUserAccessToken = getAccessToken(allowedUsername, domain);
		notallowedUserAccessToken = getAccessToken(notAllowedUsername, domain);

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void testGetRadiologiesByPidOk() throws Exception{
		Radiology radiology = new Radiology("serieUid", (long)123, 123, "orderNb", "orderObs", "orderUrl", "diagnosticService", "dsDescription", "serieObs", new java.util.Date());
		List<Radiology> radiologies = Arrays.asList(radiology);
		when(radiologyRepository.findByPatientId((long)123)).thenReturn(radiologies);

		CloseableHttpResponse response = sendRequest(url + pathSwaggerRadiologies + "?pid=123" , allowedUserAccessToken);
		LOGGER.info(url + pathSwaggerRadiologies + "?pid=123" );
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSwaggerRadiologies, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSwaggerRadiologies, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}
	
	@Test
	public void testGetRadiologiesByPidUnauthorized() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathSwaggerRadiologies, "");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
		
		// INVALID ACCES_TOKEN
		response = sendRequest(url + pathSwaggerRadiologies, "FAKE_ACCESS_TOKEN");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testGetRadiologiesByPidForbidden() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathSwaggerRadiologies, notallowedUserAccessToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testGetRadiologyReportsByPidOk() throws Exception{
		RadiologyReport radiology = new RadiologyReport("studyUid", (long)123, 1, "orderNb", null, "report", "completion", "verification", "referringPhysician", "recordPhysician", "performingPhysician", "readingPhysician",(long)1);
		List<RadiologyReport> radiologies = Arrays.asList(radiology);
		when(radiologyReportRepository.findByPatientId((long)123, null, null, 10, 0)).thenReturn(radiologies);

		CloseableHttpResponse response = sendRequest(url + pathSwaggerRadiologiesReport + "?pid=123" , allowedUserAccessToken);
		LOGGER.info(url + pathSwaggerRadiologiesReport + "?pid=123" );
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSwaggerRadiologiesReport, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSwaggerRadiologiesReport, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}
	
	@Test
	public void testGetRadiologiesReportByPidUnauthorized() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathSwaggerRadiologiesReport, "");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
		
		// INVALID ACCES_TOKEN
		response = sendRequest(url + pathSwaggerRadiologiesReport, "FAKE_ACCESS_TOKEN");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testGetRadiologiesReportByPidForbidden() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathSwaggerRadiologiesReport, notallowedUserAccessToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
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
		return null;
	}

}
