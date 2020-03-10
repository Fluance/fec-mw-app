package net.fluance.cockpit.core.test.repository.jdbc.company;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.company.RoomList;
import net.fluance.cockpit.core.repository.jdbc.company.RoomListRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class PatientRoomRepositoryTest extends AbstractTest {

	@Autowired
	private RoomListRepository patientRoomrepo;

	@Value("${net.fluance.cockpit.core.model.repository.company.hospservice}")
	private String hospservice;

	@Value("${net.fluance.cockpit.core.model.repository.company.patientroom}")
	private String patientroom;

	@Value("${net.fluance.cockpit.core.model.repository.company.patientunit}")
	private String patientunit;

	@Test
	@Ignore("Test does not compile")
	public void testFindByCompanyIdAndPatientunit() {
		List<RoomList> c = patientRoomrepo.findByCompanyIdAndPatientunit(1, patientunit, null);
		assertNotNull(c);
		assertEquals(patientroom, c.get(0).getPatientroom());
	}
	
	@Test
	@Ignore("Test does not compile")
	public void testFindByCompanyIdAndHospservice() {
		List<RoomList> c = patientRoomrepo.findByCompanyIdAndHospservice(1, hospservice, null);
		assertNotNull(c);
		assertEquals(patientroom, c.get(0).getPatientroom());
	}
	
	@Test
	@Ignore("Test does not compile")
	public void testFindByCompanyIdAndPatientunitAndHospservice() {
		List<RoomList> c = patientRoomrepo.findByCompanyIdAndPatientunitAndHospservice(1, patientunit, hospservice, null);
		assertNotNull(c);
		assertEquals(patientroom, c.get(0).getPatientroom());
	}

	// @Test
	// public void testAllCompanies() {
	// List<Unit> cList = serviceListRepo.q();
	// assertTrue(cList.size()>0);
	// }

}