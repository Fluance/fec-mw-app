package net.fluance.cockpit.core.model.search;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

public class FtSearchModelOld implements Serializable {

	private long count;
	private int offset;
	private int limit;
	private Map<String, Long> facetFields;
	private JsonNode data;

	public FtSearchModelOld() {
	}

	public FtSearchModelOld(long count, int offset, int limit, JsonNode data, Map<String, Long> facetFields) {
		super();
		this.count = count;
		this.offset = offset;
		this.limit = limit;
		this.data = data;
		this.facetFields = facetFields;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public Map<String, Long> getFacetFields() {
		return facetFields;
	}

	public void setFacetFields(Map<String, Long> facetFields) {
		this.facetFields = facetFields;
	}

	public JsonNode getData() {
		return data;
	}

	public void setData(JsonNode data) {
		this.data = data;
	}
}
