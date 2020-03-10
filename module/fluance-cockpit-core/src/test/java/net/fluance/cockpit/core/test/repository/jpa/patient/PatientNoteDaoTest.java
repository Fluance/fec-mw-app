package net.fluance.cockpit.core.test.repository.jpa.patient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.fluance.cockpit.core.model.dto.patient.NoteDto;
import net.fluance.cockpit.core.model.jpa.patient.Note;
import net.fluance.cockpit.core.model.jpa.patient.NoteMapper;
import net.fluance.cockpit.core.repository.jpa.patient.PatientNoteDao;
import net.fluance.cockpit.core.repository.jpa.patient.PatientNoteRepository;
import net.fluance.cockpit.core.util.patient.NoteMock;

/**
 * Test class for NoteDao. Test at least all the methods with the positve answer
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class PatientNoteDaoTest {

	@TestConfiguration
	static class PatientNoteDaoTestConfiguration {
		@Bean
		public PatientNoteDao patientNoteDao() {
			return new PatientNoteDao();
		}
		
		@Bean
		public NoteMapper noteMapper() {
			return new NoteMapper();
		}
	}
		
	@Before
	public void setUp() {
		Mockito.reset(patientNoteRepository);
	}
		
	@Autowired
	private PatientNoteDao patientNoteDao;
	
	@Autowired
	NoteMapper noteMapper;
	
	@MockBean
	private PatientNoteRepository patientNoteRepository;
	
	@Test
	public void getNotes_should_return() {
		Mockito.when(patientNoteRepository.findAll(Mockito.any(Pageable.class))).thenReturn(NoteMock.getNotesAsPage(10));
		
		Page<NoteDto> result = patientNoteDao.getNotes(new PageRequest(0, 10));
		assertNotNull(result);
		assertEquals("Number of elements must be 10", 10, result.getNumberOfElements());			
	}
	
	@Test
	public void getNotes_should_return_empty_list() {		
		Mockito.when(patientNoteRepository.findAll(Mockito.any(Pageable.class))).thenReturn(NoteMock.getEmptyPage());
		
		Page<NoteDto> result = patientNoteDao.getNotes(new PageRequest(0, 10));
		assertNotNull(result);
		assertEquals("Number of elements must be 0", 0, result.getNumberOfElements());
	}

	@Test
	public void save_should_return() {
		Mockito.when(patientNoteRepository.save(Mockito.any(Note.class)) ).thenReturn(NoteMock.getOneNote(1L));
		
		NoteDto note = patientNoteDao.save(NoteMock.getOneNoteDto(1L));
		assertNotNull(note);
		assertEquals("Note id must be 1L", Long.valueOf(1L), note.getId());
	}

	@Test
	public void findById_should_return() {
		Mockito.when(patientNoteRepository.findOne(Mockito.any(Long.class)) ).thenAnswer(
			new Answer<Note>(){		
				@Override
				public Note answer(InvocationOnMock invocation) throws Throwable {
					return NoteMock.getOneNote(invocation.getArgumentAt(0, Long.class));					
				}
			}
		);
		
		NoteDto note = patientNoteDao.findById(1L);
		assertNotNull(note);
		assertEquals("Note id must be 1L", Long.valueOf(1L), note.getId());
	}
	
	@Test
	public void delete() {		
		patientNoteDao.delete(1L);
	}
		
}
