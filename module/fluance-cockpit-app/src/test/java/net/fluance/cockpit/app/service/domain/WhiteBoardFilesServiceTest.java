package net.fluance.cockpit.app.service.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
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
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.fluance.cockpit.app.web.util.whiteboard.WhiteBoardMock;
import net.fluance.cockpit.core.model.jdbc.company.CompanyDetails;
import net.fluance.cockpit.core.model.jdbc.company.ServiceList;
import net.fluance.cockpit.core.model.jpa.catalog.CatalogDTO;
import net.fluance.cockpit.core.repository.jdbc.catalog.CatalogRepository;
import net.fluance.cockpit.core.repository.jdbc.company.CompanyDetailsRepository;
import net.fluance.cockpit.core.repository.jdbc.company.ServiceListRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class WhiteBoardFilesServiceTest {

	private static Logger LOGGER = LogManager.getLogger(WhiteBoardFilesServiceTest.class);

	private static final String TMP_FOLDER = "src/test/resources/whiteboard/";

	private static final String ODT_TMEPLATE = "PatientList.odt";

	private static final Long COMPANY_ID = new Long(3);

	private static final String SERVICE_ID = "GOD2";

	@TestConfiguration
	static class WhiteBoardFilesServiceTestConfiguration {

		@Bean
		public ResourceLoader resourceLoader() {
			return new GenericApplicationContext();
		}

		@Bean
		public WhiteBoardFilesService whiteBoardFilesService() {
			return new WhiteBoardFilesService();
		}

		@Bean
		PropertyPlaceholderConfigurer propConfig() {
			PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();

			Properties properties = new Properties();
			properties.put("whiteboard.file.templates.folder.resource", "classpath:whiteboard/");
			properties.put("whiteboard.patientlist.template", ODT_TMEPLATE);
			properties.put("whiteboard.patientlist.tmp", TMP_FOLDER);
			properties.put("whiteboard.patientList.maxRowsFirsPage", "30");
			properties.put("whiteboard.patientList.maxRowsByPage", "40");

			properties.put("whiteboard.patientList.maxCharactersByRowForNurse", "20");
			properties.put("whiteboard.patientList.maxCharactersByRowForReason", "20");
			properties.put("whiteboard.patientList.maxCharactersByRowForComment", "20");
			properties.put("whiteboard.patientList.maxCharactersByRowForPhysician", "20");
			properties.put("whiteboard.patientList.maxCharactersByRowForDiet", "20");

			properties.put("whiteboard.config.specialRooms", "{'0'}");

			propertyPlaceholderConfigurer.setProperties(properties);

			propertyPlaceholderConfigurer.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");

			return propertyPlaceholderConfigurer;
		}
	}

	@Before
	public void setUp() {
		Mockito.reset(companyDetailsRepository);
		Mockito.reset(serviceListRepository);
		Mockito.reset(catalogRepository);
		Mockito.reset(whiteBoardService);

		CompanyDetails companyDetails = new CompanyDetails();
		companyDetails.setId(1);
		companyDetails.setName("Test Clinic");

		List<ServiceList> services = new ArrayList<>();
		ServiceList service = new ServiceList();
		service.setHospservice(SERVICE_ID);
		service.setHospservicedesc("Test");
		services.add(service);

		CatalogDTO catalogDTO = new CatalogDTO();
		catalogDTO.setCompanyCode(COMPANY_ID.toString());
		catalogDTO.setCategory("diet");
		catalogDTO.setCode("DIET1");
		catalogDTO.setType("diet");
		catalogDTO.setLang("EN");
		catalogDTO.setCodeDesc("Test");

		Mockito.when(companyDetailsRepository.findOne(Mockito.anyInt())).thenReturn(companyDetails);

		Mockito.when(serviceListRepository.findByCompanyId(Mockito.anyInt())).thenReturn(services);

		Mockito.when(catalogRepository.getCatalogByPK(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(catalogDTO);

		Mockito.when(whiteBoardService.getAWhiteBoard(Mockito.anyObject(), Mockito.anyString(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyString(), Mockito.anyObject(), Mockito.anyObject(),
				Mockito.anyString(), Mockito.anyString())).thenReturn(WhiteBoardMock.getWhiteBoardDTO(SERVICE_ID, COMPANY_ID, 10));
	}

	@After
	public void clean() {
		// Delete temp files
		boolean deleted = false;

		File filePdf = new File(TMP_FOLDER + ODT_TMEPLATE.replace(".odt", "") + "_" + COMPANY_ID.toString() + "_" + SERVICE_ID + ".pdf");
		if (filePdf.exists()) {
			deleted = filePdf.delete();
			if (deleted) {
				LOGGER.info("Tmp file for pdf deleted...");
			} else {
				LOGGER.error("Tmp file for pdf was not deleted!!!");
			}
		}

		File fileOdt = new File(TMP_FOLDER + ODT_TMEPLATE.replace(".odt", "") + "_" + COMPANY_ID.toString() + "_" + SERVICE_ID + ".odt");
		if (fileOdt.exists()) {
			deleted = fileOdt.delete();
			if (deleted) {
				LOGGER.info("Tmp file for odt deleted...");
			} else {
				LOGGER.error("Tmp file for odt was not deleted!!!");
			}
		}
	}

	@SuppressWarnings("unused")
	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	WhiteBoardFilesService whiteBoardFilesService;

	@MockBean
	CompanyDetailsRepository companyDetailsRepository;

	@MockBean
	ServiceListRepository serviceListRepository;

	@MockBean
	CatalogRepository catalogRepository;

	@MockBean
	WhiteBoardService whiteBoardService;

	@Test
	public void generatePdfList_should_return_resource_for_pdf() throws Exception {
		String uriEnd = TMP_FOLDER + ODT_TMEPLATE.replace(".odt", "") + "_" + COMPANY_ID.toString() + "_" + SERVICE_ID + ".pdf";

		Resource pdf = whiteBoardFilesService.generatePdfList(COMPANY_ID, SERVICE_ID, null, null, null, null, null, null, "ASC", "EN");

		assertNotNull(pdf);
		assertNotNull(pdf.getURI());
		assertThat(pdf.getURI().toString().endsWith(uriEnd)).isTrue();

		LOGGER.info("generatePdfList ended some tmp files were created...");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void generatePdfList_should_throw_illegal_argument_Exception_for_company_id() throws Exception{
		whiteBoardFilesService.generatePdfList(null, SERVICE_ID, null, null, null, null, null, null, "ASC", "EN");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void generatePdfList_should_throw_illegal_argument_Exception_for_service_id() throws Exception {
		whiteBoardFilesService.generatePdfList(COMPANY_ID, null, null, null, null, null, null, null, "ASC", "EN");
	}
	
	@Test
	public void generatePdfListName_should_return_name_of_the_pdf() {
		String testName = ODT_TMEPLATE.replace(".odt", "") + "_" + COMPANY_ID.toString() + "_" + SERVICE_ID + ".pdf";

		String name = whiteBoardFilesService.generatePdfListName(COMPANY_ID, SERVICE_ID);

		assertNotNull(name);
		assertEquals(testName, name);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void generatePdfListName_should_throw_illegal_argument_Exception_for_company_id() throws Exception{
		whiteBoardFilesService.generatePdfListName(null, SERVICE_ID);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void generatePdfListName_should_throw_illegal_argument_Exception_for_service_id() throws Exception {
		whiteBoardFilesService.generatePdfListName(COMPANY_ID, null);
	}

}
