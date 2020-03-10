package net.fluance.cockpit.core.model;

import java.util.List;

import net.fluance.cockpit.core.model.jdbc.company.Capacity;

public class Capacities {

	private List<Capacity> capacities;
	
	public Capacities(List<Capacity> capacities) {
		super();
		this.capacities = capacities;
	}

	public List<Capacity> getCapacities() {
		return capacities;
	}
	
	public void setCapacities(List<Capacity> capacities) {
		this.capacities = capacities;
	}
}
