package net.fluance.cockpit.core.test.repository.jdbc.lab;

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

import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.lab.Groupname;
import net.fluance.cockpit.core.repository.jdbc.lab.GroupnameRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class GroupnameRepositoryTest extends AbstractTest {

	@Autowired
	private GroupnameRepository groupNamerepo;

	@Value("${net.fluance.cockpit.core.model.repository.lab.patientid}")
	private long patientid;
	@Value("${net.fluance.cockpit.core.model.repository.lab.allgroupnames}")
	private String allgroupnames;

	private List<String> possibleGroupnames;

	@Test
	@Ignore("Test does not compile")
	public void testIfGroupnameExists() {
		List<Groupname> lGroupnames = groupNamerepo.findByPid(patientid);
		assertNotNull(lGroupnames);

		possibleGroupnames = Arrays.asList(allgroupnames.split(","));
		assertTrue(possibleGroupnames.contains(lGroupnames.get(1).getGroupname()) == true);

	}

	// @Test
	// public void testAllCompanies() {
	// List<Unit> cList = serviceListRepo.q();
	// assertTrue(cList.size()>0);
	// }

}