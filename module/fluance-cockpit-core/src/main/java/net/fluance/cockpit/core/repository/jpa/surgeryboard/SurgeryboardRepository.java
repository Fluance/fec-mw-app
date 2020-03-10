package net.fluance.cockpit.core.repository.jpa.surgeryboard;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.fluance.cockpit.core.model.jpa.surgeryboard.Surgeryboard;
import net.fluance.cockpit.core.model.jpa.surgeryboard.SurgeryboardPK;

@Repository
public interface SurgeryboardRepository extends CrudRepository<Surgeryboard, SurgeryboardPK> {
	
}
