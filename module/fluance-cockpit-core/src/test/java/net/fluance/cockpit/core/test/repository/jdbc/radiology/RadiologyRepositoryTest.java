package net.fluance.cockpit.core.test.repository.jdbc.radiology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.radiology.Radiology;
import net.fluance.cockpit.core.repository.jdbc.radiology.RadiologyRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class RadiologyRepositoryTest extends AbstractTest {

	@Autowired
	private RadiologyRepository radioRepo;
	
	private Long patientId = (long)107998;

	@Value("${net.fluance.cockpit.core.model.repository.radiology.expectedSerieUid}")
	private String expectedSerieUid;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.expectedPatientId}")
	private Long expectedPatientId;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.expectedCompanyId}")
	private Integer expectedCompanyId;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.expectedOrderNb}")
	private String expectedOrderNb;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.expectedOrderObs}")
	private String expectedOrderObs;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.expectedOrderUrl}")
	private String expectedOrderUrl;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.expectedDiagnosticService}")
	private String expectedDiagnosticService;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.expectedDiagnosticServiceDescription}")
	private String expectedDiagnosticServiceDescription;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.expectedSerieObservation}")
	private String expectedSerieObservation;
	@Value("${net.fluance.cockpit.core.model.repository.radiology.expectedserieObservationDate}")
	private Date expectedserieObservationDate;

	@Test
	@Ignore("Test does not compile")
	public void testRadiologyByPid() {
		List<Radiology> radiologies = radioRepo.findByPatientId(patientId);
		assertNotNull(radiologies);
		assertEquals(radiologies.get(0).getCompanyId(), expectedCompanyId);
		assertEquals(radiologies.get(0).getDiagnosticService(), expectedDiagnosticService);
		assertEquals(radiologies.get(0).getDsDescription(), expectedDiagnosticServiceDescription);
		assertEquals(radiologies.get(0).getOrderNb(), expectedOrderNb);
		assertEquals(radiologies.get(0).getOrderObs(), expectedOrderObs);
		assertEquals(radiologies.get(0).getOrderUrl(), expectedOrderUrl);
		assertEquals(radiologies.get(0).getPatientId(), expectedPatientId);
		assertEquals(radiologies.get(0).getSerieObs(), expectedSerieObservation);
		assertEquals(radiologies.get(0).getSerieObsDate(), expectedserieObservationDate);
		assertEquals(radiologies.get(0).getSerieUid(), expectedSerieUid);
	}


}