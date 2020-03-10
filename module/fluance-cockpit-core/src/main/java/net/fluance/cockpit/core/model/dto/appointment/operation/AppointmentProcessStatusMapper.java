package net.fluance.cockpit.core.model.dto.appointment.operation;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import net.fluance.cockpit.core.model.jpa.appointment.AppointmentProcessStatus;
import net.fluance.cockpit.core.model.jpa.appointment.ProcessStatus;

@Component
public class AppointmentProcessStatusMapper {
	public List<AppointmentProcessStatusDto> toModel(List<AppointmentProcessStatus> appointmentProcessStatusList) {
		List<AppointmentProcessStatusDto> appointmentProcessStatusDtos = new ArrayList<>();

		appointmentProcessStatusList.stream().forEach(appointmentProcessStatus -> {
			appointmentProcessStatusDtos.add(toModel(appointmentProcessStatus));
		});

		return appointmentProcessStatusDtos;
	}

	public AppointmentProcessStatusDto toModel(AppointmentProcessStatus appointmentProcessStatus) {
		AppointmentProcessStatusDto appointmentProcessStatusDto = new AppointmentProcessStatusDto();

		appointmentProcessStatusDto.setAppointmentId(appointmentProcessStatus.getId().getAppointmentId());
		appointmentProcessStatusDto.setCompanyId(appointmentProcessStatus.getId().getCompanyId());
		appointmentProcessStatusDto.setProcessStatusId(appointmentProcessStatus.getId().getProcessStatusId());
		appointmentProcessStatusDto.setCompanyCode(appointmentProcessStatus.getCompanyCode());
		appointmentProcessStatusDto.setProcessStatusCode(appointmentProcessStatus.getProcessStatusCode());
		if(appointmentProcessStatus.getEventDate() != null) {
			appointmentProcessStatusDto.setEventDate(appointmentProcessStatus.getEventDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		}

		return appointmentProcessStatusDto;
	}
	
	public AppointmentProcessStatusDto toModelFromProcessStatus(ProcessStatus processStatus) {
		AppointmentProcessStatusDto appointmentProcessStatusDto = new AppointmentProcessStatusDto();
		appointmentProcessStatusDto.setCompanyId(processStatus.getId().getCompanyId());
		appointmentProcessStatusDto.setProcessStatusId(processStatus.getId().getProcessStatusId());		
		appointmentProcessStatusDto.setProcessStatusDescription(processStatus.getDescription());
		appointmentProcessStatusDto.setStepNumber(processStatus.getStep());

		return appointmentProcessStatusDto;
	}
}
