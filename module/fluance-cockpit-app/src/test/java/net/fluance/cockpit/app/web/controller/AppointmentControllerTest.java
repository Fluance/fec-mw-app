package net.fluance.cockpit.app.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import io.swagger.models.Swagger;
import net.fluance.app.security.service.xacml.BalanaXacmlPDP;
import net.fluance.app.security.service.xacml.XacmlPDP;
import net.fluance.app.test.AbstractWebIntegrationTest;
import net.fluance.app.test.mock.AuthorizationServerMock;
import net.fluance.app.web.util.swagger.SwaggerSpecUtils;
import net.fluance.cockpit.Application;
import net.fluance.cockpit.core.model.PatientReference;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentDoctorList;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentNew;
import net.fluance.cockpit.core.repository.jdbc.appointment.AppointmentDoctorRepository;
import net.fluance.cockpit.core.repository.jdbc.appointment.AppointmentPatientRepository;

/**
 * 
 * @author Malic
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations={ "classpath:test.properties", "classpath:webapps/conf/mw-app/application.properties"})
public class AppointmentControllerTest extends AbstractWebIntegrationTest {

	private static Logger LOGGER = LogManager.getLogger(AppointmentControllerTest.class);
	
	private String url;
	private String pathSwaggerAppointmentListDoctor;
	private String pathSwaggerAppointmentListNurse;
	
	private Swagger swaggerYamlController;
	@Value("${test.swagger.generated}")
	private String swaggerGeneratedPath;

	// Props
	@Value("${swagger.specs.location}")
	private String specsLocation;
	@Value("${test.host.local}")
	private String host;
//	@Value("${is.sp.auth}")
//	private String isServiceProviderAuth;
	private String domain = "PRIMARY";
//	@Value("${oauth2.token.url}")
//	private String oAuth2TokenUrl;

	private String allowedUserAccessToken;
	private String unallowedUserAccessToken;

    @Autowired
    XacmlPDP pdp;
    
	@Autowired
    protected WebApplicationContext wac;
	
    @Configuration
    public static class TestConfig {
    	
    	@Bean
    	@Primary
        public XacmlPDP xacmlPDP() {
            return Mockito.mock(XacmlPDP.class);
        }
    	
    	@Bean
    	@Primary
        public AppointmentPatientRepository appointmentNurseRepository() {
            return Mockito.mock(AppointmentPatientRepository.class);
        }
    	
    	@Bean
    	@Primary
        public AppointmentDoctorRepository appointmentDoctorRepository() {
            return Mockito.mock(AppointmentDoctorRepository.class);
        }
    	
    	@Bean
    	public XacmlPDP xacmlPdp() {
    		return new BalanaXacmlPDP();
    	}

    	@Bean
    	public AuthorizationServerMock authorizationServerMock() {
    		return new AuthorizationServerMock();
    	}

    }
	
	@Autowired
	AppointmentPatientRepository appointmentNurseListRepository;

	@Autowired
	AppointmentDoctorRepository appointmentDoctorRepository;
	
	@Value("${authorized.username}")
	private String allowedUsername;
	@Value("${authorized.password}")
	private String allowedPassword;
	@Value("${unauthorized.username}")
	private String notAllowedUsername;
	@Value("${unauthorized.password}")
	private String notAllowedPassword;

	@Before
	public void setUp() throws KeyManagementException, UnsupportedOperationException, NoSuchAlgorithmException, KeyStoreException, IOException, HttpException, URISyntaxException {
		url = host + serverPort;
		swaggerYamlController = SwaggerSpecUtils.load(url + swaggerGeneratedPath);
		authBaseUrl = "http://localhost:" + serverPort;
		pathSwaggerAppointmentListDoctor = "/appointments";
		pathSwaggerAppointmentListNurse =  "/appointments";
		allowedUserAccessToken = getAccessToken(allowedUsername, domain);
		unallowedUserAccessToken = getAccessToken(notAllowedUsername, domain);
		
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

	}
	
	@Test
	public void testGetNurseAppointmentList() throws Exception{
		List<AppointmentNew> appointments = Arrays.asList(new AppointmentNew(0, (long)0, (long)0, "patientroom", (long)0, "firstname", "lastname", "maidenname", new java.util.Date(), "description"));
		when(appointmentNurseListRepository.findByCompanyIdAndPatientUnitAndHospService(anyInt(), anyList(), anyList(), anyString(), anyInt(), anyInt())).thenReturn(appointments);
		
		CloseableHttpResponse response = sendRequest(url + pathSwaggerAppointmentListNurse + "?companyid=12&patientunit=44&hospservice=DIET&orderby=begindt" , allowedUserAccessToken);
		LOGGER.info(url + pathSwaggerAppointmentListNurse + "?companyid=12&patientunit=44&hospservice=DIET&orderby=begindt" );
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSwaggerAppointmentListNurse, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSwaggerAppointmentListNurse, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}

	//TODO Fix feature and test
	@Test
	public void testGetDoctorAppointmentList() throws Exception{
		List<AppointmentDoctorList> appointments = Arrays.asList(new AppointmentDoctorList(1, (long)1, "description", new Date(), new Date(), new PatientReference((long)1, "firstName", "lastName", "maidenName", new Date()), (long)23) );
		when(appointmentDoctorRepository.findByStaffidAndCompanyid(anyString(), anyInt(), anyString(), anyInt(), anyInt())).thenReturn(appointments);
		
		CloseableHttpResponse response = sendRequest(url + pathSwaggerAppointmentListDoctor + "?companyid=3&byphysician=true" , allowedUserAccessToken);
		LOGGER.info(url + pathSwaggerAppointmentListDoctor + "?companyid=3&byphysician=true" );
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSwaggerAppointmentListDoctor, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSwaggerAppointmentListDoctor, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}

	//TODO Fix feature and test
	@Test
	public void testGetDoctorAppointmentListByDate() throws Exception{
		List<AppointmentDoctorList> appointments = Arrays.asList(new AppointmentDoctorList(1, (long)1, "description", new Date(), new Date(), new PatientReference((long)1, "firstName", "lastName", "maidenName", new Date()), (long)23) );
		when(appointmentDoctorRepository.findByStaffidAndCompanyidAndBegindt(anyString(), anyInt(), eq("2016-12-16"), anyString(), anyInt(), anyInt())).thenReturn(appointments);
		
		CloseableHttpResponse response = sendRequest(url + pathSwaggerAppointmentListDoctor + "?companyid=3&date=2016-12-16&byphysician=true" , allowedUserAccessToken);
		LOGGER.info(url + pathSwaggerAppointmentListDoctor + "?companyid=3&date=2016-12-16&byphysician=true" );
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSwaggerAppointmentListDoctor, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSwaggerAppointmentListDoctor, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));

		response = sendRequest(url + pathSwaggerAppointmentListDoctor + "?companyid=3&staffid=1064&date=anydate" , allowedUserAccessToken);
		LOGGER.info(url + pathSwaggerAppointmentListDoctor + "?companyid=3&staffid=1064&date=anydate" );
		jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSwaggerAppointmentListDoctor, HttpStatus.OK));

		textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSwaggerAppointmentListDoctor, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
		assertEquals("[]", jsonResponse);
	}
	
	@Test
	public void testGetDoctorAppointmentListByMultipleStaffId() throws Exception{
		List<AppointmentDoctorList> appointments = Arrays.asList(new AppointmentDoctorList(1, (long)1, "description", new Date(), new Date(), new PatientReference((long)1, "firstName", "lastName", "maidenName", new Date()), (long)23) );
		List<String> staffids = new ArrayList<>();
		staffids.add("1064");
		staffids.add("26");
		List<Integer> companyids = new ArrayList<>();
		companyids.add(1);
		companyids.add(9);
		when(appointmentDoctorRepository.findByMultipleStaffids(staffids, companyids, eq("2016-12-16"), anyString(), anyInt(), anyInt())).thenReturn(appointments);
		
		CloseableHttpResponse response = sendRequest(url + pathSwaggerAppointmentListDoctor + "?date=2016-12-16" , allowedUserAccessToken);
		LOGGER.info(url + pathSwaggerAppointmentListDoctor + "?date=2016-12-16" );
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSwaggerAppointmentListDoctor, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSwaggerAppointmentListDoctor, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));

		response = sendRequest(url + pathSwaggerAppointmentListDoctor + "?date=2016-12-16" , allowedUserAccessToken);
		LOGGER.info(url + pathSwaggerAppointmentListDoctor + "?date=2016-12-16" );
		jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSwaggerAppointmentListDoctor, HttpStatus.OK));

		textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSwaggerAppointmentListDoctor, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
		assertEquals("[]", jsonResponse);
	}
	
	@Test
	public void testAppointmentListControllerBadRequest() throws Exception {
		CloseableHttpResponse response = sendRequest(url + pathSwaggerAppointmentListNurse + "?wrongParam=" + 123, allowedUserAccessToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testAppointmentListControllerUnAuthorized() throws Exception {
		CloseableHttpResponse response = sendRequest(url + pathSwaggerAppointmentListNurse + "?companyid=12&patientunit=44&hospservice=DIET&orderby=begindt", "");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
		
		// INVALID ACCES_TOKEN
		response = sendRequest(url + pathSwaggerAppointmentListNurse + "?companyid=12&patientunit=44&hospservice=DIET&orderby=begindt", "FAKE_ACCESS_TOKEN");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testAppointmentListControllerForbidden() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathSwaggerAppointmentListNurse + "?companyid=12&patientunit=44&hospservice=DIET&orderby=begindt", unallowedUserAccessToken);
		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusLine().getStatusCode());
	}

	@Override
	public void tearDown() {
		swaggerYamlController = null;
	}

	protected boolean checkOAuth2Authorization() {
		return false;
	}

	public void checkOk() {
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}


	@Override
	protected boolean checkOAuth2Authorization(Object... params) {
		return false;
	}

	@Override
	public void checkOk(Object... params)
			throws KeyManagementException, UnsupportedOperationException, NoSuchAlgorithmException, KeyStoreException,
			IOException, HttpException, URISyntaxException, ProcessingException {
	}
}
