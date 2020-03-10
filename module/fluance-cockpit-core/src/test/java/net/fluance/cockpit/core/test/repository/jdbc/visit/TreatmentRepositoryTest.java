package net.fluance.cockpit.core.test.repository.jdbc.visit;

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
import net.fluance.cockpit.core.model.jdbc.visit.Treatment;
import net.fluance.cockpit.core.repository.jdbc.visit.TreatmentRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class TreatmentRepositoryTest extends AbstractTest {

	@Autowired
	private TreatmentRepository treatmentRepository;

	// INPUTS PARAMS
	@Value("${net.fluance.cockpit.core.model.repository.treatment.visitNbAssociatedWithSomeTreatments}")
	private int visitNbAssociatedWithSomeTreatments;
	@Value("${net.fluance.cockpit.core.model.repository.treatment.visitNbNotAssociatedWithAnyTreatment}")
	private int visitNbNotAssociatedWithAnyTreatment;

	// EXPECTED VALUES
	@Value("${net.fluance.cockpit.core.model.repository.treatment.expectedSizeFindAll}")
	private int expectedSizeFindAll;
	@Value("${net.fluance.cockpit.core.model.repository.treatment.expectedSizeFindByVisitNb}")
	private int expectedSizeFindByVisitNb;
	@Value("${net.fluance.cockpit.core.model.repository.treatment.expectedTreatmentData}")
	private String expectedTreatmentData;
	@Value("${net.fluance.cockpit.core.model.repository.treatment.expectedTreatmentRank}")
	private int expectedTreatmentRank;
	@Value("${net.fluance.cockpit.core.model.repository.treatment.expectedTreatmentType}")
	private String expectedTreatmentType;
	@Value("${net.fluance.cockpit.core.model.repository.treatment.expectedDescLanguage}")
	private String expectedTreatmentDescLanguage;
	@Value("${net.fluance.cockpit.core.model.repository.treatment.expectedTreatmentDescription}")
	private String expectedTreatmentDescription;
	@Value("${net.fluance.cockpit.core.model.repository.treatment.expectedTreatmentNbRecords}")
	private int expectedTreatmentNbRecords;

	// COMMON CONFIG
	Integer LIMIT = new Integer(10);
	Integer OFFSET = new Integer(0);

	@Test
	@Ignore("Test does not compile")
	public void testGetTreatmentListByVisitNb() {
		// test treatment list not null and not empty when many treatments are
		// already associated to visitNb
		List<Treatment> treatments = treatmentRepository.findByVisitNb(visitNbAssociatedWithSomeTreatments);
		assertNotNull(treatments);
		assertTrue(treatments.size() > 0);
		assertTrue(treatments.size() == expectedSizeFindByVisitNb);
		assertTrue(treatments.get(0).getData().equals(expectedTreatmentData));
		assertTrue(treatments.get(0).getRank() == expectedTreatmentRank);
		assertTrue(treatments.get(0).getType().equals(expectedTreatmentType));
		assertTrue(treatments.get(0).getDescription().equals(expectedTreatmentDescription));
		assertTrue(treatments.get(0).getNbRecords() == expectedTreatmentNbRecords);

		// test treatment list not null when no treatments for visitNb
		treatments = treatmentRepository.findByVisitNb(visitNbNotAssociatedWithAnyTreatment);
		assertNotNull(treatments);
		assertTrue(treatments.size() == 0);
	}

}
