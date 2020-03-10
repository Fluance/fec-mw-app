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
public class LabControllerTestConfig {

	@Value("${swagger.specs.location}")
	private String specsLocation;
	@Value("${swagger.lab.spec.file}")
	private String labSpecFile;
	@Value("${net.fluance.cockpit.app.web.controller.lab.expectedGroupNameCount}")
	private String expectedGroupNameCount;
	@Value("${net.fluance.cockpit.app.web.controller.lab.groupName}")
	private String groupName;
	@Value("${net.fluance.cockpit.app.web.controller.lab.expectedDataForGrouNameCount}")
	private String expectedDataForGrouNameCount;
	@Value("${net.fluance.cockpit.app.web.controller.lab.pid}")
	private String pid;
	@Value("${net.fluance.cockpit.app.web.controllerlab.allgroupnames}")
	private String allGroupNames;
	@Value("${net.fluance.cockpit.app.web.controller.lab.pidWithoutResults}")
	private String pidWithoutResults;

	@Value("${net.fluance.cockpit.app.web.controller.lab.data.pid}")
	private Long labDataPid;
	@Value("${net.fluance.cockpit.app.web.controller.lab.data.groupName}")
	private String labDataGroupName;
	@Value("${net.fluance.cockpit.app.web.controller.lab.data.observationDate}")
	private String labDataObservationDate;
	@Value("${net.fluance.cockpit.app.web.controller.lab.data.analysisName}")
	private String labDataAnalysisName;
	@Value("${net.fluance.cockpit.app.web.controller.lab.data.value}")
	private String labDataValue;
	@Value("${net.fluance.cockpit.app.web.controller.lab.data.valueType}")
	private String labDataValueType;
	@Value("${net.fluance.cockpit.app.web.controller.lab.data.unit}")
	private String labDataUnit;
	@Value("${net.fluance.cockpit.app.web.controller.lab.data.refRange}")
	private String labDataRefRange;
	@Value("${net.fluance.cockpit.app.web.controller.lab.data.abnormalFlag}")
	private String labDataAbnormalFlag;
	@Value("${net.fluance.cockpit.app.web.controller.lab.data.abnormalFlagDesc}")
	private String labDataAbnormalFlagDesc;
	@Value("${net.fluance.cockpit.app.web.controller.lab.data.resultStatus}")
	private String labDataResultStatus;
	@Value("${net.fluance.cockpit.app.web.controller.lab.data.resultStatusDesc}")
	private String labDataResultStatusDesc;
	@Value("${net.fluance.cockpit.app.web.controller.lab.data.comments}")
	private String labDataComment;
	@Value("${net.fluance.cockpit.app.web.controller.lab.data.count}")
	private String labDataCount;

	/**
	 * @return the labSpecFile
	 */
	public String getLabSpecFile() {
		return labSpecFile;
	}

	/**
	 * @return the expectedGroupNameCount
	 */
	public String getExpectedGroupNameCount() {
		return expectedGroupNameCount;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @return the expectedDataForGrouNameCount
	 */
	public String getExpectedDataForGrouNameCount() {
		return expectedDataForGrouNameCount;
	}

	/**
	 * @return the pid
	 */
	public String getPid() {
		return pid;
	}

	/**
	 * @return the allGroupNames
	 */
	public String getAllGroupNames() {
		return allGroupNames;
	}

	/**
	 * @return the pidWithoutResults
	 */
	public String getPidWithoutResults() {
		return pidWithoutResults;
	}

	/**
	 * @return the specsLocation
	 */
	public String getSpecsLocation() {
		return specsLocation;
	}

	/**
	 * @return the labDataPid
	 */
	public Long getLabDataPid() {
		return labDataPid;
	}

	/**
	 * @return the labDataGroupName
	 */
	public String getLabDataGroupName() {
		return labDataGroupName;
	}

	/**
	 * @return the labDataObservationDate
	 */
	public String getLabDataObservationDate() {
		return labDataObservationDate;
	}

	/**
	 * @return the labDataAnalysisName
	 */
	public String getLabDataAnalysisName() {
		return labDataAnalysisName;
	}

	/**
	 * @return the labDataValue
	 */
	public String getLabDataValue() {
		return labDataValue;
	}

	/**
	 * @return the labDataValueType
	 */
	public String getLabDataValueType() {
		return labDataValueType;
	}

	/**
	 * @return the labDataUnit
	 */
	public String getLabDataUnit() {
		return labDataUnit;
	}

	/**
	 * @return the labDataRefRange
	 */
	public String getLabDataRefRange() {
		return labDataRefRange;
	}

	/**
	 * @return the labDataAbnormalFlag
	 */
	public String getLabDataAbnormalFlag() {
		return labDataAbnormalFlag;
	}

	/**
	 * @return the labDataAbnormalFlagDesc
	 */
	public String getLabDataAbnormalFlagDesc() {
		return labDataAbnormalFlagDesc;
	}

	/**
	 * @return the labDataResultStatus
	 */
	public String getLabDataResultStatus() {
		return labDataResultStatus;
	}

	/**
	 * @return the labDataResultStatusDesc
	 */
	public String getLabDataResultStatusDesc() {
		return labDataResultStatusDesc;
	}

	/**
	 * @return the labDataComment
	 */
	public String getLabDataComment() {
		return labDataComment;
	}

	/**
	 * @return the labDataCount
	 */
	public String getLabDataCount() {
		return labDataCount;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfig() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
