package net.fluance.cockpit.core.test.repository.jdbc.patient;

import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import net.fluance.app.test.AbstractTest;
import net.fluance.cockpit.core.model.jdbc.patient.PatientInList;
import net.fluance.cockpit.core.model.wrap.patient.AdmissionStatusEnum;
import net.fluance.cockpit.core.model.wrap.patient.PatientSexEnum;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientInListRepository;
import net.fluance.cockpit.core.test.Application;
import net.fluance.cockpit.core.test.PatientTestConfig;

@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations = "classpath:test.properties")
public class PatientInListRepositoryTest extends AbstractTest {

	@Autowired
	private PatientTestConfig patientTestConfig;
	@Autowired
	private PatientInListRepository patientInListRepository;

	@Test
	@Ignore("Test does not compile")
	public void mustListByVisitNbTest() {
		Map<String, Object> byVisitNbParams = new HashMap<>();
		byVisitNbParams.put("company_id", Integer.parseInt(patientTestConfig.getExpectedPatientLastVisitCompanyId()));
		byVisitNbParams.put("nb", patientTestConfig.getExpectedPatientLastVisitNumber());
		List<PatientInList> patientsByVisitNb = patientInListRepository.findByCriteria(byVisitNbParams,
				PatientSexEnum.UNDETERMINED, AdmissionStatusEnum.UNKNOWN, null, null);
		assertNotNull(patientsByVisitNb);
		assertEquals(Integer.parseInt(patientTestConfig.getExpectedPatientByVisitNbCount()), patientsByVisitNb.size());
		PatientInList expectedPatientByVisitNb = patientsByVisitNb.get(0);
		checkAgainstExpected(expectedPatientByVisitNb);
	}

	@Test
	@Ignore("Test does not compile")
	public void mustListByLastnameAndFirstnameMaidennameTest() {

		Map<String, Object> byLastnameAndFirstnameMaidennameParams = new HashMap<>();

		byLastnameAndFirstnameMaidennameParams.put("company_id",
				Integer.parseInt(patientTestConfig.getExpectedPatientLastVisitCompanyId()));
		byLastnameAndFirstnameMaidennameParams.put("lastname",
				patientTestConfig.getExpectedPatientLastNameStartLetters());
		byLastnameAndFirstnameMaidennameParams.put("firstname",
				patientTestConfig.getExpectedPatientFirstNameStartLetters());
		byLastnameAndFirstnameMaidennameParams.put("maidenname",
				patientTestConfig.getExpectedPatientMaidenNameStartLetters());
		List<PatientInList> patientsByLastnameAndFirstnameMaidennameBirthDate = patientInListRepository
				.findByCriteria(byLastnameAndFirstnameMaidennameParams, PatientSexEnum.UNDETERMINED, AdmissionStatusEnum.UNKNOWN, null, null);
		assertNotNull(patientsByLastnameAndFirstnameMaidennameBirthDate);
		assertEquals(Integer.parseInt(patientTestConfig.getExpectedPatientByLastNameAndFirstNameAndMaidenNameCount()),
				patientsByLastnameAndFirstnameMaidennameBirthDate.size());
		PatientInList expectedPatientByLastnameAndFirstnameMaidennameBirthDate = patientsByLastnameAndFirstnameMaidennameBirthDate
				.get(0);
		checkAgainstExpected(expectedPatientByLastnameAndFirstnameMaidennameBirthDate);
	}

	@Test
	@Ignore("Test does not compile")
	public void mustListByLastnameAndFirstnameMaidennameBirthDateTest() {
		Map<String, Object> byLastnameAndFirstnameMaidennameBirthDateParams = new HashMap<>();
		byLastnameAndFirstnameMaidennameBirthDateParams.put("company_id",
				Integer.parseInt(patientTestConfig.getExpectedPatientLastVisitCompanyId()));
		byLastnameAndFirstnameMaidennameBirthDateParams.put("lastname",
				patientTestConfig.getExpectedPatientLastNameStartLetters());
		byLastnameAndFirstnameMaidennameBirthDateParams.put("firstname",
				patientTestConfig.getExpectedPatientFirstNameStartLetters());
		byLastnameAndFirstnameMaidennameBirthDateParams.put("maidenname",
				patientTestConfig.getExpectedPatientMaidenNameStartLetters());
		byLastnameAndFirstnameMaidennameBirthDateParams.put("birthdate",
				Date.valueOf(patientTestConfig.getExpectedPatientDateOfBirth()));
		List<PatientInList> patientsByLastnameAndFirstnameMaidennameBirthDate = patientInListRepository
				.findByCriteria(byLastnameAndFirstnameMaidennameBirthDateParams, PatientSexEnum.UNDETERMINED, AdmissionStatusEnum.UNKNOWN, null,null);
		assertNotNull(patientsByLastnameAndFirstnameMaidennameBirthDate);
		assertEquals(
				Integer.parseInt(
						patientTestConfig.getExpectedPatientByLastNameAndFirstNameAndMaidenNameAndBirthDateCount()),
				patientsByLastnameAndFirstnameMaidennameBirthDate.size());
		PatientInList expectedPatientByLastnameAndFirstnameMaidennameBirthDate = patientsByLastnameAndFirstnameMaidennameBirthDate
				.get(0);
		checkAgainstExpected(expectedPatientByLastnameAndFirstnameMaidennameBirthDate);
	}

	@Test
	@Ignore("Test does not compile")
	public void mustListByLastnameAndBirthDateTest() {
		Map<String, Object> byLastnameAndBirthDateParams = new HashMap<>();
		byLastnameAndBirthDateParams.put("company_id",
				Integer.parseInt(patientTestConfig.getExpectedPatientLastVisitCompanyId()));
		byLastnameAndBirthDateParams.put("lastname", patientTestConfig.getExpectedPatientLastNameStartLetters());
		byLastnameAndBirthDateParams.put("birthdate", Date.valueOf(patientTestConfig.getExpectedPatientDateOfBirth()));
		List<PatientInList> patientsByLastnameAndBirthDate = patientInListRepository
				.findByCriteria(byLastnameAndBirthDateParams, PatientSexEnum.UNDETERMINED, AdmissionStatusEnum.UNKNOWN, null,null);
		assertNotNull(patientsByLastnameAndBirthDate);
		assertEquals(Integer.parseInt(patientTestConfig.getExpectedPatientByLastNameAndBirthDateCount()),
				patientsByLastnameAndBirthDate.size());
		PatientInList expectedPatientByLastnameAndFirstnameMaidennameBirthDate = patientsByLastnameAndBirthDate.get(0);
		checkAgainstExpected(expectedPatientByLastnameAndFirstnameMaidennameBirthDate);
	}

	@Test
	@Ignore("Test does not compile")
	public void mustListInHouseByLastnameTest() {
		Map<String, Object> inHouseByLastnameParams = new HashMap<>();
		inHouseByLastnameParams.put("company_id",
				Integer.parseInt(patientTestConfig.getExpectedPatientLastVisitCompanyId()));
		inHouseByLastnameParams.put("lastname", patientTestConfig.getExpectedInHousePatientLastNameStartLetters());
		List<PatientInList> inHousePatientsByLastname = patientInListRepository.findByCriteria(inHouseByLastnameParams, PatientSexEnum.UNDETERMINED,
				AdmissionStatusEnum.CURRENTADMISSION, null,null);
		assertNotNull(inHousePatientsByLastname);
		assertEquals(Integer.parseInt(patientTestConfig.getExpectedInHousePatientByLastNameCount()),
				inHousePatientsByLastname.size());
	}

	@Test
	@Ignore("Test does not compile")
	public void mustListPreAdmittedByLastnameTest() {
		Map<String, Object> preAdmittedByLastnameParams = new HashMap<>();
		preAdmittedByLastnameParams.put("company_id",
				Integer.parseInt(patientTestConfig.getExpectedPreAdmittedCompanyId()));
		preAdmittedByLastnameParams.put("lastname",
				patientTestConfig.getExpectedPreAdmittedPatientLastNameStartLetters());
		List<PatientInList> preAdmittedPatientsByLastname = patientInListRepository
				.findByCriteria(preAdmittedByLastnameParams, PatientSexEnum.UNDETERMINED, AdmissionStatusEnum.PREADMITTED, null,null);
		assertNotNull(preAdmittedPatientsByLastname);
		assertEquals(Integer.parseInt(patientTestConfig.getExpectedPreAdmittedPatientByLastNameCount()),
				preAdmittedPatientsByLastname.size());
	}

	@Test
	@Ignore("Test does not compile")
	public void mustListByAttendingPhysicianTest() {
		Integer companyId = Integer.valueOf(patientTestConfig.getExpectedPatientLastVisitCompanyId());
		Integer staffId = Integer.valueOf(patientTestConfig.getExpectedPatientVisitAttendingStaffId());
		List<PatientInList> patientsByAttendingPhysician = patientInListRepository.findPatientsByAttendingphysician(companyId, staffId, null, 50, 0);
		assertNotNull(patientsByAttendingPhysician);
		assertEquals(Integer.parseInt(patientTestConfig.getExpectedPatientsByAttendingStaffIdCount()),
				patientsByAttendingPhysician.size());
	}

	private void checkAgainstExpected(PatientInList patient) {
		assertEquals(Long.valueOf(patientTestConfig.getExpectedPatientId()), patient.getId());
		assertEquals(patientTestConfig.getExpectedPatientFirstName(), patient.getFirstName());
		assertEquals(patientTestConfig.getExpectedPatientLastName(), patient.getLastName());
		assertEquals(patientTestConfig.getExpectedPatientMaidenName(), patient.getMaidenName());
		assertEquals(Date.valueOf(patientTestConfig.getExpectedPatientDateOfBirth()), patient.getBirthDate());
		assertEquals(patientTestConfig.getExpectedPatientSex(), patient.getSex());
		assertEquals(patientTestConfig.getExpectedPatientAddress(), patient.getAddress());
		assertEquals(patientTestConfig.getExpectedPatientLocality(), patient.getLocality());
		assertEquals(patientTestConfig.getExpectedPatientPostCode(), patient.getPostCode());
		assertEquals(Long.valueOf(patientTestConfig.getExpectedPatientLastVisitNumber()), patient.getLastVisitnumber());
		assertEquals(Long.valueOf(patientTestConfig.getExpectedPatientLastVisitCompanyId()),
				patient.getLastVisitCompanyId());
		assertEquals(Timestamp.valueOf(patientTestConfig.getExpectedPatientLastVisitAdmitDate()),
				patient.getLastVisitAdmitDate());
		assertEquals(Timestamp.valueOf(patientTestConfig.getExpectedPatientLastVisitDischargeDate()),
				patient.getLastVisitDischargeDate());
		assertEquals(patientTestConfig.getExpectedPatientLastVisitPatientClass(), patient.getLastVisitPatientClass());
		assertEquals(patientTestConfig.getExpectedPatientLastVisitPatientClassDesc(),
				patient.getLastVisitPatientClassDesc());
		assertEquals(patientTestConfig.getExpectedPatientLastVisitPatientType(), patient.getLastVisitPatientType());
		assertEquals(patientTestConfig.getExpectedPatientLastVisitPatientTypeDesc(),
				patient.getLastVisitPatientTypeDesc());
		assertEquals(patientTestConfig.getExpectedPatientLastVisitPatientCase(), patient.getLastVisitPatientCase());
		assertEquals(patientTestConfig.getExpectedPatientLastVisitPatientCaseDesc(),
				patient.getLastVisitPatientCaseDesc());
		assertEquals(patientTestConfig.getExpectedPatientLastVisitPatientUnit(), patient.getLastVisitPatientUnit());
		assertEquals(patientTestConfig.getExpectedPatientLastVisitPatientUnitDesc(),
				patient.getLastVisitPatientUnitDesc());
		assertEquals(patientTestConfig.getExpectedPatientLastVisitHospService(), patient.getLastVisitHospService());
		assertEquals(patientTestConfig.getExpectedPatientLastVisitHospServiceDesc(),
				patient.getLastVisitHospServiceDesc());
		assertEquals(patientTestConfig.getExpectedPatientLastVisitAdmissionType(), patient.getLastVisitAdmissionType());
		assertEquals(patientTestConfig.getExpectedPatientLastVisitAdmissionTypeDesc(),
				patient.getLastVisitAdmissionTypeDesc());
		assertEquals(patientTestConfig.getExpectedPatientLastVisitPatientRoom(), patient.getLastVisitPatientRoom());
		assertEquals(patientTestConfig.getExpectedPatientLastVisitPatientBed(), patient.getLastVisitPatientBed());
		assertEquals(patientTestConfig.getExpectedPatientLastVisitFinancialClass(),
				patient.getLastVisitFinancialClass());
		assertEquals(patientTestConfig.getExpectedPatientLastVisitFinancialClassDesc(),
				patient.getLastVisitFinancialClassDesc());
	}

	@Test
	@Ignore("Test does not compile")
	public void mustListByMyUnitTest() {
		int companyId = Integer.valueOf(patientTestConfig.getExpectedMyUnitCompanyId());
		String patientunit = patientTestConfig.getExpectedMyUnitPatientunit();
		List<PatientInList> patients = patientInListRepository.findByMyUnit(companyId, patientunit, 100, 0, null, null);
		assertNotNull(patients);
		assertEquals(patientTestConfig.getExpectedMyUnitNb_records(), patients.get(0).getNbRecords());
		assertTrue(patients.size() <= 100);
		assertEquals(patientTestConfig.getExpectedMyUnitLocality(), patients.get(0).getLocality());
		assertEquals(patientTestConfig.getExpectedMyUnitLastname(), patients.get(0).getLastName());
		assertEquals(patientTestConfig.getExpectedMyUnitSex(), patients.get(0).getSex());
		assertEquals(patientTestConfig.getExpectedMyUnitAdress(), patients.get(0).getAddress());
		assertEquals(patientTestConfig.getExpectedMyUnitPostcode(), patients.get(0).getPostCode());
		assertEquals(patientTestConfig.getExpectedMyUnitAdmitdt(), patients.get(0).getLastVisitAdmitDate());
		assertEquals(patientTestConfig.getExpectedMyUnitPatientclass(), patients.get(0).getLastVisitPatientClass());
		assertEquals(patientTestConfig.getExpectedMyUnitPatienttype(), patients.get(0).getLastVisitPatientType());
		
	}

}
