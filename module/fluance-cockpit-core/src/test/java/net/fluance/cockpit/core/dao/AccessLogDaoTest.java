package net.fluance.cockpit.core.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.fluance.cockpit.core.model.AccessLog;
import net.fluance.cockpit.core.model.AccessLogShort;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.jdbc.accesslog.PatientAccessLog;
import net.fluance.cockpit.core.model.jdbc.accesslog.PatientAccessLogShort;
import net.fluance.cockpit.core.repository.jdbc.accesslog.PatientAccessLogRepository;
import net.fluance.cockpit.core.repository.jdbc.accesslog.PatientAccessLogShortRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class AccessLogDaoTest {

	private static final int 	DEFAULT_LIMIT 		= 10;
	private static final int 	DEFAULT_OFFSET 		= 0;
	private static final String DEFAULT_ORDERBY 	= "logdt";
	private static final String DEFAULT_SORTORDER 	= "ASC";

	@TestConfiguration
	static class AccessLogDaoTestConfiguration {
		@Bean
		public ResourceLoader resourceLoader() {
			return new GenericApplicationContext();
		}
		
		@Bean
		PropertyPlaceholderConfigurer propConfig() {
			PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
			
			Properties properties = new Properties();
			properties.put("net.fluance.cockpit.core.model.repository.defaultResultLimit", 					String.valueOf(DEFAULT_LIMIT));
			properties.put("net.fluance.cockpit.core.model.repository.defaultResultOffset", 				String.valueOf(DEFAULT_OFFSET));
			properties.put("net.fluance.cockpit.core.model.repository.defaultResultAccessLogListOrderBy", 	DEFAULT_ORDERBY);
			properties.put("net.fluance.cockpit.core.model.repository.defaultResultAccessLogListSortOrder", DEFAULT_SORTORDER);

			propertyPlaceholderConfigurer.setProperties(properties);

			propertyPlaceholderConfigurer.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");

			return propertyPlaceholderConfigurer;
		}
		
		@Bean
		public AccessLogDao accessLogDao() {
			return new AccessLogDao();
		}
	}	
	
	@Before
	public void setUp() {
		Mockito.reset(patientAccessLogRepository);
		Mockito.reset(patientAccessLogShortRepository);
	}
	
	@MockBean
	private PatientAccessLogRepository patientAccessLogRepository;

	@MockBean
	private PatientAccessLogShortRepository patientAccessLogShortRepository;
	
	@Autowired
	private AccessLogDao AccessLogDao;
	
	//findUsersWithAccessLogForPatient
	@Test
	public void findUsersWithAccessLogForPatient_should_return() {
		Mockito.when(patientAccessLogShortRepository.findByPid("100000", "appuser", DEFAULT_SORTORDER, DEFAULT_LIMIT, DEFAULT_OFFSET)).thenReturn(getPatientAccessLogShort(false));
		
		List<AccessLogShort> accessLogShorts = AccessLogDao.findUsersWithAccessLogForPatient("100000", "appuser", DEFAULT_SORTORDER, null, DEFAULT_OFFSET);
		
		assertNotNull("Must return values", accessLogShorts);
		assertTrue("Must return values", accessLogShorts.size() > 0);
		
		accessLogShorts.forEach(accessLogShort  -> {
			assertNotNull("Must return a accessLog", accessLogShort);
			assertNotNull("Must return a user", accessLogShort.getUser());
			assertNull("Must return a user without actualuser", accessLogShort.getUser().getActualUsername());
		});
	}
	
	@Test
	public void findUsersWithAccessLogForPatient_with_actualuser_should_return() {
		Mockito.when(patientAccessLogShortRepository.findByPid("100000", "appuser", DEFAULT_SORTORDER, DEFAULT_LIMIT, DEFAULT_OFFSET)).thenReturn(getPatientAccessLogShort(true));
		
		List<AccessLogShort> accessLogShorts = AccessLogDao.findUsersWithAccessLogForPatient("100000", "appuser", DEFAULT_SORTORDER, null, DEFAULT_OFFSET);
		
		assertNotNull("Must return values", accessLogShorts);
		assertTrue("Must return values", accessLogShorts.size() > 0);
		
		accessLogShorts.forEach(accessLogShort  -> {
			assertNotNull("Must return a accessLog", accessLogShort);
			assertNotNull("Must return a user", accessLogShort.getUser());
			assertNotNull("Must return a user with actualuser", accessLogShort.getUser().getActualUsername());
		});
	}
	
	@Test
	public void findUsersWithAccessLogForPatient_should_return_orderBy_null() {
		Mockito.when(patientAccessLogShortRepository.findByPid("100000", DEFAULT_ORDERBY, DEFAULT_SORTORDER, DEFAULT_LIMIT, DEFAULT_OFFSET)).thenReturn(getPatientAccessLogShort(false));
		
		List<AccessLogShort> accessLogShorts = AccessLogDao.findUsersWithAccessLogForPatient("100000", null, DEFAULT_SORTORDER, null, DEFAULT_OFFSET);
		
		assertNotNull("Must return values", accessLogShorts);
		assertTrue("Must return values", accessLogShorts.size() > 0);
	}
	
	@Test
	public void findUsersWithAccessLogForPatient_should_return_orderBy_foo() {
		Mockito.when(patientAccessLogShortRepository.findByPid("100000", DEFAULT_ORDERBY, DEFAULT_SORTORDER, DEFAULT_LIMIT, DEFAULT_OFFSET)).thenReturn(getPatientAccessLogShort(false));
		
		List<AccessLogShort> accessLogShorts = AccessLogDao.findUsersWithAccessLogForPatient("100000", "foo", DEFAULT_SORTORDER, null, DEFAULT_OFFSET);
		
		assertNotNull("Must return values", accessLogShorts);
		assertTrue("Must return values", accessLogShorts.size() > 0);
	}
	
	@Test
	public void findUsersWithAccessLogForPatient_should_return_sortOrder_null() {
		Mockito.when(patientAccessLogShortRepository.findByPid("100000", DEFAULT_ORDERBY, DEFAULT_SORTORDER, DEFAULT_LIMIT, DEFAULT_OFFSET)).thenReturn(getPatientAccessLogShort(false));
		
		List<AccessLogShort> accessLogShorts = AccessLogDao.findUsersWithAccessLogForPatient("100000", DEFAULT_ORDERBY, "foo", 10, DEFAULT_OFFSET);
		
		assertNotNull("Must return values", accessLogShorts);
		assertTrue("Must return values", accessLogShorts.size() > 0);
	}
	
	@Test
	public void findUsersWithAccessLogForPatient_should_return_sortOrder_foo() {
		Mockito.when(patientAccessLogShortRepository.findByPid("100000", DEFAULT_ORDERBY, DEFAULT_SORTORDER, DEFAULT_LIMIT, DEFAULT_OFFSET)).thenReturn(getPatientAccessLogShort(false));

		List<AccessLogShort> accessLogShorts = AccessLogDao.findUsersWithAccessLogForPatient("100000", DEFAULT_ORDERBY, null, 10, DEFAULT_OFFSET);
		
		assertNotNull("Must return values", accessLogShorts);
		assertTrue("Must return values", accessLogShorts.size() > 0);
	}
	
	@Test(expected = InvalidParameterException.class)
	public void findUsersWithAccessLogForPatient_InvalidParameterException() {
		AccessLogDao.findUsersWithAccessLogForPatient(null, "appuser", "DESC", DEFAULT_LIMIT, DEFAULT_OFFSET);
	}
	
	//findUsersWithAccessLogForPatientCount
	@Test
	public void findUsersWithAccessLogForPatientCount_should_return() {
		Mockito.when(patientAccessLogShortRepository.findByPidCount("100000") ).thenReturn(DEFAULT_LIMIT);
		
		Count count = AccessLogDao.findUsersWithAccessLogForPatientCount("100000");
		
		assertNotNull("Must return values", count);
		assertEquals("Must return values", (int) count.getCount(), DEFAULT_LIMIT);
	}
	
	//findAccessLogForPatientByPidAndUser
	@Test
	public void findAccessLogForPatientByPidAndUser_should_return() {
		Mockito.when(patientAccessLogRepository.findByPidAndAppuser("100000", "PRIMARY/test", "appuser", "DESC", DEFAULT_LIMIT, DEFAULT_OFFSET)).thenReturn(getPatientAccessLog(false));
		
		List<AccessLog> accessLogs = AccessLogDao.findAccessLogForPatientByPidAndUser("100000", "PRIMARY/test", "appuser", "DESC", 10, null);
		
		assertNotNull("Must return values", accessLogs);
		assertTrue("Must return values", accessLogs.size() > 0);
		
		accessLogs.forEach(accessLog  -> {
			assertNotNull("Must return a accessLog", accessLog);
			assertNotNull("Must return a user", accessLog.getUser());
			assertNull("Must return a user without actualuser", accessLog.getUser().getActualUsername());
		});
	}
	
	@Test(expected = InvalidParameterException.class)
	public void findAccessLogForPatientByPidAndUser_InvalidParameterException_pid_null() {
		AccessLogDao.findAccessLogForPatientByPidAndUser(null, "PRIMARY/test", "appuser", "DESC", DEFAULT_LIMIT, DEFAULT_OFFSET);
	}
	
	@Test(expected = InvalidParameterException.class)
	public void findAccessLogForPatientByPidAndUser_InvalidParameterException_appsuer_null() {
		AccessLogDao.findAccessLogForPatientByPidAndUser("100000", null, "appuser", "DESC", DEFAULT_LIMIT, DEFAULT_OFFSET);
	}
	
	//findAccessLogForPatientByPidAndUserCount
	@Test
	public void findAccessLogForPatientByPidAndUserCount_should_return() {
		Mockito.when(patientAccessLogRepository.findByPidAndAppuserCount("100000", "PRIMARY/test")).thenReturn(new Count(DEFAULT_LIMIT));
		
		Count count = AccessLogDao.findAccessLogForPatientByPidAndUserCount("100000", "PRIMARY/test");
		
		assertNotNull("Must return values", count);
		assertEquals("Must return values", (int) count.getCount(), DEFAULT_LIMIT);
	}
	
	@Test(expected = InvalidParameterException.class)
	public void findAccessLogForPatientByPidAndUserCount_InvalidParameterException_pid_null() {
		AccessLogDao.findAccessLogForPatientByPidAndUserCount(null, "PRIMARY/test");
	}
	
	@Test(expected = InvalidParameterException.class)
	public void findAccessLogForPatientByPidAndUserCount_InvalidParameterException_appsuer_null() {
		AccessLogDao.findAccessLogForPatientByPidAndUserCount("100000", null);
	}
	
	//findAccessLogForPatientByPidAndUserAndActualUsername
	@Test
	public void findAccessLogForPatientByPidAndUserAndActualUsername_should_return() {
		Mockito.when(patientAccessLogRepository.findByPidAndAppuserAndExternalUser("100000", "PRIMARY/test", "external", "appuser", "DESC", DEFAULT_LIMIT, DEFAULT_OFFSET)).thenReturn(getPatientAccessLog(true));
		
		List<AccessLog> accessLogs = AccessLogDao.findAccessLogForPatientByPidAndUserAndActualUsername("100000", "PRIMARY/test", "external", "appuser",  "DESC", DEFAULT_LIMIT, null);
		
		assertNotNull("Must return values", accessLogs);
		assertTrue("Must return values", accessLogs.size() > 0);
		
		accessLogs.forEach(accessLog  -> {
			assertNotNull("Must return a accessLog", accessLog);
			assertNotNull("Must return a user", accessLog.getUser());
			assertNotNull("Must return a user with actualuser", accessLog.getUser().getActualUsername());
		});
	}
	
	@Test(expected = InvalidParameterException.class)
	public void findAccessLogForPatientByPidAndUserAndActualUsername_InvalidParameterException_pid_null() {
		AccessLogDao.findAccessLogForPatientByPidAndUserAndActualUsername(null, "PRIMARY/test", "appuser", "external", "DESC", DEFAULT_LIMIT, DEFAULT_OFFSET);
	}
	
	@Test(expected = InvalidParameterException.class)
	public void findAccessLogForPatientByPidAndUserAndActualUsername_InvalidParameterException_appsuer_null() {
		AccessLogDao.findAccessLogForPatientByPidAndUserAndActualUsername("100000", null, "appuser", "external", "DESC", DEFAULT_LIMIT, DEFAULT_OFFSET);
	}
	
	@Test(expected = InvalidParameterException.class)
	public void findAccessLogForPatientByPidAndUserAndActualUsername_InvalidParameterException_actualusername_null() {
		AccessLogDao.findAccessLogForPatientByPidAndUserAndActualUsername("100000", "PRIMARY/test", null, "appuser", "DESC", DEFAULT_LIMIT, DEFAULT_OFFSET);
	}
	
	//findAccessLogForPatientByPidAndUserAndActualUsernameCount
	@Test
	public void findAccessLogForPatientByPidAndUserAndActualUsernameCount_should_return() {
		Mockito.when(patientAccessLogRepository.findByPidAndAppuserAndExternalUserCount("100000", "PRIMARY/test", "external")).thenReturn(new Count(DEFAULT_LIMIT));
		
		Count count = AccessLogDao.findAccessLogForPatientByPidAndUserAndActualUsernameCount("100000", "PRIMARY/test", "external");
		
		assertNotNull("Must return values", count);
		assertEquals("Must return values", (int) count.getCount(), DEFAULT_LIMIT);
	}
	
	@Test(expected = InvalidParameterException.class)
	public void findAccessLogForPatientByPidAndUserAndActualUsernameCount_InvalidParameterException_pid_null() {
		AccessLogDao.findAccessLogForPatientByPidAndUserAndActualUsernameCount(null, "PRIMARY/test", "external");
	}
	
	@Test(expected = InvalidParameterException.class)
	public void findAccessLogForPatientByPidAndUserAndActualUsernameCount_InvalidParameterException_appsuer_null() {
		AccessLogDao.findAccessLogForPatientByPidAndUserAndActualUsernameCount("100000", null, "external");
	}
	
	@Test(expected = InvalidParameterException.class)
	public void findAccessLogForPatientByPidAndUserAndActualUsernameCount_InvalidParameterException_actualusername_null() {
		AccessLogDao.findAccessLogForPatientByPidAndUserAndActualUsernameCount("100000", "PRIMARY/test", null);
	}
	
	private List<PatientAccessLogShort> getPatientAccessLogShort(boolean isExternal){
		List<PatientAccessLogShort> accessLogShorts = new ArrayList<>();
		PatientAccessLogShort patientAccessLogShort;
		
		String external = null;
		String externalFirstName = null;
		String externalLastName = null;
		String externalEmail = null;
		
		for(int current = 0; current<= DEFAULT_LIMIT; current++) {
			if(isExternal) {
				external = "external" + current;
				externalFirstName = "External" + current;
				externalLastName = "ExternalLast" + current;
				externalEmail = "external_email" + current + "@mail.ch";
			}
			
			patientAccessLogShort = new PatientAccessLogShort(new Date(), "PRIMARY/test" + current, "Test" + current, "TestLastname" + current, external, externalFirstName, externalLastName, externalEmail);
			accessLogShorts.add(patientAccessLogShort);
		}
		
		return accessLogShorts;
	}
	
	private List<PatientAccessLog> getPatientAccessLog(boolean isExternal){
		List<PatientAccessLog> accessLogs = new ArrayList<>();
		PatientAccessLog patientAccessLog;
		
		String external = null;
		String externalFirstName = null;
		String externalLastName = null;
		String externalEmail = null;
		
		for(int current = 0; current<= DEFAULT_LIMIT; current++) {
			if(isExternal) {
				external = "external" + current;
				externalFirstName = "External" + current;
				externalLastName = "ExternalLast" + current;
				externalEmail = "external_email" + current + "@mail.ch";
			}
			
			if(current % 2 == 0) {
				patientAccessLog = new PatientAccessLog(new Date(), "patient", "100000", "Jhon", "10000", "20000", "https", "PRIMARY/test", "Test", "TestLastName",
						external, externalFirstName, externalLastName, externalEmail);
			} else {
				patientAccessLog = new PatientAccessLog(new Date(), "patientAccessLog", "100000", "Jhon", "10000", "20000", "https", "PRIMARY/test", "Test", "TestLastName",
						external, externalFirstName, externalLastName, externalEmail);
			}
			accessLogs.add(patientAccessLog);
		}
		
		return accessLogs;
	}
}

