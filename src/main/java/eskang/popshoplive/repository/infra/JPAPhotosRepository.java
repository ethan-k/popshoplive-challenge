package eskang.popshoplive.repository.infra;

import eskang.popshoplive.repository.PhotoRepository;
import eskang.popshoplive.repository.model.Photo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JPAPhotosRepository implements PhotoRepository {

    private final SpringDataPhotoRepository springDataPhotoRepository;

    public JPAPhotosRepository(SpringDataPhotoRepository springDataPhotoRepository) {
        this.springDataPhotoRepository = springDataPhotoRepository;
    }

    @Override
    public List<Photo> getPhotos() {
        return springDataPhotoRepository.findAll();
    }
}
