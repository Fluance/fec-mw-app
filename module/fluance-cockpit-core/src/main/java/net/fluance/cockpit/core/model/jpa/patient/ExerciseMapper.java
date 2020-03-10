package net.fluance.cockpit.core.model.jpa.patient;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import net.fluance.cockpit.core.model.dto.patient.ExerciseDto;

public class ExerciseMapper {

	public static ExerciseDto toModel(Exercise entity) {
		ExerciseDto model = new ExerciseDto();
		model.setPatientId(entity.getPatientId());
		model.setCalories(entity.getCalories());
		model.setDistance(entity.getDistance());
		model.setDuration(entity.getDuration());
		model.setElevation(entity.getElevation());
		model.setExerciseDate(entity.getExerciseDate().toLocalDateTime());
		model.setHeartrate(entity.getHeartrate());
		model.setId(entity.getId());
		model.setSource(entity.getSource());
		model.setSourceId(entity.getSourceId());
		model.setSteps(entity.getSteps());
		return model;
	}

	public static List<ExerciseDto> toModel(List<Exercise> entities) {
		List<ExerciseDto> models = new ArrayList<>();
		for (Exercise entity : entities) {
			models.add(toModel(entity));
		}
		return models;
	}

	public static Exercise toEntity(ExerciseDto model) {
		Exercise entity = new Exercise();
		entity.setPatientId(model.getPatientId());
		entity.setCalories(model.getCalories());
		entity.setDistance(model.getDistance());
		entity.setDuration(model.getDuration());
		entity.setElevation(model.getElevation());
		entity.setExerciseDate(Timestamp.valueOf(model.getExerciseDate()));
		entity.setHeartrate(model.getHeartrate());
		entity.setId(model.getId());
		entity.setSource(model.getSource());
		entity.setSourceId(model.getSourceId());
		entity.setSteps(model.getSteps());
		return entity;
	}

	public static Page<ExerciseDto> toModel(Page<Exercise> entities) {
		if (entities == null) {
			return null;
		}
		return entities.map(ExerciseMapper::toModel);
	}
}
