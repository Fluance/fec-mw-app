package net.fluance.cockpit.core.model.jdbc.visit;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class VisitPolicyList implements Persistable<Integer> {

	@JsonProperty("nbRecords")
	private Integer nb_records;
	@JsonProperty("guarantorId")
	private Integer guarantor_id;
	private String name;
	private String code;
	private Integer priority;
	@JsonProperty("subPriority")
	private Integer subpriority;
	@JsonProperty("hospClass")
	private String hospclass;

	public VisitPolicyList(int nbRecords, Integer id, String name, String code, Integer priority, Integer subpriority, String hospclass) {
		this.guarantor_id = id;
		this.name = name;
		this.code = code;
		this.priority = priority;
		this.subpriority = subpriority;
		this.hospclass = hospclass;
		this.nb_records = nbRecords;
	}
	
	public VisitPolicyList(){}

	public int getNb_records() {
		return nb_records;
	}
	
	public Integer getGuarantor_id() {
		return guarantor_id;
	}

	public void setGuarantor_id(Integer guarantor_id) {
		this.guarantor_id = guarantor_id;
	}

	public void setNb_records(int nb_records) {
		this.nb_records = nb_records;
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
		this.guarantor_id = id;
	}

	@JsonIgnore
	@Override
	public Integer getId() {
		return guarantor_id;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}

}
