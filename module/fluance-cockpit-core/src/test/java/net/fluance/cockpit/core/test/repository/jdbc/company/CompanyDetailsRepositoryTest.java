package net.fluance.cockpit.core.test.repository.jdbc.company;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import net.fluance.cockpit.core.test.Application;
import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.company.CompanyDetails;
import net.fluance.cockpit.core.repository.jdbc.company.CompanyDetailsRepository;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class CompanyDetailsRepositoryTest extends AbstractTest {

	@Autowired
	private CompanyDetailsRepository companyRepo;

	private List<String> expectedPossibleLangues;

	@Value("${net.fluance.cockpit.core.model.repository.company.langs}")
	private String expectedPossibleLanguesProp;

	@Value("${net.fluance.cockpit.core.model.repository.company.preflang}")
	private String preflang;

	@Value("${net.fluance.cockpit.core.model.repository.company.country}")
	private String country;

	@Value("${net.fluance.cockpit.core.model.repository.company.canton}")
	private String canton;

	@Value("${net.fluance.cockpit.core.model.repository.company.postcode}")
	private String postcode;

	@Value("${net.fluance.cockpit.core.model.repository.company.locality}")
	private String locality;

	@Value("${net.fluance.cockpit.core.model.repository.company.email}")
	private String email;

	@Value("${net.fluance.cockpit.core.model.repository.company.code}")
	private String code;

	@Value("${net.fluance.cockpit.core.model.repository.company.name}")
	private String name;

	@Value("${net.fluance.cockpit.core.model.repository.company.id}")
	private int id;

	@Test
	@Ignore("Test does not compile")
	public void testGetCompanySimple() {
		CompanyDetails companyDetails = companyRepo.findOne(id);
		assertNotNull(companyDetails);
		assertNotNull(companyDetails.getId());
		assertNotNull(companyDetails.getName());
		assertNotNull(companyDetails.getCode());
		assertEquals(code, companyDetails.getCode());
		assertEquals(name, companyDetails.getName());
		assertEquals(country, companyDetails.getCountry());
		assertEquals(canton, companyDetails.getCanton());
		assertEquals(locality, companyDetails.getLocality());
		assertEquals(postcode, companyDetails.getPostcode());
		expectedPossibleLangues = Arrays.asList(expectedPossibleLanguesProp.split(","));
		assertTrue(expectedPossibleLangues.contains(preflang) == true);
	}

	@Test
	@Ignore("Test does not compile")
	public void testGetNonExistingCompany() {
		CompanyDetails c = companyRepo.findOne(243345);
		assertTrue(c == null);
	}
}