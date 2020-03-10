package net.fluance.cockpit.core.model.jpa.visit;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

import net.fluance.cockpit.core.model.jpa.company.CompanyEntity;

@Entity(name="visit_detailed")
@Table(name="BMV_PATIENTS_LIST")
public class VisitDetailedEntity {
	
	@Id
    @Column(name ="nb")
    private Long visitNumber;
	
	@Column(name ="patient_id")
	private Long patientId;
	
	@Column(name ="firstname")
    private String firstName;
    
    @Column(name ="lastname")
    private String lastName;
    
    @Column(name ="maidenname")
    private String maidenName;
    
    @Column(name ="birthdate")
    private Date birthdate;
    
    @Column(name ="sex")
    private String sex;
    
    @Column(name ="address")
    private String address;
    
    @Column(name ="postcode")
    private String postCode;
    
    @Column(name ="locality")
    private String locality;
    
    @Column(name ="death")
    private Boolean death;
    
    @Column(name ="deathdt")
    private Timestamp deathDate;
    
    @Column(name ="admitdt")
    private Timestamp admitDate;
    
    @Column(name ="dischargedt")
    private Timestamp dischargeDate;
    
    @Column(name ="patientclass")
    private String patientClass;
    
    @Column(name ="patienttype")
    private String patietType;
    
    @Column(name ="patientcase")
    private String patientCase;
    
    @Column(name ="hospservice")
    private String hospService;
    
    @Column(name ="admissiontype")
    private String addmissionType;
    
    @Column(name ="financialclass")
    private String financialClass;
    
    @Column(name ="patientunit")
    private String patientUnit;
    
    @Column(name ="patientroom")
    private String patientRoom;
    
    @Column(name ="patientbed")
    private String patientBed;
    
    @Column(name ="company_id")
    private Integer companyId;
    
    @Column(name ="patientclassdesc")
    private String patientClassDes;
    
    @Column(name ="patienttypedesc")
    private String patientTypeDesc;
    
    @Column(name ="patientcasedesc")
    private String patientCaseDes;
    
    @Column(name ="hospservicedesc")
    private String hostpServiceDesc;
    
    @Column(name ="admissiontypedesc")  
    private String admissionTypeDesc;
    
    @Column(name ="financialclassdesc")
    private String financialClassDesc;
    
    @Column(name ="patientunitdesc")
    private String patientUnitDesc;
    
    @Column(name ="hl7code")
    private String hl7Code;
    
    @Formula(value = " concat(patientroom, '.', patientBed) ")
	private String fullBed;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "company_id",
        referencedColumnName = "id",
        insertable = false,
        updatable = false
    )    
    private CompanyEntity company;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "nb",
        referencedColumnName = "nb",
        insertable = false,
        updatable = false
    )
    private VisitDetailEntity visitDetail;    
    	
	public Long getVisitNumber() {
		return visitNumber;
	}

	
	public void setVisitNumber(Long visitNumber) {
		this.visitNumber = visitNumber;
	}

	
	public Long getPatientId() {
		return patientId;
	}

	
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
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

	
	public String getMaidenName() {
		return maidenName;
	}

	
	public void setMaidenName(String maidenName) {
		this.maidenName = maidenName;
	}

	
	public Date getBirthdate() {
		return birthdate;
	}

	
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	
	public String getSex() {
		return sex;
	}

	
	public void setSex(String sex) {
		this.sex = sex;
	}

	
	public String getAddress() {
		return address;
	}

	
	public void setAddress(String address) {
		this.address = address;
	}

	
	public String getPostCode() {
		return postCode;
	}

	
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	
	public String getLocality() {
		return locality;
	}

	
	public void setLocality(String locality) {
		this.locality = locality;
	}

	
	public Boolean isDeath() {
		return death;
	}

	
	public void setDeath(Boolean death) {
		this.death = death;
	}

	
	public Timestamp getDeathDate() {
		return deathDate;
	}

	
	public void setDeathDate(Timestamp deathDate) {
		this.deathDate = deathDate;
	}

	
	public Timestamp getAdmitDate() {
		return admitDate;
	}

	
	public void setAdmitDate(Timestamp admitDate) {
		this.admitDate = admitDate;
	}

	
	public Timestamp getDischargeDate() {
		return dischargeDate;
	}

	
	public void setDischargeDate(Timestamp dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	
	public String getPatientClass() {
		return patientClass;
	}

	
	public void setPatientClass(String patientClass) {
		this.patientClass = patientClass;
	}

	
	public String getPatietType() {
		return patietType;
	}

	
	public void setPatietType(String patietType) {
		this.patietType = patietType;
	}

	
	public String getPatientCase() {
		return patientCase;
	}

	
	public void setPatientCase(String patientCase) {
		this.patientCase = patientCase;
	}

	
	public String getHospService() {
		return hospService;
	}

	
	public void setHospService(String hospService) {
		this.hospService = hospService;
	}

	
	public String getAddmissionType() {
		return addmissionType;
	}

	
	public void setAddmissionType(String addmissionType) {
		this.addmissionType = addmissionType;
	}

	
	public String getFinancialClass() {
		return financialClass;
	}

	
	public void setFinancialClass(String financialClass) {
		this.financialClass = financialClass;
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

	
	public Integer getCompanyId() {
		return companyId;
	}

	
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	
	public String getPatientClassDes() {
		return patientClassDes;
	}

	
	public void setPatientClassDes(String patientClassDes) {
		this.patientClassDes = patientClassDes;
	}

	
	public String getPatientTypeDesc() {
		return patientTypeDesc;
	}

	
	public void setPatientTypeDesc(String patientTypeDesc) {
		this.patientTypeDesc = patientTypeDesc;
	}

	
	public String getPatientCaseDes() {
		return patientCaseDes;
	}

	
	public void setPatientCaseDes(String patientCaseDes) {
		this.patientCaseDes = patientCaseDes;
	}

	
	public String getHostpServiceDesc() {
		return hostpServiceDesc;
	}

	
	public void setHostpServiceDesc(String hostpServiceDesc) {
		this.hostpServiceDesc = hostpServiceDesc;
	}

	
	public String getAdmissionTypeDesc() {
		return admissionTypeDesc;
	}

	
	public void setAdmissionTypeDesc(String admissionTypeDesc) {
		this.admissionTypeDesc = admissionTypeDesc;
	}

	
	public String getFinancialClassDesc() {
		return financialClassDesc;
	}

	
	public void setFinancialClassDesc(String financialClassDesc) {
		this.financialClassDesc = financialClassDesc;
	}

	
	public String getPatientUnitDesc() {
		return patientUnitDesc;
	}

	
	public void setPatientUnitDesc(String patientUnitDesc) {
		this.patientUnitDesc = patientUnitDesc;
	}

	
	public String getHl7Code() {
		return hl7Code;
	}

	
	public void setHl7Code(String hl7Code) {
		this.hl7Code = hl7Code;
	}

	
	public String getFullBed() {
		return fullBed;
	}

	
	public void setFullBed(String fullBed) {
		this.fullBed = fullBed;
	}

	
	public CompanyEntity getCompany() {
		return company;
	}

	
	public void setCompany(CompanyEntity company) {
		this.company = company;
	}


	public VisitDetailEntity getVisitDetail() {
		return visitDetail;
	}

	
	public void setVisitDetail(VisitDetailEntity visitDetail) {
		this.visitDetail = visitDetail;
	}
}
