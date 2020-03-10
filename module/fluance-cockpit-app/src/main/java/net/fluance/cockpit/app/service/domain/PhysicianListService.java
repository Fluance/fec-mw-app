package net.fluance.cockpit.app.service.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.jdbc.physician.PhysicianList;
import net.fluance.cockpit.core.repository.jdbc.physician.PhysicianListRepository;

@Service
public class PhysicianListService {

	@Autowired
	PhysicianListRepository physicianListRepository;
	
	public List<PhysicianList> findPhysicianByVisitNb(Long visitNb){
		
		if(visitNb==null){
			throw new IllegalArgumentException("Visit Number is required"); 
		}
		
		List<PhysicianList> physicianList = physicianListRepository.findPhysicianByVisitnb(visitNb);
		
		if(physicianList==null || physicianList.isEmpty()){
			return new ArrayList<PhysicianList>();
		}
		
		return physicianList;
		
	}
}
