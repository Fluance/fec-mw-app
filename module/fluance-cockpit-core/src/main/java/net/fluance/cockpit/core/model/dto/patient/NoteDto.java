package net.fluance.cockpit.core.model.dto.patient;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonFullDateDeserializer;
import net.fluance.commons.json.JsonFullDateSerializer;

public class NoteDto {

	private Long id;
	private Long patientId;
	private String title;
	private String editor;
	private String creator;
	private String content;
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonFullDateSerializer.class)
	@JsonDeserialize(using = JsonFullDateDeserializer.class)
	private Date referenceDate;
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonFullDateSerializer.class)
	@JsonDeserialize(using = JsonFullDateDeserializer.class)
	private Date editedDate;
	private Boolean shifted;
	private String source;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}
}
