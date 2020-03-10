package net.fluance.cockpit.core.test.repository.jdbc.servicefees;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.servicefees.ServiceFeesDetail;
import net.fluance.cockpit.core.repository.jdbc.servicefees.ServiceFeesDetailRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class BenefitDetailRepositoryTest extends AbstractTest{

	@Autowired
	ServiceFeesDetailRepository repository;

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
		ServiceFeesDetail benefit = repository.findBenefit((long)benefitId, descLanguage);
		assertNotNull(benefit);
	    assertEquals(benefit.getActingDeptDescription(), actingdeptdescription);
	    assertEquals(benefit.getBenefitDate(), benefitDate);
	    assertEquals(benefit.getBenefitId(), benefitId);
	    assertEquals(benefit.getCode(), code);
	    assertEquals(benefit.getDescLanguage(), descLanguage);
	    assertEquals(benefit.getDescription(), description);
	    assertEquals(benefit.getLeadPhysicianFirstName(), leadPhysicianFirstname);
	    assertEquals(benefit.getLeadPhysicianId(), leadPhysicianId);
	    assertEquals(benefit.getLeadPhysicianLastName(), leadPhysicianLastname);
	    assertEquals(benefit.getNote(), note);
	    assertEquals(benefit.getOperatingPhysicianFirstName(), operatingPhysicianFirstname);
	    assertEquals(benefit.getOperatingPhysicianId(), operatingPhysicianId);
	    assertEquals(benefit.getOperatingPhysicianLastName(), operatingPhysicianLastname);
	    assertEquals(benefit.getPaidPhysicianFirstName(), paidPhysicianFirstname);
	    assertEquals(benefit.getPaidPhysicianId(), paidPhysicianId);
	    assertEquals(benefit.getPaidPhysicianLastName(), paidPhysicianLastname);
	    assertEquals(benefit.getQuantity(), quantity);
	    assertEquals(benefit.getSide(), side);
	    assertEquals(benefit.getVisitNb(), visitNb);
	}
}
