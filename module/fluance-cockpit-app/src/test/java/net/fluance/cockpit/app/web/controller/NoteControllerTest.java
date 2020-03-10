package net.fluance.cockpit.app.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import net.fluance.app.data.model.identity.AccessControl;
import net.fluance.app.data.model.identity.EhProfile;
import net.fluance.app.data.model.identity.UserProfile;
import net.fluance.app.test.AbstractWebIntegrationTest;
import net.fluance.app.web.util.swagger.SwaggerSpecUtils;
import net.fluance.cockpit.Application;
import net.fluance.cockpit.app.service.domain.NotesService;
import net.fluance.cockpit.app.service.security.NotesRolesCategoriesService;
import net.fluance.cockpit.core.model.PatientReference;
import net.fluance.cockpit.core.model.jdbc.note.NoteDetails;
import net.fluance.cockpit.core.model.jdbc.note.NoteCategory;
import net.fluance.cockpit.core.repository.jdbc.note.NoteDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.note.roleCategory.NoteCategoriesDao;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class NoteControllerTest extends AbstractWebIntegrationTest {

	private static Logger LOGGER = LogManager.getLogger(NoteControllerTest.class);

	private Swagger swaggerYamlController;
	private String url;
	private String pathNotesList;

	private String pathNotesDetail ;
	private String pathSpecsNotesDetail ;


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
		public NoteDetailRepository noteRepository() {
			return Mockito.mock(NoteDetailRepository.class);
		}
	}

	@Autowired
	NoteDetailRepository noteRepository;

	@Mock
	private NotesService notesService;

	@Autowired
	private NotesRolesCategoriesService notesRolesCategoriesService;
	
	@Autowired
	private NoteCategoriesDao noteCategoriesDao;
	
	@Mock
	private AccessControl accessControl = Mockito.mock(AccessControl.class);
	@Mock
	private EhProfile userProfile = Mockito.mock(EhProfile.class);
	@Mock
	private UserProfile user = Mockito.mock(UserProfile.class);
	private Map<String, List<Integer>> rolesCategories;
	private static final String MAIN_ROLE = "nurse";

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
		pathNotesList = "/notes";

		pathNotesDetail = "/notes/1";
		pathSpecsNotesDetail = "/notes/{noteId}";
	
		okUserToken = getAccessToken(companyUserUsername, domain);
		notAllowedToken = getAccessToken(companyNotAllowedUserUsername, domain);

		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

		when(user.getProfile()).thenReturn(userProfile);
		when(userProfile.getGrants()).thenReturn(accessControl);
		when(accessControl.getRoles()).thenReturn(Arrays.asList(new String[]{"nurse"}));
	}

	@Test
	//@Ignore("Test was commentet out, the method of finding a patient has changed")
	public void testNoteListControllerOk() throws Exception {
		List<NoteDetails> notes = new ArrayList<>();
		notes.add(new NoteDetails(2L, "title", "description", "editor", "creator", false, new Date(), new Date(),
				false, new NoteCategory(34, "name"), new PatientReference((long) 234, "firstName", "lastName", "maidenName", new Date()), 0L));
		rolesCategories = new HashMap<>();
		rolesCategories.put(MAIN_ROLE, Arrays.asList(notes.get(0).getCategory().getId()));
		when(noteCategoriesDao.getAllRoleCategories()).thenReturn(rolesCategories);
		//when(noteRepository.findByPatientId(anyLong(), "creator", false, anyList(), anyInt(), anyInt())).thenReturn(notes);

		CloseableHttpResponse response = sendRequest(url + pathNotesList+ "?pid="+1, okUserToken);
		String jsonResponse = EntityUtils.toString(response.getEntity());
		LOGGER.info("Response.Body = " + jsonResponse);

		assertEquals(HttpStatus.OK.value(), response.getStatusLine().getStatusCode());

		assertTrue(SwaggerSpecUtils.areHeadersValid(response, swaggerYamlController, pathNotesList, HttpStatus.OK));

		String textSchema = SwaggerSpecUtils.getOperationResponseSchemaText(url + swaggerGeneratedPath, pathNotesList, HttpMethod.GET, HttpStatus.OK);
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
