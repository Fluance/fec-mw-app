package net.fluance.cockpit.solr.indicator;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.request.SolrPing;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name="management.health.solr.ping.enabled", havingValue="true", matchIfMissing=false)
public class SolrPingHealthIndicator extends AbstractHealthIndicator {
	
	@Autowired
    @Qualifier(value="solrClient")
	SolrClient solrClient;


	@Override
	protected void doHealthCheck(Builder builder) throws Exception {
		
		SolrPing ping = new SolrPing();
        SolrPingResponse rsp = ping.process(solrClient, null);
        if(rsp != null) {
	        int statusCode = rsp.getStatus();  
	        Status status = (statusCode == 0 ? Status.UP : Status.DOWN);
	        builder.status(status).withDetail("solrStatus", (statusCode == 0 ? "OK" : statusCode));
        } else {
        	builder.down();
        }

	}

	

}
