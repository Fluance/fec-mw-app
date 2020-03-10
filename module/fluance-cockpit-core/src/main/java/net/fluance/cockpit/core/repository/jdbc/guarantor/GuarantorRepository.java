package net.fluance.cockpit.core.repository.jdbc.guarantor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.nurkiewicz.jdbcrepository.JdbcRepository;
import com.nurkiewicz.jdbcrepository.RowUnmapper;
import com.nurkiewicz.jdbcrepository.TableDescription;

import net.fluance.cockpit.core.model.MappingsConfig;
import net.fluance.cockpit.core.model.jdbc.guarantor.GuarantorDetail;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class GuarantorRepository extends JdbcRepository<GuarantorDetail, Integer> {

	public GuarantorRepository() {
		this(MappingsConfig.TABLE_NAMES.get("bmv_guarantor_detail"));
	}

	public GuarantorRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "id"));
	}

	public static final RowMapper<GuarantorDetail> ROW_MAPPER = new RowMapper<GuarantorDetail>() {
		public GuarantorDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new GuarantorDetail(SqlUtils.getInt(true, rs, "id"), rs.getString("code"), rs.getString("name"), rs.getString("address"),
					rs.getString("address2"), rs.getString("locality"), rs.getString("postcode"),
					rs.getString("canton"), rs.getString("country"), rs.getString("complement"),
					rs.getString("begindate"), rs.getString("enddate"), rs.getBoolean("occasional"));
		}
	};

	public static final RowUnmapper<GuarantorDetail> ROW_UNMAPPER = new RowUnmapper<GuarantorDetail>() {
		public Map<String, Object> mapColumns(GuarantorDetail guarantor) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("id", guarantor.getIdGuarantor());
			mapping.put("code", guarantor.getCode());
			mapping.put("name", guarantor.getName());
			mapping.put("address", guarantor.getAddress());
			mapping.put("address2", guarantor.getAddress2());
			mapping.put("locality", guarantor.getLocality());
			mapping.put("postcode", guarantor.getPostcode());
			mapping.put("canton", guarantor.getCanton());
			mapping.put("complement", guarantor.getComplement());
			mapping.put("begindate", guarantor.getBegindate());
			mapping.put("enddate", guarantor.getEnddate());
			mapping.put("occasional", guarantor.isOccasional());

			return mapping;
		}
	};

//	public GuarantorDetail findById(int id) {
//		GuarantorDetail gurantor = getJdbcOperations().queryForObject(SQLStatements.FIND_GUARANTOR_BY_ID, ROW_MAPPER, id);
//		return gurantor;
//	}

	public int isGuarantorOfPatient(Long pid, Long guarantorId) {
		Integer count = getJdbcOperations().queryForObject("select count(*) FROM bmv_visit_guarantors_list VG "
				+ "INNER JOIN bmv_visit_detail V on v.nb = VG.nb "
				+ "WHERE V.patient_id = ? AND VG.id = ? "
				, Integer.class, pid, guarantorId);
		return count;
	}

}
