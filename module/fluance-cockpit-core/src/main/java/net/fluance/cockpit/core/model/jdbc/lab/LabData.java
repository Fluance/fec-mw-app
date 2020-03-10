package net.fluance.cockpit.core.model.jdbc.lab;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateTimeSerializer;

@SuppressWarnings("serial")
public class LabData implements Persistable<Integer> {
	
	@JsonProperty("pid")
	private Long patientId;
	
	private String groupName;
	
	@JsonProperty("observationDate")
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date observationdt;
	@JsonProperty("analysisName")
	private String analysisname;
	private String value;
	@JsonProperty("valueType")
	private String valuetype;
	private String unit;
	private String refrange;
	@JsonProperty("abnormalFlag")
	private String abnormalflag;
	@JsonProperty("abnormalFlagDesc")
	private String abnormalflagdesc;
	@JsonProperty("resultStatus")
	private String resultstatus;
	@JsonProperty("resultStatusDesc")
	private String resultstatusdesc;
	private JsonNode comments;

	public LabData() {}
	
	public LabData(Long patientId, String groupName, Timestamp observationdt, String analysisname, String value,
			String valuetype, String unit, String refrange, String abnormalflag, String abnormalflagdesc,
			String resultstatus, String resultstatusdesc, JsonNode comments) {
		super();
		this.patientId = patientId;
		this.groupName = groupName;
		this.observationdt = observationdt;
		this.analysisname = analysisname;
		this.value = value;
		this.valuetype = valuetype;
		this.unit = unit;
		this.refrange = refrange;
		this.abnormalflag = abnormalflag;
		this.abnormalflagdesc = abnormalflagdesc;
		this.resultstatus = resultstatus;
		this.resultstatusdesc = resultstatusdesc;
		this.comments = comments;
	}
	
	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Date getObservationdt() {
		return observationdt;
	}

	public void setObservationdt(Timestamp observationdt) {
		this.observationdt = observationdt;
	}

	public String getAnalysisname() {
		return analysisname;
	}

	public void setAnalysisname(String analysisname) {
		this.analysisname = analysisname;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValuetype() {
		return valuetype;
	}

	public void setValuetype(String valuetype) {
		this.valuetype = valuetype;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRefrange() {
		return refrange;
	}

	public void setRefrange(String refrange) {
		this.refrange = refrange;
	}

	public String getAbnormalflag() {
		return abnormalflag;
	}

	public void setAbnormalflag(String abnormalflag) {
		this.abnormalflag = abnormalflag;
	}

	public String getAbnormalflagdesc() {
		return abnormalflagdesc;
	}

	public void setAbnormalflagdesc(String abnormalflagdesc) {
		this.abnormalflagdesc = abnormalflagdesc;
	}

	public String getResultstatus() {
		return resultstatus;
	}

	public void setResultstatus(String resultstatus) {
		this.resultstatus = resultstatus;
	}

	public String getResultstatusdesc() {
		return resultstatusdesc;
	}

	public void setResultstatusdesc(String resultstatusdesc) {
		this.resultstatusdesc = resultstatusdesc;
	}

	public JsonNode getComments() {
		return comments;
	}

	public void setComments(JsonNode comments) {
		this.comments = comments;
	}

	@JsonIgnore
	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@JsonIgnore
	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
