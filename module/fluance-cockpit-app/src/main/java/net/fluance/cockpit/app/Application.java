package net.fluance.cockpit.app;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableCaching
@EnableJpaRepositories(basePackages={"net.fluance.cockpit.core.repository.jpa"})
@EntityScan(basePackages={"net.fluance.cockpit.core.model.jpa"})
@SpringBootApplication(scanBasePackages={"net.fluance.cockpit.app", "net.fluance.cockpit.core", "net.fluance.cockpit.solr","net.fluance.cockpit.app.config.websocket","net.fluance.cockpit.app.config.cron"})
@EnableAsync
public class Application extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

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
}
