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
@Table(name = "exercise", schema = "openwt")
public class Exercise {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@NotNull
	@Column(name = "patient_id")
	private Long patientId;
	@NotNull
	@Column(name = "source")
	private String source;
	@NotNull
	@Column(name = "sourceid")
	private String sourceId;
	@Column(name = "exercisedt")
	private Timestamp exerciseDate;
	@Column(name = "steps")
	private Integer steps;
	@Column(name = "distance")
	private Integer distance;
	@Column(name = "calories")
	private Integer calories;
	@Column(name = "heartrate")
	private Double heartrate;
	@Column(name = "duration")
	private Integer duration;
	@Column(name = "elevation")
	private Integer elevation;

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

	public Timestamp getExerciseDate() {
		return exerciseDate;
	}

	public void setExerciseDate(Timestamp exerciseDate) {
		this.exerciseDate = exerciseDate;
	}

	public Integer getSteps() {
		return steps;
	}

	public void setSteps(Integer steps) {
		this.steps = steps;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public Integer getCalories() {
		return calories;
	}

	public void setCalories(Integer calories) {
		this.calories = calories;
	}

	public Double getHeartrate() {
		return heartrate;
	}

	public void setHeartrate(Double heartrate) {
		this.heartrate = heartrate;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getElevation() {
		return elevation;
	}

	public void setElevation(Integer elevation) {
		this.elevation = elevation;
	}
}
