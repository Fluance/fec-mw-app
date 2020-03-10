package net.fluance.cockpit.app.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
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
import net.fluance.cockpit.core.model.CompanyReference;
import net.fluance.cockpit.core.model.jdbc.physician.PhysicianList;
import net.fluance.cockpit.core.model.jdbc.visit.Diagnosis;
import net.fluance.cockpit.core.model.jdbc.visit.GuarantorList;
import net.fluance.cockpit.core.model.jdbc.visit.Treatment;
import net.fluance.cockpit.core.model.jdbc.visit.VisitDetail;
import net.fluance.cockpit.core.model.jdbc.visit.VisitInfo;
import net.fluance.cockpit.core.model.jdbc.visit.VisitList;
import net.fluance.cockpit.core.model.jdbc.visit.VisitPolicyDetail;
import net.fluance.cockpit.core.model.jdbc.visit.VisitPolicyList;
import net.fluance.cockpit.core.repository.jdbc.physician.PhysicianListRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.DiagnosisRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.GuarantorListRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.TreatmentRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitListRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitPolicyDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitPolicyListRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class VisitControllerTest extends AbstractWebIntegrationTest {

	private static Logger LOGGER = LogManager.getLogger(VisitControllerTest.class);

	private Swagger swaggerYamlController;
	private String url;
	private String pathVisitList;
	private String pathVisitDetail;
	private String pathDiagnosisList;
	private String pathTreatmentList;
	private String pathGuarantorList;
	private String pathPolicyList;
	private String pathPolicyDetail;
	private String pathDoctorList;
	
	private String pathSpecsVisitDetail ;
	private String pathSpecsDiagnosisList;
	private String pathSpecsTreatmentList;
	private String pathSpecsGuarantorList;
	private String pathSpecsPolicyList;
	private String pathSpecsPolicyDetail;
	private String pathSpecsDoctorList;

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
		public VisitListRepository visitListRepository() {
			return Mockito.mock(VisitListRepository.class);
		}

		@Bean
		@Primary
		public VisitDetailRepository visitDetailRepository() {
			return Mockito.mock(VisitDetailRepository.class);
		}

		@Bean
		@Primary
		public DiagnosisRepository diagnosisListRepository() {
			return Mockito.mock(DiagnosisRepository.class);
		}

		@Bean
		@Primary
		public TreatmentRepository treatmentListRepository() {
			return Mockito.mock(TreatmentRepository.class);
		}

		@Bean
		@Primary
		public GuarantorListRepository guarantorListRepository() {
			return Mockito.mock(GuarantorListRepository.class);
		}

		@Bean
		@Primary
		public VisitPolicyListRepository policyListRepository() {
			return Mockito.mock(VisitPolicyListRepository.class);
		}

		@Bean
		@Primary
		public VisitPolicyDetailRepository policyDetailRepository() {
			return Mockito.mock(VisitPolicyDetailRepository.class);
		}

		@Bean
		@Primary
		public PhysicianListRepository physicianListRepository() {
			return Mockito.mock(PhysicianListRepository.class);
		}
	}

	@Autowired
	VisitListRepository visitListRepository;

	@Autowired
	VisitDetailRepository visitDetailRepository;

	@Autowired
	DiagnosisRepository diagnosisListRepository;

	@Autowired
	TreatmentRepository treatmentListRepository;

	@Autowired
	GuarantorListRepository guarantorListRepository;

	@Autowired
	VisitPolicyListRepository policyListRepository;

	@Autowired
	VisitPolicyDetailRepository policyDetailRepository;

	@Autowired
	PhysicianListRepository physicianListRepository;

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
		pathVisitList = "/visits";
		pathVisitDetail = "/visits/1";
		pathDiagnosisList = "/visits/1/diagnosis";
		pathTreatmentList = "/visits/1/treatments";
		pathGuarantorList = "/visits/1/guarantors";
		pathPolicyList = "/visits/1/policies";
		pathPolicyDetail = "/visits/1/policies";
		pathDoctorList = "/visits/1/doctors";
		
		
		pathSpecsVisitDetail = "/visits/{visitid}";
		pathSpecsDiagnosisList = "/visits/{visitid}/diagnosis";
		pathSpecsTreatmentList = "/visits/{visitid}/treatments";
		pathSpecsGuarantorList = "/visits/{visitid}/guarantors";
		pathSpecsPolicyList = "/visits/{visitid}/policies";
		pathSpecsPolicyDetail = "/visits/{visitid}/policies/{guarantorid}/{priority}/{subpriority}";
		pathSpecsDoctorList = "/visits/{visitid}/doctors";

		okUserToken = getAccessToken(companyUserUsername, domain);
		notAllowedToken = getAccessToken(companyNotAllowedUserUsername, domain);

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testVisitListControllerOk() throws Exception {
		List<VisitList> visitList = new ArrayList<>();
		
		VisitList visit1 = new VisitList();
		visit1.setNb_records(1);
		VisitInfo visitInfo1 = new VisitInfo();
		visitInfo1.setNb((long)2000001);
		visitInfo1.setAdmitDate(null);
		visitInfo1.setDischargeDate(null);
		visitInfo1.setPatientclass("patientclass");
		visitInfo1.setPatientclassdesc("patientclassdesc");
		visitInfo1.setPatienttype("patienttype");
		visitInfo1.setPatienttypedesc("patienttypedesc");
		visitInfo1.setPatientcase("patientcase");
		visitInfo1.setPatientcasedesc("patientcasedesc");
		visitInfo1.setHospservice("hospservice");
		visitInfo1.setHospservicedesc("hospservicedesc");
		visitInfo1.setAdmissiontype("admissiontype");
		visitInfo1.setAdmissiontypedesc("admissiontypedesc");
		visitInfo1.setPatientunit("patientunit");
		visitInfo1.setPatientunitdesc("patientunitdesc");
		visitInfo1.setFinancialclass("financialclass");
		visitInfo1.setFinancialclassdesc("financialclassdesc");
		visit1.setVisitInfo(visitInfo1);
		CompanyReference companyReference1 = new CompanyReference( 1, "name", "code");
		visit1.setCompany(companyReference1);
		visitList.add(visit1);
		
		when(visitListRepository.findByCompanyIdAndPatientId(1,123,false,null,null,10,0)).thenReturn(visitList);

		CloseableHttpResponse response = sendRequest(url + pathVisitList+ "?companyid=1&pid=123", okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathVisitList, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathVisitList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}

	@Test
	public void testVisitListControllerUnAuthorized() throws Exception {
		CloseableHttpResponse response = sendRequest(url + pathVisitList, "");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());

		// INVALID ACCES_TOKEN
		response = sendRequest(url + pathVisitList, "FAKE_ACCESS_TOKEN");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
	}

	@Test
	public void testVisitListControllerForbidden() throws Exception{
		CloseableHttpResponse response = sendRequest(url + pathVisitList+ "?companyid=123&pid=1", notAllowedToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusLine().getStatusCode());
	}

	@Test
	public void testVisitDetailControllerOk() throws Exception {
		when(visitDetailRepository.findByNb(anyLong())).thenReturn(new VisitDetail((long)123, (long)123, new Date(), new Date(), new Date(), "patientclass", "patientclassdesc", "patienttype", "patienttypedesc", "patientcase", "patientcasedesc", "hospservice", "hospservicedesc", "admissiontype"," admissiontypedesc", "patientunit", "patientunitdesc", "patientroom", "patientbed", "priorroom", "priorbed", "priorunit", "priorunitdesc", "admitsource", "admitsourcedesc", "financialclass", "financialclassdesc", "hl7code", new CompanyReference(1, "name", "code")));

		CloseableHttpResponse response = sendRequest(url + pathVisitDetail + "123", okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsVisitDetail, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsVisitDetail, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}

	@Test
	@Ignore("findByVisitNb method changed")
	public void testVisitDiagnosisListControllerOk() throws Exception {
		/*List<Diagnosis> diagnosisList = new ArrayList<>();
		diagnosisList.add(new Diagnosis((long)1, "code", 1, "type2", "description", "language"));
		when(diagnosisListRepository.findByVisitNb(123, 10)).thenReturn(diagnosisList);

		CloseableHttpResponse response = sendRequest(url + pathDiagnosisList + "?visitnb=123" , okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsDiagnosisList, HttpStatus.OK));
		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsDiagnosisList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));*/
	}

	@Test
	@Ignore("findByVisitNb method changed")
	public void testTreatmentListControllerOk() throws Exception {
	/*	List<Treatment> treatmentList = new ArrayList<>();
		treatmentList.add(new Treatment(1, "code", 1, "type", "description", "language"));
		when(treatmentListRepository.findByVisitNb(123, 10)).thenReturn(treatmentList);

		CloseableHttpResponse response = sendRequest(url + pathTreatmentList + "?visitnb=123", okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsTreatmentList, HttpStatus.OK));
		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsTreatmentList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));*/
	}

	@Test
	public void guarantorListControllerOk() throws Exception {
		// Init Repository mocks
		List<GuarantorList> guarantorList = new ArrayList<>();
		guarantorList.add(new GuarantorList(1, null, "name", "code", "address", "address2", "locality", "postcode", "canton", "country", "complement", null, null, false));
		when(guarantorListRepository.findByVisitNb(123, "orderBy", "sortorder", 10, 0)).thenReturn(guarantorList);

		CloseableHttpResponse response = sendRequest(url + pathGuarantorList + "?visitnb=123", okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsGuarantorList, HttpStatus.OK));
		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsGuarantorList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
		}

	@Test
	public void testPolicyListControllerOk() throws Exception {
		List<VisitPolicyList> policyList = new ArrayList<>();
		policyList.add(new VisitPolicyList(1, 1, "name", "code", 1, 1, "hospclass"));
		when(policyListRepository.findByVisitNb(123, "orderBy", "sortorder", 10, 0)).thenReturn(policyList);

		CloseableHttpResponse response = sendRequest(url + pathPolicyList +"?visitnb=123", okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsPolicyList, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsPolicyList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}

	@Test
	public void testPolicyDetailControllerOk() throws Exception {
		when(policyDetailRepository.findByVisitNbAndGuarantorIdAndPriorityAndSubPriority(1, 123, 1, 1)).thenReturn(new VisitPolicyDetail(1, "name", "code", 2, 2, "hospclass", true, "123", 1.0, "accidentnb","accidentDate"));

		CloseableHttpResponse response = sendRequest(url + pathPolicyDetail +"/123/1/1", okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsPolicyDetail, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsPolicyDetail, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
	}

	@Test
	public void testDoctorListControllerOk() throws Exception {
		List<PhysicianList> physicianList = new ArrayList<>();
		physicianList.add(new PhysicianList(1, "attendingPhysicianFirstname", "attendingPhysicianLastname", 1, 1, "referringPhysicianFirstname", "referringPhysicianLastname", 1, 1, "consultingPhysicianFirstname", "consultingPhysicianLastname", 1, 1, "admittingPhysicianFirstname", "admittingPhysicianLastname", 1));
		when(physicianListRepository.findPhysicianByVisitnb(123)).thenReturn(physicianList);

		CloseableHttpResponse response = sendRequest(url + pathDoctorList , okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathSpecsDoctorList, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsDoctorList, HttpMethod.GET, HttpStatus.OK);
		assertTrue(isJsonSchemaCompliant(textSchema, jsonResponse));
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
