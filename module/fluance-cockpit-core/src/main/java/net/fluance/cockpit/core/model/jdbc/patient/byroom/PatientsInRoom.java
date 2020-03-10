package net.fluance.cockpit.core.model.jdbc.patient.byroom;

import java.util.Date;

import org.springframework.data.domain.Persistable;

public class PatientsInRoom implements Persistable<Integer> {

	private static final long serialVersionUID = 1L;
	
	Integer roomCount;
	String patientRoom;
	Long patientId;
	String lastName;
	String firstName;
	String maidenName;
	Date birthDate;

	public PatientsInRoom(Integer roomCount, String patientRoom, Long patientId, String lastName, String firstName, String maidenName, Date birthDate) {
		this.roomCount = roomCount;
		this.patientRoom = patientRoom;
		this.patientId = patientId;
		this.lastName = lastName;
		this.firstName = firstName;
		this.maidenName = maidenName;
		this.birthDate = birthDate;
	}

	public Integer getRoomCount() {
		return roomCount;
	}

	public void setRoomCount(Integer roomCount) {
		this.roomCount = roomCount;
	}

	public String getPatientRoom() {
		return patientRoom;
	}

	public void setPatientRoom(String patientRoom) {
		this.patientRoom = patientRoom;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMaidenName() {
		return maidenName;
	}

	public void setMaidenName(String maidenName) {
		this.maidenName = maidenName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@Override
	public Integer getId() {
		return null;
	}

	@Override
	public boolean isNew() {
		return false;
	}
}
