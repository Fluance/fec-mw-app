package net.fluance.cockpit.app.service.domain;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PatientServiceTests {
PatientService patientService;
	
	@Before
	public void init() {
		patientService = new PatientService();
	}
	
	@Test
	public void getPatientList() {
		List<Long> patients = patientService.getPatientIds("1,2");
		Assert.assertTrue("Is not null?", patients != null); 
		Assert.assertTrue("Has 2 entries?", patients.size() == 2); 
	}
	
	@Test
	public void getPatientList_wrong_input() {
		List<Long> patients = patientService.getPatientIds("aaaaa");
		Assert.assertTrue("Is not null?", patients != null); 
		Assert.assertTrue("Has 2 entries?", patients.size() == 0); 
	}
	
	@Test
	public void getPatientList_one_wrong_input() {
		List<Long> patients = patientService.getPatientIds("1,2,aaaaa");
		Assert.assertTrue("Is not null?", patients != null); 
		Assert.assertTrue("Has 2 entries?", patients.size() == 2); 
	}
	
	@Test
	public void getPatientList_empty() {
		List<Long> patients = patientService.getPatientIds("");
		Assert.assertTrue("Is not null?", patients != null); 
		Assert.assertTrue("Has 0 entries?", patients.size() == 0); 
	}
	
	@Test
	public void getPatientList_null() {
		List<Long> patients = patientService.getPatientIds(null);
		Assert.assertTrue("Is not null?", patients != null); 
		Assert.assertTrue("Has 0 entries?", patients.size() == 0); 
	}
	
	@Test
	public void getPatientList_no_multiple_visit_ids() {
		List<Long> patients = patientService.getPatientIds("1,2,2,3,1");
		Assert.assertTrue("Is not null?", patients != null); 
		Assert.assertTrue("Has 0 entries?", patients.size() == 3); 
	}
}
