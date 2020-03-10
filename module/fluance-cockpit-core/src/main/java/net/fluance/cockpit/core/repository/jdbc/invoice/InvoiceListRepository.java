/**
 * 
 */
package net.fluance.cockpit.core.repository.jdbc.invoice;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import net.fluance.cockpit.core.model.jdbc.invoice.InvoiceList;
import net.fluance.commons.sql.SqlUtils;

@Repository
@Component
public class InvoiceListRepository extends JdbcRepository<InvoiceList, Long> {

	@Value("${net.fluance.cockpit.core.model.repository.defaultResultLimit}")
	private int defaultResultLimit;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultOffset}")
	private int defaultResultOffset;
	@Value("${net.fluance.cockpit.core.model.repository.defaultResultInvoiceListOrderBy}")
	private String defaultResultInvoiceListOrderBy;
	
	public InvoiceListRepository() {
		this(MappingsConfig.TABLE_NAMES.get("bmv_visit_guarantor_invoices_list"));
	}

	public InvoiceListRepository(String tableName) {
		super(ROW_MAPPER, ROW_UNMAPPER, new TableDescription(tableName, null, ""));
	}

	public static final RowMapper<InvoiceList> ROW_MAPPER = new RowMapper<InvoiceList>() {
		public InvoiceList mapRow(ResultSet rs, int rowNum) throws SQLException {
			return new InvoiceList(rs.getInt("nb_records"), SqlUtils.getLong(true, rs, "id"), rs.getTimestamp("invdt"), rs.getDouble("total"), rs.getDouble("balance"), SqlUtils.getLong(true, rs, "guarantor_id"));
		}
	};

	public static final RowUnmapper<InvoiceList> ROW_UNMAPPER = new RowUnmapper<InvoiceList>() {
		public Map<String, Object> mapColumns(InvoiceList invoice) {
			Map<String, Object> mapping = null;
			return mapping;
		}
	};

	public List<InvoiceList> findByVisitNb(Long visitnb, String orderBy, String sortorder, Integer limit, Integer offset){
		if (limit == null){
			limit = defaultResultLimit;
		}
		if (offset == null){
			offset = defaultResultOffset;
		}
		if (orderBy == null || orderBy.isEmpty())
		{
			orderBy = defaultResultInvoiceListOrderBy;
		}
		if(sortorder!=null){
			orderBy=orderBy.concat(" "+sortorder);
		}
		List<InvoiceList> invoiceList = getJdbcOperations().query(SQLStatements.FIND_INVOICE_LIST_BY_VISIT_NB.replace("ORDER BY ?", "ORDER BY "+orderBy), ROW_MAPPER, visitnb, limit, offset);
		return invoiceList;
	}
	

	public List<InvoiceList> findByVisitNbAndGuarantorId(Long visitnb, Long guarantorId, String orderBy, String sortorder, Integer limit, Integer offset){
		if (limit == null){
			limit = defaultResultLimit;
		}
		if (offset == null){
			offset = defaultResultOffset;
		}
		if (orderBy == null || orderBy.isEmpty())
		{
			orderBy = defaultResultInvoiceListOrderBy;
		}
		if(sortorder!=null){
			orderBy=orderBy.concat(" "+sortorder);
		}
		List<InvoiceList> invoiceList = getJdbcOperations().query(SQLStatements.FIND_INVOICE_LIST_BY_VISIT_NB_AND_GUARANTOR_ID.replace("ORDER BY ?", "ORDER BY "+orderBy), ROW_MAPPER, visitnb, guarantorId, limit, offset);
		return invoiceList;
	}

	public Integer findByVisitNbCount(Long visitnb) {
		return getJdbcOperations().queryForObject(SQLStatements.FIND_INVOICE_LIST_BY_VISIT_NB_COUNT, Integer.class, visitnb);
	}

	public Integer findByVisitNbAndGuarantorIdCount(Long visitnb, Long guarantorId){
		return getJdbcOperations().queryForObject(SQLStatements.FIND_INVOICE_LIST_BY_VISIT_NB_AND_GUARANTOR_ID_COUNT, Integer.class, visitnb, guarantorId);
	}
}
