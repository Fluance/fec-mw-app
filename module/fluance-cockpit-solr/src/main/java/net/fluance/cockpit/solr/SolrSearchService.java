package net.fluance.cockpit.solr;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolrSearchService {
	
	Logger logger = LogManager.getLogger();
	
	private static final String VALID_CHARS = "[^A-Za-z0-9\\.\\-]";
	
	//Match the most accurate to dd.MM.YYYY
	private static final String SWISS_DATE_REGX = "^[0,1,2,3]{1}\\d{1}\\.[0,1]{1}\\d{1}\\.\\d{4}\\z";
	
	//Create a local instance of the SimpleDateFormat objects to avoid concurrence problems
	private static final String SOLR_DATE_PATTERN = "yyyy-MM-dd";
	private static SimpleDateFormat SOLR_DATE_FORMAT;
	
	private static final String SWISS_DATE_PATTERN = "dd.MM.yyyy";
	private static SimpleDateFormat SWISS_DATE_FORMAT;
	
	static {
		SOLR_DATE_FORMAT = new SimpleDateFormat(SOLR_DATE_PATTERN);
		SOLR_DATE_FORMAT.setLenient(false);
		
		SWISS_DATE_FORMAT = new SimpleDateFormat(SWISS_DATE_PATTERN);
		SWISS_DATE_FORMAT.setLenient(false);
	}
	
	@Autowired
	private SolrClient solrClient;

	public QueryResponse searchOld(String entry, Integer offset, Integer limit, String entityType) {
		// TODO : Use the Limit and the Offset
		// TODO : If the filter param entityType is not Null then use it.
		SolrQuery query = new SolrQuery();
		entry=StringUtils.stripAccents(entry).replaceAll(VALID_CHARS, " ");
		String[] words = entry.split("\\s+");
		for (String word : words) {
			query.add("fq", "content:".concat(prepareContent(word)));
		}
		if (entityType!=null && !entityType.isEmpty()){
			query.add("fq", "entityType:".concat(entityType));
		}
	
		query.setParam("q", "*:*");		
		query.setParam("rows", limit + "");
		query.setParam("start", offset + "");
		query.setParam("sort","admitDate desc");
		query.setParam("facet", "on");
		query.setParam("indent", "on");
		query.setParam("indent", "javabin");
		query.addFacetField("entityType");
		
		QueryResponse response = new QueryResponse();
		try {
			response = solrClient.query(query);
			logger.info(response.getResults());
			logger.info(response.getFacetFields());
		} catch (SolrServerException | IOException ex) {
			logger.error("", ex);
		}
		return response;

	}

	public QueryResponse search(String entry, Integer offset, String entityType, Integer groupLimit) {
		SolrQuery query = new SolrQuery();
		entry=StringUtils.stripAccents(entry).replaceAll(VALID_CHARS, " ");
		String[] words = entry.split("\\s+");
		for (String word : words) {
			query.add("fq", "content:".concat(prepareContent(word)));
		}
		if (entityType!=null && !entityType.isEmpty()){
			query.add("fq", "entityType:".concat(entityType));
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
			logger.info(response.getResults());
			logger.info(response.getFacetFields());
		} catch (SolrServerException | IOException ex) {
			logger.error("", ex);
		}
		return response;

	}
	
	/**
	 * Test if the word needs to be treat and add all the chars that are need to prepare the content
	 * 
	 * @param word
	 * @return
	 */
	private String prepareContent(String word) {
		//It's possible that it be a Swiss Date
		if(word.matches(SWISS_DATE_REGX)) {
			word = formatStringDate(word, SWISS_DATE_FORMAT, SOLR_DATE_FORMAT);
		}
		
		word = "\"".concat(word).concat("\"");
		
		return word;
	}
	
	/**
	 * Format the String to the output format. A format for the input String is needed to do the previous conversion to a Date object.
	 * 
	 * @param strDate
	 * @param inputFormat
	 * @param outputFormat
	 * @return
	 */
	private String formatStringDate(String strDate, SimpleDateFormat inputFormat, SimpleDateFormat outputFormat){
		Date date;
		try {
			date = inputFormat.parse(strDate);
			strDate = outputFormat.format(date);
		} catch (ParseException e) {
			//don't throw error only return the same string
			logger.warn("The date don't match the format "+inputFormat.toPattern());
		}
		
		return strDate;
	}
	
}
