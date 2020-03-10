/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.physician;

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
import net.fluance.cockpit.core.model.jdbc.physician.PhysicianContact;

@Repository
@Component
public class PhysicianContactRepository extends JdbcRepository<PhysicianContact, Integer> {
	
	private static final String TABLE_NAME = "PhysicianContact";
	
	private static final String NB_TYPE = "nbtype";
	private static final String EQUIPMENT = "equipment";
	private static final String DATA = "data";
	
	public PhysicianContactRepository() {
		this(MappingsConfig.TABLE_NAMES.get(TABLE_NAME));
	}
	
	public PhysicianContactRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER,
				new TableDescription(tableName, NB_TYPE, EQUIPMENT, DATA));
	}

	public static final RowMapper<PhysicianContact> ROW_MAPPER = new RowMapper<PhysicianContact>() {
		public PhysicianContact mapRow(ResultSet rs, int rowNum) throws SQLException {

			String nbType = rs.getString(NB_TYPE);
			String equipment = rs.getString(EQUIPMENT);
			String data = rs.getString(DATA);
			
			PhysicianContact contact = new PhysicianContact( nbType, equipment, data);
			
			return contact;
		}
	};

	private static final RowUnmapper<PhysicianContact> ROW_UNMAPPER = new RowUnmapper<PhysicianContact>() {
		public Map<String, Object> mapColumns(PhysicianContact contact) {
			
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			
			mapping.put(NB_TYPE, contact.getNbType());
			mapping.put(EQUIPMENT, contact.getEquipment());
			mapping.put(DATA, contact.getData());
			
			return mapping;
		}
	};
	
	/**
	 * Get the Physician Telephones by its ID
	 * @param id
	 * @return
	 */
	public List<PhysicianContact> findTelephonesById(Integer id) {
		List<PhysicianContact> physicianContacts = getJdbcOperations().query(SQLStatements.FIND_PHYSICIAN_CONTACTS_TLF, ROW_MAPPER, id);
		return physicianContacts;
	}
	
}
