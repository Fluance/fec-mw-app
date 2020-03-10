package net.fluance.cockpit.core.test.repository.jdbc.company;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.company.PatientCount;
import net.fluance.cockpit.core.repository.jdbc.company.PatientCountRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class PatientCountRepositoryTest extends AbstractTest {

	@Autowired
	private PatientCountRepository patientCountrepo;

	@Value("${net.fluance.cockpit.core.model.repository.company.hospservicewithdata}")
	private String hospservice;

	@Value("${net.fluance.cockpit.core.model.repository.company.patientroom}")
	private String patientroom;

	@Value("${net.fluance.cockpit.core.model.repository.company.patientunitwithdata}")
	private String patientunit;

	@Value("${net.fluance.cockpit.core.model.repository.company.id}")
	private int idCompany;

	@Value("${net.fluance.cockpit.core.model.repository.company.patientcount}")
	private Integer patientcount;

	@Test
	@Ignore("Test does not compile")
	public void testPatientCountFirstWay() {
		List<PatientCount> c = patientCountrepo.findPatientunitAndCompanyId(patientunit, idCompany);
		assertNotNull(c);
		assertEquals(patientroom, c.get(0).getPatientRoom());
		assertEquals(patientcount, c.get(0).getPatientCount());

	}

	@Test
	@Ignore("Test does not compile")
	public void testPatientCountSecondWay() {
		List<PatientCount> c = patientCountrepo.findByHospserviceAndCompanyId(hospservice, idCompany);
		assertNotNull(c);
		assertEquals(patientroom, c.get(0).getPatientRoom());
		assertEquals(patientcount, c.get(0).getPatientCount());

	}

	@Test
	@Ignore("Test does not compile")
	public void testPatientCountThirdWay() {
		List<PatientCount> c = patientCountrepo.findByPatientunitAndHospserviceAndCompanyId(patientunit, hospservice, idCompany);
		assertNotNull(c);
		assertEquals(patientroom, c.get(0).getPatientRoom());
		assertEquals(patientcount, c.get(0).getPatientCount());

	}

	// @Test
	// public void testAllCompanies() {
	// List<Unit> cList = serviceListRepo.q();
	// assertTrue(cList.size()>0);
	// }

}