/**
 * 
 */
package net.fluance.cockpit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan("net.fluance.cockpit.app")
@PropertySource({ "classpath:webapps/conf/core.properties", "classpath:webapps/conf/security.properties",
		"classpath:test.properties" })
public class NextOfKinControllerTestConfig {

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

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfig() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
