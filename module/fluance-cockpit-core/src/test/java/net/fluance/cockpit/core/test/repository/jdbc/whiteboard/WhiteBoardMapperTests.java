package net.fluance.cockpit.core.test.repository.jdbc.whiteboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import net.fluance.cockpit.core.model.jdbc.whiteboard.WhiteBoardMapper;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardEntryDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewEntity;
import net.fluance.cockpit.core.test.mocks.resultset.ResultSetMock;
import net.fluance.cockpit.core.util.whiteboard.WhiteBoardMock;

public class WhiteBoardMapperTests {

	private ResultSetMock resultSetMock;
	private ResultSetMock resultSetNurse;
	private ResultSet resultSetCount;
	private final static String ID = "1";
	private final static String ROOM = "404C";
	private final static String SEX = "Männlich";
	private final static String FIRSTNAME = "Rick";
	private final static String LASTNAME = "Sanchez";
	private final static String ENTREE_DATE = "1535702105845";
	private final static String ASSURANCE = "C";
	private final static String COMPANY_ID = "3";
	private final static String HL7CODE = "I";
	private static final String PATIENT_BED = "1";
	private final static String SERVICE_ID = "GOD1";
	private final static String VISIT_NUMBER = "4542";
	private final static String DOCTOR = "Dr. Müller";
	private final static String NURSE = "Stephanie";
	private final static String DIET = "diet";
	private final static String OPERATION_DATE = "1535702105845";
	private static final String EDITED_EXPIRE_DATE = "1535702105845";
	private final static String ISOLATION_TYPE = "A";
	private final static String COMMENT = "A comment";
	private final static String REASON = "A reaseon";
	private final static String EXPIRE_DATE = "1535702105845";
	private final static String APPOINTMENT_ID = "1232";
	private static final String EDITED_OPERATION_DATE = "1535702105845";
	private final static String PATIENT_ID = "34543";
	private final static String EDITED_PHYSICIAN = "Dr. Hanswurst";
	private final static String PATIENT_CLASS = "AB";
	private static final String BIRTHDATE = "1535702105845";
	private static final String EDIT_REASON = "A reason";
	private static final String ORIGINAL_CAPACITY = "2";
	private static final String CONF_CAPACITY = "2";
	private static final String ROOM_TYPE = "2BED";

	@Before
	public void initalise() {
		Map<String, Integer> columnMap = new HashMap<>();
		int i = 0;
		columnMap.put(WhiteBoardMapper.ID, i++);
		columnMap.put(WhiteBoardMapper.ROOM, i++);
		columnMap.put(WhiteBoardMapper.SEX, i++);
		columnMap.put(WhiteBoardMapper.FIRSTNAME, i++);
		columnMap.put(WhiteBoardMapper.LASTNAME, i++);
		columnMap.put(WhiteBoardMapper.ENTREE_DATE, i++);
		columnMap.put(WhiteBoardMapper.ASSURANCE, i++);
		columnMap.put(WhiteBoardMapper.COMPANY_ID, i++);
		columnMap.put(WhiteBoardMapper.HL7CODE, i++);
		columnMap.put(WhiteBoardMapper.PATIENT_BED, i++);
		columnMap.put(WhiteBoardMapper.SERVICE_ID, i++);
		columnMap.put(WhiteBoardMapper.VISIT_NUMBER, i++);
		columnMap.put(WhiteBoardMapper.DOCTOR, i++);
		columnMap.put(WhiteBoardMapper.NURSE, i++);
		columnMap.put(WhiteBoardMapper.DIET, i++);
		columnMap.put(WhiteBoardMapper.OPERATION_DATE, i++);
		columnMap.put(WhiteBoardMapper.EDITED_EXPIRED_DATE, i++);
		columnMap.put(WhiteBoardMapper.ISOLATION_TYPE, i++);
		columnMap.put(WhiteBoardMapper.COMMENT, i++);
		columnMap.put(WhiteBoardMapper.REASON, i++);
		columnMap.put(WhiteBoardMapper.EXPIRE_DATE, i++);
		columnMap.put(WhiteBoardMapper.APPOINTMENT_ID, i++);
		columnMap.put(WhiteBoardMapper.EDITED_OPERATION_DATE_VIEW, i++);
		columnMap.put(WhiteBoardMapper.PATIENT_ID, i++);
		columnMap.put(WhiteBoardMapper.EDITED_PHYSICIAN, i++);
		columnMap.put(WhiteBoardMapper.PATIENT_CLASS, i++);
		columnMap.put(WhiteBoardMapper.BIRTHDATE, i++);
		columnMap.put(WhiteBoardMapper.EDITED_REASON, i++);
		columnMap.put(WhiteBoardMapper.ORIGINAL_CAPACITY, i++);
		columnMap.put(WhiteBoardMapper.CONF_CAPACITY, i++);
		columnMap.put(WhiteBoardMapper.ROOM_TYPE, i);

		String values = ID + "," + ROOM + "," + SEX + "," + FIRSTNAME + "," + LASTNAME + "," + ENTREE_DATE + "," + ASSURANCE + "," + COMPANY_ID + "," + HL7CODE + "," + PATIENT_BED + "," + SERVICE_ID + "," + VISIT_NUMBER + "," + DOCTOR + ","
				+ NURSE + "," + DIET + "," + OPERATION_DATE + "," + EDITED_EXPIRE_DATE + "," + ISOLATION_TYPE + "," + COMMENT + "," + REASON + "," + EXPIRE_DATE + "," + APPOINTMENT_ID + "," + EDITED_OPERATION_DATE + "," + PATIENT_ID + ","
				+ EDITED_PHYSICIAN + "," + PATIENT_CLASS + "," + BIRTHDATE + "," + EDIT_REASON +"," + ORIGINAL_CAPACITY + "," + CONF_CAPACITY + "," + ROOM_TYPE;
		String[] row = values.split(",");
		this.resultSetMock = new ResultSetMock(columnMap, row);
		Map<String, Integer> columnMapNurse = new HashMap<>();
		columnMapNurse.put(WhiteBoardMapper.NURSE, 0);
		String valuesNurse = NURSE + ",";
		String[] rowNurse = valuesNurse.split(",");
		this.resultSetNurse = new ResultSetMock(columnMapNurse, rowNurse);
		Map<String, Integer> columnMapCount = new HashMap<>();
		columnMapCount.put("c", 0);
		String valueCount = true + ",";
		String[] rowCount = valueCount.split(",");
		this.resultSetCount = new ResultSetMock(columnMapCount, rowCount);
	}

	@Test
	public void test_row_mapper_nurse() throws SQLException {
		String nurseMapping = WhiteBoardMapper.NURSE_MAPPER.mapRow(this.resultSetNurse, 0);
		assertEquals("Is right mapping?", NURSE, nurseMapping);
	}

	@Test
	public void test_row_mapper_count() throws SQLException {
		Boolean countMapping = WhiteBoardMapper.COUNT_MAPPER.mapRow(this.resultSetCount, 0);
		Assert.assertTrue("Is right mapping?", countMapping);
	}

	@Test
	public void test_row_mapper() throws SQLException {
		WhiteBoardViewEntity whiteBoardEntry = WhiteBoardMapper.ROW_MAPPER.mapRow(this.resultSetMock, 0);
		assertEquals("Has right ID?", whiteBoardEntry.getId(), Long.valueOf(ID));
		assertEquals("Has right Room?", whiteBoardEntry.getRoom(), ROOM);
		assertEquals("Has right Sex?", whiteBoardEntry.getSex(), SEX);
		assertEquals("Has right Firstname?", whiteBoardEntry.getFirstname(), FIRSTNAME);
		assertEquals("Has right Lastname?", whiteBoardEntry.getLastname(), LASTNAME);
		assertEquals("Has right EntreeDate?", whiteBoardEntry.getEntreeDate(), new Timestamp(Long.parseLong(ENTREE_DATE)));
		assertEquals("Has right Assurance?", whiteBoardEntry.getInsurance(), (ASSURANCE));
		assertEquals("Has right ComapnyId?", whiteBoardEntry.getCompanyId(), Long.valueOf(COMPANY_ID));
		assertEquals("Has right H7Code?", whiteBoardEntry.getHl7Code(), HL7CODE);
		assertEquals("Has right ServiceId?", whiteBoardEntry.getServiceId(), SERVICE_ID);
		assertEquals("Has right VisitNumber?", whiteBoardEntry.getVisitNumber(), Long.valueOf(VISIT_NUMBER));
		assertEquals("Has right Doctor?", whiteBoardEntry.getPhysician(), DOCTOR);
		assertEquals("Has right Nurse?", whiteBoardEntry.getNurseName(), NURSE);
		assertEquals("Has right Diet?", whiteBoardEntry.getDiet(), DIET);
		assertEquals("Has right Operation Date?", whiteBoardEntry.getOperationDate(), new Timestamp(Long.parseLong(OPERATION_DATE)));
		assertEquals("Has right Isolation?", whiteBoardEntry.getIsolationType(), ISOLATION_TYPE);
		assertEquals("Has right Comment?", whiteBoardEntry.getComment(), COMMENT);
		assertEquals("Has right Reason?", whiteBoardEntry.getReason(), REASON);
		assertEquals("Has right Expire Date?", whiteBoardEntry.getExpireDate(), new Timestamp(Long.parseLong(EXPIRE_DATE)));
		assertEquals("Has right AppointmentID?", whiteBoardEntry.getAppointmentId(), Long.valueOf(APPOINTMENT_ID));
		assertEquals("Has right PatientID?", whiteBoardEntry.getPatientId(), Long.valueOf(PATIENT_ID));
		assertEquals("Has right Patinetnbed?", whiteBoardEntry.getPatientBed(), PATIENT_BED);
		assertEquals("Has right Edited Doctor?", whiteBoardEntry.getEditedPhysician(), EDITED_PHYSICIAN);
	}

	@Test
	public void test_row_mapper_with_null() throws SQLException {
		WhiteBoardViewEntity whiteBoardEntry = WhiteBoardMapper.ROW_MAPPER.mapRow(null, 0);
		Assert.assertNull("Is null?", whiteBoardEntry);
	}

	@Test
	public void test_row_unmapper() {
		WhiteBoardViewEntity mock = WhiteBoardMock.getViewMock();
		Map<String, Object> mapped = WhiteBoardMapper.ROW_UNMAPPER.mapColumns(mock);
		assertSame("Is mapping ok? [id]", mapped.get(WhiteBoardMapper.ID), mock.getId());
		assertSame("Is mapping ok? [ass]", mapped.get(WhiteBoardMapper.ASSURANCE), mock.getInsurance());
		assertSame("Is mapping ok? [comment]", mapped.get(WhiteBoardMapper.COMMENT), mock.getComment());
		assertSame("Is mapping ok? [doctor]", mapped.get(WhiteBoardMapper.DOCTOR), mock.getPhysician());
		assertSame("Is mapping ok? [entree_date]", mapped.get(WhiteBoardMapper.ENTREE_DATE), mock.getEntreeDate());
		assertSame("Is mapping ok? [expire_date]", mapped.get(WhiteBoardMapper.EXPIRE_DATE), mock.getExpireDate());
		assertSame("Is mapping ok? [diet]", mapped.get(WhiteBoardMapper.DIET), mock.getDiet());
		assertSame("Is mapping ok? [nurse]", mapped.get(WhiteBoardMapper.NURSE), mock.getNurseName());
		assertSame("Is mapping ok? [operation_date]", mapped.get(WhiteBoardMapper.OPERATION_DATE), mock.getOperationDate());
		assertSame("Is mapping ok? [reason]", mapped.get(WhiteBoardMapper.REASON), mock.getReason());
		assertSame("Is mapping ok? [room]", mapped.get(WhiteBoardMapper.ROOM), mock.getRoom());
		assertSame("Is mapping ok? [firstname]", mapped.get(WhiteBoardMapper.FIRSTNAME), mock.getFirstname());
		assertSame("Is mapping ok? [lastname]", mapped.get(WhiteBoardMapper.LASTNAME), mock.getLastname());
		assertSame("Is mapping ok? [sex]", mapped.get(WhiteBoardMapper.SEX), mock.getSex());
		assertSame("Is mapping ok? [isolation]", mapped.get(WhiteBoardMapper.ISOLATION_TYPE), mock.getIsolationType());
		assertSame("Is mapping ok? [appointment]", mapped.get(WhiteBoardMapper.APPOINTMENT_ID), mock.getAppointmentId());
		assertSame("Is mapping ok? [patientId]", mapped.get(WhiteBoardMapper.PATIENT_ID), mock.getPatientId());
		assertSame("Is mapping ok? [serviceId]", mapped.get(WhiteBoardMapper.SERVICE_ID), mock.getServiceId());
		assertSame("Is mapping ok? [visitNumber]", mapped.get(WhiteBoardMapper.VISIT_NUMBER), mock.getVisitNumber());
	}

	@Test
	public void test_toEntity_WhiteBoardViewEntity() {
		WhiteBoardViewDTO whiteBoardView = WhiteBoardMock.getDTOMock();
		WhiteBoardViewEntity whiteBoardViewEntity = WhiteBoardMapper.toEntity(whiteBoardView);
		assertEquals("Is converting ok? [id]", whiteBoardViewEntity.getId(), whiteBoardView.getId());
		assertEquals("Is converting ok? [diet]", whiteBoardViewEntity.getDiet(), listToString(whiteBoardView.getDiet()));
		assertEquals("Is converting ok? [appointment_id]", whiteBoardViewEntity.getAppointmentId(), whiteBoardView.getAppointmentId());
		assertEquals("Is converting ok? [comment]", whiteBoardViewEntity.getComment(), whiteBoardView.getComment());
		assertEquals("Is converting ok? [company_id]", whiteBoardViewEntity.getCompanyId(), whiteBoardView.getCompanyId());
		assertSame("Is converting ok? [entree_date]", whiteBoardViewEntity.getEntreeDate(), whiteBoardView.getEntreeDate());
		assertSame("Is converting ok? [expire_date]", whiteBoardViewEntity.getExpireDate(), whiteBoardView.getExpireDate());
		assertEquals("Is converting ok? [firstname]", whiteBoardViewEntity.getFirstname(), whiteBoardView.getFirstname());
		assertEquals("Is converting ok? [hl7code]", whiteBoardViewEntity.getHl7Code(), whiteBoardView.getHl7Code());
		assertEquals("Is converting ok? [insurance]", whiteBoardViewEntity.getInsurance(), whiteBoardView.getInsurance());
		assertEquals("Is converting ok? [lastname]", whiteBoardViewEntity.getLastname(), whiteBoardView.getLastname());
		assertEquals("Is converting ok? [nurse_name]", whiteBoardViewEntity.getNurseName(), whiteBoardView.getNurseName());
		assertSame("Is converting ok? [operation_date]", whiteBoardViewEntity.getOperationDate(), whiteBoardView.getOperationDate());
		assertSame("Is converting ok? [patient_id]", whiteBoardViewEntity.getPatientId(), whiteBoardView.getPatientId());
		assertEquals("Is converting ok? [physician]", whiteBoardViewEntity.getPhysician(), listToString(whiteBoardView.getPhysician()));
		assertEquals("Is converting ok? [room]", whiteBoardViewEntity.getRoom(), whiteBoardView.getRoom());
		assertEquals("Is converting ok? [sex]", whiteBoardViewEntity.getSex(), whiteBoardView.getSex());
		assertEquals("Is converting ok? [visit_number]", whiteBoardViewEntity.getVisitNumber(), whiteBoardView.getVisitNumber());
	}

	@Test
	public void test_toEntity_WhiteBoardViewEntity_list() {
		List<WhiteBoardViewEntity> entities = WhiteBoardMock.getMocks(1);
		List<WhiteBoardViewDTO> whiteBoardViewDTOs = WhiteBoardMapper.toModel(entities);
		WhiteBoardViewDTO whiteBoardViewDTO = whiteBoardViewDTOs.get(0);
		WhiteBoardViewEntity whiteBoardView = entities.get(0);
		assertEquals("Is converting ok? [id]", whiteBoardViewDTO.getId(), whiteBoardView.getId());
		assertEquals("Is converting ok? [diet]", whiteBoardViewDTO.getDiet(), stringToList(whiteBoardView.getDiet()));
		assertEquals("Is converting ok? [appointment_id]", whiteBoardViewDTO.getAppointmentId(), whiteBoardView.getAppointmentId());
		assertEquals("Is converting ok? [comment]", whiteBoardViewDTO.getComment(), whiteBoardView.getComment());
		assertEquals("Is converting ok? [company_id]", whiteBoardViewDTO.getCompanyId(), whiteBoardView.getCompanyId());
		assertSame("Is converting ok? [entree_date]", whiteBoardViewDTO.getEntreeDate(), whiteBoardView.getEntreeDate());
		assertSame("Is converting ok? [expire_date]", whiteBoardViewDTO.getExpireDate(), whiteBoardView.getExpireDate());
		assertEquals("Is converting ok? [firstname]", whiteBoardViewDTO.getFirstname(), whiteBoardView.getFirstname());
		assertEquals("Is converting ok? [hl7code]", whiteBoardViewDTO.getHl7Code(), whiteBoardView.getHl7Code());
		assertEquals("Is converting ok? [insurance]", whiteBoardViewDTO.getInsurance(), whiteBoardView.getInsurance());
		assertEquals("Is converting ok? [lastname]", whiteBoardViewDTO.getLastname(), whiteBoardView.getLastname());
		assertEquals("Is converting ok? [nurse_name]", whiteBoardViewDTO.getNurseName(), whiteBoardView.getNurseName());
		assertSame("Is converting ok? [operation_date]", whiteBoardViewDTO.getOperationDate(), whiteBoardView.getOperationDate());
		assertEquals("Is converting ok? [patient_id]", whiteBoardViewDTO.getPatientId(), whiteBoardView.getPatientId());
		assertEquals("Is converting ok? [physician]", whiteBoardViewDTO.getPhysician(), stringToList(whiteBoardView.getPhysician()));
		assertEquals("Is converting ok? [room]", whiteBoardViewDTO.getRoom(), whiteBoardView.getRoom());
		assertEquals("Is converting ok? [sex]", whiteBoardViewDTO.getSex(), whiteBoardView.getSex());
		assertEquals("Is converting ok? [visit_number]", whiteBoardViewDTO.getVisitNumber(), whiteBoardView.getVisitNumber());
	}

	@Test
	public void test_toEntry_Model() {
		List<WhiteBoardViewEntity> whiteBoardViewEntity = WhiteBoardMock.getMocks(1);
		WhiteBoardViewEntity whiteBoardView = whiteBoardViewEntity.get(0);
		List<WhiteBoardEntryDTO> entries = WhiteBoardMapper.toEntryModel(whiteBoardViewEntity);
		WhiteBoardEntryDTO entry = entries.get(0);
		assertSame("Is converting ok? [id]", entry.getId(), whiteBoardView.getId());
		assertEquals("Is converting ok? [diet]", entry.getDiet(), stringToList(whiteBoardView.getDiet()));
		assertSame("Is converting ok? [nurse_name]", entry.getNurseName(), whiteBoardView.getNurseName());
		assertSame("Is converting ok? [operation_date]", entry.getOperationDate(), whiteBoardView.getOperationDate());
		assertSame("Is converting ok? [visit_number]", entry.getVisitNumber(), whiteBoardView.getVisitNumber());
		assertSame("Is converting ok? [comment]", entry.getComment(), whiteBoardView.getComment());
		assertSame("Is converting ok? [isolation]", entry.getIsolationType(), whiteBoardView.getIsolationType());
		assertSame("Is converting ok? [isolation]", entry.getDischargeDate(), whiteBoardView.getDischargeDate());
		assertEquals("Is converting ok? [reason]", entry.getReason(), stringToList(whiteBoardView.getEditedReason()));
		assertEquals("Is converting ok? [physician]", entry.getPhysician(), stringToList(whiteBoardView.getEditedPhysician()));
	}

	@Test
	public void test_string_to_list_null() {
		WhiteBoardViewDTO model = new WhiteBoardViewDTO();
		model.setReason(null);
		WhiteBoardViewEntity entity = WhiteBoardMapper.toEntity(model);
		Assert.assertNull(entity.getReason());
	}

	@Test
	public void test_string_to_list_one_arrgument() {
		WhiteBoardViewDTO model = new WhiteBoardViewDTO();
		List<String> reasons = new ArrayList<>();
		reasons.add("Hallo");
		model.setReason(reasons);
		WhiteBoardViewEntity entity = WhiteBoardMapper.toEntity(model);
		Assert.assertNotNull(entity.getReason());
		assertEquals("[\"Hallo\"]", entity.getReason());
	}

	@Test
	public void test_string_to_list_two_arrguments() {
		WhiteBoardViewDTO model = new WhiteBoardViewDTO();
		List<String> reasons = new ArrayList<>();
		reasons.add("Hallo");
		reasons.add("Rick");
		model.setReason(reasons);
		WhiteBoardViewEntity entity = WhiteBoardMapper.toEntity(model);
		Assert.assertNotNull(entity.getReason());
		assertEquals("[\"Hallo\",\"Rick\"]", entity.getReason());
	}

	@Test
	public void test_list_to_string_null() {
		WhiteBoardViewEntity model = new WhiteBoardViewEntity();
		model.setReason(null);
		WhiteBoardViewDTO entity = WhiteBoardMapper.toModel(model);
		Assert.assertNull(entity.getReason());
	}


	@Test
	public void test_list_to_string_one_arrgument() {
		WhiteBoardViewEntity model = new WhiteBoardViewEntity();
		model.setReason("[\"Hallo\"]");
		WhiteBoardViewDTO entity = WhiteBoardMapper.toModel(model);
		assertEquals(1, entity.getReason().size());
		assertEquals("Hallo", entity.getReason().get(0));
	}

	@Test
	public void test_list_to_string_two_arrguments() {
		WhiteBoardViewEntity model = new WhiteBoardViewEntity();
		model.setReason("\"Hallo\",\"Rick\"");
		WhiteBoardViewDTO entity = WhiteBoardMapper.toModel(model);
		assertEquals(2, entity.getReason().size());
		assertEquals("Hallo", entity.getReason().get(0));
		assertEquals("Rick", entity.getReason().get(1));
	}


	private static String listToString(List<String> list) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		StringBuilder s = new StringBuilder();
		for (String listString : list) {
			s.append(",\"").append(listString).append("\"");
		}
		return "[" + s.substring(1).concat("]");
	}


	protected static List<String> stringToList(String s) {
		List<String> list = new ArrayList<>();
		try {
			if (StringUtils.isEmpty(s)) {
				return null;
			}
			if (!s.substring(0, 1).equals("[")) {
				s = "[" + s;
			}
			if (!s.substring(s.length() - 2, s.length() - 1).equals("]")) {
				s += "]";
			}
			JSONArray jsonArray = new JSONArray(s);
			int len = jsonArray.length();
			for (int i = 0; i < len; i++) {
				list.add(jsonArray.get(i).toString());
			}
		} catch (Exception e) {
			return null;
		}
		return list;

	}
}
