package net.fluance.cockpit.core.test.repository.jdbc.patient;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.patient.PatientNextOfKinContact;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientNextOfKinContactRepository;
import net.fluance.cockpit.core.test.Application;
import net.fluance.cockpit.core.test.PatientNokTestConfig;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class PatientNextOfKinContactRepositoryTest extends AbstractTest {

	@Autowired
	private PatientNokTestConfig patientNokTestConfig;
	@Autowired
	private PatientNextOfKinContactRepository patientNextOfKinContactRepository;

	@Test
	@Ignore("Test does not compile")
	public void mustListNextOfKinContactsTest() {
		Long nokId = Long.valueOf(patientNokTestConfig.getExpectedNokId());
		List<PatientNextOfKinContact> patientsNokContacts = patientNextOfKinContactRepository.findByNokId(nokId);
		assertNotNull(patientsNokContacts);
		assertEquals(Integer.parseInt(patientNokTestConfig.getExpectedNokContactsCount()), patientsNokContacts.size());
		PatientNextOfKinContact expectedPatientNokContact = patientsNokContacts.get(0);
		assertEquals(patientNokTestConfig.getExpectedNokContactEquipment(), expectedPatientNokContact.getEquipment());
		assertEquals(patientNokTestConfig.getExpectedNokContactData(), expectedPatientNokContact.getData());
	}
		
}
