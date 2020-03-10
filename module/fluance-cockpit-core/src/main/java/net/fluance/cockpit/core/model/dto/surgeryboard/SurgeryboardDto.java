package net.fluance.cockpit.core.model.dto.surgeryboard;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SurgeryboardDto {

	private Integer companyId;
	private Date noteDate;
	private String note;
	
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
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	//Mapping the PK for locking table
	public Long getLockId() {
		Long pk = null;
		if(this.companyId != null && this.noteDate != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
	        String formattedDate = formatter.format(this.noteDate);
			pk = Long.valueOf(companyId + formattedDate);
		}
		return pk;
	}
	
}
