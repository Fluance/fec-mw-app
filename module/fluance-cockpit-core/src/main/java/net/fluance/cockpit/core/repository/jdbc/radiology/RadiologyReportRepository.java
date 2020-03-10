package net.fluance.cockpit.core.repository.jdbc.radiology;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
import net.fluance.cockpit.core.model.jdbc.radiology.RadiologyReport;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class RadiologyReportRepository extends JdbcRepository<RadiologyReport, Integer> {
	
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultRadiologyReportListOrderBy}")
	private String defaultResultRadiologyReportListOrderBy;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultRadiologyReportListSortOrder}")
	private String defaultResultRadiologyReportListSortOrder;
	
	public RadiologyReportRepository() {
		this(MappingsConfig.TABLE_NAMES.get("bmv_radiological_report"));
	}

	public RadiologyReportRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, ""));
	}
	
	public static final RowMapper<RadiologyReport> ROW_MAPPER = new RowMapper<RadiologyReport>() {
		public RadiologyReport mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new RadiologyReport(rs.getString("studyuid"), SqlUtils.getLong(true, rs, "patient_id"), SqlUtils.getInt(true, rs, "company_id"),
					rs.getString("ordernb"), rs.getTimestamp("studydt"), rs.getString("report"), rs.getString("completion"), rs.getString("verification"),
					rs.getString("referringphysician"), rs.getString("recordphysician"), rs.getString("performingphysician"), rs.getString("readingphysician"), rs.getLong("rank"));
		}
	};

	public static final RowUnmapper<RadiologyReport> ROW_UNMAPPER = new RowUnmapper<RadiologyReport>() {
		public Map<String, Object> mapColumns(RadiologyReport radiologyReport) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("studyuid", radiologyReport.getStudyUid());
			mapping.put("patient_id", radiologyReport.getPatientId());
			mapping.put("company_id", radiologyReport.getCompanyId());
			mapping.put("ordernb", radiologyReport.getOrderNb());
			mapping.put("studydt", radiologyReport.getStudyDate());
			mapping.put("report", radiologyReport.getReport());
			mapping.put("completion", radiologyReport.getCompletion());
			mapping.put("verification", radiologyReport.getVerification());
			mapping.put("referringphysician", radiologyReport.getReferringPhysician());
			mapping.put("recordphysician", radiologyReport.getRecordPhysician());
			mapping.put("performingphysician", radiologyReport.getPerformingPhysician());
			mapping.put("readingphysician", radiologyReport.getReadingPhysician());
			mapping.put("rank", radiologyReport.getRank());
			return mapping;
		}
	};
	
	public List<RadiologyReport> findByPatientId(Long patientid, String orderby, String sortorder, Integer limit, Integer offset) {
		if (limit == null){
			limit = defaultResultLimit;
		}
		if (offset == null){
			offset = defaultResultOffset;
		}
		if (orderby == null || orderby.isEmpty())
		{
			orderby=defaultResultRadiologyReportListOrderBy;
		}
		if (sortorder == null)
		{
			sortorder=defaultResultRadiologyReportListSortOrder;
		}
		orderby=orderby.concat(" "+sortorder);
		String query = SQLStatements.FIND_RADIOLOGY_REPORT_BY_PATIENTID + " ORDER BY " + orderby +" LIMIT " +limit + " OFFSET " +offset;
		List<RadiologyReport> radiologies = getJdbcOperations().query(query, ROW_MAPPER, patientid);
		return radiologies;
	}
	
	public RadiologyReport findByStudyUId(String uid) {
		String query = SQLStatements.FIND_RADIOLOGY_REPORT_BY_STUDYUID;
		return getJdbcOperations().queryForObject(query.replace("(?)", uid), ROW_MAPPER, uid);
		//return new RadiologyReport("studyUid", (long)10020, 1, "505922", null, "report", "completion", "verification", null, null, null, null);
	}

	public Integer findByPatientIdCount(Long patientid) {
		return getJdbcOperations().queryForObject(SQLStatements.FIND_RADIOLOGY_REPORT_BY_PATIENTID_COUNT, Integer.class, patientid);
	}
}
