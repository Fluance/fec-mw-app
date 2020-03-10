package net.fluance.cockpit.core.test.repository.jdbc.patient;

import static org.junit.Assert.*;

import java.sql.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.patient.Patient;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientRepository;
import net.fluance.cockpit.core.test.Application;
import net.fluance.cockpit.core.test.PatientTestConfig;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class PatientRepositoryTest extends AbstractTest {

	@Autowired
	private PatientTestConfig patientTestConfig;
	@Autowired
	private PatientRepository patientRepository;

	@Test
	@Ignore("Test does not compile")
	public void mustFindDetailTest() {
		Long patientId = Long.valueOf(patientTestConfig.getExpectedPatientId());
		Patient foundPatient = patientRepository.findOne(patientId);
		assertNotNull(foundPatient);
		checkAgainstExpected(foundPatient);
	}
	
	private void checkAgainstExpected(Patient patient) {
		assertEquals(Long.valueOf(patientTestConfig.getExpectedPatientId()), patient.getId());
		assertEquals(patientTestConfig.getExpectedPatientFirstName(), patient.getFirstName());
		assertEquals(patientTestConfig.getExpectedPatientLastName(), patient.getLastName());
		assertEquals(patientTestConfig.getExpectedPatientMaidenName(), patient.getMaidenName());
		assertEquals(Date.valueOf(patientTestConfig.getExpectedPatientDateOfBirth()), patient.getBirthDate());
		assertEquals(patientTestConfig.getExpectedPatientSex(), patient.getSex());
		assertEquals(patientTestConfig.getExpectedPatientAvsNumber(), patient.getAvsNumber());
		assertEquals(patientTestConfig.getExpectedPatientLanguage(), patient.getLanguage());
		assertEquals(patientTestConfig.getExpectedPatientNationality(), patient.getNationality());
		assertEquals(patientTestConfig.getExpectedPatientAddress(), patient.getAddress());
		assertTrue(patient.getAddress2() == null);
		assertTrue(patient.getCareOf() == null);
		assertTrue(patient.getAdressComplement() == null);
		assertEquals(patientTestConfig.getExpectedPatientLocality(), patient.getLocality());
		assertEquals(patientTestConfig.getExpectedPatientPostCode(), patient.getPostCode());
		assertEquals(patientTestConfig.getExpectedPatientSubPostCode(), patient.getSubPostCode());
		assertEquals(patientTestConfig.getExpectedPatientCanton(), patient.getCanton());
		assertEquals(patientTestConfig.getExpectedPatientCountry(), patient.getCountry());
	}
}
