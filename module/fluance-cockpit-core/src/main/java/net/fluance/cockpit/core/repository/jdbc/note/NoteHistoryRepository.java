package net.fluance.cockpit.core.repository.jdbc.note;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;
import com.nurkiewicz.jdbcrepository.sql.SqlGenerator;

import net.fluance.app.security.service.Person;
import net.fluance.app.security.service.UserProfileLoader;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.jpa.note.History;

@Repository
public class NoteHistoryRepository extends JdbcRepository<History, Long> {

	@Autowired
	UserProfileLoader profileLoader;

	public NoteHistoryRepository() {
		this(MappingsConfig.TABLE_NAMES.get("Note"));
	}

	public NoteHistoryRepository(String tableName) {
		super(FAKE_ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "noteid"));
	}
	
	public NoteHistoryRepository(RowMapper<History> rowMapper, RowUnmapper<History> rowUnmapper, SqlGenerator sqlGenerator, TableDescription table) {
		super(rowMapper, rowUnmapper, sqlGenerator, table);
	}
	
	public static final RowMapper<History> FAKE_ROW_MAPPER = new RowMapper<History>() {
		public History mapRow(ResultSet rs, int rowNum) throws SQLException {
			return null;
		}
	};
	
	public final RowMapper<History> ROW_MAPPER = new RowMapper<History>() {
		public History mapRow(ResultSet rs, int rowNum) throws SQLException {
			Person editor = new Person(rs.getString("editor"));
			editor = profileLoader.buildFullName(editor);
			return new History(rs.getLong("history_id"), rs.getLong("entity_id"), rs.getString("resource_type"), rs.getTimestamp("date"), editor);
		}
	};

	public static final RowUnmapper<History> ROW_UNMAPPER = new RowUnmapper<History>() {
		public Map<String, Object> mapColumns(History history) {
			return null;
		}
	};
	
	public List<History> getNoteHistory(Long noteId, String resourcetype, Long resourceid, String beforedate, Integer limit, Integer offset){
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from bmv_note_history where note_id = ? ");
		
		if(resourcetype != null && !resourcetype.isEmpty() && resourcetype.length() > 0) {
			sql.append(" and resource_type = '" + resourcetype + "'");
		}		
		if(resourceid != null) {
			sql.append(" and entity_id = " + resourceid);
		}
		if(beforedate != null && !beforedate.isEmpty() && beforedate.length() > 0) {
			sql.append(" and date < '" + beforedate + "'");
		}
		
		sql.append(" order by date desc limit ? offset ?");
		
		return getJdbcOperations().query(sql.toString(), ROW_MAPPER, noteId, limit, offset);
	}
		
	public Count getNoteHistoryCount(Long noteId, String resourcetype, Long resourceid, String beforedate){
		
		StringBuffer sql = new StringBuffer();
		sql.append("select COUNT(*) from bmv_note_history where note_id = ?");
		
		if(resourcetype != null && !resourcetype.isEmpty() && resourcetype.length() > 0) {
			sql.append(" and resource_type = '" + resourcetype + "'");
		}		
		if(resourceid != null) {
			sql.append(" and entity_id = " + resourceid);
		}
		if(beforedate != null && !beforedate.isEmpty() && beforedate.length() > 0) {
			sql.append(" and date < '" + beforedate + "'");
		}
		
		Integer count = getJdbcOperations().queryForObject(sql.toString(), Integer.class, noteId);
		return new Count(count);
	}
}
