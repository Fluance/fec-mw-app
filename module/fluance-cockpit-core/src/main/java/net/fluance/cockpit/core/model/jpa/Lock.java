package net.fluance.cockpit.core.model.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModelProperty;
import net.fluance.commons.json.JsonDateTimeSerializer;

@Entity
@Table(name = "lock")
public class Lock {

	@Id
	@Column(name = "id")
	@SequenceGenerator(name="lockIdSeqGenerator", sequenceName="lock_id_seq")
	@GeneratedValue(strategy=GenerationType.AUTO, generator="lockIdSeqGenerator")
	private Integer id;

	@Column(name = "object_id")
	private Long resourceId;

	@Column(name = "type")
	private String resourceType;

	@Column(name = "username")
	private String userName;

	@Column(name = "domain")
	private String domain;

	@Column(name = "expirationdt")
	@ApiModelProperty(dataType = "java.lang.String")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date expirationDate;

	public Lock(){}
	
	public Lock(Long ressourceId, String ressourceType, String userName, String domain, Date expirationDate) {
		this.resourceId = ressourceId;
		this.resourceType = ressourceType;
		this.userName = userName;
		this.domain = domain;
		this.expirationDate = expirationDate;
	}


	@JsonIgnore
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setRessourceId(Long ressourceId) {
		this.resourceId = ressourceId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setRessourceType(String ressourceType) {
		this.resourceType = ressourceType;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
}
