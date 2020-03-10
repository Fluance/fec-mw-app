package net.fluance.cockpit.app.service.domain.appointment.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.fluance.cockpit.app.service.domain.AppointmentService;
import net.fluance.cockpit.app.service.domain.model.RoomOperationsByStatus;
import net.fluance.cockpit.core.dao.AppointmentProcessStatusDao;
import net.fluance.cockpit.core.model.dto.appointment.operation.AppointmentProcessStatusDto;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentDetail;

@RunWith(SpringJUnit4ClassRunner.class)
public class RoomOperationsByStatusServiceTest {
	
	private LocalDate today;
	private LocalDateTime now;
	
	private String roomName = "SALLE OP1";
	private Integer companyId = 1;
	private List<AppointmentDetail> appointments = new ArrayList<>();
	private Map<Long, List<AppointmentProcessStatusDto>> appointmentProcessStatus = new HashMap<>();
	
	@TestConfiguration
	static class RoomOperationsByStatusServiceTestConfiguration {
		@Bean
		public RoomOperationsByStatusService roomOperationsByStatusService() {
			return new RoomOperationsByStatusService();
		}
	}

	@Before
	public void setUp() {
		Mockito.reset(appointmentService);
		Mockito.reset(appointmentProcessStatusDao);
		
		Mockito.when(appointmentService.getAppointmentDetailsByCriteriaShortedAndPaginated(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(),
				Mockito.anyString(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyBoolean(), Mockito.anyObject(),
				Mockito.anyObject(), Mockito.anyString(), Mockito.anyString(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject())).thenReturn(appointments);
				
		Mockito.when(appointmentProcessStatusDao.getProcessStatusFromAppointmentList(Mockito.anyListOf(AppointmentDetail.class))).thenReturn(appointmentProcessStatus);
	}	
	
	@MockBean
	AppointmentService appointmentService;
	
	@MockBean
	AppointmentProcessStatusDao appointmentProcessStatusDao;
	
	@Autowired
	RoomOperationsByStatusService roomOperationsByStatusService;
	
	@Test
	public void getRoomOperationByStatusByDate_default_data(){
		setDefaultMockData();
		printMockData();
		
		RoomOperationsByStatus roomOperationsByStatus = roomOperationsByStatusService.getRoomOperationByStatusByDate(this.companyId, this.roomName, this.today, this.now);
		
		assertEquals("Should be in the two inprogress", 2, roomOperationsByStatus.getAppointmentsInProgress().size());
		assertEquals("Should be in the inprogress list", new Long(1001), roomOperationsByStatus.getAppointmentsInProgress().get(0));
		assertEquals("Should be in the inprogress list", new Long(1002), roomOperationsByStatus.getAppointmentsInProgress().get(1));
		assertEquals("Should be operation live", new Long(1001), roomOperationsByStatus.getOperationLive());
		assertEquals("Should be upcoming", new Long(1003), roomOperationsByStatus.getUpcomingAppointment());
		
		printResult(roomOperationsByStatus);
	}
	
	@Test
	public void getRoomOperationByStatusByDate_no_upcoming(){
		setDefaultMockData();
		this.now = LocalDateTime.of(2020, 1, 28, 16, 0, 0);
		printMockData();
		
		RoomOperationsByStatus roomOperationsByStatus = roomOperationsByStatusService.getRoomOperationByStatusByDate(this.companyId, this.roomName, this.today, this.now);
		
		assertEquals("Should be in the two inprogress", 2, roomOperationsByStatus.getAppointmentsInProgress().size());
		assertEquals("Should be in the inprogress list", new Long(1001), roomOperationsByStatus.getAppointmentsInProgress().get(0));
		assertEquals("Should be in the inprogress list", new Long(1002), roomOperationsByStatus.getAppointmentsInProgress().get(1));
		assertEquals("Should be operation live", new Long(1001), roomOperationsByStatus.getOperationLive());
		assertNull("Should be null, no upcoming", roomOperationsByStatus.getUpcomingAppointment());
		
		printResult(roomOperationsByStatus);
	}
	
	@Test
	public void getRoomOperationByStatusByDate_no_operation_live(){
		setDefaultMockData();
		
		this.appointmentProcessStatus.get(1001L).get(2).setOperationLive(false);
		
		printMockData();
		
		RoomOperationsByStatus roomOperationsByStatus = roomOperationsByStatusService.getRoomOperationByStatusByDate(this.companyId, this.roomName, this.today, this.now);
		
		assertEquals("Should be in the two inprogress", 2, roomOperationsByStatus.getAppointmentsInProgress().size());
		assertEquals("Should be in the inprogress list ", new Long(1001), roomOperationsByStatus.getAppointmentsInProgress().get(0));
		assertEquals("Should be in the inprogress list ", new Long(1002), roomOperationsByStatus.getAppointmentsInProgress().get(1));
		assertNull("No operation live", roomOperationsByStatus.getOperationLive());
		assertEquals("Should be upcoming ", new Long(1003), roomOperationsByStatus.getUpcomingAppointment());
		
		printResult(roomOperationsByStatus);
	}
	
	@Test
	public void getRoomOperationByStatusByDate_no_in_progress(){
		setDefaultMockData();
		
		this.appointmentProcessStatus.get(1001L).get(2).setOperationLive(false);
		this.appointmentProcessStatus.get(1001L).get(2).setLive(false);
		this.appointmentProcessStatus.get(1001L).get(3).setEventDate(this.appointmentProcessStatus.get(1001L).get(2).getEventDate().plusMinutes(30));
		
		this.appointmentProcessStatus.get(1002L).get(1).setOperationLive(false);
		this.appointmentProcessStatus.get(1002L).get(1).setLive(false);
		this.appointmentProcessStatus.get(1002L).get(2).setEventDate(this.appointmentProcessStatus.get(1002L).get(1).getEventDate().plusMinutes(30));
		this.appointmentProcessStatus.get(1002L).get(3).setEventDate(this.appointmentProcessStatus.get(1002L).get(1).getEventDate().plusMinutes(60));
		
		printMockData();
		
		RoomOperationsByStatus roomOperationsByStatus = roomOperationsByStatusService.getRoomOperationByStatusByDate(this.companyId, this.roomName, this.today, this.now);
		
		assertEquals("Should be in the two inprogress", 0, roomOperationsByStatus.getAppointmentsInProgress().size());
		assertNull("No operation live", roomOperationsByStatus.getOperationLive());
		assertEquals("Should be upcoming ", new Long(1003), roomOperationsByStatus.getUpcomingAppointment());
		
		printResult(roomOperationsByStatus);
	}
	
	private void printMockData() {
		System.out.println("CURRENT TIME: " + this.now);
		
		System.out.println("----APPOINTMENTS AND STATUS----");
		this.appointments.forEach(appointmentDetail ->{
			System.out.println(appointmentDetail.getAppointmentId()+"-"+appointmentDetail.getDescription()+ " Beging date: " + appointmentDetail.getBeginDate());
			this.appointmentProcessStatus.get(appointmentDetail.getAppointmentId())
					.forEach(appointmentProcessStatus -> System.out.println("\t" + appointmentProcessStatus.getProcessStatusId()
																				+ " Event Date: " + appointmentProcessStatus.getEventDate()
																				+ " live: " + appointmentProcessStatus.isLive()
																				+ " operationLive: " + appointmentProcessStatus.isOperationLive()));
		});		
	}
	
	private void printResult(RoomOperationsByStatus roomOperationsByStatus) {
		System.out.println("----RESULT----");
		System.out.println("{");
		System.out.println("\troomName: '"+ roomOperationsByStatus.getRoomName()+"'");
		System.out.println("\tappointmentsInProgress: " + roomOperationsByStatus.getAppointmentsInProgress());
		System.out.println("\toperationLive: " + roomOperationsByStatus.getOperationLive());
		System.out.println("\tupcomingAppointment: " + roomOperationsByStatus.getUpcomingAppointment());
		System.out.println("}");
	}
	
	private void setDefaultMockData() {
		this.today = LocalDate.of(2020, 1, 28);
		this.now = LocalDateTime.of(2020, 1, 28, 14, 38, 0);
		LocalDateTime startOfToday = LocalDateTime.of(2020, 1, 28, 1, 0, 0);
		
		List<AppointmentProcessStatusDto> processStatus;
		
		LocalDateTime startOfAppointment;
		Long appointmentId;
		
		AppointmentDetail appointmentDetail;
		
		startOfAppointment = startOfToday.plusHours(7);
		appointmentId = 1000L;
		
		appointmentDetail = generateAppointmentDetail(appointmentId, Timestamp.valueOf(startOfAppointment), Timestamp.valueOf(startOfAppointment.plusHours(2)));
		processStatus = new ArrayList<>();
		
		processStatus.add(generateAppointmentProcessStatusDto(appointmentId, companyId, 1, startOfAppointment, false, false));
		processStatus.add(generateAppointmentProcessStatusDto(appointmentId, companyId, 2, startOfAppointment.plusMinutes(30), false, false));
		processStatus.add(generateAppointmentProcessStatusDto(appointmentId, companyId, 3, startOfAppointment.plusMinutes(60), false, false));
		processStatus.add(generateAppointmentProcessStatusDto(appointmentId, companyId, 4, startOfAppointment.plusMinutes(90), false, false));
		
		appointments.add(appointmentDetail);		
		appointmentProcessStatus.put(appointmentId, processStatus);		
		
		startOfAppointment = startOfToday.plusHours(9).plusMinutes(20);
		appointmentId = 1001L;
		
		appointmentDetail = generateAppointmentDetail(appointmentId, Timestamp.valueOf(startOfAppointment), Timestamp.valueOf(startOfAppointment.plusHours(2)));
		processStatus = new ArrayList<>();
		
		processStatus.add(generateAppointmentProcessStatusDto(appointmentId, companyId, 1, startOfAppointment, false, false));
		processStatus.add(generateAppointmentProcessStatusDto(appointmentId, companyId, 2, startOfAppointment.plusMinutes(30), false, false));
		processStatus.add(generateAppointmentProcessStatusDto(appointmentId, companyId, 3, startOfAppointment.plusMinutes(60), true, true));
		processStatus.add(generateAppointmentProcessStatusDto(appointmentId, companyId, 4, null, false, false));
		
		appointments.add(appointmentDetail);
		appointmentProcessStatus.put(appointmentId, processStatus);
		
		startOfAppointment = startOfToday.plusHours(12);
		appointmentId = 1002L;
		
		appointmentDetail = generateAppointmentDetail(appointmentId, Timestamp.valueOf(startOfAppointment), Timestamp.valueOf(startOfAppointment.plusHours(2)));
		processStatus = new ArrayList<>();
		
		processStatus.add(generateAppointmentProcessStatusDto(appointmentId, companyId, 1, startOfAppointment, false, false));
		processStatus.add(generateAppointmentProcessStatusDto(appointmentId, companyId, 2, startOfAppointment.plusMinutes(30), true, false));
		processStatus.add(generateAppointmentProcessStatusDto(appointmentId, companyId, 3, null, false, false));
		processStatus.add(generateAppointmentProcessStatusDto(appointmentId, companyId, 4, null, false, false));
		
		appointments.add(appointmentDetail);
		appointmentProcessStatus.put(appointmentId, processStatus);
		
		startOfAppointment = startOfToday.plusHours(14);
		appointmentId = 1003L;
		
		appointmentDetail = generateAppointmentDetail(appointmentId, Timestamp.valueOf(startOfAppointment), Timestamp.valueOf(startOfAppointment.plusHours(2)));
		processStatus = new ArrayList<>();
		
		processStatus.add(generateAppointmentProcessStatusDto(appointmentId, companyId, 1, null, false, false));
		processStatus.add(generateAppointmentProcessStatusDto(appointmentId, companyId, 2, null, false, false));
		processStatus.add(generateAppointmentProcessStatusDto(appointmentId, companyId, 3, null, false, false));
		processStatus.add(generateAppointmentProcessStatusDto(appointmentId, companyId, 4, null, false, false));
		
		appointments.add(appointmentDetail);
		appointmentProcessStatus.put(appointmentId, processStatus);
	}
	
	private AppointmentDetail generateAppointmentDetail(Long appointmentId, Timestamp beginDate, Timestamp endDate) {
		return  new AppointmentDetail(4, appointmentId, beginDate, endDate, "Operation", null, "Operation one", null, null,  null, null,null, null, null, null, null, null);
	}
	
	private AppointmentProcessStatusDto generateAppointmentProcessStatusDto(Long appointmentId, Integer companyId, Integer processStatusId, LocalDateTime eventDate, boolean live, boolean operationLive) {
		AppointmentProcessStatusDto appointmentProcessStatusDto = new AppointmentProcessStatusDto();
		
		appointmentProcessStatusDto.setAppointmentId(appointmentId);
		appointmentProcessStatusDto.setCompanyId(companyId);
		appointmentProcessStatusDto.setProcessStatusId(processStatusId);
		appointmentProcessStatusDto.setProcessStatusDescription(appointmentProcessStatusDto.getProcessStatusId() + " Description");
		appointmentProcessStatusDto.setEventDate(eventDate);
		appointmentProcessStatusDto.setLive(live);
		appointmentProcessStatusDto.setOperationLive(operationLive);
		
		return appointmentProcessStatusDto;
	}

}
