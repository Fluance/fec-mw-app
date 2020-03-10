package net.fluance.cockpit.core.repository.jdbc.accesslog;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PALIgnoredUsersDao {
	
	@Autowired
	@Qualifier("ehlogdataSource")
	private DataSource ehlogdataSource;

	@Cacheable("PASIgnoredUsers")
	public List<String> getAll(){
		return getJdbcOperations().queryForList("select appuser from ehealth.ignoredusers", String.class);
	}
	
	protected JdbcOperations getJdbcOperations(){
		return new JdbcTemplate(ehlogdataSource);
	}
}
