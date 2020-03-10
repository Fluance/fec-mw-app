package net.fluance.cockpit.core.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.dto.appointment.operation.AppointmentOperationNoteDto;
import net.fluance.cockpit.core.model.jpa.operationnote.OperationNote;
import net.fluance.cockpit.core.model.jpa.operationnote.OperationNoteMapper;
import net.fluance.cockpit.core.repository.jpa.operationnote.OperationNoteRepository;

/**
 * Data access service for getting the operation note of one appointment that is operation appointment
 */
@Service
public class AppointmentOperationNoteDao {
	
	@Autowired
	OperationNoteRepository operationNoteRepository;
	@Autowired
	OperationNoteMapper operationNoteMapper;

	
	/**
	 * Gets the note for the given appointment
	 * 
	 * @param appointmentId
	 * @return
	 */
	public AppointmentOperationNoteDto getOperationNotesByAppointmentId(Long appointmentId){
		if(appointmentId == null) {
			throw new IllegalArgumentException("Appointment Id must have a value");
		}
		
		AppointmentOperationNoteDto result = null;
		List<AppointmentOperationNoteDto> status = operationNoteMapper.toModel(operationNoteRepository.findByAppointmentId(appointmentId));
		
		if(!status.isEmpty() && status != null && status.get(0) != null) {
			result = status.get(0);
		}
		
		return result;
	}
		
	/**
	 * 
	 * @param appointmentId
	 * @param id
	 * @param note
	 * @return
	 */
	public AppointmentOperationNoteDto updateOperationNote(Long appointmentId, Integer id, String note){
		if(appointmentId == null) {
			throw new IllegalArgumentException("Appointment Id must have a value");
		}
		if(id == null) {
			throw new IllegalArgumentException("Id must have a value");
		}
		OperationNote operationNote = new OperationNote();
		operationNote.setAppointmentId(appointmentId);
		operationNote.setId(id);
		operationNote.setNote(note);
		AppointmentOperationNoteDto status = operationNoteMapper.toModel(operationNoteRepository.save(operationNote));
		
		return status;
	}
	
	/**
	 * 
	 * @param appointmentId
	 * @param id
	 * @param note
	 * @return
	 */
	public AppointmentOperationNoteDto createOperationNote(Long appointmentId, String note){
		if(appointmentId == null) {
			throw new IllegalArgumentException("Appointment Id must have a value");
		}
		OperationNote operationNote = new OperationNote();
		operationNote.setAppointmentId(appointmentId);
		operationNote.setNote(note);
		AppointmentOperationNoteDto status = operationNoteMapper.toModel(operationNoteRepository.save(operationNote));
		
		return status;
	}
}
