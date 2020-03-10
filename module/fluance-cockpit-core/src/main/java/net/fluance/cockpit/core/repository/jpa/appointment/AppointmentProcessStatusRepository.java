package net.fluance.cockpit.core.repository.jpa.appointment;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import net.fluance.cockpit.core.model.jpa.appointment.AppointmentProcessStatus;
import net.fluance.cockpit.core.model.jpa.appointment.AppointmentProcessStatusPK;

public interface AppointmentProcessStatusRepository extends CrudRepository<AppointmentProcessStatus, AppointmentProcessStatusPK> {
	public List<AppointmentProcessStatus> findByIdAppointmentId(Long appointmentId);
}
