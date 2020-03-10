package net.fluance.cockpit.core.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.fluance.cockpit.core.model.VisitDetailed;
import net.fluance.cockpit.core.model.jpa.visit.VisitDetailedMapper;
import net.fluance.cockpit.core.repository.jpa.visit.VisitDetailedRepository;
import net.fluance.cockpit.core.util.JpaUtils;

@Service
public class VisitDetailedDao {
	
	@Autowired
	private VisitDetailedRepository visitDetailedRepository;

	public List<VisitDetailed> findAdmitedNowByCompanyIdAndBeds(Integer companyId, List<String> beds, Integer limit, Integer offset){
		return VisitDetailedMapper.toModel(
				visitDetailedRepository.findAdmitedNowByCompanyIdAndFullBedIn(companyId, beds, JpaUtils.createPageableFromLimitAndOffset(limit, offset))
			);
	}
	
	public Long countAdmitedNowByCompanyIdAndBeds(Integer companyId, List<String> beds){ 
		return visitDetailedRepository.countAdmitedNowByCompanyIdAndFullBedIn(companyId, beds);
	}
}
