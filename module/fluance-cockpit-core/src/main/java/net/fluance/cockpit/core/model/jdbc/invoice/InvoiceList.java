package net.fluance.cockpit.core.model.jdbc.invoice;

import java.util.Date;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import net.fluance.commons.json.JsonDateTimeSerializer;

@SuppressWarnings("serial")
public class InvoiceList implements Persistable<Long> {

	@JsonProperty("nbRecords")
	private int nb_records;
	private Long id;
	@JsonProperty("date")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date invdt;
	@JsonProperty("totalAmount")
	private double total;
	private double balance;
	@JsonProperty("guarantorId")
	private Long guarantor_id;

	public InvoiceList(int nb_records, Long id, Date invdt, double total, double balance, Long guarantor_id) {
		this.nb_records = nb_records;
		this.id = id;
		this.invdt = invdt;
		this.total = total;
		this.balance = balance;
		this.guarantor_id = guarantor_id;
	}
	
	public InvoiceList(){}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNb_records() {
		return nb_records;
	}

	public void setNb_records(int nb_records) {
		this.nb_records = nb_records;
	}

	public Date getInvdt() {
		return invdt;
	}

	public void setInvdt(Date invdt) {
		this.invdt = invdt;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public Long getGuarantor_id() {
		return guarantor_id;
	}

	public void setGuarantor_id(Long guarantor_id) {
		this.guarantor_id = guarantor_id;
	}

}
