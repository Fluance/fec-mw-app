package net.fluance.cockpit.core.repository.jdbc.picture;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.app.security.service.Person;
import net.fluance.app.security.service.UserProfileLoader;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.picture.PictureDetail;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class PictureDetailRepository extends JdbcRepository<PictureDetail, Long>{

	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;
	
	@Autowired
	UserProfileLoader profileLoader;
	
	public PictureDetailRepository() {
		this(MappingsConfig.TABLE_NAMES.get("picture"));
	}

	public PictureDetailRepository(String tableName) {
		super(FAKE_ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "id"));
	}
	
	public static final RowMapper<PictureDetail> FAKE_ROW_MAPPER = new RowMapper<PictureDetail>() {
		public PictureDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			return null;
		}
	};
	
	public final RowMapper<PictureDetail> ROW_MAPPER = new RowMapper<PictureDetail>() {
		public PictureDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long pictureId = SqlUtils.getLong(true, rs, "id");
			Long noteId = SqlUtils.getLong(true, rs, "note_id");;
			String fileName = rs.getString("filename");
			Date editedDate = rs.getTimestamp("editeddt");
			Date referenceDate = rs.getTimestamp("referencedt");
		    String annotation = rs.getString("annotation");
			Person editor = new Person(rs.getString("editor"));
			editor = profileLoader.buildFullName(editor);
			Integer order = rs.getInt("sortorder");
			Boolean deleted = rs.getBoolean("deleted");
			
			return new PictureDetail(pictureId, noteId, fileName, editedDate, referenceDate, annotation, editor, order, deleted);
		}
	};

	public static final RowUnmapper<PictureDetail> ROW_UNMAPPER = new RowUnmapper<PictureDetail>() {
		public Map<String, Object> mapColumns(PictureDetail picture) {
			return null;
		}
	};
	
	public List<PictureDetail> findAllPicturesByShiftNoteId(Long noteId, Boolean deleted) {
		if(deleted==null){
			deleted=false;
		}
		List<PictureDetail> pictureList = getJdbcOperations().query(SQLStatements.FIND_ALL_PICTURES_BY_SHIFT_NOTE_ID, ROW_MAPPER, noteId, deleted);
		return pictureList;
	}
	
	public Count findAllPicturesByShiftNoteIdCount(Long noteId, Boolean deleted) {
		if(deleted==null){
			deleted=false;
		}
		Integer count = getJdbcOperations().queryForObject(SQLStatements.FIND_ALL_PICTURES_BY_SHIFT_NOTE_ID_COUNT, Integer.class, noteId, deleted);
		return new Count(count);
	}
	
	public List<PictureDetail> findPicturesByNoteId(Long noteId, Boolean deleted, Integer limit, Integer offset) {
		if (limit == null){
			limit = defaultResultLimit;
		}
		if (offset == null){
			offset = defaultResultOffset;
		}
		if(deleted == null){
			deleted = false;
		}
		List<PictureDetail> pictureList = getJdbcOperations().query(SQLStatements.FIND_PICTURES_BY_NOTE_ID, ROW_MAPPER, noteId, deleted, limit, offset);
		return pictureList;
	}
	
	public Count findPicturesByNoteIdCount(Long noteId, Boolean deleted) {
		if(deleted==null){
			deleted=false;
		}
		Integer count = getJdbcOperations().queryForObject(SQLStatements.FIND_PICTURES_BY_NOTE_ID_COUNT, Integer.class, noteId, deleted);
		return new Count(count);
	}
	
	public PictureDetail findPictureById(Long pictureId) {
		PictureDetail pictureDetail = getJdbcOperations().queryForObject(SQLStatements.FIND_PICTURE_BY_ID, ROW_MAPPER, pictureId);
		return pictureDetail;
	}
	
}
