package net.fluance.cockpit.app.service.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.GroupResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.noggit.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.fluance.cockpit.core.model.search.FtSearchGroup;
import net.fluance.cockpit.core.model.search.FtSearchModel;
import net.fluance.cockpit.core.model.search.FtSearchModelOld;
import net.fluance.cockpit.core.repository.jdbc.visit.VisitListRepository;
import net.fluance.cockpit.solr.SolrSearchService;
import net.fluance.cockpit.solr.SolrSearchTelephoneService;

@Service
public class FtSearchService {
	
	@Autowired
	private SolrSearchService solrSearchService;
	
	@Autowired
	private SolrSearchTelephoneService solrSearchTelephoneService;
	
	@Autowired
	PatientService patientService;

	@Autowired
	VisitListRepository visitListRepository;

	public FtSearchModelOld search(String keyWord, Integer offset, Integer limit, String entityType) throws Exception {
		QueryResponse response = solrSearchService.searchOld(keyWord, offset, limit, entityType);
		
		//Build Data
		SolrDocumentList results = response.getResults();
		String returnValue = JSONUtil.toJSON(results);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode data = mapper.readTree(returnValue);

		// Build Facets
		List<FacetField> facetFields = response.getFacetFields();
		Map<String, Long> facets = new HashMap<>();
		for (int i=0; i<facetFields.get(0).getValues().size(); i++){
			facets.put(facetFields.get(0).getValues().get(i).getName(), facetFields.get(0).getValues().get(i).getCount());
		}
		
		// Prepare Response Payload
		FtSearchModelOld result = new FtSearchModelOld(results.getNumFound(), offset, limit, data, facets);
		return result;
	}
	
	public FtSearchModel search2(String keyWord, Integer offset, Integer limit, String entityType) throws Exception {
		QueryResponse response = solrSearchService.search(keyWord, offset, entityType, limit);
		GroupResponse groups = response.getGroupResponse();
		List<FtSearchGroup> ftSearchGroups = new ArrayList<>();
		long totalCount = 0;
		for (GroupCommand group : groups.getValues()){
			totalCount = group.getMatches();
			for ( Group g : group.getValues()){
				String groupEntityType = g.getGroupValue();
				long groupNumFound = g.getResult().getNumFound();
				String returnValue = JSONUtil.toJSON(g.getResult());
				ObjectMapper mapper = new ObjectMapper();
				JsonNode data = mapper.readTree(returnValue);
				FtSearchGroup ftSearchGroup = new FtSearchGroup(groupNumFound, groupEntityType, data);
				ftSearchGroups.add(ftSearchGroup);
			}
		}
		FtSearchModel result = new FtSearchModel(totalCount, offset, limit, ftSearchGroups);
		return result;
	}
	
	/**
	 * Serach Patients with the one of the telephones given as parameter. The telephones must be separated by whitespaces
	 * @param telephone The list of telephones
	 * @param offset
	 * @param limit
	 * @param entityType Entity type where the search is going to be done. By default is 'patient'
	 * @return
	 * @throws Exception
	 */
	public FtSearchModel searchTelephone(String telephone, Integer offset, Integer limit, String entityType) throws Exception {
		QueryResponse response = solrSearchTelephoneService.searchTelephone(telephone, offset, entityType, limit);
		GroupResponse groups = response.getGroupResponse();
		List<FtSearchGroup> ftSearchGroups = new ArrayList<>();
		long totalCount = 0;
		for (GroupCommand group : groups.getValues()){
			totalCount = group.getMatches();
			for ( Group g : group.getValues()){
				String groupEntityType = g.getGroupValue();
				long groupNumFound = g.getResult().getNumFound();
				String returnValue = JSONUtil.toJSON(g.getResult());
				ObjectMapper mapper = new ObjectMapper();
				JsonNode data = mapper.readTree(returnValue);
				FtSearchGroup ftSearchGroup = new FtSearchGroup(groupNumFound, groupEntityType, data);
				ftSearchGroups.add(ftSearchGroup);
			}
		}
		FtSearchModel result = new FtSearchModel(totalCount, offset, limit, ftSearchGroups);
		return result;
	}
}
