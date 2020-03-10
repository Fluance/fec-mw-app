/**
 * 
 */
package net.fluance.cockpit.core.test;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("net.fluance.cockpit.core")
@EnableJpaRepositories("net.fluance.cockpit.core")
@EnableAutoConfiguration
@PropertySource({"classpath:webapps/conf/mw-app/application.properties"})
public class TestConfig {
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfig() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
}
