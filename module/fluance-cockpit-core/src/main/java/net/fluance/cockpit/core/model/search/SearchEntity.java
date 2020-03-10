package net.fluance.cockpit.core.model.search;

public class SearchEntity {

	private String entityType;
	private Object entity;

	public SearchEntity() {}

	public SearchEntity(String entityType, Object entity) {
		this.entityType = entityType;
		this.entity = entity;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}
}
