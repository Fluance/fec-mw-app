/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.radiology;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import net.fluance.cockpit.core.model.jdbc.radiology.Radiology;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class RadiologyRepository extends JdbcRepository<Radiology, Integer> {

	public RadiologyRepository() {
		this(MappingsConfig.TABLE_NAMES.get("bmv_radiological_serie"));
	}

	public RadiologyRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, ""));
	}

	public static final RowMapper<Radiology> ROW_MAPPER = new RowMapper<Radiology>() {
		public Radiology mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Radiology(rs.getString("serieiuid"), SqlUtils.getLong(true, rs, "patient_id"), SqlUtils.getInt(true, rs, "company_id"),
					rs.getString("ordernb"), rs.getString("orderobs"), rs.getString("orderurl"), rs.getString("diagnosticservice"),
					rs.getString("dsdesc"), rs.getString("serieobs"), rs.getTimestamp("serieobsdt"));
		}
	};

	public static final RowUnmapper<Radiology> ROW_UNMAPPER = new RowUnmapper<Radiology>() {
		public Map<String, Object> mapColumns(Radiology radiology) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("serieiuid", radiology.getSerieUid());
			mapping.put("patient_id", radiology.getPatientId());
			mapping.put("company_id", radiology.getCompanyId());
			mapping.put("ordernb", radiology.getOrderNb());
			mapping.put("orderobs", radiology.getOrderObs());
			mapping.put("orderurl", radiology.getOrderUrl());
			mapping.put("diagnosticservice", radiology.getDiagnosticService());
			mapping.put("dsdesc", radiology.getDsDescription());
			mapping.put("serieobs", radiology.getSerieObs());
			mapping.put("serieobsdt", radiology.getSerieObsDate());
			return mapping;
		}
	};

	public List<Radiology> findByPatientId(Long patientid) {
		List<Radiology> lRadiologies = getJdbcOperations().query(SQLStatements.FIND_RADIOLOGY_BY_PATIENTID,
				ROW_MAPPER, patientid);
		return lRadiologies;
	}

	public Integer findByPatientIdCount(Long patientid) {
		return getJdbcOperations().queryForObject(SQLStatements.FIND_RADIOLOGY_BY_PATIENTID_COUNT, Integer.class, patientid);
	}

}
