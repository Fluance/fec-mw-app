package net.fluance.cockpit.core.model.jpa.surgeryboard;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class SurgeryboardPK implements Serializable {

	private static final long serialVersionUID = -5666012919543326121L;

	@Column(name="company_id")
	private Integer companyId;
	
	@Temporal(TemporalType.DATE)
	@Column(name="notedate")
	private Date noteDate;

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Date getNoteDate() {
		return noteDate;
	}

	public void setNoteDate(Date noteDate) {
		this.noteDate = noteDate;
	}
	
}
