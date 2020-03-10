package net.fluance.cockpit.core.test.repository.jdbc.radiology;

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
import net.fluance.cockpit.core.model.jdbc.radiology.RadiologyReport;
import net.fluance.cockpit.core.repository.jdbc.radiology.RadiologyReportRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class RadiologyReportRepositoryTest extends AbstractTest {

	@Autowired
	private RadiologyReportRepository radioRepo;
	
	private Long patientId = (long)107998;

	@Value("${net.fluance.cockpit.core.model.repository.radiology.report.expectedStudyUid}")
	private String expectedStudyUid;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.report.expectedPatientId}")
	private Long expectedPatientId;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.report.expectedCompanyId}")
	private Integer expectedCompanyId;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.report.expectedOrderNb}")
	private String expectedOrderNb;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.report.expectedStudyDt}")
	private String expectedStudyDt;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.report.expectedReport}")
	private String expectedReport;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.report.expectedCompletion}")
	private String expectedCompletion;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.report.expectedVerification}")
	private String expectedVerification;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.report.expectedReferringPhysician}")
	private String expectedReferringPhysician;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.report.expectedRecordPhysician}")
	private String expectedRecordPhysician;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.report.expectedPerformingPhysician}")
	private String expectedPerformingPhysician;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.report.expectedReadingPhysician}")
	private String expectedReadingPhysician;

	@Test
	@Ignore("Test does not compile")
	public void testRadiologyReportByPid() {
		List<RadiologyReport> radiologies = radioRepo.findByPatientId(patientId, null, null, 10, 0);
		assertNotNull(radiologies);
		assertEquals(radiologies.get(0).getCompanyId(), expectedCompanyId);
		assertEquals(radiologies.get(0).getCompletion(), expectedCompletion);
		assertEquals(radiologies.get(0).getOrderNb(), expectedOrderNb);
		assertEquals(radiologies.get(0).getPatientId(), expectedPatientId);
		assertEquals(radiologies.get(0).getPerformingPhysician(), expectedPerformingPhysician);
		assertEquals(radiologies.get(0).getReadingPhysician(), expectedReadingPhysician);
		assertEquals(radiologies.get(0).getRecordPhysician(), expectedRecordPhysician);
		assertEquals(radiologies.get(0).getReferringPhysician(), expectedReferringPhysician);
		assertEquals(radiologies.get(0).getReport(), expectedReport);
		assertEquals(radiologies.get(0).getStudyUid(), expectedStudyUid);
	}


}