/**
 * 
 */
package net.fluance.cockpit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.test.context.TestPropertySource;

import net.fluance.cockpit.app.Application;

@Configuration
@ComponentScan("net.fluance.cockpit.core")
@SpringBootTest(classes= Application.class)
@TestPropertySource(locations="classpath:test.properties")
public class PatientTestConfig {
	
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
	
	/**
	 * @return the expectedInHousePatientLastNameStartLetters
	 */
	public String getExpectedInHousePatientLastNameStartLetters() {
		return expectedInHousePatientLastNameStartLetters;
	}

	/**
	 * @return the expectedPreAdmittedPatientLastNameStartLetters
	 */
	public String getExpectedPreAdmittedPatientLastNameStartLetters() {
		return expectedPreAdmittedPatientLastNameStartLetters;
	}

	/**
	 * @return the expectedPatientLastNameStartLetters
	 */
	public String getExpectedPatientLastNameStartLetters() {
		return expectedPatientLastNameStartLetters;
	}

	/**
	 * @return the expectedPatientFirstNameStartLetters
	 */
	public String getExpectedPatientFirstNameStartLetters() {
		return expectedPatientFirstNameStartLetters;
	}

	/**
	 * @return the expectedPatientMaidenNameStartLetters
	 */
	public String getExpectedPatientMaidenNameStartLetters() {
		return expectedPatientMaidenNameStartLetters;
	}
	
	/**
	 * @return the expectedPreAdmittedCompanyId
	 */
	public String getExpectedPreAdmittedCompanyId() {
		return expectedPreAdmittedCompanyId;
	}

	/**
	 * @return the expectedPatientId
	 */
	public String getExpectedPatientId() {
		return expectedPatientId;
	}

	/**
	 * @param expectedPatientId the expectedPatientId to set
	 */
	public void setExpectedPatientId(String expectedPatientId) {
		this.expectedPatientId = expectedPatientId;
	}

	/**
	 * @return the unexpectedPatientId
	 */
	public String getUnexpectedPatientId() {
		return unexpectedPatientId;
	}

	/**
	 * @param unexpectedPatientId the unexpectedPatientId to set
	 */
	public void setUnexpectedPatientId(String unexpectedPatientId) {
		this.unexpectedPatientId = unexpectedPatientId;
	}

	/**
	 * @return the expectedPatientFirstName
	 */
	public String getExpectedPatientFirstName() {
		return expectedPatientFirstName;
	}

	/**
	 * @param expectedPatientFirstName the expectedPatientFirstName to set
	 */
	public void setExpectedPatientFirstName(String expectedPatientFirstName) {
		this.expectedPatientFirstName = expectedPatientFirstName;
	}

	/**
	 * @return the expectedPatientLastName
	 */
	public String getExpectedPatientLastName() {
		return expectedPatientLastName;
	}

	/**
	 * @param expectedPatientLastName the expectedPatientLastName to set
	 */
	public void setExpectedPatientLastName(String expectedPatientLastName) {
		this.expectedPatientLastName = expectedPatientLastName;
	}

	/**
	 * @return the expectedPatientMaidenName
	 */
	public String getExpectedPatientMaidenName() {
		return expectedPatientMaidenName;
	}

	/**
	 * @param expectedPatientMaidenName the expectedPatientMaidenName to set
	 */
	public void setExpectedPatientMaidenName(String expectedPatientMaidenName) {
		this.expectedPatientMaidenName = expectedPatientMaidenName;
	}

	/**
	 * @return the expectedPatientDateOfBirth
	 */
	public String getExpectedPatientDateOfBirth() {
		return expectedPatientDateOfBirth;
	}

	/**
	 * @param expectedPatientDateOfBirth the expectedPatientDateOfBirth to set
	 */
	public void setExpectedPatientDateOfBirth(String expectedPatientDateOfBirth) {
		this.expectedPatientDateOfBirth = expectedPatientDateOfBirth;
	}

	/**
	 * @return the expectedPatientNationality
	 */
	public String getExpectedPatientNationality() {
		return expectedPatientNationality;
	}

	/**
	 * @return the expectedPatientLanguage
	 */
	public String getExpectedPatientLanguage() {
		return expectedPatientLanguage;
	}

	/**
	 * @return the expectedPatientAvsNumber
	 */
	public String getExpectedPatientAvsNumber() {
		return expectedPatientAvsNumber;
	}

	/**
	 * @return the expectedPatientSex
	 */
	public String getExpectedPatientSex() {
		return expectedPatientSex;
	}

	/**
	 * @param expectedPatientSex the expectedPatientSex to set
	 */
	public void setExpectedPatientSex(String expectedPatientSex) {
		this.expectedPatientSex = expectedPatientSex;
	}

	/**
	 * @return the expectedPatientAddressLine
	 */
	public String getExpectedPatientAddress() {
		return expectedPatientAddress;
	}

	/**
	 * @return the expectedPatientAddressLine2
	 */
	public String getExpectedPatientAddress2() {
		return expectedPatientAddress2;
	}
	
	/**
	 * @return the expectedPatientComplement
	 */
	public String getExpectedPatientComplement() {
		return expectedPatientComplement;
	}

	/**
	 * @return the expectedPatientCareOf
	 */
	public String getExpectedPatientCareOf() {
		return expectedPatientCareOf;
	}

	/**
	 * @return the expectedPatientLocality
	 */
	public String getExpectedPatientLocality() {
		return expectedPatientLocality;
	}

	/**
	 * @return the expectedPatientPostCode
	 */
	public String getExpectedPatientPostCode() {
		return expectedPatientPostCode;
	}

	/**
	 * @return the expectedPatientSubPostCode
	 */
	public String getExpectedPatientSubPostCode() {
		return expectedPatientSubPostCode;
	}

	/**
	 * @return the expectedPatientCanton
	 */
	public String getExpectedPatientCanton() {
		return expectedPatientCanton;
	}

	/**
	 * @return the expectedPatientCountry
	 */
	public String getExpectedPatientCountry() {
		return expectedPatientCountry;
	}

	/**
	 * @return the expectedPatientLastVisitNumber
	 */
	public String getExpectedPatientLastVisitNumber() {
		return expectedPatientLastVisitNumber;
	}

	/**
	 * @return the expectedPatientLastVisitCompanyId
	 */
	public String getExpectedPatientLastVisitCompanyId() {
		return expectedPatientLastVisitCompanyId;
	}

	/**
	 * @return the expectedPatientLastVisitAdmitDate
	 */
	public String getExpectedPatientLastVisitAdmitDate() {
		return expectedPatientLastVisitAdmitDate;
	}

	/**
	 * @return the expectedPatientLastVisitDischargeDate
	 */
	public String getExpectedPatientLastVisitDischargeDate() {
		return expectedPatientLastVisitDischargeDate;
	}

	/**
	 * @return the expectedPatientLastVisitPatientClass
	 */
	public String getExpectedPatientLastVisitPatientClass() {
		return expectedPatientLastVisitPatientClass;
	}

	/**
	 * @return the expectedPatientLastVisitPatientClassDesc
	 */
	public String getExpectedPatientLastVisitPatientClassDesc() {
		return expectedPatientLastVisitPatientClassDesc;
	}

	/**
	 * @return the expectedPatientLastVisitPatientType
	 */
	public String getExpectedPatientLastVisitPatientType() {
		return expectedPatientLastVisitPatientType;
	}

	/**
	 * @return the expectedPatientLastVisitPatientTypeDesc
	 */
	public String getExpectedPatientLastVisitPatientTypeDesc() {
		return expectedPatientLastVisitPatientTypeDesc;
	}

	/**
	 * @return the expectedPatientLastVisitPatientCase
	 */
	public String getExpectedPatientLastVisitPatientCase() {
		return expectedPatientLastVisitPatientCase;
	}

	/**
	 * @return the expectedPatientLastVisitPatientCaseDesc
	 */
	public String getExpectedPatientLastVisitPatientCaseDesc() {
		return expectedPatientLastVisitPatientCaseDesc;
	}

	/**
	 * @return the expectedPatientLastVisitHospService
	 */
	public String getExpectedPatientLastVisitHospService() {
		return expectedPatientLastVisitHospService;
	}

	/**
	 * @return the expectedPatientLastVisitHospServiceDesc
	 */
	public String getExpectedPatientLastVisitHospServiceDesc() {
		return expectedPatientLastVisitHospServiceDesc;
	}

	/**
	 * @return the expectedPatientLastVisitAdmissionType
	 */
	public String getExpectedPatientLastVisitAdmissionType() {
		return expectedPatientLastVisitAdmissionType;
	}

	/**
	 * @return the expectedPatientLastVisitAdmissionTypeDesc
	 */
	public String getExpectedPatientLastVisitAdmissionTypeDesc() {
		return expectedPatientLastVisitAdmissionTypeDesc;
	}

	/**
	 * @return the expectedPatientLastVisitPatientUnit
	 */
	public String getExpectedPatientLastVisitPatientUnit() {
		return expectedPatientLastVisitPatientUnit;
	}

	/**
	 * @return the expectedPatientLastVisitPatientUnitDesc
	 */
	public String getExpectedPatientLastVisitPatientUnitDesc() {
		return expectedPatientLastVisitPatientUnitDesc;
	}

	/**
	 * @return the expectedPatientLastVisitPatientRoom
	 */
	public String getExpectedPatientLastVisitPatientRoom() {
		return expectedPatientLastVisitPatientRoom;
	}

	/**
	 * @return the expectedPatientLastVisitPatientBed
	 */
	public String getExpectedPatientLastVisitPatientBed() {
		return expectedPatientLastVisitPatientBed;
	}

	/**
	 * @return the expectedPatientLastVisitFinancialClass
	 */
	public String getExpectedPatientLastVisitFinancialClass() {
		return expectedPatientLastVisitFinancialClass;
	}

	/**
	 * @return the expectedPatientLastVisitFinancialClassDesc
	 */
	public String getExpectedPatientLastVisitFinancialClassDesc() {
		return expectedPatientLastVisitFinancialClassDesc;
	}

	/**
	 * @return the expectedPatientByLastNameCount
	 */
	public String getExpectedPatientByLastNameCount() {
		return expectedPatientByLastNameCount;
	}

	/**
	 * @return the expectedPatientByLastNameAndFirstNameCount
	 */
	public String getExpectedPatientByLastNameAndFirstNameCount() {
		return expectedPatientByLastNameAndFirstNameCount;
	}

	/**
	 * @return the expectedPatientByBirthDateCount
	 */
	public String getExpectedPatientByBirthDateCount() {
		return expectedPatientByBirthDateCount;
	}

	/**
	 * @return the expectedPatientByLastNameAndBirthDateCount
	 */
	public String getExpectedPatientByLastNameAndBirthDateCount() {
		return expectedPatientByLastNameAndBirthDateCount;
	}

	/**
	 * @return the expectedPatientByLastNameAndFirstNameAndBirthDateCount
	 */
	public String getExpectedPatientByLastNameAndFirstNameAndBirthDateCount() {
		return expectedPatientByLastNameAndFirstNameAndBirthDateCount;
	}

	/**
	 * @return the expectedPreAdmittedPatientByLastNameCount
	 */
	public String getExpectedPreAdmittedPatientByLastNameCount() {
		return expectedPreAdmittedPatientByLastNameCount;
	}

	/**
	 * @return the expectedPreAdmittedPatientByLastNameAndFirstNameCount
	 */
	public String getExpectedPreAdmittedPatientByLastNameAndFirstNameCount() {
		return expectedPreAdmittedPatientByLastNameAndFirstNameCount;
	}

	/**
	 * @return the expectedPreAdmittedPatientByBirthDateCount
	 */
	public String getExpectedPreAdmittedPatientByBirthDateCount() {
		return expectedPreAdmittedPatientByBirthDateCount;
	}

	/**
	 * @return the expectedPreAdmittedPatientByLastNameAndBirthDateCount
	 */
	public String getExpectedPreAdmittedPatientByLastNameAndBirthDateCount() {
		return expectedPreAdmittedPatientByLastNameAndBirthDateCount;
	}

	/**
	 * @return the expectedPreAdmittedPatientByLastNameAndFirstNameAndBirthDateCount
	 */
	public String getExpectedPreAdmittedPatientByLastNameAndFirstNameAndBirthDateCount() {
		return expectedPreAdmittedPatientByLastNameAndFirstNameAndBirthDateCount;
	}

	/**
	 * @return the expectedInHousePatientByLastNameCount
	 */
	public String getExpectedInHousePatientByLastNameCount() {
		return expectedInHousePatientByLastNameCount;
	}

	/**
	 * @return the expectedInHousePatientByLastNameAndFirstNameCount
	 */
	public String getExpectedInHousePatientByLastNameAndFirstNameCount() {
		return expectedInHousePatientByLastNameAndFirstNameCount;
	}

	/**
	 * @return the expectedInHousePatientByBirthDateCount
	 */
	public String getExpectedInHousePatientByBirthDateCount() {
		return expectedInHousePatientByBirthDateCount;
	}

	/**
	 * @return the expectedInHousePatientByLastNameAndBirthDateCount
	 */
	public String getExpectedInHousePatientByLastNameAndBirthDateCount() {
		return expectedInHousePatientByLastNameAndBirthDateCount;
	}

	/**
	 * @return the expectedInHousePatientByLastNameAndFirstNameAndBirthDateCount
	 */
	public String getExpectedInHousePatientByLastNameAndFirstNameAndBirthDateCount() {
		return expectedInHousePatientByLastNameAndFirstNameAndBirthDateCount;
	}
	
	/**
	 * @return the expectedPatientByLastNameAndFirstNameAndMaidenNameAndBirthDateCount
	 */
	public String getExpectedPatientByLastNameAndFirstNameAndMaidenNameAndBirthDateCount() {
		return expectedPatientByLastNameAndFirstNameAndMaidenNameAndBirthDateCount;
	}
	
	/**
	 * @return the expectedPatientByLastNameAndFirstNameAndMaidenNameCount
	 */
	public String getExpectedPatientByLastNameAndFirstNameAndMaidenNameCount() {
		return expectedPatientByLastNameAndFirstNameAndMaidenNameCount;
	}
	
	/**
	 * @return the expectedPatientVisitAttendingStaffId
	 */
	public String getExpectedPatientVisitAttendingStaffId() {
		return expectedPatientVisitAttendingStaffId;
	}

	/**
	 * @return the expectedPatientsByAttendingStaffIdCount
	 */
	public String getExpectedPatientsByAttendingStaffIdCount() {
		return expectedPatientsByAttendingStaffIdCount;
	}

	/**
	 * @return the expectedPatientByVisitNbCount
	 */
	public String getExpectedPatientByVisitNbCount() {
		return expectedPatientByVisitNbCount;
	}

	/**
	 * @return the expectedPatientContactEquipment
	 */
	public String getExpectedPatientContactEquipment() {
		return expectedPatientContactEquipment;
	}

	/**
	 * @return the expectedPatientContactData
	 */
	public String getExpectedPatientContactData() {
		return expectedPatientContactData;
	}

	/**
	 * @return the expectedPatientContactsCount
	 */
	public String getExpectedPatientContactsCount() {
		return expectedPatientContactsCount;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfig() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
		
	
	/**
	 * @return the expectedPidWithNoNok
	 */
	public String getExpectedPidWithNoNok() {
		return expectedPidWithNoNok;
	}

	/**
	 * @return the labSpecFile
	 */
	public String getLabSpecFile() {
		return labSpecFile;
	}

	/**
	 * @return the specsLocation
	 */
	public String getSpecsLocation() {
		return specsLocation;
	}

	/**
	 * @return the expectedNokId
	 */
	public String getExpectedNokId() {
		return expectedNokId;
	}

	/**
	 * @return the expectedNokPatientId
	 */
	public String getExpectedNokPatientId() {
		return expectedNokPatientId;
	}

	/**
	 * @return the expectedNokFirstName
	 */
	public String getExpectedNokFirstName() {
		return expectedNokFirstName;
	}

	/**
	 * @return the expectedNokLastName
	 */
	public String getExpectedNokLastName() {
		return expectedNokLastName;
	}

	/**
	 * @return the expectedNokAddressType
	 */
	public String getExpectedNokAddressType() {
		return expectedNokAddressType;
	}

	/**
	 * @return the expectedNokType
	 */
	public String getExpectedNokType() {
		return expectedNokType;
	}

	/**
	 * @return the expectedNokAddress
	 */
	public String getExpectedNokAddress() {
		return expectedNokAddress;
	}

	/**
	 * @return the expectedNokAddress2
	 */
	public String getExpectedNokAddress2() {
		return expectedNokAddress2;
	}

	/**
	 * @return the expectedNokCareOf
	 */
	public String getExpectedNokCareOf() {
		return expectedNokCareOf;
	}

	/**
	 * @return the expectedNokLocality
	 */
	public String getExpectedNokLocality() {
		return expectedNokLocality;
	}

	/**
	 * @return the expectedNokPostCode
	 */
	public String getExpectedNokPostCode() {
		return expectedNokPostCode;
	}

	/**
	 * @return the expectedNokCanton
	 */
	public String getExpectedNokCanton() {
		return expectedNokCanton;
	}

	/**
	 * @return the expectedNokCountry
	 */
	public String getExpectedNokCountry() {
		return expectedNokCountry;
	}

	/**
	 * @return the expectedNokComplement
	 */
	public String getExpectedNokComplement() {
		return expectedNokComplement;
	}

	/**
	 * @return the expectedNokCount
	 */
	public String getExpectedNokCount() {
		return expectedNokCount;
	}

	/**
	 * @return the expectedNokContactEquipment
	 */
	public String getExpectedNokContactEquipment() {
		return expectedNokContactEquipment;
	}

	/**
	 * @return the expectedNokContactData
	 */
	public String getExpectedNokContactData() {
		return expectedNokContactData;
	}

	/**
	 * @return the expectedNokContactsCount
	 */
	public String getExpectedNokContactsCount() {
		return expectedNokContactsCount;
	}

	

	
}
