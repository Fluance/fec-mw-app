package net.fluance.cockpit.core.model.jdbc.whiteboard;

import java.util.List;

public class PatientRule {
  private PATIENT_FIELD field;
  private List<String> value;
  
  public PATIENT_FIELD getField() {
    return field;
  }
  public void setField(PATIENT_FIELD field) {
    this.field = field;
  }
  public List<String> getValue() {
    return value;
  }
  public void setValue(List<String> value) {
    this.value = value;
  }
  
}
