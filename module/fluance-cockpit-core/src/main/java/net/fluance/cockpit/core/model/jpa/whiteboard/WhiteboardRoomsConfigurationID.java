package net.fluance.cockpit.core.model.jpa.whiteboard;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class WhiteboardRoomsConfigurationID implements Serializable {

	@Column(name = "company_code", nullable = false)
	private String companyCode;
	@Column(name = "hospservice", nullable = false)
	private String hospservice;
	@Column(name = "room", nullable = false)
	private String room;

	public WhiteboardRoomsConfigurationID() {}

	public WhiteboardRoomsConfigurationID(String companyCode, String hospservice, String room) {
		this.companyCode = companyCode;
		this.hospservice = hospservice;
		this.room = room;
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
