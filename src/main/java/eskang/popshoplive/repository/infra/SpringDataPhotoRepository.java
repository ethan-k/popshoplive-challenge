package eskang.popshoplive.repository.infra;

import eskang.popshoplive.repository.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface SpringDataPhotoRepository extends JpaRepository<Photo, Long> {

}
