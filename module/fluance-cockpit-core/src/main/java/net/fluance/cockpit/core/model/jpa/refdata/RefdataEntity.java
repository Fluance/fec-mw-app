package net.fluance.cockpit.core.model.jpa.refdata;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "refdata", schema = "ehealth")
public class RefdataEntity {

	@EmbeddedId
	private RefdataPK id;

	@Column(name="lang", columnDefinition = "bpchar")
	private String lang;

	@Column(name="codedesc")
	private String codedesc;

	public RefdataPK getId() {  return id;  }
	public String getLang() {  return lang; }
	public String getCodedesc() {  return codedesc;  }
}
