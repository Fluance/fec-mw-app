package net.fluance.cockpit.core.model.jpa.whiteboard;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.springframework.data.domain.Persistable;

@Entity(name = "room_configuration")
public class WhiteboardRoomsConfigurationEntity implements Persistable<WhiteboardRoomsConfigurationID>, Serializable {

	@EmbeddedId
	private WhiteboardRoomsConfigurationID whiteboardRoomsConfigurationID;
	@Column(name = "display")
	private boolean display;
	@Column(name = "room_type")
	private String roomType;
	@Column(name = "merge_previous")
	private Boolean mergePrevious;
	@Column(name = "merge_next")
	private Boolean mergeNext;

	public WhiteboardRoomsConfigurationEntity(){}
	
	public WhiteboardRoomsConfigurationEntity(WhiteboardRoomsConfigurationDTO whiteboardRoomsConfigurationDTO) {
		WhiteboardRoomsConfigurationID whiteboardRoomsConfigurationID = new WhiteboardRoomsConfigurationID(whiteboardRoomsConfigurationDTO.getCompanyCode(), whiteboardRoomsConfigurationDTO.getHospservice(),
				whiteboardRoomsConfigurationDTO.getRoom());
		this.whiteboardRoomsConfigurationID = whiteboardRoomsConfigurationID;
		this.display = whiteboardRoomsConfigurationDTO.isDisplay();
		this.roomType = whiteboardRoomsConfigurationDTO.getRoomType();
		this.mergePrevious = whiteboardRoomsConfigurationDTO.isMergePrevious();
		this.mergeNext = whiteboardRoomsConfigurationDTO.isMergeNext();
	}

	public WhiteboardRoomsConfigurationID getWhiteboardRoomsConfigurationID() {
		return whiteboardRoomsConfigurationID;
	}

	public void setWhiteboardRoomsConfigurationID(WhiteboardRoomsConfigurationID whiteboardRoomsConfigurationID) {
		this.whiteboardRoomsConfigurationID = whiteboardRoomsConfigurationID;
	}

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

	public Boolean isMergePrevious() {
		return mergePrevious;
	}

	public void setMergePrevious(Boolean mergePrevious) {
		this.mergePrevious = mergePrevious;
	}

	public Boolean isMergeNext() {
		return mergeNext;
	}

	public void setMergeNext(Boolean mergeNext) {
		this.mergeNext = mergeNext;
	}

	@Override
	public WhiteboardRoomsConfigurationID getId() {
		return this.whiteboardRoomsConfigurationID;
	}

	@Override
	public boolean isNew() {
		return false;
	}
}
