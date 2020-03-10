package net.fluance.cockpit.app.service.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.VCardVersion;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Address;
import ezvcard.property.Revision;
import ezvcard.property.StructuredName;
import ezvcard.property.Uid;
import net.fluance.cockpit.core.model.jdbc.patient.PatientContact;
import net.fluance.cockpit.core.model.jdbc.physician.Physician;
import net.fluance.cockpit.core.model.wrap.patient.Patient;
import net.fluance.cockpit.core.model.wrap.patient.PatientAddress;
import net.fluance.cockpit.core.repository.jdbc.physician.PhysicianRepository;

@Component
public class VCardService {

	@Autowired
	private PatientService patientService;
	
	@Autowired
	private PhysicianRepository physicianRepository;

	public String getPatientContact(long pid){
		VCard vcard = new VCard();
		Patient patient = patientService.patientDetail(pid);
		if(patient != null){
			StructuredName structuredName = new StructuredName();
			structuredName.setFamily(patient.getPatientInfo().getLastName());
			structuredName.setGiven(patient.getPatientInfo().getFirstName());
			structuredName.getPrefixes().add(patient.getPatientInfo().getCourtesy());
			vcard.setStructuredName(structuredName);
			PatientAddress address = patient.getAddress();
			Address adr = new Address();
			adr.setLocality(address.getLocality());
			adr.setCountry(address.getCountry());
			adr.setPostalCode(address.getPostCode());
			adr.setStreetAddress(address.getAddressLine());
			adr.setRegion(address.getCanton());
			adr.setExtendedAddress(address.getAdressComplement());
			vcard.addAddress(adr);
			List<PatientContact> contacts = patient.getContacts();
			if(contacts != null && !contacts.isEmpty()){
				for (int i = 0; i < contacts.size(); i++) {
					PatientContact contact = contacts.get(i);
					switch(contact.getEquipment()){
						case "cellular_phone": vcard.addTelephoneNumber(contact.getData(), TelephoneType.CELL);
						break;
						default: vcard.addTelephoneNumber(contact.getData());
						break;
					}
				}
			}
		}
		vcard.setUid(Uid.random());
		vcard.setRevision(Revision.now());
		return Ezvcard.write(vcard).version(VCardVersion.V3_0).go();
	}

	public String getPhysicianContact(Integer id){
		VCard vcard = new VCard();
		Physician physician = physicianRepository.findPhysicianByid(id);
		if(physician != null){
			StructuredName structuredName = new StructuredName();
			structuredName.setGiven(physician.getFirstname());
			structuredName.setFamily(physician.getLastname());
			if(physician.getPrefix() != null){
				structuredName.getPrefixes().add(physician.getPrefix());
			}
			vcard.setStructuredName(structuredName);
			Address adr = new Address();
			adr.setLocality(physician.getLocality());
			adr.setCountry(physician.getCountry());
			adr.setPostalCode(physician.getPostCode());
			adr.setStreetAddress(physician.getAddress());
			adr.setRegion(physician.getCanton());
			adr.setExtendedAddress(physician.getComplement());
			vcard.addAddress(adr);
			vcard.setCategories(physician.getPhySpecialityDesc());
		}
		vcard.setUid(Uid.random());
		vcard.setRevision(Revision.now());
		return Ezvcard.write(vcard).version(VCardVersion.V3_0).go();
	}
}
