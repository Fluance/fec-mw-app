package net.fluance.cockpit.solr.test;

import org.apache.solr.client.solrj.SolrServerException;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.fluance.app.test.AbstractIntegrationTest;
import net.fluance.cockpit.solr.SolrSearchService;

@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("net.fluance.cockpit.app")
@TestPropertySource(locations = "classpath:test.properties")
public class SolrSearchServiceTest extends AbstractIntegrationTest {
	
	@Autowired
	private SolrSearchService solrSearchService;

	/**
	 * @throws SolrServerException 
	 */
	@Test
	@Ignore
	public void testApp(){
		solrSearchService.searchOld("test", 0, 100, "");
	}
	
	/**
	 * Anouther Rigourous Test for Lionel:-)
	 * @throws Exception 
	 * @throws SolrServerException 
	 */
	@Test
	@Ignore
	public void testApp2() throws Exception{
		solrSearchService.search("test", 1, "", 3);
	}
}
