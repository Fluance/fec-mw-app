package net.fluance.cockpit.app.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collections;
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

import io.swagger.models.Swagger;
import net.fluance.app.security.service.xacml.BalanaXacmlPDP;
import net.fluance.app.security.service.xacml.XacmlPDP;
import net.fluance.app.test.AbstractWebIntegrationTest;
import net.fluance.app.test.mock.AuthorizationServerMock;
import net.fluance.app.web.util.swagger.SwaggerSpecUtils;
import net.fluance.cockpit.Application;
import net.fluance.cockpit.app.service.domain.AppointmentService;
import net.fluance.cockpit.core.model.PatientReference;
import net.fluance.cockpit.core.model.VisitReference;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentDetail;
import net.fluance.cockpit.core.repository.jdbc.appointment.AppointmentDoctorRepository;
import net.fluance.cockpit.core.repository.jdbc.appointment.AppointmentPatientRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations={ "classpath:test.properties", "classpath:webapps/conf/mw-app/application.properties"})
public class AppointmentDetailControllerTest extends AbstractWebIntegrationTest{
	
	private static Logger LOGGER = LogManager.getLogger(AppointmentControllerTest.class);
	private static final String DOMAIN = "PRIMARY";

	private String url;
	private String pathSwaggerDetailedAppointmentListDoctor;
	private String pathSwaggerDetailedAppointmentListUnitServicePid;
	private String pathSwaggerAppointmentDetail;

	private Swagger swaggerYamlController;
	@Value("${test.swagger.generated}")
	private String swaggerGeneratedPath;

	// Props
	@Value("${swagger.specs.location}")
	private String specsLocation;
	@Value("${test.host.local}")
	private String host;



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
	AppointmentService appointmentService;

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
		pathSwaggerDetailedAppointmentListDoctor = "/detailedappointments/mypatients";
		pathSwaggerDetailedAppointmentListUnitServicePid =  "/detailedappointments";
		pathSwaggerAppointmentDetail = "/detailedappointments/123";
		allowedUserAccessToken = getAccessToken(allowedUsername, DOMAIN);
		unallowedUserAccessToken = getAccessToken(notAllowedUsername, DOMAIN);

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testGetDetailedAppointmentListUnitServicePid() throws Exception{
		PatientReference patient = new PatientReference((long)100020, "firstName", "lastName", "maidenName", new java.util.Date());
		VisitReference visit = new VisitReference((long)111111, "patientUnit", "hospService", "patientRoom");
		List<AppointmentDetail> appointments = Collections.singletonList(new AppointmentDetail(anyInt(), (long) 123, new Timestamp(123123), new Timestamp(123123), "appointmentType", anyInt(), "description", anyString(), "description", anyInt(), anyString(), anyString(), patient, visit, null, null, null));
		when(appointmentService.getAppointmentDetailsByCriteriaShortedAndPaginated(anyInt(),(long)1234, anyLong(), anyListOf(String.class), anyListOf(String.class), anyString(), anyListOf(String.class), anyListOf(String.class), anyBoolean(), LocalDate.now(), LocalDate.now(), anyString(), anyString(), anyInt(), anyInt(), anyString())).thenReturn(appointments);

		CloseableHttpResponse response = sendRequest(url + pathSwaggerDetailedAppointmentListUnitServicePid + "?companyid=12&patientunit=44&hospservice=DIET&orderby=begindt&pid=123" , allowedUserAccessToken);
		LOGGER.info(url + pathSwaggerDetailedAppointmentListUnitServicePid + "?companyid=12&patientunit=44&hospservice=DIET&orderby=begindt&pid=123" );
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSwaggerDetailedAppointmentListUnitServicePid, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSwaggerDetailedAppointmentListUnitServicePid, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}

	@Test
	public void testGetDetailedDoctorAppointmentList() throws Exception{
		PatientReference patient = new PatientReference((long)100020, "firstName", "lastName", "maidenName", new java.util.Date());
		VisitReference visit = new VisitReference((long)111111, "patientUnit", "hospService", "patientRoom");
		List<AppointmentDetail> appointments = Collections.singletonList(new AppointmentDetail(anyInt(), (long) 123, new Timestamp(123123), new Timestamp(123123), "appointmentType", anyInt(), "description", anyString(), anyString(), anyInt(), anyString(), anyString(), patient, visit, null, null, null));
		when(appointmentService.getAppointmentsPhysician(anyInt(), new net.fluance.app.data.model.identity.UserProfile(), "date", "orderby", "sortorder", 10	, 0, anyString())).thenReturn(appointments);

		CloseableHttpResponse response = sendRequest(url + pathSwaggerDetailedAppointmentListDoctor + "?companyid=12&orderby=begindt" , allowedUserAccessToken);
		LOGGER.info(url + pathSwaggerDetailedAppointmentListDoctor + "?companyid=12&orderby=begindt" );
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSwaggerDetailedAppointmentListDoctor, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSwaggerDetailedAppointmentListDoctor, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}
	
	@Test
	public void testGetAppointmentdetailById() throws Exception{
		PatientReference patient = new PatientReference((long)100020, "firstName", "lastName", "maidenName", new java.util.Date());
		VisitReference visit = new VisitReference((long)111111, "patientUnit", "hospService", "patientRoom");
		AppointmentDetail appointment = new AppointmentDetail(anyInt(), (long)123, new java.sql.Timestamp(123123), new java.sql.Timestamp(123123), "appointmentType", anyInt(), "description", anyString(), anyString(), anyInt(), anyString(), anyString(), patient, visit, null, null, null);
		when(appointmentService.getAppointmentDetail((long)anyInt(), anyString())).thenReturn(appointment);

		CloseableHttpResponse response = sendRequest(url + pathSwaggerAppointmentDetail, allowedUserAccessToken);
		LOGGER.info(url + pathSwaggerAppointmentDetail);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSwaggerAppointmentDetail, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSwaggerAppointmentDetail, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}

	@Test
	public void testDetailedAppointmentListUnitServicePidBadRequest() throws Exception {
		CloseableHttpResponse response = sendRequest(url + pathSwaggerDetailedAppointmentListUnitServicePid + "?wrongParam=" + 123, allowedUserAccessToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testDetailedAppointmentListDoctorBadRequest() throws Exception {
		CloseableHttpResponse response = sendRequest(url + pathSwaggerDetailedAppointmentListDoctor + "?wrongParam=" + 123, allowedUserAccessToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusLine().getStatusCode());
	}

	@Test
	public void testDetailedAppointmentListUnitServicePidUnAuthorized() throws Exception {
		CloseableHttpResponse response = sendRequest(url + pathSwaggerDetailedAppointmentListUnitServicePid + "?companyid=12&patientunit=44&hospservice=DIET&orderby=begindt", "");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());

		// INVALID ACCES_TOKEN
		response = sendRequest(url + pathSwaggerDetailedAppointmentListUnitServicePid + "?companyid=12&patientunit=44&hospservice=DIET&orderby=begindt", "FAKE_ACCESS_TOKEN");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testDetailedAppointmentListDoctorUnAuthorized() throws Exception {
		CloseableHttpResponse response = sendRequest(url + pathSwaggerDetailedAppointmentListDoctor + "?companyid=12&orderby=begindt", "");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());

		// INVALID ACCES_TOKEN
		response = sendRequest(url + pathSwaggerDetailedAppointmentListDoctor + "?companyid=12&orderby=begindt", "FAKE_ACCESS_TOKEN");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
	}

	@Test
	public void testDetailedAppointmentListUnitServicePidForbidden() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathSwaggerDetailedAppointmentListUnitServicePid + "?companyid=12&patientunit=44&hospservice=DIET&orderby=begindt", unallowedUserAccessToken);
		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testDetailedAppointmentListDoctorForbidden() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathSwaggerDetailedAppointmentListDoctor + "?companyid=12&orderby=begindt", unallowedUserAccessToken);
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
	public void checkOk(Object... params) throws UnsupportedOperationException {}
}
