package net.fluance.cockpit.core.util.whiteboard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import net.fluance.cockpit.core.model.PatientListRule;
//import net.fluance.cockpit.core.model.Rule;
import net.fluance.cockpit.core.model.jdbc.whiteboard.WhiteBoardMapper;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewDTO;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewEntity;


public class WhiteBoardMock {
	
	private WhiteBoardMock() {}
	
	public static WhiteBoardViewEntity getViewMock() {
		WhiteBoardViewEntity whiteBoard = new WhiteBoardViewEntity();
		whiteBoard.setId(1L);
		whiteBoard.setInsurance("Insurance");
		whiteBoard.setComment("Just a comment");
		whiteBoard.setPhysician("[Mr Dr Heinz]");
		whiteBoard.setEntreeDate(new Date());
		whiteBoard.setExpireDate(new Date());
		whiteBoard.setDiet("[diet]");
		whiteBoard.setNurseName("Stephanie");
		whiteBoard.setOperationDate(new Date());
		whiteBoard.setFirstname("Mr Patinent");
		whiteBoard.setLastname("Impatient");
		whiteBoard.setReason("A reason to life...");
		whiteBoard.setRoom("404");
		whiteBoard.setAppointmentId(323L);
		whiteBoard.setHl7Code("fafa");
		whiteBoard.setOperationDate(new Date());
		whiteBoard.setIsolationType("A");
		whiteBoard.setPatientId(232L);
		whiteBoard.setServiceId("GOD1");
		whiteBoard.setSex("Monsieur");
		whiteBoard.setEditedReason("A reason to life...");
		whiteBoard.setEditedPhysician("[Mr Dr Heinz]");
		return whiteBoard;
	}
	
	public static WhiteBoardViewDTO getDTOMock() {
		return WhiteBoardMapper.toModel(getViewMock());
	}

//	public static List<PatientListRule> getMockRules() {
//		List<PatientListRule> patientListRules = new ArrayList<>();
//		PatientListRule patientListRule = new PatientListRule();
//		patientListRule.setResult("ACC");
//		Rule rule = new Rule();
//		rule.setComparisonColumn("insurance");
//		List<String> values = new ArrayList<>(); 
//		values.add("P1");
//		values.add("P2");
//		values.add("P3");
//		values.add("P4");
//		values.add("P5");
//		values.add("P6");
//		rule.setValues(values);
//		List<Rule> rules = new ArrayList<>();
//		rules.add(rule);
//		patientListRule.setRules(rules);
//		patientListRules.add(patientListRule);
//		return patientListRules;
//	}

	public static List<WhiteBoardViewEntity> getMocks(int howMany) {
		List<WhiteBoardViewEntity> whiteboards = new ArrayList<>();
		int i = 0;
		WhiteBoardViewEntity whiteBoardEntry;
		while (i < howMany) {
			whiteBoardEntry = getViewMock();
			whiteBoardEntry.setId(new Long(i));
			whiteboards.add(whiteBoardEntry);
			i++;
		}
		return whiteboards;
	}

}
