package net.fluance.cockpit.core.model.jpa.appointment;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.domain.Persistable;

@Entity(name="ProcessStatus")
@Table(name = "process_status", schema = "ehealth")
public class ProcessStatus implements Persistable<ProcessStatusPK>, Serializable {	
	private static final long serialVersionUID = 6376583849388383756L;

	@EmbeddedId
	private ProcessStatusPK id;
	
	@Column(name = "step")
	private Integer step;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "code")
	private String processStatusCode;

	public ProcessStatusPK getId() {
		return id;
	}
	
	public void setId(ProcessStatusPK id) {
		this.id = id;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProcessStatusCode() {
		return processStatusCode;
	}

	public void setProcessStatusCode(String processStatusCode) {
		this.processStatusCode = processStatusCode;
	}

	@Override
	public boolean isNew() {
		return false;
	}
}
