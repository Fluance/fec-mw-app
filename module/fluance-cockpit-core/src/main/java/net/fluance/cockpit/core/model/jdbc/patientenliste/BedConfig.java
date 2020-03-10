package net.fluance.cockpit.core.model.jdbc.patientenliste;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BedConfig implements Persistable<Integer> {

	private int companyId;
	private String unit;
	private String room;
	private String bed;
	private int order;

	public BedConfig(int companyId, String unit, String room, String bed, int order) {
		this.companyId = companyId;
		this.unit = unit;
		this.room = room;
		this.bed = bed;
		this.order = order;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getBed() {
		return bed;
	}

	public void setBed(String bed) {
		this.bed = bed;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	@JsonIgnore
	public Integer getId() {
		return null;
	}

	@Override
	@JsonIgnore
	public boolean isNew() {
		return false;
	}
}
