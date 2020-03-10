package net.fluance.cockpit.core.model.dto.patient;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.fluance.commons.json.LocalDateTimeDeserializer;
import net.fluance.commons.json.LocalDateTimeSerializer;

public class WeightDto {

	private Long id;
	private Long patientId;
	private String source;
	private String sourceId;
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime weightDate;
	private Double weight;
	private Double fatFreeMass;
	private Double fatRatio;
	private Double bodyTemperature;
	private Double muscleMass;
	private Double hydration;
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
	
	public LocalDateTime getWeightDate() {
		return weightDate;
	}

	public void setWeightDate(LocalDateTime weightDate) {
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
