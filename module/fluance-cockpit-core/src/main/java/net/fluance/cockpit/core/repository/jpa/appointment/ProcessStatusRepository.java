package net.fluance.cockpit.core.repository.jpa.appointment;

import java.util.List;

import org.springframework.data.repository.Repository;

import net.fluance.cockpit.core.model.jpa.appointment.ProcessStatus;

public interface ProcessStatusRepository extends Repository<ProcessStatus, Long> {
	public List<ProcessStatus> findByIdCompanyIdOrderByStep(Integer compannyId);
}
