package net.fluance.cockpit.app.service.domain;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.fluance.app.data.model.identity.User;
import net.fluance.app.web.util.exceptions.LockedException;
import net.fluance.app.web.util.exceptions.MustRequestLockException;
import net.fluance.app.web.util.exceptions.NotFoundException;
import net.fluance.cockpit.core.model.dto.surgeryboard.SurgeryboardDto;
import net.fluance.cockpit.core.model.jpa.surgeryboard.Surgeryboard;
import net.fluance.cockpit.core.repository.jpa.surgeryboard.SurgeryboardDao;

@Service
public class SurgeryboardService {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final String LOCK_RESOURCE_TYPE_SURGERYBOARD = "surgeryboard";
	
	@Autowired
	SurgeryboardDao surgeryboardDao;
	
	@Autowired
	LockService lockService;
	
	/**
	 * Get the value from {@link Surgeryboard}
	 * 
	 * @param companyId
	 * @param noteDate
	 * @return {@link SurgeryboardDto}
	 */
	public SurgeryboardDto getOneEntry(Integer companyId, Date noteDate) {
		SurgeryboardDto surgeryboardDto = surgeryboardDao.findById(companyId, noteDate);
		return surgeryboardDto;
	}
	
	/**
	 * Set the value from {@link Surgeryboard}
	 * 
	 * @param companyId
	 * @param noteDate
	 * @param note
	 * @return {@link SurgeryboardDto}
	 */
	public SurgeryboardDto update(Integer companyId, Date noteDate, String note, User user) {
		
		SurgeryboardDto surgeryBoardDto = new SurgeryboardDto();
		surgeryBoardDto.setCompanyId(companyId);
		surgeryBoardDto.setNoteDate(noteDate);
		surgeryBoardDto.setNote(note);
		
		validateLockForSurgerboardEntity(surgeryBoardDto, user);
		
		SurgeryboardDto surgeryboardDto = surgeryboardDao.update(companyId, noteDate, note);
		return surgeryboardDto;
	}
	
	/**
	 * Check if the surgeryboard is locked or not by the the current user
	 * 
	 * @param entity
	 * @param user
	 */
	private void validateLockForSurgerboardEntity(SurgeryboardDto entity, User user) {
		if (entity != null && entity.getLockId() != null && entity.getLockId() > 0) {
			LOGGER.info(
					"The user:" + user.getUsername() + " Tries to LOCK whiteboard item:" + entity.getLockId());
			if (!lockService.isLockedByUser(entity.getLockId(), LOCK_RESOURCE_TYPE_SURGERYBOARD, user.getUsername(),
					user.getDomain())) {
				if (lockService.verify(entity.getLockId(), LOCK_RESOURCE_TYPE_SURGERYBOARD).isLocked()) {
					throw new LockedException();
				} else {
					throw new MustRequestLockException(MustRequestLockException.RESOURCE_NOT_LOCKED);
				}
			}
		} else {
			LOGGER.error("The given Whiteboard item ID doesn't exist: " + entity.getLockId());
			throw new NotFoundException();
		}
	}

}
