package net.fluance.cockpit.app.service;

import static org.junit.Assert.assertNotNull;
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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.HttpException;
import org.apache.http.ParseException;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.context.WebApplicationContext;
import org.xml.sax.SAXException;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import net.fluance.app.data.model.identity.AccessControl;
import net.fluance.app.data.model.identity.EhProfile;
import net.fluance.app.data.model.identity.UserProfile;
import net.fluance.app.test.AbstractWebIntegrationTest;
import net.fluance.cockpit.app.Application;
import net.fluance.cockpit.app.service.domain.NotesService;
import net.fluance.cockpit.app.service.domain.PatientService;
import net.fluance.cockpit.app.service.security.NotesRolesCategoriesService;
import net.fluance.cockpit.core.model.PatientReference;
import net.fluance.cockpit.core.model.jdbc.note.NoteCategory;
import net.fluance.cockpit.core.model.jdbc.note.NoteDetails;
import net.fluance.cockpit.core.repository.jdbc.note.NoteDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.note.roleCategory.NoteCategoriesDao;

@ComponentScan(basePackages={"net.fluance.cockpit.core", "net.fluance.cockpit.app"})
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class NoteServiceTest extends AbstractWebIntegrationTest {

	@Autowired
	private PatientService patientService;

	@Autowired
	protected WebApplicationContext wac;

	
	@Configuration
	public static class TestConfig {
		@Bean
		public NotesRolesCategoriesService notesRolesCategoriesService() {
			return Mockito.mock(NotesRolesCategoriesService.class);
		}

		@Bean
		public NoteCategoriesDao noteCategoriesDao() {
			return Mockito.mock(NoteCategoriesDao.class);
		}
	}
	@Autowired
	NoteDetailRepository noteRepository;

	@Autowired
	private NotesService noteService;
	
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
	
	@Before
	public void setUp() throws Exception {
		when(user.getProfile()).thenReturn(userProfile);
		when(userProfile.getGrants()).thenReturn(accessControl);
		when(accessControl.getRoles()).thenReturn(Arrays.asList(new String[]{"nurse"}));
		rolesCategories = new HashMap<>();
		rolesCategories.put(MAIN_ROLE, Arrays.asList(new Integer[]{Integer.valueOf(0)}));
		when(noteCategoriesDao.getAllRoleCategories()).thenReturn(rolesCategories);
	}

	@Test
	@Ignore("Test was commented")
	public void noteDetailTest() {
		NoteDetails note = new NoteDetails(2L, "title", "description", "editor", "creator", false, new Date(), new Date(),
				false, new NoteCategory(34, "name"), new PatientReference((long) 234, "firstName", "lastName", "maidenName", new Date()), 0L);
		
		when(noteRepository.findOne(anyLong())).thenReturn(note);
		when(noteRepository.findByNoteId(anyLong())).thenReturn(note);
		when(notesRolesCategoriesService.hasAccessToCategory(user, note.getCategory().getId())).thenReturn(true);
		when(notesRolesCategoriesService.hasCategory(MAIN_ROLE, note.getCategory().getId())).thenReturn(true);
		NoteDetails noteDetail = noteService.getNoteDetail(note.getId(), user); 
		assertNotNull(noteDetail);
		
	}
	
	@Test
	public void emptyTest(){}

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
	public Logger getLogger() {
		return null;
	}	

}
