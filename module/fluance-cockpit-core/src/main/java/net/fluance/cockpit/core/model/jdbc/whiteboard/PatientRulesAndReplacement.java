package net.fluance.cockpit.core.model.jdbc.whiteboard;

import java.util.List;

public class PatientRulesAndReplacement {
	private List<PatientRule> patientRules;	
	private String replacement;
	
	public List<PatientRule> getPatientRules() {
		return patientRules;
	}
	
	public void setPatientRules(List<PatientRule> patientRules) {
		this.patientRules = patientRules;
	}
	
	public String getReplacement() {
		return replacement;
	}
	
	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}
}
