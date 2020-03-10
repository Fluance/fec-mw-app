/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc;

import java.io.Serializable;

import org.springframework.data.domain.Persistable;
import org.springframework.jdbc.core.RowMapper;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;
import com.nurkiewicz.jdbcrepository.sql.SqlGenerator;

public abstract class AbstractJdbcRepository<T extends Persistable<Serializable>> extends JdbcRepository<T, Serializable> {

	public AbstractJdbcRepository(RowMapper<T> rowMapper, RowUnmapper<T> rowUnmapper, SqlGenerator sqlGenerator,
			TableDescription table) {
		super(rowMapper, rowUnmapper, sqlGenerator, table);
	}

}
