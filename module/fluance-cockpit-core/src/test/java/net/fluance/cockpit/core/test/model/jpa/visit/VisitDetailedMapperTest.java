package net.fluance.cockpit.core.test.model.jpa.visit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.fluance.cockpit.core.model.VisitDetailed;
import net.fluance.cockpit.core.model.jpa.visit.VisitDetailedEntity;
import net.fluance.cockpit.core.model.jpa.visit.VisitDetailedMapper;
import net.fluance.cockpit.core.util.visit.VisitDetailedMock;

@RunWith(SpringJUnit4ClassRunner.class)
public class VisitDetailedMapperTest {
	
	@Test
	public void getAllForCompanyAndService_without_limit_should_return() {
		
		VisitDetailedEntity detailedEntity = VisitDetailedMock.generateVisitDetailedEntity();
		
		VisitDetailed visitDetailed = VisitDetailedMapper.toModel(detailedEntity);
		
		assertEntityToModel(detailedEntity, visitDetailed);
	}
	
	private void assertEntityToModel(VisitDetailedEntity detailedEntity, VisitDetailed visitDetailed) {
		assertNotNull(visitDetailed);
		assertNotNull(visitDetailed.getPatient());
		assertNotNull(visitDetailed.getCompany());
		
		assertEquals(detailedEntity.getVisitNumber(), visitDetailed.getVisitNb());
		assertEquals(detailedEntity.getHl7Code(), visitDetailed.getHl7code());
		assertEquals(detailedEntity.getVisitDetail().getAdmitSource(), visitDetailed.getAdmitSource());
		assertEquals(detailedEntity.getPatientId(), visitDetailed.getPatient().getPid());
		assertEquals(detailedEntity.getCompany().getCode(), visitDetailed.getCompany().getCode());
	}
}

