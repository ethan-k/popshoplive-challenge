package eskang.popshoplive.service;

import eskang.popshoplive.controller.dto.PhotoListDTO;
import eskang.popshoplive.repository.PhotoRepository;
import eskang.popshoplive.repository.model.Photo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotosService {

    private final PhotoRepository photoRepository;

    public PhotosService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public List<PhotoListDTO> getPhotos() {
        List<Photo> photos = photoRepository.getPhotos();
        return photos.stream().map(photo -> new PhotoListDTO(
                photo.getUuid().toString(),
                photo.getTitle(),
                photo.getDescription(),
                photo.getThumbnailUrl()
        )).collect(Collectors.toList());
    }
}
