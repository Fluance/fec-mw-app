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
import net.fluance.app.test.AbstractWebIntegrationTest;
import net.fluance.app.web.util.swagger.SwaggerSpecUtils;
import net.fluance.cockpit.Application;
import net.fluance.cockpit.core.model.PatientReference;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentDoctorList;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentNew;
import net.fluance.cockpit.core.model.jdbc.company.BedList;
import net.fluance.cockpit.core.model.jdbc.company.CompaniesList;
import net.fluance.cockpit.core.model.jdbc.company.CompanyDetails;
import net.fluance.cockpit.core.model.jdbc.company.RoomList;
import net.fluance.cockpit.core.model.jdbc.company.RoomOnlyList;
import net.fluance.cockpit.core.model.jdbc.company.ServiceList;
import net.fluance.cockpit.core.model.jdbc.company.Unit;
import net.fluance.cockpit.core.repository.jdbc.appointment.AppointmentDoctorRepository;
import net.fluance.cockpit.core.repository.jdbc.appointment.AppointmentPatientRepository;
import net.fluance.cockpit.core.repository.jdbc.company.BedListRepository;
import net.fluance.cockpit.core.repository.jdbc.company.CompaniesListRepository;
import net.fluance.cockpit.core.repository.jdbc.company.CompanyDetailsRepository;
import net.fluance.cockpit.core.repository.jdbc.company.RoomListRepository;
import net.fluance.cockpit.core.repository.jdbc.company.RoomOnlyListRepository;
import net.fluance.cockpit.core.repository.jdbc.company.ServiceListRepository;
import net.fluance.cockpit.core.repository.jdbc.company.UnitListRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class CompanyControllerTest extends AbstractWebIntegrationTest {

	private static Logger LOGGER = LogManager.getLogger(CompanyControllerTest.class);
	
	private Swagger swaggerYamlController;
	private String url;
	private String pathCompanyList;
	private String pathCompanyDetails;
	private String pathCompanyUnitList;
	private String pathCompanyService;
	private String pathCompanyRoomList;
	private String pathCompanyBedList;
	private String pathSpecsCompanyBedList;
	private String pathSpecsCompanyRoomList;
	private String pathSpecsCompanyUnitList;
	private String pathSpecsCompanyServiceList;
	
	private String pathAppointmentList;
	private String pathDoctorAppointmentList;
	private String pathSwaggerAppointmentListDoctor;
	private String pathSwaggerAppointmentListNurse;
	private String pathSpecsCompanyDetails;
	
	// Props
	@Value("${swagger.specs.location}")
	private String specsLocation;
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
	@Value("${test.swagger.generated}")
	private String swaggerGeneratedPath;

	private String okUserToken;
	private String notAllowedToken;
	
	@Autowired
    protected WebApplicationContext wac;
	
    @Configuration
    public static class TestConfig {
    	@Bean
    	@Primary
        public CompaniesListRepository companiesListRepository() {
            return Mockito.mock(CompaniesListRepository.class);
        }

    	@Bean
    	@Primary
        public CompanyDetailsRepository companyDetailsRepository() {
            return Mockito.mock(CompanyDetailsRepository.class);
        }

    	@Bean
    	@Primary
        public UnitListRepository unitListRepository() {
            return Mockito.mock(UnitListRepository.class);
        }

    	@Bean
    	@Primary
        public RoomOnlyListRepository roomOnlyListRepository() {
            return Mockito.mock(RoomOnlyListRepository.class);
        }

    	@Bean
    	@Primary
        public RoomListRepository roomListRepository() {
            return Mockito.mock(RoomListRepository.class);
        }

    	@Bean
    	@Primary
        public ServiceListRepository serviceListRepository() {
            return Mockito.mock(ServiceListRepository.class);
        }
    	
    	@Bean
    	@Primary
        public BedListRepository bedListRepository() {
            return Mockito.mock(BedListRepository.class);
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

    }
    
	@Autowired
	AppointmentPatientRepository appointmentNurseListRepository;

	@Autowired
	AppointmentDoctorRepository appointmentDoctorRepository;
    
    @Autowired
    CompaniesListRepository companiesListRepository;

    @Autowired
    CompanyDetailsRepository companyDetailsRepository;

    @Autowired
    UnitListRepository unitListRepository;

    @Autowired
    RoomOnlyListRepository roomOnlyListRepository;
    
    @Autowired
    RoomListRepository roomListRepository;
    
    @Autowired
    ServiceListRepository serviceListRepository;

    @Autowired
    BedListRepository bedListRepository;
    
	@Value("${net.fluance.cockpit.app.web.company.userUsername}")
	private String allowedUsername;
	@Value("${net.fluance.cockpit.app.web.company.userPassword}")
	private String allowedPassword;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserUsername}")
	private String notAllowedUsername;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserPassword}")
	private String notAllowedPassword;
    
	@Before
	public void setUp() throws KeyManagementException, UnsupportedOperationException, NoSuchAlgorithmException, KeyStoreException, IOException, HttpException, URISyntaxException {
		url = host + serverPort;
		swaggerYamlController = SwaggerSpecUtils.load(url + swaggerGeneratedPath);
		baseUrl = "http://localhost:" + serverPort;
		authBaseUrl = "http://localhost:" + serverPort;
		pathCompanyList = "/companies";
		pathCompanyDetails = "/companies/1";
		pathCompanyUnitList = "/companies/1/units";
		pathCompanyService = "/companies/1/services";
		pathCompanyRoomList = "/companies/1/rooms";
		pathCompanyBedList = "/companies/1/beds";
		
		pathSpecsCompanyDetails = "/companies/{companyid}";
		pathSpecsCompanyUnitList = "/companies/{companyid}/units";
		pathSpecsCompanyServiceList = "/companies/{companyid}/services";
		pathSpecsCompanyRoomList = "/companies/{companyid}/rooms";
		pathSpecsCompanyBedList = "/companies/{companyid}/beds";
		pathAppointmentList = "/companies/23/appointments";
		pathDoctorAppointmentList = "/companies/23/doctorappointments";
		pathSwaggerAppointmentListDoctor = "/companies/{companyid}/doctorappointments";
		pathSwaggerAppointmentListNurse =  "/companies/{companyid}/appointments";
		
		okUserToken = getAccessToken(companyUserUsername, domain);
		notAllowedToken = getAccessToken(companyNotAllowedUserUsername, domain);
		
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

	}

	@Test
	public void testCompanyListControllerOk() throws Exception {
        List<CompaniesList> companiesList = new ArrayList<>();
        List<Unit> units = Arrays.asList(new Unit("patientunit", "codedesc"));
        List<ServiceList> services = Arrays.asList(new ServiceList("hospservice", "hospservicedesc"));
        companiesList.add(new CompaniesList(1, "CODE", "companyMock", units, services));
        when(companiesListRepository.findAll()).thenReturn(companiesList);

		CloseableHttpResponse response = sendRequest(url + pathCompanyList, okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathCompanyList, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathCompanyList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}
	
	@Test
	public void testCompanyListControllerUnAuthorized() throws Exception {
		CloseableHttpResponse response = sendRequest(url + pathCompanyList, "");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
		
		// INVALID ACCES_TOKEN
		response = sendRequest(url + pathCompanyList, "FAKE_ACCESS_TOKEN");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testCompanyListControllerForbidden() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathCompanyList, notAllowedToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testCompanyDetailsControllerOk() throws Exception {
		when(companyDetailsRepository.findOne(anyInt())).thenReturn(new CompanyDetails(1, "123", "name", "address", "canton", "country", "email", "fax", "locality", "phone", 1234, "preflang"));
		
		CloseableHttpResponse response = sendRequest(url + pathCompanyDetails + 1, okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsCompanyDetails, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsCompanyDetails, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}

	@Test
	public void testCompanyUnitListControllerOk() throws Exception {
		List<Unit> unitList = new ArrayList<>();
		unitList.add(new Unit("patientunit", "codedesc"));
		when(unitListRepository.findByCompanyId(anyInt())).thenReturn(unitList);
		
		CloseableHttpResponse response = sendRequest(url + pathCompanyUnitList , okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsCompanyUnitList, HttpStatus.OK));
		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsCompanyUnitList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}

	@Test
	public void testCompanyServiceControllerOk() throws Exception {
		List<ServiceList> serviceList = new ArrayList<>();
		serviceList.add(new ServiceList("hospservice", "hospservicedesc"));
		when(serviceListRepository.findByCompanyId(anyInt())).thenReturn(serviceList);
		
		CloseableHttpResponse response = sendRequest(url + pathCompanyService + "?companyid=" + 1 + "/services", okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsCompanyServiceList, HttpStatus.OK));
		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsCompanyServiceList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));

		// TEST WITH PATIENTUNIT
		response = sendRequest(url + pathCompanyService + "?companyid=" + 1 + "/services" +  "&patientunit=1", okUserToken);
		jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsCompanyServiceList, HttpStatus.OK));
		textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsCompanyServiceList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}
	
	@Test
	public void testCompanyRoomListWithoutCountControllerOk() throws Exception {
		// Init Repository mocks
		List<RoomOnlyList> roomOnlyList = new ArrayList<>();
		roomOnlyList.add(new RoomOnlyList("room"));
		when(roomOnlyListRepository.findByCompanyIdAndPatientunit(anyInt(), anyString(), null)).thenReturn(roomOnlyList);
		when(roomOnlyListRepository.findByCompanyIdAndHospservice(anyInt(), anyString(), null)).thenReturn(roomOnlyList);
		when(roomOnlyListRepository.findByCompanyIdAndPatientunitAndHospservice(anyInt(), anyString(), anyString(), null)).thenReturn(roomOnlyList);
		
		CloseableHttpResponse response = sendRequest(url + pathCompanyRoomList + "?companyid=" + 14 + "&patientunit=01", okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsCompanyRoomList, HttpStatus.OK));
		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsCompanyRoomList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));

		// TEST WITH COMPANY_ID AND HOSPSERVICE
		response = sendRequest(url + pathCompanyRoomList + "?companyid=" + 1 + "&hospservice=1ANG", okUserToken);
		jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsCompanyRoomList, HttpStatus.OK));
		textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsCompanyRoomList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));

		// TEST WITH ALL PARAMS
		response = sendRequest(url + pathCompanyRoomList + "?companyid=" + 1 + "&patientunit=01" + "&hospservice=1ANG", okUserToken);
		jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsCompanyRoomList, HttpStatus.OK));
		textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsCompanyRoomList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}
	
	@Test
	public void testCompanyRoomListWithCountControllerOk() throws Exception {
		// Init Repository mocks
		List<RoomList> roomList = new ArrayList<>();
		roomList.add(new RoomList("room"));
		when(roomListRepository.findByCompanyIdAndPatientunit(anyInt(), anyString(), null)).thenReturn(roomList);
		when(roomListRepository.findByCompanyIdAndHospservice(anyInt(), anyString(), null)).thenReturn(roomList);
		when(roomListRepository.findByCompanyIdAndPatientunitAndHospservice(anyInt(), anyString(), anyString(), null)).thenReturn(roomList);

		// TEST WITH COMPANY_ID AND PATIENTUNIT 
		CloseableHttpResponse response = sendRequest(url + swaggerYamlController.getBasePath() + pathCompanyRoomList + "?companyid=" + 1 + "&patientunit=OC" + "&occupancy=true", okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsCompanyRoomList, HttpStatus.OK));
		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsCompanyRoomList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));

		// TEST WITH COMPANY_ID AND HOSPSERVICE
		response = sendRequest(url + swaggerYamlController.getBasePath() + pathCompanyRoomList + "?companyid=" + 1 + "&hospservice=2ONC" + "&occupancy=true", okUserToken);
		jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsCompanyRoomList, HttpStatus.OK));
		textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsCompanyRoomList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));

		// TEST WITH ALL PARAMS
		response = sendRequest(url + swaggerYamlController.getBasePath() + pathCompanyRoomList + "?companyid=" + 1 + "&patientunit=OC" + "&hospservice=2ONC" + "&occupancy=true", okUserToken);
		jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsCompanyRoomList, HttpStatus.OK));
		textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsCompanyRoomList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}
	
	@Test
	public void testCompanyRoomListControllerBadRequest() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathCompanyRoomList+ "?wrongparma=", okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testCompanyBedListControllerOk() throws Exception {
		List<BedList> bedList = new ArrayList<>();
		bedList.add(new BedList(123));
		when(bedListRepository.findCompanyIdAndPatientUnitAndPatientroom(anyInt(), anyString(), anyString())).thenReturn(bedList);
		when(bedListRepository.findCompanyIdAndPatientunitAndHospserviceAndPatientroom(anyInt(), anyString(), anyString(), anyString())).thenReturn(bedList);
		when(bedListRepository.findCompanyIdAndHospserviceAndpatientroom(anyInt(), anyString(), anyString())).thenReturn(bedList);
		
		// TEST WITH COMPANY_ID AND PATIENTROOM AND PATIENTUNIT 
		CloseableHttpResponse response = sendRequest(url + pathCompanyBedList + "?patientroom=" + 0 + "&patientunit=01", okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsCompanyBedList, HttpStatus.OK));
		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsCompanyBedList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));

		// TEST WITH COMPANY_ID  AND PATIENTROOM AND HOSPSERVICE
		response = sendRequest(url + swaggerYamlController.getBasePath() + pathCompanyBedList  + "?patientroom=" + 0 + "&hospservice=1ANG", okUserToken);
		jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsCompanyBedList, HttpStatus.OK));
		textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsCompanyBedList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));

		// TEST WITH ALL PARAMS
		response = sendRequest(url + swaggerYamlController.getBasePath() + pathCompanyBedList + "?patientroom=" + 0 + "&patientunit=01" + "&hospservice=1ANG", okUserToken);
		jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsCompanyBedList, HttpStatus.OK));
		textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsCompanyBedList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}
	
	@Test
	public void testCompanyBedListControllerBadRequest() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathCompanyBedList + "?patientroom=" + 1, okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testGetNurseAppointmentList() throws Exception{
		List<AppointmentNew> appointments = Arrays.asList(new AppointmentNew(0, (long)0, (long)0, "patientroom", (long)0, "firstname", "lastname", "maidenname", new java.util.Date(), "description"));
		when(appointmentNurseListRepository.findByCompanyIdAndPatientUnitAndHospService(anyInt(), anyList(), anyList(), anyString(), anyInt(), anyInt())).thenReturn(appointments);
		
		CloseableHttpResponse response = sendRequest(url + pathAppointmentList + "?patientunit=44&hospservice=DIET&orderby=begindt" , okUserToken);
		LOGGER.info(url + pathAppointmentList + "?companyid=12&patientunit=44&hospservice=DIET&orderby=begindt" );
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSwaggerAppointmentListNurse, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSwaggerAppointmentListNurse, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}

	@Test
	public void testGetDoctorAppointmentList() throws Exception{
		List<AppointmentDoctorList> appointments = Arrays.asList(new AppointmentDoctorList(1, (long)1, "description", new Date(), new Date(), new PatientReference((long)1, "firstName", "lastName", "maidenName", new Date()), (long)23) );
		when(appointmentDoctorRepository.findByStaffidAndCompanyid(anyString(), anyInt(), anyString(), anyInt(), anyInt())).thenReturn(appointments);
		
		CloseableHttpResponse response = sendRequest(url + pathDoctorAppointmentList + "?staffid=1064" , okUserToken);
		LOGGER.info(url + pathDoctorAppointmentList + "?staffid=1064" );
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSwaggerAppointmentListDoctor, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSwaggerAppointmentListDoctor, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}

	@Test
	public void testGetDoctorAppointmentListByDate() throws Exception{
		List<AppointmentDoctorList> appointments = Arrays.asList(new AppointmentDoctorList(1, (long)1, "description", new Date(), new Date(), new PatientReference((long)1, "firstName", "lastName", "maidenName", new Date()), (long)23) );

		when(appointmentDoctorRepository.findByStaffidAndCompanyidAndBegindt(anyString(), anyInt(), eq("2016-12-16"), anyString(), anyInt(), anyInt())).thenReturn(appointments);
		
		CloseableHttpResponse response = sendRequest(url + pathDoctorAppointmentList + "?staffid=1064&date=2016-12-16" , okUserToken);
		LOGGER.info(url + pathDoctorAppointmentList + "?staffid=1064&date=2016-12-16" );
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSwaggerAppointmentListDoctor, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSwaggerAppointmentListDoctor, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));

		response = sendRequest(url + pathDoctorAppointmentList + "?companyid=3&staffid=1064&date=anydate" , okUserToken);
		LOGGER.info(url + pathDoctorAppointmentList + "?companyid=3&staffid=1064&date=anydate" );
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
		CloseableHttpResponse response = sendRequest(url + pathAppointmentList + "?wrongParam=" + 123, okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testAppointmentListControllerUnAuthorized() throws Exception {
		CloseableHttpResponse response = sendRequest(url + pathAppointmentList + "?companyid=12&patientunit=44&hospservice=DIET&orderby=begindt", "");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
		
		// INVALID ACCES_TOKEN
		response = sendRequest(url + pathAppointmentList + "?companyid=12&patientunit=44&hospservice=DIET&orderby=begindt", "FAKE_ACCESS_TOKEN");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
	}
	
	@Test
	public void testAppointmentListControllerForbidden() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathAppointmentList + "?patientunit=44&hospservice=DIET&orderby=begindt", notAllowedToken);
		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusLine().getStatusCode());
	}

	@Override
	public void tearDown() {
		swaggerYamlController = null;
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
