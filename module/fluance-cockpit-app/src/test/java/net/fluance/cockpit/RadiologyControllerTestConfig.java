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
public class RadiologyControllerTestConfig {

	@Value("${swagger.specs.location}")
	private String specsLocation;
	@Value("${swagger.radiology.spec.file}")
	private String radiologySpecFile;
	@Value("${net.fluance.cockpit.app.web.controller.radiology.pid}")
	private String pid;
	@Value("${net.fluance.cockpit.app.web.controller.radiology.orderNb}")
	private String orderNb;
	@Value("${net.fluance.cockpit.app.web.controller.radiology.observationDate}")
	private String observationDate;
	@Value("${net.fluance.cockpit.app.web.controller.radiology.diagnosticService}")
	private String diagnosticService;
	@Value("${net.fluance.cockpit.app.web.controller.radiology.identifierCode}")
	private String identifierCode;
	@Value("${net.fluance.cockpit.app.web.controller.radiology.observation}")
	private String observation;
	@Value("${net.fluance.cockpit.app.web.controller.radiology.resultUrl}")
	private String resultUrl;
	@Value("${net.fluance.cockpit.app.web.controller.radiology.companyId}")
	private String companyId;
	@Value("${net.fluance.cockpit.app.web.controller.radiology.name}")
	private String name;
	@Value("${net.fluance.cockpit.app.web.controller.radiology.examCount}")
	private String examCount;
	@Value("${net.fluance.cockpit.app.web.controller.radiology.pidWithNoExam}")
	private String pidWithNoExam;	
	
	
	/**
	 * @return the specsLocation
	 */
	public String getSpecsLocation() {
		return specsLocation;
	}
	
	/**
	 * @return the radiologySpecFile
	 */
	public String getRadiologySpecFile() {
		return radiologySpecFile;
	}

	/**
	 * @return the pid
	 */
	public String getPid() {
		return pid;
	}

	/**
	 * @return the orderNb
	 */
	public String getOrderNb() {
		return orderNb;
	}

	/**
	 * @return the observationDate
	 */
	public String getObservationDate() {
		return observationDate;
	}

	/**
	 * @return the diagnosticService
	 */
	public String getDiagnosticService() {
		return diagnosticService;
	}

	/**
	 * @return the identifierCode
	 */
	public String getIdentifierCode() {
		return identifierCode;
	}

	/**
	 * @return the observation
	 */
	public String getObservation() {
		return observation;
	}

	/**
	 * @return the resultUrl
	 */
	public String getResultUrl() {
		return resultUrl;
	}

	/**
	 * @return the companyId
	 */
	public String getCompanyId() {
		return companyId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the examCount
	 */
	public String getExamCount() {
		return examCount;
	}
	
	/**
	 * @return the pidWithNoExam
	 */
	public String getPidWithNoExam() {
		return pidWithNoExam;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfig() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
