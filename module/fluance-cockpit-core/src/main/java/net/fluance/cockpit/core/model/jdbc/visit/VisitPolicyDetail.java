package net.fluance.cockpit.core.model.jdbc.visit;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.fluance.cockpit.core.model.PayloadDisplayLogName;

public class VisitPolicyDetail implements Persistable<String>, PayloadDisplayLogName {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String code;
	private Integer priority;
	@JsonProperty("subPriority")
	private Integer subpriority;
	@JsonProperty("hospClass")
	private String hospclass;
	private boolean inactive;
	@JsonProperty("policyNb")
	private String policynb;
	@JsonProperty("coverCardNb")
	private double covercardnb;
	@JsonProperty("accidentNb")
	private String accidentnb;
	private String accidentDate;

	public VisitPolicyDetail(Integer id, String name, String code, Integer priority, Integer subpriority,
			String hospclass, boolean inactive, String policynb, double covercardnb, String accidentnb, String accidentDate) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.priority = priority;
		this.subpriority = subpriority;
		this.hospclass = hospclass;
		this.inactive = inactive;
		this.policynb = policynb;
		this.covercardnb = covercardnb;
		this.accidentnb = accidentnb;
		this.accidentDate = accidentDate;
	}
	
	public VisitPolicyDetail(){}

	@JsonProperty("guarantorId")
	public Integer getGuarantor_id(){
		return id;
	}
	
	public boolean getInactive() {
		return inactive;
	}

	public void setInactive(boolean inactive) {
		this.inactive = inactive;
	}

	public String getPolicynb() {
		return policynb;
	}

	public void setPolicynb(String policynb) {
		this.policynb = policynb;
	}

	public double getCovercardnb() {
		return covercardnb;
	}

	public void setCovercardnb(double covercardnb) {
		this.covercardnb = covercardnb;
	}

	public String getAccidentnb() {
		return accidentnb;
	}

	public void setAccidentnb(String accidentnb) {
		this.accidentnb = accidentnb;
	}

	public String getAccidentDate() {
		return accidentDate;
	}

	public void setAccidentDate(String accidentDate) {
		this.accidentDate = accidentDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Integer getSubpriority() {
		return subpriority;
	}

	public void setSubpriority(Integer subpriority) {
		this.subpriority = subpriority;
	}

	public String getHospclass() {
		return hospclass;
	}

	public void setHospclass(String hospclass) {
		this.hospclass = hospclass;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	@JsonIgnore
	public String getId() {
		return id+"/"+priority+"/"+subpriority;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public String displayName() {
		return this.getPolicynb();
	}
	
	/**
	 * If priorities are the same, uses the subprority
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public static int compareByPriorityAndSubpriority(VisitPolicyDetail lhs, VisitPolicyDetail rhs) {
		
		if(lhs.priority == null){
			return 1;
		}
		
		if(rhs.priority == null){
			return -1;
		}
		
		if (lhs.priority.equals(rhs.priority)) {
			if(lhs.subpriority == null){
				return 1;
			}
			if(rhs.subpriority == null){
				return -1;
			}
			return lhs.subpriority.compareTo(rhs.subpriority);
		} else {
			return lhs.priority.compareTo(rhs.priority);
		}
	}
}
