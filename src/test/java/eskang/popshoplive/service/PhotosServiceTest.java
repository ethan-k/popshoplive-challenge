package eskang.popshoplive.service;

import eskang.popshoplive.PopshopliveApplication;
import eskang.popshoplive.controller.dto.PhotoListDTO;
import eskang.popshoplive.repository.PhotoRepository;
import eskang.popshoplive.repository.infra.SpringDataPhotoRepository;
import eskang.popshoplive.repository.model.Photo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = PopshopliveApplication.class)
class PhotosServiceTest {


    @Autowired
    PhotosService photosService;

    @Autowired
    SpringDataPhotoRepository springDataPhotoRepository;

    @Autowired
    PhotoRepository photoRepository;


    @Test
    void getPhotos() {
        String uuid = "f900b4f4-2522-44dd-8b75-c96864d7228b";

        Photo photo= new Photo();
        photo.setTitle("title");
        photo.setUuid(UUID.fromString(uuid));
        photo.setDescription("description");
        photo.setThumbnailUrl("thumnailurl");
        photo.setUrl("photo");

        Photo savedPhoto = springDataPhotoRepository.save(photo);
        PhotoListDTO expectedPhotoListDto = new PhotoListDTO(
                savedPhoto.getUuid().toString(),
                savedPhoto.getTitle(),
                savedPhoto.getDescription(),
                savedPhoto.getThumbnailUrl()
                );
        List<PhotoListDTO> expected = List.of(expectedPhotoListDto);

        List<PhotoListDTO> photos = photosService.getPhotos();

        Assertions.assertEquals(expected, photos);
    }
}