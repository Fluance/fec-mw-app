package net.fluance.cockpit.core.model.jpa.whiteboard;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WhiteboardRoomsConfigurationDTO implements Serializable {

	private String companyCode;
	private String hospservice;
	private String room;
	private boolean display;
	private String roomType;
	private Boolean mergePrevious;
	private Boolean mergeNext;

	public WhiteboardRoomsConfigurationDTO() {}

	public WhiteboardRoomsConfigurationDTO(WhiteboardRoomsConfigurationEntity whiteboardRoomsConfigurationEntity) {
		this.companyCode = whiteboardRoomsConfigurationEntity.getWhiteboardRoomsConfigurationID().getCompanyCode();
		this.hospservice = whiteboardRoomsConfigurationEntity.getWhiteboardRoomsConfigurationID().getHospservice();
		this.room = whiteboardRoomsConfigurationEntity.getWhiteboardRoomsConfigurationID().getRoom();
		this.display = whiteboardRoomsConfigurationEntity.isDisplay();
		this.roomType = whiteboardRoomsConfigurationEntity.getRoomType();
		this.mergePrevious = whiteboardRoomsConfigurationEntity.isMergePrevious();
		this.mergeNext = whiteboardRoomsConfigurationEntity.isMergeNext();
	}

	@JsonProperty("display")
	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	@JsonProperty("mergePrevious")
	public Boolean isMergePrevious() {
		return mergePrevious;
	}

	public void setMergePrevious(Boolean mergePrevious) {
		this.mergePrevious = mergePrevious;
	}

	@JsonProperty("mergeNext")
	public Boolean isMergeNext() {
		return mergeNext;
	}

	public void setMergeNext(Boolean mergeNext) {
		this.mergeNext = mergeNext;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getHospservice() {
		return hospservice;
	}

	public void setHospservice(String hospservice) {
		this.hospservice = hospservice;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}
}
