package net.fluance.cockpit.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LightServiceDescription {
    
    private String code;
    private String codeDescription;
    @JsonProperty("nbPatients")
    private Integer numberOfPatients;
    
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeDescription() {
		return codeDescription;
	}
	public void setCodeDescription(String codeDescription) {
		this.codeDescription = codeDescription;
	}
	public Integer getNumberOfPatients() {
		return numberOfPatients;
	}
	public void setNumberOfPatients(Integer numberOfPatients) {
		this.numberOfPatients = numberOfPatients;
	}
}