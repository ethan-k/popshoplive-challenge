package eskang.popshoplive.service;

import eskang.popshoplive.PopshopliveApplication;
import eskang.popshoplive.controller.dto.PhotoListDTO;
import eskang.popshoplive.repository.PhotoRepository;
import eskang.popshoplive.repository.infra.SpringDataPhotoRepository;
import eskang.popshoplive.repository.model.Photo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.UUID;

@SpringBootTest
class PhotosServiceTest {

    @Autowired
    PhotosService photosService;

    @Autowired
    SpringDataPhotoRepository springDataPhotoRepository;

    @Test
    void getPhotos() {
        String uuid = "f900b4f4-2522-44dd-8b75-c96864d7228b";
        Photo photo = new Photo();
        photo.setTitle("title");
        photo.setUuid(UUID.fromString(uuid));
        photo.setDescription("description");
        photo.setThumbnailUrl("thumnailurl");
        photo.setFullPictureUrl("photo");

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

    @Test
    void savePhoto() {
        MockMultipartFile multipartFile = new MockMultipartFile("photo", "test.jpeg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                this.getClass().getResource("/images/test-image.jpg").getFile().getBytes());

        photosService.uploadPhoto(multipartFile, "title", "description");

        Photo photo = springDataPhotoRepository.findAll().get(0);

        Assertions.assertAll(() -> {
            Assertions.assertEquals("description", photo.getDescription());
            Assertions.assertEquals("title", photo.getTitle());
            Assertions.assertEquals("/images/thumbnail-test.jpeg", photo.getThumbnailUrl());
            Assertions.assertEquals("/images/test.jpeg", photo.getFullPictureUrl());
        });
    }

    @AfterEach
    void cleanup() {
        springDataPhotoRepository.deleteAll();
    }
}