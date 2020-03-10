package net.fluance.cockpit.core.test.repository.jdbc.intervention;

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
import net.fluance.cockpit.core.domain.intervention.Intervention;
import net.fluance.cockpit.core.repository.jdbc.intervention.InterventionRepository;
import net.fluance.cockpit.core.test.Application;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class InterventionRepositoryTest extends AbstractTest{
	
	@Autowired
	InterventionRepository repository;

	@Value("${net.fluance.cockpit.core.model.repository.intervention.visitnb}")
	private Integer visitNb;

	@Value("${net.fluance.cockpit.core.model.repository.intervention.date}")
	private String date;

	@Value("${net.fluance.cockpit.core.model.repository.intervention.data}")
	private String data;

	@Value("${net.fluance.cockpit.core.model.repository.intervention.type}")
	private String type;
	
	@Value("${net.fluance.cockpit.core.model.repository.intervention.rank}")
	private Integer rank;

	@Test
	@Ignore("Test does not compile")
	public void testFindIntervention(){
		List<Intervention> intervention = repository.getByVisitnb((long)visitNb);
		assertNotNull(intervention);
	    assertEquals(intervention.get(0).getInterventionDate(), date);
	    assertEquals(intervention.get(0).getRank(), rank);
	    assertEquals(intervention.get(0).getType(), type);
	    assertEquals(intervention.get(0).getVisitNb(), visitNb);
	    assertEquals(intervention.get(0).getData(), data);
	}
}
