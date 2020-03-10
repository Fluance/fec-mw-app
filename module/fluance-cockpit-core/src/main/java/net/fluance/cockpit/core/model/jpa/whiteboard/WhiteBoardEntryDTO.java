package net.fluance.cockpit.core.model.jpa.whiteboard;

import java.util.Date;
import java.util.List;

public class WhiteBoardEntryDTO {
  private Long id;

  private Long visitNumber;

  private String nurseName;

  private List<String> diet;

  private Date operationDate;

  private Date dischargeDate;

  private String isolationType;

  private List<String> reason;

  private String comment;

  private List<String> physician;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getVisitNumber() {
    return visitNumber;
  }

  public void setVisitNumber(Long visitNumber) {
    this.visitNumber = visitNumber;
  }

  public String getNurseName() {
    return nurseName;
  }

  public void setNurseName(String nurseName) {
    this.nurseName = nurseName;
  }

  public Date getOperationDate() {
    return operationDate;
  }

  public void setOperationDate(Date operationDate) {
    this.operationDate = operationDate;
  }

  public String getIsolationType() {
    return isolationType;
  }

  public void setIsolationType(String isolationType) {
    this.isolationType = isolationType;
  }

  public List<String> getReason() {
    return reason;
  }

  public void setReason(List<String> reason) {
    this.reason = reason;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }


  public List<String> getPhysician() {
    return physician;
  }

  public void setPhysician(List<String> physician) {
    this.physician = physician;
  }

  public Date getDischargeDate() {
    return dischargeDate;
  }

  public void setDischargeDate(Date dischargeDate) {
    this.dischargeDate = dischargeDate;
  }

  public List<String> getDiet() {
    return diet;
  }

  public void setDiet(List<String> diet) {
    this.diet = diet;
  }
}
