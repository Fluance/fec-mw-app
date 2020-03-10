package net.fluance.cockpit.core.model.jpa.visit;

import java.util.ArrayList;
import java.util.List;

import net.fluance.cockpit.core.model.CompanyReference;
import net.fluance.cockpit.core.model.PatientReference;
import net.fluance.cockpit.core.model.VisitDetailed;

public class VisitDetailedMapper {
	private VisitDetailedMapper() {}

	public static VisitDetailed toModel(VisitDetailedEntity entity) {
		VisitDetailed model = null;
		if (entity != null) {
			model = new VisitDetailed();
			
			model.setVisitNb(entity.getVisitNumber());
			model.setAdmitDate(entity.getAdmitDate());
			model.setDischargeDate(entity.getDischargeDate());
			model.setExpDischargeDate(entity.getVisitDetail().getExpiredDischargeDate());
			model.setPatientClass(entity.getPatientClass());
			model.setPatientClassDesc(entity.getVisitDetail().getPatientClassDesc());
			model.setPatientType(entity.getVisitDetail().getPatientType());
			model.setPatientTypeDesc(entity.getPatientTypeDesc());
			model.setPatientCase(entity.getPatientCase());
			model.setPatientCaseDesc(entity.getVisitDetail().getPatientCaseDesc());			
			model.setHospService(entity.getHospService());
			model.setHospServiceDesc(entity.getVisitDetail().getHospServiceDesc());			
			model.setAdmissionType(entity.getVisitDetail().getAdmissionType());
			model.setAdmissionTypeDesc(entity.getAdmissionTypeDesc());
			model.setPatientUnit(entity.getPatientUnit());
			model.setPatientUnitDesc(entity.getPatientUnitDesc());
			model.setPatientRoom(entity.getPatientRoom());
			model.setPatientBed(entity.getPatientBed());
			model.setPriorRoom(entity.getVisitDetail().getPriorRoom());
			model.setPriorBed(entity.getVisitDetail().getPriorBed());
			model.setPriorUnit(entity.getVisitDetail().getPriorUnit());
			model.setPriorUnitDesc(entity.getVisitDetail().getPriorUnitDesc());
			model.setAdmitSource(entity.getVisitDetail().getAdmitSource());
			model.setAdmitSourceDesc(entity.getVisitDetail().getAdmitSourceDesc());
			model.setFinancialClass(entity.getFinancialClass());
			model.setFinancialClassDesc(entity.getFinancialClassDesc());
			model.setHl7code(entity.getHl7Code());
			
			model.setCompany(new CompanyReference(entity.getCompanyId(), entity.getCompany().getName(), entity.getCompany().getCode()));			
			model.setPatient(new PatientReference(entity.getPatientId(), entity.getFirstName(), entity.getLastName(), entity.getMaidenName(), entity.getBirthdate()));
		}
		return model;
	}
	
	public static List<VisitDetailed> toModel(List<VisitDetailedEntity> entities) {
		List<VisitDetailed> visitDetaileds = new ArrayList<>();
		
		entities.stream().forEach(entity -> {
			visitDetaileds.add(toModel(entity));
		});
		
		return visitDetaileds;
	}
}
