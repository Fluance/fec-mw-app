package net.fluance.cockpit.core.model.jdbc.intervention;

public class Diagnosis {

	private String description;
	private Integer rank;

	public Diagnosis(String description, Integer rank) {
		this.description = description;
		this.rank = rank;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
}
