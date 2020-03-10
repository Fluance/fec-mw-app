package net.fluance.cockpit.core.test.repository.jdbc.lab;

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
import net.fluance.cockpit.core.model.jdbc.lab.LabData;
import net.fluance.cockpit.core.repository.jdbc.lab.LabDataRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class LabDataRepositoryTest extends AbstractTest {

	@Autowired
	private LabDataRepository labDataRepo;

	@Value("${net.fluance.cockpit.core.model.repository.lab.patientid}")
	private long patientid;
	@Value("${net.fluance.cockpit.core.model.repository.lab.groupname}")
	private String groupname;
	@Value("${net.fluance.cockpit.core.model.repository.lab.value}")
	private String value;
	@Value("${net.fluance.cockpit.core.model.repository.lab.resultstatus}")
	private String resultstatus;

	@Test
	@Ignore("Test does not compile")
	public void testIfListExists() {
		List<LabData> list_Labdata = labDataRepo.findByPidAndGroupName(patientid, groupname);
		assertNotNull(list_Labdata);
		assertEquals(groupname, list_Labdata.get(0).getGroupName());
		assertEquals(value, list_Labdata.get(0).getValue());
		assertEquals(resultstatus, list_Labdata.get(0).getResultstatus());

	}


}