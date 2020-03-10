/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.invoice;

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
import net.fluance.cockpit.core.model.jdbc.invoice.Invoice;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class InvoiceRepository extends JdbcRepository<Invoice, Long> {

	public InvoiceRepository() {
		this(MappingsConfig.TABLE_NAMES.get("bmv_invoice_detail"));
	}

	public InvoiceRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, "id"));
	}

	public static final RowMapper<Invoice> ROW_MAPPER = new RowMapper<Invoice>() {
		public Invoice mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new Invoice(SqlUtils.getLong(true, rs, "id"), rs.getTimestamp("invdt"), rs.getDouble("total"),
					rs.getDouble("balance"), rs.getString("apdrg_code"), rs.getString("apdrg_descr"),
					rs.getString("mdc_code"), rs.getString("mdc_descr"), rs.getString("name"), rs.getString("code"), SqlUtils.getLong(true, rs, "visit_nb"), 
					SqlUtils.getLong(false, rs, "guarantor_id"));

		}
	};

	public static final RowUnmapper<Invoice> ROW_UNMAPPER = new RowUnmapper<Invoice>() {
		public Map<String, Object> mapColumns(Invoice invoice) {
			Map<String, Object> mapping = new LinkedHashMap<String, Object>();
			mapping.put("id", invoice.getId());
			mapping.put("invdt", invoice.getInvdt());
			mapping.put("total", invoice.getTotal());
			mapping.put("balance", invoice.getBalance());
			mapping.put("apdrg_code", invoice.getApdrg_code());
			mapping.put("apdrg_descr", invoice.getApdrg_descr());
			mapping.put("mdc_code", invoice.getMdc_code());
			mapping.put("mdc_descr", invoice.getMdc_descr());
			mapping.put("name", invoice.getGuarantor_name());
			mapping.put("code", invoice.getGuarantor_code());

			return mapping;
		}
	};

//	public Invoice findById(long id) {
//		Invoice invoice = getJdbcOperations().queryForObject(SQLStatements.FIND_INVOICE_BY_ID, ROW_MAPPER, id);
//		return invoice;
//	}

}
