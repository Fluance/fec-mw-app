/**
 * 
 */
package net.fluance.cockpit.core.model.jdbc.patient;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import static com.nurkiewicz.jdbcrepository.JdbcRepository.pk;

@SuppressWarnings("serial")
public class PatientNextOfKinContact implements Persistable<Object[]> {
	
	private transient boolean persisted;
	@JsonIgnore
	private Long nokId;
	@JsonIgnore
	private String nbType;
	private String equipment;
	private String data;
	
	/**
	 * 
	 */
	public PatientNextOfKinContact() {}

	/**
	 * @param pid
	 * @param nbType
	 * @param equipment
	 * @param data
	 */
	public PatientNextOfKinContact(Long nokId, String nbType, String equipment, String data) {
		super();
		this.nokId = nokId;
		this.nbType = nbType;
		this.equipment = equipment;
		this.data = data;
	}

	
	
	/**
	 * @return the nbType
	 */
	public String getNbType() {
		return nbType;
	}

	/**
	 * @param nbType the nbType to set
	 */
	public void setNbType(String nbType) {
		this.nbType = nbType;
	}

	/**
	 * @return the equipment
	 */
	public String getEquipment() {
		return equipment;
	}

	/**
	 * @param equipment the equipment to set
	 */
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}

	@JsonIgnore
	@Override
	public Object[] getId() {
		return pk(nokId, nbType);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return !persisted;
	}

}
