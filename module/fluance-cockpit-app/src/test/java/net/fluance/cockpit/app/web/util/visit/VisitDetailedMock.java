package net.fluance.cockpit.app.web.util.visit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.fluance.cockpit.core.model.CompanyReference;
import net.fluance.cockpit.core.model.PatientReference;
import net.fluance.cockpit.core.model.VisitDetailed;

public class VisitDetailedMock {
	
	private static final Long VISIT_NUMBER_BASE = 1000000L;
	private static final Long PATIENT_ID_BASE = 1000L;
	private static final Long PATIENT_ROOM_BASE = 100L;
	
	public static List<VisitDetailed> generateVisitDetailed(Long howMany) {
		List<VisitDetailed> visitDetailedEntities = new ArrayList<>();
		Long currentGenerationNumber = 1L;
		Long patientRoomIncrement = 1L;
		Long patientRoom = PATIENT_ROOM_BASE;
		String bed = "";
				
		for(; currentGenerationNumber<=howMany; currentGenerationNumber++) {
			VisitDetailed visitDetailed = new VisitDetailed();
			
			
			if(currentGenerationNumber%2L > 0  ) {
				patientRoom = PATIENT_ROOM_BASE + patientRoomIncrement;
				patientRoomIncrement++;
				bed = "1";
			} else {
				bed = "2";
			}
			
			visitDetailed.setVisitNb(VISIT_NUMBER_BASE + currentGenerationNumber);
			visitDetailed.setAdmitDate(new Date());
			visitDetailed.setDischargeDate(new Date());
			visitDetailed.setExpDischargeDate(new Date());
			visitDetailed.setPatientClass("ALIT");
			visitDetailed.setPatientClassDesc("Clinique de jour");
			visitDetailed.setPatientType("CM");
			visitDetailed.setPatientTypeDesc("Contrat Maladie - Convention avec la clinique");
			visitDetailed.setPatientCase("CHG");
			visitDetailed.setPatientCaseDesc("Chirurgie Générale");
			visitDetailed.setHospService("U2S");
			visitDetailed.setHospServiceDesc("Unité 2è sud");
			visitDetailed.setAdmissionType("CHG");
			visitDetailed.setAdmissionTypeDesc("Chirurgie Générale");
			visitDetailed.setPatientUnit("2S");
			visitDetailed.setPatientUnitDesc("2ème étage Sud chirurgie");
			visitDetailed.setPatientRoom(patientRoom.toString());
			visitDetailed.setPatientBed(bed);
			visitDetailed.setPriorUnit("PRIO");
			visitDetailed.setPriorUnitDesc("PRIO Desc");
			visitDetailed.setAdmitSource("ANN");
			visitDetailed.setAdmitSourceDesc("Annoncé");
			visitDetailed.setFinancialClass("FClass");
			visitDetailed.setFinancialClassDesc("F Class Desc");
			visitDetailed.setHl7code("O");
			
			visitDetailed.setCompany(new CompanyReference(5, "Clinique de Genolier", "CDG"));
			
			visitDetailed.setPatient(new PatientReference(PATIENT_ID_BASE + currentGenerationNumber, "FirstName " + currentGenerationNumber, "LastName " + currentGenerationNumber, "MaidenName " + currentGenerationNumber, new Date()));
			
			visitDetailedEntities.add(visitDetailed);
		}
		
		return visitDetailedEntities;
	}
	
	public static VisitDetailed generatevisitDetailed() {
		return generateVisitDetailed(1L).get(0);
	}
}

