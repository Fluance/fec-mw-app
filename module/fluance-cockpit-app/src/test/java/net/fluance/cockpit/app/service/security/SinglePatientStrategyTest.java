package net.fluance.cockpit.app.service.security;

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
import org.junit.Ignore;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import net.fluance.app.test.AbstractWebIntegrationTest;
import net.fluance.app.web.servlet.filter.CORSFilter;
import net.fluance.cockpit.Application;
import net.fluance.cockpit.app.service.domain.PatientService;
import net.fluance.cockpit.app.web.filter.EntitlementFilterTest;
import net.fluance.cockpit.app.web.filter.HeadersFilter;
import net.fluance.cockpit.app.web.filter.UserProfileFilter;
import net.fluance.cockpit.core.model.CompanyReference;
import net.fluance.cockpit.core.model.jdbc.invoice.Invoice;
import net.fluance.cockpit.core.model.jdbc.invoice.InvoiceList;
import net.fluance.cockpit.core.model.jdbc.lab.Groupname;
import net.fluance.cockpit.core.model.jdbc.lab.LabData;
import net.fluance.cockpit.core.model.jdbc.patient.Patient;
import net.fluance.cockpit.core.model.jdbc.patient.PatientInList;
import net.fluance.cockpit.core.model.jdbc.visit.Diagnosis;
import net.fluance.cockpit.core.model.jdbc.visit.GuarantorList;
import net.fluance.cockpit.core.model.jdbc.visit.Treatment;
import net.fluance.cockpit.core.model.jdbc.visit.VisitDetail;
import net.fluance.cockpit.core.model.jdbc.visit.VisitInfo;
import net.fluance.cockpit.core.model.jdbc.visit.VisitList;
import net.fluance.cockpit.core.model.jdbc.visit.VisitPolicyList;
import net.fluance.cockpit.core.model.wrap.patient.AdmissionStatusEnum;
import net.fluance.cockpit.core.model.wrap.patient.PatientSexEnum;
import net.fluance.cockpit.core.repository.jdbc.guarantor.GuarantorRepository;
import net.fluance.cockpit.core.repository.jdbc.invoice.InvoiceListRepository;
import net.fluance.cockpit.core.repository.jdbc.invoice.InvoiceRepository;
import net.fluance.cockpit.core.repository.jdbc.lab.GroupnameRepository;
import net.fluance.cockpit.core.repository.jdbc.lab.LabDataRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientContactRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientInListRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientNextOfKinContactRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientNextOfKinRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientRepository;
import net.fluance.cockpit.core.repository.jdbc.physician.PhysicianListRepository;
import net.fluance.cockpit.core.repository.jdbc.radiology.RadiologyRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.DiagnosisRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.GuarantorListRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.TreatmentRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitListRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitPolicyDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitPolicyListRepository;
import net.fluance.commons.codec.Base64Utils;
import net.fluance.commons.codec.PKIUtils;
import net.fluance.commons.json.jwt.JWTUtils;


@ComponentScan("net.fluance.cockpit.app")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class SinglePatientStrategyTest extends AbstractWebIntegrationTest implements ApplicationListener<EmbeddedServletContainerInitializedEvent>{

	private static Logger LOGGER = LogManager.getLogger(SinglePatientStrategyTest.class);
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	protected WebApplicationContext wac;
	
	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private PatientNextOfKinRepository patientNextOfKinRepository;
	
	@Autowired
	private PatientNextOfKinContactRepository patientNextOfKinContactRepository;

	@Autowired
	private PatientInListRepository patientInListRepository;

	@Autowired
	private PatientContactRepository patientContactRepository;
	
	@Autowired
	private VisitListRepository visitListRepository;

	@Autowired
	private VisitDetailRepository visitDetailRepository;

	@Autowired
	private DiagnosisRepository diagnosisListRepository;

	@Autowired
	private TreatmentRepository treatmentListRepository;

	@Autowired
	private GuarantorListRepository guarantorListRepository;

	@Autowired
	private VisitPolicyListRepository policyListRepository;

	@Autowired
	private VisitPolicyDetailRepository policyDetailRepository;
	
	@Autowired
	private InvoiceListRepository invoiceListRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	@Mock
	@Autowired
	private GroupnameRepository groupnameRepository;

	@Mock
	@Autowired
	private LabDataRepository labDataRepository;

	@Mock
	@Autowired
	private PatientService patientService;

	@Mock
	@Autowired
	private PhysicianListRepository physicianListRepository;

	@Mock
	@Autowired
	private GuarantorRepository guarantorRepository;

	@Mock
	@Autowired
	private RadiologyRepository radiologyRepository;

	@Autowired
	private EntitlementFilterTest entitlementFilterBean;
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
	
	@Value("${singlePatient1.token}")
	private String token;
	
	@Value("${singlePatient1.jwt.payload}")
	private String payload;
	
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
	
//	@Autowired
//	private JwtHelper jwtHelper;
	
	private static final String DOMAIN = "PRIMARY";		
	
	@Value("${singlePatient1.id}")
	private long patient1Id;
	@Value("${singlePatient1.visitNb}")
	private long patient1VisitNb;
	@Value("${singlePatient1.invoiceId}")
	private long patient1InvoiceId;
	@Value("${singlePatient1.guarantorId}")
	private int patient1GuarantorId;
	@Value("${singlePatient1.priority}")
	private long patient1Priority;
	@Value("${singlePatient1.subpriority}")
	private long patient1Subpriority;
	@Value("${singlePatient1.nokId}")
	private long patient1nokId;
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
	
	@Value("${singlePatient.jwt.header}")
	private String jwtHeader;
	@Value("${singlePatient1.jwt.payload}")
	private String jwtPayload;
	@Value("${singlePatient1.jwt.payload}")
	private String jwtPayloadWrongPid;
	@Value("${singlePatient1.jwt.payload}")
	private String jwtPayloadWrongLastName;
	@Value("${singlePatient1.jwt.payload}")
	private String jwtPayloadWrongFirstName;
	@Value("${singlePatient1.jwt.payload}")
	private String jwtPayloadWrongBirthDate;
	@Value("${singlePatient1.jwt.payload}")
	private String jwtPayloadWrongSex;
	@Value("${singlePatient1.jwt.payload}")
	private String jwtPayloadWrongAlgorithm;
	@Value("${singlePatient1.jwt.payload}")
	private String jwtPayloadWrongType;
	@Value("${singlePatient1.jwt.payload}")
	private String jwtPayloadWrongIssuer;
	@Value("${singlePatient1.jwt.payload}")
	private String jwtPayloadExpired;
	
	private String patientDetailResourcePath = "/patients/{pid}";
	private String patientVisitsResourcePath = "/visits";
	private String patientLabGroupnamesResourcePath = "/patients/{pid}/lab/groupnames";
	private String patientLabResultsResourcePath = "/patients/{pid}/lab";
	private String patientRadiologyResourcePath = "/patients/{pid}/radiology/exams";
	private String patientNokResourcePath = "/patients/{pid}/noks";
	private String patientNokContactsResourcePath = "/patients/{pid}/noks/{nokid}/contacts";
	private String invoiceDetailResourcePath = "/invoices/{invoiceId}";
	private String visitInvoicesResourcePath = "/invoices";
	private String visitDetailResourcePath = "/visits/{visitNb}";
	private String visitGuarantorsResourcePath = "/visits/{visitid}/guarantors";
	private String visitPoliciesResourcePath = "/visits/{visitid}/policies";
	private String visitDiagnosisResourcePath = "/visits/{visitid}/diagnosis";
	private String visitTreatmentsResourcePath = "/visits/{visitid}/treatments";
	private String visitPolicyDetailResourcePath = "/visits/{visitid}/policies/{guarantorid}/{priority}/{subpriority}";
	private String visitDoctorsResourcePath = "/visits/{visitid}/doctors";
	private String visitGuarantorDetailResourcePath = "/visits/{visitid}/guarantors/{guarantorid}";
	
	private String guarantorDetailResourcePath = "/guarantors/{guarantorid}";
	private String companyListResourcePath = "/companies";
	private String companyDetailResourcePath = "/companies/{companyid}";
	
	@PostConstruct
	public void init() {
		MockitoAnnotations.initMocks(this);
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(entitlementFilterBean, "/patients", "/patients/*", "/visits", "/visits/*", "/invoices", "/invoices/*", "/guarantors", "/guarantors/*", "/companies", "/companies/*").addFilter(userProfileFilter, "/*").addFilter(headersFilter, "/*").addFilter(corsFilter, "/*").build();
	}
	
	@Override
	@Ignore("findByVisitNb method parameters changed")
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
		Date patient1VisitAdmitDate = DATE_FORMAT.parse(patient1VisitAdmitDateStr);
		Date patient1VisitDischargeDate = DATE_FORMAT.parse(patient1VisitDischargeDateStr);

		when(patientRepository.findOne(patient1Id)).thenReturn(new Patient(patient1Id, "en", "courtesy", patient1FirstName, patient1LastName, null, birthDate, "756.0000.0000.01", "Switzerland", "Female", "Fluance Street Street 00B", null, "There", "0000", null, null, null, "Solothurn", "Switzerland", false, null, null));
		when(visitDetailRepository.findByNb(patient1VisitNb)).thenReturn(new VisitDetail(patient1VisitNb, patient1Id, patient1VisitAdmitDate, patient1VisitDischargeDate, patient1VisitDischargeDate, "patientclass", "patientclassdesc", "patienttype", "patienttypedesc", "patientcase", "patientcasedesc", "hospservice", "hospservicedesc", "admissiontype", "admissiontypedesc", "patientunit", "patientunitdesc", "patientroom", "patientbed", "priorroom", "priorbed", "priorunit", "priorunitdesc", "admitsource", "admitsourcedesc", "financialclass", "financialclassdesc", "hl7code",  new CompanyReference(1, "name", "code")));
		
		when(patientInListRepository.findByCriteria(patientSearchCriteria(), PatientSexEnum.UNKNOWN, AdmissionStatusEnum.UNKNOWN, "", null)).thenReturn(patientByCriteriaList());

		List<VisitList> patient1VisitList = new ArrayList<>();
		
		VisitList visit1 = new VisitList();
		visit1.setNb_records(2);
		VisitInfo visitInfo1 = new VisitInfo();
		visitInfo1.setAdmitDate(patient1VisitAdmitDate);
		visitInfo1.setDischargeDate(patient1VisitDischargeDate);
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
		patient1VisitList.add(visit1);
		
		VisitList visit2 = new VisitList();
		visit2.setNb_records(2);
		VisitInfo visitInfo2 = new VisitInfo();
		visitInfo2.setAdmitDate(patient1VisitAdmitDate);
		visitInfo2.setDischargeDate(patient1VisitDischargeDate);
		visitInfo2.setPatientclass("patientclass2");
		visitInfo2.setPatientclassdesc("patientclassdesc");
		visitInfo2.setPatienttype("patienttype2");
		visitInfo2.setPatienttypedesc("patienttypedesc2");
		visitInfo2.setPatientcase("patientcase2");
		visitInfo2.setPatientcasedesc("patientcasedesc2");
		visitInfo2.setHospservice("hospservice2");
		visitInfo2.setHospservicedesc("hospservicedesc2");
		visitInfo2.setAdmissiontype("admissiontype2");
		visitInfo2.setAdmissiontypedesc("admissiontypedesc2");
		visitInfo2.setPatientunit("patientunit2");
		visitInfo2.setPatientunitdesc("patientunitdesc2");
		visitInfo2.setFinancialclass("financialclass2");
		visitInfo2.setFinancialclassdesc("financialclassdesc2");
		visit2.setVisitInfo(visitInfo2);
		CompanyReference companyReference2 = new CompanyReference( 1, "name", "code");
		visit1.setCompany(companyReference2);
		patient1VisitList.add(visit2);
		
		when(visitListRepository.findByPatientId(patient1Id, false, "nb", "asc", 50, 0)).thenReturn(patient1VisitList);
		
		when(invoiceRepository.findOne(patient1InvoiceId)).thenReturn(new Invoice(patient1InvoiceId, patient1VisitDischargeDate, 5000.50, 1500.00, "ApdrgCode", "ApdrgDesc", "MdcCode", "MdcDesc", "Name", "Code", patient1VisitNb, patient1GuarantorId));
		when(invoiceListRepository.findByVisitNb(patient1VisitNb, null, null, null, null)).thenReturn(visitInvoiceList());
		
		when(groupnameRepository.findByPid(patient1Id)).thenReturn(groupNameList());
		when(labDataRepository.findByPidAndGroupName(patient1Id, patient1LabGroupName)).thenReturn(labDataList());	
		
		List<Diagnosis> diagnosisList = new ArrayList<>();
		diagnosisList.add(new Diagnosis((long)1, "code", 1, "type2", "description", "language"));
		//when(diagnosisListRepository.findByVisitNb(patient1VisitNb, 10)).thenReturn(diagnosisList);
		
		List<Treatment> treatmentList = new ArrayList<>();
		treatmentList.add(new Treatment(1, "code", 1, "type", "description", "language"));
		//when(treatmentListRepository.findByVisitNb(patient1VisitNb, 10)).thenReturn(treatmentList);
		when(treatmentListRepository.findByVisitNb(patient1VisitNb)).thenReturn(treatmentList);
		
		when(guarantorListRepository.findByVisitNb(patient1VisitNb, "orderBy", "sortorder", 10, 0)).thenReturn(visitGuarantorList());
		
		List<VisitPolicyList> policyList = new ArrayList<>();
		policyList.add(new VisitPolicyList(1, 1, "name", "code", 1, 1, "hospclass"));
		when(policyListRepository.findByVisitNb(patient1VisitNb, "orderBy", "sortorder", 10, 0)).thenReturn(policyList);
	}

	private ObjectNode payload(String keyToChange, Object newValue) throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode payload = (ObjectNode) mapper.readTree(jwtPayload);

		if(keyToChange != null) {
			if(newValue instanceof JsonNode) {
				payload.set(keyToChange, (JsonNode) newValue);
			}			
			if(newValue instanceof String) {
				payload.put(keyToChange, (String) newValue);
			}
			if(newValue instanceof Long) {
				payload.put(keyToChange, (Long) newValue);
			}
		}
		
		return payload;
	}

	private ObjectNode header(String keyToChange, Object newValue) throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode header = (ObjectNode) mapper.readTree(jwtHeader);
		
		if(keyToChange != null) {
			if(newValue instanceof JsonNode) {
				header.set(keyToChange, (JsonNode) newValue);
			}			
			if(newValue instanceof String) {
				header.put(keyToChange, (String) newValue);
			}
			if(newValue instanceof Long) {
				header.put(keyToChange, (Long) newValue);
			}
		}
		
		return header;
	}
	
	@Override
	public Logger getLogger() {
		return null;
	}

	private Map<String, Object> patientSearchCriteria() {
		Map<String, Object> criteria = new HashMap<>();
		
		criteria.put("patient_id", patient1Id);
		criteria.put("lastname", patient1LastName);
		criteria.put("firstname", patient1FirstName);
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

	
	private List<Groupname> groupNameList() {
		List<Groupname> list = new ArrayList<>();
		
		Groupname groupname1 = new Groupname(patient1LabGroupName);
		Groupname groupname2 = new Groupname("OtherGroupName");
		Groupname groupname3 = new Groupname("AnotherGroupName");
		
		list.add(groupname1);
		list.add(groupname2);
		list.add(groupname3);
		
		return list;
	}

	private List<LabData> labDataList() {
		List<LabData> list = new ArrayList<>();
		
		LabData labData1 = new LabData(patient1Id, patient1LabGroupName, Timestamp.valueOf(patient1VisitAdmitDateStr), "Analysis 1", "Value 1",
				"STR", "Unit 1", "Ref range", "abnormalflag", "abnormalflagdesc", "resultstatus", "resultstatusdesc", new ObjectMapper().createObjectNode());
		LabData labData2 = new LabData(patient1Id, patient1LabGroupName, Timestamp.valueOf(patient1VisitAdmitDateStr), "Analysis 1", "Value 2",
				"STR", "Unit 1", "Ref range", "abnormalflag", "abnormalflagdesc", "resultstatus", "resultstatusdesc", new ObjectMapper().createObjectNode());
		LabData labData3 = new LabData(patient1Id, patient1LabGroupName, Timestamp.valueOf(patient1VisitAdmitDateStr), "Analysis 2", "Value",
				"STR", "Unit 1", "Ref range", "abnormalflag", "abnormalflagdesc", "resultstatus", "resultstatusdesc", new ObjectMapper().createObjectNode());
		
		list.add(labData1);
		list.add(labData2);
		list.add(labData3);
		
		return list;
	}
	
	private List<GuarantorList> visitGuarantorList() {
		List<GuarantorList> list = new ArrayList<>();

		GuarantorList guarantor1 = new GuarantorList(3, 1, "Guarantor-1", "code", "address", "address2", "locality", "postcode", "canton", "country", "complement", null, null, false);
		GuarantorList guarantor2 = new GuarantorList(3, 2, "Guarantor-2", "code", "address", "address2", "locality", "postcode", "canton", "country", "complement", null, null, false);
		GuarantorList guarantor3 = new GuarantorList(3, 3, "Guarantor-3", "code", "address", "address2", "locality", "postcode", "canton", "country", "complement", null, null, false);

		list.add(guarantor1);
		list.add(guarantor2);
		list.add(guarantor3);

		return list;
	}
	
	private List<InvoiceList> visitInvoiceList() {
		List<InvoiceList> list = new ArrayList<>();
		
		InvoiceList firstInvoice = new InvoiceList(2, patient1InvoiceId, Timestamp.valueOf(patient1VisitDischargeDateStr), 5000.50, 1500, 1L);
		InvoiceList secondInvoice = new InvoiceList(2, patient1InvoiceId+1, Timestamp.valueOf(patient1VisitDischargeDateStr), 1000, 0, 1L);
		InvoiceList thirdInvoice = new InvoiceList(2, patient1InvoiceId+2, Timestamp.valueOf(patient1VisitDischargeDateStr), 1000, 0, 1L);
		
		list.add(firstInvoice);
		list.add(secondInvoice);
		list.add(thirdInvoice);
		
		return list;
	}

	
	@Test
	public void checkTokenValid() throws Exception{
		JsonNode header = header(null, null);
		JsonNode payload = payload(null, null);
		String jwt = JWTUtils.buildToken(signingKey, header, payload);
		
		testEndPointCalls(jwt, MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	public void checkPidNotMatching() throws Exception {
		JsonNode header = header(null, null);
		JsonNode payload = payload("pid", patient1WrongPid);
		String jwt = JWTUtils.buildToken(signingKey, header, payload);
		
		testEndPointCalls(jwt, MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	public void checkFirstNameNotMatching() throws Exception {
		JsonNode header = header(null, null);
		JsonNode payload = payload("firstName", patient1WrongFirstname);
		String jwt = JWTUtils.buildToken(signingKey, header, payload);
		
		testEndPointCalls(jwt, MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	public void checkLastNameNotMatching() throws Exception {
		JsonNode header = header(null, null);
		JsonNode payload = payload("lastName", patient1WrongLastname);
		String jwt = JWTUtils.buildToken(signingKey, header, payload);
		
		testEndPointCalls(jwt, MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	public void checkBirthdateNotMatching() throws Exception {
		JsonNode header = header(null, null);
		JsonNode payload = payload("birthDate", patient1WrongBirthdate);
		String jwt = JWTUtils.buildToken(signingKey, header, payload);
		
		testEndPointCalls(jwt, MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	public void checkSexNotMatching() throws Exception {
//		JsonNode header = header(null, null);
//		JsonNode payload = payload("sex", patient1WrongSex);
//		String jwt = JWTUtils.buildToken(signingKey, header, payload);
//
//		testEndPointCalls(jwt, MockMvcResultMatchers.status().isForbidden());
	}
	
	@Test
	public void checkInvalidJwt() throws Exception{
		JsonNode header = header("alg", "NOT_AN_ALGO");
		JsonNode payload = payload(null, null);
		String encodedHeader = Base64Utils.encode(header.toString());
		String encodedPayload = Base64Utils.encode(payload.toString());
		String signature = new String(PKIUtils.signData((header.toString() + "." + payload.toString()).getBytes(), signingKey));
		
		String jwt = encodedHeader + "." + encodedPayload + "." + Base64Utils.encode(signature);
		testEndPointCalls(jwt, MockMvcResultMatchers.status().isUnauthorized());

		jwt = "IASUdsfdsf90324kjdsfldsf.akl34FDSFPOadsofsfdsfds.0000000000000000000000000000000000000";
		testEndPointCalls(jwt, MockMvcResultMatchers.status().isUnauthorized());
	}
	
	/**
	 * 
	 * @param jwt
	 * @param resultMatcher
	 * @throws Exception
	 */
	private void testEndPointCalls(String jwt, ResultMatcher resultMatcher) throws Exception {
		baseUrl = ((Boolean.valueOf(sslEnabled)) ? "https" : "http") + "://localhost:" + serverPort; 
		
		// Read patient detail
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + patientDetailResourcePath, patient1Id)
				.header("Authorization", "Bearer " + jwt))
				.andExpect(resultMatcher);

		// Read patient visits
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + patientVisitsResourcePath).param("pid", Long.toString(patient1Id))
				.header("Authorization", "Bearer " + jwt))
		.andExpect(resultMatcher);

		// Read patient lab group names
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + patientLabGroupnamesResourcePath, patient1Id)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(resultMatcher);

//		// Read patient lab results
//		mockMvc.perform(MockMvcRequestBuilders.get(patientLabResultsResourcePath, patient1Id)
//				.header("Authorization", "Bearer " + jwt))
//		.andExpect(resultMatcher);

		// Read patient readiology exams
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + patientRadiologyResourcePath, patient1Id)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(resultMatcher);

		// Read visit invoices
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + visitInvoicesResourcePath).param("visitnb", Long.toString(patient1VisitNb))
				.header("Authorization", "Bearer " + jwt))
		.andExpect(resultMatcher);

		// Read invoice detail
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + invoiceDetailResourcePath, patient1InvoiceId)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(resultMatcher);

		// Read visit detail
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + visitDetailResourcePath, patient1VisitNb)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(resultMatcher);

		// Read visit detail
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + visitDiagnosisResourcePath, patient1VisitNb)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(resultMatcher);

		// Read visit detail
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + visitTreatmentsResourcePath, patient1VisitNb)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(resultMatcher);

		// Read visit detail
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + visitGuarantorsResourcePath, patient1VisitNb)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(resultMatcher);

		// Read visit detail
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + visitPoliciesResourcePath, patient1VisitNb)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(resultMatcher);

		// Read policy detail
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + visitPolicyDetailResourcePath, patient1VisitNb, patient1GuarantorId, patient1Priority, patient1Subpriority)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(resultMatcher);

		//read nok list
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + patientNokResourcePath, patient1Id)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(resultMatcher);

		//read nok contact list
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + patientNokContactsResourcePath, patient1Id, patient1nokId)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(resultMatcher);

		// read physician list
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + visitDoctorsResourcePath, patient1VisitNb)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(resultMatcher);

		// read guarantor detail
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + visitGuarantorDetailResourcePath, patient1VisitNb, patient1GuarantorId)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(resultMatcher);
	}
	
	@Test
	public void checkForbiddenEndpoints() throws Exception {
		JsonNode header = header(null, null);
		JsonNode payload = payload(null, null);
		String jwt = JWTUtils.buildToken(signingKey, header, payload);
		
		baseUrl = ((Boolean.valueOf(sslEnabled)) ? "https" : "http") + "://localhost:" + serverPort;
		
		// read company list
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + companyListResourcePath)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(MockMvcResultMatchers.status().isForbidden());

		// read company detail
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + companyDetailResourcePath, Integer.toString(1))
				.header("Authorization", "Bearer " + jwt))
		.andExpect(MockMvcResultMatchers.status().isForbidden());

		// read guarantor detail
		mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + guarantorDetailResourcePath, patient1GuarantorId)
				.header("Authorization", "Bearer " + jwt))
		.andExpect(MockMvcResultMatchers.status().isForbidden());
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
