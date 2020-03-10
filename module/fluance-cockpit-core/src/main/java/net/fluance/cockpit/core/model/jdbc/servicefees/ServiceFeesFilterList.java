package net.fluance.cockpit.core.model.jdbc.servicefees;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ServiceFeesFilterList implements Persistable<Long>{
	private String filter;
	
	public ServiceFeesFilterList(String filter) {
		this.filter = filter;
	}
	
	public String getFilter() {
		return this.filter;
	}
	
	public void setFilter(String filter) {
		this.filter= filter;
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
