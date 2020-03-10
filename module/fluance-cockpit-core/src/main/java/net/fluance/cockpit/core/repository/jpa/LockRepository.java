package net.fluance.cockpit.core.repository.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.fluance.cockpit.core.model.jpa.Lock;


@Repository
public interface LockRepository extends JpaRepository<Lock, Integer>{

	public Lock findByResourceIdAndResourceType(Long resourceId, String resourceType);

	@Query(value = "SELECT * from notesandpictures.lock where object_id=?1 and type=?2 and username = ?3 and domain =?4", nativeQuery = true)
	public Lock findByResourceIdAndResourceTypeAndProfile(Long resourceId, String resourceType, String username, String domain);
}
