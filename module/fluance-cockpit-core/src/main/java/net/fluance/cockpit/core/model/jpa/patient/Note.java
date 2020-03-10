package net.fluance.cockpit.core.model.jpa.patient;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="PatientNote")
@Table(name = "note", schema = "openwt")
public class Note {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="patient_id")
	private Long patientId;
	
	@Column(name="source")
	private String source;
	
	@Column(name="editor")
	private String editor;
	
	@Column(name="creator")
	private String creator;
	
	@Column(name="title")
	private String title;
	
	@Column(name="content")
	private String content;
	
	@Column(name="referencedt")
	private Date referenceDate;
	
	@Column(name="editeddt")
	private Date editedDate;
	
	@Column(name="shifted")
	private Boolean shifted;

	
	public Long getId() {
		return id;
	}

	
	public void setId(Long id) {
		this.id = id;
	}

	
	public Long getPatientId() {
		return patientId;
	}

	
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	
	public String getSource() {
		return source;
	}

	
	public void setSource(String source) {
		this.source = source;
	}

	
	public String getEditor() {
		return editor;
	}

	
	public void setEditor(String editor) {
		this.editor = editor;
	}

	
	public String getCreator() {
		return creator;
	}

	
	public void setCreator(String creator) {
		this.creator = creator;
	}

	
	public String getTitle() {
		return title;
	}

	
	public void setTitle(String title) {
		this.title = title;
	}

	
	public String getContent() {
		return content;
	}

	
	public void setContent(String content) {
		this.content = content;
	}

	
	public Date getReferenceDate() {
		return referenceDate;
	}

	
	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}

	
	public Date getEditedDate() {
		return editedDate;
	}

	
	public void setEditedDate(Date editedDate) {
		this.editedDate = editedDate;
	}

	
	public Boolean getShifted() {
		return shifted;
	}

	
	public void setShifted(Boolean shifted) {
		this.shifted = shifted;
	}
	
	
}
