package net.fluance.cockpit.core.model.jdbc.patientfiles;

import java.util.Date;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.cockpit.core.model.CompanyReference;
import net.fluance.cockpit.core.model.PayloadDisplayLogName;
import net.fluance.commons.json.JsonDateTimeSerializer;

public class PatientFile implements Persistable<Long>, PayloadDisplayLogName {

	private long id;
	private long pid;
	private String fileName;
	private String path;
	@ApiModelProperty( dataType="java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date creationDate;
	private CompanyReference company;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	@JsonIgnore
	public boolean isNew() {
		return false;
	}

	public PatientFile(long id, long pid, String fileName, String path, Date creationDate, CompanyReference company) {
		this.id = id;
		this.pid = pid;
		this.fileName = fileName;
		this.path = path;
		this.creationDate = creationDate;
		this.company = company;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@JsonIgnore
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public CompanyReference getCompany() {
		return company;
	}

	public void setCompany(CompanyReference company) {
		this.company = company;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String displayName() {
		return fileName;
	}
}
