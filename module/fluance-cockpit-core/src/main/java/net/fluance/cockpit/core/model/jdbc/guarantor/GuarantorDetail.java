package net.fluance.cockpit.core.model.jdbc.guarantor;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.fluance.cockpit.core.model.PayloadDisplayLogName;

@SuppressWarnings("serial")
public class GuarantorDetail implements Persistable<Integer>, PayloadDisplayLogName {

	private Integer idGuarantor;
	private String code;
	private String name;
	private String address;
	private String address2;
	private String locality;
	private String postcode;
	private String canton;
	private String country;
	private String complement;
	@JsonProperty("beginDate")
	private String begindate;
	@JsonProperty("endDate")
	private String enddate;
	private boolean occasional;

	public GuarantorDetail(Integer id, String code, String name, String address, String address2, String locality,
			String postcode, String canton, String country, String complement, String begindate, String enddate,
			boolean occasional) {
		this.idGuarantor = id;
		this.code = code;
		this.name = name;
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
	
	public GuarantorDetail(){}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public boolean isOccasional() {
		return occasional;
	}

	public void setOccasional(boolean occasional) {
		this.occasional = occasional;
	}

	public void setIdGuarantor(Integer id) {
		this.idGuarantor = id;
	}

	@JsonIgnore
	public Integer getIdGuarantor() {
		return idGuarantor;
	}

	@Override
	@JsonIgnore
	public Integer getId() {
		return this.idGuarantor;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public String displayName() {
		return this.code;
	}

}
