package net.fluance.cockpit.core.model.jpa.whiteboard;

import java.util.List;

public class WhiteBoardDTO {
	private Long companyId;
	private String serviceId;
	private List<WhiteBoardViewDTO> entries;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public List<WhiteBoardViewDTO> getEntries() {
		return entries;
	}

	public void setEntries(List<WhiteBoardViewDTO> entries) {
		this.entries = entries;
	}
}
