package net.fluance.cockpit.core.repository.jdbc.physician;

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
import net.fluance.commons.sql.SqlUtils;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.physician.Physician;
import net.fluance.cockpit.core.model.jdbc.resourcepersonnel.ResourcePersonnel;
import net.fluance.cockpit.core.sql.PhysicianSQLStatements;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class PhysicianRepository extends JdbcRepository<Physician, Integer> {

	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private Integer defaultResultLimit;	
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private Integer defaultResultOffset;

	public PhysicianRepository() {
		this(MappingsConfig.TABLE_NAMES.get("Physician"));
	}

	/**
	 * 
	 * @param tableName
	 */
	public PhysicianRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "id"));
	}

	public static final RowMapper<Physician> ROW_MAPPER = new RowMapper<Physician>() {
		public Physician mapRow(ResultSet rs, int rowNum) throws SQLException {
			//TODO: refactore it
			Physician Physician = new Physician(rs.getInt("id"), rs.getString("firstname"), rs.getString("lastname"),
					rs.getString("prefix"), SqlUtils.getInt(true, rs, "staffid"), rs.getString("alternateid"),
					rs.getString("alternateidname"), rs.getString("speciality"), rs.getString("address"),
					rs.getString("locality"), rs.getString("postcode"), rs.getString("canton"), rs.getString("country"),
					rs.getString("complement"), rs.getString("language"), rs.getInt("company_id"),
					rs.getString("physpecialitydesc"));
			return Physician;
		}
	};
	
	public static final RowMapper<ResourcePersonnel> ROW_MAPPER_PERSONELLE = new RowMapper<ResourcePersonnel>() {
		public ResourcePersonnel mapRow(ResultSet resultSet, int rowNum) throws SQLException {
			ResourcePersonnel resourcePersonnel = new ResourcePersonnel();
			resourcePersonnel.setId(String.valueOf(resultSet.getInt("id")));
			resourcePersonnel.setCompany_id(resultSet.getInt("company_id"));
			resourcePersonnel.setRsid(resultSet.getInt("rsid"));
			resourcePersonnel.setStaffid(resultSet.getString("staffid"));
			resourcePersonnel.setRole(resultSet.getString("role"));
			resourcePersonnel.setName(resultSet.getString("name"));
			resourcePersonnel.setAddress(resultSet.getString("address"));
			resourcePersonnel.setAddress2(resultSet.getString("address2"));
			resourcePersonnel.setPostcode(resultSet.getString("postcode"));
			resourcePersonnel.setLocality(resultSet.getString("locality"));
			resourcePersonnel.setInternalphone(resultSet.getString("internalphone"));
			resourcePersonnel.setPrivatephone(resultSet.getString("privatephone"));
			resourcePersonnel.setAltphone(resultSet.getString("altphone"));
			resourcePersonnel.setFax(resultSet.getString("fax"));
			resourcePersonnel.setSource(resultSet.getString("source"));
			resourcePersonnel.setSourceid(resultSet.getString("sourceid"));
			return resourcePersonnel;
		}
	};
	

	private static final RowUnmapper<Physician> ROW_UNMAPPER = new RowUnmapper<Physician>() {
		public Map<String, Object> mapColumns(Physician Physician) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			return mapping;
		}
	};

	public Physician findPhysicianByid(Integer doctorid) {
		Physician Physician = getJdbcOperations().queryForObject(SQLStatements.PHYSICIAN_BY_ID, ROW_MAPPER, doctorid);
		return Physician;
	}
	
	public List<Physician> getDoctorsFilterByField(String field, Integer limit, Integer offset) {
		validateLimitAndOffsetValues(limit, offset);
		List<Physician> physicians = getJdbcOperations().query(SQLStatements.PHYSICIAN_FILTER_BY_FIELD + " LIMIT " +limit + " OFFSET " + offset, ROW_MAPPER, field, field, field);
		return physicians;
	}
	
	/**
	 * Returns a {@link List} of {@link Physician} which match with the param different to null
	 * @param companyId
	 * @param firstName
	 * @param lastName
	 * @param speciality
	 * @return
	 */
	public List<Physician> getDoctorsFilterByField(Long companyId, String firstName, String lastName, String speciality, Integer limit, Integer offset) {
		
		StringBuffer sql = new StringBuffer();
		sql.append(SQLStatements.PHYSICIAN_FILTER_BY_COMPANY_ID);
		
		List<Object> params = new ArrayList<>();
		params.add(companyId);
		
		if (firstName != null && !firstName.isEmpty()){
			sql.append(" AND pd.firstname ILIKE ? ");
			params.add(firstName);
		}
		
		if (lastName != null && !lastName.isEmpty()){
			sql.append(" AND pd.lastname ILIKE ? ");
			params.add(lastName);
		}
		
		if (speciality != null && !speciality.isEmpty()){
			sql.append(" AND pd.speciality ILIKE ? ");
			params.add(speciality);
		}
		
		validateLimitAndOffsetValues(limit, offset);
		sql.append(" LIMIT " +limit + " OFFSET " + offset);
		
		List<Physician> physicians = getJdbcOperations().query(sql.toString(), ROW_MAPPER, params.toArray());

		return physicians;
	}

	public List<ResourcePersonnel> getDoctorsByIdFromFResourcePersonell(Long companyId, String staffId) {
		return getJdbcOperations().query(PhysicianSQLStatements.PHYSICIAN_BY_ID_PERSONNEL, ROW_MAPPER_PERSONELLE, staffId, companyId);
	}

	public List<Physician> getDoctorsByIdFromPhysician(Long companyId, Long staffId) {
		return getJdbcOperations().query(PhysicianSQLStatements.PHYSICIAN_BY_ID_PHYSICIAN, ROW_MAPPER, staffId, companyId);

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
