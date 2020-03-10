package net.fluance.cockpit.core.repository.jpa.whiteboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteboardRoomsConfigurationEntity;
import net.fluance.cockpit.core.model.jpa.whiteboard.WhiteboardRoomsConfigurationID;

@Repository
public interface WhiteboardRoomConfigurationRepository extends CrudRepository<WhiteboardRoomsConfigurationEntity, WhiteboardRoomsConfigurationID>{
	
	@Query(value = "select * from room_configuration where company_code = ?1 and hospservice = ?2 ORDER BY room asc", nativeQuery = true)
	public List<WhiteboardRoomsConfigurationEntity> findByCompanyAndService(String companyCode, String hospservice);
}
