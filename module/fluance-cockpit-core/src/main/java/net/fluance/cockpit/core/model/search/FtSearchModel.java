package net.fluance.cockpit.core.model.search;

import java.io.Serializable;
import java.util.List;

public class FtSearchModel implements Serializable {

	private long totalCount;
	private int offset;
	private int limit;
	private List<FtSearchGroup> groups;

	public FtSearchModel() {
	}

	public FtSearchModel(long totalCount, int offset, int limit, List<FtSearchGroup> groups) {
		super();
		this.totalCount = totalCount;
		this.offset = offset;
		this.limit = limit;
		this.groups = groups;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long count) {
		this.totalCount = count;
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

	public List<FtSearchGroup> getGroups() {
		return groups;
	}

	public void setTotalGroups(List<FtSearchGroup> groups) {
		this.groups = groups;
	}
}
