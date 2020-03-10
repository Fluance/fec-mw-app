package net.fluance.cockpit.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ComponentScan("net.fluance.cockpit.app")
public class ProviderConfig {
	
	@Value("${provider.opal.id}")
	protected String providerOpalId;
	@Value("${provider.polypoint.id}")
	protected String providerPolypointId;
	@Value("${provider.synlab.id}")
	protected String providerSynlabId;

	public Integer getProviderOpalId() {
		return Integer.valueOf(providerOpalId);
	}
	
	public Integer getProviderPolypointId() {
		return Integer.valueOf(providerPolypointId);
	}

	public Integer getProviderSynlabId() {
		return Integer.valueOf(providerSynlabId);
	}
}