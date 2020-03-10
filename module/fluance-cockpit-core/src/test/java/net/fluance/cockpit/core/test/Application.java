package net.fluance.cockpit.core.test;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import net.fluance.app.security.service.UserProfileLoader;
import net.fluance.app.security.util.OAuth2Helper;

@EnableJpaRepositories("net.fluance.cockpit.core")
@EntityScan("net.fluance.cockpit.core")
@SpringBootApplication(scanBasePackages={"net.fluance.cockpit.core"})
public class Application {
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		DataSource debug = DataSourceBuilder.create().build();
		return debug;
	}

	@Bean(name = "ehlogdataSource")
	@ConfigurationProperties(prefix = "spring.ehlogdataSource")
	public DataSource ehlogdataSource() {
		DataSource debug = DataSourceBuilder.create().build();
		return debug;
	}
	
	@Bean
	public UserProfileLoader userProfileLoader(){
		return new UserProfileLoader();
	}
	
	@Bean
	public OAuth2Helper oAuth2Helper() {
		return new OAuth2Helper();
	}
}
