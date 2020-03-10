package net.fluance.cockpit.core.model.jdbc.visit;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GuarantorList implements Persistable<Integer> {

	private long nb_records;
	private Integer guarantor_id;
	private String name;
	private String code;
	private String address;
	private String address2;
	private String locality;
	private String postcode;
	private String canton;
	private String country;
	private String complement;
	private String begindate;
	private String enddate;
	private boolean occasional;
	

	public GuarantorList(long nbRecords, Integer id, String name, String code, String address, String address2, String locality,
			String postcode, String canton, String country, String complement, String begindate, String enddate,
			boolean occasional) {
		this.nb_records = nbRecords;
		this.guarantor_id = id;
		this.name = name;
		this.code = code;
		this.address = address;
		this.address2 = address2;
		this.locality = locality;
		this.postcode = postcode;
		this.canton = canton;
		this.country = country;
		this.complement = complement;
		this.begindate = begindate;
		this.enddate = enddate;
		this.occasional = occasional;
	}
	
	public GuarantorList(){}
	
	public Integer getGuarantor_id(){
		return this.guarantor_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCanton() {
		return canton;
	}

	public void setCanton(String canton) {
		this.canton = canton;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public boolean getOccasional() {
		return occasional;
	}

	public void setOccasional(boolean occasional) {
		this.occasional = occasional;
	}

	public void setId(Integer id) {
		this.guarantor_id = id;
	}

	@JsonIgnore
	public long getNb_records() {
		return nb_records;
	}

	public void setNb_records(long nb_records) {
		this.nb_records = nb_records;
	}

	@Override
	@JsonIgnore
	public Integer getId() {
		return this.guarantor_id;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}

}
