package net.fluance.cockpit.core.model.jdbc.radiology;

import java.util.Date;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateTimeSerializer;

@SuppressWarnings("serial")
public class Radiology implements Persistable<Integer> {

	private String serieUid;
	private Long patientId;
	private Integer companyId;
	private String orderNb;
	private String orderObs;
	private String orderUrl;
	private String diagnosticService;
	private String dsDescription;
	private String serieObs;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date serieObsDate;


	public Radiology() {}

	public Radiology(String serieUid, Long patientId, Integer companyId, String orderNb, String orderObs,
			String orderUrl, String diagnosticService, String dsDescription, String serieObs, Date serieObsDate) {
		this.serieUid = serieUid;
		this.patientId = patientId;
		this.companyId = companyId;
		this.orderNb = orderNb;
		this.orderObs = orderObs;
		this.orderUrl = orderUrl;
		this.diagnosticService = diagnosticService;
		this.dsDescription = dsDescription;
		this.serieObs = serieObs;
		this.serieObsDate = serieObsDate;
	}

	public String getSerieUid() {
		return serieUid;
	}

	public void setSerieUid(String serieUid) {
		this.serieUid = serieUid;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getOrderNb() {
		return orderNb;
	}

	public void setOrderNb(String orderNb) {
		this.orderNb = orderNb;
	}

	public String getOrderObs() {
		return orderObs;
	}

	public void setOrderObs(String orderObs) {
		this.orderObs = orderObs;
	}

	public String getOrderUrl() {
		return orderUrl;
	}

	public void setOrderUrl(String orderUrl) {
		this.orderUrl = orderUrl;
	}

	public String getDiagnosticService() {
		return diagnosticService;
	}

	public void setDiagnosticService(String diagnosticService) {
		this.diagnosticService = diagnosticService;
	}

	public String getDsDescription() {
		return dsDescription;
	}

	public void setDsDescription(String dsDescription) {
		this.dsDescription = dsDescription;
	}

	public String getSerieObs() {
		return serieObs;
	}

	public void setSerieObs(String serieObs) {
		this.serieObs = serieObs;
	}

	public Date getSerieObsDate() {
		return serieObsDate;
	}

	public void setSerieObsDate(Date serieObsDate) {
		this.serieObsDate = serieObsDate;
	}

	@JsonIgnore
	@Override
	public Integer getId() {
		return null;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return false;
	}
}
