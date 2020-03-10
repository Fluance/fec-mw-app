package net.fluance.cockpit.app.service.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.fluance.cockpit.app.web.util.visit.VisitDetailedMock;
import net.fluance.cockpit.core.dao.VisitDetailedDao;
import net.fluance.cockpit.core.model.VisitDetailed;
import net.fluance.cockpit.core.repository.jdbc.visit.GuarantorListRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitDetailRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitListRepository;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitPolicyListRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class VisitServiceTest {
	
	private static final Long HOW_MANY_VISIT_DETAILED = 10L;
	
	@TestConfiguration
	static class VisitServiceTestConfiguration {
		@Bean
		public VisitService visitService() {
			return new VisitService();
		}
	}
	
	@Before
	public void setUp() {
		Mockito.reset(guarantorListRepo);
		Mockito.reset(policyListRepo);
		Mockito.reset(visitListRepo);
		Mockito.reset(visitDetailRepository);
		Mockito.reset(visitDetailedDao);
	}
	
	@MockBean
	private GuarantorListRepository guarantorListRepo;

	@MockBean
	private VisitPolicyListRepository policyListRepo;

	@MockBean
	private VisitListRepository visitListRepo;
	
	@MockBean
	private	VisitDetailRepository visitDetailRepository;
	 
	@MockBean
	private VisitDetailedDao visitDetailedDao;
	
	@Autowired
	private VisitService visitService;
	
	@Test
	public void getVisitByCriteria_should_return() {
		Mockito.when(visitDetailedDao.findAdmitedNowByCompanyIdAndBeds(Mockito.anyInt(), Mockito.anyListOf(String.class), Mockito.anyInt(), Mockito.anyInt())).thenReturn(VisitDetailedMock.generateVisitDetailed(HOW_MANY_VISIT_DETAILED));
		
		List<VisitDetailed> result = visitService.getVisitByCriteria(5, new ArrayList<>(), 10, 0);
		
		assertNotNull("Should return a list of VisitDetailed", result);
		assertEquals("Should return " + HOW_MANY_VISIT_DETAILED + " number of VisitDetailed", HOW_MANY_VISIT_DETAILED.intValue(), result.size());
	}
	
	@Test
	public void getVisitByCriteriaCount_should_return() {
		
		Mockito.when(visitDetailedDao.countAdmitedNowByCompanyIdAndBeds(Mockito.anyInt(), Mockito.anyListOf(String.class))).thenReturn(HOW_MANY_VISIT_DETAILED);
		
		Long result = visitService.getVisitByCriteriaCount(5, new ArrayList<>());
		assertNotNull("Should return a Long", result);
		assertEquals("Should return " + HOW_MANY_VISIT_DETAILED + " as result", HOW_MANY_VISIT_DETAILED, result);
	}

}
