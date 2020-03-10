package net.fluance.cockpit.core.test.repository.jdbc.physician;

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
import net.fluance.cockpit.core.model.jdbc.physician.PhysicianList;
import net.fluance.cockpit.core.repository.jdbc.physician.PhysicianListRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class PhysicianListRepositoryTest extends AbstractTest {

	@Autowired
	private PhysicianListRepository physicianListRepository;
	
	// EXPECTED VALUES

	@Value("${net.fluance.cockpit.core.model.repository.physician.expectedAttendingId}")
	private Integer expectedAttendingId;
	@Value("${net.fluance.cockpit.core.model.repository.physician.expectedAttendingFirstname}")
	private String expectedAttendingFirstname;
	@Value("${net.fluance.cockpit.core.model.repository.physician.expectedAttendingLastname}")
	private String expectedAttendingLastname;
	@Value("${net.fluance.cockpit.core.model.repository.physician.expectedAttendingStaffid}")
	private Integer expectedAttendingStaffid;
	@Value("${net.fluance.cockpit.core.model.repository.physician.expectedReferringId}")
	private Integer expectedReferringId;
	@Value("${net.fluance.cockpit.core.model.repository.physician.expectedReferringFirstname}")
	private String expectedReferringFirstname;
	@Value("${net.fluance.cockpit.core.model.repository.physician.expectedReferringLastname}")
	private String expectedReferringLastname;
	@Value("${net.fluance.cockpit.core.model.repository.physician.expectedReferringStaffid}")
	private Integer expectedReferringStaffid;
	@Value("${net.fluance.cockpit.core.model.repository.physician.expectedConsultingId}")
	private Integer expectedConsultingId;
	@Value("${net.fluance.cockpit.core.model.repository.physician.expectedConsultingFirstname}")
	private String expectedConsultingFirstname;
	@Value("${net.fluance.cockpit.core.model.repository.physician.expectedConsultingLastname}")
	private String expectedConsultingLastname;
	@Value("${net.fluance.cockpit.core.model.repository.physician.expectedConsultingStaffId}")
	private Integer expectedConsultingStaffId;
	@Value("${net.fluance.cockpit.core.model.repository.physician.expectedAdmittingId}")
	private Integer expectedAdmittingId;
	@Value("${net.fluance.cockpit.core.model.repository.physician.expectedAdmittingFirstname}")
	private String expectedAdmittingFirstname;
	@Value("${net.fluance.cockpit.core.model.repository.physician.expectedAdmittingLastname}")
	private String expectedAdmittingLastname;
	@Value("${net.fluance.cockpit.core.model.repository.physician.expectedAdmittingStaffId}")
	private Integer expectedAdmittingStaffId;

	@Test
	@Ignore("Test does not compile")
	public void testGetPhysicianListListByVisitNb() {

		List<PhysicianList> PhysicianLists = physicianListRepository.findPhysicianByVisitnb(3058264);
		assertNotNull(PhysicianLists);
		assertTrue(PhysicianLists.size() > 0);
		assertEquals(PhysicianLists.get(0).getAttendingPhysicianId(), expectedAttendingId);
		assertEquals(PhysicianLists.get(0).getAttendingPhysicianFirstname(), expectedAttendingFirstname);
		assertEquals(PhysicianLists.get(0).getAttendingPhysicianLastname(), expectedAttendingLastname);
		assertEquals(PhysicianLists.get(0).getAttendingPhysicianStaffid(), expectedAttendingStaffid);
		assertEquals(PhysicianLists.get(0).getReferringPhysicianId(), expectedReferringId);
		assertEquals(PhysicianLists.get(0).getReferringPhysicianFirstname(), expectedReferringFirstname);
		assertEquals(PhysicianLists.get(0).getReferringPhysicianLastname(), expectedReferringLastname);
		assertEquals(PhysicianLists.get(0).getReferringPhysicianStaffid(), expectedReferringStaffid);
		assertEquals(PhysicianLists.get(0).getConsultingPhysicianId(), expectedConsultingId);
		assertEquals(PhysicianLists.get(0).getConsultingPhysicianFirstname(), expectedConsultingFirstname);
		assertEquals(PhysicianLists.get(0).getConsultingPhysicianLastname(), expectedConsultingLastname);
		assertEquals(PhysicianLists.get(0).getConsultingPhysicianStaffid(), expectedConsultingStaffId);
		assertEquals(PhysicianLists.get(0).getAdmittingPhysicianId(), expectedAdmittingId);
		assertEquals(PhysicianLists.get(0).getAdmittingPhysicianFirstname(), expectedAdmittingFirstname);
		assertEquals(PhysicianLists.get(0).getAdmittingPhysicianLastname(), expectedAdmittingLastname);
		assertEquals(PhysicianLists.get(0).getAdmittingPhysicianStaffid(), expectedAdmittingStaffId);
		//
		//		// test PhysicianList list not null when no PhysicianLists for visitNb
		PhysicianLists = physicianListRepository.findPhysicianByVisitnb(2);
		assertNotNull(PhysicianLists);
		assertTrue(PhysicianLists.size() == 0);
	}

}
