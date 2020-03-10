package net.fluance.cockpit.core.repository.jpa.surgeryboard;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.dto.surgeryboard.SurgeryboardDto;
import net.fluance.cockpit.core.model.jpa.surgeryboard.Surgeryboard;
import net.fluance.cockpit.core.model.jpa.surgeryboard.SurgeryboardMapper;
import net.fluance.cockpit.core.model.jpa.surgeryboard.SurgeryboardPK;

@Service
public class SurgeryboardDao {
	
	@Autowired
	SurgeryboardRepository surgeryboardRepository;
	
	@Autowired
	SurgeryboardMapper surgeryboardMapper;

	public SurgeryboardDto findById(Integer companyId, Date noteDate) {
		SurgeryboardPK surgeryboardPK = new SurgeryboardPK();
		surgeryboardPK.setCompanyId(companyId);
		surgeryboardPK.setNoteDate(noteDate);
		return surgeryboardMapper.toModel(surgeryboardRepository.findOne(surgeryboardPK));
	}
	
	public SurgeryboardDto update(Integer companyId, Date noteDate, String note) {
		SurgeryboardPK surgeryboardPK = new SurgeryboardPK();
		surgeryboardPK.setCompanyId(companyId);
		surgeryboardPK.setNoteDate(noteDate);
		
		Surgeryboard surgeryboard = new Surgeryboard();
		surgeryboard.setId(surgeryboardPK);
		surgeryboard.setNote(note);
		
		return surgeryboardMapper.toModel(surgeryboardRepository.save(surgeryboard));
	}
	
}
