package net.fluance.cockpit.core.model.jdbc.note;

import java.util.Date;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.app.security.service.Person;
import net.fluance.cockpit.core.model.PatientReference;
import net.fluance.cockpit.core.model.PayloadDisplayLogName;
import net.fluance.commons.json.JsonFullDateDeserializer;
import net.fluance.commons.json.JsonFullDateSerializer;
import net.fluance.commons.lang.HTMLUtils;

public class NoteDetails implements Persistable<Long>, PayloadDisplayLogName {

	private long id;
	private String title;
	private String description;
	private Person editor;
	private Person creator;
	private String textPreview;
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonFullDateSerializer.class)
	@JsonDeserialize(using = JsonFullDateDeserializer.class)
	private Date referenceDate;
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonFullDateSerializer.class)
	@JsonDeserialize(using = JsonFullDateDeserializer.class)
	private Date editedDate;
	private boolean isDeleted;
	private boolean read;
	private boolean shift;
	private NoteCategory category;
	private PatientReference patient;
	private Long visitNb;
	private boolean hasPictures;

	public NoteDetails(){}

	public NoteDetails(long id, String title, String description, String editor, String creator, Boolean shift,
			Date referenceDate, Date editedDate, boolean isDeleted, NoteCategory category, PatientReference patient, Long visitNb) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.editor = new Person(editor);
		this.creator = new Person(creator);
		this.textPreview = preview();
		this.referenceDate = referenceDate;
		this.editedDate = editedDate;
		this.isDeleted = isDeleted;
		this.shift = shift;
		this.category = category;
		this.patient = patient;
		this.visitNb = visitNb;
	}

	@Override
	public Long getId() {
		return id;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}
	public NoteCategory getCategory() {
		return category;
	}

	public void setCategory(NoteCategory category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Person getEditor() {
		return editor;
	}

	public void setEditor(Person editor) {
		this.editor = editor;
	}
	
	public Person getCreator() {
		return creator;
	}

	public void setCreator(Person creator) {
		this.creator = creator;
	}

	public String getTextPreview() {
		return textPreview;
	}

	public void setTextPreview(String textPreview) {
		this.textPreview = textPreview;
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

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
	
	public boolean isShift() {
		return shift;
	}

	public void setShift(boolean shift) {
		this.shift = shift;
	}

	public PatientReference getPatient() {
		return patient;
	}

	public void setPatient(PatientReference patient) {
		this.patient = patient;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Long getVisitNb() {
		return visitNb;
	}
	
	public void setVisitNb(Long visitNb) {
		this.visitNb = visitNb;
	}
	
	public boolean isHasPictures() {
		return hasPictures;
	}

	public void setHasPictures(boolean hasPictures) {
		this.hasPictures = hasPictures;
	}

	private String preview(){
		String preview = HTMLUtils.cleanStringFromHtml(description);
		if (preview != null && preview.length() > 20)
			return preview.substring(0, 20);
		else
			return preview;
	}

	@Override
	public String displayName() {
		return this.title;
	}
}
