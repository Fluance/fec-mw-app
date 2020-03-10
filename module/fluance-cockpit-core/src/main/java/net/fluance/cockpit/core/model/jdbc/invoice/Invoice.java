package net.fluance.cockpit.core.model.jdbc.invoice;

import java.util.Date;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.fluance.cockpit.core.model.PayloadDisplayLogName;
import net.fluance.commons.json.JsonDateTimeSerializer;

@SuppressWarnings("serial")
public class Invoice implements Persistable<Long>,PayloadDisplayLogName{

	private Long id;
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonProperty("date")
	private Date invdt;
	@JsonProperty("totalAmount")
	private double total;
	private double balance;
	@JsonProperty("apdrgCode")
	private String apdrg_code;
	@JsonProperty("apdrgDescription")
	private String apdrg_descr;
	@JsonProperty("mdcCode")
	private String mdc_code;
	@JsonProperty("mdcDescription")
	private String mdc_descr;
	@JsonProperty("guarantorName")
	private String guarantor_name;
	@JsonProperty("guarantorCode")
	private String guarantor_code;
	private long visit_nb;
	private long guarantorId;

	public Invoice(Long idInvoice, Date invdt, Double total, Double balance, String apdrg_code, String apdrg_descr,
			String mdc_code, String mdc_descr, String name, String code, long visit_nb, long gurantorId) {
		this.id = idInvoice;
		this.invdt = invdt;
		this.total = total;
		this.balance = balance;
		this.apdrg_code = apdrg_code;
		this.apdrg_descr = apdrg_descr;
		this.mdc_code = mdc_code;
		this.mdc_descr = mdc_descr;
		this.guarantor_name = name;
		this.guarantor_code = code;
		this.visit_nb = visit_nb;
		this.guarantorId = gurantorId;
	}
	
	public Invoice(){}

	public Date getInvdt() {
		return invdt;
	}

	public void setInvdt(Date invdt) {
		this.invdt = invdt;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getApdrg_code() {
		return apdrg_code;
	}

	public void setApdrg_code(String apdrg_code) {
		this.apdrg_code = apdrg_code;
	}

	public String getApdrg_descr() {
		return apdrg_descr;
	}

	public void setApdrg_descr(String apdrg_descr) {
		this.apdrg_descr = apdrg_descr;
	}

	public String getMdc_code() {
		return mdc_code;
	}

	public void setMdc_code(String mdc_code) {
		this.mdc_code = mdc_code;
	}

	public String getMdc_descr() {
		return mdc_descr;
	}

	public void setMdc_descr(String mdc_descr) {
		this.mdc_descr = mdc_descr;
	}

	public String getGuarantor_name() {
		return guarantor_name;
	}

	public void setGuarantor_name(String guarantor_name) {
		this.guarantor_name = guarantor_name;
	}

	public String getGuarantor_code() {
		return guarantor_code;
	}

	public void setGuarantor_code(String guarantor_code) {
		this.guarantor_code = guarantor_code;
	}

	public void setIdInvoice(Long idInvoice) {
		this.id = idInvoice;
	}

	public long getGuarantorId() {
		return guarantorId;
	}

	public void setGuarantorId(long guarantorId) {
		this.guarantorId = guarantorId;
	}

	@JsonIgnore
	public Long getVisit_nb() {
		return visit_nb;
	}

	@JsonIgnore
	public void setVisit_nb(long visit_nb) {
		this.visit_nb = visit_nb;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String displayName() {
		return id.toString();
	}
}
