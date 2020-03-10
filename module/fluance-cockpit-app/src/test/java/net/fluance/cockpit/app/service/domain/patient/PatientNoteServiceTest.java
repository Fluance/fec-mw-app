package net.fluance.cockpit.app.service.domain.patient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javassist.NotFoundException;
import net.fluance.cockpit.app.web.util.patient.NoteDtoMock;
import net.fluance.cockpit.core.model.dto.patient.NoteDto;
import net.fluance.cockpit.core.repository.jpa.patient.PatientNoteDao;

/**
 * Class to test PatientNoteService
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class PatientNoteServiceTest {

	@TestConfiguration
	static class WhiteBoardFilesServiceTestConfiguration {

		@Bean
		public PatientNoteService patientNoteService() {
			return new PatientNoteService();
		}

		@Bean
		PropertyPlaceholderConfigurer propConfig() {
			PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();

			Properties properties = new Properties();
			properties.put("net.fluance.cockpit.core.model.repository.defaultResultLimit", "10");

			propertyPlaceholderConfigurer.setProperties(properties);

			propertyPlaceholderConfigurer.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");

			return propertyPlaceholderConfigurer;
		}
	}

	@Before
	public void setUp() {
		Mockito.reset(patientNoteDao);
	}

	@Autowired
	PatientNoteService patientNoteService;

	@MockBean
	PatientNoteDao patientNoteDao;

	@Test
	public void getNotes_should_return() {
		Mockito.when(patientNoteDao.getNotes(Mockito.any(PageRequest.class))).thenReturn(NoteDtoMock.getNotesDtoAsPage(10));

		Page<NoteDto> page = patientNoteService.getNotes(10);
		assertNotNull(page);
		assertEquals("Should return 10 elements", 10, page.getNumberOfElements());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getNotes_IllegalArgumentException() {
		patientNoteService.getNotes(-1);
	}

	@Test
	public void saveNote_should_return() {
		Long newId = new Long(2l);
		
		Mockito.when(patientNoteDao.save(Mockito.any(NoteDto.class))).then(new Answer<NoteDto>() {
			@Override
			public NoteDto answer(InvocationOnMock invocation) throws Throwable {
				NoteDto note = invocation.getArgumentAt(0, NoteDto.class);
				note.setId(newId);
				return note;
			}
		});
		
		NoteDto noteRequest = NoteDtoMock.getOneNoteDto(1L);
		//The service must work with null on the id, a new one will be assigned.
		noteRequest.setId(null);

		NoteDto note = patientNoteService.saveNote(noteRequest);
		assertNotNull(note);
		assertEquals("note id must be 1L", newId, note.getId());
	}
	
	@Test
	public void getNoteById_should_return() throws NotFoundException {
		Mockito.when(patientNoteDao.findById(Mockito.anyLong())).thenReturn(NoteDtoMock.getOneNoteDto(1L));
		
		NoteDto note = patientNoteService.getNoteById(1L);
		assertNotNull(note);
		assertEquals("note id must be 1L", Long.valueOf(1L), note.getId());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getNoteById_IllegalArgumentException() throws NotFoundException {
		patientNoteService.getNoteById(null);
	}
	
	@Test(expected=NotFoundException.class)
	public void getNoteById_NotFoundException() throws NotFoundException {
		Mockito.when(patientNoteDao.findById(Mockito.anyLong())).thenReturn(null);
		patientNoteService.getNoteById(1L);
	}
	
	@Test
	public void updateNote_should_return() {
		Mockito.when(patientNoteDao.save(Mockito.any(NoteDto.class))).then(new Answer<NoteDto>() {
			@Override
			public NoteDto answer(InvocationOnMock invocation) throws Throwable {
				return invocation.getArgumentAt(0, NoteDto.class);
			}
		});
		
		NoteDto note = patientNoteService.updateNote(1L, NoteDtoMock.getOneNoteDto(1L));
		
		assertNotNull(note);
		assertEquals("note id must be 1L", Long.valueOf(1L), note.getId());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void updateNote_IllegalArgumentException_id_null() {
		patientNoteService.updateNote(null, NoteDtoMock.getOneNoteDto(1L));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void updateNote_IllegalArgumentException_id_not_match() {
		patientNoteService.updateNote(2L, NoteDtoMock.getOneNoteDto(1L));
	}
	
	@Test
	public void deleteNote_should_return() {
		patientNoteService.deleteNote(1L);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void deleteNote_IllegalArgumentException() {
		patientNoteService.deleteNote(null);
	}
}
