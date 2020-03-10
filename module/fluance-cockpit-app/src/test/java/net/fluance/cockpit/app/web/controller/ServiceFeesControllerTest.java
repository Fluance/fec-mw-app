package net.fluance.cockpit.app.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
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
import org.junit.Ignore;
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
import net.fluance.cockpit.core.model.jdbc.servicefees.ServiceFeesDetail;
import net.fluance.cockpit.core.model.jdbc.servicefees.ServiceFeesList;
import net.fluance.cockpit.core.repository.jdbc.servicefees.ServiceFeesDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.servicefees.ServiceFeesListRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class ServiceFeesControllerTest extends AbstractWebIntegrationTest{

	private static Logger LOGGER = LogManager.getLogger(ServiceFeesControllerTest.class);
	
	private String url;
	private Swagger swaggerYamlController;
	private String okUserToken;
	private String notAllowedToken;
	private String pathBenefit;
	
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
	private ServiceFeesDetailRepository serviceFeesDetailRepository;
	
	@Autowired
	private ServiceFeesListRepository serviceFeesListRepository;
	
	@Override
	public void setUp() throws Exception {
		url = host + serverPort;
		swaggerYamlController = SwaggerSpecUtils.load(url + swaggerGeneratedPath);
		baseUrl = "http://localhost:" + serverPort;
		pathBenefit = "/benefits";
		
		okUserToken = getAccessToken(companyUserUsername, domain);
		notAllowedToken = getAccessToken(companyNotAllowedUserUsername, domain);
		
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	@Ignore("ServiceFeesList Construcor changed")
	public void testBenefitListControllerOk() throws Exception {
       /* List<ServiceFeesList> benefitList = new ArrayList<>();
        benefitList.add(new ServiceFeesList(10, (long)123, (long)123, "code", new java.sql.Timestamp((long)123), 1 , "side", "actingDeptDescription", "note", "description","descLanguage"));
        when(serviceFeesListRepository.findBenefits((long)anyInt(), anyString(), anyString(), anyString(), anyInt(), anyInt())).thenReturn(benefitList);

		CloseableHttpResponse response = sendRequest(url + pathBenefit +"?visitnb=123&lang=fr", okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathBenefit +"visitnb=123&lang=fr", HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathBenefit +"visitnb=123&lang=fr", HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));*/
	}
	
	@Test
	public void testBenefitDetailControllerOk() throws Exception {
        ServiceFeesDetail benefitDetail = new ServiceFeesDetail((long)123, (long)123, "code", new java.sql.Timestamp((long)123), 1 , "side", "actingDeptDescription", "note", "description", "descLanguage", anyInt(), "", "", anyInt(), "", "", anyInt(), "", "");
        when(serviceFeesDetailRepository.findBenefit((long)anyInt(), anyString())).thenReturn(benefitDetail);

		CloseableHttpResponse response = sendRequest(url + pathBenefit +"/123?lang=fr", okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathBenefit +"/123?lang=fr", HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathBenefit +"/123?lang=fr", HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}
	
	@Test
	public void testBenefitListControllerUnAuthorized() throws Exception {
		CloseableHttpResponse response = sendRequest(url + pathBenefit +"?visitnb=123&lang=fr", "");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
		
		// INVALID ACCES_TOKEN
		response = sendRequest(url + pathBenefit +"?visitnb=123&lang=fr", "FAKE_ACCESS_TOKEN");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testBenefitDetailControllerUnAuthorized() throws Exception {
		CloseableHttpResponse response = sendRequest(url + pathBenefit +"/123?lang=fr", "");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
		
		// INVALID ACCES_TOKEN
		response = sendRequest(url + pathBenefit +"/123?lang=fr", "FAKE_ACCESS_TOKEN");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testBenefitListControllerBadRequest() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathBenefit +"?visitnb=123&lang=fr", okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testBenefitDetailControllerBadRequest() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathBenefit +"/123?lang=fr", okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testbenefitListControllerForbidden() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathBenefit +"?visitnb=123&lang=fr" , notAllowedToken);
		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testbenefitDetailControllerForbidden() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathBenefit +"/123?lang=fr" , notAllowedToken);
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
