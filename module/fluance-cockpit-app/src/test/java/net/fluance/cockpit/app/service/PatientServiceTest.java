package net.fluance.cockpit.app.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.apache.http.HttpException;
import org.apache.http.ParseException;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.context.WebApplicationContext;
import org.xml.sax.SAXException;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import net.fluance.app.test.AbstractWebIntegrationTest;
import net.fluance.cockpit.app.Application;
import net.fluance.cockpit.app.service.domain.PatientService;
import net.fluance.cockpit.core.model.jdbc.patient.Patient;
import net.fluance.cockpit.core.model.jdbc.patient.PatientInList;
import net.fluance.cockpit.core.model.wrap.patient.AdmissionStatusEnum;
import net.fluance.cockpit.core.model.wrap.patient.PatientSexEnum;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientContactRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientInListRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientNextOfKinRepository;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientRepository;

@ComponentScan(basePackages={"net.fluance.cockpit.core", "net.fluance.cockpit.app"})
@SpringBootTest(classes = Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class PatientServiceTest extends AbstractWebIntegrationTest {

	@Autowired
	private PatientService patientService;

	@Autowired
	protected WebApplicationContext wac;

	@Value("${net.fluance.cockpit.app.web.company.userUsername}")
	private String okUserUsername;
	@Value("${net.fluance.cockpit.app.web.company.userPassword}")
	private String okUserPassword;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserUsername}")
	private String notAllowedUserUsername;
	@Value("${net.fluance.cockpit.app.web.company.notAllowedUserPassword}")
	private String notAllowedUserPassword;

	
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedInHousePatientLastNameStartLetters}")
	private String expectedInHousePatientLastNameStartLetters;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPreAdmittedPatientLastNameStartLetters}")
	private String expectedPreAdmittedPatientLastNameStartLetters;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPreAdmittedCompanyId}")
	private String expectedPreAdmittedCompanyId;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastNameStartLetters}")
	private String expectedPatientLastNameStartLetters;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientFirstNameStartLetters}")
	private String expectedPatientFirstNameStartLetters;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientMaidenNameStartLetters}")
	private String expectedPatientMaidenNameStartLetters;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientId}")
	private String expectedPatientId;
	@Value("${net.fluance.cockpit.core.model.repository.patient.unexpectedPatientId}")
	private String unexpectedPatientId;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientFirstName}")
	private String expectedPatientFirstName;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastName}")
	private String expectedPatientLastName;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientMaidenName}")
	private String expectedPatientMaidenName;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientDateOfBirth}")
	private String expectedPatientDateOfBirth;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientNationality}")
	private String expectedPatientNationality;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLanguage}")
	private String expectedPatientLanguage;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientAvsNumber}")
	private String expectedPatientAvsNumber;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientSex}")
	private String expectedPatientSex;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientAddress}")
	private String expectedPatientAddress;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientAddress2}")
	private String expectedPatientAddress2;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientComplement}")
	private String expectedPatientComplement;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientCareOf}")
	private String expectedPatientCareOf;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLocality}")
	private String expectedPatientLocality;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientPostCode}")
	private String expectedPatientPostCode;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientSubPostCode}")
	private String expectedPatientSubPostCode;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientCanton}")
	private String expectedPatientCanton;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientCountry}")
	private String expectedPatientCountry;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitNumber}")
	private String expectedPatientLastVisitNumber;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitCompanyId}")
	private String expectedPatientLastVisitCompanyId;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitAdmitDate}")
	private String expectedPatientLastVisitAdmitDate;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitDischargeDate}")
	private String expectedPatientLastVisitDischargeDate;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientClass}")
	private String expectedPatientLastVisitPatientClass;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientClassDesc}")
	private String expectedPatientLastVisitPatientClassDesc;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientType}")
	private String expectedPatientLastVisitPatientType;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientTypeDesc}")
	private String expectedPatientLastVisitPatientTypeDesc;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientCase}")
	private String expectedPatientLastVisitPatientCase;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientCaseDesc}")
	private String expectedPatientLastVisitPatientCaseDesc;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitHospService}")
	private String expectedPatientLastVisitHospService;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitHospServiceDesc}")
	private String expectedPatientLastVisitHospServiceDesc;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitAdmissionType}")
	private String expectedPatientLastVisitAdmissionType;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitAdmissionTypeDesc}")
	private String expectedPatientLastVisitAdmissionTypeDesc;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientUnit}")
	private String expectedPatientLastVisitPatientUnit;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientUnitDesc}")
	private String expectedPatientLastVisitPatientUnitDesc;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientRoom}")
	private String expectedPatientLastVisitPatientRoom;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitPatientBed}")
	private String expectedPatientLastVisitPatientBed;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitFinancialClass}")
	private String expectedPatientLastVisitFinancialClass;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientLastVisitFinancialClassDesc}")
	private String expectedPatientLastVisitFinancialClassDesc;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientByLastNameCount}")
	private String expectedPatientByLastNameCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientByLastNameAndFirstNameCount}")
	private String expectedPatientByLastNameAndFirstNameCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientByBirthDateCount}")
	private String expectedPatientByBirthDateCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientByLastNameAndBirthDateCount}")
	private String expectedPatientByLastNameAndBirthDateCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientByLastNameAndFirstNameAndBirthDateCount}")
	private String expectedPatientByLastNameAndFirstNameAndBirthDateCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPreAdmittedPatientByLastNameCount}")
	private String expectedPreAdmittedPatientByLastNameCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPreAdmittedPatientByLastNameAndFirstNameCount}")
	private String expectedPreAdmittedPatientByLastNameAndFirstNameCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPreAdmittedPatientByBirthDateCount}")
	private String expectedPreAdmittedPatientByBirthDateCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPreAdmittedPatientByLastNameAndBirthDateCount}")
	private String expectedPreAdmittedPatientByLastNameAndBirthDateCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPreAdmittedPatientByLastNameAndFirstNameAndBirthDateCount}")
	private String expectedPreAdmittedPatientByLastNameAndFirstNameAndBirthDateCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedInHousePatientByLastNameCount}")
	private String expectedInHousePatientByLastNameCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedInHousePatientByLastNameAndFirstNameCount}")
	private String expectedInHousePatientByLastNameAndFirstNameCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedInHousePatientByBirthDateCount}")
	private String expectedInHousePatientByBirthDateCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedInHousePatientByLastNameAndBirthDateCount}")
	private String expectedInHousePatientByLastNameAndBirthDateCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedInHousePatientByLastNameAndFirstNameAndBirthDateCount}")
	private String expectedInHousePatientByLastNameAndFirstNameAndBirthDateCount;
			
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientByLastNameAndFirstNameAndMaidenNameAndBirthDateCount}")
	private String expectedPatientByLastNameAndFirstNameAndMaidenNameAndBirthDateCount;
	
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientByLastNameAndFirstNameAndMaidenNameCount}")
	private String expectedPatientByLastNameAndFirstNameAndMaidenNameCount;
	
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientVisitAttendingStaffId}")
	private String expectedPatientVisitAttendingStaffId;
	
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientsByAttendingStaffIdCount}")
	private String expectedPatientsByAttendingStaffIdCount;
	
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientByVisitNbCount}")
	private String expectedPatientByVisitNbCount;
	
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientContactEquipment}")
	private String expectedPatientContactEquipment;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientContactData}")
	private String expectedPatientContactData;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPatientContactsCount}")
	private String expectedPatientContactsCount;
	
	@Value("${swagger.specs.location}")
	private String specsLocation;
	@Value("${swagger.nextofkin.spec.file}")
	private String labSpecFile;

	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokId}")
	private String expectedNokId;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokPatientId}")
	private String expectedNokPatientId;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokFirstName}")
	private String expectedNokFirstName;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokLastName}")
	private String expectedNokLastName;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokAddressType}")
	private String expectedNokAddressType;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokType}")
	private String expectedNokType;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokAddress}")
	private String expectedNokAddress;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokAddress2}")
	private String expectedNokAddress2;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokCareOf}")
	private String expectedNokCareOf;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokLocality}")
	private String expectedNokLocality;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokPostCode}")
	private String expectedNokPostCode;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokCanton}")
	private String expectedNokCanton;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokCountry}")
	private String expectedNokCountry;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokComplement}")
	private String expectedNokComplement;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokCount}")
	private String expectedNokCount;
//	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokContactId}")
//	private String expectedNokContactId;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokContactEquipment}")
	private String expectedNokContactEquipment;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokContactData}")
	private String expectedNokContactData;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedNokContactsCount}")
	private String expectedNokContactsCount;
	@Value("${net.fluance.cockpit.core.model.repository.patient.expectedPidWithNoNok}")
	private String expectedPidWithNoNok;
	
	@Configuration
	public static class TestConfig {

	}
	@Autowired
	PatientRepository patientRepository;

	@Autowired
	PatientNextOfKinRepository patientNextOfKinRepository;

	@Autowired
	PatientInListRepository patientInListRepository;

	@Autowired
	PatientContactRepository patientContactRepository;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void mustFindDetailTest() {
		Long patientId = Long.valueOf(expectedPatientId);
		when(patientRepository.findOne(anyLong())).thenReturn(new Patient((long) 2342, "language", "courtesy", "firstname", "lastname", "maidenName", new java.util.Date(),
				"avsNumber", "nationality","sex", "address", "address2", "", "postCode", "subPostCode", "adressComplement", "careOf", "canton", "country", false, new Date(), null));
		net.fluance.cockpit.core.model.wrap.patient.Patient foundPatient = patientService.patientDetail(patientId);
		assertNotNull(foundPatient);
		assertNotNull(foundPatient.getContacts());
		
		//Test findDetail = null
		patientId = (long) 12345;
		when(patientRepository.findOne((long) 12345)).thenReturn(null);
		foundPatient = patientService.patientDetail(patientId);
		assertNull(foundPatient);
		
	}

	@Override
	public void tearDown() {
	}

	@Override
	protected boolean checkOAuth2Authorization(Object... params) {
		return false;
	}

	@Override
	public void checkOk(Object... params) throws KeyManagementException, UnsupportedOperationException,
	NoSuchAlgorithmException, KeyStoreException, IOException, HttpException, URISyntaxException,
	ProcessingException, NumberFormatException, ParseException, XPathExpressionException,
	ParserConfigurationException, SAXException, TransformerFactoryConfigurationError, TransformerException {
	}

	@Override
	public Logger getLogger() {
		return null;
	}	

}
