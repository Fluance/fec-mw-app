package net.fluance.cockpit.app.service.domain.model;

import java.util.List;

public class RoomOperationsByStatus {	
	private String roomName;
	private List<Long> appointmentsInProgress;
	private Long operationLive;
	private Long upcomingAppointment;
	
	public String getRoomName() {
		return roomName;
	}
	
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	public List<Long> getAppointmentsInProgress() {
		return appointmentsInProgress;
	}
	
	public void setAppointmentsInProgress(List<Long> appointmentsInProgress) {
		this.appointmentsInProgress = appointmentsInProgress;
	}
	
	public Long getOperationLive() {
		return operationLive;
	}
	
	public void setOperationLive(Long operationLive) {
		this.operationLive = operationLive;
	}
	
	public Long getUpcomingAppointment() {
		return upcomingAppointment;
	}
	
	public void setUpcomingAppointment(Long upcomingAppointment) {
		this.upcomingAppointment = upcomingAppointment;
	}
	
	

}
