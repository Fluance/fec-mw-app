package net.fluance.cockpit.core.model.jpa.patient;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="weight",schema="openwt")
public class Weight {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="patient_id")
	@NotNull
	private Long patientId;
	
	@Column(name="source")
	@NotNull
	private String source;
	
	@Column(name="sourceid")
	@NotNull
	private String sourceId;
	
	@Column(name="weightdt")
	private Timestamp weightDate;
	
	@Column(name="weight")
	private Double weight;
	
	@Column(name="fatfreemass")
	private Double fatFreeMass;
	
	@Column(name="fatratio")
	private Double fatRatio;
	
	@Column(name="bodytemperature")
	private Double bodyTemperature;
	
	@Column(name="musclemass")
	private Double muscleMass;
	
	@Column(name="hydration")
	private Double hydration;
	
	@Column(name="bonemass")
	private Double boneMass;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getPatientId() {
		return patientId;
	}
	
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}
	
	public String getSource() {
		return source;
	}
	
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getSourceId() {
		return sourceId;
	}
	
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	public Timestamp getWeightDate() {
		return weightDate;
	}

	public void setWeightDate(Timestamp weightDate) {
		this.weightDate = weightDate;
	}

	public Double getWeight() {
		return weight;
	}
	
	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getFatFreeMass() {
		return fatFreeMass;
	}
	
	public void setFatFreeMass(Double fatFreeMass) {
		this.fatFreeMass = fatFreeMass;
	}
	
	public Double getFatRatio() {
		return fatRatio;
	}
	
	public void setFatRatio(Double fatRatio) {
		this.fatRatio = fatRatio;
	}

	public Double getBodyTemperature() {
		return bodyTemperature;
	}

	public void setBodyTemperature(Double bodyTemperature) {
		this.bodyTemperature = bodyTemperature;
	}
	
	public Double getMuscleMass() {
		return muscleMass;
	}
	
	public void setMuscleMass(Double muscleMass) {
		this.muscleMass = muscleMass;
	}

	public Double getHydration() {
		return hydration;
	}
	
	public void setHydration(Double hydration) {
		this.hydration = hydration;
	}
	
	public Double getBoneMass() {
		return boneMass;
	}
	
	public void setBoneMass(Double boneMass) {
		this.boneMass = boneMass;
	}
	
}
