package net.fluance.cockpit.app.service.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.web.util.exceptions.ConflictException;
import net.fluance.app.web.util.exceptions.ForbiddenException;
import net.fluance.app.web.util.exceptions.LockedException;
import net.fluance.app.web.util.exceptions.MustRequestLockException;
import net.fluance.cockpit.app.web.util.whiteboard.WhiteBoardMock;
import net.fluance.cockpit.app.web.util.whiteboard.WhiteboardViewDtoAsserts;
import net.fluance.cockpit.core.model.jdbc.company.Capacity;
import net.fluance.cockpit.core.model.jdbc.company.CompanyDetails;
import net.fluance.cockpit.core.model.jdbc.whiteboard.WhiteBoardDao;
import net.fluance.cockpit.core.model.jpa.LockStatus;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardEntryDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewEntity;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteboardRoomsConfigurationDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteboardRoomsConfigurationEntity;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteboardRoomsConfigurationID;
import net.fluance.cockpit.core.repository.jdbc.company.CompaniesListRepository;
import net.fluance.cockpit.core.repository.jdbc.company.CompanyDetailsRepository;
import net.fluance.cockpit.core.repository.jpa.whiteboard.WhiteboardRoomConfigurationRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class WhiteBoardServiceTest {
	
	private static Logger LOGGER = LogManager.getLogger(WhiteBoardServiceTest.class);

	private static final Long COMPANY_ID = 3L;
	private static final String HOSP_SERVICE = "GOD2";
	private static final String COMPANY_CODE = "CDG";
	
	private static final String ROOM_TYPE_2BED = "2BED";

	@TestConfiguration
	static class WhiteBoardFilesServiceTestConfiguration {
		@Bean
		public WhiteBoardService whiteBoardService() {
			return new WhiteBoardService();
		}		
		
		@Bean
		public WhiteBoardCapacityService whiteBoardCapacityService() {
			return new WhiteBoardCapacityService();
		}
		
		@Bean
		PropertyPlaceholderConfigurer propConfig() {
			PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
			
			Properties properties = new Properties();
			properties.put("whiteboard.config.featureEnabledFor", "{"+ COMPANY_ID +":{'" + HOSP_SERVICE + "'}}");
			properties.put("whiteboard.config.patientListRules", "{" + COMPANY_ID  +  ":'{\"patientListRules\":[{\"rules\":[{\"comparisonColumn\": \"insurance\", \"values\": [\"P1\", \"P2\", \"P3\", \"P4\", \"P5\", \"P6\", \"S\", \"A\"]}], \"result\": \"ACC\"},{\"rules\":[{\"comparisonColumn\": \"insurance\", \"values\": [\"P\", \"DP\", \"C\", \"SU\"]}, {\"comparisonColumn\": \"patientclass\", \"values\": [\"HVIP\"]}], \"result\": \"VIP\"}]}'}");			
			properties.put("whiteboard.config.roomConfigEnabledFor", "{"+ COMPANY_ID +":{'" + HOSP_SERVICE + "'}}");
			
			propertyPlaceholderConfigurer.setProperties(properties);

			propertyPlaceholderConfigurer.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");

			return propertyPlaceholderConfigurer;
		}
	}

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() {
		Mockito.reset(visitService);
		Mockito.reset(catalogService);
		Mockito.reset(companyDetailsRepository);
		Mockito.reset(lockService);
		Mockito.reset(whiteboardRoomConfigurationRepository);
		Mockito.reset(companyService);
		Mockito.reset(companiesListRepository);

		CompanyDetails companyDetails = new CompanyDetails();
		companyDetails.setId(COMPANY_ID.intValue());
		companyDetails.setCode(COMPANY_CODE);
		companyDetails.setName("Test Clinic");
		
//		Map<String, WhiteboardRoomsConfigurationEntity> roomsConfigured1Bed = new HashMap<>();
		List<WhiteBoardViewEntity> whiteBoardViewEntities;
		
		//Generate view entities to mock DB request, every entry is patientClass="P" roomType="2BED"
		whiteBoardViewEntities = WhiteBoardMock.getMocks(HOSP_SERVICE, COMPANY_ID, 30);
		
		//Generate mocks data for capacity request, the amount of rooms must be greater than the rooms defined for patients
		//to ensure there will be empty rooms
		List<Capacity> capacities =WhiteBoardMock.getCapacities(20);
				
//		generateDataForFullTest(roomsConfigured1Bed, whiteBoardViewEntities, capacities);

		Mockito.when(whiteBoardDao.getAllWhiteboardEntries(Mockito.anyObject(), Mockito.anyString(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(),
				Mockito.anyString(), Mockito.anyString())).thenReturn(whiteBoardViewEntities);
		
		Mockito.when(whiteboardRoomConfigurationRepository.findOne(Mockito.any(WhiteboardRoomsConfigurationID.class))).thenAnswer(
				invocation->{
					WhiteboardRoomsConfigurationID invocationConfigurationID = invocation.getArgumentAt(0, WhiteboardRoomsConfigurationID.class);
					
//					if(invocationConfigurationID != null && roomsConfigured1Bed.get(invocationConfigurationID.getRoom()) != null){
//						return roomsConfigured1Bed.get(invocationConfigurationID.getRoom());
//					} else {
						//Default 2BED
						return WhiteBoardMock.createWhiteboardRoomsConfigurationEntity(true, false, false, ROOM_TYPE_2BED, invocationConfigurationID);
//					}
				});
		
		Mockito.when(companyService.getCapacity(Mockito.anyObject(), Mockito.anyString(), Mockito.anyString(), Mockito.anyObject(), Mockito.anyObject())).thenReturn(capacities);
		
		Mockito.when(whiteboardRoomConfigurationRepository.findByCompanyAndService(Mockito.anyString(), Mockito.anyString())).thenReturn(WhiteBoardMock.getMockRoomConfigurations(HOSP_SERVICE, COMPANY_CODE, 10));
		
		Mockito.when(whiteboardRoomConfigurationRepository.save(Mockito.isA(Iterable.class))).thenAnswer(invocation-> invocation.getArgumentAt(0, List.class));

		Mockito.when(companyDetailsRepository.findOne(Mockito.anyInt())).thenReturn(companyDetails);
	
		
		Mockito.when(whiteBoardDao.getOneEntry(Mockito.anyObject())).thenReturn(WhiteBoardMock.getViewMock(HOSP_SERVICE, COMPANY_ID));
		
		Mockito.when(lockService.isLockedByUser(Mockito.anyObject(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(true);
		
		LockStatus lockStatus = new LockStatus(true);
		Mockito.when(lockService.verify(Mockito.anyObject(), Mockito.anyString())).thenReturn(lockStatus);
		
		Mockito.when(whiteBoardDao.update(Mockito.anyObject())).thenReturn(WhiteBoardMock.getViewMock(HOSP_SERVICE, COMPANY_ID));
		
		Mockito.when(whiteBoardDao.existVisitNumber(Mockito.anyObject())).thenReturn(true);
		
		Mockito.when(whiteBoardDao.getOneEntryByVisitNummer(Mockito.anyObject())).thenReturn(WhiteBoardMock.getViewMock(HOSP_SERVICE, COMPANY_ID));
		
		Mockito.when(whiteBoardDao.existVisitNumberOnView(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyString())).thenReturn(true);
	}

	@Autowired
	WhiteBoardService whiteBoardService;
	
	@Autowired
	WhiteBoardCapacityService whiteBoardCapacityService;
	
	@MockBean
	WhiteBoardDao whiteBoardDao;
	
	@MockBean
	VisitService visitService;
	
	@MockBean
	CatalogService catalogService;
	
	@MockBean
	CompanyDetailsRepository companyDetailsRepository;
	
	@MockBean
	LockService lockService;
	
	@MockBean
	WhiteboardRoomConfigurationRepository whiteboardRoomConfigurationRepository;
	
	@MockBean
	CompanyService companyService;
	
	@MockBean
	CompaniesListRepository companiesListRepository;

	//getRoomsConfiguration tests
	@Test
	public void getRoomsConfiguration_should_return_list_of_WhiteboardRoomsConfigurationDTO() {
		List<WhiteboardRoomsConfigurationDTO> whiteBoardsConfigurations = whiteBoardService.getRoomsConfiguration(COMPANY_ID, HOSP_SERVICE);
		
		assertNotNull(whiteBoardsConfigurations);
		assertNotNull(whiteBoardsConfigurations.get(0));
	}
	
	@Test(expected = ConflictException.class)
	public void getRoomsConfiguration_should_throw_ConflictException() {
		Mockito.when(companyDetailsRepository.findOne(Mockito.anyInt())).thenReturn(null);
		
		whiteBoardService.getRoomsConfiguration(99L, "FOO");
	}
	
	//updateRoomConfiguration tests
	@Test
	public void updateRoomConfiguration_should_return_WhiteboardRoomsConfigurationDTO() {
		List<WhiteboardRoomsConfigurationDTO> whiteboardRoomsConfigurationDTOsReturn;
		
		WhiteboardRoomsConfigurationDTO whiteboardRoomsConfigurationDTO = new WhiteboardRoomsConfigurationDTO();
		whiteboardRoomsConfigurationDTO.setCompanyCode(COMPANY_ID.toString());
		whiteboardRoomsConfigurationDTO.setHospservice(HOSP_SERVICE);
		
		List<WhiteboardRoomsConfigurationDTO> whiteboardRoomsConfigurationDTOs = new ArrayList<>();
		whiteboardRoomsConfigurationDTOs.add(whiteboardRoomsConfigurationDTO);
				
		whiteboardRoomsConfigurationDTOsReturn = whiteBoardService.updateRoomConfiguration(whiteboardRoomsConfigurationDTOs);
		assertNotNull(whiteboardRoomsConfigurationDTOsReturn);
		assertEquals(whiteboardRoomsConfigurationDTOsReturn.get(0).getCompanyCode(), whiteboardRoomsConfigurationDTO.getCompanyCode());
		assertEquals(whiteboardRoomsConfigurationDTOsReturn.get(0).getHospservice(), whiteboardRoomsConfigurationDTO.getHospservice());	
	}	
	
	//getAWhiteBoard tests
	@Test
	public void getAWhiteBoard_all_should_return() {
		Map<String, WhiteboardViewDtoAsserts> whiteboardViewDtoAssertData = new HashMap<>();	
		Map<String, WhiteboardRoomsConfigurationEntity> roomsConfigured1Bed = new HashMap<>();
		List<String> roomsMustNotBePresent = new ArrayList<>();
		List<WhiteBoardViewEntity> whiteBoardViewEntities = new ArrayList<>();
		List<Capacity> capacities = new ArrayList<>();
		
		WhiteBoardMock.generateDataForFullTest(COMPANY_CODE, COMPANY_ID, HOSP_SERVICE, roomsConfigured1Bed, whiteBoardViewEntities, whiteboardViewDtoAssertData, roomsMustNotBePresent);
		
		//Prepare mock Daos and repositories
		Mockito.when(whiteBoardDao.getAllWhiteboardEntries(Mockito.anyObject(), Mockito.anyString(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(),
				Mockito.anyString(), Mockito.anyString())).thenReturn(whiteBoardViewEntities);
		
		Mockito.when(whiteboardRoomConfigurationRepository.findOne(Mockito.any(WhiteboardRoomsConfigurationID.class))).thenAnswer(
				invocation->{
					WhiteboardRoomsConfigurationID invocationConfigurationID = invocation.getArgumentAt(0, WhiteboardRoomsConfigurationID.class);
					
					if(roomsConfigured1Bed.get(invocationConfigurationID.getRoom()) != null){
						return roomsConfigured1Bed.get(invocationConfigurationID.getRoom());
					} else {
						//Default 2BED
						return WhiteBoardMock.createWhiteboardRoomsConfigurationEntity(true, false, false, ROOM_TYPE_2BED, invocationConfigurationID);
					}
				});
		
		Mockito.when(companyService.getCapacity(Mockito.anyObject(), Mockito.anyString(), Mockito.anyString(), Mockito.anyObject(), Mockito.anyObject())).thenReturn(capacities);
		
		WhiteBoardDTO whiteBoardDTO= whiteBoardService.getAWhiteBoard(COMPANY_ID,  HOSP_SERVICE, null, null, null, null, "all", 10000, 0, null, null);
		assertNotNull(whiteBoardDTO);
		
		//Aux data for test
		String key;		
		List<String> keyTested = new ArrayList<>();
		
		LOGGER.info("Testing results for getAWhiteBoard with data for full test");
		
		LOGGER.info("ROOM.BED\tPID\tInsu.\tClass\tRoom Type");
		LOGGER.info("---------------------------------------");
		
		for (WhiteBoardViewDTO whiteBoardViewDTO : whiteBoardDTO.getEntries()) {			
			LOGGER.info(String.format("%s.%s\t%s\t%s\t%s\t%s", 
							whiteBoardViewDTO.getRoom(), whiteBoardViewDTO.getPatientBed(), whiteBoardViewDTO.getPatientId(), whiteBoardViewDTO.getInsurance(), whiteBoardViewDTO.getPatientClass(), whiteBoardViewDTO.getRoomType()));
			
			key = whiteBoardViewDTO.getRoom()+"."+whiteBoardViewDTO.getPatientBed();
			
			assertFalse(String.format("%s should be unic", key), Arrays.asList(keyTested.toArray()).contains(key));
			
			keyTested.add(key);
			
			WhiteboardViewDtoAsserts whiteboardViewDtoAsserts = whiteboardViewDtoAssertData.get(key);
			
			if(whiteboardViewDtoAsserts != null) {
				assertEquals(String.format("%s expected room", whiteboardViewDtoAsserts.getKey()), whiteboardViewDtoAsserts.getRoom() , whiteBoardViewDTO.getRoom());
				assertEquals(String.format("%s expected patient bed", whiteboardViewDtoAsserts.getKey()), whiteboardViewDtoAsserts.getPatientBed(), whiteBoardViewDTO.getPatientBed());
				assertEquals(String.format("%s expected patient ID", whiteboardViewDtoAsserts.getKey()), whiteboardViewDtoAsserts.getPatientId(), whiteBoardViewDTO.getPatientId());
				assertEquals(String.format("%s expected insurance", whiteboardViewDtoAsserts.getKey()), whiteboardViewDtoAsserts.getInsurance(), whiteBoardViewDTO.getInsurance());
			}
			
			assertFalse(String.format("%s should not be present", key), Arrays.asList(roomsMustNotBePresent.toArray()).contains(key));
		}
		
		for (String assertsKey: whiteboardViewDtoAssertData.keySet() ) {
			WhiteboardViewDtoAsserts whiteboardViewDtoAsserts = whiteboardViewDtoAssertData.get(assertsKey);
			boolean present = false;
			for (WhiteBoardViewDTO whiteBoardViewDTO : whiteBoardDTO.getEntries()) {
				String viewKey = whiteBoardViewDTO.getRoom()+"."+whiteBoardViewDTO.getPatientBed();
				if(whiteboardViewDtoAsserts.getKey().equals(viewKey)) {
					present = true;
					break;
				}
			}
			
			assertTrue(String.format("%s expected in results", whiteboardViewDtoAsserts.getKey()), present);
		}
	}
	
	@Test
	public void getAWhiteBoard_all_not_display_room_should_return() {
		String roomNotDisplayed = "002";
		
		WhiteboardRoomsConfigurationEntity whiteboardRoomsConfigurationEntity = new WhiteboardRoomsConfigurationEntity();
		whiteboardRoomsConfigurationEntity.setDisplay(false);
		whiteboardRoomsConfigurationEntity.setMergeNext(false);
		whiteboardRoomsConfigurationEntity.setMergePrevious(true);
		whiteboardRoomsConfigurationEntity.setRoomType("type");
						
		Mockito.when(whiteboardRoomConfigurationRepository.findOne(Mockito.any(WhiteboardRoomsConfigurationID.class))).thenAnswer(
				invocation->{
					WhiteboardRoomsConfigurationID invocationConfigurationID = invocation.getArgumentAt(0, WhiteboardRoomsConfigurationID.class);
					
					if (invocationConfigurationID != null && roomNotDisplayed.equals(invocationConfigurationID.getRoom())){
						return WhiteBoardMock.createWhiteboardRoomsConfigurationEntity(false, false, false, ROOM_TYPE_2BED, invocationConfigurationID);
					} else {
						//Default 2BED
						return WhiteBoardMock.createWhiteboardRoomsConfigurationEntity(true, false, false, ROOM_TYPE_2BED, invocationConfigurationID);
					}
				});
		
		WhiteBoardDTO whiteBoardDTO= whiteBoardService.getAWhiteBoard(COMPANY_ID,  HOSP_SERVICE, null, null, null, null, "availabilities", 10000, 0, null, null);
		assertNotNull(whiteBoardDTO);
		
		for (WhiteBoardViewDTO whiteBoardViewDTO : whiteBoardDTO.getEntries()) {				
			assertNotEquals("The room is different than not diplayed room", roomNotDisplayed, whiteBoardViewDTO.getRoom());
		}
	}
	
	@Test
	public void getAWhiteBoard_patients_should_return() {
		Map<String, WhiteboardViewDtoAsserts> whiteboardViewDtoAssertData = new HashMap<>();	
		Map<String, WhiteboardRoomsConfigurationEntity> roomsConfigured1Bed = new HashMap<>();
		List<String> roomsMustNotBePresent = new ArrayList<>();
		List<WhiteBoardViewEntity> whiteBoardViewEntities = new ArrayList<>();
		List<Capacity> capacities = new ArrayList<>();
		
		WhiteBoardMock.generateDataForFullTest(COMPANY_CODE, COMPANY_ID, HOSP_SERVICE, roomsConfigured1Bed, whiteBoardViewEntities, whiteboardViewDtoAssertData, roomsMustNotBePresent);
		
		//Prepare mock Daos and repositories
		Mockito.when(whiteBoardDao.getAllWhiteboardEntries(Mockito.anyObject(), Mockito.anyString(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(),
				Mockito.anyString(), Mockito.anyString())).thenReturn(whiteBoardViewEntities);
		
		Mockito.when(whiteboardRoomConfigurationRepository.findOne(Mockito.any(WhiteboardRoomsConfigurationID.class))).thenAnswer(
				invocation->{
					WhiteboardRoomsConfigurationID invocationConfigurationID = invocation.getArgumentAt(0, WhiteboardRoomsConfigurationID.class);
					
					if(roomsConfigured1Bed.get(invocationConfigurationID.getRoom()) != null){
						return roomsConfigured1Bed.get(invocationConfigurationID.getRoom());
					} else {
						//Default 2BED
						return WhiteBoardMock.createWhiteboardRoomsConfigurationEntity(true, false, false, ROOM_TYPE_2BED, invocationConfigurationID);
					}
				});
		
		Mockito.when(companyService.getCapacity(Mockito.anyObject(), Mockito.anyString(), Mockito.anyString(), Mockito.anyObject(), Mockito.anyObject())).thenReturn(capacities);
		
		WhiteBoardDTO whiteBoardDTO= whiteBoardService.getAWhiteBoard(COMPANY_ID,  HOSP_SERVICE, null, null, null, null, "patients", 10000, 0, null, null);
		assertNotNull(whiteBoardDTO);
		
		String key;
		for (WhiteBoardViewDTO whiteBoardViewDTO : whiteBoardDTO.getEntries()) {
			key = whiteBoardViewDTO.getRoom()+"."+whiteBoardViewDTO.getPatientBed();
			assertNotNull("Should be filled bed", whiteBoardViewDTO.getId());
			
			WhiteboardViewDtoAsserts whiteboardViewDtoAsserts = whiteboardViewDtoAssertData.get(key);			
			if(whiteboardViewDtoAsserts != null) {
				assertEquals(String.format("%s expected room", whiteboardViewDtoAsserts.getKey()), whiteboardViewDtoAsserts.getRoom() , whiteBoardViewDTO.getRoom());
				assertEquals(String.format("%s expected patient bed", whiteboardViewDtoAsserts.getKey()), whiteboardViewDtoAsserts.getPatientBed(), whiteBoardViewDTO.getPatientBed());
				assertEquals(String.format("%s expected patient ID", whiteboardViewDtoAsserts.getKey()), whiteboardViewDtoAsserts.getPatientId(), whiteBoardViewDTO.getPatientId());
				assertEquals(String.format("%s expected insurance", whiteboardViewDtoAsserts.getKey()), whiteboardViewDtoAsserts.getInsurance(), whiteBoardViewDTO.getInsurance());
				assertNotEquals(String.format("%s expected insurance disctinct to ACC", whiteboardViewDtoAsserts.getKey()), WhiteBoardMock.ACC_INSURANCE, whiteBoardViewDTO.getInsurance());
			}
		}
	}
	
	@Test
	public void getAWhiteBoard_availabilities_should_return() {
		Map<String, WhiteboardViewDtoAsserts> whiteboardViewDtoAssertData = new HashMap<>();	
		Map<String, WhiteboardRoomsConfigurationEntity> roomsConfigured1Bed = new HashMap<>();
		List<String> roomsMustNotBePresent = new ArrayList<>();
		List<WhiteBoardViewEntity> whiteBoardViewEntities = new ArrayList<>();
		List<Capacity> capacities = new ArrayList<>();
		
		WhiteBoardMock.generateDataForFullTest(COMPANY_CODE, COMPANY_ID, HOSP_SERVICE, roomsConfigured1Bed, whiteBoardViewEntities, whiteboardViewDtoAssertData, roomsMustNotBePresent);
		
		//Prepare mock Daos and repositories
		Mockito.when(whiteBoardDao.getAllWhiteboardEntries(Mockito.anyObject(), Mockito.anyString(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject(),
				Mockito.anyString(), Mockito.anyString())).thenReturn(whiteBoardViewEntities);
		
		Mockito.when(whiteboardRoomConfigurationRepository.findOne(Mockito.any(WhiteboardRoomsConfigurationID.class))).thenAnswer(
				invocation->{
					WhiteboardRoomsConfigurationID invocationConfigurationID = invocation.getArgumentAt(0, WhiteboardRoomsConfigurationID.class);
					
					if(roomsConfigured1Bed.get(invocationConfigurationID.getRoom()) != null){
						return roomsConfigured1Bed.get(invocationConfigurationID.getRoom());
					} else {
						//Default 2BED
						return WhiteBoardMock.createWhiteboardRoomsConfigurationEntity(true, false, false, ROOM_TYPE_2BED, invocationConfigurationID);
					}
				});
		
		Mockito.when(companyService.getCapacity(Mockito.anyObject(), Mockito.anyString(), Mockito.anyString(), Mockito.anyObject(), Mockito.anyObject())).thenReturn(capacities);
		
		WhiteBoardDTO whiteBoardDTO= whiteBoardService.getAWhiteBoard(COMPANY_ID,  HOSP_SERVICE, null, null, null, null, "availabilities", 10000, 0, null, null);
		assertNotNull(whiteBoardDTO);
		
		String key;
		//only rooms available, all the entries must havve id = null
		for (WhiteBoardViewDTO whiteBoardViewDTO : whiteBoardDTO.getEntries()) {
			key = whiteBoardViewDTO.getRoom()+"."+whiteBoardViewDTO.getPatientBed();
			assertNull("Should be empty bed", whiteBoardViewDTO.getId());
			
			//every room defined to be tested here should be empty
			WhiteboardViewDtoAsserts whiteboardViewDtoAsserts = whiteboardViewDtoAssertData.get(key);			
			if(whiteboardViewDtoAsserts != null) {
				assertNull(String.format("%s expected to be empty", whiteboardViewDtoAsserts.getKey()), whiteboardViewDtoAsserts.getPatientId());
			}
			
			assertFalse(String.format("%s should not be present", key), Arrays.asList(roomsMustNotBePresent.toArray()).contains(key));
		}
	}
	
	@Test(expected = NullPointerException.class)
	public void getAWhiteBoard_should_throw_NullPointerException_for_company_id() {
		whiteBoardService.getAWhiteBoard(null, HOSP_SERVICE, null, null, null, null, null, null, null, null, null);
	}	
	
	@Test(expected = ForbiddenException.class)
	public void getAWhiteBoard_should_throw_ForbiddenException_service_null() {
		whiteBoardService.getAWhiteBoard(COMPANY_ID,  null, null, null, null, null, null, null, null, null, null);
	}
	
	@Test(expected = ForbiddenException.class)
	public void getAWhiteBoard_should_throw_ForbiddenException_bad_service() {
		whiteBoardService.getAWhiteBoard(COMPANY_ID, "FOO", null, null, null, null, null, null, null, null, null);
	}
	
	@Test(expected = ForbiddenException.class)
	public void getAWhiteBoard_should_throw_ForbiddenException_bad_company_id() {
		whiteBoardService.getAWhiteBoard(99L, "FOO", null, null, null, null, null, null, null, null, null);
	}
	
	
	//getAllWhiteboardEntries tests
	@Test
	public void getAllWhiteboardEntries_should_return() {
		List<WhiteBoardEntryDTO> whiteBoardViewDTOs = whiteBoardService.getAllWhiteboardEntries(COMPANY_ID, HOSP_SERVICE, 10000, 0, null, null);
		assertNotNull(whiteBoardViewDTOs);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getAllWhiteboardEntries_should_return_IllegalArgumentException() {
		whiteBoardService.getAllWhiteboardEntries(null, null, 10000, 0, null, null);
	}
	
	//getOneEntry tests
	@Test
	public void getOneEntry_should_return() {
		Long whiteboardId = 1L;
		
		WhiteBoardViewDTO whiteBoardViewDTO = whiteBoardService.getOneEntry(whiteboardId, COMPANY_ID, HOSP_SERVICE);
		assertNotNull(whiteBoardViewDTO);
	}
	
	@Test(expected = NullPointerException.class)
	public void getOneEntry_should_throw_NullPointerException_for_company_id() {
		whiteBoardService.getOneEntry(null, null, HOSP_SERVICE);
	}
	
	@Test(expected = ForbiddenException.class)
	public void getOneEntry_should_throw_forbidden_exception_service_null() {
		whiteBoardService.getOneEntry(null, COMPANY_ID, null);
	}
	
	@Test(expected = ForbiddenException.class)
	public void getOneEntry_should_throw_forbidden_exception_bad_service() {
		whiteBoardService.getOneEntry(null, COMPANY_ID, "FOO");
	}
	
	@Test(expected = ForbiddenException.class)
	public void getOneEntry_should_throw_forbidden_exception_bad_company_id() {
		whiteBoardService.getOneEntry(null, 99L, "FOO");
	}
	
	//isFeatureEnabled test
	@Test
	public void isFeatureEnabled_should_return_true() {
		assertTrue(whiteBoardService.isFeatureEnabled(COMPANY_ID.intValue(), HOSP_SERVICE));
	}
	
	@Test
	public void isFeatureEnabled_should_return_false() {
		assertFalse(whiteBoardService.isFeatureEnabled(99, "FOO"));
	}
	
	//checkFeauture test
	@Test
	public void checkFeauture_should_do_nothing() {
		whiteBoardService.checkFeauture(COMPANY_ID, HOSP_SERVICE);
	}
	
	@Test(expected = ForbiddenException.class)
	public void checkFeauture_should_throw_ForbiddenException() {
		whiteBoardService.checkFeauture(99L, "FOO");
	}
	
	//update test
	@Test
	public void update_should_return() {
		User user = new User();
		user.setUsername("USER");
		user.setDomain("Domain");
		
		WhiteBoardViewDTO whiteBoardViewDTO =  WhiteBoardMock.getDTOMock(HOSP_SERVICE, COMPANY_ID);
		
		WhiteBoardViewDTO returnedWhiteBoardViewDTO = whiteBoardService.update(whiteBoardViewDTO, COMPANY_ID, HOSP_SERVICE, user);
		
		assertNotNull(returnedWhiteBoardViewDTO);
		assertEquals(returnedWhiteBoardViewDTO.getId(), whiteBoardViewDTO.getId());
	}
	
	@Test
	public void update_whiteboard_id_null_should_return() {
		User user = new User();
		user.setUsername("USER");
		user.setDomain("Domain");
		
		WhiteBoardViewDTO whiteBoardViewDTO =  WhiteBoardMock.getDTOMock(HOSP_SERVICE, COMPANY_ID);
		whiteBoardViewDTO.setId(null);
		
		WhiteBoardViewDTO returnedWhiteBoardViewDTO = whiteBoardService.update(whiteBoardViewDTO, COMPANY_ID, HOSP_SERVICE, user);
		
		assertNotNull(returnedWhiteBoardViewDTO);
		assertEquals(returnedWhiteBoardViewDTO.getId(), whiteBoardViewDTO.getId());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void update_whiteboard_companyId_null_should_throw_IllegalArgumentException() {
		User user = new User();
		user.setUsername("USER");
		user.setDomain("Domain");
		
		WhiteBoardViewDTO whiteBoardViewDTO =  WhiteBoardMock.getDTOMock(HOSP_SERVICE, COMPANY_ID);
		
		whiteBoardService.update(whiteBoardViewDTO, null, HOSP_SERVICE, user);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void update_whiteboard_serviceId_null_should_throw_IllegalArgumentException() {
		User user = new User();
		user.setUsername("USER");
		user.setDomain("Domain");
		
		WhiteBoardViewDTO whiteBoardViewDTO =  WhiteBoardMock.getDTOMock(HOSP_SERVICE, COMPANY_ID);
		
		whiteBoardService.update(whiteBoardViewDTO, COMPANY_ID, null, user);
	}
	
	@Test(expected = ConflictException.class)
	public void update_whiteboard_id_null_not_exists_visit_number_should_throw_ConflictException() {
		Mockito.when(whiteBoardDao.existVisitNumberOnView(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyString())).thenReturn(false);
		
		User user = new User();
		user.setUsername("USER");
		user.setDomain("Domain");
		
		WhiteBoardViewDTO whiteBoardViewDTO =  WhiteBoardMock.getDTOMock(HOSP_SERVICE, COMPANY_ID);
		whiteBoardViewDTO.setId(null);
		
		whiteBoardService.update(whiteBoardViewDTO, COMPANY_ID, HOSP_SERVICE, user);
	}
	
	@Test(expected = LockedException.class)
	public void update_should_throw_LockedException() {
		Mockito.when(lockService.isLockedByUser(Mockito.anyObject(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		
		User user = new User();
		user.setUsername("USER");
		user.setDomain("Domain");
		
		WhiteBoardViewDTO whiteBoardViewDTO =  WhiteBoardMock.getDTOMock(HOSP_SERVICE, COMPANY_ID);
		
		whiteBoardService.update(whiteBoardViewDTO, COMPANY_ID, HOSP_SERVICE, user);
	}
	
	@Test(expected = MustRequestLockException.class)
	public void update_should_throw_MustRequestLockException() {
		Mockito.when(lockService.isLockedByUser(Mockito.anyObject(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		
		LockStatus lockStatus = new LockStatus(false);
		Mockito.when(lockService.verify(Mockito.anyObject(), Mockito.anyString())).thenReturn(lockStatus);
		
		User user = new User();
		user.setUsername("USER");
		user.setDomain("Domain");
		
		WhiteBoardViewDTO whiteBoardViewDTO =  WhiteBoardMock.getDTOMock(HOSP_SERVICE, COMPANY_ID);
		
		whiteBoardService.update(whiteBoardViewDTO, COMPANY_ID, HOSP_SERVICE, user);
	}

	//getAllWhiteboardViewEntriesByCompanyIdAndServiceIdForNow
	@Test
	public void getAllWhiteboardViewEntriesByCompanyIdAndServiceIdForNow_should_return() {
		List<WhiteBoardViewDTO> whiteBoardViewDTOs = whiteBoardService.getAllWhiteboardViewEntriesByCompanyIdAndServiceIdForNow(COMPANY_ID, HOSP_SERVICE);
		
		assertNotNull(whiteBoardViewDTOs);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getAllWhiteboardViewEntriesByCompanyIdAndServiceIdForNow_IllegalArgumentException() {
		whiteBoardService.getAllWhiteboardViewEntriesByCompanyIdAndServiceIdForNow(null, null);
	}
	
	//countAllWhiteboardViewEntriesByCompanyIdAndServiceIdForNow
	@Test
	public void countAllWhiteboardViewEntriesByCompanyIdAndServiceIdForNow_should_return() {
		Integer count = whiteBoardService.countAllWhiteboardViewEntriesByCompanyIdAndServiceIdForNow(COMPANY_ID, HOSP_SERVICE);
		assertNotNull(count);
		assertEquals(new Integer(30), count);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void countAllWhiteboardViewEntriesByCompanyIdAndServiceIdForNow_IllegalArgumentException() {
		whiteBoardService.countAllWhiteboardViewEntriesByCompanyIdAndServiceIdForNow(null, null);
	}
	
}
