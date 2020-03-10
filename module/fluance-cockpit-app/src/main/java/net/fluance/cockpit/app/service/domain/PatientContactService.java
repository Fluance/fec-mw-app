package net.fluance.cockpit.app.service.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import net.fluance.cockpit.core.model.jdbc.patient.PatientContact;
import net.fluance.cockpit.core.model.search.FtSearchModel;
import net.fluance.cockpit.core.model.wrap.patient.Patient;
import net.fluance.cockpit.core.repository.jdbc.patient.PatientContactRepository;
import net.fluance.cockpit.core.util.PhoneUtils;

@Service
public class PatientContactService {	
	private static Logger LOGGER = LogManager.getLogger(PatientContactService.class);
	
	private static final String SOLR_ENTITY_TYPE = "patient";
	private static final Integer SOLR_OFFSET = 0;
	private static final Integer SOLR_GROUP_LIMIT = 10;
	private static final String SOLR_RESPONSE_PID = "id";
	
	@Autowired
	PatientContactRepository patientContactRepository;
	
	@Autowired
	PatientService patientService;
	
	@Autowired
	private FtSearchService ftSearchService;
	
	/**
	 * Search patients by the PatientContact table
	 * 
	 * @param telephone
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public Patient getPatientByContact(String telephone) throws JsonProcessingException, IOException {
		String newPhone = PhoneUtils.getCleanPhoneNumber(telephone);
		
		List<PatientContact> patients = patientContactRepository.findByContact(telephone, newPhone);
		if(patients == null || patients.isEmpty()) {
			return null;
		}
		return patientService.patientDetail(patients.get(0).getPid());
	}
	
	/**
	 * Gets the patients that match the given telephone. Spaces and not number characters will be remove
	 * 
	 * @param telephone
	 * @return
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public List<Patient> getPatientByTelephone(String telephone) throws JsonProcessingException, IOException {
		List<Patient> patients = new ArrayList<>();
		
		String newPhone = PhoneUtils.getCleanPhoneNumber(telephone);
		
		if(!StringUtils.isEmpty(newPhone)) {
			FtSearchModel searchResult = findByTelehoneOnSolr(newPhone);
			
			if(searchResult != null) {
				getPidsfromFtSearchModel(searchResult).stream().forEach((pid) -> {
					Patient patient = patientService.patientDetail(pid);
					if(patient!=null) {
						patients.add(patient);
					}
				});	
			}
		}
		return patients;
	}
	
	/**
	 * Launch the solr request to search by telephone.
	 * Uses {@link FtSearchService}
	 * 
	 * @param telephone
	 * @return
	 */
	private FtSearchModel findByTelehoneOnSolr(String telephone) {
		FtSearchModel ftSearchModel = null;
		try {
			ftSearchModel = ftSearchService.searchTelephone(telephone, SOLR_OFFSET, SOLR_GROUP_LIMIT, SOLR_ENTITY_TYPE);
		} catch (Exception e) {
			LOGGER.warn("Error getting Solr response");
		}
		
		return ftSearchModel;
	}
	
	/**
	 * Iterates over the results of the solr search to get the list of pid
	 * 
	 * @param ftSearchModel
	 * @return
	 */
	private List<Long> getPidsfromFtSearchModel(FtSearchModel ftSearchModel){
		List<Long> pidsFromSearch = new ArrayList<>();
		if(ftSearchModel.getGroups()!=null && ftSearchModel.getGroups().size()>0) {
			ftSearchModel.getGroups().stream().forEach((ftSearchGroupsResult) ->{
				if(ftSearchGroupsResult.getCount() > 0) {
					JsonNode patientNode;
					Iterator<JsonNode> nodes= ftSearchGroupsResult.getData().elements();
					while (nodes.hasNext()) {
						patientNode = nodes.next();
						
						if(patientNode.get(SOLR_RESPONSE_PID)!=null) {
							pidsFromSearch.add(patientNode.get(SOLR_RESPONSE_PID).asLong());
						}
					}
				}
			});
		}
		return pidsFromSearch;
	}
}
