package net.fluance.cockpit.app.web.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.HttpException;
import org.apache.http.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import net.fluance.cockpit.core.repository.jdbc.accesslog.PatientAccessLogRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ComponentScan("net.fluance.cockpit.app")
@TestPropertySource(locations = "classpath:test.properties")
public class AccessLogControllerTest extends AbstractWebIntegrationTest{

	private static Logger LOGGER = LogManager.getLogger(AccessLogControllerTest.class);

	private Swagger swagger;
	private Swagger swaggerYamlController;
	private String url;
	private String pathAccessLog;
	private String pathAccessLogCount;
	private String pathSwaggerAccessLog;
	private String pathSwaggerAccessLogCount;

	private String domain = "PRIMARY";
	private String authorizedUserAccessToken;
	private String unauthorizedUserAccessToken;

	@Value("${test.host.local}")
	private String host;

	@Value("${test.swagger.generated}")
	private String swaggerGeneratedPath;

	@Value("${authorized.username}")
	private String authorizedUsername;
	@Value("${unauthorized.username}")
	private String unauthorizedUsername;

	@Autowired
	protected WebApplicationContext wac;

	@Autowired
	PatientAccessLogRepository accessLogRepository;

	@Configuration
	public static class TestConfig {

		@Bean
		@Primary
		public PatientAccessLogRepository accessLogRepository() {
			return Mockito.mock(PatientAccessLogRepository.class);
		}
	}

	@Override
	public void setUp() throws Exception {
		url = host + serverPort;
		swaggerYamlController = SwaggerSpecUtils.load(url + swaggerGeneratedPath);
		authBaseUrl = "http://localhost:" + serverPort;
		pathAccessLog = "/logs/patient/{pid}";
		pathAccessLogCount = "/logs/patient/{pid}/count";
		pathSwaggerAccessLog = "/logs/patient/123";
		pathSwaggerAccessLogCount = "/logs/patient/123/count";
		authorizedUserAccessToken = getAccessToken(authorizedUsername, domain);
		unauthorizedUserAccessToken = getAccessToken(unauthorizedUsername, domain);
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
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

	@Override
	public void tearDown() {
	}

}
