package net.fluance.cockpit.core.model.jpa.note;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.fluance.cockpit.core.model.jdbc.note.NoteDetails;

import java.io.Serializable;
import java.util.Date;

@Entity(name = "Note") // Name of the entity
public class Note implements Serializable {

	@Id // signifies the primary key
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "noteIdSeqGenerator")
	@SequenceGenerator(name = "noteIdSeqGenerator", sequenceName = "notesandpictures.note_id_seq", allocationSize = 1)
	private Long id;
	@Column(name = "category_id", nullable = false)
	private Integer categoryId;
	@Column(name = "patient_id")
	private Long patientId;
	@Column(name = "visit_nb")
	private Long visitNb;
	private String editor;
	private String creator;
	@Column(name = "shifted")
	private boolean shift;

	private String content;
	private String title;
	private Date referencedt;
	private Date editeddt;
	private boolean deleted;

	public Note() {}

	public Note(net.fluance.cockpit.core.model.jdbc.note.NoteDetails note) {
		this.id = note.getId();
		this.categoryId = note.getCategory().getId();
		this.patientId = note.getPatient().getPid();
		this.content = note.getDescription();
		this.title = note.getTitle();
		this.referencedt = note.getReferenceDate();
		this.editeddt = note.getEditedDate();
		this.visitNb = note.getVisitNb();
		this.shift = note.isShift();
	}

	public Note(NoteDetails note, String editor) {
		this(note);
		this.setEditor(editor);
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Long getVisitNb() {
		return visitNb;
	}

	public void setVisitNb(Long visitNb) {
		this.visitNb = visitNb;
	}

	public String getDescription() {
		return content;
	}

	public void setDescription(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getReferenceDate() {
		return referencedt;
	}

	public void setReferenceDate(Date referencedt) {
		this.referencedt = referencedt;
	}

	public Date getEditedDate() {
		return editeddt;
	}

	public void setEditedDate(Date editeddt) {
		this.editeddt = editeddt;
	}
	
	@JsonIgnore
	public String getCreator() {
		return creator;
	}

	@JsonIgnore
	public void setCreator(String creator) {
		this.creator = creator;
	}

	public boolean isShift() {
		return shift;
	}

	public void setShift(boolean shift) {
		this.shift = shift;
	}

	@JsonIgnore
	public boolean isDeleted() {
		return deleted;
	}

	@JsonIgnore
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@JsonIgnore
	public String getEditor() {
		return editor;
	}

	@JsonIgnore
	public void setEditor(String editor) {
		this.editor = editor;
	}

}
