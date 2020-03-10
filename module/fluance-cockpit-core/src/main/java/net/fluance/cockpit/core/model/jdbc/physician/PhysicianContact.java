/**
 * 
 */
package net.fluance.cockpit.core.model.jdbc.physician;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
public class PhysicianContact implements Persistable<Integer> {

	private String nbType;
	private String equipment;
	private String data;

    public PhysicianContact(){}
	public PhysicianContact(String nbType, String equipment, String data) {
		super();
		this.nbType = nbType;
		this.equipment = equipment;
		this.data = data;
	}
	
	public String getNbType() {
		return nbType;
	}
	
	public void setNbType(String nbType) {
		this.nbType = nbType;
	}
	
	public String getEquipment() {
		return equipment;
	}
	
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}
	
	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return null;
	}

}