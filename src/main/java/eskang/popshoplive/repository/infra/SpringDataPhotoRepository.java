package eskang.popshoplive.repository.infra;

import eskang.popshoplive.controller.dto.PhotoItemDTO;
import eskang.popshoplive.repository.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface SpringDataPhotoRepository extends JpaRepository<Photo, Long> {

    Photo findByUuid(UUID uuid);
}
