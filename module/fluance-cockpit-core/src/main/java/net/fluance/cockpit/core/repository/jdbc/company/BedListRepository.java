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

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.SQLStatements;
import net.fluance.cockpit.core.model.jdbc.company.BedList;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class BedListRepository extends JdbcRepository<BedList, Integer> {

	public BedListRepository() {
		this(MappingsConfig.TABLE_NAMES.get("m_bmv_patientbed"));
	}

	public BedListRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, ""));
	}

	public static final RowMapper<BedList> ROW_MAPPER = new RowMapper<BedList>() {
		public BedList mapRow(ResultSet rs, int rowNum) throws SQLException{
			return new BedList(SqlUtils.getInt(true, rs, "patientbed"));

		}
	};

	public static final RowUnmapper<BedList> ROW_UNMAPPER = new RowUnmapper<BedList>() {
		public Map<String, Object> mapColumns(BedList patientbed) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("patientbed", patientbed.getPatientbed());
			return mapping;
		}
	};

	public List<BedList> findCompanyIdAndPatientUnitAndPatientroom(int companyId, String patientunit, String patientRoom) {
		List<BedList> lPatientBed = getJdbcOperations().query(SQLStatements.FIND_PATIENT_BED_LIST_BY_COMPANYID_AND_PATIENTUNIT_AND_PATIENTROOM, ROW_MAPPER, companyId, patientunit, patientRoom );
		return lPatientBed;
	}
	
	public List<BedList> findCompanyIdAndHospserviceAndpatientroom(int companyId, String hospservice, String patientroom) {
		List<BedList> lPatientBed = getJdbcOperations().query(SQLStatements.FIND_PATIENT_BED_LIST_BY_COMPANYID_AND_HOSPSERVICE_AND_PATIENTROOM, ROW_MAPPER, companyId, hospservice, patientroom);
		return lPatientBed;
	}
	
	public List<BedList> findCompanyIdAndPatientunitAndHospserviceAndPatientroom(int companyId, String patientunit, String hospservice, String patientRoom) {
		List<BedList> lPatientBed = getJdbcOperations().query(SQLStatements.FIND_PATIENT_BED_LIST_BY_COMPANYID_AND_PATIENTUNIT_AND_HOSPSERVICE_AND_PATIENTROOM, ROW_MAPPER, companyId, patientunit, hospservice, patientRoom);
		return lPatientBed;
	}

}
