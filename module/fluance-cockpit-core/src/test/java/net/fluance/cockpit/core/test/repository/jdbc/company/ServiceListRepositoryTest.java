package net.fluance.cockpit.core.test.repository.jdbc.company;

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
import net.fluance.cockpit.core.model.jdbc.company.ServiceList;
import net.fluance.cockpit.core.repository.jdbc.company.ServiceListRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class ServiceListRepositoryTest extends AbstractTest {

	@Autowired
	private ServiceListRepository serviceListRepo;

	@Value("${net.fluance.cockpit.core.model.repository.company.hospservice}")
	private String hospservice;

	@Value("${net.fluance.cockpit.core.model.repository.company.hospservicedesc}")
	private String hospservicedesc;

	@Value("${net.fluance.cockpit.core.model.repository.company.id}")
	private int id;
	@Value("${net.fluance.cockpit.core.model.repository.company.patientunit}")
	private String patientunit;

	@Test
	@Ignore("Test does not compile")
	public void testGetCompanySimple() {
		List<ServiceList> lServiceList = serviceListRepo.findByCompanyId(id);
		assertNotNull(lServiceList);
		assertEquals(hospservice, lServiceList.get(0).getHospService());
		assertEquals(hospservicedesc, lServiceList.get(0).getHospServiceDesc());
	}

}