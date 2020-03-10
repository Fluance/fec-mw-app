package net.fluance.cockpit.core.model.jdbc.visit;

import static com.nurkiewicz.jdbcrepository.JdbcRepository.pk;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
public class Diagnosis implements Persistable<Object[]>{

	private long nbRecords;
	private String data;
	private Integer rank;
	private String type;
	private String description;
	private String descLanguage;

	public Diagnosis(){}
	
	public Diagnosis(long nbRecords, String data, Integer rank, String type, String description, String descLanguage) {
		this.nbRecords = nbRecords;
		this.data = data;
		this.rank = rank;
		this.type = type;
		this.description = description;
		this.descLanguage = descLanguage;
	}

	@JsonIgnore
	@Override
	public Object[] getId() {
		return pk(type, rank);
	}

	public long getNbRecords() {
		return nbRecords;
	}
	public void setNbrecords(long nbrecords) {
		this.nbRecords = nbrecords;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescLanguage() {
		return descLanguage;
	}

	public void setDescLanguage(String descLanguage) {
		this.descLanguage = descLanguage;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return true;
	}

}
