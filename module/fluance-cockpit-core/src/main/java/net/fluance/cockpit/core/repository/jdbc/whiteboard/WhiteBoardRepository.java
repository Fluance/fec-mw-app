package net.fluance.cockpit.core.repository.jdbc.whiteboard;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.jdbc.whiteboard.WhiteBoardMapper;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewEntity;
import net.fluance.cockpit.core.sql.WhiteBoardSQLStatements;

@Repository
@Component
public class WhiteBoardRepository extends JdbcRepository<WhiteBoardViewEntity, Long> {

	
	public WhiteBoardRepository() {
		this(MappingsConfig.TABLE_NAMES.get("whiteboard_view"));
	}

	public WhiteBoardRepository(String tableName) {
		super(WhiteBoardMapper.ROW_MAPPER, WhiteBoardMapper.ROW_UNMAPPER,
				new TableDescription(tableName, null, "id"));
	}
	
	public List<WhiteBoardViewEntity> findEntries(String sql, List<Object> params) {
		return getJdbcOperations().query(sql, WhiteBoardMapper.ROW_MAPPER, params.toArray());
	}
	
	public WhiteBoardViewEntity findOneEntry(Long id) {
		List<WhiteBoardViewEntity> entries = getJdbcOperations().query(WhiteBoardSQLStatements.SELECT_BY_ID,
				WhiteBoardMapper.ROW_MAPPER, id);
		if (!entries.isEmpty()) {
			return entries.get(0);
		}
		return null;
	}

	public List<String> getNurses(Long companyId, String serviceId, Integer limit, Integer offset) {
		return getJdbcOperations().query(WhiteBoardSQLStatements.SELECT_NURSES, WhiteBoardMapper.NURSE_MAPPER,
				companyId, serviceId, limit, offset);
	}
	
	public List<String> getPhysicians(Long companyId, String serviceId, Integer limit, Integer offset) {
		return getJdbcOperations().query(WhiteBoardSQLStatements.SELECT_PHYSICIANS, WhiteBoardMapper.PHYSICIAN_MAPPER,
				companyId, serviceId, limit, offset);
	}

	public Boolean existEntryForVisitNumber(Long visitNumber) {
		return getJdbcOperations().queryForObject(WhiteBoardSQLStatements.COUNT_BY_VISIT_NUMBER, WhiteBoardMapper.COUNT_MAPPER,visitNumber);
	}

	public Boolean existEntryForVisitNumberOnView(Long visitNumber, Long companyId, String serviceId) {
		return getJdbcOperations().queryForObject(WhiteBoardSQLStatements.COUNT_BY_VISIT_NUMBER_VIEW, WhiteBoardMapper.COUNT_MAPPER,visitNumber,serviceId, companyId);
	}
}
