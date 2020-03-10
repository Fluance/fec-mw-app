package net.fluance.cockpit.core.util;

import java.util.Arrays;
import java.util.List;

/**
 * Patient Class hl7code values available in DDBB
 *
 */
public enum PatientClassIOU {
	
	HOSPITALIZED(new String[] {"I"}),
	NOT_HOSPITALIZED_OVERNIGHT(new String[] {"O"}),
    NOT_KNOW(new String[]{"U"});
	
	private List<String> patientClassIOU;

	private PatientClassIOU(String... patientClassIOU) {
        this.patientClassIOU = Arrays.asList(patientClassIOU);
	}
	
	public List<String> getPatientClassIOU() {
		return patientClassIOU;
	}	
}