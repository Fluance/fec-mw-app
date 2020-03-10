package net.fluance.cockpit.core.model.jdbc.visit;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateDeserializer;
import net.fluance.commons.json.JsonDateTimeSerializer;
import net.fluance.cockpit.core.model.jdbc.intervention.Diagnosis;
import net.fluance.cockpit.core.model.jdbc.intervention.Operation;

@SuppressWarnings("serial")
public class VisitPatientList implements Persistable<Integer> {

	private long visitNb;
	private long companyId;
	private String firstName;
	private String lastName;
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date birthDate;
	private String sex;
	private String patientUnit;
	private String patientRoom;
	private String patientBed;
	private String physicianPrefix;
	private String physicianFirstName;
	private String physicianLastName;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date admissionDate;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date dischargeDate;
	private String financialClass;
	private String food;
	private String mobility;
	private String postOperationDays;
	private List<Diagnosis> diagnosis;
	private List<Operation> operations;
	private boolean preadmitted;
	private String operationsOrDiagnosis;
	
	public VisitPatientList(long visitNb, long companyId, String firstName, String lastName, Date birthDate, String sex, String patientUnit, String patientRoom, String patientBed, String physicianPrefix, String physicianFirstName,
			String physicianLastName, Date admissionDate, Date dischargeDate, String financialClass, String food, String mobility, String postOperationDays, List<Diagnosis> diagnosis, List<Operation> operations) {
		super();
		this.visitNb = visitNb;
		this.companyId = companyId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.sex = sex;
		this.patientUnit = patientUnit;
		this.patientRoom = patientRoom;
		this.patientBed = patientBed;
		this.physicianPrefix = physicianPrefix;
		this.physicianFirstName = physicianFirstName;
		this.physicianLastName = physicianLastName;
		this.admissionDate = admissionDate;
		this.dischargeDate = dischargeDate;
		this.financialClass = financialClass;
		this.food = food;
		this.mobility = mobility;
		this.postOperationDays = postOperationDays;
		this.diagnosis = diagnosis;
		this.operations = operations;
		if(diagnosis != null && diagnosis.size()>0){
			this.operationsOrDiagnosis = diagnosis.get(0).getDescription();
		} else if (operations!=null && operations.size() > 0){
			this.operationsOrDiagnosis = operations.get(0).getDescription();
		} else {
			operationsOrDiagnosis = "";
		}
	}


	public long getVisitNb() {
		return visitNb;
	}

	
	public void setVisitNb(long visitNb) {
		this.visitNb = visitNb;
	}

	
	public long getCompanyId() {
		return companyId;
	}

	
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	
	public String getFirstName() {
		return firstName;
	}

	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	
	public String getLastName() {
		return lastName;
	}

	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
	public Date getBirthDate() {
		return birthDate;
	}

	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	
	public String getSex() {
		return sex;
	}

	
	public void setSex(String sex) {
		this.sex = sex;
	}

	
	public String getPatientUnit() {
		return patientUnit;
	}

	
	public void setPatientUnit(String patientUnit) {
		this.patientUnit = patientUnit;
	}

	
	public String getPatientRoom() {
		return patientRoom;
	}

	
	public void setPatientRoom(String patientRoom) {
		this.patientRoom = patientRoom;
	}

	
	public String getPatientBed() {
		return patientBed;
	}

	
	public void setPatientBed(String patientBed) {
		this.patientBed = patientBed;
	}

	
	public String getPhysicianPrefix() {
		return physicianPrefix;
	}

	
	public void setPhysicianPrefix(String physicianPrefix) {
		this.physicianPrefix = physicianPrefix;
	}

	
	public String getPhysicianFirstName() {
		return physicianFirstName;
	}

	
	public void setPhysicianFirstName(String physicianFirstName) {
		this.physicianFirstName = physicianFirstName;
	}

	
	public String getPhysicianLastName() {
		return physicianLastName;
	}

	
	public void setPhysicianLastName(String physicianLastName) {
		this.physicianLastName = physicianLastName;
	}

	
	public Date getAdmissionDate() {
		return admissionDate;
	}


	
	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}


	public Date getDischargeDate() {
		return dischargeDate;
	}

	
	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	
	public String getFinancialClass() {
		return financialClass;
	}

	
	public void setFinancialClass(String financialClass) {
		this.financialClass = financialClass;
	}

	
	public String getFood() {
		return food;
	}

	
	public void setFood(String food) {
		this.food = food;
	}

	
	public String getMobility() {
		return mobility;
	}

	
	public void setMobility(String mobility) {
		this.mobility = mobility;
	}

	
	public String getPostOperationDays() {
		return postOperationDays;
	}

	
	public void setPostOperationDays(String postOperationDays) {
		this.postOperationDays = postOperationDays;
	}

	
	public List<Diagnosis> getDiagnosis() {
		return diagnosis;
	}

	
	public void setDiagnosis(List<Diagnosis> diagnosis) {
		this.diagnosis = diagnosis;
	}

	
	public List<Operation> getOperations() {
		return operations;
	}

	
	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	@JsonIgnore
	public boolean isPreadmitted() {
		return preadmitted;
	}
	
	public void setPreadmitted(boolean isPreadmitted) {
		this.preadmitted = isPreadmitted;
	}
	
	@JsonIgnore
	public String getOperationsOrDiagnosis() {
		return operationsOrDiagnosis;
	}

	@JsonIgnore
	@Override
	public Integer getId() {
		return null;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}
}
