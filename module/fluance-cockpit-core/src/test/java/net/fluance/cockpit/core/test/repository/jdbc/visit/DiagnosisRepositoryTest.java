package net.fluance.cockpit.core.test.repository.jdbc.visit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;


import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.visit.Diagnosis;
import net.fluance.cockpit.core.repository.jdbc.visit.DiagnosisRepository;
import net.fluance.cockpit.core.test.Application;

import org.junit.Ignore;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.visit.Diagnosis;
import net.fluance.cockpit.core.repository.jdbc.visit.DiagnosisRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class DiagnosisRepositoryTest extends AbstractTest{

	@Autowired
	private DiagnosisRepository diagnosisRepository;

	// INPUTS PARAMS
	@Value("${net.fluance.cockpit.core.model.repository.diagnosis.visitNbAssociatedWithSomeDiagnosis}")
	private int visitNbAssociatedWithSomeDiagnosis;
	@Value("${net.fluance.cockpit.core.model.repository.diagnosis.visitNbNotAssociatedWithAnyDiagnosis}")
	private int visitNbNotAssociatedWithAnyDiagnosis;

	// EXPECTED VALUES
	@Value("${net.fluance.cockpit.core.model.repository.diagnosis.expectedSizeFindAll}")
	private int expectedSizeFindAll;
	@Value("${net.fluance.cockpit.core.model.repository.diagnosis.expectedSizeFindByVisitNb}")
	private int expectedSizeFindByVisitNb;
	@Value("${net.fluance.cockpit.core.model.repository.diagnosis.expectedDiagnosisData}")
	private String expectedDiagnosisData;
	@Value("${net.fluance.cockpit.core.model.repository.diagnosis.expectedDiagnosisSequence}")
	private int expectedDiagnosisRank;
	@Value("${net.fluance.cockpit.core.model.repository.diagnosis.expectedDiagnosisType}")
	private String expectedDiagnosisType;
	@Value("${net.fluance.cockpit.core.model.repository.diagnosis.expectedDiagnosisDescription}")
	private String expectedDiagnosisDescription;
	@Value("${net.fluance.cockpit.core.model.repository.diagnosis.expectedDiagnosisDescLanguage}")
	private String expectedDiagnosisDescLanguage;
	@Value("${net.fluance.cockpit.core.model.repository.diagnosis.expectedDiagnosisNbRecords}")
	private int expectedDiagnosisNbRecords;
	
	// COMMON CONFIG
	Integer LIMIT = new Integer(10);
	Integer OFFSET = new Integer(0);

	@Test
	@Ignore("The method on diagnoisRepository changed, findByVisitNb has now a offset")
	public void testGetDiagnosisListByVisitNb() {
		// test diagnosis list not null and not empty when many diagnosis are
		// already associated to visitNb
		List<Diagnosis> diagnosis = diagnosisRepository.findByVisitNb(visitNbAssociatedWithSomeDiagnosis);
		assertNotNull(diagnosis);
		assertTrue(diagnosis.size() > 0);
		assertTrue(diagnosis.size() == expectedSizeFindByVisitNb);
		assertTrue(diagnosis.get(0).getData().equals(expectedDiagnosisData));
		assertTrue(diagnosis.get(0).getRank() == expectedDiagnosisRank);
		assertTrue(diagnosis.get(0).getType().equals(expectedDiagnosisType));
		assertTrue(diagnosis.get(0).getDescription().equals(expectedDiagnosisDescription));
		assertTrue(diagnosis.get(0).getNbRecords() == expectedDiagnosisNbRecords);
		
		// test diagnosis list not null when no diagnosis for visitNb

		///diagnosis = diagnosisRepository.findByVisitNbAndLanguage(visitNbNotAssociatedWithAnyDiagnosis,"de",10);
		diagnosis = diagnosisRepository.findByVisitNbAndLanguage(visitNbNotAssociatedWithAnyDiagnosis,"de",10,0);

		assertNotNull(diagnosis);
		assertTrue(diagnosis.size() == 0);
	}
	
	@Test
	@Ignore("The method on diagnoisRepository changed, findByVisitNb has now a offset")
	public void testGetDiagnosisListByVisitNbAndLanguage() {
		// test diagnosis list not null and not empty when many diagnosis are
		// already associated to visitNb and the language of the diagnosis description

		//List<Diagnosis> diagnosis = diagnosisRepository.findByVisitNbAndLanguage(visitNbAssociatedWithSomeDiagnosis,"de",10);
		List<Diagnosis> diagnosis = diagnosisRepository.findByVisitNbAndLanguage(visitNbAssociatedWithSomeDiagnosis,"de",10,0);

		assertNotNull(diagnosis);
		assertTrue(diagnosis.size() > 0);
		assertTrue(diagnosis.size() == expectedSizeFindByVisitNb);
		assertTrue(diagnosis.get(0).getData().equals(expectedDiagnosisData));
		assertTrue(diagnosis.get(0).getRank() == expectedDiagnosisRank);
		assertTrue(diagnosis.get(0).getType().equals(expectedDiagnosisType));
		assertTrue(diagnosis.get(0).getDescription().equals(expectedDiagnosisDescription));
		assertTrue(diagnosis.get(0).getNbRecords() == expectedDiagnosisNbRecords);
		assertTrue(diagnosis.get(0).getDescLanguage() == expectedDiagnosisDescLanguage);
		
		// test diagnosis list not null when no diagnosis for visitNb
		diagnosis = diagnosisRepository.findByVisitNb(visitNbNotAssociatedWithAnyDiagnosis);
		assertNotNull(diagnosis);
		assertTrue(diagnosis.size() == 0);
	}
	
	@Test
	@Ignore("The method on diagnoisRepository changed, findByVisitNb has now a offset")
	public void testGetDiagnosisListByVisitNbAndLimit() {
		// test diagnosis list not null and not empty when many diagnosis are
		// already associated to visitNb

		List<Diagnosis> diagnosis = diagnosisRepository.findByVisitNb(visitNbAssociatedWithSomeDiagnosis, LIMIT, OFFSET);

		assertNotNull(diagnosis);
		assertTrue(diagnosis.size() > 0);
		assertTrue(diagnosis.size() == LIMIT);
		assertTrue(diagnosis.get(0).getData().equals(expectedDiagnosisData));
		assertTrue(diagnosis.get(0).getRank() == expectedDiagnosisRank);
		assertTrue(diagnosis.get(0).getType().equals(expectedDiagnosisType));
		assertTrue(diagnosis.get(0).getDescription().equals(expectedDiagnosisDescription));
		assertTrue(diagnosis.get(0).getNbRecords() == expectedDiagnosisNbRecords);
		
		// test diagnosis list not null when no diagnosis for visitNb

		//diagnosis = diagnosisRepository.findByVisitNb(visitNbNotAssociatedWithAnyDiagnosis, limit);

		diagnosis = diagnosisRepository.findByVisitNb(visitNbNotAssociatedWithAnyDiagnosis, LIMIT, OFFSET);
		assertNotNull(diagnosis);
		assertTrue(diagnosis.size() == 0);
	}
}
