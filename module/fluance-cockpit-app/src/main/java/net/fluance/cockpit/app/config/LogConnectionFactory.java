package net.fluance.cockpit.app.config;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnection;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.mock.env.MockEnvironment;

import net.fluance.commons.codec.Base64Utils;

public class LogConnectionFactory {
    private static interface Singleton {
        final LogConnectionFactory INSTANCE = new LogConnectionFactory();
    }

    private DataSource ehLogDataSource;
    
    private String PROFILE_KEY = "spring.profiles.active";
    private String URL_CONFIG_SERVER_KEY = "spring.cloud.config.uri";
    private String AUTHORIZATION_CONFIG_SERVER_KEY = "spring.cloud.config.headers.authorization";
    private String APPLICATION_NAME_KEY = "spring.application.name";
    private String CONFIG_SERVER_USERNAME = "spring.cloud.config.username";
    private String CONFIG_SERVER_PASSWORD = "spring.cloud.config.password";
    
    private LogConnectionFactory() {
    	Properties properties = new Properties();
    	PropertySource<?> applicationProperties = loadEhLogProperties();
    	if(applicationProperties != null) {
    		properties.setProperty("user", (String) applicationProperties.getProperty("spring.ehlogdataSource.username"));
    		properties.setProperty("password", (String) applicationProperties.getProperty("spring.ehlogdataSource.password"));
    		String ehLogDataSourceUrl = (String) applicationProperties.getProperty("spring.ehlogdataSource.url");
    		String ehLogDataSourceValidationQuery = (String) applicationProperties.getProperty("spring.datasource.validation-query"); 
    		GenericObjectPool<PoolableConnection> pool = new GenericObjectPool<PoolableConnection>();
    		pool.setTimeBetweenEvictionRunsMillis(Long.valueOf((String) applicationProperties.getProperty("spring.ehlogdataSource.time-between-eviction-runs-millis")));
    		pool.setMinEvictableIdleTimeMillis(Long.valueOf((String) applicationProperties.getProperty("spring.ehlogdataSource.min-evictable-idle-time-millis")));
    		pool.setMaxActive(Integer.valueOf((String) applicationProperties.getProperty("spring.ehlogdataSource.max-active")));
    		pool.setMaxIdle(Integer.valueOf((String) applicationProperties.getProperty("spring.ehlogdataSource.max-active")));
    		pool.setMinIdle(Integer.valueOf((String) applicationProperties.getProperty("spring.ehlogdataSource.min-idle")));
    		DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory(ehLogDataSourceUrl, properties);
    		new PoolableConnectionFactory(connectionFactory, pool, null, ehLogDataSourceValidationQuery, 3, false, false, Connection.TRANSACTION_READ_COMMITTED);
    		this.ehLogDataSource = new PoolingDataSource(pool);
    	} else {
    		System.out.println("Unable to find EhLog Properties");
    	}
    }
 
    private PropertySource<?> loadEhLogProperties() {
    	CompositePropertySource propertySource = null;
        try {
        	Properties bootstrapProperties = new Properties();
        	Properties systemProps = System.getProperties();
        	bootstrapProperties.load(LogConnectionFactory.class.getResourceAsStream("/bootstrap.properties"));
        	overrideBootstrapProperties(bootstrapProperties, systemProps);
        	propertySource = (CompositePropertySource) loadRemoteProperties(bootstrapProperties);
		} catch (IOException e) {
			System.out.println("Unable to Load EHLOG Configuration");
			e.printStackTrace();
		}
        Iterator<PropertySource<?>> it = propertySource.getPropertySources().iterator();
        while(it.hasNext()){
        	PropertySource<?> applicationProperties = it.next(); 
        	if(applicationProperties.containsProperty("spring.ehlogdataSource.url")){
        		return applicationProperties;
        	}
        }
        return null;
	}

	private void overrideBootstrapProperties(Properties bootstrapProperties, Properties systemProps) {
		String url = systemProps.getProperty(URL_CONFIG_SERVER_KEY);
		if(url !=null && !url.isEmpty()){
			bootstrapProperties.put(URL_CONFIG_SERVER_KEY, url);
		}
		String username = systemProps.getProperty(CONFIG_SERVER_USERNAME);
		String pwd = systemProps.getProperty(CONFIG_SERVER_PASSWORD);
		if(username !=null && !username.isEmpty() && pwd !=null && !pwd.isEmpty()){
			String authorization = "Basic " + Base64Utils.encode(username+":"+pwd);
			bootstrapProperties.put(AUTHORIZATION_CONFIG_SERVER_KEY, authorization);
		}
		String profile = systemProps.getProperty(PROFILE_KEY);
		if(profile !=null && !profile.isEmpty()){
			bootstrapProperties.put(PROFILE_KEY, profile);
		}

	}

	public static Connection getDatabaseConnection() throws SQLException {
        return Singleton.INSTANCE.ehLogDataSource.getConnection();
    }
    
	private PropertySource<?> loadRemoteProperties(Properties bootstrapProperties) {
		MockEnvironment environment = new MockEnvironment(); 
		try{
			ConfigClientProperties configClientProperties = new ConfigClientProperties(environment);
			configClientProperties.setUri(bootstrapProperties.getProperty(URL_CONFIG_SERVER_KEY));
			environment.setProperty(APPLICATION_NAME_KEY, bootstrapProperties.getProperty(APPLICATION_NAME_KEY));
			configClientProperties.setProfile(bootstrapProperties.getProperty(PROFILE_KEY));
			configClientProperties.setAuthorization(bootstrapProperties.getProperty(AUTHORIZATION_CONFIG_SERVER_KEY));
			ConfigServicePropertySourceLocator configServicePropertySourceLocator = new ConfigServicePropertySourceLocator(configClientProperties);
			return configServicePropertySourceLocator.locate(environment);
		} catch (Exception e ){
			e.printStackTrace();
			return null;
		}
	}
}