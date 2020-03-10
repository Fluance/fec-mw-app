package net.fluance.cockpit.core.test.repository.jdbc.guarantor;

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
import net.fluance.cockpit.core.model.jdbc.guarantor.GuarantorDetail;
import net.fluance.cockpit.core.repository.jdbc.guarantor.GuarantorRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class GuarantorRepositoryTest extends AbstractTest {

	@Autowired
	private GuarantorRepository guarantorRepo;

	@Value("${net.fluance.cockpit.core.model.repository.guarantor.id}")
	private Integer id;
	@Value("${net.fluance.cockpit.core.model.repository.guarantor.address}")
	private String address;
	@Value("${net.fluance.cockpit.core.model.repository.guarantor.name}")
	private String name;

	@Test
	@Ignore("Test does not compile")
	public void testIfGuarantorExists() {
		GuarantorDetail guarantor = guarantorRepo.findOne(id);
		assertNotNull(guarantor);
		assertEquals(name, guarantor.getName());
		assertEquals(address, guarantor.getAddress());
	}

}