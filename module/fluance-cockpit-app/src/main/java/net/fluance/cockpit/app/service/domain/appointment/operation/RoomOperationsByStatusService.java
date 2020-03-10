package net.fluance.cockpit.app.service.domain.appointment.operation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.app.service.domain.AppointmentService;
import net.fluance.cockpit.app.service.domain.model.RoomOperationsByStatus;
import net.fluance.cockpit.core.dao.AppointmentProcessStatusDao;
import net.fluance.cockpit.core.model.dto.appointment.operation.AppointmentProcessStatusDto;
import net.fluance.cockpit.core.model.jdbc.appointment.AppointmentDetail;

@Service
public class RoomOperationsByStatusService {
	
	private static final String APPOINTMENT_TYPE = "operation";
	private static final String ORDER_BY = "begingdt";
	private static final String SORT_ORDER = "ASC";
	private static final Integer LIMIT = 100;
	private static final Integer OFFSET = 0;
	private static final boolean INCLUDE_ACTIVE = false;
	
	@Autowired
	AppointmentService appointmentService;
	
	@Autowired
	AppointmentProcessStatusDao appointmentProcessStatusDao;
	
	/**
	 * Get the operations for the given list of rooms grouped by the status defined by {@link RoomOperationsByStatus}
	 * 
	 * @param companyId
	 * @param rooms
	 * @return
	 */
	public List<RoomOperationsByStatus> getRoomsOperationsByStatus(Integer companyId, List<String> rooms) {
		List<RoomOperationsByStatus> roomsOperationsByStatus = new ArrayList<>();
		
		for (String roomName : rooms) {
			roomsOperationsByStatus.add(getRoomOperationByStatusByDate(companyId, roomName, today(), now()));
		}
		
		return roomsOperationsByStatus;
	}

	/**
	 * <b>Package scope for test purpose</b><br>
	 * Get the operations for the given room grouped by the status defined by {@link RoomOperationsByStatus}
	 * 
	 * @param companyId
	 * @param roomName
	 * @param date
	 * @param nowDateTime
	 * @return
	 */
	RoomOperationsByStatus getRoomOperationByStatusByDate(Integer companyId, String roomName, LocalDate date, LocalDateTime nowDateTime) {
		RoomOperationsByStatus roomOperationsByStatus = new RoomOperationsByStatus();
		roomOperationsByStatus.setRoomName(roomName);

		List<AppointmentDetail> appointments = appointmentService.getAppointmentDetailsByCriteriaShortedAndPaginated(companyId, null, null, null, null, APPOINTMENT_TYPE, null, Collections.singletonList(roomName), INCLUDE_ACTIVE, date, date, ORDER_BY, SORT_ORDER, LIMIT, OFFSET, null);
		
		Long operationLive = null;
		List<Long> appointmentsInProgress = new ArrayList<>();
		Long upcomingAppointment = null;
		
		Map<Long, List<AppointmentProcessStatusDto>> appointmentProcessStatusByAppointmentId =  appointmentProcessStatusDao.getProcessStatusFromAppointmentList(appointments);
		
		for (AppointmentDetail appointment : appointments) {
			AppointmentProcessStatusDto lastStatus = getLastStatus(appointmentProcessStatusByAppointmentId.get(appointment.getAppointmentId()));
			
			if(isAppointmentLive(lastStatus)) {
				appointmentsInProgress.add(appointment.getAppointmentId());
				
				if(lastStatus.isOperationLive()) {
					operationLive = appointment.getAppointmentId();
				}
			}
			
			if(isUpcommingAppointment(appointment, upcomingAppointment, nowDateTime)) {
				upcomingAppointment = appointment.getAppointmentId();
			}
		}
		
		roomOperationsByStatus.setAppointmentsInProgress(appointmentsInProgress);
		roomOperationsByStatus.setOperationLive(operationLive);
		roomOperationsByStatus.setUpcomingAppointment(upcomingAppointment);
				
		return roomOperationsByStatus;
	}

	/**
	 * Null control and statusLive 
	 * 
	 * @param lastStatus
	 * @return
	 */
	public boolean isAppointmentLive(AppointmentProcessStatusDto lastStatus) {
		return lastStatus != null && lastStatus.isLive();
	}	
	
	/**
	 * Determinates if the appointment is the next upcomming based in the date and time and the current value of the upcomming
	 * 
	 * @param appointment
	 * @param upcomingAppointment
	 * @param nowDateTime
	 * @return
	 */
	private boolean isUpcommingAppointment(AppointmentDetail appointment, Long upcomingAppointment, LocalDateTime nowDateTime) {
		return upcomingAppointment == null && appointment.getBeginDate() != null && appointment.getBeginDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().compareTo(nowDateTime) >= 0;
	}
	
	/**
	 * Get the last status with eventDate.<br>
	 * And status with event date can be live and if it is live can be operationLive as well, the logic to manages those status is in {@link AppointmentProcessStatusDao}
	 * 
	 * @param appointmentProcessStatus
	 * @return
	 */
	private AppointmentProcessStatusDto getLastStatus(List<AppointmentProcessStatusDto> appointmentProcessStatus) {
		AppointmentProcessStatusDto lastStatus = null;
		if(appointmentProcessStatus != null) {
			for (AppointmentProcessStatusDto appointmentProcessStatusDto : appointmentProcessStatus) {
				if(appointmentProcessStatusDto.getEventDate() != null) {
					lastStatus = appointmentProcessStatusDto;
				}
			}
		}
		return lastStatus;
	}
	
	private LocalDate today() {
		return LocalDate.now();
	}
	
	private LocalDateTime now() {
		return LocalDateTime.now();
	}
}
