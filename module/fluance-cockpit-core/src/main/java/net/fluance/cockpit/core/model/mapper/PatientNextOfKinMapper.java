package net.fluance.cockpit.core.model.mapper;

import java.util.ArrayList;

import net.fluance.cockpit.core.model.dto.PatientNextOfKinDto;
import net.fluance.cockpit.core.model.jdbc.patient.PatientNextOfKin;

/**
 * Controlled conversion between {@code PatientNextOfKinDto} and {@code PatientNextOfKin}
 *
 */
public class PatientNextOfKinMapper {
	
	/**
	 * Converts a {@code PatientNextOfKin} instance in a equivalent {@code PatientNextOfKinDto} instance
	 * @param entity The {@code PatientNextOfKin} instance
	 * @return A {@code PatientNextOfKinDto} with the same info that the entity sorted
	 */
	public static PatientNextOfKinDto toModel(PatientNextOfKin entity) {
		
		PatientNextOfKinDto dto = new PatientNextOfKinDto();
		dto.setEmails(new ArrayList<>());
		dto.setTelephones(new ArrayList<>());
		
		dto.setAddress(entity.getAddress());
		dto.setAddress2(entity.getAddress2());
		dto.setAddressType(entity.getAddressType());
		dto.setCanton(entity.getCanton());
		dto.setCareOf(entity.getCareOf());
		dto.setComplement(entity.getComplement());
		dto.setCountry(entity.getCountry());
		
		if(entity!=null && entity.getEquipment().equals("internet_address")) {
			dto.getEmails().add(entity.getData());
		}
		
		dto.setFirstName(entity.getFirstName());
		dto.setId(entity.getId());
		dto.setLastName(entity.getLastName());
		dto.setLocality(entity.getLocality());
		dto.setNbRecords(entity.getNbRecords());
		dto.setPatientId(entity.getPatientId());
		dto.setPostCode(entity.getPostCode());
		
		if(entity!=null && (entity.getEquipment().equals("telephone") || entity.getEquipment().equals("cellular_phone"))) {
			dto.getTelephones().add(entity.getData());
		}
		
		dto.setType(entity.getType());
		
		return dto;
	}
}
