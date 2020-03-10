/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.note.roleCategory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;
import javax.sql.RowSet;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.PatientReference;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.note.NoteDetails;
import net.fluance.cockpit.core.model.jdbc.note.NoteCategory;
import net.fluance.cockpit.core.model.jdbc.patient.PatientContact;
import net.fluance.commons.sql.SqlUtils;

@Component
public class NoteCategoriesDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public static final RowMapper<RoleCategory> ROW_MAPPER_RoleCategory = new RowMapper<RoleCategory>() {
		public RoleCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
			RoleCategory rc = new RoleCategory(rs.getString("role"), rs.getInt("category_id"));
			return rc;
		}
	};

	public static final RowMapper<NoteCategory> ROW_MAPPER_NoteCategory = new RowMapper<NoteCategory>() {
		public NoteCategory mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new NoteCategory(rs.getInt("id"), rs.getString("name"), rs.getInt("priority"));
		}
	};

	@Cacheable("RoleCategories")
	public Map<String, List<Integer>> getAllRoleCategories(){
		Map<String, List<Integer>> result = new HashMap<>();
		List<RoleCategory> roleCategoryList = jdbcTemplate.query(SQLStatements.ROLES_CATEGORIES_GET_ALL, ROW_MAPPER_RoleCategory);
		for (RoleCategory roleCategory : roleCategoryList){
			if (result.get(roleCategory.getRole()) != null){
				result.get(roleCategory.getRole()).add(roleCategory.getCategory());
			} else {
				List<Integer> category = new ArrayList<>();
				category.add(roleCategory.getCategory());
				result.put(roleCategory.getRole(), category);
			}
		}
		return result;
	}

	public List<Integer> getCategoriesForRoles(List<String> roles) {
		String rolesString = SqlUtils.toIn(roles);
		return jdbcTemplate.queryForList(SQLStatements.ROLES_CATEGORIES_GET_CATEGORIES.replace("(?)", rolesString), Integer.class);
	}
	
	public List<NoteCategory> getCategoriesDetails(List<String> roles){
		String rolesString = SqlUtils.toIn(roles);
		return jdbcTemplate.query(SQLStatements.ROLES_CATEGORIES_GET_CATEGORIES_DETAILS.replace("(?)", rolesString), ROW_MAPPER_NoteCategory);
	}

}
