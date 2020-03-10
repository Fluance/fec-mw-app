/**
 * 
 */
package net.fluance.cockpit.core.model.jdbc.company;

import java.util.List;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
public class CompaniesList implements Persistable<Integer> {

	private Integer id;
	private String code;
	private String name;
	private List<Unit> units;
	private List<ServiceList> hospServices;
	
	public CompaniesList(Integer id, String code, String name, List<Unit> list, List<ServiceList> services) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.units = list;
		this.hospServices = services;
	}
	
	public CompaniesList(){}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Unit> getUnits() {
		return units;
	}

	public void setUnits(List<Unit> units) {
		this.units = units;
	}

	public List<ServiceList> getHospServices() {
		return hospServices;
	}

	public void setHospServices(List<ServiceList> services) {
		this.hospServices = services;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}
}