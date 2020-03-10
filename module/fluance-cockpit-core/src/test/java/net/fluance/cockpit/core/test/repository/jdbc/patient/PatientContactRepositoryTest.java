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
import net.fluance.cockpit.core.model.jdbc.patient.PatientContact;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientContactRepository;
import net.fluance.cockpit.core.test.Application;
import net.fluance.cockpit.core.test.PatientTestConfig;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class PatientContactRepositoryTest extends AbstractTest {

	@Autowired
	private PatientTestConfig patientTestConfig;
	@Autowired
	private PatientContactRepository patientContactRepository;

	@Test
	@Ignore("Test does not compile")
	public void mustListContactsTest() {
		Long patientId = Long.valueOf(patientTestConfig.getExpectedPatientId());
		List<PatientContact> patientContacts = patientContactRepository.findByPid(patientId);
		assertNotNull(patientContacts);
		assertEquals(Integer.parseInt(patientTestConfig.getExpectedPatientContactsCount()), patientContacts.size());
		PatientContact expectedPatientContact = patientContacts.get(0);
		assertEquals(patientTestConfig.getExpectedPatientContactEquipment(), expectedPatientContact.getEquipment());
		assertEquals(patientTestConfig.getExpectedPatientContactData(), expectedPatientContact.getData());
	}
		
}
