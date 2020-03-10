package net.fluance.cockpit.core.repository.jpa.picture;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.fluance.cockpit.core.model.jpa.picture.PictureHistoryDetail;

@Repository
public interface PictureHistoryDetailRepository extends CrudRepository<PictureHistoryDetail, Long>{}
