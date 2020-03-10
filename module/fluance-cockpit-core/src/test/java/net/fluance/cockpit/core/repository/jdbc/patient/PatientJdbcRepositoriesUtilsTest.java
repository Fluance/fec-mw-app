package net.fluance.cockpit.core.repository.jdbc.patient;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.wrap.patient.AdmissionStatusEnum;
import net.fluance.cockpit.core.model.wrap.patient.PatientSexEnum;


public class PatientJdbcRepositoriesUtilsTest {
	
	@Test
	public void createWhereForByCriteriaSearch_should_match() {
		Map<String, Object> params =  new HashMap<>();
	
		params.put("lastname", "foo");
		params.put("maidenname", "foo");
		params.put("firstname", "foo");	
		params.put("admitdt", Date.from(LocalDate.of(2220, 10, 22).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		params.put("email", "foo@foo");
		params.put("orderby", "foo");
		params.put("limit", "foo");
		params.put("test1", "foo");
		params.put("test2", "foo");
		
		String expectedWhere = "WHERE firstname ILIKE 'foo%' AND test2='foo' AND date(admitdt)= CAST('Sun Oct 22 00:00:00 CEST 2220' AS DATE) AND maidenname ILIKE 'foo%' AND test1='foo' AND pcl.data ILIKE 'foo@foo' AND pcl.nbtype='email_address' AND lastname ILIKE 'foo%'";
			
		String where = PatientJdbcRepositoriesUtils.createWhereForByCriteriaSearch(params, null, null);
		
		assertEquals(expectedWhere, where);
	}
	
	@Test
	public void createWhereForByCriteriaSearch_sex_F_should_match() {
		Map<String, Object> params =  new HashMap<>();
	
		params.put("lastname", "foo");
		params.put("maidenname", "foo");
		params.put("firstname", "foo");	
		params.put("admitdt", Date.from(LocalDate.of(2220, 10, 22).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		params.put("email", "foo@foo");
		params.put("orderby", "foo");
		params.put("limit", "foo");
		params.put("test1", "foo");
		params.put("test2", "foo");
		
		String expectedWhere = "WHERE firstname ILIKE 'foo%' AND test2='foo' AND date(admitdt)= CAST('Sun Oct 22 00:00:00 CEST 2220' AS DATE) AND maidenname ILIKE 'foo%' AND test1='foo' AND pcl.data ILIKE 'foo@foo' AND pcl.nbtype='email_address' AND lastname ILIKE 'foo%'" +
				" AND lower(sex) IN ('f', 'weiblich','féminin','femminile')";
			
		String where = PatientJdbcRepositoriesUtils.createWhereForByCriteriaSearch(params, PatientSexEnum.F, null);
		
		assertEquals(expectedWhere, where);
	}
	
	@Test
	public void createWhereForByCriteriaSearch_sex_M_should_match() {
		Map<String, Object> params =  new HashMap<>();
	
		params.put("lastname", "foo");
		params.put("maidenname", "foo");
		params.put("firstname", "foo");	
		params.put("admitdt", Date.from(LocalDate.of(2220, 10, 22).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		params.put("email", "foo@foo");
		params.put("orderby", "foo");
		params.put("limit", "foo");
		params.put("test1", "foo");
		params.put("test2", "foo");
		
		String expectedWhere = "WHERE firstname ILIKE 'foo%' AND test2='foo' AND date(admitdt)= CAST('Sun Oct 22 00:00:00 CEST 2220' AS DATE) AND maidenname ILIKE 'foo%' AND test1='foo' AND pcl.data ILIKE 'foo@foo' AND pcl.nbtype='email_address' AND lastname ILIKE 'foo%'" +
				" AND lower(sex) IN ('m', 'masculin','männlich','maschile')";
			
		String where = PatientJdbcRepositoriesUtils.createWhereForByCriteriaSearch(params, PatientSexEnum.M, null);
		
		assertEquals(expectedWhere, where);
	}
	
	@Test
	public void createWhereForByCriteriaSearch_sex_U_should_match() {
		Map<String, Object> params =  new HashMap<>();
	
		params.put("lastname", "foo");
		params.put("maidenname", "foo");
		params.put("firstname", "foo");	
		params.put("admitdt", Date.from(LocalDate.of(2220, 10, 22).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		params.put("email", "foo@foo");
		params.put("orderby", "foo");
		params.put("limit", "foo");
		params.put("test1", "foo");
		params.put("test2", "foo");
		
		String expectedWhere = "WHERE firstname ILIKE 'foo%' AND test2='foo' AND date(admitdt)= CAST('Sun Oct 22 00:00:00 CEST 2220' AS DATE) AND maidenname ILIKE 'foo%' AND test1='foo' AND pcl.data ILIKE 'foo@foo' AND pcl.nbtype='email_address' AND lastname ILIKE 'foo%'" +
				" AND (sex = 'U' OR sex is null)";
			
		String where = PatientJdbcRepositoriesUtils.createWhereForByCriteriaSearch(params, PatientSexEnum.U, null);
		
		assertEquals(expectedWhere, where);
	}
	
	@Test
	public void createWhereForByCriteriaSearch_PREADMITTED_should_match() {
		Map<String, Object> params =  new HashMap<>();
	
		params.put("lastname", "foo");
		params.put("maidenname", "foo");
		params.put("firstname", "foo");	
		params.put("admitdt", Date.from(LocalDate.of(2220, 10, 22).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		params.put("email", "foo@foo");
		params.put("orderby", "foo");
		params.put("limit", "foo");
		params.put("test1", "foo");
		params.put("test2", "foo");
		
		String expectedWhere = "WHERE firstname ILIKE 'foo%' AND test2='foo' AND date(admitdt)= CAST('Sun Oct 22 00:00:00 CEST 2220' AS DATE) AND maidenname ILIKE 'foo%' AND test1='foo' AND pcl.data ILIKE 'foo@foo' AND pcl.nbtype='email_address' AND lastname ILIKE 'foo%'" +
				" AND " + SQLStatements.PREADMITTED_PATIENTS_CRITERIA;
			
		String where = PatientJdbcRepositoriesUtils.createWhereForByCriteriaSearch(params, null, AdmissionStatusEnum.PREADMITTED);
		
		assertEquals(expectedWhere, where);
	}
	
	@Test
	public void createWhereForByCriteriaSearch_PREADMISSION_should_match() {
		Map<String, Object> params =  new HashMap<>();
	
		params.put("lastname", "foo");
		params.put("maidenname", "foo");
		params.put("firstname", "foo");	
		params.put("admitdt", Date.from(LocalDate.of(2220, 10, 22).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		params.put("email", "foo@foo");
		params.put("orderby", "foo");
		params.put("limit", "foo");
		params.put("test1", "foo");
		params.put("test2", "foo");
		
		String expectedWhere = "WHERE firstname ILIKE 'foo%' AND test2='foo' AND date(admitdt)= CAST('Sun Oct 22 00:00:00 CEST 2220' AS DATE) AND maidenname ILIKE 'foo%' AND test1='foo' AND pcl.data ILIKE 'foo@foo' AND pcl.nbtype='email_address' AND lastname ILIKE 'foo%'" +
				" AND " + SQLStatements.PREADMITTED_PATIENTS_CRITERIA;
			
		String where = PatientJdbcRepositoriesUtils.createWhereForByCriteriaSearch(params, null, AdmissionStatusEnum.PREADMISSION);
		
		assertEquals(expectedWhere, where);
	}
	
	@Test
	public void createWhereForByCriteriaSearch_CURRENTADMISSION_should_match() {
		Map<String, Object> params =  new HashMap<>();
	
		params.put("lastname", "foo");
		params.put("maidenname", "foo");
		params.put("firstname", "foo");	
		params.put("admitdt", Date.from(LocalDate.of(2220, 10, 22).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
		params.put("email", "foo@foo");
		params.put("orderby", "foo");
		params.put("limit", "foo");
		params.put("test1", "foo");
		params.put("test2", "foo");
		
		String expectedWhere = "WHERE firstname ILIKE 'foo%' AND test2='foo' AND date(admitdt)= CAST('Sun Oct 22 00:00:00 CEST 2220' AS DATE) AND maidenname ILIKE 'foo%' AND test1='foo' AND pcl.data ILIKE 'foo@foo' AND pcl.nbtype='email_address' AND lastname ILIKE 'foo%'" +
				" AND " + SQLStatements.INHOUSE_PATIENTS_CRITERIA;
			
		String where = PatientJdbcRepositoriesUtils.createWhereForByCriteriaSearch(params, null, AdmissionStatusEnum.CURRENTADMISSION);
		
		assertEquals(expectedWhere, where);
	}
	
}
