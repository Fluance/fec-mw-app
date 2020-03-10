package net.fluance.cockpit.core.test.repository.jdbc.visit;

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
import net.fluance.cockpit.core.model.jdbc.visit.GuarantorList;
import net.fluance.cockpit.core.model.jdbc.visit.VisitPolicyList;
import net.fluance.cockpit.core.repository.jdbc.visit.GuarantorListRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitPolicyListRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class VisitPolicyListRepositoryTest extends AbstractTest {

	@Autowired
	private VisitPolicyListRepository policyListRepository;

	// INPUTS PARAMS
	@Value("${net.fluance.cockpit.core.model.repository.policyList.visitNbAssociatedWithSomePolicies}")
	private Integer visitNb;

	// EXPECTED VALUES
	@Value("${net.fluance.cockpit.core.model.repository.policyList.expectedNbRecords}")
	private int expectedNbRecords;
	@Value("${net.fluance.cockpit.core.model.repository.policyList.expectedId}")
	private Integer expectedId;
	@Value("${net.fluance.cockpit.core.model.repository.policyList.code}")
	private String expectedCode;
	@Value("${net.fluance.cockpit.core.model.repository.policyList.name}")
	private String expectedName;
	@Value("${net.fluance.cockpit.core.model.repository.policyList.Priority}")
	private Integer expectedPriority;
	@Value("${net.fluance.cockpit.core.model.repository.policyList.subPriority}")
	private Integer expectedSubPriority;
	@Value("${net.fluance.cockpit.core.model.repository.policyList.expectedHospclass}")
	private String expectedHospclass;


	@Test
	@Ignore("Test does not compile")
	public void testFindByNbVisit() {
		List<VisitPolicyList> policies = policyListRepository.findByVisitNb(visitNb, null, null, null, null);
		assertNotNull(policies);
		assertEquals(expectedNbRecords, policies.size());
		assertEquals(expectedNbRecords, policies.get(0).getNb_records());
		assertTrue(expectedId == policies.get(0).getId());
		assertEquals(expectedCode, policies.get(0).getCode());
		assertEquals(expectedName, policies.get(0).getName());
		assertEquals(expectedPriority, policies.get(0).getPriority());
		assertEquals(expectedSubPriority, policies.get(0).getSubpriority());
		assertEquals(expectedHospclass, policies.get(0).getHospclass());
	}
}
