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
import net.fluance.cockpit.core.model.jdbc.company.Unit;
import net.fluance.cockpit.core.repository.jdbc.company.UnitListRepository;
import net.fluance.cockpit.core.test.Application;


@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class UnitListRepositoryTest extends AbstractTest {

	@Autowired
	private UnitListRepository unitRepo;

	@Value("${net.fluance.cockpit.core.model.repository.company.code}")
	private String code;
	
	@Value("${net.fluance.cockpit.core.model.repository.company.name}")
	private String name;
	
	@Value("${net.fluance.cockpit.core.model.repository.company.id}")
	private int id;
	
	@Value("${net.fluance.cockpit.core.model.repository.company.patientunit}")
	private String patientunit;
	
	@Value("${net.fluance.cockpit.core.model.repository.company.patientunitdesc}")
	private String patientunitdesc;

	@Test
	@Ignore("Test does not compile")
	public void testGetCompanySimple() {
		List<Unit> c = unitRepo.findByCompanyId(1);
		assertNotNull(c);
		assertEquals(patientunit, c.get(0).getPatientunit());
		assertEquals(patientunitdesc,  c.get(0).getCodedesc());
	}

	@Test
	@Ignore("Test does not compile")
	public void testAllCompanies() {
		List<Unit> cList = unitRepo.findAll();
		assertTrue(cList.size()>0);
	}
	

}