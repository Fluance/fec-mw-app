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
import net.fluance.cockpit.core.model.jdbc.patient.PatientNextOfKin;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientNextOfKinRepository;
import net.fluance.cockpit.core.test.Application;
import net.fluance.cockpit.core.test.PatientNokTestConfig;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class PatientNextOfKinRepositoryTest extends AbstractTest {

	@Autowired
	private PatientNokTestConfig patientNokTestConfig;
	@Autowired
	private PatientNextOfKinRepository patientNextOfKinRepository;

	@Test
	@Ignore("Test does not compile")
	public void mustListNextOfKinsTest() {
		Long patientId = Long.valueOf(patientNokTestConfig.getExpectedNokPatientId());
		List<PatientNextOfKin> patientsNoks = patientNextOfKinRepository.findByPid(patientId);
		assertNotNull(patientsNoks);
		assertEquals(Integer.parseInt(patientNokTestConfig.getExpectedNokCount()), patientsNoks.size());
		PatientNextOfKin expectedPatientNok = patientsNoks.get(0);
		checkAgainstExpected(expectedPatientNok);
	}

	private void checkAgainstExpected(PatientNextOfKin patientNok) {
		assertEquals(Integer.valueOf(patientNokTestConfig.getExpectedNokCount()), patientNok.getNbRecords());
		assertEquals(Long.valueOf(patientNokTestConfig.getExpectedNokId()), patientNok.getId());
		assertEquals(Long.valueOf(patientNokTestConfig.getExpectedNokPatientId()), patientNok.getPatientId());
		assertTrue(patientNok.getFirstName() == null);
		assertEquals(patientNokTestConfig.getExpectedNokLastName(), patientNok.getLastName());
		assertEquals(patientNokTestConfig.getExpectedNokAddress(), patientNok.getAddress());
		assertTrue(patientNok.getAddress2() == null);
		assertEquals(patientNokTestConfig.getExpectedNokLocality(), patientNok.getLocality());
		assertEquals(patientNokTestConfig.getExpectedNokPostCode(), patientNok.getPostCode());
		assertEquals(patientNokTestConfig.getExpectedNokCanton(), patientNok.getCanton());
		assertTrue(patientNok.getCareOf() == null);
		assertEquals(patientNokTestConfig.getExpectedNokCountry(), patientNok.getCountry());
		assertEquals(patientNokTestConfig.getExpectedNokType(), patientNok.getType());
		assertTrue(patientNok.getAddressType() == null);
		assertTrue(patientNok.getComplement() == null);
	}
}
