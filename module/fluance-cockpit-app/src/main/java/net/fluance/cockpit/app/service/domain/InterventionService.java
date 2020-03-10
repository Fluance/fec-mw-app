package net.fluance.cockpit.app.service.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.fluance.app.web.util.exceptions.NotFoundException;
import net.fluance.cockpit.core.model.jdbc.intervention.Diagnosis;
import net.fluance.cockpit.core.model.jdbc.intervention.Intervention;
import net.fluance.cockpit.core.model.jdbc.intervention.Operation;
import net.fluance.cockpit.core.repository.jdbc.intervention.InterventionRepository;

@Service
public class InterventionService {

	@Autowired
	private InterventionRepository interventionRepository;

	public Intervention getByVisitnb(Long visitnb){
		List<net.fluance.cockpit.core.domain.intervention.Intervention> rawInterventions = interventionRepository.getByVisitnb(visitnb);
		if(rawInterventions != null && !rawInterventions.isEmpty()){
			List<Diagnosis> diagnosis = new ArrayList<>();
			List<Operation> operations = new ArrayList<>();
			for (int i = 0; i < rawInterventions.size(); i++) {
				net.fluance.cockpit.core.domain.intervention.Intervention rawInv = rawInterventions.get(i);
				if(rawInv.getType().equalsIgnoreCase("diagnosis")){
					diagnosis.add(new Diagnosis(rawInv.getData(), rawInv.getRank()));
				}else if(rawInv.getType().equalsIgnoreCase("operation")){
					operations.add(new Operation(rawInv.getData(), rawInv.getRank()));
				}
			}
			return new Intervention(visitnb, rawInterventions.get(0).getInterventionDate(), operations, diagnosis);
		}
		return null;
	}
}
