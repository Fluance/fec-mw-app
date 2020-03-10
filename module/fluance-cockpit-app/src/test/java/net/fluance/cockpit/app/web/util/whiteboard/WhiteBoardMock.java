package net.fluance.cockpit.app.web.util.whiteboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.fluance.cockpit.core.model.jdbc.company.Capacity;
import net.fluance.cockpit.core.model.jdbc.whiteboard.WhiteBoardMapper;
import net.fluance.cockpit.core.model.jpa.catalog.CatalogDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewEntity;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteboardRoomsConfigurationEntity;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteboardRoomsConfigurationID;


public class WhiteBoardMock {
	
	private WhiteBoardMock() {}
	
	private static final String DEFAULT_SERVICE_ID = "GOD1";
	private static final Long DEFAULT_COMPANY_ID = 99L;
	private static final Long DEFAULT_VISIT_NUMBER = 99L;
	private static final Long DEFAULT_PID_NUMBER = 99999L;
	
	private static final String ROOM_TYPE_2BED = "2BED";
	
	private static final String PRESIDENTIAL_SUITE = "PRS";
	private static final String MONT_BLANC_SUITE = "MBS";
	private static final String ROOM_TYPE_1BED = "1BED";
	private static final String ROOM_TYPE_1BED_JUNIOR_SUITE = "1BJS";
	private static final String MEDICAL_DESK = "MED";
	
	//Exported value to use in tests
	public static String DEFAULT_INSURANCE = "P";
	public static String ACC_INSURANCE = "ACC";
	
	public static WhiteBoardViewEntity getViewMock(String serviceId, Long companyId) {
		WhiteBoardViewEntity whiteBoard = new WhiteBoardViewEntity();
		whiteBoard.setId(1L);
				
		whiteBoard.setInsurance(DEFAULT_INSURANCE);
		whiteBoard.setComment("Just a comment");
		whiteBoard.setPhysician("Mr Dr Heinz");
		whiteBoard.setEntreeDate(new Date());
		whiteBoard.setExpireDate(new Date());
		whiteBoard.setDiet("DIET1,DIET2");
		whiteBoard.setNurseName("\"Stephanie\", \"Mary\"");
		whiteBoard.setOperationDate(new Date());
		whiteBoard.setEditedOperationDate(new Date());
		whiteBoard.setFirstname("Mr Patinent");
		whiteBoard.setLastname("Impatient");
		whiteBoard.setReason("\"A reason to life...\",\"Other reason to life...\"");
		whiteBoard.setRoom("404");
		whiteBoard.setAppointmentId(323L);
		whiteBoard.setHl7Code("PatientClass");
		whiteBoard.setOperationDate(new Date());
		whiteBoard.setIsolationType("A");
		whiteBoard.setPatientId(DEFAULT_PID_NUMBER);
		whiteBoard.setServiceId(serviceId);
		whiteBoard.setCompanyId(companyId);
		whiteBoard.setSex("Monsieur");
		whiteBoard.setVisitNumber(DEFAULT_VISIT_NUMBER);
		whiteBoard.setPatientClass("HOSP");
		whiteBoard.setOriginalCapacity(2);
		whiteBoard.setRoomType(ROOM_TYPE_2BED);
		whiteBoard.setConfCapacity(2);
		
		return whiteBoard;
	}
	
	public static WhiteBoardViewEntity getViewMock() {
		return getViewMock(DEFAULT_SERVICE_ID, DEFAULT_COMPANY_ID);
	}
	
	@SuppressWarnings("unused")
	public static WhiteBoardViewDTO getDTOMock() {
		return WhiteBoardMapper.toModel(getViewMock());
	}
	
	public static WhiteBoardViewDTO getDTOMock(String serviceId, Long companyId) {
		return WhiteBoardMapper.toModel(getViewMock(serviceId, companyId));
	}

	public static List<WhiteBoardViewEntity> getMocks(String serviceId, Long companyId, Integer howManyWhiteBoardViewEntit) {
		List<WhiteBoardViewEntity> whiteboards = new ArrayList<>();
		return getMocks(whiteboards, serviceId, companyId, howManyWhiteBoardViewEntit);
	}
	
	public static List<WhiteBoardViewEntity> getMocks(List<WhiteBoardViewEntity> whiteboards, String serviceId, Long companyId, Integer howManyWhiteBoardViewEntit) {
		//First patient 1
		int i = 1;
		howManyWhiteBoardViewEntit++;
		
		WhiteBoardViewEntity whiteBoardEntry;
		String room = "";
		int roomNumber = 1;
				
		while (i < howManyWhiteBoardViewEntit) {
			whiteBoardEntry = getViewMock(serviceId, companyId);
			whiteBoardEntry.setId((long) i + 100L);
			
			//calculate one room for two patients
			if(i % 2 != 0) {
				room = String.format("%03d", roomNumber);
				roomNumber++;
				//set bed
				whiteBoardEntry.setPatientBed("1");
			} else {
				//set bed
				whiteBoardEntry.setPatientBed("2");
			}
			//set the room
			whiteBoardEntry.setRoom(room);
			
			whiteBoardEntry.setPatientId(whiteBoardEntry.getPatientId() + i);
			
			whiteboards.add(whiteBoardEntry);
			i++;
		}
		return whiteboards;
	}
	
	public static List<Capacity> getCapacities(Integer howManyCapacities) {
		List<Capacity> capacities =  new ArrayList<>();

		//First room 001
		int i = 1;
		howManyCapacities++;
		
		while (i < howManyCapacities) {
			capacities.add(new Capacity(String.format("%03d", i), 2));
			i++;
		}

		return capacities;
	}
	
	
	@SuppressWarnings("unused")
	public static List<CatalogDTO> getCatalogs(Long companyId, String codeStart, String category, String type, Integer howManyCatalogs, String[] langs) {
		List<CatalogDTO> catalogDTOs = new ArrayList<>();
		int i = 0;
		CatalogDTO catalogDTO;
		while (i < (howManyCatalogs + 1)) {
			for (String lang : langs) {
				catalogDTO = new CatalogDTO();				
				catalogDTO.setCompanyCode(companyId.toString());			
				catalogDTO.setCategory(category);
				catalogDTO.setCode(codeStart.concat(Integer.toString(i)));
				catalogDTO.setType(type);
				catalogDTO.setLang(lang);
				catalogDTO.setCodeDesc("Test ".concat(catalogDTO.getLang()).concat(Integer.toString(i)));
				
				catalogDTOs.add(catalogDTO);
			}
		
			i++;
		}
		
		return catalogDTOs;
	}
	
	
	public static WhiteBoardDTO getWhiteBoardDTO(String serviceId, Long companyId, Integer howManyWhiteBoardDTOs) {
		
		WhiteBoardDTO whiteBoardDTO = new WhiteBoardDTO();
		whiteBoardDTO.setServiceId(serviceId);
		whiteBoardDTO.setCompanyId(companyId);
		
		
		List<WhiteBoardViewEntity>boardViewEntities = getMocks(serviceId, companyId, howManyWhiteBoardDTOs);
		List<WhiteBoardViewDTO> boardViewDTOs = new ArrayList<>();
		
		for (WhiteBoardViewEntity whiteBoardViewEntity : boardViewEntities) {
			boardViewDTOs.add(WhiteBoardMapper.toModel(whiteBoardViewEntity));
		}
		
		whiteBoardDTO.setEntries(boardViewDTOs);
		
		return whiteBoardDTO;
	}
	
	public static List<WhiteboardRoomsConfigurationEntity> getMockRoomConfigurations(String serviceId, String companyCode, Integer howManyRoomsConfigurationEntities){
		List<WhiteboardRoomsConfigurationEntity> whiteboardRoomsConfigurationEntities = new ArrayList<>();
		WhiteboardRoomsConfigurationEntity whiteboardRoomsConfigurationEntity;
		
		int i = 1;
		
		while (i < (howManyRoomsConfigurationEntities + 1)) {
			whiteboardRoomsConfigurationEntity = new WhiteboardRoomsConfigurationEntity();
			whiteboardRoomsConfigurationEntity.setDisplay(true);
			whiteboardRoomsConfigurationEntity.setMergeNext(false);
			whiteboardRoomsConfigurationEntity.setMergePrevious(true);
			whiteboardRoomsConfigurationEntity.setRoomType("type");
			
			whiteboardRoomsConfigurationEntity.setWhiteboardRoomsConfigurationID(new WhiteboardRoomsConfigurationID(companyCode, serviceId, String.format("%03d", i)));
			
			whiteboardRoomsConfigurationEntities.add(whiteboardRoomsConfigurationEntity);
			
			i++;
		}
		
		return whiteboardRoomsConfigurationEntities;
	}
	
	public static WhiteboardRoomsConfigurationEntity createWhiteboardRoomsConfigurationEntity(Boolean display, Boolean mergeNext, Boolean mergePrevious, String roomType, WhiteboardRoomsConfigurationID id) {
		WhiteboardRoomsConfigurationEntity whiteboardRoomsConfigurationEntity;
		
		whiteboardRoomsConfigurationEntity = new WhiteboardRoomsConfigurationEntity();		
		whiteboardRoomsConfigurationEntity.setDisplay(display);
		whiteboardRoomsConfigurationEntity.setMergeNext(mergeNext);
		whiteboardRoomsConfigurationEntity.setMergePrevious(mergePrevious);
		whiteboardRoomsConfigurationEntity.setRoomType(roomType);			
		whiteboardRoomsConfigurationEntity.setWhiteboardRoomsConfigurationID(id);
		
		return whiteboardRoomsConfigurationEntity;
	}
	
	public static WhiteboardRoomsConfigurationID createWhiteboardRoomsConfigurationID(String companyCode, String hospservice, String room) {
		WhiteboardRoomsConfigurationID whiteboardRoomsConfigurationID;
		
		whiteboardRoomsConfigurationID = new  WhiteboardRoomsConfigurationID();	
		whiteboardRoomsConfigurationID.setCompanyCode(companyCode);
		whiteboardRoomsConfigurationID.setHospservice(hospservice);
		whiteboardRoomsConfigurationID.setRoom(room);
		
		return whiteboardRoomsConfigurationID;
	}
	
	public static void generateDataForFullTest(String companyCode, Long companyId, String hospservice, Map<String, WhiteboardRoomsConfigurationEntity> roomsConfigured1Bed, List<WhiteBoardViewEntity> whiteBoardViewEntities, Map<String, WhiteboardViewDtoAsserts> whiteboardViewDtoAssertData, List<String> roomsMustNotBePresent) {
		//Generate view entities to mock DB request, every entry is patientClass="P" roomType="2BED"
		WhiteBoardMock.getMocks(whiteBoardViewEntities, hospservice, companyId, 100);

		//Generate mocks data for capacity request, the amount of rooms must be greater than the rooms defined for patients
		//to ensure there will be empty rooms
		List<Capacity> capacities = WhiteBoardMock.getCapacities(70);
		
		WhiteBoardViewEntity whiteBoardViewEntity;
		WhiteboardViewDtoAsserts whiteboardViewDtoAsserts;
		String roomType;
		Integer position = -1;
		
		//Room configured as 2Bed type with one VIP patient and other ACC		
		position = generateEntitiesForConfiguredRoomsWithVIPandACC(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, ROOM_TYPE_2BED, position, whiteboardViewDtoAssertData, roomsMustNotBePresent);
		
		//Rooms configured as 1Bed type with one VIP patient and other ACC
		position = generateEntitiesForConfiguredRoomsWithVIPandACC(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, PRESIDENTIAL_SUITE, position, whiteboardViewDtoAssertData, roomsMustNotBePresent);		
		position = generateEntitiesForConfiguredRoomsWithVIPandACC(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, MONT_BLANC_SUITE, position, whiteboardViewDtoAssertData, roomsMustNotBePresent);
		position = generateEntitiesForConfiguredRoomsWithVIPandACC(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, ROOM_TYPE_1BED, position, whiteboardViewDtoAssertData, roomsMustNotBePresent);	
		position = generateEntitiesForConfiguredRoomsWithVIPandACC(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, ROOM_TYPE_1BED_JUNIOR_SUITE, position, whiteboardViewDtoAssertData, roomsMustNotBePresent);
		
		//Rooms configured as 1Bed type with two patients different than VIP
		position = generateEntitiesForConfiguredBedRoomsWithTwoNotVIP(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, PRESIDENTIAL_SUITE, position, whiteboardViewDtoAssertData);
		position = generateEntitiesForConfiguredBedRoomsWithTwoNotVIP(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, MONT_BLANC_SUITE, position, whiteboardViewDtoAssertData);
		position = generateEntitiesForConfiguredBedRoomsWithTwoNotVIP(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, ROOM_TYPE_1BED, position, whiteboardViewDtoAssertData);
		position = generateEntitiesForConfiguredBedRoomsWithTwoNotVIP(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, ROOM_TYPE_1BED_JUNIOR_SUITE, position, whiteboardViewDtoAssertData);
		
		//Rooms configured as 2Bed type with one patient VIP and other NO VIP
		position = generateEntitiesForConfiguredRoomsWithVIPandNotVIP(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, ROOM_TYPE_2BED, position, whiteboardViewDtoAssertData);
		
		//Rooms configured as 1Bed type with one patient VIP and other NO VIP
		position = generateEntitiesForConfiguredRoomsWithVIPandNotVIP(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, PRESIDENTIAL_SUITE, position, whiteboardViewDtoAssertData);
		position = generateEntitiesForConfiguredRoomsWithVIPandNotVIP(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, MONT_BLANC_SUITE, position, whiteboardViewDtoAssertData);
		position = generateEntitiesForConfiguredRoomsWithVIPandNotVIP(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, ROOM_TYPE_1BED, position, whiteboardViewDtoAssertData);
		position = generateEntitiesForConfiguredRoomsWithVIPandNotVIP(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, ROOM_TYPE_1BED_JUNIOR_SUITE, position, whiteboardViewDtoAssertData);
		
		//Rooms configured as 2Bed type with one patient ACC and other NO ACC
		position = generateEntitiesForConfiguredBedRoomsWithACCandNotACC(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, ROOM_TYPE_2BED, position, whiteboardViewDtoAssertData);
		
		//Rooms configured as 1Bed type with one patient ACC and other NO ACC
		position = generateEntitiesForConfiguredBedRoomsWithACCandNotACC(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, PRESIDENTIAL_SUITE, position, whiteboardViewDtoAssertData);
		position = generateEntitiesForConfiguredBedRoomsWithACCandNotACC(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, MONT_BLANC_SUITE, position, whiteboardViewDtoAssertData);
		position = generateEntitiesForConfiguredBedRoomsWithACCandNotACC(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, ROOM_TYPE_1BED, position, whiteboardViewDtoAssertData);
		position = generateEntitiesForConfiguredBedRoomsWithACCandNotACC(companyCode, hospservice, roomsConfigured1Bed, whiteBoardViewEntities, ROOM_TYPE_1BED_JUNIOR_SUITE, position, whiteboardViewDtoAssertData);
		
		//Set Insurance to null
		position = generateEntityForConfiguredRoom(companyCode, hospservice, whiteBoardViewEntities, whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_2BED, position, null, "HOSP", null);	
		
		//Skip second Bed
		position++;
		
		//Set patientclass to null
		position = generateEntityForConfiguredRoom(companyCode, hospservice, whiteBoardViewEntities, whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_2BED, position, "P", null, "P");
		
		//Skip second Bed
		position++;
		
		//Two acc same room as 2BED
		position = generateEntityForConfiguredRoom(companyCode, hospservice, whiteBoardViewEntities, whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_2BED, position, "P1", "HOSP", "ACC");
		position = generateEntityForConfiguredRoom(companyCode, hospservice, whiteBoardViewEntities, whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_2BED, position, "P1", "HOSP", "ACC");
		
		//Two VIP same room as 2BED
		position = generateEntityForConfiguredRoom(companyCode, hospservice, whiteBoardViewEntities, whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_2BED, position, "P", "HVIP", "VIP");
		position = generateEntityForConfiguredRoom(companyCode, hospservice, whiteBoardViewEntities, whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_2BED, position, "P", "HVIP", "VIP");
				
		//Two ACC same room as 1BED
		position = generateEntityForConfiguredRoom(companyCode, hospservice, whiteBoardViewEntities, whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_1BED, position, "P1", "HOSP", "ACC");
		position = generateEntityForConfiguredRoom(companyCode, hospservice, whiteBoardViewEntities, whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_1BED, position, "P1", "HOSP", "ACC");
		
		//Two VIP same room as 1BED
		position = generateEntityForConfiguredRoom(companyCode, hospservice, whiteBoardViewEntities, whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_1BED, position, "P", "HVIP", "VIP");
		position = generateEntityForConfiguredRoom(companyCode, hospservice, whiteBoardViewEntities, whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_1BED, position, "P", "HVIP", "VIP");
		
		//MEDICAL DESK		
		position++;
		roomType = MEDICAL_DESK;
		whiteBoardViewEntity = whiteBoardViewEntities.get(position);
		whiteBoardViewEntity.setRoomType(roomType);
		//patient must be in the final result
		whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), whiteBoardViewEntity.getPatientBed(), null, null);
		whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
		
		position++;
		whiteBoardViewEntity = whiteBoardViewEntities.get(position);
		whiteBoardViewEntity.setRoomType(roomType);
		//patient must be in the final result
		whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), whiteBoardViewEntity.getPatientBed(), null, null);
		whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
				
		roomsConfigured1Bed.put(whiteBoardViewEntity.getRoom(), 
				WhiteBoardMock.createWhiteboardRoomsConfigurationEntity(true, false, false, roomType, WhiteBoardMock.createWhiteboardRoomsConfigurationID(companyCode, hospservice, whiteBoardViewEntity.getRoom())));
		//END MEDICAL DESK
		
		//Test for patient Rules
		generateConfigurationAndAssertForEntity(companyCode, hospservice, whiteBoardViewEntities.get(whiteBoardViewEntities.size() - 10 ), whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_2BED, "P1", "HOSP", "ACC");
		generateConfigurationAndAssertForEntity(companyCode, hospservice, whiteBoardViewEntities.get(whiteBoardViewEntities.size() - 9 ), whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_2BED, "P2", "HOSP", "ACC");
		generateConfigurationAndAssertForEntity(companyCode, hospservice, whiteBoardViewEntities.get(whiteBoardViewEntities.size() - 8 ), whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_2BED, "P3", "HOSP", "ACC");
		generateConfigurationAndAssertForEntity(companyCode, hospservice, whiteBoardViewEntities.get(whiteBoardViewEntities.size() - 7 ), whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_2BED, "P4", "HOSP", "ACC");
		generateConfigurationAndAssertForEntity(companyCode, hospservice, whiteBoardViewEntities.get(whiteBoardViewEntities.size() - 6 ), whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_2BED, "P5", "HOSP", "ACC");
		generateConfigurationAndAssertForEntity(companyCode, hospservice, whiteBoardViewEntities.get(whiteBoardViewEntities.size() - 5 ), whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_2BED, "P6", "HOSP", "ACC");
		
		generateConfigurationAndAssertForEntity(companyCode, hospservice, whiteBoardViewEntities.get(whiteBoardViewEntities.size() - 5 ), whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_2BED, "P", "HVIP", "VIP");
		generateConfigurationAndAssertForEntity(companyCode, hospservice, whiteBoardViewEntities.get(whiteBoardViewEntities.size() - 5 ), whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_2BED, "DP", "HVIP", "VIP");
		generateConfigurationAndAssertForEntity(companyCode, hospservice, whiteBoardViewEntities.get(whiteBoardViewEntities.size() - 5 ), whiteboardViewDtoAssertData, roomsConfigured1Bed, ROOM_TYPE_2BED, "C", "HVIP", "VIP");
		
		//Only one patient in bed 1 for room 2BED
		whiteBoardViewEntity = WhiteBoardMock.getViewMock(hospservice, companyId);
		whiteBoardViewEntity.setRoom(capacities.get(capacities.size() - 7).getRoomnumber());
		whiteBoardViewEntity.setPatientBed("1");
		
		whiteBoardViewEntities.add(whiteBoardViewEntity);
		
		//patient remains and it must in bed 1 due to Room for 2 beds
		whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), "1", whiteBoardViewEntity.getPatientId(), whiteBoardViewEntity.getInsurance());
		whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
		
		//Empty row must be present for bed 2
		whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), "2", null, null);
		whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
					
		//Only one patient not VIP in bed 1 for room 1BED
		roomType = ROOM_TYPE_1BED;
		whiteBoardViewEntity = WhiteBoardMock.getViewMock(hospservice, companyId);
		whiteBoardViewEntity.setRoom(capacities.get(capacities.size() - 6).getRoomnumber());
		whiteBoardViewEntity.setPatientBed("1");
		whiteBoardViewEntity.setRoomType(roomType);
		
		whiteBoardViewEntities.add(whiteBoardViewEntity);
		
		roomsConfigured1Bed.put(whiteBoardViewEntity.getRoom(), 
				WhiteBoardMock.createWhiteboardRoomsConfigurationEntity(true, false, false, roomType, WhiteBoardMock.createWhiteboardRoomsConfigurationID(companyCode, hospservice, whiteBoardViewEntity.getRoom())));
		
		//Patient remains and it must be in bed 1
		whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), "1", whiteBoardViewEntity.getPatientId(), whiteBoardViewEntity.getInsurance());
		whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
		
		//No second BED should be show
		roomsMustNotBePresent.add(whiteBoardViewEntity.getRoom()+".2");
		
		//only one patient in second Bed for a 2BED 
		generateEntityInBed2ForConfiguredRooms(companyCode, companyId, hospservice,roomsConfigured1Bed, whiteBoardViewEntities, ROOM_TYPE_2BED, capacities.get(capacities.size() - 5).getRoomnumber(), whiteboardViewDtoAssertData, roomsMustNotBePresent);
		
		//Add patients to second Bed to rooms configured as 1Bed type add patients to end based in rooms defined by the company capacity
		generateEntityInBed2ForConfiguredRooms(companyCode, companyId, hospservice,roomsConfigured1Bed, whiteBoardViewEntities, PRESIDENTIAL_SUITE, capacities.get(capacities.size() - 4).getRoomnumber(), whiteboardViewDtoAssertData, roomsMustNotBePresent);
		generateEntityInBed2ForConfiguredRooms(companyCode, companyId, hospservice,roomsConfigured1Bed, whiteBoardViewEntities, MONT_BLANC_SUITE, capacities.get(capacities.size() - 3).getRoomnumber(), whiteboardViewDtoAssertData, roomsMustNotBePresent);
		generateEntityInBed2ForConfiguredRooms(companyCode, companyId, hospservice,roomsConfigured1Bed, whiteBoardViewEntities, ROOM_TYPE_1BED, capacities.get(capacities.size() - 2).getRoomnumber(), whiteboardViewDtoAssertData, roomsMustNotBePresent);	
		generateEntityInBed2ForConfiguredRooms(companyCode, companyId, hospservice,roomsConfigured1Bed, whiteBoardViewEntities, ROOM_TYPE_1BED_JUNIOR_SUITE, capacities.get(capacities.size() - 1).getRoomnumber(), whiteboardViewDtoAssertData, roomsMustNotBePresent);
	}
	
	
	private static void generateConfigurationAndAssertForEntity(String companyCode, String hospservice, WhiteBoardViewEntity whiteBoardViewEntity, Map<String, WhiteboardViewDtoAsserts> whiteboardViewDtoAssertData, Map<String, WhiteboardRoomsConfigurationEntity> roomsConfigured1Bed,
			String roomType, String insurance, String patientClass, String expectedInsurance) {
		
		whiteBoardViewEntity.setInsurance(insurance);
		whiteBoardViewEntity.setPatientClass(patientClass);
		whiteBoardViewEntity.setRoomType(roomType);
		
		//patient must be in the final result
		WhiteboardViewDtoAsserts whiteboardViewDtoAsserts;
		whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), whiteBoardViewEntity.getPatientBed(), whiteBoardViewEntity.getPatientId(), expectedInsurance);
		whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
		
		roomsConfigured1Bed.put(whiteBoardViewEntity.getRoom(), 
				WhiteBoardMock.createWhiteboardRoomsConfigurationEntity(true, false, false, roomType, WhiteBoardMock.createWhiteboardRoomsConfigurationID(companyCode, hospservice, whiteBoardViewEntity.getRoom())));
	}
	
	public static Integer generateEntityForConfiguredRoom(String companyCode, String hospservice, List<WhiteBoardViewEntity> whiteBoardViewEntities, Map<String, WhiteboardViewDtoAsserts> whiteboardViewDtoAssertData, Map<String, WhiteboardRoomsConfigurationEntity> roomsConfigured1Bed,
			String roomType, Integer position, String insurance, String patientClass, String expectedInsurance) {
		
		position++;
		WhiteBoardViewEntity whiteBoardViewEntity;
		whiteBoardViewEntity = whiteBoardViewEntities.get(position);
		
		generateConfigurationAndAssertForEntity(companyCode, hospservice, whiteBoardViewEntity, whiteboardViewDtoAssertData, roomsConfigured1Bed, roomType, insurance, patientClass, expectedInsurance);		
		
		return position;
	}

	public static Integer generateEntitiesForConfiguredRoomsWithVIPandACC(String companyCode, String hospservice, Map<String, WhiteboardRoomsConfigurationEntity> roomsConfigurationMap, List<WhiteBoardViewEntity> whiteBoardViewEntities, String roomType, Integer position, Map<String, WhiteboardViewDtoAsserts> whiteboardViewDtoAssertData, List<String> roomsMustNotBePresent) {
		WhiteBoardViewEntity whiteBoardViewEntity;
		WhiteboardViewDtoAsserts whiteboardViewDtoAsserts;
		
		//VIP patient
		position++;
		whiteBoardViewEntity = whiteBoardViewEntities.get(position);
		whiteBoardViewEntity.setPatientClass("HVIP");
		whiteBoardViewEntity.setRoomType(roomType);

		whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), "1", whiteBoardViewEntity.getPatientId(), "VIP");
		whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);

		//ACC patient
		position++;
		whiteBoardViewEntity = whiteBoardViewEntities.get(position);
		whiteBoardViewEntity.setInsurance("P1");
		whiteBoardViewEntity.setRoomType(roomType);
		
		if(ROOM_TYPE_2BED.equals(roomType)) {
			whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), "2", whiteBoardViewEntity.getPatientId(), "ACC");
			whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
		} else {
			roomsMustNotBePresent.add(whiteBoardViewEntity.getRoom()+".2");
		}
		
		roomsConfigurationMap.put(whiteBoardViewEntity.getRoom(), 
				WhiteBoardMock.createWhiteboardRoomsConfigurationEntity(true, false, false, roomType, WhiteBoardMock.createWhiteboardRoomsConfigurationID(companyCode, hospservice, whiteBoardViewEntity.getRoom())));
		
		//ACC patient first Bed
		position++;
		whiteBoardViewEntity = whiteBoardViewEntities.get(position);
		whiteBoardViewEntity.setInsurance("P1");
		whiteBoardViewEntity.setRoomType(roomType);
		if(ROOM_TYPE_2BED.equals(roomType)) {
			whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), "1", whiteBoardViewEntity.getPatientId(), "ACC");
			whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
		}
		
		//VIP patient second bed
		position++;
		whiteBoardViewEntity = whiteBoardViewEntities.get(position);
		whiteBoardViewEntity.setPatientClass("HVIP");
		whiteBoardViewEntity.setRoomType(roomType);
		
		if(ROOM_TYPE_2BED.equals(roomType)) {
			whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), "2", whiteBoardViewEntity.getPatientId(), "VIP");
			whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
		} else {
			//patient VIP remains and it must in bed 1
			whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), "1", whiteBoardViewEntity.getPatientId(), "VIP");
			whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
			
			roomsMustNotBePresent.add(whiteBoardViewEntity.getRoom()+".2");
		}
		
		roomsConfigurationMap.put(whiteBoardViewEntity.getRoom(), 
				WhiteBoardMock.createWhiteboardRoomsConfigurationEntity(true, false, false, roomType, WhiteBoardMock.createWhiteboardRoomsConfigurationID(companyCode, hospservice, whiteBoardViewEntity.getRoom())));
		
		return position;
	}
	
	public static Integer generateEntitiesForConfiguredRoomsWithVIPandNotVIP(String companyCode, String hospservice, Map<String, WhiteboardRoomsConfigurationEntity> roomsConfigurationMap, List<WhiteBoardViewEntity> whiteBoardViewEntities, String roomType, Integer position, Map<String, WhiteboardViewDtoAsserts> whiteboardViewDtoAssertData) {
		WhiteBoardViewEntity whiteBoardViewEntity;
		WhiteboardViewDtoAsserts whiteboardViewDtoAsserts;
		
		//VIP patient first bed
		position++;
		whiteBoardViewEntity = whiteBoardViewEntities.get(position);
		whiteBoardViewEntity.setPatientClass("HVIP");
		whiteBoardViewEntity.setRoomType(roomType);
		
		//patient VIP remains and it must in bed 1
		whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), "1", whiteBoardViewEntity.getPatientId(), "VIP");
		whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
		
		//P Patient second bed
		position++;
		whiteBoardViewEntity = whiteBoardViewEntities.get(position);
		whiteBoardViewEntity.setRoomType(roomType);
		
		//patient P remains and it must in bed 2
		whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), "2", whiteBoardViewEntity.getPatientId(), "P");
		whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
				
		roomsConfigurationMap.put(whiteBoardViewEntity.getRoom(), 
				WhiteBoardMock.createWhiteboardRoomsConfigurationEntity(true, false, false, roomType, WhiteBoardMock.createWhiteboardRoomsConfigurationID(companyCode, hospservice, whiteBoardViewEntity.getRoom())));
		
		//P patient first Bed
		position++;
		whiteBoardViewEntity = whiteBoardViewEntities.get(position);
		whiteBoardViewEntity.setRoomType(roomType);
		
		whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), "1", whiteBoardViewEntity.getPatientId(), "P");
		whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
		
		//VIP patient second bed
		position++;
		whiteBoardViewEntity = whiteBoardViewEntities.get(position);
		whiteBoardViewEntity.setPatientClass("HVIP");
		whiteBoardViewEntity.setRoomType(roomType);
		
		roomsConfigurationMap.put(whiteBoardViewEntity.getRoom(), 
				WhiteBoardMock.createWhiteboardRoomsConfigurationEntity(true, false, false, roomType, WhiteBoardMock.createWhiteboardRoomsConfigurationID(companyCode, hospservice, whiteBoardViewEntity.getRoom())));
		
		return position;
	}
	
	
	private static Integer generateEntitiesForConfiguredBedRoomsWithTwoNotVIP(String companyCode, String hospservice, Map<String, WhiteboardRoomsConfigurationEntity> roomsConfigurationMap, List<WhiteBoardViewEntity> whiteBoardViewEntities, String roomType, Integer position, Map<String, WhiteboardViewDtoAsserts> whiteboardViewDtoAssertData) {
		WhiteBoardViewEntity whiteBoardViewEntity;
		WhiteboardViewDtoAsserts whiteboardViewDtoAsserts;
		
		//Default patient P
		position++;
		whiteBoardViewEntity = whiteBoardViewEntities.get(position);
		whiteBoardViewEntity.setRoomType(roomType);
		
		//must remain
		whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), whiteBoardViewEntity.getPatientBed(), whiteBoardViewEntity.getPatientId(), "P");
		whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
		
		//Default patient P
		position++;
		whiteBoardViewEntity = whiteBoardViewEntities.get(position);
		whiteBoardViewEntity.setRoomType(roomType);
		
		//must remain
		whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), whiteBoardViewEntity.getPatientBed(), whiteBoardViewEntity.getPatientId(), "P");
		whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
				
		roomsConfigurationMap.put(whiteBoardViewEntity.getRoom(), 
				WhiteBoardMock.createWhiteboardRoomsConfigurationEntity(true, false, false, roomType, WhiteBoardMock.createWhiteboardRoomsConfigurationID(companyCode, hospservice, whiteBoardViewEntity.getRoom())));
		
		return position;
	}
	
	private static Integer generateEntitiesForConfiguredBedRoomsWithACCandNotACC(String companyCode, String hospservice, Map<String, WhiteboardRoomsConfigurationEntity> roomsConfigurationMap, List<WhiteBoardViewEntity> whiteBoardViewEntities, String roomType, Integer position, Map<String, WhiteboardViewDtoAsserts> whiteboardViewDtoAssertData) {
		WhiteBoardViewEntity whiteBoardViewEntity;
		WhiteboardViewDtoAsserts whiteboardViewDtoAsserts;
		
		//Default patient P
		position++;
		whiteBoardViewEntity = whiteBoardViewEntities.get(position);
		whiteBoardViewEntity.setInsurance("P1");
		whiteBoardViewEntity.setRoomType(roomType);
		
		//must remain
		whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), whiteBoardViewEntity.getPatientBed(), whiteBoardViewEntity.getPatientId(), "ACC");
		whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
		
		//Default patient P
		position++;
		whiteBoardViewEntity = whiteBoardViewEntities.get(position);
		whiteBoardViewEntity.setRoomType(roomType);
		
		//must remain
		whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), whiteBoardViewEntity.getPatientBed(), whiteBoardViewEntity.getPatientId(), "P");
		whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
				
		roomsConfigurationMap.put(whiteBoardViewEntity.getRoom(), 
				WhiteBoardMock.createWhiteboardRoomsConfigurationEntity(true, false, false, roomType, WhiteBoardMock.createWhiteboardRoomsConfigurationID(companyCode, hospservice, whiteBoardViewEntity.getRoom())));
		
		//Default patient P
		position++;
		whiteBoardViewEntity = whiteBoardViewEntities.get(position);
		whiteBoardViewEntity.setRoomType(roomType);
		
		//must remain
		whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), whiteBoardViewEntity.getPatientBed(), whiteBoardViewEntity.getPatientId(), "P");
		whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
		
		//Default patient P
		position++;
		whiteBoardViewEntity = whiteBoardViewEntities.get(position);
		whiteBoardViewEntity.setInsurance("P1");
		whiteBoardViewEntity.setRoomType(roomType);
		
		//must remain
		whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), whiteBoardViewEntity.getPatientBed(), whiteBoardViewEntity.getPatientId(), "ACC");
		whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
				
		roomsConfigurationMap.put(whiteBoardViewEntity.getRoom(), 
				WhiteBoardMock.createWhiteboardRoomsConfigurationEntity(true, false, false, roomType, WhiteBoardMock.createWhiteboardRoomsConfigurationID(companyCode, hospservice, whiteBoardViewEntity.getRoom())));
		
		return position;
	}
	
	
	private static void generateEntityInBed2ForConfiguredRooms(String companyCode, Long companyId, String hospservice, Map<String, WhiteboardRoomsConfigurationEntity> roomsConfigured1Bed, List<WhiteBoardViewEntity> whiteBoardViewEntities, String roomType, String room, Map<String, WhiteboardViewDtoAsserts> whiteboardViewDtoAssertData, List<String> roomsMustNotBePresent) {
		WhiteBoardViewEntity whiteBoardViewEntity;
		
		whiteBoardViewEntity = WhiteBoardMock.getViewMock(hospservice, companyId);
		whiteBoardViewEntity.setRoom(room);
		whiteBoardViewEntity.setPatientBed("2");
		whiteBoardViewEntity.setRoomType(roomType);
		
		whiteBoardViewEntities.add(whiteBoardViewEntity);
		
		roomsConfigured1Bed.put(room, 
				WhiteBoardMock.createWhiteboardRoomsConfigurationEntity(true, false, false, roomType, WhiteBoardMock.createWhiteboardRoomsConfigurationID(companyCode, hospservice, room)));		
		
		WhiteboardViewDtoAsserts whiteboardViewDtoAsserts;
		if(ROOM_TYPE_2BED.equals(roomType)) {
			//Bed 1 remains empty
			whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), "1", null, null);
			whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
			
			//patient remains and it must in bed 2 due to Room for 2 beds
			whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), "2", whiteBoardViewEntity.getPatientId(), whiteBoardViewEntity.getInsurance());
			whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
		} else {
			//patient remains and it must in bed 1 due to Roomm not for 2 beds
			whiteboardViewDtoAsserts = new WhiteboardViewDtoAsserts(whiteBoardViewEntity.getRoom(), "1", whiteBoardViewEntity.getPatientId(), whiteBoardViewEntity.getInsurance());
			whiteboardViewDtoAssertData.put(whiteboardViewDtoAsserts.getKey(), whiteboardViewDtoAsserts);
			
			//No second BED should be show
			roomsMustNotBePresent.add(whiteBoardViewEntity.getRoom()+".2");
		}
	}

}
