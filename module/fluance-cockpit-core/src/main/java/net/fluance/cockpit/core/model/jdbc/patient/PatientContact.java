/**
 * 
 */
package net.fluance.cockpit.core.model.jdbc.patient;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import static com.nurkiewicz.jdbcrepository.JdbcRepository.pk;

@SuppressWarnings("serial")
public class PatientContact implements Persistable<Object[]> {
	
	
	public Long getPid() {
		return pid;
	}

	
	public void setPid(Long pid) {
		this.pid = pid;
	}

	private transient boolean persisted;
	@JsonIgnore
	private Long pid;
	private String nbType;
	private String equipment;
	private String data;
	
	/**
	 * 
	 */
	public PatientContact() {}

	/**
	 * @param pid
	 * @param nbType
	 * @param equipment
	 * @param data
	 */
	public PatientContact(Long pid, String nbType, String equipment, String data) {
		super();
		this.pid = pid;
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
		return pk(pid, nbType);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return !persisted;
	}

}
