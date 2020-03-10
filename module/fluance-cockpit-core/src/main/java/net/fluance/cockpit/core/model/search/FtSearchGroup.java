package net.fluance.cockpit.core.model.search;

import com.fasterxml.jackson.databind.JsonNode;

public class FtSearchGroup {

	private long count;
	private String entityType;
	private JsonNode data;

	public FtSearchGroup(long count, String entityType, JsonNode data) {
		super();
		this.count = count;
		this.entityType = entityType;
		this.data = data;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long total) {
		this.count = total;
	}

	public JsonNode getData() {
		return data;
	}

	public void setData(JsonNode data) {
		this.data = data;
	}
	
	public String getEntityType() {
		return entityType;
	}
	
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	
}
