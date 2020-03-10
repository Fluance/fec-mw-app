package net.fluance.cockpit.app.service.domain;

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
import net.fluance.app.data.model.identity.UserProfile;
import net.fluance.app.security.service.UserProfileLoader;
import net.fluance.app.web.util.exceptions.LockedException;
import net.fluance.app.web.util.exceptions.MustRequestLockException;
import net.fluance.cockpit.app.service.domain.picture.ThumbnailService;
import net.fluance.cockpit.app.web.util.note.NoteMock;
import net.fluance.cockpit.core.model.jpa.LockStatus;
import net.fluance.cockpit.core.model.jpa.picture.Picture;
import net.fluance.cockpit.core.repository.jdbc.picture.PictureDetailRepository;
import net.fluance.cockpit.core.repository.jpa.note.NoteHistoryDetailRepository;
import net.fluance.cockpit.core.repository.jpa.note.NoteRepository;
import net.fluance.cockpit.core.repository.jpa.note.NoteTrackRepository;
import net.fluance.cockpit.core.repository.jpa.picture.PictureHistoryDetailRepository;
import net.fluance.cockpit.core.repository.jpa.picture.PictureRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class PictureServiceTest {
	
	private UserProfile fooUser;
	Picture fooPicture;

	@TestConfiguration
	static class PictureServiceTestConfiguration {
		
		@Bean
		PictureService pictureService() {
			return new PictureService();
		}

		@Bean
		PropertyPlaceholderConfigurer propConfig() {
			PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();			
			Properties properties = new Properties();
			
			
			properties.put("net.fluance.cockpit.core.model.repository.defaultResultLimit", "10");
			properties.put("net.fluance.cockpit.core.model.repository.defaultResultOffset", "0");
			properties.put("notes.pictures.baseDirectory", "/foo/foo1");
			properties.put("notes.pictures.thumbnail.squareSize", "300");
			
			propertyPlaceholderConfigurer.setProperties(properties);

			propertyPlaceholderConfigurer.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");

			return propertyPlaceholderConfigurer;
		}
	}

	@Before
	public void setUp() {
		Mockito.reset(pictureDetailRepository);		
		Mockito.reset(pictureRepository);
		Mockito.reset(pictureHistoryDetailRepository);
		Mockito.reset(noteRepository);
		Mockito.reset(noteTrackRepository);
		Mockito.reset(noteHistoryDetailRepository);
		Mockito.reset(lockService);
		Mockito.reset(profileLoader);
		Mockito.reset(shiftService);
		Mockito.reset(noteService);
	}
	
	@MockBean
	PictureDetailRepository pictureDetailRepository;
	@MockBean
	NoteTrackRepository noteTrackRepository;
	@MockBean
	PictureRepository pictureRepository;
	@MockBean
	NoteHistoryDetailRepository noteHistoryDetailRepository;
	@MockBean
	PictureHistoryDetailRepository pictureHistoryDetailRepository;
	@MockBean
	NotesService noteService;
	@MockBean
	LockService lockService;
	@MockBean
	NoteRepository noteRepository;
	@MockBean
	UserProfileLoader profileLoader;
	@MockBean
	ShiftService shiftService;
	@MockBean
	ThumbnailService thumbnailService;
	
	@Autowired
	PictureService pictureService;
	

	@Test
	public void setPictureAsDeleted_should_return() throws NotFoundException {
		initializeFoo();
	
		Mockito.when(pictureRepository.findOne(Mockito.anyLong())).thenReturn(fooPicture);
		Mockito.when(noteService.getNoteDetail(Mockito.anyLong(), Mockito.anyObject())).thenReturn(NoteMock.generateNoteDetails());
		Mockito.when(lockService.isLockedByUser(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);		
		Mockito.when(pictureRepository.save(fooPicture)).thenReturn(fooPicture);
		Mockito.when(noteTrackRepository.markNoteAsUnReadForOtherUsers(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		
		pictureService.setPictureAsDeleted(1L, fooUser, true);
	}
	
	@Test(expected=LockedException.class)
	public void setPictureAsDeleted_LockedException() throws NotFoundException {
		initializeFoo();
	
		Mockito.when(pictureRepository.findOne(Mockito.anyLong())).thenReturn(fooPicture);		
		Mockito.when(noteService.getNoteDetail(Mockito.anyLong(), Mockito.anyObject())).thenReturn(NoteMock.generateNoteDetails());
		Mockito.when(lockService.isLockedByUser(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		LockStatus lockStatus = new LockStatus(true);
		Mockito.when(lockService.verify(Mockito.anyLong(), Mockito.anyString())).thenReturn(lockStatus);
		
		pictureService.setPictureAsDeleted(1L, fooUser, true);
	}
	
	@Test(expected=MustRequestLockException.class)
	public void setPictureAsDeleted_MustRequestLockException() throws NotFoundException {
		initializeFoo();
	
		Mockito.when(pictureRepository.findOne(Mockito.anyLong())).thenReturn(fooPicture);		
		Mockito.when(noteService.getNoteDetail(Mockito.anyLong(), Mockito.anyObject())).thenReturn(NoteMock.generateNoteDetails());
		Mockito.when(lockService.isLockedByUser(Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		LockStatus lockStatus = new LockStatus(false);
		Mockito.when(lockService.verify(Mockito.anyLong(), Mockito.anyString())).thenReturn(lockStatus);
		
		pictureService.setPictureAsDeleted(1L, fooUser, true);
	}
	
	@Test
	public void createPicture_should_return() throws NotFoundException {
		initializeFoo();
	
		Mockito.when(pictureRepository.save(fooPicture)).thenReturn(fooPicture);
		Mockito.when(noteTrackRepository.markNoteAsUnReadForOtherUsers(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		
		pictureService.createPicture(fooPicture, fooUser);
	}
	
	@Test
	public void updatePicture_should_return() throws NotFoundException {
		initializeFoo();
	
		Mockito.when(pictureRepository.findOne(Mockito.anyLong())).thenReturn(fooPicture);
		Mockito.when(pictureRepository.save(fooPicture)).thenReturn(fooPicture);
		Mockito.when(noteTrackRepository.markNoteAsUnReadForOtherUsers(Mockito.anyLong(), Mockito.anyString())).thenReturn(1);
		
		pictureService.updatePicture(fooPicture, fooUser);
	}
	
	@Test(expected=NotFoundException.class)
	public void updatePicture_NotFoundException() throws NotFoundException {
		initializeFoo();
	
		Mockito.when(pictureRepository.findOne(Mockito.anyLong())).thenReturn(null);
		
		pictureService.updatePicture(fooPicture, fooUser);
	}
	
	private void initializeFoo() {
		fooUser = new UserProfile();
		fooUser.setUsername("foo");
		fooUser.setDomain("ADMIN");
		
		fooPicture = new Picture();
		fooPicture.setPictureId(1L);
		fooPicture.setEditor(fooUser.getDomain() +"/"+ fooUser.getUsername());
		fooPicture.setAnnotation("annotation");
		fooPicture.setFileName("foo.jpg");
		fooPicture.setNoteId(1L);
		fooPicture.setDeleted(false);	
	}
}
