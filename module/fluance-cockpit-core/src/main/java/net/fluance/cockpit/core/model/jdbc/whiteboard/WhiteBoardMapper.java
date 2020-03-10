package net.fluance.cockpit.core.model.jdbc.whiteboard;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.nurkiewicz.jdbcrepository.RowUnmapper;

import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardEntryDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewEntity;
import net.fluance.commons.sql.SqlUtils;

public class WhiteBoardMapper {


  private WhiteBoardMapper() {
    throw new IllegalStateException("Utility class");
  }

  public static final String ROOM = "patientroom";

  public static final String REASON = "admitreason";

  public static final String EDITED_REASON = "editedadmitreason";

  public static final String EDITED_REASON_WHITEBOAD_TABLE = "admitreason";

  public static final String EDITED_EXPIRED_DATE = "editeddischargedt";

  public static final String OPERATION_DATE = "operationdate";

  // we need here the value of the table, not what it's actually named in the frontend
  public static final String EDITED_OPERATION_DATE = "operationdate";

  public static final String FIRSTNAME = "firstname";

  public static final String LASTNAME = "lastname";

  public static final String NURSE = "nurse";


  public static final String DIET = "diet";

  public static final String EXPIRE_DATE = "dischargedt";

  public static final String ENTREE_DATE = "admitdt";

  public static final String EXP_ENTREE_DATE = "expadmitdt";

  public static final String DOCTOR = "physician";

  public static final String COMMENT = "reserve";

  public static final String ASSURANCE = "assurance";

//  public static final String INSURANCE = "insurance";

  public static final String ID = "id";

  public static final String VISIT_NUMBER = "visit_nb";

  public static final String SEX = "sex";

  public static final String ISOLATION_TYPE = "isolationtype";

  public static final String SERVICE_ID = "hospservice";

  public static final String COMPANY_ID = "company_id";

  public static final String HL7CODE = "hl7code";

  public static final String PATIENT_ID = "patient_id";

  public static final String APPOINTMENT_ID = "appointment_id";

  public static final String PATIENT_BED = "patientbed";

  public static final String EDITED_PHYSICIAN = "editedPhysician";

  public static final String EDITED_PHYSICIAN_WHITEBOAD_TABLE = "physician";

  public static final String BIRTHDATE = "birthdate";

  public static final String EDITED_OPERATION_DATE_VIEW = "editedoperationdate";

  public static final String PATIENT_CLASS = "patientclass";

  public static final String ORIGINAL_CAPACITY = "original_capacity";

  public static final String CONF_CAPACITY = "conf_capacity";
  
  public static final String ROOM_TYPE = "room_type";

  private static final Logger LOGGER = LogManager.getLogger(WhiteBoardMapper.class);

   public static WhiteBoardViewDTO toModel(WhiteBoardViewEntity entity) {
		WhiteBoardViewDTO model = null;

		if (entity != null) {
			model = new WhiteBoardViewDTO();
			model.setAppointmentId(entity.getAppointmentId());
			//We check it later and overwrite it, if it has rules
			model.setInsurance(entity.getInsurance());
			model.setComment(entity.getComment());
			model.setCompanyId(entity.getCompanyId());
			model.setPhysician(stringToList(entity.getPhysician()));
			model.setEntreeDate(entity.getEntreeDate());
			model.setExpireDate(entity.getExpireDate());
			model.setEditedExpireDate(entity.getDischargeDate());
			model.setFirstname(entity.getFirstname());
			model.setLastname(entity.getLastname());
			model.setHl7Code(entity.getHl7Code());
			model.setId(entity.getId());
			model.setDiet(stringToList(entity.getDiet()));
			model.setIsolationType(entity.getIsolationType());
			model.setNurseName(entity.getNurseName());
			model.setOperationDate(entity.getOperationDate());
			model.setEditedOperationDate(entity.getEditedOperationDate());
			model.setPatientId(entity.getPatientId());
			model.setReason(stringToList(entity.getReason()));
			model.setEditedReason(stringToList(entity.getEditedReason()));
			model.setRoom(entity.getRoom());
			model.setServiceId(entity.getServiceId());
			model.setSex(entity.getSex());
			model.setVisitNumber(entity.getVisitNumber());
			model.setPatientBed(entity.getPatientBed());
			model.setEditedPhysician(stringToList(entity.getEditedPhysician()));
			model.setBirthDate(entity.getBirthDate());
			model.setPatientClass(entity.getPatientClass());
			model.setOriginalCapacity(entity.getOriginalCapacity());
			model.setConfCapacity(entity.getConfCapacity());
			model.setRoomType(entity.getRoomType());
		}
		return model;
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
      LOGGER.warn("Error while converting to a list: The Data => " + s
          + " is not migrated or is not a valid JSONArray");
      return null;
    }
    return list;
  }

  private static String listToString(List<String> list) {
    if (CollectionUtils.isEmpty(list)) {
      return null;
    }
    StringBuilder s = new StringBuilder();
    for (String listString : list) {
      s.append(",\"").append(listString).append("\"");
    }
    return "["+s.substring(1).concat("]");
  }

  public static WhiteBoardViewEntity toEntity(WhiteBoardViewDTO model) {
	WhiteBoardViewEntity entity = null;
	if(model != null) {
		entity = new WhiteBoardViewEntity();
		entity.setAppointmentId(model.getAppointmentId());
	    entity.setInsurance(model.getInsurance());
	    entity.setComment(model.getComment());
	    entity.setCompanyId(model.getCompanyId());
	    entity.setPhysician(listToString(model.getPhysician()));
	    entity.setEntreeDate(model.getEntreeDate());
	    entity.setExpireDate(model.getExpireDate());
	    entity.setDischargeDate(model.getEditedExpireDate());
	    entity.setFirstname(model.getFirstname());
	    entity.setLastname(model.getLastname());
	    entity.setHl7Code(model.getHl7Code());
	    entity.setId(model.getId());
	    entity.setDiet(listToString(model.getDiet()));
	    entity.setIsolationType(model.getIsolationType());
	    entity.setNurseName(model.getNurseName());
	    entity.setOperationDate(model.getOperationDate());
	    entity.setVisitNumber(model.getVisitNumber());
	    entity.setEditedPhysician(listToString(model.getEditedPhysician()));
	    entity.setBirthDate(model.getBirthDate());
	    entity.setPatientClass(model.getPatientClass());
	    entity.setCapacity(model.getCapacity());
	    entity.setOriginalCapacity(model.getOriginalCapacity());
	    entity.setConfCapacity(model.getConfCapacity());
	    entity.setRoomType(model.getRoomType());
	    entity.setEditedReason(listToString(model.getEditedReason()));
	    entity.setEditedOperationDate(model.getEditedOperationDate());
	    entity.setReason(listToString(model.getReason()));
	    entity.setPatientId(model.getPatientId());
        entity.setRoom(model.getRoom());
        entity.setSex(model.getSex());

    }
	return entity;
  }

  public static List<WhiteBoardViewDTO> toModel(List<WhiteBoardViewEntity> entities) {
    List<WhiteBoardViewDTO> models = new ArrayList<>();
    for (WhiteBoardViewEntity entity : entities) {
      models.add(toModel(entity));
    }
    return models;
  }

  public static WhiteBoardEntryDTO toEntryModel(WhiteBoardViewEntity entity) {
    WhiteBoardEntryDTO model = null;
    if (entity != null) {
      model = new WhiteBoardEntryDTO();
      model.setComment(entity.getComment());
      model.setId(entity.getId());
      model.setDiet(stringToList(entity.getDiet()));
      model.setIsolationType(entity.getIsolationType());
      model.setNurseName(entity.getNurseName());
      model.setOperationDate(entity.getOperationDate());
      model.setReason(stringToList(entity.getEditedReason()));
      model.setVisitNumber(entity.getVisitNumber());
      model.setPhysician(stringToList(entity.getEditedPhysician()));
      model.setDischargeDate(entity.getDischargeDate());
    }
	if( entity != null) {
		model = new WhiteBoardEntryDTO();
	    model.setComment(entity.getComment());
	    model.setId(entity.getId());
	    model.setDiet(stringToList(entity.getDiet()));
	    model.setIsolationType(entity.getIsolationType());
	    model.setNurseName(entity.getNurseName());
	    model.setOperationDate(entity.getOperationDate());
	    model.setVisitNumber(entity.getVisitNumber());
	    model.setPhysician(stringToList(entity.getEditedPhysician()));
	    model.setDischargeDate(entity.getDischargeDate());
	    model.setReason(stringToList(entity.getEditedReason()));
	}
    return model;
  }

  public static List<WhiteBoardEntryDTO> toEntryModel(List<WhiteBoardViewEntity> entities) {
    List<WhiteBoardEntryDTO> models = new ArrayList<>();
    for (WhiteBoardViewEntity entity : entities) {
      models.add(toEntryModel(entity));
    }
    return models;
  }

  public static final RowMapper<WhiteBoardViewEntity> ROW_MAPPER = (resultSet, rowNumber) -> {
    if (resultSet == null) {
      LOGGER.info("ResultSet is null");
      return null;
    }

    WhiteBoardViewEntity whiteBoard = new WhiteBoardViewEntity();
    // LONG
    whiteBoard.setId(SqlUtils.getLong(true, resultSet, ID));
    whiteBoard.setCompanyId(SqlUtils.getLong(true, resultSet, COMPANY_ID));
    whiteBoard.setVisitNumber(SqlUtils.getLong(true, resultSet, VISIT_NUMBER));
    whiteBoard.setPatientId(SqlUtils.getLong(true, resultSet, PATIENT_ID));
    whiteBoard.setAppointmentId(SqlUtils.getLong(true, resultSet, APPOINTMENT_ID));
    // INTEGER
    whiteBoard.setOriginalCapacity(SqlUtils.getInt(true, resultSet, ORIGINAL_CAPACITY));
    whiteBoard.setConfCapacity(SqlUtils.getInt(true, resultSet, CONF_CAPACITY));
    // STRING
    whiteBoard.setServiceId(SqlUtils.getString(true, resultSet, SERVICE_ID));
    whiteBoard.setInsurance(SqlUtils.getString(true, resultSet, ASSURANCE));
    whiteBoard.setComment(SqlUtils.getString(true, resultSet, COMMENT));
    whiteBoard.setPhysician(SqlUtils.getString(true, resultSet, DOCTOR));
    whiteBoard.setNurseName(SqlUtils.getString(true, resultSet, NURSE));
    whiteBoard.setFirstname(SqlUtils.getString(true, resultSet, FIRSTNAME));
    whiteBoard.setLastname(SqlUtils.getString(true, resultSet, LASTNAME));
    whiteBoard.setReason(SqlUtils.getString(true, resultSet, REASON));
    whiteBoard.setEditedReason(SqlUtils.getString(true, resultSet, EDITED_REASON));
    whiteBoard.setRoom(SqlUtils.getString(true, resultSet, ROOM));
    whiteBoard.setSex(SqlUtils.getString(true, resultSet, SEX));
    whiteBoard.setHl7Code(SqlUtils.getString(true, resultSet, HL7CODE));
    whiteBoard.setPatientBed(SqlUtils.getString(true, resultSet, PATIENT_BED));
    whiteBoard.setPatientClass(SqlUtils.getString(true, resultSet, PATIENT_CLASS));
    whiteBoard.setIsolationType(SqlUtils.getString(true, resultSet, ISOLATION_TYPE));
    whiteBoard.setDiet(SqlUtils.getString(true, resultSet, DIET));
    whiteBoard.setRoomType(SqlUtils.getString(true, resultSet, ROOM_TYPE));
    // DATE
    whiteBoard.setEntreeDate(SqlUtils.getDate(true, resultSet, ENTREE_DATE));
    whiteBoard.setExpireDate(SqlUtils.getDate(true, resultSet, EXPIRE_DATE));
    whiteBoard.setOperationDate(SqlUtils.getDate(true, resultSet, OPERATION_DATE));
    whiteBoard.setDischargeDate(SqlUtils.getDate(true, resultSet, EDITED_EXPIRED_DATE));
    whiteBoard.setEditedOperationDate(SqlUtils.getDate(true, resultSet, EDITED_OPERATION_DATE_VIEW));
    whiteBoard.setBirthDate(SqlUtils.getDate(true, resultSet, BIRTHDATE));
    // BOOLEAN
    whiteBoard.setEditedPhysician(SqlUtils.getString(true, resultSet, EDITED_PHYSICIAN));

    return whiteBoard;
  };

  public static final RowUnmapper<WhiteBoardViewEntity> ROW_UNMAPPER = whiteBoard -> {
    Map<String, Object> mapping = new LinkedHashMap<>();
    mapping.put(ID, whiteBoard.getId());
    mapping.put(ASSURANCE, whiteBoard.getInsurance());
    mapping.put(COMMENT, whiteBoard.getComment());
    mapping.put(DOCTOR, whiteBoard.getPhysician());
    mapping.put(ENTREE_DATE, whiteBoard.getEntreeDate());
    mapping.put(EXPIRE_DATE, whiteBoard.getExpireDate());
    mapping.put(DIET, whiteBoard.getDiet());
    mapping.put(NURSE, whiteBoard.getNurseName());
    mapping.put(FIRSTNAME, whiteBoard.getFirstname());
    mapping.put(LASTNAME, whiteBoard.getLastname());
    mapping.put(REASON, whiteBoard.getReason());
    mapping.put(EDITED_REASON, whiteBoard.getEditedReason());
    mapping.put(ROOM, whiteBoard.getRoom());
    mapping.put(SEX, whiteBoard.getSex());
    mapping.put(ISOLATION_TYPE, whiteBoard.getIsolationType());
    mapping.put(COMPANY_ID, whiteBoard.getCompanyId());
    mapping.put(VISIT_NUMBER, whiteBoard.getVisitNumber());
    mapping.put(SERVICE_ID, whiteBoard.getServiceId());
    mapping.put(HL7CODE, whiteBoard.getHl7Code());
    mapping.put(APPOINTMENT_ID, whiteBoard.getAppointmentId());
    mapping.put(PATIENT_ID, whiteBoard.getPatientId());
    mapping.put(PATIENT_BED, whiteBoard.getPatientBed());
    mapping.put(PATIENT_CLASS, whiteBoard.getPatientClass());
    mapping.put(EDITED_OPERATION_DATE, whiteBoard.getOperationDate());
    mapping.put(EDITED_PHYSICIAN, whiteBoard.getEditedPhysician());
    mapping.put(EDITED_EXPIRED_DATE, whiteBoard.getDischargeDate());
    mapping.put(EDITED_OPERATION_DATE_VIEW, whiteBoard.getEditedOperationDate());
    mapping.put(BIRTHDATE, whiteBoard.getBirthDate());
    mapping.put(ORIGINAL_CAPACITY, whiteBoard.getOriginalCapacity());
    mapping.put(CONF_CAPACITY, whiteBoard.getConfCapacity());
    mapping.put(ROOM_TYPE, whiteBoard.getRoomType());
    return mapping;
  };

  public static final RowMapper<String> NURSE_MAPPER =
      (resultSet, rowNumber) -> SqlUtils.getString(true, resultSet, NURSE);

  public static final RowMapper<String> PHYSICIAN_MAPPER =
      (resultSet, rowNumber) -> SqlUtils.getString(true, resultSet, DOCTOR);

  public static final RowMapper<Boolean> COUNT_MAPPER =
      (resultSet, rowNumber) -> SqlUtils.getBoolean(true, resultSet, "c");

}
