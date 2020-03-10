package net.fluance.cockpit.core.dao;

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
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.fluance.cockpit.core.model.VisitDetailed;
import net.fluance.cockpit.core.model.jpa.visit.VisitDetailedEntity;
import net.fluance.cockpit.core.repository.jpa.visit.VisitDetailedRepository;
import net.fluance.cockpit.core.util.visit.VisitDetailedMock;

@RunWith(SpringJUnit4ClassRunner.class)
public class VisitDetailedDaoTest {
	
	@TestConfiguration
	static class VisitDetailedDaoTestConfiguration {
		
		@Bean
		public VisitDetailedDao visitDetailedDao() {
			return new VisitDetailedDao();
		}
	}
	
	private static final Long HOW_MANY = 20L;
	private static final int COMPANY_ID = 5;

	private List<VisitDetailedEntity> visitsDetailEntities;
	
	@Before
	public void setUp() {
		Mockito.reset(visitDetailedJpaRepository);
		
		visitsDetailEntities = VisitDetailedMock.generateVisitDetailedEntities(HOW_MANY);
		
		Mockito.when(visitDetailedJpaRepository.findAdmitedNowByCompanyIdAndFullBedIn(Mockito.anyInt(), Mockito.anyListOf(String.class), Mockito.any(Pageable.class))).thenReturn(visitsDetailEntities);
		Mockito.when(visitDetailedJpaRepository.countAdmitedNowByCompanyIdAndFullBedIn(Mockito.anyInt(), Mockito.anyListOf(String.class))).thenReturn(HOW_MANY);
	}
	
	
	@MockBean
	private VisitDetailedRepository visitDetailedJpaRepository;
	
	@Autowired
	private VisitDetailedDao visitDetailedDao;
	
	@Test
	public void find_should_return() {
		List<VisitDetailed> visitDetaileds = visitDetailedDao.findAdmitedNowByCompanyIdAndBeds(COMPANY_ID, new ArrayList<>(), 10, 0);
		
		assertNotNull(visitDetaileds);
		assertEquals(HOW_MANY, new Long(visitDetaileds.size()));
		
		for (VisitDetailed visitDetailed : visitDetaileds) {
			for (VisitDetailedEntity visitsDetailedEntity : visitsDetailEntities) {
				if(visitsDetailedEntity.getVisitNumber().equals(visitDetailed.getVisitNb())) {
					assertEntityToModel(visitsDetailedEntity, visitDetailed);
					break;
				}
			}
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void find_limit_null_IllegalArgumentException() {
		visitDetailedDao.findAdmitedNowByCompanyIdAndBeds(COMPANY_ID, new ArrayList<>(), null, 0);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void find_offset_null_IllegalArgumentException() {
		visitDetailedDao.findAdmitedNowByCompanyIdAndBeds(COMPANY_ID, new ArrayList<>(), 10, null);
	}

	@Test
	public void count_should_return() {
		Long count = visitDetailedDao.countAdmitedNowByCompanyIdAndBeds(COMPANY_ID, new ArrayList<>());
		
		assertNotNull(count);
		assertEquals(HOW_MANY, count);
	}
	
	private void assertEntityToModel(VisitDetailedEntity visitsDetailedEntity, VisitDetailed visitDetailed) {
		assertNotNull(visitDetailed);
		assertNotNull(visitDetailed.getPatient());
		assertNotNull(visitDetailed.getCompany());
		
		assertEquals(visitsDetailedEntity.getVisitNumber(), visitDetailed.getVisitNb());
		assertEquals(visitsDetailedEntity.getHl7Code(), visitDetailed.getHl7code());
		assertEquals(visitsDetailedEntity.getVisitDetail().getAdmitSource(), visitDetailed.getAdmitSource());
		assertEquals(visitsDetailedEntity.getPatientId(), visitDetailed.getPatient().getPid());
		assertEquals(visitsDetailedEntity.getCompany().getCode(), visitDetailed.getCompany().getCode());
	}
	

}
