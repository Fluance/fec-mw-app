package net.fluance.cockpit.core.util.visit;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import net.fluance.cockpit.core.model.jpa.company.CompanyEntity;
import net.fluance.cockpit.core.model.jpa.visit.VisitDetailEntity;
import net.fluance.cockpit.core.model.jpa.visit.VisitDetailedEntity;


public class VisitDetailedMock {
	
	private static final Long VISIT_NUMBER_BASE = 1000000L;
	private static final Long PATIENT_ID_BASE = 1000L;
	private static final Long PATIENT_ROOM_BASE = 100L;
	
	public static List<VisitDetailedEntity> generateVisitDetailedEntities(Long howMany) {
		List<VisitDetailedEntity> visitDetailedEntities = new ArrayList<>();
		Long currentGenerationNumber = 1L;
		Long patientRoomIncrement = 1L;
		Long patientRoom = PATIENT_ROOM_BASE;
		String bed = "";
				
		for(; currentGenerationNumber<=howMany; currentGenerationNumber++) {
			VisitDetailedEntity visitDetailedEntity = new VisitDetailedEntity();
			VisitDetailEntity visitDetailEntity = new VisitDetailEntity();
			CompanyEntity companyEntity = new CompanyEntity();
			
			if(currentGenerationNumber%2L > 0  ) {
				patientRoom = PATIENT_ROOM_BASE + patientRoomIncrement;
				patientRoomIncrement++;
				bed = "1";
			} else {
				bed = "2";
			}
			
			visitDetailedEntity.setVisitNumber(VISIT_NUMBER_BASE + currentGenerationNumber);
			visitDetailedEntity.setAdmitDate(Timestamp.valueOf(LocalDateTime.now()));
			visitDetailedEntity.setDischargeDate(Timestamp.valueOf(LocalDateTime.now().plusDays(4)));
			visitDetailedEntity.setPatientClass("ALIT");
			visitDetailedEntity.setPatientCase("CHG");
			visitDetailedEntity.setHospService("U2S");
			visitDetailedEntity.setAdmissionTypeDesc("Chirurgie Générale");
			visitDetailedEntity.setPatientUnit("2S");
			visitDetailedEntity.setPatientUnitDesc("2ème étage Sud chirurgie");
			visitDetailedEntity.setPatientRoom(patientRoom.toString());
			visitDetailedEntity.setPatientBed(bed);
			visitDetailedEntity.setFinancialClass("FClass");
			visitDetailedEntity.setFinancialClassDesc("F Class Desc");			
			visitDetailedEntity.setHl7Code("O");			
			visitDetailedEntity.setCompanyId(5);
			visitDetailedEntity.setPatientId(PATIENT_ID_BASE + currentGenerationNumber);						
			visitDetailedEntity.setFirstName("FirstName " + currentGenerationNumber);			
			visitDetailedEntity.setLastName("FirstName " + currentGenerationNumber);
			visitDetailedEntity.setMaidenName("MaidenName " + currentGenerationNumber);			
			visitDetailedEntity.setBirthdate(Date.valueOf(LocalDate.now()));
			
			visitDetailEntity.setVisitNumber(VISIT_NUMBER_BASE + currentGenerationNumber);
			visitDetailEntity.setAdmitDate(Timestamp.valueOf(LocalDateTime.now()));
			visitDetailEntity.setDischargeDate(Timestamp.valueOf(LocalDateTime.now().plusDays(4)));
			visitDetailEntity.setExpectedDischargeDate(Timestamp.valueOf(LocalDateTime.now().plusDays(5)));
			visitDetailEntity.setPatientClass("ALIT");			
			visitDetailEntity.setPatientClassDesc("Clinique de jour");			
			visitDetailEntity.setPatientType("CM");
			visitDetailEntity.setPatientTypeDesc("Contrat Maladie - Convention avec la clinique");
			visitDetailEntity.setPatientCase("CHG");
			visitDetailEntity.setPatientCaseDesc("Chirurgie Générale");
			visitDetailEntity.setHospService("U2S");
			visitDetailEntity.setHospServiceDesc("Unité 2è sud");			
			visitDetailEntity.setAdmissionType("CHG");
			visitDetailEntity.setAdmissionTypeDesc("Chirurgie Générale");
			visitDetailEntity.setPatientUnit("2S");			
			visitDetailEntity.setPatientUnitDesc("2ème étage Sud chirurgie");
			visitDetailEntity.setPatientRoom(patientRoom.toString());
			visitDetailEntity.setPatientBed(bed);			
			visitDetailEntity.setPriorUnit("PRIO");
			visitDetailEntity.setPriorUnitDesc("PRIO Desc");
			visitDetailEntity.setAdmitSource("ANN");			
			visitDetailEntity.setAdmitSourceDesc("Annoncé");
			visitDetailEntity.setCompanyId(5);
			visitDetailEntity.setPatientId(PATIENT_ID_BASE + currentGenerationNumber);
						
			companyEntity.setCompanyId(5);
			companyEntity.setName("Clinique de Genolier");
			companyEntity.setCode("CDG");
						
			visitDetailedEntity.setVisitDetail(visitDetailEntity);
			visitDetailedEntity.setCompany(companyEntity);
			
			visitDetailedEntities.add(visitDetailedEntity);
		}
		
		return visitDetailedEntities;
	}
	
	public static VisitDetailedEntity generateVisitDetailedEntity() {
		return generateVisitDetailedEntities(1L).get(0);
	}
}
