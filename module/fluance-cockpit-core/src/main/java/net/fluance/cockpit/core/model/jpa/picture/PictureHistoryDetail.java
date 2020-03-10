package net.fluance.cockpit.core.model.jpa.picture;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonFullDateDeserializer;
import net.fluance.commons.json.JsonFullDateSerializer;

@Entity(name = "PictureHistory")
@Table(name = "picturehistory")
public class PictureHistoryDetail {
	
	@Id // signifies the primary key
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "noteHistoryIdSeqGenerator")
	@SequenceGenerator(name = "noteHistoryIdSeqGenerator", sequenceName = "notehistory_id_seq", allocationSize = 1)
	@Column(name="id")
	private Long historyId;
	@Column(name = "picture_id", nullable = false)
	private Long pictureId;
	@Column(name = "note_id", nullable = false)
	private Long noteId;
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
	private String filename;
	private String annotation;
	private String editor;
	private boolean deleted;
	@Column(name="sortorder")
	private Integer sortOrder;
	
	public Long getHistoryId() {
		return historyId;
	}
	
	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}
	
	public Long getPictureId() {
		return pictureId;
	}
	
	public void setPictureId(Long pictureId) {
		this.pictureId = pictureId;
	}
	
	public Long getNoteId() {
		return noteId;
	}
	
	public void setNoteId(Long noteId) {
		this.noteId = noteId;
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
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getAnnotation() {
		return annotation;
	}
	
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	
	public String getEditor() {
		return editor;
	}
	
	public void setEditor(String editor) {
		this.editor = editor;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public Integer getSortOrder() {
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	
}
