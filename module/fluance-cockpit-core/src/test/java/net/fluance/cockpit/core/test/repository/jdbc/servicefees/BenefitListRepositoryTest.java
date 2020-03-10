package net.fluance.cockpit.core.test.repository.jdbc.servicefees;

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

import net.fluance.cockpit.core.model.jdbc.servicefees.ServiceFeesList;
import net.fluance.cockpit.core.repository.jdbc.servicefees.ServiceFeesListRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class BenefitListRepositoryTest {
	@Autowired
	ServiceFeesListRepository repository;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.nbrecords}")
	private Integer nbRecords;
	
	@Value("${net.fluance.cockpit.core.model.repository.benefit.benefitid}")
	private Integer benefitId;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.visitnb}")
	private Integer visitNb;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.code}")
	private String code;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.benefitdate}")
	private String benefitDate;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.quantity}")
	private String quantity;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.side}")
	private String side;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.actingdeptdescription}")
	private String actingdeptdescription;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.note}")
	private String note;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.description}")
	private String description;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.desclanguage}")
	private String descLanguage;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.paidphysicianid}")
	private Integer paidPhysicianId;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.paidphysicianfirstname}")
	private String paidPhysicianFirstname;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.paidphysicianlastname}")
	private String paidPhysicianLastname;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.operatingphysicianid}")
	private Integer operatingPhysicianId;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.operatingphysicianfirstname}")
	private String operatingPhysicianFirstname;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.operatingphysicianlastname}")
	private String operatingPhysicianLastname;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.leadphysicianid}")
	private Integer leadPhysicianId;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.leadphysicianfirstname}")
	private String leadPhysicianFirstname;

	@Value("${net.fluance.cockpit.core.model.repository.benefit.leadphysicianlastname}")
	private String leadPhysicianLastname;

	@Test
	@Ignore("Test does not compile")
	public void testFindBenefit(){
		List<ServiceFeesList> benefits = repository.findBenefits((long)visitNb, descLanguage, "benefitdt", "desc", 10, 0);
		assertNotNull(benefits);
		ServiceFeesList benefit = benefits.get(0);
		assertEquals(benefit.getNbRecords(), nbRecords);
	    assertEquals(benefit.getBenefitDate(), benefitDate);
	    assertEquals(benefit.getBenefitId(), benefitId);
	    assertEquals(benefit.getCode(), code);
	    assertEquals(benefit.getDescLanguage(), descLanguage);
	    assertEquals(benefit.getDescription(), description);
	    assertEquals(benefit.getNote(), note);
	    assertEquals(benefit.getQuantity(), quantity);
	    assertEquals(benefit.getSide(), side);
	    assertEquals(benefit.getVisitNb(), visitNb);
	}
}
