package net.fluance.cockpit.core.test.repository.jdbc.company;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.company.BedList;
import net.fluance.cockpit.core.repository.jdbc.company.BedListRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class PatientBedRepositoryTest extends AbstractTest {

	@Autowired
	private BedListRepository patientBedrepo;

	@Value("${net.fluance.cockpit.core.model.repository.company.hospservice}")
	private String hospservice;

	@Value("${net.fluance.cockpit.core.model.repository.company.patientroom}")
	private String patientroom;

	@Value("${net.fluance.cockpit.core.model.repository.company.patientunit}")
	private String patientunit;
	
	@Value("${net.fluance.cockpit.core.model.repository.company.id}")
	private int idCompany;

	@Test
	@Ignore("Test does not compile")
	public void testPatientRoomFirstWay() {
		List<BedList> lPatientBed = patientBedrepo.findCompanyIdAndPatientUnitAndPatientroom(idCompany, patientunit, patientroom);
		assertNotNull(lPatientBed);
		assertTrue(lPatientBed.size() > 0);
		assertEquals(patientroom, lPatientBed.get(0).getPatientbed());
		
	}
	
	@Test
	@Ignore("Test does not compile")
	public void testPatientRoomSecondWay() {
		List<BedList> lPatientBed = patientBedrepo.findCompanyIdAndHospserviceAndpatientroom(idCompany, hospservice, patientroom);
		assertNotNull(lPatientBed);
		assertTrue(lPatientBed.size() > 0);
		assertEquals(patientroom, lPatientBed.get(0).getPatientbed());
		
	}
	@Test
	@Ignore("Test does not compile")
	public void testPatientRoomThirdWay() {
		List<BedList> lPatientBed = patientBedrepo.findCompanyIdAndPatientunitAndHospserviceAndPatientroom(idCompany, patientunit, hospservice, patientroom);
		assertNotNull(lPatientBed);
		assertTrue(lPatientBed.size() > 0);
		assertEquals(patientroom, lPatientBed.get(0).getPatientbed());
	}
}