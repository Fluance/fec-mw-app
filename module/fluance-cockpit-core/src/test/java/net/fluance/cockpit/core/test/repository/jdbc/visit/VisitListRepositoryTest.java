package net.fluance.cockpit.core.test.repository.jdbc.visit;

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
import net.fluance.cockpit.core.model.jdbc.visit.VisitList;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitListRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class VisitListRepositoryTest extends AbstractTest {

	@Autowired
	private VisitListRepository visitListReponseRepository;

	// INPUTS PARAMS
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.patientId}")
	private Integer patientId;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.companyId}")
	private Integer companyId;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.patientIdWithoutVisits}")
	private Integer patientIdWithoutVisits;

	// EXPECTED VALUES
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedSize}")
	private int expectedSize;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedNbRecords}")
	private int expectedNbRecords;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedNb}")
	private Integer expectedNb;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedPatientClass}")
	private String expectedPatientClass;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedPatientUnit}")
	private String expectedPatientUnit;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedPatientCase}")
	private String expectedPatientCase;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedAdmissionType}")
	private String expectedAdmissionType;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedFinancialClass}")
	private String expectedFinancialClass;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedHospService}")
	private String expectedHospService;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedPatientType}")
	private String expectedPatientType;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedAdmitDt}")
	private String expectedAdmitDt;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedDischargeDt}")
	private String expectedDischargeDt;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedPatientClassDesc}")
	private String expectedPatientClassDesc;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedPatientTypeDesc}")
	private String expectedPatientTypeDesc;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedPatientCaseDesc}")
	private String expectedPatientCaseDesc;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedAdmissionTypeDesc}")
	private String expectedAdmissionTypeDesc;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedPatientUnitDesc}")
	private String expectedPatientUnitDesc;
	@Value("${net.fluance.cockpit.core.model.repository.visitListResponse.expectedHospServiceDesc}")
	private String expectedHospServiceDesc;

	@Test
	@Ignore("Test does not compile")
	public void testFindByNb() {
		List<VisitList> visitListResponses = visitListReponseRepository.findByCompanyIdAndPatientId(companyId, patientId, false, null, null, null, null);
		assertNotNull(visitListResponses);
		assertEquals(expectedSize, visitListResponses.size());
		assertEquals(expectedNbRecords, visitListResponses.get(0).getNb_records());
		assertEquals(expectedNb, visitListResponses.get(0).getVisitInfo().getNb());
		assertEquals(expectedAdmitDt, visitListResponses.get(0).getVisitInfo().getAdmitDate());
		assertEquals(expectedDischargeDt, visitListResponses.get(0).getVisitInfo().getDischargeDate());
		assertEquals(expectedPatientClass, visitListResponses.get(0).getVisitInfo().getPatientclass());
		assertEquals(expectedPatientType, visitListResponses.get(0).getVisitInfo().getPatienttype());
		assertEquals(expectedPatientCase, visitListResponses.get(0).getVisitInfo().getPatientcase());
		assertEquals(expectedHospService, visitListResponses.get(0).getVisitInfo().getHospservice());
		assertEquals(expectedAdmissionType, visitListResponses.get(0).getVisitInfo().getAdmissiontype());
		assertEquals(expectedPatientUnit, visitListResponses.get(0).getVisitInfo().getPatientunit());
		assertEquals(expectedPatientClassDesc, visitListResponses.get(0).getVisitInfo().getPatientclassdesc());
		assertEquals(expectedPatientTypeDesc, visitListResponses.get(0).getVisitInfo().getPatienttypedesc());
		assertEquals(expectedPatientCaseDesc, visitListResponses.get(0).getVisitInfo().getPatientcasedesc());
		assertEquals(expectedAdmissionTypeDesc, visitListResponses.get(0).getVisitInfo().getAdmissiontypedesc());
		assertEquals(expectedPatientUnitDesc, visitListResponses.get(0).getVisitInfo().getPatientunitdesc());
		assertEquals("", visitListResponses.get(0).getVisitInfo().getFinancialclass());
		assertEquals("", visitListResponses.get(0).getVisitInfo().getFinancialclassdesc());
	}
	
	@Test
	@Ignore("Test does not compile")
	public void testWhenResultEmpty(){
		List<VisitList> visitListResponses = visitListReponseRepository.findByCompanyIdAndPatientId(companyId, patientIdWithoutVisits, false, null, null, null, null);
		assertNotNull(visitListResponses);
		assertEquals(0, visitListResponses.size());
	}
}
