package net.fluance.cockpit.core.repository.jpa.whiteboard;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteBoardViewEntity;


@Repository
public interface WhiteBoardJpaRepository extends CrudRepository<WhiteBoardViewEntity, Long>{
 WhiteBoardViewEntity findByVisitNumber(Long visitNumber);

}
