/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.note;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.app.security.service.UserProfileLoader;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.PatientReference;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.note.NoteCategory;
import net.fluance.cockpit.core.model.jdbc.note.NoteDetails;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class NoteDetailRepository extends JdbcRepository<NoteDetails, Long> {

	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultNoteDetailsListOrderBy}")
	private String defaultResultNoteDetailsListOrderBy;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultNoteDetailsListSortOrder}")
	private String defaultResultNoteDetailsListSortOrder;
	@Value("${net.fluance.cockpit.core.model.repository.defaultHasPicturesValue}")
	private boolean defaultHasPictures;
	
	@Autowired
	UserProfileLoader profileLoader;

	public NoteDetailRepository() {
		this(MappingsConfig.TABLE_NAMES.get("Note"));
	}

	public NoteDetailRepository(String tableName) {
		super(FAKE_ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "noteid"));
	}

	public final static RowMapper<NoteDetails> FAKE_ROW_MAPPER = new RowMapper<NoteDetails>() {
		public NoteDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
			return null;
		}
	};

	public final RowMapper<NoteDetails> ROW_MAPPER = new RowMapper<NoteDetails>() {
		public NoteDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long id = SqlUtils.getLong(true, rs, "noteid");
			Long pid = SqlUtils.getLong(true, rs, "patient_id");
			String description = rs.getString("content");
			String title = rs.getString("title");
			Date referenceDate = rs.getTimestamp("referencedt");
			Date editedDate = rs.getTimestamp("editeddt");
			boolean isDeleted = rs.getBoolean("deleted");
			boolean shifted = rs.getBoolean("shifted");
			Integer categoryId = rs.getInt("categoryid");
			String name = rs.getString("name");
			//			Integer priority = rs.getInt("priority");
			String firstName = rs.getString("firstname");
			String lastName= rs.getString("lastname");
			String maidenName= rs.getString("maidenname");
			Date birthDate= rs.getDate("birthdate");
			String editor = rs.getString("editor");
			String creator = rs.getString("creator");
			Long visitNb = SqlUtils.getLong(true, rs, "visit_nb");

			NoteCategory noteCategory = new NoteCategory(categoryId, name);		
			PatientReference patientReference = new PatientReference(pid, firstName, lastName, maidenName, birthDate);
			NoteDetails note = new NoteDetails(id, title, description, editor, creator, shifted,
					referenceDate, editedDate, isDeleted, noteCategory, patientReference, visitNb);
			buildFullNames(note);
			return note;
		}
	};

	private void buildFullNames(NoteDetails note) {
		note.setEditor(profileLoader.buildFullName(note.getEditor()));
		note.setCreator(profileLoader.buildFullName(note.getCreator()));
	}


	public static final RowUnmapper<NoteDetails> ROW_UNMAPPER = new RowUnmapper<NoteDetails>() {
		public Map<String, Object> mapColumns(NoteDetails note) {
			return null;
		}
	};

	public List<NoteDetails> findByPatientId(long patientid, String creator, Boolean deleted, List<Integer> accessibleCategories, Boolean hasPictures, String orderBy, String sortOrder, Integer limit, Integer offset) {
		String sqlStatement;
		
		if (accessibleCategories.isEmpty())
			return new ArrayList<>();
		
		limit = (limit == null) ? defaultResultLimit : limit;
		offset = (offset == null) ? defaultResultOffset : offset;
		orderBy = (orderBy == null) ? defaultResultNoteDetailsListOrderBy : orderBy;
		sortOrder = (sortOrder == null) ? defaultResultNoteDetailsListSortOrder : sortOrder;
		hasPictures = (hasPictures == null) ? hasPictures: hasPictures;
		
		String accessibleCategoriesString = StringUtils.join(accessibleCategories, ',');
		
		if(creator != null && !creator.isEmpty()){
			if(hasPictures) {
				sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_CREATOR_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, patientid, creator, deleted, limit, offset);
			} else {
				sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_CREATOR, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, patientid, creator, deleted, limit, offset);	
			}
		} else {
			if(hasPictures) {
				sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, patientid, deleted, limit, offset);
			} else {
				sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_PID, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, patientid, deleted, limit, offset);
			}
		}
	}

	public List<NoteDetails> findShiftNotesByPatientId(long patientid, String creator, Boolean deleted, List<Integer> accessibleCategories, Boolean hasPictures, Date shiftStartDate, Date shiftEndDate, 
			String orderBy, String sortOrder, Integer limit, Integer offset) {
		
		String sqlStatement;
		
		if (accessibleCategories.isEmpty())
			return new ArrayList<>();
		
		limit = (limit == null) ? defaultResultLimit : limit;
		offset = (offset == null) ? defaultResultOffset : offset;
		orderBy = (orderBy == null) ? defaultResultNoteDetailsListOrderBy : orderBy;
		sortOrder = (sortOrder == null) ? defaultResultNoteDetailsListSortOrder : sortOrder;
		hasPictures = (hasPictures == null) ? hasPictures: hasPictures;
		
		String accessibleCategoriesString = StringUtils.join(accessibleCategories, ',');
		
		if(creator != null && !creator.isEmpty()){
			if(hasPictures) {
				sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_CREATOR_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, patientid, creator, deleted, shiftStartDate, shiftEndDate, limit, offset);
			} else {
				sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_CREATOR, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, patientid, creator, deleted, shiftStartDate, shiftEndDate, limit, offset);
			}
		} else {
			if(hasPictures) {
				sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);			
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, patientid, deleted, shiftStartDate, shiftEndDate, limit, offset);
			} else {
				sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID, accessibleCategoriesString, orderBy, sortOrder);			
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, patientid, deleted, shiftStartDate, shiftEndDate, limit, offset);
			}
		}
	}

	public Count findShiftNotesByPatientIdCount(long patientid, String creator, Boolean deleted, List<Integer> accessibleCategories, Boolean hasPictures, Date shiftStartDate, Date shiftEndDate) {
		String accessibleCategoriesString = "(" + StringUtils.join(accessibleCategories, ',') + ")";
		
		hasPictures = (hasPictures == null) ? hasPictures: hasPictures;
		
		Integer count = 0;
		if(creator != null && !creator.isEmpty()){
			if(hasPictures) {
				count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_CREATOR_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString),  Integer.class, patientid, creator, deleted, shiftStartDate, shiftEndDate);
			} else {
				count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_CREATOR_COUNT.replace("(?)", accessibleCategoriesString),  Integer.class, patientid, creator, deleted, shiftStartDate, shiftEndDate);
			}
		}else{
			if(hasPictures) {
				count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString),  Integer.class, patientid, deleted, shiftStartDate, shiftEndDate);
			} else {
				count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_COUNT.replace("(?)", accessibleCategoriesString),  Integer.class, patientid, deleted, shiftStartDate, shiftEndDate);
			}
		}
		return new Count(count);
	}

	public Count findByPatientIdCount(long patientid, String creator, Boolean deleted, List<Integer> accessibleCategories, Boolean hasPictures) {
		String accessibleCategoriesString = "(" + StringUtils.join(accessibleCategories, ',') + ")";
		
		hasPictures = (hasPictures == null) ? hasPictures: hasPictures;
		
		Integer count = 0;
		if(creator != null && !creator.isEmpty()){
			if(hasPictures) {
				count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_CREATOR_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, patientid, creator, deleted);
			} else {
				count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_CREATOR_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, patientid, creator, deleted);
			}
		} else{
			if(hasPictures) {
				count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, patientid, deleted);
			} else {
				count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_PID_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, patientid, deleted);
			}
		}
		return new Count(count);
	}

	public List<NoteDetails> findByPatientIdAndRead(long patientid, List<Integer> accessibleCategories, String creator, Boolean read, Boolean deleted, Boolean hasPictures, String userName, 
			String orderBy, String sortOrder, Integer limit, Integer offset) {
		
		String sqlStatement;
		
		if (accessibleCategories.isEmpty())
			return new ArrayList<>();
		
		limit = (limit == null) ? defaultResultLimit : limit;
		offset = (offset == null) ? defaultResultOffset : offset;
		orderBy = (orderBy == null) ? defaultResultNoteDetailsListOrderBy : orderBy;
		sortOrder = (sortOrder == null) ? defaultResultNoteDetailsListSortOrder : sortOrder;
		hasPictures = (hasPictures == null) ? hasPictures: hasPictures;
		
		String accessibleCategoriesString = StringUtils.join(accessibleCategories, ',');
		
		if(read == true){
			if(creator != null && !creator.isEmpty()){
				if(hasPictures) {
					sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_READ_AND_CREATOR_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, patientid, creator, deleted, limit, offset);
				} else {
					sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_READ_AND_CREATOR, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, patientid, creator, deleted, limit, offset);
				}
			} else {
				if(hasPictures) {
					sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_READ_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);				
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, patientid, deleted, limit, offset);
				} else {
					sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_READ, accessibleCategoriesString, orderBy, sortOrder);				
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, patientid, deleted, limit, offset);
				}
			}
		}
		else{
			if(creator != null && !creator.isEmpty()){
				if(hasPictures) {
					sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_UNREAD_AND_CREATOR_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, patientid, creator, deleted, limit, offset);
				} else {
					sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_UNREAD_AND_CREATOR, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, patientid, creator, deleted, limit, offset);
				}
			} else {				
				if(hasPictures) {
					sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_UNREAD_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, patientid, deleted, limit, offset);
				} else {
					sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_UNREAD, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, patientid, deleted, limit, offset);
				}
			}
		}
	}

	public List<NoteDetails> findShiftNotesByPatientIdAndRead(long patientid, List<Integer> accessibleCategories, String creator, Boolean read, Boolean deleted, Boolean hasPictures, String userName, Date shiftStartDate, Date shiftEndDate, 
			String orderBy, String sortOrder, Integer limit, Integer offset) {
		
		String sqlStatement;
		
		if (accessibleCategories.isEmpty())
			return new ArrayList<>();

		limit = (limit == null) ? defaultResultLimit : limit;
		offset = (offset == null) ? defaultResultOffset : offset;
		orderBy = (orderBy == null) ? defaultResultNoteDetailsListOrderBy : orderBy;
		sortOrder = (sortOrder == null) ? defaultResultNoteDetailsListSortOrder : sortOrder;
		hasPictures = (hasPictures == null) ? hasPictures: hasPictures;
		
		String accessibleCategoriesString = StringUtils.join(accessibleCategories, ',');
		
		if(read == true){
			if(creator != null && !creator.isEmpty()){
				if(hasPictures) {
					sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_READ_AND_CREATOR_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, patientid, creator, deleted, shiftStartDate, shiftEndDate, limit, offset);
				} else {
					sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_READ_AND_CREATOR, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, patientid, creator, deleted, shiftStartDate, shiftEndDate, limit, offset);
				}
			} else {
				if(hasPictures) {
					sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_READ_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, patientid, deleted, shiftStartDate, shiftEndDate, limit, offset);
				} else {
					sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_READ, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, patientid, deleted, shiftStartDate, shiftEndDate, limit, offset);
				}
			}
		}
		else{
			if(creator != null && !creator.isEmpty()){
				if(hasPictures) {
					sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_UNREAD_AND_CREATOR_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, patientid, creator, deleted, shiftStartDate, shiftEndDate, limit, offset);
				} else {
					sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_UNREAD_AND_CREATOR, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, patientid, creator, deleted, shiftStartDate, shiftEndDate, limit, offset);
				}
			} else {	
				if(hasPictures) {
					sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_UNREAD_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, patientid, deleted, shiftStartDate, shiftEndDate, limit, offset);
				} else {
					sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_UNREAD, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, patientid, deleted, shiftStartDate, shiftEndDate, limit, offset);
				}
			}
		}
	}

	public Count findShiftNotesByPatientIdAndReadCount(long patientid, List<Integer> accessibleCategories, String creator, Boolean read, Boolean deleted, Boolean hasPictures, String userName, Date shiftStartDate, Date shiftEndDate) {
		String accessibleCategoriesString = "(" + StringUtils.join(accessibleCategories, ',') + ")";
		
		hasPictures = (hasPictures == null) ? hasPictures: hasPictures;
		
		Integer count = 0;
		if(read == true){
			if(creator != null && !creator.isEmpty()){
				if(hasPictures) {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_READ_AND_CREATOR_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, creator, patientid, creator, deleted, shiftStartDate, shiftEndDate);
				} else {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_READ_AND_CREATOR_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, creator, patientid, creator, deleted, shiftStartDate, shiftEndDate);
				}
			} else{
				if(hasPictures) {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_READ_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, userName, patientid, deleted, shiftStartDate, shiftEndDate);
				} else {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_READ_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, userName, patientid, deleted, shiftStartDate, shiftEndDate);
				}
			}
		}
		else{
			if(creator != null && !creator.isEmpty()){
				if(hasPictures) {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_UNREAD_AND_CREATOR_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, creator, patientid, creator, deleted, shiftStartDate, shiftEndDate);
				} else {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_UNREAD_AND_CREATOR_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, creator, patientid, creator, deleted, shiftStartDate, shiftEndDate);
				}
			}else{
				if(hasPictures) {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_UNREAD_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, userName, patientid, deleted, shiftStartDate, shiftEndDate);
				} else {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_PID_AND_UNREAD_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, userName, patientid, deleted, shiftStartDate, shiftEndDate);
				}
			}
		}
		return new Count(count);
	}

	public Count findByPatientIdAndReadCount(long patientid, String creator, List<Integer> accessibleCategories, Boolean read, Boolean deleted, Boolean hasPictures, String userName) {
		String accessibleCategoriesString = "(" + StringUtils.join(accessibleCategories, ',') + ")";
		
		hasPictures = (hasPictures == null) ? hasPictures: hasPictures;
		
		Integer count = 0;
		if(read==true){
			if(creator != null && !creator.isEmpty()){
				if(hasPictures) {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_READ_AND_CREATOR_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, creator, patientid, creator, deleted);
				} else {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_READ_AND_CREATOR_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, creator, patientid, creator, deleted);	
				}
			}else{
				if(hasPictures) {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_READ_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, userName, patientid, deleted);
				} else {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_READ_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, userName, patientid, deleted);
				}
			}
		}
		else{
			if(creator != null && !creator.isEmpty()){
				if(hasPictures) {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_UNREAD_AND_CREATOR_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, creator, patientid, creator, deleted);
				} else {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_UNREAD_AND_CREATOR_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, creator, patientid, creator, deleted);
				}
			}else{
				if(hasPictures) {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_UNREAD_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, userName, patientid, deleted);
				} else {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_PID_AND_UNREAD_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, userName, patientid, deleted);
				}
			}
		}
		return new Count(count);
	}

	public List<NoteDetails> findByVisitNbAndRead(Long visitNb, List<Integer> accessibleCategories, String creator, Boolean read, Boolean deleted, Boolean hasPictures, String userName, 
			String orderBy, String sortOrder, Integer limit, Integer offset) {
		
		String sqlStatement;
		
		if (accessibleCategories.isEmpty())
			return new ArrayList<>();

		limit = (limit == null) ? defaultResultLimit : limit;
		offset = (offset == null) ? defaultResultOffset : offset;
		orderBy = (orderBy == null) ? defaultResultNoteDetailsListOrderBy : orderBy;
		sortOrder = (sortOrder == null) ? defaultResultNoteDetailsListSortOrder : sortOrder;
		hasPictures = (hasPictures == null) ? hasPictures: hasPictures;
		
		String accessibleCategoriesString = StringUtils.join(accessibleCategories, ',');
		
		if(read == true){
			if(creator != null && !creator.isEmpty()){
				if(hasPictures) {
					sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_READ_AND_CREATOR_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, visitNb, creator, deleted, limit, offset);
				} else {
					sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_READ_AND_CREATOR, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, visitNb, creator, deleted, limit, offset);
				}
			} else {
				if(hasPictures) {
					sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_READ_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, visitNb, deleted, limit, offset);
				} else {
					sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_READ, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, visitNb, deleted, limit, offset);
				}
			}
		}else{
			if(creator != null && !creator.isEmpty()){
				if(hasPictures) {
					sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, visitNb, creator, deleted, limit, offset);
				} else {
					sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, visitNb, creator, deleted, limit, offset);
				}
			} else {				
				if(hasPictures) {
					sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_UNREAD_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, visitNb, deleted, limit, offset);
				} else {
					sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_UNREAD, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, visitNb, deleted, limit, offset);
				}
			}
		}
	}

	public List<NoteDetails> findShiftNotesByVisitNbAndRead(Long visitNb, List<Integer> accessibleCategories, String creator, Boolean read, Boolean deleted, Boolean hasPictures, String userName, Date shiftStartDate, Date shiftEndDate, 
			String orderBy, String sortOrder, Integer limit, Integer offset) {
		
		String sqlStatement;
		
		if (accessibleCategories.isEmpty())
			return new ArrayList<>();

		limit = (limit == null) ? defaultResultLimit : limit;
		offset = (offset == null) ? defaultResultOffset : offset;
		orderBy = (orderBy == null) ? defaultResultNoteDetailsListOrderBy : orderBy;
		sortOrder = (sortOrder == null) ? defaultResultNoteDetailsListSortOrder : sortOrder;
		hasPictures = (hasPictures == null) ? hasPictures: hasPictures;
		
		String accessibleCategoriesString = StringUtils.join(accessibleCategories, ',');
		
		if(read == true){
			if(creator != null && !creator.isEmpty()) {
				if(hasPictures) {
					sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_READ_AND_CREATOR_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, visitNb, creator, deleted, shiftStartDate, shiftEndDate, limit, offset);
				} else {
					sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_READ_AND_CREATOR, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, visitNb, creator, deleted, shiftStartDate, shiftEndDate, limit, offset);
				}
			} else {				
				if(hasPictures) {
					sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_READ_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, visitNb, deleted, shiftStartDate, shiftEndDate, limit, offset);
				} else {
					sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_READ, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, visitNb, deleted, shiftStartDate, shiftEndDate, limit, offset);
				}
			}
		}else{
			if(creator != null && !creator.isEmpty()){
				if(hasPictures) {
					sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, visitNb, creator, deleted, shiftStartDate, shiftEndDate, limit, offset);
				} else {
					sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, visitNb, creator, deleted, shiftStartDate, shiftEndDate, limit, offset);
				}
			} else {
				if(hasPictures) {
					sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_UNREAD_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, visitNb, deleted, shiftStartDate, shiftEndDate, limit, offset);
				} else {
					sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_UNREAD, accessibleCategoriesString, orderBy, sortOrder);
					return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, visitNb, deleted, shiftStartDate, shiftEndDate, limit, offset);
				}
			}
		}
	}

	public Count findShiftNotesByVisitNbAndReadCount(Long visitNb, List<Integer> accessibleCategories, String creator, Boolean read, Boolean deleted, Boolean hasPictures, String userName, Date shiftStartDate, Date shiftEndDate) {
		String accessibleCategoriesString = "(" + StringUtils.join(accessibleCategories, ',') + ")";
		
		hasPictures = (hasPictures == null) ? hasPictures: hasPictures;
		
		Integer count = 0;
		if(read == true){
			if(creator != null && !creator.isEmpty()){
				if(hasPictures) {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_READ_AND_CREATOR_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, creator, visitNb, creator, deleted, shiftStartDate, shiftEndDate);
				} else {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_READ_AND_CREATOR_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, creator, visitNb, creator, deleted, shiftStartDate, shiftEndDate);
				}
			}else{
				if(hasPictures) {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_READ_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, userName, visitNb, deleted, shiftStartDate, shiftEndDate);
				} else {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_READ_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, userName, visitNb, deleted, shiftStartDate, shiftEndDate);
				}
			}
		}else{
			if(creator != null && !creator.isEmpty()){
				if(hasPictures) {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, creator, visitNb, creator, deleted, shiftStartDate, shiftEndDate);
				} else {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, creator, visitNb, creator, deleted, shiftStartDate, shiftEndDate);
				}
			}else{
				if(hasPictures) {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_UNREAD_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, userName, visitNb, deleted, shiftStartDate, shiftEndDate);
				} else {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_UNREAD_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, userName, visitNb, deleted, shiftStartDate, shiftEndDate);
				}
			}
		}
		return new Count(count);
	}

	public Count findByVisitNbAndReadCount(Long visitNb, String creator, List<Integer> accessibleCategories, boolean read, Boolean deleted, Boolean hasPictures, String userName) {
		String accessibleCategoriesString = "(" + StringUtils.join(accessibleCategories, ',') + ")";
		
		hasPictures = (hasPictures == null) ? hasPictures: hasPictures;
		
		Integer count = 0;
		if(read==true){
			if(creator != null && !creator.isEmpty()){
				if(hasPictures) {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_READ_AND_CREATOR_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, creator, visitNb, creator, deleted );
				} else {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_READ_AND_CREATOR_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, creator, visitNb, creator, deleted );
				}
			}else{
				if(hasPictures) {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_READ_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, userName, visitNb, deleted );
				} else {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_READ_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, userName, visitNb, deleted );
				}
			}
		}else{
			if(creator != null && !creator.isEmpty()){
				if(hasPictures) {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, creator, visitNb, creator, deleted);
				} else {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, creator, visitNb, creator, deleted);
				}
			}else{
				if(hasPictures) {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_UNREAD_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, userName, visitNb, deleted);
				} else {
					count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_UNREAD_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, userName, visitNb, deleted);
				}
			}
		}
		return new Count(count);
	}

	public List<NoteDetails> findAllByPatientIdAndRead(long patientid, String creator, List<Integer> accessibleCategories, Boolean read, Boolean deleted, Boolean hasPictures, String userName,
			String orderBy, String sortOrder, Integer limit, Integer offset) {
		
		String sqlStatement;
		
		if (accessibleCategories.isEmpty())
			return new ArrayList<>();
		
		String accessibleCategoriesString = StringUtils.join(accessibleCategories, ',');
		
		if(read == true){
			if(creator != null && !creator.isEmpty()){
				sqlStatement = String.format(NoteSQLQuerries.FIND_ALL_NOTES_BY_PID_AND_READ_AND_CREATOR, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, patientid, creator, deleted);
			} else {
				sqlStatement = String.format(NoteSQLQuerries.FIND_ALL_NOTES_BY_PID_AND_READ, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, patientid, deleted);				
			}
		}else{
			if(creator != null && !creator.isEmpty()){
				sqlStatement = String.format(NoteSQLQuerries.FIND_ALL_NOTES_BY_PID_AND_UNREAD_AND_CREATOR, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, creator, patientid, creator, deleted);
			} else {				
				sqlStatement = String.format(NoteSQLQuerries.FIND_ALL_NOTES_BY_PID_AND_UNREAD, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, userName, patientid, deleted);
			}
		}
	}

	public List<NoteDetails> findAllShiftNotesByPatientIdAndRead(long patientid, String creator, List<Integer> accessibleCategories, Boolean read, Boolean deleted, Boolean hasPictures, String userName, Date shiftStartDate, Date shiftEndDate) {
		if (accessibleCategories.isEmpty())
			return new ArrayList<>();
		String accessibleCategoriesString = "(" + StringUtils.join(accessibleCategories, ',') + ")";
		if(read == true){
			if(creator != null && !creator.isEmpty()){
				return getJdbcOperations().query(NoteSQLQuerries.FIND_ALL_SHIFT_NOTES_BY_PID_AND_READ_AND_CREATOR.replace("(?)", accessibleCategoriesString), ROW_MAPPER, creator, patientid, creator, deleted, shiftStartDate, shiftEndDate);
			}
			return getJdbcOperations().query(NoteSQLQuerries.FIND_ALL_SHIFT_NOTES_BY_PID_AND_READ.replace("(?)", accessibleCategoriesString), ROW_MAPPER, userName, patientid, deleted, shiftStartDate, shiftEndDate);
		}else{
			if(creator != null && !creator.isEmpty()){
				return getJdbcOperations().query(NoteSQLQuerries.FIND_ALL_SHIFT_NOTES_BY_PID_AND_UNREAD_AND_CREATOR.replace("(?)", accessibleCategoriesString), ROW_MAPPER, creator, patientid, creator, deleted, shiftStartDate, shiftEndDate);
			}
			return getJdbcOperations().query(NoteSQLQuerries.FIND_ALL_SHIFT_NOTES_BY_PID_AND_UNREAD.replace("(?)", accessibleCategoriesString), ROW_MAPPER, userName, patientid, deleted, shiftStartDate, shiftEndDate);
		}
	}

	public NoteDetails findByNoteId(Long noteId){
		NoteDetails note = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTE_BY_ID, ROW_MAPPER, noteId);
		return note;
	}

	public Boolean getHasRead(String username, String creator, Long noteId){
		Integer read = 0;
		if(creator != null && !creator.isEmpty()){
			read = getJdbcOperations().queryForObject(NoteSQLQuerries.GET_HAS_READ_FOR_NOTE, Integer.class, creator, noteId);
		}else{
			read = getJdbcOperations().queryForObject(NoteSQLQuerries.GET_HAS_READ_FOR_NOTE, Integer.class, username, noteId);
		}
		if(read == 1){
			return true;
		}
		return false;
	}

	public List<NoteDetails> findByVisitNb(Long visitNb, String creator, boolean deleted, List<Integer> accessibleCategories, Boolean hasPictures, String orderBy, String sortOrder, Integer limit, Integer offset) {
		
		String sqlStatement;
		
		if (accessibleCategories.isEmpty())
			return new ArrayList<>();
		
		hasPictures = (hasPictures == null) ? hasPictures: hasPictures;
		
		String accessibleCategoriesString = StringUtils.join(accessibleCategories, ',');
		
		if(creator != null && !creator.isEmpty()){
			if(hasPictures) {
				sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_CREATOR_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, visitNb, creator, deleted, limit, offset);
			} else {
				sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_CREATOR, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, visitNb, creator, deleted, limit, offset);
			}
		} else {
			if(hasPictures) {
				sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, visitNb, deleted, limit, offset);
			} else {
				sqlStatement = String.format(NoteSQLQuerries.FIND_NOTES_BY_VISITNB, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, visitNb, deleted, limit, offset);
			}
		}
	}

	public List<NoteDetails> findShiftNotesByVisitNb(Long visitNb, String creator, boolean deleted, List<Integer> accessibleCategories, Boolean hasPictures, Date shiftStartDate, Date shiftEndDate, 
			String orderBy, String sortOrder, Integer limit, Integer offset) {
		
		String sqlStatement;
		
		if (accessibleCategories.isEmpty())
			return new ArrayList<>();
		
		hasPictures = (hasPictures == null) ? hasPictures: hasPictures;
		
		String accessibleCategoriesString = StringUtils.join(accessibleCategories, ',');
		
		if(creator != null && !creator.isEmpty()){
			if(hasPictures) {
				sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_CREATOR_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, visitNb, creator, deleted, shiftStartDate, shiftEndDate, limit, offset);
			} else {
				sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_CREATOR, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, visitNb, creator, deleted, shiftStartDate, shiftEndDate, limit, offset);
			}
		} else {			
			if(hasPictures) {
				sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_HAS_PICTURES, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, visitNb, deleted, shiftStartDate, shiftEndDate, limit, offset);
			} else {
				sqlStatement = String.format(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB, accessibleCategoriesString, orderBy, sortOrder);
				return getJdbcOperations().query(sqlStatement, ROW_MAPPER, visitNb, deleted, shiftStartDate, shiftEndDate, limit, offset);
			}
		}
	}

	public Count findShiftNotesByVisitNbCount(Long visitNb, String creator, boolean deleted, List<Integer> accessibleCategories, Boolean hasPictures, Date shiftStartDate, Date shiftEndDate) {
		String accessibleCategoriesString = "(" + StringUtils.join(accessibleCategories, ',') + ")";
		
		hasPictures = (hasPictures == null) ? hasPictures: hasPictures;
		
		Integer count = 0;
		if(creator != null && !creator.isEmpty()){
			if(hasPictures) {
				count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_CREATOR_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, visitNb, creator, deleted, shiftStartDate, shiftEndDate);
			} else {
				count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_CREATOR_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, visitNb, creator, deleted, shiftStartDate, shiftEndDate);
			}
		} else{
			if(hasPictures) {
				count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, visitNb, deleted, shiftStartDate, shiftEndDate);
			} else {
				count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_SHIFT_NOTES_BY_VISITNB_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, visitNb, deleted, shiftStartDate, shiftEndDate);
			}
		}
		return new Count(count);
	}

	public Count findByVisitNbCount(Long visitNb, String creator, Boolean deleted, List<Integer> accessibleCategories, Boolean hasPictures) {
		String accessibleCategoriesString = "(" + StringUtils.join(accessibleCategories, ',') + ")";
		
		hasPictures = (hasPictures == null) ? hasPictures: hasPictures;
		
		Integer count = 0;
		if(creator != null && !creator.isEmpty()){
			if(hasPictures) {
				count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_CREATOR_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, visitNb, creator, deleted );
			} else {
				count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_CREATOR_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, visitNb, creator, deleted );
			}
		}
		else{
			if(hasPictures) {
				count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_AND_HAS_PICTURES_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, visitNb, deleted );
			} else {
				count = getJdbcOperations().queryForObject(NoteSQLQuerries.FIND_NOTES_BY_VISITNB_COUNT.replace("(?)", accessibleCategoriesString), Integer.class, visitNb, deleted );
			}
		}
		return new Count(count);
	}

	public List<NoteDetails> findAllByVisitNbAndRead(Long visitNb, String creator,  List<Integer> accessibleCategories, Boolean read, Boolean deleted, String userName) {
		if (accessibleCategories.isEmpty())
			return new ArrayList<>();
		String accessibleCategoriesString = "(" + StringUtils.join(accessibleCategories, ',') + ")";
		if(read == true){
			if(creator != null && !creator.isEmpty()){
				return getJdbcOperations().query(NoteSQLQuerries.FIND_ALL_NOTES_BY_VISITNB_AND_READ_AND_CREATOR.replace("(?)", accessibleCategoriesString), ROW_MAPPER, creator, visitNb, creator, deleted);
			}
			return getJdbcOperations().query(NoteSQLQuerries.FIND_ALL_NOTES_BY_VISITNB_AND_READ.replace("(?)", accessibleCategoriesString), ROW_MAPPER, userName, visitNb, deleted);
		}else{
			if(creator != null && !creator.isEmpty()){
				return getJdbcOperations().query(NoteSQLQuerries.FIND_ALL_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR.replace("(?)", accessibleCategoriesString), ROW_MAPPER, creator, visitNb, creator, deleted);
			}
			return getJdbcOperations().query(NoteSQLQuerries.FIND_ALL_NOTES_BY_VISITNB_AND_UNREAD.replace("(?)", accessibleCategoriesString), ROW_MAPPER, userName, visitNb, deleted);
		}
	}

	public List<NoteDetails> findAllShiftNotesByVisitNbAndRead(Long visitNb, String creator,  List<Integer> accessibleCategories, Boolean read, Boolean deleted, String userName, Date shiftStartDate, Date shiftEndDate) {
		if (accessibleCategories.isEmpty())
			return new ArrayList<>();
		String accessibleCategoriesString = "(" + StringUtils.join(accessibleCategories, ',') + ")";
		if(read == true){
			if(creator != null && !creator.isEmpty()){
				return getJdbcOperations().query(NoteSQLQuerries.FIND_ALL_SHIFT_NOTES_BY_VISITNB_AND_READ_AND_CREATOR.replace("(?)", accessibleCategoriesString), ROW_MAPPER, creator, visitNb, creator, deleted, shiftStartDate, shiftEndDate);
			}
			return getJdbcOperations().query(NoteSQLQuerries.FIND_ALL_SHIFT_NOTES_BY_VISITNB_AND_READ.replace("(?)", accessibleCategoriesString), ROW_MAPPER, userName, visitNb, deleted, shiftStartDate, shiftEndDate);
		}else{
			if(creator != null && !creator.isEmpty()){
				return getJdbcOperations().query(NoteSQLQuerries.FIND_ALL_SHIFT_NOTES_BY_VISITNB_AND_UNREAD_AND_CREATOR.replace("(?)", accessibleCategoriesString), ROW_MAPPER, creator, visitNb, creator, deleted, shiftStartDate, shiftEndDate);
			}
			return getJdbcOperations().query(NoteSQLQuerries.FIND_ALL_SHIFT_NOTES_BY_VISITNB_AND_UNREAD.replace("(?)", accessibleCategoriesString), ROW_MAPPER, userName, visitNb, deleted, shiftStartDate, shiftEndDate);
		}
	}
}
