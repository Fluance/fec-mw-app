package net.fluance.cockpit.app.web.filter;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.HttpException;
import org.apache.http.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import net.fluance.app.security.auth.OAuth2AccessToken;
import net.fluance.app.security.util.OAuth2Helper;
import net.fluance.app.test.AbstractWebIntegrationTest;
import net.fluance.app.web.servlet.filter.BasicSecurityFilter;
import net.fluance.app.web.servlet.filter.CORSFilter;
import net.fluance.cockpit.Application;
import net.fluance.cockpit.core.model.jdbc.company.CompaniesList;
import net.fluance.cockpit.core.model.jdbc.company.ServiceList;
import net.fluance.cockpit.core.model.jdbc.company.Unit;
import net.fluance.cockpit.core.model.jdbc.patient.Patient;
import net.fluance.cockpit.core.model.jdbc.patient.PatientInList;
import net.fluance.cockpit.core.model.wrap.patient.AdmissionStatusEnum;
import net.fluance.cockpit.core.model.wrap.patient.PatientSexEnum;
import net.fluance.cockpit.core.repository.jdbc.company.CompaniesListRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientInListRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientRepository;
import net.fluance.commons.codec.Base64Utils;
import net.fluance.commons.codec.PKIUtils;
import net.fluance.commons.json.jwt.JWTUtils;


@ComponentScan("net.fluance.cockpit.app")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class EntitlementFilterTest extends AbstractWebIntegrationTest implements ApplicationListener<EmbeddedServletContainerInitializedEvent>{

	private static Logger LOGGER = LogManager.getLogger(EntitlementFilterTest.class);
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private OAuth2Helper oAuth2Helper;
	
	@Autowired
	protected WebApplicationContext wac;
	
	@Mock
	@Autowired
	private CompaniesListRepository companiesListRepository;
	
	@Autowired
	private PatientRepository patientRepository;
	
	@Autowired
	private PatientInListRepository patientInListRepository;

	@Autowired
	private BasicSecurityFilter basicSecurityFilter;
	@Autowired
	private UserProfileFilter userProfileFilter;
	@Autowired
	private HeadersFilter headersFilter;
	@Autowired
	private CORSFilter corsFilter;
	
	@Value("${singlePatient1.id}")
	private String patientid;
	
	@Value("${singlePatient1.lastname}")
	private String lastname;
	
	@Value("${singlePatient1.firstname}")
	private String firstname;
	
	@Value("${singlePatient1.birthdate}")
	private String birthdate;
	
	@Value("${singlePatient1.sex}")
	private String sex;
	
	@Value("${singlePatient.jwt.header}")
	private String jwtHeader;
	@Value("${singlePatient1.jwt.payload}")
	private String jwtSinglePatientPayload;
	@Value("${trustedPartner.jwt.payload}")
	private String jwtTrustedPartnerPayload;
	
	@Value("${singlePatient1.jwt.payloadWrongPid}")
	private String payloadWrongPid;
	
	@Value("${singlePatient1.jwt.payloadWrongFirstName}")
	private String payloadWrongFirstName;
	
	@Value("${singlePatient1.jwt.payloadWrongLastname}")
	private String payloadWrongLastname;
	
	@Value("${singlePatient1.jwt.payloadWrongBirthDate}")
	private String payloadWrongBirthDate;
	
	@Value("${singlePatient1.jwt.payloadWrongSex}")
	private String payloadWrongSex;
	
	@Value("${server.port}")
	private int serverPort;
	
	@Value("${server.ssl.key-alias}")
	private String sslKeyAlias;
	
	@Value("${server.ssl.enabled}")
	private String sslEnabled;
	
	@Value("${server.ssl.key-password}")
	private String sslKeyPassword;
	
	@Value("${singlePatient1.lab.groupname}")
	private String patient1LabGroupName;
	
	private final String certificateAlias = "fluance";
	private PublicKey publicKey;
	PrivateKey signingKey;
	
	private int runningPort;
	
	private static final String DOMAIN = "PRIMARY";		
	
	@Value("${singlePatient1.id}")
	private long patient1Id;
	@Value("${singlePatient1.visitNb}")
	private long patient1VisitNb;
	@Value("${singlePatient1.invoiceId}")
	private long patient1InvoiceId;
	@Value("${singlePatient1.lastname}")
	private String patient1LastName;
	@Value("${singlePatient1.firstname}")
	private String patient1FirstName;
	@Value("${singlePatient1.birthdate}")
	private String patient1Birthdate;
	@Value("${singlePatient1.sex}")
	private String patient1Sex;
	
	@Value("${singlePatient1.wrongPid}")
	private long patient1WrongPid;
	@Value("${singlePatient1.wrongVisitNb}")
	private long patient1WrongVisitNb;
	@Value("${singlePatient1.wrongInvoiceId}")
	private long patient1WrongInvoiceId;
	@Value("${singlePatient1.wrongLastname}")
	private String patient1WrongLastname;
	@Value("${singlePatient1.wrongFirstname}")
	private String patient1WrongFirstname;
	@Value("${singlePatient1.wrongBirthdate}")
	private String patient1WrongBirthdate;
	@Value("${singlePatient1.wrongSex}")
	private String patient1WrongSex;
	
	private String patient1VisitAdmitDateStr = "2015-01-15 00:00:00";
	private String patient1VisitDischargeDateStr = "2015-01-25 00:00:00";
	
	@Value("${oauth2.service.token.url}")
	private String oAuth2TokenUrl;
	@Value("${oauth2.service.client.authorization-type}")
	private String oAuth2ClientAuthorizationType;
	@Value("${oauth2.service.client.id}")
	private String oAuth2ClientId;
	@Value("${oauth2.service.client.secret}")
	private String oAuth2ClientSecret;
	
	@Value("${net.fluance.cockpit.app.web.company.userUsername}")
	private String allowedUsername;
	@Value("${net.fluance.cockpit.app.web.company.userPassword}")
	private String allowedPassword;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserUsername}")
	private String notAllowedUsername;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserPassword}")
	private String notAllowedPassword;
	
	String allowedUserOAuth2AccessToken;
	
	private String companiesResourcePath = "/companies";
	private String patientDetailResourcePath = "/patients/{pid}";
	
	@PostConstruct
	public void init() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(basicSecurityFilter, "/companies", "/companies/*", "/patients", "/patients/*").addFilter(userProfileFilter, "/*").addFilter(headersFilter, "/*").addFilter(corsFilter, "/*").build();
	}
	
	@Override
	public void setUp() throws Exception {
		
		ClassLoader classLoader = getClass().getClassLoader();
        String keyStorePath = new File(classLoader.getResource("keystore.jks").getFile()).getAbsolutePath();
        assertTrue(new File(keyStorePath).exists());
        String trustStorePath = new File(classLoader.getResource("truststore.jks").getFile()).getAbsolutePath();
        assertTrue(new File(trustStorePath).exists());
       
        LOGGER.info("keyStorePath: " + keyStorePath);
        LOGGER.info("trustStorePath: " + trustStorePath);
		
		System.setProperty("javax.net.ssl.keyStore", keyStorePath);
		System.setProperty("javax.net.ssl.keyStoreType", "JKS");
		System.setProperty("javax.net.ssl.keyStorePassword", "fluance");	
		
		System.setProperty("javax.net.ssl.trustStore", trustStorePath);
		System.setProperty("javax.net.ssl.trustStoreType", "JKS");
		System.setProperty("javax.net.ssl.trustStorePassword", "fluance");
		
		signingKey = PKIUtils.readPrivateKey(sslKeyAlias, sslKeyPassword);
		publicKey = PKIUtils.readPublicKey(certificateAlias, PKIUtils.DEFAULT_CERTIFICATE_TYPE);
		
		
		Date birthDate = DATE_FORMAT.parse(patient1Birthdate);
		when(patientRepository.findOne(patient1Id)).thenReturn(new Patient(patient1Id, "en", "courtesy", patient1FirstName, patient1LastName, null, birthDate, "756.0000.0000.01", "Switzerland", "Female", "Fluance Street Street 00B", null, "There", "0000", null, null, null, "Solothurn", "Switzerland", false, null, null));
		
		when(patientInListRepository.findByCriteria(patientSearchCriteria(), PatientSexEnum.UNKNOWN, AdmissionStatusEnum.UNKNOWN, "", null)).thenReturn(patientByCriteriaList());
		when(companiesListRepository.findAll()).thenReturn(companiesList());
		
		baseUrl = ((Boolean.valueOf(sslEnabled)) ? "https" : "http") + "://localhost:" + serverPort; 
	}

	@Override
	public Logger getLogger() {
		return null;
	}

	@Test
	public void checkOAuth2Authorization() throws Exception {
		String clientAuth = Base64Utils.encode(oAuth2ClientId + ":" + oAuth2ClientSecret);
		OAuth2AccessToken oAuth2Response = oAuth2Helper.accessToken(oAuth2TokenUrl, clientAuth, "password", allowedUsername, allowedPassword);
		
		// OAuth2 valid token
		allowedUserOAuth2AccessToken = oAuth2Response.getAccessToken();
		
		// Read patient detail
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + patientDetailResourcePath, patient1Id)
				.header("Authorization", "Bearer " + allowedUserOAuth2AccessToken))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
		// OAuth2 invalid token
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + companiesResourcePath)
				.header("Authorization", "Bearer " + allowedUserOAuth2AccessToken))
				.andExpect(MockMvcResultMatchers.status().isOk());

		allowedUserOAuth2AccessToken = "FAKE_OAUTH2_ACCESS_TOKEN";
	}

	@Test
	public void checkJwtSinglePatientAuthorization() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode header = objectMapper.readTree(jwtHeader);
		
		// Single patient good JWT
		JsonNode payload = objectMapper.readTree(jwtSinglePatientPayload);
		String jwt = JWTUtils.buildToken(signingKey, header, payload);
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + companiesResourcePath)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(MockMvcResultMatchers.status().isForbidden());
		// Read patient detail
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + patientDetailResourcePath, patient1Id)
				.header("Authorization", "Bearer " + jwt))
				.andExpect(MockMvcResultMatchers.status().isOk());

		// Single patient bad JWT
		payload = objectMapper.readTree(jwtSinglePatientPayload);
		((ObjectNode)payload).put("pid", 0);
		jwt = JWTUtils.buildToken(signingKey, header, payload);
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + companiesResourcePath)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(MockMvcResultMatchers.status().isForbidden());

		// Single patient invalid JWT
		jwt = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa.bbbbbbbbbbbbbbbbbbbbbbbbb.ccccccccccccccccccccccccccc";
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + companiesResourcePath)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}
	
	@Test
	public void checkJwtTrustedPartnerPayloadAuthorization() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode header = objectMapper.readTree(jwtHeader);
		
		JsonNode trustedPartnerPayload = objectMapper.readTree(jwtTrustedPartnerPayload);
		
		// good JWT
		String jwt = JWTUtils.buildToken(signingKey, header, trustedPartnerPayload);
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + companiesResourcePath)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(MockMvcResultMatchers.status().isOk());
		// Read patient detail
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + patientDetailResourcePath, patient1Id)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		// bad JWT
		trustedPartnerPayload = objectMapper.readTree(jwtTrustedPartnerPayload);
		((ObjectNode)trustedPartnerPayload).put("username", "UNKNOWN_USER");
		jwt = JWTUtils.buildToken(signingKey, header, trustedPartnerPayload);
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + companiesResourcePath)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(MockMvcResultMatchers.status().isForbidden());
		// Read patient detail
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + patientDetailResourcePath, patient1Id)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(MockMvcResultMatchers.status().isForbidden());
		
		//invalid JWT
		jwt = "DDDDDDDDDDDDDDDDD.EEEEEEEEEEEEEEEEEEEE.FFFFFFFFFFFFFFFFFFFFFFFFFFF";
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + companiesResourcePath)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(MockMvcResultMatchers.status().isUnauthorized());
		// Read patient detail
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + patientDetailResourcePath, patient1Id)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}

	private List<CompaniesList> companiesList() throws java.text.ParseException {
		List<CompaniesList> list = new ArrayList<>();
		
		List<Unit> company1Units = new ArrayList<>();
		company1Units.add(new Unit("UNIT-1A", "UNIT-1A description"));
		company1Units.add(new Unit("UNIT-1B", "UNIT-1B description"));
		List<ServiceList> company1Services = new ArrayList<>();
		company1Services.add(new ServiceList("SERVICE-1A", "SERVICE-1A description"));
		company1Services.add(new ServiceList("SERVICE-1B", "SERVICE-1B description"));

		List<Unit> company2Units = new ArrayList<>();
		company2Units.add(new Unit("UNIT-2A", "UNIT-2A description"));
		company2Units.add(new Unit("UNIT-2B", "UNIT-2B description"));
		List<ServiceList> company2Services = new ArrayList<>();
		company2Services.add(new ServiceList("SERVICE-2A", "SERVICE-2A description"));
		company2Services.add(new ServiceList("SERVICE-2B", "SERVICE-2B description"));
		
		List<Unit> company3Units = new ArrayList<>();
		company3Units.add(new Unit("UNIT-3A", "UNIT-3A description"));
		company3Units.add(new Unit("UNIT-3B", "UNIT-3B description"));
		List<ServiceList> company3Services = new ArrayList<>();
		company3Services.add(new ServiceList("SERVICE-3A", "SERVICE-3A description"));
		company3Services.add(new ServiceList("SERVICE-3B", "SERVICE-3B description"));
		
		CompaniesList company1 = new CompaniesList(1, "COMPANY-1", "Company 1", company1Units, company1Services);
		CompaniesList company2 = new CompaniesList(2, "COMPANY-2", "Company 2", company2Units, company2Services);
		CompaniesList company3 = new CompaniesList(3, "COMPANY-3", "Company 3", company3Units, company3Services);
		
		list.add(company1);
		list.add(company2);
		list.add(company3);
		
		return list;
	}
	
	private Map<String, Object> patientSearchCriteria() {
		Map<String, Object> criteria = new HashMap<>();
		
			criteria.put("patient_id", patient1Id);
			criteria.put("lastname", patient1LastName);
			criteria.put("firstname", patient1FirstName);
//			criteria.put("sex", patient1Sex);
			criteria.put("birthdate", patient1Birthdate);

		return criteria;
	}

	private List<PatientInList> patientByCriteriaList() throws java.text.ParseException {
		List<PatientInList> list = new ArrayList<>();
		
		PatientInList patient = new PatientInList(1, patient1Id,
				patient1FirstName, patient1LastName, null,
				DATE_FORMAT.parse(patient1Birthdate), patient1Sex, false, null, null, null,
				null, patient1VisitNb, 1L,
				Timestamp.valueOf(patient1VisitAdmitDateStr), Timestamp.valueOf(patient1VisitDischargeDateStr),
				"patientclass", "patientclassdesc", "patienttype", "patienttypedesc", "patientcase", "patientcasedesc", "hospservice", "hospservicedesc", "admissiontype", "admissiontypedesc", "patientunit", "patientunitdesc", "room", "bed",
				"financialclass", "financialclassdesc");
		
		list.add(patient);
		
		return list;
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
    public void onApplicationEvent(final EmbeddedServletContainerInitializedEvent event) {
        runningPort = event.getEmbeddedServletContainer().getPort();
    }
    
}
