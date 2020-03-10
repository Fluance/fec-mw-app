package net.fluance.cockpit.core.model.jdbc.visit;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.fluance.cockpit.core.model.CompanyReference;

@SuppressWarnings("serial")
public class VisitList implements Persistable<Integer> {

	@JsonProperty("nbRecords")
	private int nb_records;
	private VisitInfo visitInfo;
	private CompanyReference company;

	public VisitList(){}

	public int getNb_records() {
		return nb_records;
	}

	public void setNb_records(int nb_records) {
		this.nb_records = nb_records;
	}

	public VisitInfo getVisitInfo() {
		return visitInfo;
	}

	public void setVisitInfo(VisitInfo visitInfo) {
		this.visitInfo = visitInfo;
	}

	public CompanyReference getCompany() {
		return company;
	}

	public void setCompany(CompanyReference company) {
		this.company = company;
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		return true;
	}

	@JsonIgnore
	@Override
	public Integer getId() {
		return null;
	}

}
