package net.fluance.cockpit.solr;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolrConfig {

	@Value("${search.solr.server.url}")
	String solrServerUrl;

	@Value("${search.solr.server.userName}")
	String solrServerUserName;

	@Value("${search.solr.server.pwd}")
	String solrServerPwd;

	@Bean
	public SolrClient solrClient() {
		CredentialsProvider provider = new BasicCredentialsProvider();
		Credentials credentials = new UsernamePasswordCredentials(solrServerUserName, solrServerPwd);
		provider.setCredentials(AuthScope.ANY, credentials);

		HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

		return new HttpSolrClient.Builder(solrServerUrl).withHttpClient(client).build();
	}
}
