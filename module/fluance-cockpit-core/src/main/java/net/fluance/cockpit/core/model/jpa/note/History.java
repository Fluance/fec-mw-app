package net.fluance.cockpit.core.model.jpa.note;

import java.util.Date;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.app.security.service.Person;
import net.fluance.commons.json.JsonDateTimeSerializer;

public class History implements Persistable<Long> {

	private Long historyId;
	private Long entityId;
	private String resourceType;
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date date;
	private Person editor;

	public History(){}
	
	public History(Long historyId, Long entityId, String resourceType, Date date, Person editor) {
		this.historyId = historyId;
		this.entityId = entityId;
		this.resourceType = resourceType;
		this.date = date;
		this.editor = editor;
	}

	public Long getHistoryId() {
		return historyId;
	}

	
	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}

	
	public Long getEntityId() {
		return entityId;
	}

	
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	
	public String getResourceType() {
		return resourceType;
	}

	
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	
	public Date getDate() {
		return date;
	}

	
	public void setDate(Date date) {
		this.date = date;
	}

	
	public Person getEditor() {
		return editor;
	}

	
	public void setEditor(Person editor) {
		this.editor = editor;
	}

	@JsonIgnore
	@Override
	public Long getId() {
		return null;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}
}
