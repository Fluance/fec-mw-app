package net.fluance.cockpit.app.service.domain;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.fluance.cockpit.core.model.jdbc.company.BedList;
import net.fluance.cockpit.core.model.jdbc.company.Capacity;
import net.fluance.cockpit.core.model.jdbc.company.CompanyDetails;
import net.fluance.cockpit.core.model.jdbc.company.CompanyMetadata;
import net.fluance.cockpit.core.model.jdbc.company.RoomList;
import net.fluance.cockpit.core.model.jdbc.company.RoomOnlyList;
import net.fluance.cockpit.core.model.jdbc.company.ServiceList;
import net.fluance.cockpit.core.repository.jdbc.company.BedListRepository;
import net.fluance.cockpit.core.repository.jdbc.company.CapacityRepository;
import net.fluance.cockpit.core.repository.jdbc.company.CompanyDetailsRepository;
import net.fluance.cockpit.core.repository.jdbc.company.CompanyMetadataRepository;
import net.fluance.cockpit.core.repository.jdbc.company.RoomListRepository;
import net.fluance.cockpit.core.repository.jdbc.company.RoomOnlyListRepository;
import net.fluance.cockpit.core.repository.jdbc.company.ServiceListRepository;

@Component
public class CompanyService {

	@Autowired
	ServiceListRepository serviceListRepository;

	@Autowired
	RoomListRepository roomListRepository;
	
	@Autowired
	RoomOnlyListRepository roomOnlyListRepository;

	@Autowired
	BedListRepository bedListRepository;
	
	@Autowired
	CompanyMetadataRepository companyMetadataRepository;
	
	@Autowired
	CompanyDetailsRepository companyDetailsRepository;
	
	@Autowired
	CapacityRepository capacityRepository;

	public List<ServiceList> getServiceList(Integer companyId, String patientunit, List<String> hospServices) {
		// TODO : INVESTIGATE WHY COLUMN patientunit DOESNT EXIST
		// if (patientunit == null){
		if(hospServices != null && !hospServices.isEmpty()){
			return serviceListRepository.findByCompanyIdWithCount(companyId, hospServices);
		} else {
			return serviceListRepository.findByCompanyId(companyId);
		}
		// } else {
		// return serviceListRepository.findByCompanyIdAndPatientUnit(companyId,
		// patientunit);
		// }
	}
	
	public List<RoomList> getRoomList(Integer companyId, String patientunit, String patientclass, String hospservice, Boolean occupancy, List<String> excluderooms) {

		if (patientunit != null && !patientunit.isEmpty() && hospservice != null && !hospservice.isEmpty()) {
			return roomListRepository.findByCompanyIdAndPatientunitAndHospserviceIncludeCount(companyId, patientunit, patientclass, hospservice, excluderooms);
		} else if (patientunit != null && !patientunit.isEmpty()) {
			return roomListRepository.findByCompanyIdAndPatientunitIncludeCount(companyId, patientunit, patientclass, excluderooms);
		} else if (hospservice != null && !hospservice.isEmpty()) {
			return roomListRepository.findByCompanyIdAndHospserviceIncludeCount(companyId, hospservice, patientclass, excluderooms);
		} else {
			throw new IllegalArgumentException("Parameters patientunit or hospservice or both MUST be present with companyId");
		}


	}
	public List<RoomOnlyList> getRoomOnlyList(Integer companyId, String patientunit, String hospservice, List<String> excluderooms) {

		if (patientunit != null && !patientunit.isEmpty() && hospservice != null && !hospservice.isEmpty()) {
			return roomOnlyListRepository.findByCompanyIdAndPatientunitAndHospservice(companyId, patientunit, hospservice, excluderooms);
		} else if (patientunit != null && !patientunit.isEmpty()) {
			return roomOnlyListRepository.findByCompanyIdAndPatientunit(companyId, patientunit, excluderooms);
		} else if (hospservice != null && !hospservice.isEmpty()) {
			return roomOnlyListRepository.findByCompanyIdAndHospservice(companyId, hospservice, excluderooms);
		} else {
			throw new IllegalArgumentException("Parameters patientunit or hospservice or both MUST be present with companyId");
		}

	}


	public List<BedList> getBedList(Integer companyId, String patientroom, String patientunit, String hospservice) {
		if (patientunit != null && !patientunit.isEmpty() && hospservice != null && !hospservice.isEmpty()) {
			return bedListRepository.findCompanyIdAndPatientunitAndHospserviceAndPatientroom(companyId, patientunit,
					hospservice, patientroom);
		} else if (patientunit != null && !patientunit.isEmpty()) {
			return bedListRepository.findCompanyIdAndPatientUnitAndPatientroom(companyId, patientunit, patientroom);
		} else if (hospservice != null && !hospservice.isEmpty()) {
			return bedListRepository.findCompanyIdAndHospserviceAndpatientroom(companyId, hospservice, patientroom);
		} else {
			throw new IllegalArgumentException("Parameters patientunit or hospservice or both MUST be present with companyId and patientroom");
		}
	}
	
	/**
	 * Set Company Logo path
	 * @param companyDetails 
	 * @return The {@link CompanyDetails} given by argument with the 'logo' path added if it exists
	 */
	public CompanyDetails setCompanyLogoPath(CompanyDetails companyDetails) {
		
		if (companyDetails != null && companyDetails.getId() != null ) {
			CompanyMetadata companyMetadata = companyMetadataRepository.findCompanyMetadataByIdAndTitle(companyDetails.getId(), "logo");
		
			if(companyMetadata != null) {
				companyDetails.setLogo(companyMetadata.getName());
			}
		}
		
		return companyDetails;
	}
	
	/**
	 * Given a Company ID, returns the {@link CompanyDetails} with the detailed info
	 * @param companyId
	 * @return <ul><li>A {@link CompanyDetails} instance with the Company Info if the ID exists.</li><li>If the Company does not exist, the return value is null</li></ul>
	 */
	public CompanyDetails find(Long companyId){
		CompanyDetails companyDetails = companyDetailsRepository.findOne(companyId.intValue());
		return companyDetails;
	}

	/**
	 * Returns a {@link Capacity} list which elements contain the Company ID and the Patient Unit or the Hosp Service (one of them or both)
	 * @param companyId
	 * @param patientUnit
	 * @param hospService
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<Capacity> getCapacity(Integer companyId, String patientUnit, String hospService, Integer limit, Integer offset) {
		if((patientUnit == null || patientUnit.isEmpty()) && (hospService == null || hospService.isEmpty())){
			throw new IllegalArgumentException("Parameters patientunit or hospservice or both MUST be present with companyId ");
		}		
		return capacityRepository.getCapacity(companyId, patientUnit, hospService, limit, offset);
	}
}
