package eskang.popshoplive.repository.infra;

import eskang.popshoplive.controller.dto.PhotoItemDTO;
import eskang.popshoplive.repository.PhotoRepository;
import eskang.popshoplive.repository.model.Photo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

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

    @Override
    public Photo save(Photo photo) {
        return this.springDataPhotoRepository.save(photo);
    }

    @Override
    public Photo getPhotoByUuid(String uuid) {
        return this.springDataPhotoRepository.findByUuid(UUID.fromString(uuid));
    }
}
