package net.fluance.cockpit.core.test.repository.jdbc.whiteboard;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import net.fluance.cockpit.core.model.jdbc.whiteboard.PatientListRulesMapper;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewDTO;

public class PatientListRulesMapperTest {
  private String json ="{\"patientListRules\":[{\"rules\":[{\"comparisonColumn\": \"fooColumn\", \"values\": [\"foo1\", \"foo2\"]}], \"result\": \"fooresult\"},{\"rules\":[{\"comparisonColumn\": \"insurance\", \"values\": [\"P1\", \"P2\", \"P3\", \"P4\", \"P5\", \"P6\"]}], \"result\": \"ACC\"},{\"rules\":[{\"comparisonColumn\": \"insurance\", \"values\": [\"S\", \"SU\"]}], \"result\": \"VIP\"},{\"rules\":[{\"comparisonColumn\": \"insurance\", \"values\": [\"P\", \"DP\", \"C\"]}, {\"comparisonColumn\": \"patientclass\", \"values\": [\"HVIP\"]}], \"result\": \"VIP\"}]}";
  private PatientListRulesMapper patientListRulesMapper;
  @Before
  public void initalise() throws JsonParseException, JsonMappingException, IOException {
   
   Map<Long,String> map = new HashMap<>();
   map.put(5L, json);
   patientListRulesMapper = new PatientListRulesMapper(map,5L);
  }
  
  @Test
  public void hasRule_1() {
    assertTrue("Has rules?", patientListRulesMapper.hasRules());
  }
  
  @Test
  public void hasRule_2() {
    Map<Long,String> map = new HashMap<>();
    map.put(5L, json);
    patientListRulesMapper = new PatientListRulesMapper(map,6L);
    assertTrue("Has no rules?", !patientListRulesMapper.hasRules());
  }
  
  @Test
  public void fullTest_1() {
    WhiteBoardViewDTO whiteboard1 = new WhiteBoardViewDTO();
    whiteboard1.setInsurance("test");
    whiteboard1.setPatientClass("any");
    patientListRulesMapper.updateInsuranceByRules(whiteboard1);
    assertTrue("Is not replaced?", whiteboard1.getInsurance().equals("test"));
    assertTrue("Patient Class should remain", whiteboard1.getPatientClass().equals("any"));
  }
  
  @Test
  public void fullTest_2() {
    WhiteBoardViewDTO whiteboard1 = new WhiteBoardViewDTO();
    whiteboard1.setInsurance("P1");
    whiteboard1.setPatientClass("any");
    patientListRulesMapper.updateInsuranceByRules(whiteboard1);
    assertTrue("Is replaced?", whiteboard1.getInsurance().equals("ACC"));
    assertTrue("Patient Class should remain", whiteboard1.getPatientClass().equals("any"));
  }
  
  @Test
  public void fullTest_3() {
    WhiteBoardViewDTO whiteboard1 = new WhiteBoardViewDTO();
    whiteboard1.setInsurance("test");
    whiteboard1.setPatientClass("HVIP");
    patientListRulesMapper.updateInsuranceByRules(whiteboard1);
    assertTrue("Is not replaced?", whiteboard1.getInsurance().equals("test"));
    assertTrue("Patient Class should remain", whiteboard1.getPatientClass().equals("HVIP"));
  }
  
  @Test
  public void fullTest_4() {
    WhiteBoardViewDTO whiteboard1 = new WhiteBoardViewDTO();
    whiteboard1.setInsurance("P");
    whiteboard1.setPatientClass("any");
    patientListRulesMapper.updateInsuranceByRules(whiteboard1);
    assertTrue("Is not replaced?", whiteboard1.getInsurance().equals("P"));
    assertTrue("Patient Class should remain", whiteboard1.getPatientClass().equals("any"));
  }
  
  @Test
  public void fullTest_5() {
    WhiteBoardViewDTO whiteboard1 = new WhiteBoardViewDTO();
    whiteboard1.setInsurance("P");
    whiteboard1.setPatientClass("HVIP");
    patientListRulesMapper.updateInsuranceByRules(whiteboard1);
    assertTrue("Is replaced?", whiteboard1.getInsurance().equals("VIP"));
    assertTrue("Patient Class should remain", whiteboard1.getPatientClass().equals("HVIP"));
  }
  
  @Test
  public void fullTest_6() {
    WhiteBoardViewDTO whiteboard1 = new WhiteBoardViewDTO();
    whiteboard1.setInsurance("SU");
    whiteboard1.setPatientClass("any");
    patientListRulesMapper.updateInsuranceByRules(whiteboard1);
    assertTrue("Is replaced?", whiteboard1.getInsurance().equals("VIP"));
    assertTrue("Patient Class should remain", whiteboard1.getPatientClass().equals("any"));
  }
  
  @Test
  public void fullTest_7() {
    WhiteBoardViewDTO whiteboard1 = new WhiteBoardViewDTO();
    whiteboard1.setInsurance("P5");
    whiteboard1.setPatientClass("HVIP");
    patientListRulesMapper.updateInsuranceByRules(whiteboard1);
    assertTrue("Is not replaced?", whiteboard1.getInsurance().equals("ACC"));
    assertTrue("Patient Class should remain", whiteboard1.getPatientClass().equals("HVIP"));
  }
  
  @Test
  public void fullTest_company_no_rule_1() {
    Map<Long,String> map = new HashMap<>();
    map.put(5L, json);
    
    patientListRulesMapper = new PatientListRulesMapper(map,6L);
	
    WhiteBoardViewDTO whiteboard1 = new WhiteBoardViewDTO();
    whiteboard1.setInsurance("test");
    whiteboard1.setPatientClass("any");
    patientListRulesMapper.updateInsuranceByRules(whiteboard1);
    assertTrue("Is not replaced?", whiteboard1.getInsurance().equals("test"));
    assertTrue("Patient Class should remain", whiteboard1.getPatientClass().equals("any"));
  }
  
}
