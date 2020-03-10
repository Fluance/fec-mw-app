package net.fluance.cockpit.app.service.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.fluance.cockpit.core.dao.AppointmentOperationNoteDao;
import net.fluance.cockpit.core.dao.AppointmentProcessStatusDao;
import net.fluance.cockpit.core.model.dto.appointment.operation.AppointmentProcessStatusDto;

@RunWith(SpringJUnit4ClassRunner.class)
public class AppointmentOperationServiceTest {
	private static final Integer COMPANY_ID = 8;
	
	@TestConfiguration
	static class AppointmentOperationServiceTestConfiguration {

		@Bean
		public AppointmentOperationService appointmentOperationService() {
			return new AppointmentOperationService();
		}
	}
	
	@Before
	public void setUp() {
		Mockito.reset(appointmentProcessStatusDao);
		Mockito.reset(appointmentOperationNoteDao);
		Mockito.reset(lockService);
	}
	
	@MockBean
	AppointmentProcessStatusDao appointmentProcessStatusDao;
	@MockBean
	AppointmentOperationNoteDao appointmentOperationNoteDao;
	@MockBean
	LockService lockService;
	
	@Autowired
	AppointmentOperationService appointmentOperationService;
	
	@Test
	public void getOperationStatus_should_return() {
		Long appointmentId = 100l;
		
		Mockito.when(appointmentProcessStatusDao.getProcessStatusByAppointmentId(appointmentId)).thenReturn(getListAppointmentProcessStatusDto(appointmentId));
		
		List<AppointmentProcessStatusDto> status = appointmentOperationService.getOperationProcessStatusByAppointmentId(appointmentId);
		
		verify(appointmentProcessStatusDao, times(1)).getProcessStatusByAppointmentId(appointmentId);	
		assertNotNull("Must return an array", status);
		assertEquals("Must return an array with data", 5, status.size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getOperationStatus_should_throw_IllegalArgumentException() {
		appointmentOperationService.getOperationProcessStatusByAppointmentId(null);
	}
	
	/**
	 * Generates a list of Mocks for the given appointment
	 * 
	 * @param appointmentId
	 * @return
	 */
	private List<AppointmentProcessStatusDto> getListAppointmentProcessStatusDto(Long appointmentId){		
		List<AppointmentProcessStatusDto> status = new ArrayList<>();
		LocalDateTime eventDate = LocalDateTime.now();
		
		IntStream.rangeClosed(1, 5)
    	.forEach(processStatusId -> {
    		status.add(getAppointmentProcessStatusDto(appointmentId, processStatusId, COMPANY_ID, eventDate.plusMinutes(processStatusId * 5l)));
    	});
		
		return status;
	}
	
	/**
	 * Return a {@link AppointmentProcessStatusDto} with the given data
	 * 
	 * @param appointmentId
	 * @param processStatusId
	 * @param companyId
	 * @param eventDate
	 * @return
	 */
	private AppointmentProcessStatusDto getAppointmentProcessStatusDto(Long appointmentId, Integer processStatusId, Integer companyId, LocalDateTime eventDate) {
		AppointmentProcessStatusDto appointmentProcessStatusDto = new AppointmentProcessStatusDto();
		
		appointmentProcessStatusDto.setAppointmentId(appointmentId);
		appointmentProcessStatusDto.setCompanyId(companyId);
		appointmentProcessStatusDto.setEventDate(eventDate);
		appointmentProcessStatusDto.setProcessStatusId(processStatusId);
		appointmentProcessStatusDto.setStepNumber(processStatusId);
		appointmentProcessStatusDto.setProcessStatusDescription("Description " + processStatusId);
		
		return appointmentProcessStatusDto;
	}
}
