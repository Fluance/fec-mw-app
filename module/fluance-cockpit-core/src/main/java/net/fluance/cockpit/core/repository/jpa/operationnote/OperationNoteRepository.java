package net.fluance.cockpit.core.repository.jpa.operationnote;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.fluance.cockpit.core.model.dto.appointment.operation.AppointmentOperationNoteDto;
import net.fluance.cockpit.core.model.jpa.operationnote.OperationNote;

@Repository
public interface OperationNoteRepository extends CrudRepository<OperationNote, Integer>{

	public List<OperationNote> findByAppointmentId(Long appointmentId);
	
}
