package net.fluance.cockpit.core.model.jpa.note;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonFullDateDeserializer;
import net.fluance.commons.json.JsonFullDateSerializer;

@Entity(name = "NoteHistory")
@Table(name = "notehistory")
public class NoteHistoryDetail implements Serializable{

	@Id // signifies the primary key
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "noteHistoryIdSeqGenerator")
	@SequenceGenerator(name = "noteHistoryIdSeqGenerator", sequenceName = "notehistory_id_seq", allocationSize = 1)
	@Column(name="id")
	private Long historyId;
	@Column(name = "note_id", nullable = false)
	private Long noteId;
	@Column(name = "category_id")
	private Integer categoryId;
	@Column(name = "patient_id")
	private Long patientId;
	@Column(name = "visit_nb")
	private Long visitNb;
	private String editor;
	private String creator;
	@Column(name = "content")
	private String description;
	private String title;
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonFullDateSerializer.class)
	@JsonDeserialize(using = JsonFullDateDeserializer.class)
	@Column(name="editeddt")
	private Date editedDate;
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonFullDateSerializer.class)
	@JsonDeserialize(using = JsonFullDateDeserializer.class)
	@Column(name="referencedt")
	private Date referenceDate;
	private boolean deleted;
	private Boolean shifted;
	@Column(name = "picturesorder")
	private String picturesOrder;

	public Long getHistoryId() {
		return historyId;
	}

	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}

	public Long getNoteId() {
		return noteId;
	}

	public void setNoteId(Long noteId) {
		this.noteId = noteId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getEditedDate() {
		return editedDate;
	}

	public void setEditedDate(Date editedDate) {
		this.editedDate = editedDate;
	}

	public Date getReferenceDate() {
		return referenceDate;
	}

	public void setReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@JsonIgnore
	public Boolean isShifted() {
		if(this.shifted != null){
			return shifted;
		}else{
			return false;
		}
	}

	public void setShifted(Boolean shifted) {
		this.shifted = shifted;
	}

	public String getPicturesOrder() {
		return picturesOrder;
	}

	public void setPicturesOrder(String picturesOrder) {
		this.picturesOrder = picturesOrder;
	}

}
