package net.fluance.cockpit.core.repository.jdbc.resourcepersonnel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.physician.Physician;
import net.fluance.cockpit.core.model.jdbc.resourcepersonnel.ResourcePersonnel;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class ResourcePersonnelRepository extends JdbcRepository<ResourcePersonnel, String> {

	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private Integer defaultResultOffset;
	
	public ResourcePersonnelRepository() {
		this(MappingsConfig.TABLE_NAMES.get("ResourcePersonnel"));
	}
	
	/**
	 * 
	 * @param tableName
	 */
	public ResourcePersonnelRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER,
				new TableDescription(tableName, null, "id"));
	}

	public static final RowMapper<ResourcePersonnel> ROW_MAPPER = new RowMapper<ResourcePersonnel>() {
		public ResourcePersonnel mapRow(ResultSet rs, int rowNum) throws SQLException {
			ResourcePersonnel resourcePersonnel = new ResourcePersonnel(
					rs.getString("id"), 
					SqlUtils.getInt(true, rs, "company_id"), 
					SqlUtils.getInt(true, rs, "rsid"), 
					rs.getString("staffid"), 
					rs.getString("role"), 
					rs.getString("name"),
					rs.getString("address"), 
					rs.getString("address2"), 
					rs.getString("postcode"), 
					rs.getString("locality"),
					rs.getString("internalphone"), 
					rs.getString("privatephone"), 
					rs.getString("altphone"), 
					rs.getString("fax"), 
					rs.getString("source"), 
					rs.getString("sourceid"));
			return resourcePersonnel;
		}
	};
	
	private static final RowUnmapper<ResourcePersonnel> ROW_UNMAPPER = new RowUnmapper<ResourcePersonnel>() {
		public Map<String, Object> mapColumns(ResourcePersonnel resourcePersonnel) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			return mapping;
		}
	};
	
	public ResourcePersonnel findResourcePersonnelByid(String id) {
		ResourcePersonnel resourcePersonnel = getJdbcOperations().queryForObject(SQLStatements.RESOURCE_PERSONNEL_BY_ID, ROW_MAPPER, id);
		return resourcePersonnel;
	}
	
	public List<ResourcePersonnel> getDoctorsFilterByField(String field, Integer limit, Integer offset) {
		validateLimitAndOffsetValues(limit, offset);
		List<ResourcePersonnel> resourcePersonnels = getJdbcOperations().query(SQLStatements.RESOURCE_PERSONNEL_FILTER_BY_FIELD + " LIMIT " +limit + " OFFSET " + offset, ROW_MAPPER, field, field);
		return resourcePersonnels;
	}
	
	/**
	 * Returns a {@link ResourcePersonnel} of {@link Physician} which match with the param different to null
	 * @param companyId
	 * @param firstName
	 * @param lastName
	 * @param speciality
	 * @return
	 */
	public List<ResourcePersonnel> getDoctorsFilterByField(Long companyId, String firstName, String lastName, String speciality, Integer limit, Integer offset) {
		
		StringBuffer sql = new StringBuffer();
		sql.append(SQLStatements.RESOURCE_PERSONNEL_FILTER_BY_COMPANY_ID);
		
		List<Object> params = new ArrayList<>();
		params.add(companyId);
		
		if (firstName != null && !firstName.isEmpty()){
			sql.append(" AND rp.name ILIKE CONCAT('%',?,'%') ");
			params.add(firstName);
		}
		
		if (lastName != null && !lastName.isEmpty()){
			sql.append(" AND rp.name ILIKE CONCAT('%',?,'%') ");
			params.add(lastName);
		}
		
		if (speciality != null && !speciality.isEmpty()){
			sql.append(" AND rp.role ILIKE ? ");
			params.add(speciality);
		}
		
		validateLimitAndOffsetValues(limit, offset);
		sql.append(" LIMIT " +limit + " OFFSET " + offset);
		
		List<ResourcePersonnel> resourcePersonnels = getJdbcOperations().query(sql.toString(), ROW_MAPPER, params.toArray());
		return resourcePersonnels;
	}
	
	public void validateLimitAndOffsetValues(Integer limit, Integer offset) {
		if(limit == null){
			limit = defaultResultLimit;
		}
		
		if(offset == null){
			offset = defaultResultOffset;
		}	
	}
}
