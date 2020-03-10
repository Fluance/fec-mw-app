package net.fluance.cockpit.core.repository.jdbc.patient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import net.fluance.cockpit.core.model.DoctorReference;
import net.fluance.cockpit.core.model.PatientVisitReference;

@Repository
@Component
public class PatientDoctorReferenceRepository{

	/**
	 * Retrieving the hospitalysed list of patients from the active clinics of the current doctor
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public List<PatientVisitReference> getPatientsForCurrentDoctorByDates(LocalDate from, LocalDate to) {
		
		List<PatientVisitReference> patientsList = new ArrayList<PatientVisitReference>();
		
		putMockedPatientDoctorReference(patientsList);
		
		return patientsList;
	}

	/**
	 * Method to create the mock list
	 * @param patientsList
	 */
	private void putMockedPatientDoctorReference(List<PatientVisitReference> patientsList) {
		for (int i=0; i < 5; i++) {
			PatientVisitReference patientDoctorReference = new PatientVisitReference(Long.valueOf(i), "firstName", "lastName", new Date(), "m");
			patientsList.add(patientDoctorReference);
		}
	}
	
}
