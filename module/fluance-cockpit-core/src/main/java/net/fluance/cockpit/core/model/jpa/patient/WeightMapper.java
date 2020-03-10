package net.fluance.cockpit.core.model.jpa.patient;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;

import net.fluance.cockpit.core.model.dto.patient.WeightDto;

public class WeightMapper {
	
	public static WeightDto toModel(Weight entity) {
		Logger.getAnonymousLogger().info("ENT:"+entity.getId());
		WeightDto model = new WeightDto();
		model.setPatientId(entity.getPatientId());
		model.setId(entity.getId());
		model.setSource(entity.getSource());
		model.setSourceId(entity.getSourceId());
		model.setBodyTemperature(entity.getBodyTemperature());
		model.setBoneMass(entity.getBoneMass());
		model.setFatRatio(entity.getFatRatio());
		model.setFatFreeMass(entity.getFatFreeMass());
		model.setHydration(entity.getHydration());
		model.setMuscleMass(entity.getMuscleMass());
		model.setWeight(entity.getWeight());
		model.setWeightDate(entity.getWeightDate().toLocalDateTime());
		return model;
	}

	public static List<WeightDto> toModel(List<Weight> entities) {
		List<WeightDto> models = new ArrayList<>();
		for (Weight entity : entities) {
			models.add(toModel(entity));
		}
		return models;
	}

	public static Weight toEntity(WeightDto model) {
		Weight entity = new Weight();
		entity.setPatientId(model.getPatientId());
		entity.setId(model.getId());
		entity.setSource(model.getSource());
		entity.setSourceId(model.getSourceId());
		entity.setBodyTemperature(model.getBodyTemperature());
		entity.setBoneMass(model.getBoneMass());
		entity.setFatRatio(model.getFatRatio());
		entity.setFatFreeMass(model.getFatFreeMass());
		entity.setHydration(model.getHydration());
		entity.setMuscleMass(model.getMuscleMass());
		entity.setWeight(model.getWeight());
		entity.setWeightDate(Timestamp.valueOf(model.getWeightDate()));
		return entity;
	}

	public static Page<WeightDto> toModel(Page<Weight> entities) {
		if (entities == null) {
			return null;
		}
		return entities.map(new Converter<Weight, WeightDto>() {

			@Override
			public WeightDto convert(Weight entity) {
				return toModel(entity);
			}
		});
	}
}
