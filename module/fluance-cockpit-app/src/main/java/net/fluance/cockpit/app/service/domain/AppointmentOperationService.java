package net.fluance.cockpit.app.service.domain;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.web.util.exceptions.LockedException;
import net.fluance.app.web.util.exceptions.MustRequestLockException;
import net.fluance.app.web.util.exceptions.NotFoundException;
import net.fluance.cockpit.core.dao.AppointmentOperationNoteDao;
import net.fluance.cockpit.core.dao.AppointmentProcessStatusDao;
import net.fluance.cockpit.core.model.dto.appointment.operation.AppointmentOperationNoteDto;
import net.fluance.cockpit.core.model.dto.appointment.operation.AppointmentProcessStatusDto;

/**
 * Manage all the stuff related with appointments that represents an operation
 */
@Service
public class AppointmentOperationService {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String LOCK_RESOURCE_TYPE_OPERATIONNOTE = "operation_note";
	
	@Autowired
	AppointmentProcessStatusDao appointmentOperationStatusDao;
	@Autowired
	AppointmentOperationNoteDao appointmentOperationNoteDao;
	@Autowired
	LockService lockService;
	
	
	/**
	 * get the list of operation status for an appointment
	 * 
	 * @return
	 */
	public List<AppointmentProcessStatusDto> getOperationProcessStatusByAppointmentId(Long appointmentId) {
		if(appointmentId == null) {
			throw new IllegalArgumentException("Appointment Id must have a value");
		}
		
		return appointmentOperationStatusDao.getProcessStatusByAppointmentId(appointmentId);
	}
	
	/**
	 * get the operation note for an appointment
	 * 
	 * @param appointmentId
	 * @return
	 */
	public AppointmentOperationNoteDto getOperationNoteByAppointmentId(Long appointmentId) {
		if(appointmentId == null) {
			throw new IllegalArgumentException("Appointment Id must have a vlue");
		}
		
		return appointmentOperationNoteDao.getOperationNotesByAppointmentId(appointmentId);
	}
	
	/**
	 * Update Operation Note related with the appointment
	 * 
	 * @param appointmentId
	 * @param note
	 * @return
	 */
	public AppointmentOperationNoteDto updateOperationNoteByAppointmentId(Long appointmentId, String note, User user) {
		if(appointmentId == null) {
			throw new IllegalArgumentException("Appointment Id must have a vlue");
		}
		
		AppointmentOperationNoteDto appointmentOperationNoteDto = appointmentOperationNoteDao.getOperationNotesByAppointmentId(appointmentId);
		
		if(appointmentOperationNoteDto != null && appointmentOperationNoteDto.getId() != null) {
			validateLockForOperationNoteEntity(appointmentOperationNoteDto, user);
			appointmentOperationNoteDto = appointmentOperationNoteDao.updateOperationNote(appointmentId, appointmentOperationNoteDto.getId(), note);
		}else {
			appointmentOperationNoteDto = appointmentOperationNoteDao.createOperationNote(appointmentId, note);
		}
		return appointmentOperationNoteDto;
	}
	
	/**
	 * Create Operation Note related with the appointment
	 * 
	 * @param appointmentId
	 * @param note
	 * @return
	 */
	public AppointmentOperationNoteDto createOperationNote(Long appointmentId, String note) {
		if(appointmentId == null) {
			throw new IllegalArgumentException("Appointment Id must have a value");
		}
		
		return appointmentOperationNoteDao.createOperationNote(appointmentId, note);
	}
	
	/**
	 * Check if the operation note is locked or not by the current user
	 * 
	 * @param entity
	 * @param user
	 */
	private void validateLockForOperationNoteEntity(AppointmentOperationNoteDto entity, User user) {
		if (entity != null && entity.getAppointmentId() != null && entity.getAppointmentId() > 0) {
			LOGGER.info(
					"The user:" + user.getUsername() + " Tries to LOCK Operation note item:" + entity.getAppointmentId());
			if (!lockService.isLockedByUser(entity.getAppointmentId().longValue(), LOCK_RESOURCE_TYPE_OPERATIONNOTE, user.getUsername(),
					user.getDomain())) {
				if (lockService.verify(entity.getAppointmentId().longValue(), LOCK_RESOURCE_TYPE_OPERATIONNOTE).isLocked()) {
					throw new LockedException();
				} else {
					throw new MustRequestLockException(MustRequestLockException.RESOURCE_NOT_LOCKED);
				}
			}
		} else {
			LOGGER.error("The given Operation note item AppointmentId doesn't exist: " + entity.getAppointmentId());
			throw new NotFoundException();
		}
	}

}
