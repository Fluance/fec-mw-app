package net.fluance.cockpit.core.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.fluance.cockpit.core.model.dto.appointment.operation.AppointmentProcessStatusDto;
import net.fluance.cockpit.core.model.dto.appointment.operation.AppointmentProcessStatusMapper;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentDetail;
import net.fluance.cockpit.core.model.jpa.appointment.AppointmentProcessStatus;
import net.fluance.cockpit.core.model.jpa.appointment.AppointmentProcessStatusPK;
import net.fluance.cockpit.core.model.jpa.appointment.ProcessStatus;
import net.fluance.cockpit.core.model.jpa.appointment.ProcessStatusPK;
import net.fluance.cockpit.core.repository.jdbc.appointment.AppointmentDetailRepository;
import net.fluance.cockpit.core.repository.jpa.appointment.AppointmentProcessStatusRepository;
import net.fluance.cockpit.core.repository.jpa.appointment.ProcessStatusRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class AppointmentProcessStatusDaoTest {
	
	private static final Integer COMPANY_ID = 8;
	private static final String COMPANY_CODE = "CDG";
	private static final boolean PRINT_RESULTS = true;
	private static final long APPOINTMENT_ID = 100L;

	//Date to us as now for all the tests
	private LocalDateTime now;
	
	@TestConfiguration
	static class AppointmentProcessStatusDaoConfiguration {
		@Bean
		PropertyPlaceholderConfigurer propConfig() {
			PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
			
			Properties properties = new Properties();
			properties.put("whiteboardsurgery.config.operationLifeStatusByCompany", "{'CDG':{'A3', 'A4'}}");

			propertyPlaceholderConfigurer.setProperties(properties);

			propertyPlaceholderConfigurer.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");

			return propertyPlaceholderConfigurer;
		}
		
		@Bean
		public AppointmentProcessStatusDao appointmentProcessStatusDao() {
			return new AppointmentProcessStatusDao();
		}
		
		@Bean
		public AppointmentProcessStatusMapper appointmentProcessStatusMapper() {
			return new AppointmentProcessStatusMapper();
		}
	}
	
	@Before
	public void setUp() {
		Mockito.reset(appointmentProcessStatusRepository);
		Mockito.reset(processStatusRepository);
		Mockito.reset(appointmentDetailRepository);
		
		now = LocalDateTime.now();
		
		doAnswer((Answer<AppointmentDetail>) invocation -> getAppointmentDetail(invocation.getArgumentAt(0, Long.class))).when(appointmentDetailRepository).findByAppointmentId(Mockito.anyLong());

		doAnswer((Answer<List<ProcessStatus>>) invocation -> getProcessStatusListByCompany(invocation.getArgumentAt(0, Integer.class))).when(processStatusRepository).findByIdCompanyIdOrderByStep(COMPANY_ID);

		doAnswer((Answer<List<AppointmentProcessStatus>>) invocation -> getAppointmentProcessStatus(invocation.getArgumentAt(0, Long.class), LocalDateTime.now(), 5)).when(appointmentProcessStatusRepository).findByIdAppointmentId(Mockito.anyLong());
	}
	
	@MockBean
	private AppointmentProcessStatusRepository appointmentProcessStatusRepository;

	@MockBean
	private ProcessStatusRepository processStatusRepository;
	
	@MockBean
	private AppointmentDetailRepository appointmentDetailRepository; 
	
	@Autowired
	AppointmentProcessStatusDao appointmentProcessStatusDao;
	
	@Test
	public void getStatusByAppointmentId_should_one_live_and_operation_live() {
		Long appointmentId = APPOINTMENT_ID;
		
		doAnswer((Answer<List<AppointmentProcessStatus>>) invocation -> getAppointmentProcessStatus(invocation.getArgumentAt(0, Long.class), LocalDateTime.now(), 3)).when(appointmentProcessStatusRepository).findByIdAppointmentId(Mockito.anyLong());

		List<AppointmentProcessStatusDto> status = appointmentProcessStatusDao.getProcessStatusByAppointmentId(appointmentId);
				
		verify(appointmentDetailRepository, times(1)).findByAppointmentId(appointmentId);
		verify(processStatusRepository, times(1)).findByIdCompanyIdOrderByStep(COMPANY_ID);
		verify(appointmentProcessStatusRepository, times(1)).findByIdAppointmentId(Mockito.anyLong());
				
		assertNotNull("Must return an array", status);
		assertEquals("Must return an array with data", 5, status.size());
		status.forEach(appointmentProcessStatusDto -> assertNotNull("Must have description", appointmentProcessStatusDto.getProcessStatusDescription()));
		
		printStatus("getStatusByAppointmentId_should_one_live_and_operation_live", status);

		assertFalse("Must not be live", status.get(0).isLive());
		assertNotNull("Must not be null", status.get(0).getEventDate());
		assertFalse("Must not be operation live", status.get(0).isOperationLive());

		assertFalse("Must not be live", status.get(1).isLive());
		assertNotNull("Must not be null", status.get(1).getEventDate());
		assertFalse("Must not be operation live", status.get(1).isOperationLive());

		assertTrue("Must be live", status.get(2).isLive());
		assertNotNull("Must not be null", status.get(2).getEventDate());
		assertTrue("Must be operation live", status.get(2).isOperationLive());

		assertFalse("Must not be live", status.get(3).isLive());
		assertNull("Must be null", status.get(3).getEventDate());
		assertFalse("Must not be operation live", status.get(3).isOperationLive());

		assertFalse("Must not live", status.get(4).isLive());
		assertNull("Must be null", status.get(4).getEventDate());
		assertFalse("Must not be operation live", status.get(4).isOperationLive());
	}
	
	@Test
	public void getStatusByAppointmentId_should_return_one_live_and_not_operation_live() {
		Long appointmentId = APPOINTMENT_ID;
				
		doAnswer((Answer<List<AppointmentProcessStatus>>) invocation -> getAppointmentProcessStatus(invocation.getArgumentAt(0, Long.class), LocalDateTime.now(), 2)).when(appointmentProcessStatusRepository).findByIdAppointmentId(Mockito.anyLong());

		List<AppointmentProcessStatusDto> status = appointmentProcessStatusDao.getProcessStatusByAppointmentId(appointmentId);
				
		verify(appointmentDetailRepository, times(1)).findByAppointmentId(appointmentId);
		verify(processStatusRepository, times(1)).findByIdCompanyIdOrderByStep(COMPANY_ID);
		verify(appointmentProcessStatusRepository, times(1)).findByIdAppointmentId(Mockito.anyLong());	
				
		assertNotNull("Must return an array", status);
		assertEquals("Must return an array with data", 5, status.size());
		status.forEach(appointmentProcessStatusDto -> assertNotNull("Must have description", appointmentProcessStatusDto.getProcessStatusDescription()));
		
		printStatus("getStatusByAppointmentId_should_return_one_live_and_not_operation_live", status);

		assertFalse("Must not be live", status.get(0).isLive());
		assertNotNull("Must not be null", status.get(0).getEventDate());
		assertFalse("Must not be operation live", status.get(0).isOperationLive());

		assertTrue("Must be live", status.get(1).isLive());
		assertNotNull("Must not be null", status.get(1).getEventDate());
		assertFalse("Must not be operation live", status.get(1).isOperationLive());

		assertFalse("Must not be live", status.get(2).isLive());
		assertNull("Must be null", status.get(2).getEventDate());
		assertFalse("Must not be operation live", status.get(2).isOperationLive());

		assertFalse("Must not be live", status.get(3).isLive());
		assertNull("Must be null", status.get(3).getEventDate());
		assertFalse("Must not be operation live", status.get(3).isOperationLive());

		assertFalse("Must not live", status.get(4).isLive());
		assertNull("Must be null", status.get(4).getEventDate());
		assertFalse("Must not be operation live", status.get(4).isOperationLive());
	}
	
	@Test
	public void getStatusByAppointmentId_should_return_last_live() {
		Long appointmentId = APPOINTMENT_ID;

		List<AppointmentProcessStatusDto> status = appointmentProcessStatusDao.getProcessStatusByAppointmentId(appointmentId);
				
		verify(appointmentDetailRepository, times(1)).findByAppointmentId(appointmentId);
		verify(processStatusRepository, times(1)).findByIdCompanyIdOrderByStep(COMPANY_ID);
		verify(appointmentProcessStatusRepository, times(1)).findByIdAppointmentId(Mockito.anyLong());	
				
		assertNotNull("Must return an array", status);
		assertEquals("Must return an array with data", 5, status.size());
		status.forEach(appointmentProcessStatusDto -> assertNotNull("Must have description", appointmentProcessStatusDto.getProcessStatusDescription()));
		
		printStatus("getStatusByAppointmentId_should_return", status);

		assertFalse("Must not be live", status.get(0).isLive());
		assertNotNull("Must not be null", status.get(0).getEventDate());

		assertFalse("Must not be live", status.get(1).isLive());
		assertNotNull("Must not be null", status.get(1).getEventDate());

		assertFalse("Must not be live", status.get(2).isLive());
		assertNotNull("Must be null", status.get(2).getEventDate());

		assertFalse("Must not be live", status.get(3).isLive());
		assertNotNull("Must not be null", status.get(3).getEventDate());

		assertTrue("Must be live", status.get(4).isLive());
		assertNotNull("Must not be null", status.get(4).getEventDate());
	}
	
	@Test
	public void getStatusByAppointmentId_should_return_any_live() {
		Long appointmentId = APPOINTMENT_ID;
		
		doAnswer((Answer<AppointmentDetail>) invocation -> getAppointmentDetail(invocation.getArgumentAt(0, Long.class), now.minusHours(1).minusMinutes(5), now.minusHours(1).plusMinutes(40))).when(appointmentDetailRepository).findByAppointmentId(Mockito.anyLong());
		doAnswer((Answer<List<AppointmentProcessStatus>>) invocation -> getAppointmentProcessStatus(invocation.getArgumentAt(0, Long.class), LocalDateTime.now().minusHours(1), 5)).when(appointmentProcessStatusRepository).findByIdAppointmentId(Mockito.anyLong());
		
		List<AppointmentProcessStatusDto> status = appointmentProcessStatusDao.getProcessStatusByAppointmentId(appointmentId);
				
		verify(appointmentDetailRepository, times(1)).findByAppointmentId(appointmentId);
		verify(processStatusRepository, times(1)).findByIdCompanyIdOrderByStep(COMPANY_ID);
		verify(appointmentProcessStatusRepository, times(1)).findByIdAppointmentId(Mockito.anyLong());	
				
		assertNotNull("Must return an array", status);
		assertEquals("Must return an array with data", 5, status.size());
		status.forEach(appointmentProcessStatusDto -> assertNotNull("Must have description", appointmentProcessStatusDto.getProcessStatusDescription()));
		
		printStatus("getStatusByAppointmentId_should_return", status);

		assertFalse("Must not be live", status.get(0).isLive());
		assertNotNull("Must not be null", status.get(0).getEventDate());

		assertFalse("Must not be live", status.get(1).isLive());
		assertNotNull("Must not be null", status.get(1).getEventDate());

		assertFalse("Must not be live", status.get(2).isLive());
		assertNotNull("Must be null", status.get(2).getEventDate());

		assertFalse("Must not be live", status.get(3).isLive());
		assertNotNull("Must not be null", status.get(3).getEventDate());

		assertFalse("Must not be live", status.get(4).isLive());
		assertNotNull("Must not be null", status.get(4).getEventDate());
	}
	
	@Test
	public void getStatusByAppointmentId_should_return_any_live_because_no_data() {
		Long appointmentId = APPOINTMENT_ID;
		
		Mockito.when(appointmentProcessStatusRepository.findByIdAppointmentId(Mockito.anyLong())).thenReturn(new ArrayList<>());

		List<AppointmentProcessStatusDto> status = appointmentProcessStatusDao.getProcessStatusByAppointmentId(appointmentId);
				
		verify(appointmentDetailRepository, times(1)).findByAppointmentId(appointmentId);
		verify(processStatusRepository, times(1)).findByIdCompanyIdOrderByStep(COMPANY_ID);
		verify(appointmentProcessStatusRepository, times(1)).findByIdAppointmentId(Mockito.anyLong());
				
		assertNotNull("Must return an array", status);
		assertEquals("Must return an array with data", 5, status.size());
		status.forEach(appointmentProcessStatusDto -> assertNotNull("Must have description", appointmentProcessStatusDto.getProcessStatusDescription()));
		
		printStatus("getStatusByAppointmentId_should_return", status);

		assertFalse("Must not be live", status.get(0).isLive());
		assertNull("Must be null", status.get(0).getEventDate());

		assertFalse("Must not be live", status.get(1).isLive());
		assertNull("Must be null", status.get(1).getEventDate());

		assertFalse("Must not be live", status.get(2).isLive());
		assertNull("Must be null", status.get(2).getEventDate());

		assertFalse("Must not be live", status.get(3).isLive());
		assertNull("Must be null", status.get(3).getEventDate());

		assertFalse("Must not be live", status.get(4).isLive());
		assertNull("Must be null", status.get(4).getEventDate());
	}
	
	@Test
	public void getStatusByAppointmentId_should_return_empty_no_status_for_company() {
		Long appointmentId = APPOINTMENT_ID;
		
		doAnswer((Answer<AppointmentDetail>) invocation -> getAppointmentDetail(invocation.getArgumentAt(0, Long.class), now.minusHours(1).minusMinutes(5), now.minusHours(1).plusMinutes(40))).when(appointmentDetailRepository).findByAppointmentId(Mockito.anyLong());
		
		Mockito.when(processStatusRepository.findByIdCompanyIdOrderByStep(COMPANY_ID)).thenReturn(new ArrayList<>());

		List<AppointmentProcessStatusDto> status = appointmentProcessStatusDao.getProcessStatusByAppointmentId(appointmentId);
				
		verify(appointmentDetailRepository, times(1)).findByAppointmentId(appointmentId);
		verify(processStatusRepository, times(1)).findByIdCompanyIdOrderByStep(COMPANY_ID);
		verify(appointmentProcessStatusRepository, times(1)).findByIdAppointmentId(Mockito.anyLong());	
				
		assertNotNull("Must return an array", status);
		assertEquals("Must return an array without data", 0, status.size());
		
		printStatus("getStatusByAppointmentId_should_return", status);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getStatusByAppointmentId_should_throw_IllegalArgumentException() {
		appointmentProcessStatusDao.getProcessStatusByAppointmentId(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getStatusByAppointmentId_should_throw_IllegalArgumentException_appointment_not_exists() {
		doAnswer((Answer<AppointmentDetail>) invocation -> null).when(appointmentDetailRepository).findByAppointmentId(Mockito.anyLong());
		
		appointmentProcessStatusDao.getProcessStatusByAppointmentId(APPOINTMENT_ID);
	}
	
	/**
	 * Generate {@link AppointmentProcessStatus} with the given data
	 * 
	 * @param appointmentId
	 * @param processStatusId
	 * @param eventDate
	 * @return
	 */
	private AppointmentProcessStatus getAppointmentProcessStatus(Long appointmentId, Integer processStatusId, LocalDateTime eventDate) {
		AppointmentProcessStatus appointmentProcessStatus = new AppointmentProcessStatus();
		AppointmentProcessStatusPK id = new AppointmentProcessStatusPK();
		
		id.setAppointmentId(appointmentId);
		id.setCompanyId(AppointmentProcessStatusDaoTest.COMPANY_ID);
		id.setProcessStatusId(processStatusId);
		
		appointmentProcessStatus.setId(id);

		appointmentProcessStatus.setEventDate(Timestamp.valueOf(eventDate));
		
		appointmentProcessStatus.setCompanyCode(AppointmentProcessStatusDaoTest.COMPANY_CODE);
		appointmentProcessStatus.setProcessStatusCode("A" + processStatusId);
		
		return appointmentProcessStatus;
	}
	
	/**
	 * Return the list Appointment Process Status for one appointment
	 * 
	 * @param appointmentId
	 * @param now
	 * @param howMany
	 * @return
	 */
	private List<AppointmentProcessStatus> getAppointmentProcessStatus(Long appointmentId, LocalDateTime now, int howMany){
		List<AppointmentProcessStatus> appointmentProcessStatus = new ArrayList<>();
		
		IntStream.rangeClosed(1, howMany)
    	.forEach(processStatusId -> {	
    		LocalDateTime eventDate = now.plusMinutes(appointmentId * 5L);
    		appointmentProcessStatus.add(getAppointmentProcessStatus(appointmentId, processStatusId, eventDate));
    	});
		
		return appointmentProcessStatus;
	}
	
	/**
	 * Generates the appointment detail with begin date five minutes before and end date 5 hours later
	 * 
	 * @param appointmentId
	 * @return
	 */
	private AppointmentDetail getAppointmentDetail(Long appointmentId) {
		return getAppointmentDetail(appointmentId, now.minusMinutes(5));
	}
	
	/**
	 * Generates the appointment detail with begin date the given date and end date null
	 * 
	 * @param appointmentId
	 * @param givenBeginDate
	 * @return
	 */
	private AppointmentDetail getAppointmentDetail(Long appointmentId, LocalDateTime givenBeginDate) {
		return getAppointmentDetail(appointmentId, givenBeginDate, null);
	}
	
	/**
	 * Generates the appointment detail with the given beging date and the given end date
	 * 
	 * @param appointmentId
	 * @param givenBeginDate
	 * @param givenEndDate can be null
	 * @return
	 */
	private AppointmentDetail getAppointmentDetail(Long appointmentId, LocalDateTime givenBeginDate, LocalDateTime givenEndDate) {
		Timestamp beginDate = Timestamp.from(givenBeginDate.atZone( ZoneId.systemDefault()).toInstant());
		Timestamp endDate = null;
		if(givenEndDate != null) {
			endDate = Timestamp.from(givenEndDate.atZone( ZoneId.systemDefault()).toInstant());
		}

		return new AppointmentDetail(1, appointmentId, beginDate, endDate, "OP", null, "Foo operation", null, null, AppointmentProcessStatusDaoTest.COMPANY_ID, null, null, null, null, null, null, null);
	}
	
	/**
	 * Gets the list of status for the company
	 * 
	 * 
	 * @param companyId
	 * @return
	 */
	private List<ProcessStatus> getProcessStatusListByCompany(Integer companyId){
		List<ProcessStatus> processStatus = new ArrayList<>();
		
		IntStream.rangeClosed(1, 5).forEach(processStatusId -> processStatus.add(getProcessStatus(companyId, processStatusId, processStatusId)));
		
		return processStatus;
	}
	
	/**
	 * Creates a ProcessStatus using as id the processStatusId
	 * 
	 * @param companyId
	 * @param processStatusId
	 * @param step
	 * @return
	 */
	private ProcessStatus getProcessStatus(Integer companyId, Integer processStatusId, Integer step) {
		ProcessStatus processStatus = new ProcessStatus();
		ProcessStatusPK processStatusPK = new ProcessStatusPK();
		
		processStatusPK.setId(processStatusId);
		processStatusPK.setCompanyId(companyId);
		processStatusPK.setProcessStatusId(processStatusId);
		
		processStatus.setId(processStatusPK);
		processStatus.setStep(step);
		processStatus.setDescription("Description " + processStatusId);
		processStatus.setProcessStatusCode("A"+processStatusId);
		
		return processStatus;
	}
	
	/**
	 * Prints the status as well formated JSON
	 * 
	 * @param tittle
	 * @param status
	 */
	private void printStatus(String tittle, List<AppointmentProcessStatusDto> status){
		if(PRINT_RESULTS) {
			ObjectMapper mapper = new ObjectMapper();
	
			//Object to JSON in String
			String jsonInString = "Error";
			try {
				jsonInString = mapper.writeValueAsString(status);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
			System.out.println(tittle);
			System.out.println(jsonInString.replace("{", "\n\t{\n\t\t").replace("}", "\n\t}").replace(",", ",\n\t\t").replace("]", "\n]"));
			System.out.println("------------------------------------------------------------------------------------");
		}
	}
}
