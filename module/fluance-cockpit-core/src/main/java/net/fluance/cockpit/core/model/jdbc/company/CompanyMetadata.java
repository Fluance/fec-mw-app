package net.fluance.cockpit.core.model.jdbc.company;

import org.springframework.data.domain.Persistable;

@SuppressWarnings("serial")
public class CompanyMetadata implements Persistable<Integer>{
	
	private Integer id;
	private Integer companyId;
	private String type;
	private String title;
	private String name;
	private String location;
	
	public CompanyMetadata(Integer id, Integer companyId, String type, String title, String name, String location) {
		this.id = id;
		this.companyId = companyId;
		this.type = type;
		this.title = title;
		this.name = name;
		this.location = location;
	}

	@Override
	public Integer getId() {
		return this.id;
	}
	
	public Integer setId() {
		return this.id;
	}
	
	@Override
	public boolean isNew() {
		return id == null;
	}

	
	public Integer getCompanyId() {
		return companyId;
	}

	
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	
	public String getType() {
		return type;
	}

	
	public void setType(String type) {
		this.type = type;
	}

	
	public String getTitle() {
		return title;
	}

	
	public void setTitle(String title) {
		this.title = title;
	}

	
	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	
	public String getLocation() {
		return location;
	}

	
	public void setLocation(String location) {
		this.location = location;
	}
	
}
