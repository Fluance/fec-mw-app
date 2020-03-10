package net.fluance.cockpit.core.model.jpa.picture;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonFullDateDeserializer;
import net.fluance.commons.json.JsonFullDateSerializer;

@Entity
@Table(name = "picture")
public class Picture implements Serializable {

	@Id // signifies the primary key
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pictureIdSeqGenerator")
	@SequenceGenerator(name = "pictureIdSeqGenerator", sequenceName = "picture_id_seq", allocationSize = 1)
	@Column(name = "id")
	private Long pictureId;
	@Column(name = "note_id", nullable = false)
	private Long noteId;
	private String filename;
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonFullDateSerializer.class)
	@JsonDeserialize(using = JsonFullDateDeserializer.class)
	@Column(name = "editeddt")
	private Date editedDate;
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonFullDateSerializer.class)
	@JsonDeserialize(using = JsonFullDateDeserializer.class)
	@Column(name = "referencedt")
	private Date referenceDate;
	private String annotation;
	private String editor;
	private boolean deleted;
	@Column(name = "sortorder")
	private Integer order;

	public Long getPictureId() {
		return pictureId;
	}

	public void setPictureId(Long id) {
		this.pictureId = id;
	}

	public Long getNoteId() {
		return noteId;
	}

	public void setNoteId(Long noteId) {
		this.noteId = noteId;
	}

	public String getFileName() {
		return filename;
	}

	public void setFileName(String filename) {
		this.filename = filename;
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

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public String getEditor() {
		return editor;
	}

	@JsonIgnore
	public void setEditor(String editor) {
		this.editor = editor;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer sortOrder) {
		this.order = sortOrder;
	}
}
