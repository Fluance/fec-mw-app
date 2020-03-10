package net.fluance.cockpit.app.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.ParseException;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import io.swagger.models.Swagger;
import net.fluance.app.test.AbstractWebIntegrationTest;
import net.fluance.app.web.util.swagger.SwaggerSpecUtils;
import net.fluance.cockpit.Application;
import net.fluance.cockpit.core.model.jdbc.patient.PatientContact;
import net.fluance.cockpit.core.model.jdbc.patient.PatientInList;
import net.fluance.cockpit.core.model.jdbc.patient.PatientNextOfKin;
import net.fluance.cockpit.core.model.jdbc.patient.PatientNextOfKinContact;
import net.fluance.cockpit.core.model.wrap.patient.AdmissionStatusEnum;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientContactRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientInListRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientNextOfKinContactRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientNextOfKinRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class PatientControllerTest extends AbstractWebIntegrationTest {

	private static Logger LOGGER = LogManager.getLogger(PatientControllerTest.class);

	private Swagger swaggerYamlVisit;
	private String url;
	
	//Path to controllers
	private String pathPatientDetails;
	private String pathPatientList;
	private String nokContactResourcePath;
	private String nokListResourcePath;
	private String pathRadiologyList;
	
	private String groupNameListResourcePath;
	private String dataListResourcePath;
	
	private String pathSpecsRadiologyList;
	private String pathSpecsPatientDetails;
	private String SpecsnokListResourcePath;
	private String SpecsnokContactResourcePath;
	private String dataListSpecsResourcePath;
	private String groupNameListSpecsResourcePath;

	// Props
	@Value("${swagger.specs.location}")
	private String specsLocation;
	@Value("${test.host.local}")
	private String host;
	@Value("${net.fluance.cockpit.app.web.company.userUsername}")
	private String okUserUsername;
	@Value("${net.fluance.cockpit.app.web.company.userPassword}")
	private String okUserPassword;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserUsername}")
	private String notAllowedUserUsername;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserPassword}")
	private String notAllowedUserPassword;
	
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokId}")
	private String expectedNokId;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokPatientId}")
	private String expectedNokPatientId;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedInHousePatientLastNameStartLetters}")
	private String expectedInHousePatientLastNameStartLetters;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPreAdmittedPatientLastNameStartLetters}")
	private String expectedPreAdmittedPatientLastNameStartLetters;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPreAdmittedCompanyId}")
	private String expectedPreAdmittedCompanyId;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastNameStartLetters}")
	private String expectedPatientLastNameStartLetters;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientFirstNameStartLetters}")
	private String expectedPatientFirstNameStartLetters;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientMaidenNameStartLetters}")
	private String expectedPatientMaidenNameStartLetters;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientId}")
	private String expectedPatientId;
	@Value("${net.fluance.cockpit.core.model.repository.patient.unexpectedPatientId}")
	private String unexpectedPatientId;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientFirstName}")
	private String expectedPatientFirstName;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastName}")
	private String expectedPatientLastName;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientMaidenName}")
	private String expectedPatientMaidenName;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientDateOfBirth}")
	private String expectedPatientDateOfBirth;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientNationality}")
	private String expectedPatientNationality;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLanguage}")
	private String expectedPatientLanguage;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientAvsNumber}")
	private String expectedPatientAvsNumber;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientSex}")
	private String expectedPatientSex;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientAddress}")
	private String expectedPatientAddress;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientAddress2}")
	private String expectedPatientAddress2;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientComplement}")
	private String expectedPatientComplement;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientCareOf}")
	private String expectedPatientCareOf;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLocality}")
	private String expectedPatientLocality;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientPostCode}")
	private String expectedPatientPostCode;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientSubPostCode}")
	private String expectedPatientSubPostCode;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientCanton}")
	private String expectedPatientCanton;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientCountry}")
	private String expectedPatientCountry;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitNumber}")
	private String expectedPatientLastVisitNumber;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitCompanyId}")
	private int expectedPatientLastVisitCompanyId;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitAdmitDate}")
	private String expectedPatientLastVisitAdmitDate;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitDischargeDate}")
	private String expectedPatientLastVisitDischargeDate;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientClass}")
	private String expectedPatientLastVisitPatientClass;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientClassDesc}")
	private String expectedPatientLastVisitPatientClassDesc;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientType}")
	private String expectedPatientLastVisitPatientType;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientTypeDesc}")
	private String expectedPatientLastVisitPatientTypeDesc;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientCase}")
	private String expectedPatientLastVisitPatientCase;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientCaseDesc}")
	private String expectedPatientLastVisitPatientCaseDesc;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitHospService}")
	private String expectedPatientLastVisitHospService;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitHospServiceDesc}")
	private String expectedPatientLastVisitHospServiceDesc;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitAdmissionType}")
	private String expectedPatientLastVisitAdmissionType;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitAdmissionTypeDesc}")
	private String expectedPatientLastVisitAdmissionTypeDesc;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientUnit}")
	private String expectedPatientLastVisitPatientUnit;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientUnitDesc}")
	private String expectedPatientLastVisitPatientUnitDesc;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientRoom}")
	private String expectedPatientLastVisitPatientRoom;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientBed}")
	private String expectedPatientLastVisitPatientBed;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitFinancialClass}")
	private String expectedPatientLastVisitFinancialClass;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitFinancialClassDesc}")
	private String expectedPatientLastVisitFinancialClassDesc;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientByLastNameCount}")
	private String expectedPatientByLastNameCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientByLastNameAndFirstNameCount}")
	private String expectedPatientByLastNameAndFirstNameCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientByBirthDateCount}")
	private String expectedPatientByBirthDateCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientByLastNameAndBirthDateCount}")
	private String expectedPatientByLastNameAndBirthDateCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientByLastNameAndFirstNameAndBirthDateCount}")
	private String expectedPatientByLastNameAndFirstNameAndBirthDateCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPreAdmittedPatientByLastNameCount}")
	private String expectedPreAdmittedPatientByLastNameCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPreAdmittedPatientByLastNameAndFirstNameCount}")
	private String expectedPreAdmittedPatientByLastNameAndFirstNameCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPreAdmittedPatientByBirthDateCount}")
	private String expectedPreAdmittedPatientByBirthDateCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPreAdmittedPatientByLastNameAndBirthDateCount}")
	private String expectedPreAdmittedPatientByLastNameAndBirthDateCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPreAdmittedPatientByLastNameAndFirstNameAndBirthDateCount}")
	private String expectedPreAdmittedPatientByLastNameAndFirstNameAndBirthDateCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedInHousePatientByLastNameCount}")
	private String expectedInHousePatientByLastNameCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedInHousePatientByLastNameAndFirstNameCount}")
	private String expectedInHousePatientByLastNameAndFirstNameCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedInHousePatientByBirthDateCount}")
	private String expectedInHousePatientByBirthDateCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedInHousePatientByLastNameAndBirthDateCount}")
	private String expectedInHousePatientByLastNameAndBirthDateCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedInHousePatientByLastNameAndFirstNameAndBirthDateCount}")
	private String expectedInHousePatientByLastNameAndFirstNameAndBirthDateCount;
			
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientByLastNameAndFirstNameAndMaidenNameAndBirthDateCount}")
	private String expectedPatientByLastNameAndFirstNameAndMaidenNameAndBirthDateCount;
	
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientByLastNameAndFirstNameAndMaidenNameCount}")
	private String expectedPatientByLastNameAndFirstNameAndMaidenNameCount;
	
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientVisitAttendingStaffId}")
	private String expectedPatientVisitAttendingStaffId;
	
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientsByAttendingStaffIdCount}")
	private String expectedPatientsByAttendingStaffIdCount;
	
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientByVisitNbCount}")
	private String expectedPatientByVisitNbCount;
	
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientContactEquipment}")
	private String expectedPatientContactEquipment;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientContactData}")
	private String expectedPatientContactData;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientContactsCount}")
	private String expectedPatientContactsCount;

	private String okUserToken;
	private String notAllowedToken;

	private String domain = "PRIMARY";

	private Long patientId;

	Map<String, Header> headersMap;
	
	@Autowired
	protected WebApplicationContext wac;
	@Value("${test.swagger.generated}")
	private String swaggerGeneratedPath;

	@Configuration
	public static class TestConfig {

		@Bean
		@Primary
		public PatientInListRepository patientInListRepository() {
			return Mockito.mock(PatientInListRepository.class);
		}

		@Bean
		@Primary
		public PatientRepository patientRepository() {
			return Mockito.mock(PatientRepository.class);
		}

		@Bean
		@Primary
		public PatientContactRepository patientContactRepository() {
			return Mockito.mock(PatientContactRepository.class);
		}
		@Bean
		@Primary
		public PatientNextOfKinRepository patientNextOfKinRepository() {
			return Mockito.mock(PatientNextOfKinRepository.class);
		}
		@Bean
		@Primary
		public PatientNextOfKinContactRepository patientNextOfKinContactRepository() {
			return Mockito.mock(PatientNextOfKinContactRepository.class);
		}
	}
	@Autowired
	PatientRepository patientRepository;

	@Autowired
	PatientNextOfKinRepository patientNextOfKinRepository;
	
	@Autowired
	PatientNextOfKinContactRepository patientNextOfKinContactRepository;

	@Autowired
	PatientInListRepository patientInListRepository;

	@Autowired
	PatientContactRepository patientContactRepository;

	@Value("${net.fluance.cockpit.app.web.company.userUsername}")
	private String allowedUsername;
	@Value("${net.fluance.cockpit.app.web.company.userPassword}")
	private String allowedPassword;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserUsername}")
	private String notAllowedUsername;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserPassword}")
	private String notAllowedPassword;
    
	@Before
	public void setUp() throws Exception {
		url = host + serverPort;
		swaggerYamlVisit = SwaggerSpecUtils.load(url + swaggerGeneratedPath);
		
		//Path to controllers
		pathPatientDetails = "/patients/1";
		pathPatientList = "/patients";
		nokListResourcePath = "/patients/1/noks";
		nokContactResourcePath = "/patients/1/noks/1/contacts";
		pathRadiologyList = "/patients/1/radiology/exams";
		
		groupNameListResourcePath = "/patients/1/lab/groupnames";
		dataListResourcePath = "/patients/1/lab";
		
		dataListSpecsResourcePath = "/patients/{pid}/lab";
		groupNameListSpecsResourcePath = "/patients/{pid}/lab/groupnames";
		
		pathSpecsPatientDetails = "/patients/{pid}";
		pathSpecsRadiologyList = "/patients/{pid}/radiology/exams";
		//pathSpecsPatientList = "/patients";
		SpecsnokListResourcePath = "/patients/{pid}/noks";
		SpecsnokContactResourcePath = "/patients/{pid}/noks/{nokid}/contacts";
		
		patientId = Long.valueOf(expectedPatientId);
		baseUrl = "http://localhost:" + serverPort + swaggerYamlVisit.getBasePath();
		authBaseUrl = "http://localhost:" + serverPort;
		okUserToken = getAccessToken(okUserUsername, domain);
		notAllowedToken = getAccessToken(notAllowedUserUsername, domain);
		specLocation = specsLocation;

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void testGetByPidControllerOk() throws Exception {

		List<PatientContact> patientContact = new ArrayList<>();
		Long pid = (long) 2342342;
		patientContact.add(new PatientContact(pid, "nbType", "equpment", "data"));

		when(patientContactRepository.findByPid(anyLong())).thenReturn(patientContact);
		when(patientRepository.findOne(anyLong())).thenReturn(new net.fluance.cockpit.core.model.jdbc.patient.Patient((long) 123123, "language", "courtesy", "firstname", "lastname", "maidenName", new Date(), "65165", "nationality", "sex", "address", "address2", "locality", "postCode", "subPostCode", "adressComplement", "careOf", "canton", "country", false, new Date(), null));

		CloseableHttpResponse response = sendRequest(url + swaggerYamlVisit.getBasePath() + pathPatientDetails, okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		// test status code
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		// test headers
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlVisit, pathSpecsPatientDetails, HttpStatus.OK));

		// test schema
		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsPatientDetails, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
		
		
		long patientIdNotFound = (long) 12345;
		when(patientRepository.findOne((long) 12345)).thenReturn(null);
		
		response = sendRequest(url + swaggerYamlVisit.getBasePath() + pathPatientDetails+"/213", okUserToken);
		jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		// test status code
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusLine().getStatusCode());
		
	}

	@Test
	public void testGetByPidControllerUnAuthorized() throws Exception {
		CloseableHttpResponse response = sendRequest(url + swaggerYamlVisit.getBasePath() + pathPatientDetails + "/"+ patientId, "");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());

		// INVALID ACCES_TOKEN
		response = sendRequest(url + swaggerYamlVisit.getBasePath() + pathPatientDetails + "/" +patientId, "FAKE_ACCESS_TOKEN");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
	}

	@Test
	public void testGetByPidControllerForbidden() throws Exception{
		CloseableHttpResponse response = sendRequest(url + swaggerYamlVisit.getBasePath() + pathPatientDetails + "/"+ patientId, notAllowedToken);
		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusLine().getStatusCode());
	}

	@Test
	public void testFindByCompanyIdAndVisitNbControllerOk() throws Exception {
		String parameters = "?companyid=" + expectedPatientLastVisitCompanyId + "&visitnb="+expectedPatientLastVisitNumber;
		testPatientList(parameters);
	}		

	@Test
	public void testFindByCompanyIdLastNameFirstNameMaidenNameControllerOk() throws Exception {
		StringBuilder parameters = new StringBuilder();
		parameters.append("?companyid=");
		parameters.append(expectedPatientLastVisitCompanyId);
		parameters.append("&lastname=");
		parameters.append(expectedPatientLastName);
		parameters.append("&firstname=");
		parameters.append(expectedPatientFirstName);
		parameters.append("&maidenname=");
		parameters.append(expectedPatientMaidenName);
		testPatientList(parameters.toString());
	}

	@Test
	public void testFindByCompanyIdLastNameBirthDateControllerOk() throws Exception {
		StringBuilder parameters = new StringBuilder();
		parameters.append("?companyid=");
		parameters.append(expectedPatientLastVisitCompanyId);
		parameters.append("&lastname=");
		parameters.append(expectedPatientLastName);
		parameters.append("&birthdate=");
		parameters.append(java.sql.Date.valueOf(expectedPatientDateOfBirth));
		testPatientList(parameters.toString());
	}

	@Test
	public void testFindByCompanyIdAdmissionUnknowControllerOk() throws Exception {
		StringBuilder parameters = new StringBuilder();
		parameters.append("?companyid=");
		parameters.append(expectedPatientLastVisitCompanyId);
		parameters.append("&admissionstatus=");
		parameters.append(AdmissionStatusEnum.UNKNOWN.name());
		testPatientList(parameters.toString());
	}
	
	@Test
	public void testFindByMyUnitControllerOk() throws Exception {
		StringBuilder parameters = new StringBuilder();
		parameters.append("?companyid=");
		parameters.append(expectedPatientLastVisitCompanyId);
		parameters.append("&patientunit=");
		parameters.append("abc");
		testPatientList(parameters.toString());
	}

	private void testPatientList(String parameters)
			throws URISyntaxException, HttpException, IOException, NoSuchAlgorithmException, KeyStoreException,
			KeyManagementException, ProcessingException, JsonParseException, JsonMappingException {

		List<PatientInList> patientInList = new ArrayList<>();
		
		patientInList.add(new PatientInList(54,  (long) 2342, "firstname", "lastname", "maidenName", new Date(), "sex", false, null, "address", "locality", "postCode",
				(long) 123123, (long)expectedPatientLastVisitCompanyId, new java.sql.Timestamp((long) 34234), new java.sql.Timestamp((long) 324234),
				"patientClass", "patientClassDesc", "patientType", "patientTypeDesc", "patientCase", "patientCaseDesc", "hospService"," hospServiceDesc", "admissionType", "admissionTypeDesc", "patientUnit",
				"patientUnitDesc", "patientRoom", "patientBed", "financialClass", "financialClassDesc"));
		
		when(patientInListRepository.findOne(anyLong())).thenReturn(new PatientInList(54,  (long) 2342, "firstname", "lastname", "maidenName", new Date(), "sex", false, null, "address", "locality", "postCode",
				(long) 123123, (long) 3421342, new java.sql.Timestamp((long) 34234), new java.sql.Timestamp((long) 324234),
				"patientClass", "patientClassDesc", "patientType", "patientTypeDesc", "patientCase", "patientCaseDesc", "hospService"," hospServiceDesc", "admissionType", "admissionTypeDesc", "patientUnit",
				"patientUnitDesc", "patientRoom", "patientBed", "financialClass", "financialClassDesc"));
		
		when(patientInListRepository.findByMyUnit(anyInt(), anyString(), anyInt(), anyInt(), anyString(), anyString())).thenReturn( patientInList);

		when(patientRepository.findOne(anyLong())).thenReturn(new net.fluance.cockpit.core.model.jdbc.patient.Patient((long) 123123, "courtesy", "language", "firstname", "lastname", "maidenName", new Date(), "65165", "nationality", "sex", "address", "address2", "locality", "postCode", "subPostCode", "adressComplement", "careOf", "canton", "country", false, new Date(), null));

		CloseableHttpResponse response = sendRequest(url +pathPatientList + parameters, okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		// test status code
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		// test headers
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlVisit, pathPatientList, HttpStatus.OK));

		// test schema
		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathPatientList, HttpMethod.GET, HttpStatus.OK);
//		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}

	@Test
	public void testFindByCriteriaControllerUnAuthorized() throws Exception {
		CloseableHttpResponse response = sendRequest(url + swaggerYamlVisit.getBasePath()+ pathPatientList, "");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());

		// INVALID ACCES_TOKEN
		response = sendRequest(url + swaggerYamlVisit.getBasePath()+ pathPatientList, "FAKE_ACCESS_TOKEN");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
	}

	@Test
	public void testFindByCriteriaControllerForbidden() throws Exception{
		CloseableHttpResponse response = sendRequest(url + swaggerYamlVisit.getBasePath() +pathPatientList, notAllowedToken);
		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusLine().getStatusCode());
	}

	@Override
	public void tearDown() {
		swaggerYamlVisit = null;
	}

	@Test
	public void testcheckNokListOk() throws Exception {
		checkNokListOk();
	}
	private void checkNokListOk()
			throws KeyManagementException, UnsupportedOperationException, NoSuchAlgorithmException, KeyStoreException,
			IOException, HttpException, URISyntaxException, ParseException, ProcessingException {

		StringBuilder parameters = new StringBuilder();
		parameters.append("?pid=");
		parameters.append(Integer.parseInt(expectedNokPatientId));

		when(patientNextOfKinRepository.findOne(anyLong())).thenReturn(new PatientNextOfKin(23, (long) 12312, "firstName", "lastName", (long) 2312, "addressType", "address",
				"address2", "complement", "locality", "postCode", "canton", "country", "type", "careOf", null, null));

		CloseableHttpResponse response = sendRequest(url +nokListResourcePath + parameters, okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		
		// test status code
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		// test headersresponse, swaggerYamlVisit, pathPatientList, HttpStatus.OK
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlVisit, SpecsnokListResourcePath, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, SpecsnokListResourcePath, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));

		// Check size of the returned list
		ObjectMapper objectMapper = new ObjectMapper();
		List<PatientNextOfKin> nokList = objectMapper.readValue(jsonResponse,
				objectMapper.getTypeFactory().constructCollectionType(List.class, PatientNextOfKin.class));
		assertNotNull(nokList);
		assertEquals(0, nokList.size());
	}
	
	@Test
	public void checkNokContactOk()
			throws KeyManagementException, UnsupportedOperationException, NoSuchAlgorithmException, KeyStoreException,
			IOException, HttpException, URISyntaxException, ParseException, ProcessingException {
	
		List<PatientNextOfKinContact> patientNextOfKinContact = new ArrayList<>();
		
		patientNextOfKinContact.add(new PatientNextOfKinContact((long) 234, "nbType", "equipment", "data"));
		
		when(patientNextOfKinContactRepository.findByNokId((anyLong()))).thenReturn(patientNextOfKinContact);

		CloseableHttpResponse response = sendRequest(url +nokContactResourcePath, okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		
		// test status code
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		// test headersresponse, swaggerYamlVisit, pathPatientList, HttpStatus.OK
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlVisit, SpecsnokContactResourcePath, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, SpecsnokContactResourcePath, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));

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
