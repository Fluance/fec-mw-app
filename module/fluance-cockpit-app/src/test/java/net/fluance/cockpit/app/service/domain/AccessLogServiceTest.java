package net.fluance.cockpit.app.service.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
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

import net.fluance.cockpit.app.service.domain.impl.AccessLogServiceImpl;
import net.fluance.cockpit.core.dao.AccessLogDao;
import net.fluance.cockpit.core.model.AccessLog;
import net.fluance.cockpit.core.model.AccessLogShort;
import net.fluance.cockpit.core.model.AccessLogUser;
import net.fluance.cockpit.core.model.Count;

@RunWith(SpringJUnit4ClassRunner.class)
public class AccessLogServiceTest {
	
	String LOG_DETAIL_URL = "/users/%s";
	String LOG_DETAIL_URL_WITH_ACTUAL_USERNAME = "/users/%s/actualusername/%s";
	
	@TestConfiguration
	static class AccessLogServiceTestConfiguration {

		@Bean
		public AccessLogService accessLogService() {
			return new AccessLogServiceImpl();
		}
	}

	@Before
	public void setUp() {
		Mockito.reset(accessLogDao);
	}
	@MockBean
	private AccessLogDao accessLogDao;

	@Autowired
	AccessLogService accessLogService;

	//getPatientActions
	@Test
	public void getPatientActions_should_return() throws Exception {
		Mockito.when(accessLogDao.findUsersWithAccessLogForPatient("10000000", "firstname", "DESC", Integer.valueOf(10), Integer.valueOf(0))).thenReturn(generateAccessLogShorts(10, false));
		
		List<AccessLogShort> accessLogShorts = accessLogService.getPatientActions("10000000", "firstname", "DESC", 10, 0);
		
		assertNotNull("must be not null", accessLogShorts);
		assertTrue("Must contain at least one", accessLogShorts.size() > 0);
		
		accessLogShorts.forEach(accessLogShort  -> {
			assertNotNull("Must return a accessLog", accessLogShort);
			assertNotNull("Must return a user", accessLogShort.getUser());
			assertNull("Must return a user without actualuser", accessLogShort.getUser().getActualUsername());
			assertNotNull("Must return a detail url", accessLogShort.getDetailUrl());
			assertEquals("Detail must match", String.format(LOG_DETAIL_URL, accessLogShort.getUser().getUsername()), accessLogShort.getDetailUrl());
		});
	}
	
	@Test
	public void getPatientActions_with_actualuser_should_return() throws Exception {
		Mockito.when(accessLogDao.findUsersWithAccessLogForPatient("10000000", "firstname", "DESC", Integer.valueOf(10), Integer.valueOf(0))).thenReturn(generateAccessLogShorts(10, true));
		
		List<AccessLogShort> accessLogShorts = accessLogService.getPatientActions("10000000", "firstname", "DESC", 10, 0);
		
		assertNotNull("must be not null", accessLogShorts);
		assertTrue("Must contain at least one", accessLogShorts.size() > 0);
		
		accessLogShorts.forEach(accessLogShort  -> {
			assertNotNull("Must return a accessLog", accessLogShort);
			assertNotNull("Must return a user", accessLogShort.getUser());
			assertNotNull("Must return a user without actualuser", accessLogShort.getUser().getActualUsername());
			assertNotNull("Must return a detail url", accessLogShort.getDetailUrl());
			assertEquals("Detail must match", String.format(LOG_DETAIL_URL_WITH_ACTUAL_USERNAME, accessLogShort.getUser().getUsername(), accessLogShort.getUser().getActualUsername()), accessLogShort.getDetailUrl());
		});
	}
	
	@Test(expected = InvalidParameterException.class)
	public void getPatientActions_InvalidParameterException_pid_null() throws Exception {
		accessLogService.getPatientActions(null, "appuser", "DESC", 10, 0);
	}

	//getPatientActionsCount
	@Test
	public void getPatientActionsCount_should_return() throws Exception {
		Count count = new Count(10);
		Mockito.when(accessLogDao.findUsersWithAccessLogForPatientCount("10000000")).thenReturn(count);
		
		Count countReturn = accessLogService.getPatientActionsCount("10000000");
		
		assertNotNull("must be not null", countReturn);
		assertTrue("Must be the correct count value", countReturn.getCount() == count.getCount());
	}
	
	@Test(expected = InvalidParameterException.class)
	public void getPatientActionsCount_InvalidParameterException_pid_null() throws Exception {
		accessLogService.getPatientActionsCount(null);
	}
	
	@Test
	public void getPatientActionsByUser_should_return() throws Exception {
		Mockito.when(accessLogDao.findAccessLogForPatientByPidAndUser("10000000", "PRIMARY/Test", "appuser", "DESC", Integer.valueOf(10), Integer.valueOf(0))).thenReturn(generateAccessLogs(10));
		
		List<AccessLog> accessLogs = accessLogService.getPatientActionsByUser("10000000", "PRIMARY", "Test", "appuser", "DESC", 10, 0);
		
		assertNotNull("must be not null", accessLogs);
		assertTrue("Must contain at least one", accessLogs.size() > 0);
	}
	
	//getPatientActionsByUser
	@Test(expected = InvalidParameterException.class)
	public void getPatientActionsByUser_InvalidParameterException_domain_username_null() throws Exception {
		accessLogService.getPatientActionsByUser("10000000",  null, null, "appuser", "DESC", 10, 0);
	}
	
	@Test(expected = InvalidParameterException.class)
	public void getPatientActionsByUser_InvalidParameterException_pid_null() throws Exception {
		accessLogService.getPatientActionsByUser(null, "PRIMARY", "Test", "appuser", "DESC", 10, 0);
	}
	
	//getPatientActionsByUserCount
	@Test
	public void getPatientActionsByUserCount_should_return() throws Exception {
		Count count = new Count(10);
		Mockito.when(accessLogDao.findAccessLogForPatientByPidAndUserCount("10000000", "PRIMARY/Test")).thenReturn(count);
		
		Count countReturn = accessLogService.getPatientActionsByUserCount("10000000", "PRIMARY", "Test");
		
		assertNotNull("must be not null", countReturn);
		assertTrue("Must be the correct count value", countReturn.getCount() == count.getCount());
	}
	
	@Test(expected = InvalidParameterException.class)
	public void getPatientActionsByUserCount_InvalidParameterException_domain_username_null() throws Exception {
		accessLogService.getPatientActionsByUserCount("10000000",  null, null);
	}
	
	@Test(expected = InvalidParameterException.class)
	public void getPatientActionsByUserCount_InvalidParameterException_pid_null() throws Exception {
		accessLogService.getPatientActionsByUserCount(null, "PRIMARY", "Test");
	}
	
	//getPatientActionsByUserAndActualUsername
	@Test
	public void getPatientActionsByUserAndActualUsername_should_return() throws Exception {
		Mockito.when(accessLogDao.findAccessLogForPatientByPidAndUserAndActualUsername("10000000", "PRIMARY/Test", "actualusername", "logdt", "DESC", Integer.valueOf(10), Integer.valueOf(0))).thenReturn(generateAccessLogs(10));
		
		List<AccessLog> accessLogs = accessLogService.getPatientActionsByUserAndActualUsername("10000000", "PRIMARY", "Test", "actualusername", "logdt", "DESC", 10, 0);
		
		assertNotNull("must be not null", accessLogs);
		assertTrue("Must contain at least one", accessLogs.size() > 0);
	}
	
	@Test(expected = InvalidParameterException.class)
	public void getPatientActionsByUserAndActualUsername_InvalidParameterException_domain_username_null() throws Exception {
		accessLogService.getPatientActionsByUserAndActualUsername("10000000",  null, null, "actualusername", "appuser", "DESC", 10, 0);
	}
	
	@Test(expected = InvalidParameterException.class)
	public void getPatientActionsByUserAndActualUsername_InvalidParameterException_pid_null() throws Exception {
		accessLogService.getPatientActionsByUserAndActualUsername(null, "PRIMARY", "Test", "actualusername", "appuser", "DESC", 10, 0);
	}
	
	@Test(expected = InvalidParameterException.class)
	public void getPatientActionsByUserAndActualUsername_InvalidParameterException_actualusername_null() throws Exception {
		accessLogService.getPatientActionsByUserAndActualUsername("10000000", "PRIMARY", "Test", null, "appuser", "DESC", 10, 0);
	}
	
	//getPatientActionsByUserAndActualUsernameCount
	@Test
	public void getPatientActionsByUserAndActualUsernameCount_should_return() throws Exception {
		Count count = new Count(10);
		Mockito.when(accessLogDao.findAccessLogForPatientByPidAndUserAndActualUsernameCount("10000000", "PRIMARY/Test", "actualusername")).thenReturn(count);
		
		Count countReturn = accessLogService.getPatientActionsByUserAndActualUsernameCount("10000000", "PRIMARY", "Test", "actualusername");
		
		assertNotNull("must be not null", countReturn);
		assertTrue("Must be the correct count value", countReturn.getCount() == count.getCount());
	}
	
	@Test(expected = InvalidParameterException.class)
	public void getPatientActionsByUserAndActualUsernameCount_InvalidParameterException_domain_username_null() throws Exception {
		accessLogService.getPatientActionsByUserAndActualUsernameCount("10000000",  null, null, "actualusername");
	}
	
	@Test(expected = InvalidParameterException.class)
	public void getPatientActionsByUserAndActualUsernameCount_InvalidParameterException_pid_null() throws Exception {
		accessLogService.getPatientActionsByUserAndActualUsernameCount(null, "PRIMARY", "Test", "actualusername");
	}
	
	@Test(expected = InvalidParameterException.class)
	public void getPatientActionsByUserAndActualUsernameCount_InvalidParameterException_actualusername_null() throws Exception {
		accessLogService.getPatientActionsByUserAndActualUsernameCount("10000000", "PRIMARY", "Test", null);
	}
	
	private List<AccessLogShort> generateAccessLogShorts(int howMany, boolean isExternal) {
		List<AccessLogShort> accessLogShorts = new ArrayList<AccessLogShort>();
		AccessLogShort accessLogShort;
		AccessLogUser accessLogUser;
		
		String external = null;
		String externalFirstName = null;
		String externalLastName = null;
		String externalEmail = null;
		
		for(int current = 0; current<=howMany; current++) {
			accessLogShort = new AccessLogShort();
			accessLogShort.setLogDate(new Date());
			
			if(isExternal) {
				external = "external" + current;
				externalFirstName = "External" + current;
				externalLastName = "ExternalLast" + current;
				externalEmail = "external_email" + current + "@mail.ch";
			}
			
			accessLogUser = new AccessLogUser();
			accessLogUser.setFirstName("Test"+current);
			accessLogUser.setLastName("TestLastname"+current);
			accessLogUser.setUsername("PRIMARY/Test"+current);
			
			accessLogUser.setActualUsername(external);
			accessLogUser.setActualFirstName(externalFirstName);
			accessLogUser.setActualLastName(externalLastName);
			accessLogUser.setActualEmail(externalEmail);
			
			accessLogShort.setUser(accessLogUser);
			
			accessLogShorts.add(accessLogShort);
		}
		
		return accessLogShorts;
	}
	
	private List<AccessLog> generateAccessLogs(int howMany) {
		List<AccessLog> accessLogs = new ArrayList<AccessLog>();
		AccessLog accessLog;
		AccessLogUser accessLogUser;
		for(int current = 0; current<=howMany; current++) {
			accessLog = new AccessLog();
			accessLog.setLogDate(new Date());			
			accessLog.setDisplayName("Jhon");
			accessLog.setHttpMethod("https");
			accessLog.setObjectType("patient");
			accessLog.setParentPid(null);
			accessLog.setParentVisitNb(String.valueOf(100000 + current));
			
			accessLogUser = new AccessLogUser();
			accessLogUser.setFirstName("Test");
			accessLogUser.setLastName("TestLastname");
			accessLogUser.setUsername("PRIMARY/Test");
			
			accessLog.setUser(accessLogUser);
			
			accessLogs.add(accessLog);
		}
		
		return accessLogs;
	}
}
