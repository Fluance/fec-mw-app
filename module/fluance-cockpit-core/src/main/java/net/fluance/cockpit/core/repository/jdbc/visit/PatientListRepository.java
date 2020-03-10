package net.fluance.cockpit.core.repository.jdbc.visit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.domain.visit.PatientList;
import net.fluance.cockpit.core.model.Count;
import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.wrap.patient.AdmissionStatusEnum;
import net.fluance.cockpit.core.model.wrap.patient.PatientSexEnum;

@Repository
@Component
public class PatientListRepository extends JdbcRepository<PatientList, Long>{

	public PatientListRepository() {
		this(MappingsConfig.TABLE_NAMES.get("bmv_patientslist_detail"));
	}

	public PatientListRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER,
				new TableDescription(tableName, null, ""));
	}

	public static final RowMapper<PatientList> ROW_MAPPER = new RowMapper<PatientList>() {
		public PatientList mapRow(ResultSet rs, int rowNum) throws SQLException {
			long visitNb = rs.getLong("nb");
			long companyId = rs.getLong("company_id");
			String firstName = rs.getString("firstname");
			String lastName = rs.getString("lastname");
			Date birthDate = rs.getTimestamp("birthdate");
			String sex = PatientSexEnum.getCode(rs.getString("sex"));
			String patientUnit = rs.getString("patientunit");
			String patientRoom = rs.getString("patientroom");
			String patientBed = rs.getString("patientbed");
			String physicianPrefix = rs.getString("physician_prefix");
			String physicianFirstName = rs.getString("physician_firstname");
			String physicianLastName = rs.getString("physician_lastname");
			Date admissionDate = rs.getTimestamp("admitdt");
			Date dischargeDate = rs.getTimestamp("dischargedt");
			String financialClass = rs.getString("financialclass");
			String food = rs.getString("food");
			String mobility = rs.getString("mobility");
			PatientList patientList = new PatientList(visitNb, companyId, firstName, lastName, birthDate, sex, patientUnit, patientRoom, patientBed, physicianPrefix, physicianFirstName, physicianLastName, admissionDate, dischargeDate, financialClass, food, mobility);
			return patientList;
		}
	};

	private static final RowUnmapper<PatientList> ROW_UNMAPPER = new RowUnmapper<PatientList>() {
		@Override
		public Map<String, Object> mapColumns(PatientList t) {
			return null;
		}
	};

	public List<PatientList> getVisitsPatientList(long companyid, String patientunit, String admissionstatus, String patientroom, String patientbed){
		String queryBase = SQLStatements.VISITS_PATIENTLIST_BASE_CRITERIA;
		String searchCriteria = searchCriteria(admissionstatus, patientroom, patientbed);
		String query = queryBase + searchCriteria;
		List<PatientList> visitPatientsList = getJdbcOperations().query(query + " order by sortorder asc" , ROW_MAPPER, companyid, patientunit);
		return visitPatientsList;
	}
	
	public Count getVisitsPatientListCount(long companyid, String patientunit, String admissionstatus, String patientroom, String patientbed){
		String queryBase = SQLStatements.VISITS_PATIENTLIST_COUNT_BASE_CRITERIA;
		String searchCriteria = searchCriteria(admissionstatus, patientroom, patientbed);
		String query = queryBase + searchCriteria;
		Integer visits = getJdbcOperations().queryForObject(query, Integer.class, companyid, patientunit);
		return new Count(visits);
	}
	
	private String searchCriteria(String admissionstatus, String patientroom, String patientbed){
		String searchCriteria = "";
		if(admissionstatus != null && !admissionstatus.isEmpty()){
			AdmissionStatusEnum status = AdmissionStatusEnum.permissiveValueOf(admissionstatus);
			if(!status.getSql().isEmpty()){
				searchCriteria += " AND "+ status.getSql();
			}
		} else{
			searchCriteria += " AND "+ SQLStatements.VISITS_PATIENTLIST_ALL_CRITERIA;
		}
		if(patientroom != null && !patientroom.isEmpty()){
			searchCriteria += " AND patientroom = '"+ patientroom+"'";
		}
		if(patientbed != null && !patientbed.isEmpty()){
			searchCriteria += " AND patientbed ='"+ patientbed+"'";
		}
		return searchCriteria;
	}
}
