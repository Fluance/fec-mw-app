package net.fluance.cockpit.core.test.model.jdbc.whiteboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.fluance.cockpit.core.model.jdbc.whiteboard.WhiteBoardDao;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewEntity;
import net.fluance.cockpit.core.repository.jdbc.whiteboard.WhiteBoardRepository;
import net.fluance.cockpit.core.repository.jpa.whiteboard.WhiteBoardJpaRepository;
import net.fluance.cockpit.core.util.whiteboard.NursesListMock;
import net.fluance.cockpit.core.util.whiteboard.WhiteBoardMock;


@RunWith(SpringJUnit4ClassRunner.class)
public class WhiteBoardDAOTest {
	
	@TestConfiguration
	static class WhiteBoardDAOTestConfiguration {
		
		@Bean
		PropertyPlaceholderConfigurer propConfig() {
			PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();

			Properties properties = new Properties();
			properties.put("whiteboard.default.sortOrder", "ASC");
			properties.put("whiteboard.default.orderBy", "ID");
			properties.put("whiteboard.default.limit", "10");
			properties.put("whiteboard.default.offset", "0");
			
			propertyPlaceholderConfigurer.setProperties(properties);

			propertyPlaceholderConfigurer.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");

			return propertyPlaceholderConfigurer;
		}
		
		@Bean
		public WhiteBoardDao whiteBoardDao() {
			return new WhiteBoardDao();
		}
	}
	
	private static final Integer HOW_MANY= 10;
	
	@Before
	public void setUp() {
		Mockito.reset(whiteBoardRepository);
		Mockito.reset(whiteBoardJpaRepository);
		
		Mockito.when(whiteBoardRepository.findEntries(Mockito.anyString(), Mockito.anyListOf(Object.class)) )
		.thenReturn(WhiteBoardMock.getMocks(HOW_MANY));
	    
	    Mockito.when(whiteBoardRepository.getNurses(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()) )
	      .thenReturn(NursesListMock.getList(HOW_MANY));
	    
	    Mockito.when(whiteBoardRepository.existEntryForVisitNumber(Mockito.anyLong()))
	      .thenReturn(true);
	    
	    Mockito.when(whiteBoardRepository.findOne(Mockito.anyLong()))
	      .thenReturn(WhiteBoardMock.getViewMock());
	    
	    Mockito.when(whiteBoardJpaRepository.save(Mockito.any(WhiteBoardViewEntity.class)))
	      .thenReturn(WhiteBoardMock.getViewMock());
	    
	    Mockito.when(whiteBoardJpaRepository.findByVisitNumber(Mockito.anyLong()))
	      .thenReturn(WhiteBoardMock.getViewMock());
	    
	    Mockito.when(whiteBoardRepository.existEntryForVisitNumber(Mockito.anyLong()))
	      .thenReturn(true);
	    
	    
	    Mockito.when(whiteBoardRepository.existEntryForVisitNumberOnView(1L,1L,"s")).thenReturn(Boolean.TRUE);
	    Mockito.when(whiteBoardRepository.existEntryForVisitNumberOnView(2L,1L,"s")).thenReturn(Boolean.FALSE);
	    
	}
	
	@Autowired
	private WhiteBoardDao whiteBoardDao;
	
	@MockBean
	private WhiteBoardRepository whiteBoardRepository;
	
	@MockBean
	private WhiteBoardJpaRepository whiteBoardJpaRepository;

	@Test
	public void getAllForCompanyAndService_without_limit_should_return() {
		List<WhiteBoardViewEntity> result = whiteBoardDao.getAllWhiteboardEntries(1L, "1", null, null, null, null, 10, 0, null, null);
		 
		assertNotNull(result);
		assertThat(result.size() == HOW_MANY).isTrue();
	}
	
	@Test
	public void getAllForCompanyAndService_with_limit_should_return() {
		List<WhiteBoardViewEntity> result = whiteBoardDao.getAllWhiteboardEntries(1L, "1", null, null, null, null, HOW_MANY, 0, null, null);
		
		assertNotNull(result);
		assertThat(result.size() == HOW_MANY).isTrue();
	}
	
	@Test
	public void getOneEntryt_should_return() {
		WhiteBoardViewEntity result = whiteBoardDao.getOneEntry(1L);
		
		assertNotNull(result);
	}
	
	@Test
	public void getNurses_should_return() {
		List<String> result = whiteBoardDao.getNurses(1L, "1", HOW_MANY, 0);
		
		assertNotNull(result);
		assertThat(result.size() == HOW_MANY).isTrue();
	}
	
	@Test
	public void exist_should_return() {
		Boolean result = whiteBoardDao.exist(1L);
		System.out.println(result);
		assertNotNull(result);
		assertThat(result).isTrue();
	}
	
	@Test
	public void existVisitNumber_should_return() {
		Boolean result = whiteBoardDao.existVisitNumber(1L);
		
		assertNotNull(result);
		assertThat(result).isTrue();
	}
	
	@Test
	public void write_should_return() {
		WhiteBoardViewEntity mock = WhiteBoardMock.getViewMock();
		WhiteBoardViewEntity result = whiteBoardDao.update(mock);
		assertNotNull(result);
		assertThat(result.getId().equals(mock.getId())).isTrue();
	}
	
	@Test
	public void entry_by_visit_number() {
		WhiteBoardViewEntity result = whiteBoardDao.getOneEntryByVisitNummer(1L);
		assertNotNull(result);
	}
	
	@Test
	public void exist_visit_on_view() {
		Boolean result = whiteBoardDao.existVisitNumberOnView(1L, 1L, "s");
		assertNotNull(result);
		assertThat(result).isTrue();
	}
	
	@Test
	public void exist_no_visit_on_view() {
		Boolean result = whiteBoardDao.existVisitNumberOnView(2L, 1L, "s");
		assertNotNull(result);
		assertThat(result).isFalse();
	}
}
