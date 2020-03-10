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
import net.fluance.cockpit.core.repository.jdbc.visit.GuarantorListRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class GuarantorListRepositoryTest extends AbstractTest {

	@Autowired
	private GuarantorListRepository guagantorListRepo;

	// INPUTS PARAMS
	@Value("${net.fluance.cockpit.core.model.repository.guarantorListResponse.visitNb}")
	private Integer visitNb;

	// EXPECTED VALUES
	@Value("${net.fluance.cockpit.core.model.repository.guarantorListResponse.expectedId}")
	private Integer expectedId;
	@Value("${net.fluance.cockpit.core.model.repository.guarantorListResponse.code}")
	private String expectedCode;
	@Value("${net.fluance.cockpit.core.model.repository.guarantorListResponse.name}")
	private String expectedName;
	@Value("${net.fluance.cockpit.core.model.repository.guarantorListResponse.address}")
	private String expectedAddress;
	@Value("${net.fluance.cockpit.core.model.repository.guarantorListResponse.address2}")
	private String expectedAddress2;
	@Value("${net.fluance.cockpit.core.model.repository.guarantorListResponse.locality}")
	private String expectedLocality;
	@Value("${net.fluance.cockpit.core.model.repository.guarantorListResponse.postcode}")
	private Integer expectedPostCode;
	@Value("${net.fluance.cockpit.core.model.repository.guarantorListResponse.canton}")
	private String expectedCanton;
	@Value("${net.fluance.cockpit.core.model.repository.guarantorListResponse.country}")
	private String expectedCountry;
	@Value("${net.fluance.cockpit.core.model.repository.guarantorListResponse.complement}")
	private String expectedComplement;
	@Value("${net.fluance.cockpit.core.model.repository.guarantorListResponse.begindate}")
	private String expectedBeginDate;
	@Value("${net.fluance.cockpit.core.model.repository.guarantorListResponse.enddate}")
	private String expectedEndDate;
	@Value("${net.fluance.cockpit.core.model.repository.guarantorListResponse.occasional}")
	private boolean expectedOccasional;

	@Test
	@Ignore("Test does not compile")
	public void testFindByNbVisit() {
		List<GuarantorList> guarantors = guagantorListRepo.findByVisitNb(visitNb, null, null, null, null);
		assertNotNull(guarantors);
		assertEquals(2, guarantors.size());
		assertTrue(expectedId == guarantors.get(1).getId());
		assertEquals(expectedCode, guarantors.get(1).getCode());
		assertEquals(expectedName, guarantors.get(1).getName());
		assertEquals(expectedAddress, guarantors.get(1).getAddress());
		assertEquals(expectedAddress2, guarantors.get(1).getAddress2());
		assertEquals(expectedLocality, guarantors.get(1).getLocality());
		assertEquals(expectedPostCode, guarantors.get(1).getPostcode());
		assertEquals(expectedCanton, guarantors.get(1).getCanton());
		assertEquals(expectedCountry, guarantors.get(1).getCountry());
		assertEquals(expectedComplement, guarantors.get(1).getComplement());
		assertEquals(expectedBeginDate, guarantors.get(1).getBegindate());
		assertEquals(expectedEndDate, guarantors.get(1).getEnddate());
		assertEquals(expectedOccasional, guarantors.get(1).getOccasional());
	}

	@Test
	@Ignore("Test does not compile")
	public void testWhenResultEmpty() {
		List<GuarantorList> guarantorListReponses = guagantorListRepo.findByVisitNb(3087767, null, null, null, null);
		assertNotNull(guarantorListReponses);
		assertEquals(0, guarantorListReponses.size());
	}
}
