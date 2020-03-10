package net.fluance.cockpit.core.repository.jpa.picture;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.fluance.cockpit.core.model.jpa.picture.Picture;

@Repository
public interface PictureRepository extends CrudRepository<Picture, Long>{

}
