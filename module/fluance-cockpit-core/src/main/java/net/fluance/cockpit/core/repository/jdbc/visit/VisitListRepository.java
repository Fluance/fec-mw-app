package net.fluance.cockpit.core.repository.jdbc.visit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.CompanyReference;
import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.visit.VisitInfo;
import net.fluance.cockpit.core.model.jdbc.visit.VisitList;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class VisitListRepository extends JdbcRepository<VisitList, Integer> {

	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultVisitListOrderBy}")
	private String defaultResultVisitListOrderBy;

	public static final RowMapper<VisitList> ROW_MAPPER = new RowMapper<VisitList>() {
		public VisitList mapRow(ResultSet rs, int rowNum) throws SQLException {
			int nbrecords = rs.getInt("nb_records");
			Long nb = SqlUtils.getLong(true, rs, "nb");
			Date admitdt = rs.getTimestamp("admitdt");
			Date dischargedt = rs.getTimestamp("dischargedt");
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
			String financialclass = rs.getString("financialclass");
			String financialclassdesc = rs.getString("financialclassdesc");
			Integer attendingPhysicianId = rs.getInt("attendingPhysicianId");
			String attendingPhysicianLastname = rs.getString("attendingPhysicianLastname");
			String attendingPhysicianFirstname = rs.getString("attendingPhysicianFirstname");
			
			VisitInfo visitInfo = new VisitInfo();
			visitInfo.setNb(nb);
			visitInfo.setAdmitDate(admitdt);
			visitInfo.setDischargeDate(dischargedt);
			visitInfo.setPatientclass(patientclass);
			visitInfo.setPatientclassdesc(patientclassdesc);
			visitInfo.setPatienttype(patienttype);
			visitInfo.setPatienttypedesc(patienttypedesc);
			visitInfo.setPatientcase(patientcase);
			visitInfo.setPatientcasedesc(patientcasedesc);
			visitInfo.setHospservice(hospservice);
			visitInfo.setHospservicedesc(hospservicedesc);
			visitInfo.setAdmissiontype(admissiontype); 
			visitInfo.setAdmissiontypedesc(admissiontypedesc);
			visitInfo.setPatientunit(patientunit);
			visitInfo.setPatientunitdesc(patientunitdesc);
			visitInfo.setFinancialclass(financialclass);
			visitInfo.setFinancialclassdesc(financialclassdesc);
			visitInfo.setAttendingPhysicianId(attendingPhysicianId);
			visitInfo.setAttendingPhysicianLastname(attendingPhysicianLastname);
			visitInfo.setAttendingPhysicianFirstname(attendingPhysicianFirstname);
			
			Integer companyid = SqlUtils.getInt(true, rs, "company_id");
			String name = rs.getString("name");
			String code = rs.getString("code");
			CompanyReference company = new CompanyReference(companyid, name, code);
			
			VisitList visitList = new VisitList();
			visitList.setNb_records(nbrecords);					
			visitList.setVisitInfo(visitInfo);
			visitList.setCompany(company);
			
			return visitList;
		}
	};

	private static final RowUnmapper<VisitList> ROW_UNMAPPER = new RowUnmapper<VisitList>() {
		@Override
		public Map<String, Object> mapColumns(VisitList t) {
			return null;
		}
	};

	public VisitListRepository() {
		this(MappingsConfig.TABLE_NAMES.get("bmv_patient_visits_list"));
	}

	/**
	 * 
	 * @param tableName
	 */
	public VisitListRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER,
				new TableDescription(tableName, null, "nb","patient_id","company_id", "name", "code",
						"admitdt","dischargedt","patientclass",
						"patientclassdesc","patienttype","patienttypedesc",
						"patientcase","patientcasedesc","hospservice",
						"hospservicedesc","admissiontype","admissiontypedesc",
						"patientunit","patientunitdesc","patientroom",
						"patientbed","priorroom","priorbed",
						"admitsource","financialclass","financialclassdesc",
						"attendingPhysicianId","attendingPhysicianLastname","attendingPhysicianFirstname") 
				);
	}

	public List<VisitList> findByCompanyIdAndPatientId(int companyId, int patientId, boolean openvisits, String orderBy, String sortorder, Integer limit, Integer offset){
		if (limit == null){
			limit = defaultResultLimit;
		}
		if (offset == null){
			offset = defaultResultOffset;
		}
		if (orderBy == null || orderBy.isEmpty())
		{
			orderBy = defaultResultVisitListOrderBy;
		}
		if(sortorder!=null){
			orderBy=orderBy.concat(" "+sortorder);
		}
		if(openvisits==true){
			return getJdbcOperations().query(SQLStatements.PATIENT_OPEN_VISITS_BY_COMPANY_ID_AND_PID.replace("order by ?", "order by "+orderBy), ROW_MAPPER, companyId, patientId, limit, offset);
		}
		return getJdbcOperations().query(SQLStatements.PATIENT_VISITS_BY_COMPANY_ID_AND_PID.replace("order by ?", "order by "+orderBy), ROW_MAPPER, companyId, patientId, limit, offset);
	}

	public List<VisitList> findByPatientId(long patientId, boolean openvisits, String orderBy, String sortorder, Integer limit, Integer offset){
		if (limit == null){
			limit = defaultResultLimit;
		}
		if (offset == null){
			offset = defaultResultOffset;
		}
		if (orderBy == null || orderBy.isEmpty())
		{
			orderBy = defaultResultVisitListOrderBy;
		}
		if(sortorder!=null){
			orderBy=orderBy.concat(" "+sortorder);
		}
		if(openvisits == true){
			return getJdbcOperations().query(SQLStatements.PATIENT_OPEN_VISITS_BY_PID_.replace("order by ?", "order by "+orderBy), ROW_MAPPER, patientId, limit, offset);
		}
		if(openvisits == true){
			return getJdbcOperations().query(SQLStatements.PATIENT_OPEN_VISITS_BY_PID_.replace("order by ?", "order by "+orderBy), ROW_MAPPER, patientId, limit, offset);
		}
		return getJdbcOperations().query(SQLStatements.PATIENT_VISITS_BY_PID.replace("order by ?", "order by "+orderBy), ROW_MAPPER, patientId, limit, offset);
	}
	
	/**
	 * Amount of visits on a specific date.
	 * @param companyid	Clinic that receives visits
	 * @param date	Date of Visits
	 * @param isHosp	Return only those that are hospitalized
	 * @param isAmb	Return only those that are not hospitalized
	 * @return
	 */
	public Integer visitCountByDate(Integer companyid, Date date, Boolean isHosp, Boolean isAmb) {
		
		String query = SQLStatements.VISIT_COUNT_BY_DATE;
		
		if(BooleanUtils.isFalse(isAmb)) {
			query = query.concat(SQLStatements.IS_HOSP);
		}
		
		if(BooleanUtils.isFalse(isHosp)) {
			query = query.concat(SQLStatements.IS_AMB);
		}
		
		return getJdbcOperations().queryForObject(query, Integer.class, companyid, date);
	}

}
