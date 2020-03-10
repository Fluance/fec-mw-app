package net.fluance.cockpit.core.test.repository.jdbc.visit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.visit.VisitPolicyDetail;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitPolicyDetailRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class VisitPolicyDetailRepositoryTest extends AbstractTest {

	@Autowired
	private VisitPolicyDetailRepository visitPolicyDetailRepository;

	// INPUTS PARAMS
	@Value("${net.fluance.cockpit.core.model.repository.policyList.visitNbAssociatedWithSomePolicies}")
	private Integer visitNb;
	@Value("${net.fluance.cockpit.core.model.repository.policyDetail.guarantorId}")
	private Integer guarantorId;
	@Value("${net.fluance.cockpit.core.model.repository.policyDetail.priority}")
	private Integer priority;
	@Value("${net.fluance.cockpit.core.model.repository.policyDetail.subPriority}")
	private Integer subPriority;


	//EXPECTED_VALUES
	@Value("${net.fluance.cockpit.core.model.repository.policyDetail.expectedName}")
	private String expectedName;
	@Value("${net.fluance.cockpit.core.model.repository.policyDetail.expectedCode}")
	private String expectedCode;
	@Value("${net.fluance.cockpit.core.model.repository.policyDetail.expectedPriority}")
	private Integer expectedPriority;
	@Value("${net.fluance.cockpit.core.model.repository.policyDetail.expectedSubPriority}")
	private Integer expectedSubPriority;
	@Value("${net.fluance.cockpit.core.model.repository.policyDetail.expectedHospclass}")
	private String expectedHospclass;
	@Value("${net.fluance.cockpit.core.model.repository.policyDetail.expectedInactive}")
	private boolean expectedInactive;
	@Value("${net.fluance.cockpit.core.model.repository.policyDetail.expectedPolicyNb}")
	private String expectedPolicyNb;
	@Value("${net.fluance.cockpit.core.model.repository.policyDetail.expectedCoverCardNb}")
	private double expectedCoverCardNb;
	@Value("${net.fluance.cockpit.core.model.repository.policyDetail.expectedAccidentNb}")
	private String expectedAccidentNb;
	@Value("${net.fluance.cockpit.core.model.repository.policyDetail.expectedaccidentDate}")
	private String expectedaccidentDate;
	
	@Test
	@Ignore("Test does not compile")
	public void testFindByNbVisit() {
		VisitPolicyDetail visitPolicy = visitPolicyDetailRepository.findByVisitNbAndGuarantorIdAndPriorityAndSubPriority(visitNb, guarantorId, priority, subPriority);
		assertNotNull(visitPolicy);
		assertTrue("".equals(visitPolicy.getId()));
		assertEquals(expectedName, visitPolicy.getName());
		assertEquals(expectedCode, visitPolicy.getCode());
		assertEquals(expectedPriority, visitPolicy.getPriority());
		assertEquals(expectedSubPriority, visitPolicy.getSubpriority());
		assertEquals(expectedHospclass, visitPolicy.getHospclass());
		assertEquals(expectedInactive, visitPolicy.getInactive());
		assertEquals(expectedPolicyNb, visitPolicy.getPolicynb());
		assertTrue(expectedCoverCardNb == visitPolicy.getCovercardnb());
		assertEquals(expectedAccidentNb, visitPolicy.getAccidentnb());
		assertEquals(expectedaccidentDate, visitPolicy.getAccidentDate());
	}
}
