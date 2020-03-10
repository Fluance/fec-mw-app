/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;
import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.company.ServiceList;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class ServiceListRepository extends JdbcRepository<ServiceList, Integer> {

	public ServiceListRepository() {
		this(MappingsConfig.TABLE_NAMES.get("m_bmv_hospservice"));
	}

	public ServiceListRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, ""));
	}

	public static final RowMapper<ServiceList> ROW_MAPPER = new RowMapper<ServiceList>() {
		public ServiceList mapRow(ResultSet rs, int rowNum) throws SQLException{
			return new ServiceList(rs.getString("hospservice"), rs.getString("hospservicedesc"));

		}
	};
	
	public static final RowMapper<ServiceList> ROW_MAPPER_WITHCOUNT = new RowMapper<ServiceList>() {
		public ServiceList mapRow(ResultSet rs, int rowNum) throws SQLException{
			return new ServiceList(rs.getString("hospservice"), rs.getString("hospservicedesc"), SqlUtils.getInt(true, rs, "nb_patients"));
		}
	};
	
	public static final RowUnmapper<ServiceList> ROW_UNMAPPER = new RowUnmapper<ServiceList>() {
		public Map<String, Object> mapColumns(ServiceList ServiceList) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("hospservice", ServiceList.getHospService());
			mapping.put("hospservicedesc", ServiceList.getHospServiceDesc());
			return mapping;
		}
	};

	public List<ServiceList> findByCompanyId(int companyId) {
		List<ServiceList> u= getJdbcOperations().query(SQLStatements.FIND_SERVICE_LIST_BY_ID, ROW_MAPPER, companyId);
		return u;
	}

	public List<ServiceList> findByCompanyIdWithCount(int companyId, List<String> hospServices) {
		
		String services = new Gson().toJson(hospServices);
		
		List<ServiceList> u= getJdbcOperations().query(SQLStatements.FIND_SERVICE_LIST_BY_ID_WITH_COUNT, ROW_MAPPER_WITHCOUNT, services, companyId);
		return u;
	}
	
}
