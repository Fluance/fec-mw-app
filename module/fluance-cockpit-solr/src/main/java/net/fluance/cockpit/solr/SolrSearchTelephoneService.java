package net.fluance.cockpit.solr;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Sends a query to the Solr Client to search Patients with a matching telephone.
 *
 */
@Service
public class SolrSearchTelephoneService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SolrSearchTelephoneService.class);
	
	private static final String NOT_VALID_CHARS = "[^A-Za-z0-9\\.\\-]";
	private static final String REG_EXP_WHITESPACES = "\\s+";
	
	private static final String DEFAULT_ENTITY_TYPE = "patient";
	private static final String SEARCH_FIELD = "phoneNumber";

	@Autowired
	private SolrClient solrClient;
	
	/**
	 * Prepare each telephone to search inside records with more than one number
	 * @param telephone
	 * @return
	 */
	private String prepareContent(String telephone) {
		telephone = "*".concat(telephone).concat("*").concat(" ");
		return telephone;
	}
	
	/**
	 * Prepare the telephones to search to create the Solr Query and send it to the Solr Server
	 * @param telephones A String with the telephones separated by whitespaces "tel1 tel2 tel3"
	 * @param offset
	 * @param entityType 'patient' by default
	 * @param groupLimit
	 * @return
	 */
	public QueryResponse searchTelephone(String telephones, Integer offset, String entityType, Integer groupLimit) {
		
		SolrQuery query = new SolrQuery();
		
		telephones=StringUtils.stripAccents(telephones).replaceAll(NOT_VALID_CHARS, " ");
		
		String[] searching = telephones.split(REG_EXP_WHITESPACES);
		
		String condition = new String();
		for (String telephone : searching) {
			condition = condition + prepareContent(telephone);
		}
		
		query.addFilterQuery(SEARCH_FIELD + ":" + StringUtils.chop(condition));
		
		if (entityType!=null && !entityType.isEmpty()){
			query.addFilterQuery("entityType:".concat(entityType));
		} else {
			query.addFilterQuery("entityType:"+DEFAULT_ENTITY_TYPE);
		}
		
		query.setParam("group", true);
		query.setParam("group.field", "entityType");
		query.setParam("group.limit", groupLimit + "");
		query.setParam("group.offset", offset + "");
	
		query.setParam("q", "*:*");		
		query.setParam("sort","admitDate desc");		
		query.setParam("indent", "on");
		query.setParam("indent", "javabin");
		
		QueryResponse response = new QueryResponse();
		try {
			response = solrClient.query(query);
		} catch (SolrServerException | IOException ex) {
			LOGGER.error("", ex);
		}
		return response;

	}
}
