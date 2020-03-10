package net.fluance.cockpit.app.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import io.swagger.models.Swagger;
import net.fluance.app.test.AbstractWebIntegrationTest;
import net.fluance.app.web.util.swagger.SwaggerSpecUtils;
import net.fluance.cockpit.Application;
import net.fluance.cockpit.core.model.jdbc.invoice.Invoice;
import net.fluance.cockpit.core.model.jdbc.invoice.InvoiceList;
import net.fluance.cockpit.core.repository.jdbc.invoice.InvoiceListRepository;
import net.fluance.cockpit.core.repository.jdbc.invoice.InvoiceRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ComponentScan("net.fluance.cockpit.app")
@TestPropertySource(locations = "classpath:test.properties")
public class InvoiceControllerTest extends AbstractWebIntegrationTest {

	private static Logger LOGGER = LogManager.getLogger(InvoiceControllerTest.class);
	private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final String SWAGGER_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";
	private static final DateFormat SWAGGER_DATE_FORMAT = new SimpleDateFormat(SWAGGER_DATETIME_PATTERN);

	private Swagger swagger;
	private String url;
	private String pathInvoiceDetail;
	private String pathSpecsInvoiceDetail;
	private String pathInvoiceList;
	private String pathSpecsInvoiceList;

	@Value("${test.swagger.generated}")
	private String swaggerGeneratedPath;
	
	@Configuration
	public static class TestConfig {

		@Bean
		@Primary
		public InvoiceListRepository invoiceListRepository() {
			return Mockito.mock(InvoiceListRepository.class);
		}
		
		@Bean
		@Primary
		public InvoiceRepository invoiceRepository() {
			return Mockito.mock(InvoiceRepository.class);
		}
	}

	@Autowired
	protected WebApplicationContext wac;

	@Autowired
	InvoiceListRepository invoiceListRepository;

	@Autowired
	InvoiceRepository invoiceRepository;

	@Value("${test.host.local}")
	private String host;
	@Value("${is.sp.auth}")
	private String isServiceProviderAuth;
	@Value("${net.fluance.cockpit.app.web.visit.policyDetailUserUsername}")
	private String policyDetailUserUsername;
	@Value("${net.fluance.cockpit.app.web.visit.policyDetailUserPassword}")
	private String policyDetailUserPassword;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserUsername}")
	private String policyDetailNotAllowedUserUsername;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserPassword}")
	private String policyDetailNotAllowedUserPassword;
	@Value("${oauth2.token.url}")
	private String oAuth2TokenUrl;

	@Value("${authorized.username}")
	private String authorizedUsername;
	@Value("${unauthorized.username}")
	private String unauthorizedUsername;

	@Value("${net.fluance.cockpit.core.model.repository.invoice.id.first}")
	private long invoiceIdFirst;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.name.first}")
	private String invoiceNameFirst;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.invdt.first}")
	private String invoiceDateFirst;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.code.first}")
	private String invoiceCodeFirst;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.visitnb.first}")
	private long invoiceVisitNbFirst;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.total.first}")
	private Double invoiceTotalFirst;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.nb_records.first}")
	private int invoiceListNbRecordsFirst;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.balance.first}")
	private Double invoiceBalanceFirst;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.guarantor_id.first}")
	private long invoiceGuarantorIdFirst;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.apdrg_code.first}")
	private String invoiceApdrgCodeFirst;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.apdrg_desc.first}")
	private String invoiceApdrgDescFirst;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.mdc_code.first}")
	private String invoiceMdcCodeFirst;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.mdc_desc.first}")
	private String invoiceMdcDescFirst;

	@Value("${net.fluance.cockpit.core.model.repository.invoice.id.second}")
	private long invoiceIdSecond;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.name.second}")
	private String invoiceNameSecond;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.invdt.second}")
	private String invoiceDateSecond;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.code.second}")
	private String invoiceCodeSecond;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.visitnb.second}")
	private long invoiceVisitNbSecond;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.total.second}")
	private Double invoiceTotalSecond;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.nb_records.second}")
	private int invoiceListNbRecordsSecond;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.balance.second}")
	private Double invoiceBalanceSecond;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.guarantor_id.second}")
	private long invoiceGuarantorIdSecond;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.apdrg_code.second}")
	private String invoiceApdrgCodeSecond;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.apdrg_desc.second}")
	private String invoiceApdrgDescSecond;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.mdc_code.second}")
	private String invoiceMdcCodeSecond;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.mdc_desc.second}")
	private String invoiceMdcDescSecond;

	@Value("${net.fluance.cockpit.core.model.repository.invoice.id.third}")
	private long invoiceIdThird;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.name.third}")
	private String invoiceNameThird;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.invdt.third}")
	private String invoiceDateThird;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.code.third}")
	private String invoiceCodeThird;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.visitnb.first}")
	private long invoiceVisitNbThird;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.total.third}")
	private Double invoiceTotalThird;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.nb_records.third}")
	private int invoiceListNbRecordsThird;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.balance.third}")
	private Double invoiceBalanceThird;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.guarantor_id.third}")
	private long invoiceGuarantorIdThird;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.apdrg_code.third}")
	private String invoiceApdrgCodeThird;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.apdrg_desc.third}")
	private String invoiceApdrgDescThird;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.mdc_code.third}")
	private String invoiceMdcCodeThird;
	@Value("${net.fluance.cockpit.core.model.repository.invoice.mdc_desc.third}")
	private String invoiceMdcDescThird;

	private String domain = "PRIMARY";
	private String authorizedUserAccessToken;
	private String unauthorizedUserAccessToken;
	
	@Value("${net.fluance.cockpit.app.web.company.userUsername}")
	private String allowedUsername;
	@Value("${net.fluance.cockpit.app.web.company.userPassword}")
	private String allowedPassword;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserUsername}")
	private String notAllowedUsername;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserPassword}")
	private String notAllowedPassword;
    
	@SuppressWarnings("unchecked")
	@Override
	public void setUp() throws Exception {
		url = host + serverPort;
		swagger = SwaggerSpecUtils.load(url + swaggerGeneratedPath);
		baseUrl = "http://localhost:" + serverPort;
		authBaseUrl = "http://localhost:" + serverPort;
		pathInvoiceDetail = "invoices/1";
		pathInvoiceList = "invoices";
		pathSpecsInvoiceList = "/invoices";
		pathSpecsInvoiceDetail = "/invoices/{invoiceid}";

		pathInvoiceDetail = "invoices/1";
		pathInvoiceList = "invoices";
		pathSpecsInvoiceList = "/invoices";
		
		pathSpecsInvoiceDetail = "/invoices/{invoiceid}";

		authorizedUserAccessToken = getAccessToken(authorizedUsername, domain);
		unauthorizedUserAccessToken = getAccessToken(unauthorizedUsername, domain);

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

		Date invoiceDate = DEFAULT_DATE_FORMAT.parse(invoiceDateFirst);
		Invoice firstInvoice = new Invoice(invoiceIdFirst, invoiceDate, invoiceTotalFirst, invoiceBalanceFirst,
				invoiceApdrgCodeFirst, invoiceApdrgDescFirst, invoiceMdcCodeFirst, invoiceMdcDescFirst,
				invoiceNameFirst, invoiceCodeFirst, invoiceVisitNbFirst, invoiceGuarantorIdFirst);
		List<InvoiceList> visitInvoiceList = visitInvoiceList();
		List<InvoiceList> visitAndGuarantorInvoiceList = visitAndGuarantorInvoiceList();
		doReturn(visitInvoiceList).when(invoiceListRepository).findByVisitNb((long) 1, null, null, null, null);
		doReturn(visitAndGuarantorInvoiceList).when(invoiceListRepository).findByVisitNbAndGuarantorId((long)1, invoiceGuarantorIdFirst, null,
				null, null, null);
		when(invoiceRepository.findOne((long) 1)).thenReturn(firstInvoice);
		when(invoiceRepository.findOne(invoiceIdFirst)).thenReturn(firstInvoice);

	}
	
	@Test
	public void testInvoiceDetailControllerOk() throws Exception {
		
		String detailUrl = url +"/" +  pathInvoiceDetail;
		CloseableHttpResponse response = sendRequest(detailUrl, authorizedUserAccessToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);
		
		// test status code
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		// test headers
		assertTrue(
				SwaggerSpecUtils.areHeadersValid(response, swagger, pathSpecsInvoiceDetail, HttpStatus.OK));

		// test schema
		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath,
				pathSpecsInvoiceDetail, HttpMethod.GET, HttpStatus.OK);
		// OPTIONAL : test attributes - test already done in repository's unit
		// test
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(DEFAULT_DATE_FORMAT);
		Invoice invoice = objectMapper.readValue(jsonResponse, Invoice.class);
		
		String swaggerDate = SWAGGER_DATE_FORMAT.format(invoice.getInvdt());
		
		JsonNode swaggerInvoiceNode = objectMapper.valueToTree(invoice);
		((ObjectNode) swaggerInvoiceNode).put("date", swaggerDate);
		
		String swaggerInvoiceJson = objectMapper.writeValueAsString(swaggerInvoiceNode);

		assertTrue(isJsonSchemaCompliant(textSchema, swaggerInvoiceJson));

		assertNotNull(invoice);
		assertTrue(invoiceIdFirst == invoice.getId());
		assertEquals(invoiceNameFirst, invoice.getGuarantor_name());
		assertEquals(invoiceDateFirst, DEFAULT_DATE_FORMAT.format(invoice.getInvdt()));
		assertEquals(invoiceCodeFirst, invoice.getGuarantor_code());
		assertEquals(invoiceTotalFirst, invoice.getTotal());
	}

	@Test
	public void testInvoiceListByVisitNbControllerOk() throws Exception {
		String listUrl = url + swagger.getBasePath() + pathInvoiceList + "?visitnb=" + invoiceVisitNbFirst;
		CloseableHttpResponse response = sendRequest(listUrl, authorizedUserAccessToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		// test status code
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		// test headers
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swagger, pathSpecsInvoiceList, HttpStatus.OK));

		// test schema
		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsInvoiceList,
				HttpMethod.GET, HttpStatus.OK);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(DEFAULT_DATE_FORMAT);
		List<InvoiceList> invoices = objectMapper.readValue(jsonResponse,
		objectMapper.getTypeFactory().constructCollectionType(List.class, InvoiceList.class));
		
		JsonNode listNode = objectMapper.valueToTree(invoices);
		
		int invoicesCount = listNode.size();
		for (int i = 0; i < invoicesCount; i++) {
			JsonNode invoiceNode = listNode.get(i);
			String currentDate = invoiceNode.get("date").asText();
			String swaggerDate = SWAGGER_DATE_FORMAT.format(DEFAULT_DATE_FORMAT.parse(currentDate));
			((ObjectNode) invoiceNode).put("date", swaggerDate);
		}
		
		String swaggerInvoicesJson = objectMapper.writeValueAsString(listNode);
		
		assertTrue(isJsonSchemaCompliant(textSchema, swaggerInvoicesJson));

		// OPTIONAL : test attributes - test already done in repository's unit
		// test
		assertNotNull(invoices);
//		assertEquals(3, invoices.size());
//		assertTrue(invoiceIdFirst == invoices.get(0).getId());
//		assertEquals(invoiceListNbRecordsFirst, invoices.get(0).getNb_records());
//		assertEquals(invoiceDateFirst, DEFAULT_DATE_FORMAT.format(invoices.get(0).getInvdt()));
//		assertTrue(invoiceTotalFirst == invoices.get(0).getTotal());
//		assertTrue(invoiceBalanceFirst == invoices.get(0).getBalance());
	}

	@Test
	public void testInvoiceListByVisitNbAndGuarantorControllerOk() throws Exception {
		CloseableHttpResponse response = sendRequest(url + swagger.getBasePath() + pathInvoiceList + "?visitnb="
				+ invoiceVisitNbFirst + "&guarantorid=" + invoiceGuarantorIdFirst, authorizedUserAccessToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		// test status code
		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		// test headers
		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swagger, pathSpecsInvoiceList, HttpStatus.OK));

		// test schema
		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathSpecsInvoiceList,
				HttpMethod.GET, HttpStatus.OK);
		
		// OPTIONAL : test attributes - test already done in repository's unit
		// test
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(DEFAULT_DATE_FORMAT);
		List<InvoiceList> invoices = objectMapper.readValue(jsonResponse,
				objectMapper.getTypeFactory().constructCollectionType(List.class, InvoiceList.class));

		JsonNode listNode = objectMapper.valueToTree(invoices);
		
		int invoicesCount = listNode.size();
		for (int i = 0; i < invoicesCount; i++) {
			JsonNode invoiceNode = listNode.get(i);
			String currentDate = invoiceNode.get("date").asText();
			String swaggerDate = SWAGGER_DATE_FORMAT.format(DEFAULT_DATE_FORMAT.parse(currentDate));
			((ObjectNode) invoiceNode).put("date", swaggerDate);
		}
		
		String swaggerInvoicesJson = objectMapper.writeValueAsString(listNode);
		
		assertTrue(isJsonSchemaCompliant(textSchema, swaggerInvoicesJson));

		assertNotNull(invoices);
//		assertEquals(2, invoices.size());
//		assertTrue(invoiceIdFirst == invoices.get(0).getId());
//		assertEquals(invoiceListNbRecordsFirst, invoices.get(0).getNb_records());
//		assertEquals(invoiceDateFirst, DEFAULT_DATE_FORMAT.format(invoices.get(0).getInvdt()));
//		assertTrue(invoiceTotalFirst == invoices.get(0).getTotal());
//		assertTrue(invoiceBalanceFirst == invoices.get(0).getBalance());
	}

	@Test
	public void testInvoiceDetailControllerNotFound() throws Exception {
		CloseableHttpResponse response = sendRequest(url + swagger.getBasePath() + pathInvoiceDetail + "/" + 99999,
				authorizedUserAccessToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		// test status code
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusLine().getStatusCode());
	}

	@Test
	public void testInvoiceDetailControllerUnAuthorized() throws Exception {
		// No access token
		CloseableHttpResponse response = sendRequest(
				url + swagger.getBasePath() + pathInvoiceDetail + "/" + invoiceIdFirst, "");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());

		// INVALID ACCES_TOKEN
		response = sendRequest(url + swagger.getBasePath() + pathInvoiceDetail + "/" + invoiceIdFirst,
				"FAKE_ACCESS_TOKEN");
		assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusLine().getStatusCode());
	}

	@Test
	public void testInvoiceDetailControllerForbidden() throws Exception {
		CloseableHttpResponse response = sendRequest(
				url + swagger.getBasePath() + pathInvoiceDetail + "/" + invoiceIdFirst, unauthorizedUserAccessToken);
		assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusLine().getStatusCode());
	}

	@Override
	public void tearDown() {
		swagger = null;
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

	private List<InvoiceList> visitInvoiceList() {
		List<InvoiceList> list = new ArrayList<>();

		InvoiceList firstInvoice = new InvoiceList(invoiceListNbRecordsFirst, invoiceIdFirst,
				Timestamp.valueOf(invoiceDateFirst), invoiceTotalFirst, invoiceBalanceFirst, invoiceGuarantorIdFirst);
		InvoiceList secondInvoice = new InvoiceList(invoiceListNbRecordsSecond, invoiceIdSecond,
				Timestamp.valueOf(invoiceDateSecond), invoiceTotalSecond, invoiceBalanceSecond,
				invoiceGuarantorIdSecond);
		InvoiceList thirdInvoice = new InvoiceList(invoiceListNbRecordsThird, invoiceIdThird,
				Timestamp.valueOf(invoiceDateThird), invoiceTotalThird, invoiceBalanceThird, invoiceGuarantorIdThird);

		list.add(firstInvoice);
		list.add(secondInvoice);
		list.add(thirdInvoice);

		return list;
	}

	private List<InvoiceList> visitAndGuarantorInvoiceList() {
		List<InvoiceList> list = new ArrayList<>();

		InvoiceList firstInvoice = new InvoiceList(invoiceListNbRecordsFirst, invoiceIdFirst,
				Timestamp.valueOf(invoiceDateFirst), invoiceTotalFirst, invoiceBalanceFirst, invoiceGuarantorIdFirst);
		InvoiceList secondInvoice = new InvoiceList(invoiceListNbRecordsSecond, invoiceIdSecond,
				Timestamp.valueOf(invoiceDateSecond), invoiceTotalSecond, invoiceBalanceSecond,
				invoiceGuarantorIdSecond);

		list.add(firstInvoice);
		list.add(secondInvoice);

		return list;
	}
}
