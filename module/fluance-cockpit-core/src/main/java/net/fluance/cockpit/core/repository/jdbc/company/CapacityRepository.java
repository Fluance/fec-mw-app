/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.company;

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
import net.fluance.cockpit.core.model.jdbc.company.Capacity;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class CapacityRepository extends JdbcRepository<Capacity, Integer> {
	
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;
	
	private static final String ROOM_NUMBER = "roomnumber";
	private static final String NB_BED = "nbbed";

	public CapacityRepository() {
		this(MappingsConfig.TABLE_NAMES.get("m_bmv_capacity"));
	}

	public CapacityRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, ""));
	}

	public static final RowMapper<Capacity> ROW_MAPPER = new RowMapper<Capacity>() {
		public Capacity mapRow(ResultSet rs, int rowNum) throws SQLException{
			return new Capacity(SqlUtils.getString(true, rs, ROOM_NUMBER),SqlUtils.getInt(true, rs, NB_BED));

		}
	};

	public static final RowUnmapper<Capacity> ROW_UNMAPPER = new RowUnmapper<Capacity>() {
		public Map<String, Object> mapColumns(Capacity capacity) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put(ROOM_NUMBER, capacity.getRoomnumber());
			mapping.put(NB_BED, capacity.getNbbed());
			return mapping;
		}
	};

	/**
	 * Returns a {@link Capacity} list which elements contain the Company ID and the Patient Unit or the Hosp Service (optional)
	 * @param companyId
	 * @param patientUnit
	 * @param hospService
	 * @param limit
	 * @param offset
	 * @return
	 */
	public List<Capacity> getCapacity(Integer companyId, String patientUnit, String hospService, Integer limit, Integer offset) {
		List<Object> params = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append(SQLStatements.CAPACITY_BY_COMPANY);
		params.add(companyId);
		if(patientUnit!=null && !patientUnit.isEmpty()){
			sql.append(SQLStatements.AND_BY_UNIT);
			params.add(patientUnit);
		}
		if(hospService!=null && !hospService.isEmpty()){
			sql.append(SQLStatements.AND_BY_SERVICE);
			params.add(hospService);
		}
		sql.append(SQLStatements.CAPACITY_ORDER_BY);
		if (limit == null || limit < 0) {
			limit = defaultResultLimit;
		}
		if (offset == null || offset < 0) {
			offset = defaultResultOffset;
		}
		params.add(limit);
		params.add(offset);
		return getJdbcOperations().query(sql.toString(), ROW_MAPPER, params.toArray());
	}

}
