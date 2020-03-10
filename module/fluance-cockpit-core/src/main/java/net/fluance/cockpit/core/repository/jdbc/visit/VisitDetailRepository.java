package net.fluance.cockpit.core.repository.jdbc.visit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.CompanyReference;
import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.visit.VisitDetail;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class VisitDetailRepository extends JdbcRepository<VisitDetail, Long> {
	
	public static final RowMapper<VisitDetail> ROW_MAPPER = new RowMapper<VisitDetail>() {
		public VisitDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long nb = SqlUtils.getLong(true, rs, "nb");
			Long pid = SqlUtils.getLong(true, rs, "patient_id");
			Date admitdt = rs.getTimestamp("admitdt");
			Date dischargedt = rs.getTimestamp("dischargedt");
			Date expdischargedt = rs.getTimestamp("expdischargedt");
			String patientclass = rs.getString("patientclass");
			String patientclassdesc = rs.getString("patientclassdesc");
			String patienttype = rs.getString("patienttype");
			String patienttypedesc = rs.getString("patienttypedesc");
			String patientcase = rs.getString("patientcase");
			String patientcasedesc =  rs.getString("patientcasedesc");
			String hospservice =  rs.getString("hospservice");
			String hospservicedesc = rs.getString("hospservicedesc");
			String admissiontype = rs.getString("admissiontype");
			String admissiontypedesc = rs.getString("admissiontypedesc");
			String patientunit = rs.getString("patientunit");
			String patientunitdesc =  rs.getString("patientunitdesc");
			String patientroom = rs.getString("patientroom");
			String patientbed = rs.getString("patientbed");
			String priorroom = rs.getString("priorroom");
			String priorbed = rs.getString("priorbed");
			String priorunit =  rs.getString("priorunit");
			String priorunitdesc = rs.getString("priorunitdesc");
			String admitsource = rs.getString("admitsource");
			String admitsourcedesc = rs.getString("admitsourcedesc");
			String financialclass = rs.getString("financialclass");
			String financialclassdesc = rs.getString("financialclassdesc");
			String hl7code = rs.getString("hl7code");
			VisitDetail v = new VisitDetail(nb, pid, admitdt, dischargedt, expdischargedt, patientclass, patientclassdesc, patienttype, patienttypedesc, patientcase, patientcasedesc, hospservice, hospservicedesc, admissiontype, admissiontypedesc, patientunit, patientunitdesc, patientroom, patientbed, priorroom, priorbed, priorunit, priorunitdesc, admitsource, admitsourcedesc, financialclass, financialclassdesc, hl7code, null);
			return v;
		}
	};
	
	public static final RowMapper<VisitDetail> ROW_MAPPER_JOIN_COMPANY = new RowMapper<VisitDetail>() {
		public VisitDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long nb = SqlUtils.getLong(true, rs, "nb");
			Long pid = SqlUtils.getLong(true, rs, "patient_id");
			Date admitdt = rs.getTimestamp("admitdt");
			Date dischargedt = rs.getTimestamp("dischargedt");
			Date expdischargedt = rs.getTimestamp("expdischargedt");
			String patientclass = rs.getString("patientclass");
			String patientclassdesc = rs.getString("patientclassdesc");
			String patienttype = rs.getString("patienttype");
			String patienttypedesc = rs.getString("patienttypedesc");
			String patientcase = rs.getString("patientcase");
			String patientcasedesc =  rs.getString("patientcasedesc");
			String hospservice =  rs.getString("hospservice");
			String hospservicedesc = rs.getString("hospservicedesc");
			String admissiontype = rs.getString("admissiontype");
			String admissiontypedesc = rs.getString("admissiontypedesc");
			String patientunit = rs.getString("patientunit");
			String patientunitdesc =  rs.getString("patientunitdesc");
			String patientroom = rs.getString("patientroom");
			String patientbed = rs.getString("patientbed");
			String priorroom = rs.getString("priorroom");
			String priorbed = rs.getString("priorbed");
			String priorunit =  rs.getString("priorunit");
			String priorunitdesc = rs.getString("priorunitdesc");
			String admitsource = rs.getString("admitsource");
			String admitsourcedesc = rs.getString("admitsourcedesc");
			String financialclass = rs.getString("financialclass");
			String financialclassdesc = rs.getString("financialclassdesc");
			Integer companyid = SqlUtils.getInt(true, rs, "company_id");
			String name = rs.getString("name");
			String code = rs.getString("code");
			String hl7code = rs.getString("hl7code");
			CompanyReference company = new CompanyReference(companyid, name, code);
			VisitDetail v = new VisitDetail(nb, pid, admitdt, dischargedt, expdischargedt, patientclass, patientclassdesc, patienttype, patienttypedesc, patientcase, patientcasedesc, hospservice, hospservicedesc, admissiontype, admissiontypedesc, patientunit, patientunitdesc, patientroom, patientbed, priorroom, priorbed, priorunit, priorunitdesc, admitsource, admitsourcedesc, financialclass, financialclassdesc, hl7code, company);
			return v;
		}
	};


	private static final RowUnmapper<VisitDetail> ROW_UNMAPPER = new RowUnmapper<VisitDetail>() {
		@Override
		public Map<String, Object> mapColumns(VisitDetail t) {
			return null;
		}
	};

	public VisitDetailRepository() {
		this(MappingsConfig.TABLE_NAMES.get("bmv_visit_detail"));
	}

	/**
	 * 
	 * @param tableName
	 */
	public VisitDetailRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER,
				new TableDescription(tableName, null, "nb") );
	}
	
	public VisitDetail findByNb(long nb){
		return getJdbcOperations().queryForObject(SQLStatements.VISIT_DETAIL, ROW_MAPPER_JOIN_COMPANY, nb);
	}

}
