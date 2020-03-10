package net.fluance.cockpit.app.service.domain;

import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javassist.NotFoundException;
import net.fluance.app.data.model.identity.User;
import net.fluance.app.security.service.UserProfileLoader;
import net.fluance.app.web.util.exceptions.LockedException;
import net.fluance.app.web.util.exceptions.MustRequestLockException;
import net.fluance.cockpit.app.service.security.NotesRolesCategoriesService;
import net.fluance.cockpit.app.web.util.note.NoteMock;
import net.fluance.cockpit.core.model.jpa.LockStatus;
import net.fluance.cockpit.core.model.jpa.note.Note;
import net.fluance.cockpit.core.repository.jdbc.note.NoteDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.note.NoteHistoryRepository;
import net.fluance.cockpit.core.repository.jpa.note.NoteHistoryDetailRepository;
import net.fluance.cockpit.core.repository.jpa.note.NoteRepository;
import net.fluance.cockpit.core.repository.jpa.note.NoteTrackRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class NotesServiceTest {
	
	private Note fooNote;
	private User fooUser;

	@TestConfiguration
	static class NotesServiceTestConfiguration {
		
		@Bean
		NotesService notesService() {
			return new NotesService();
		}

		@Bean
		PropertyPlaceholderConfigurer propConfig() {
			PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();			
			Properties properties = new Properties();
			
			properties.put("net.fluance.cockpit.core.model.repository.defaultResultLimit", "10");
			properties.put("net.fluance.cockpit.core.model.repository.defaultResultOffset", "0");
			properties.put("net.fluance.cockpit.core.model.repository.defaultResultAppointmentListOrderBy", "ID");
			properties.put("net.fluance.cockpit.core.model.repository.defaultResultNoteDetailsListOrderBy", "ID");
			properties.put("net.fluance.cockpit.core.model.repository.defaultResultNoteDetailsListSortOrder", "DESC");
			
			propertyPlaceholderConfigurer.setProperties(properties);

			propertyPlaceholderConfigurer.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");

			return propertyPlaceholderConfigurer;
		}
	}

	@Before
	public void setUp() {
		Mockito.reset(pictureService);
		Mockito.reset(noteRepository);
		Mockito.reset(repository);
		Mockito.reset(noteTrackRepository);
		Mockito.reset(noteHistoryDetailRepository);
		Mockito.reset(notesRolesCategoriesService);
		Mockito.reset(noteHistoryRepository);
		Mockito.reset(lockService);
		Mockito.reset(profileLoader);
		Mockito.reset(shiftService);
	}
	
	@MockBean
	PictureService pictureService;

	@MockBean
	NoteDetailRepository noteRepository;

	@MockBean
	NoteRepository repository;

	@MockBean
	NoteTrackRepository noteTrackRepository;

	@MockBean
	NoteHistoryDetailRepository noteHistoryDetailRepository;
	
	@MockBean
	NotesRolesCategoriesService notesRolesCategoriesService;

	@MockBean
	NoteHistoryRepository noteHistoryRepository;

	@MockBean
	LockService lockService;

	@MockBean
	UserProfileLoader profileLoader;

	@MockBean
	ShiftService shiftService;
	
	@Autowired
	NotesService notesService;

	@Test
	public void update_should_return_updated_note() throws NotFoundException {
		initializeFoo();
		
		String editor = fooUser.getDomain() +"/"+ fooUser.getUsername();
		
		Mockito.when(noteRepository.findByNoteId(Mockito.anyLong())).thenReturn(NoteMock.generateNoteDetails());		
		Mockito.when(lockService.isLockedByUser(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);		
		Mockito.when(repository.save(fooNote)).thenReturn(fooNote);
		Mockito.when(noteTrackRepository.markNoteAsUnReadForOtherUsers(fooNote.getId(), editor)).thenReturn(1);
		
		Note result = notesService.update(fooNote, fooUser);
		
		assertNotNull("Should return a Note", result);
	}
	
	@Test(expected=LockedException.class)
	public void update_LockedException() throws NotFoundException {
		initializeFoo();
		
		Mockito.when(noteRepository.findByNoteId(Mockito.anyLong())).thenReturn(NoteMock.generateNoteDetails());		
		Mockito.when(lockService.isLockedByUser(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		LockStatus lockStatus = new LockStatus(true);
		Mockito.when(lockService.verify(Mockito.anyLong(), Mockito.anyString())).thenReturn(lockStatus);
		
		notesService.update(fooNote, fooUser);
	}
	
	@Test(expected=MustRequestLockException.class)
	public void update_MustRequestLockException() throws NotFoundException {
		initializeFoo();
		
		Mockito.when(noteRepository.findByNoteId(Mockito.anyLong())).thenReturn(NoteMock.generateNoteDetails());		
		Mockito.when(lockService.isLockedByUser(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		LockStatus lockStatus = new LockStatus(false);
		Mockito.when(lockService.verify(Mockito.anyLong(), Mockito.anyString())).thenReturn(lockStatus);
		
		notesService.update(fooNote, fooUser);
	}
	
	@Test(expected=NotFoundException.class)
	public void update_erro_not_found() throws NotFoundException {
		initializeFoo();
		
		Mockito.when(noteRepository.findByNoteId(Mockito.anyLong())).thenReturn(null);
		
		notesService.update(fooNote, fooUser);
	}
	
	private void initializeFoo() {
		fooUser = new User();
		fooUser.setUsername("foo");
		fooUser.setDomain("ADMIN");
		
		fooNote = NoteMock.generateNote();
	}
	
}
