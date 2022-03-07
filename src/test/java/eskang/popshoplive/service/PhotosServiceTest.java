package eskang.popshoplive.service;

import eskang.popshoplive.controller.dto.PhotoDTO;
import eskang.popshoplive.controller.dto.PhotoItemDTO;
import eskang.popshoplive.controller.dto.PhotoListDTO;
import eskang.popshoplive.repository.infra.SpringDataPhotoRepository;
import eskang.popshoplive.repository.model.Photo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
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
        String uuid = "f900b4f4-2522-44dd-8b75-c96864d7228b";
        MockMultipartFile multipartFile = new MockMultipartFile("photo", "test.jpeg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                this.getClass().getResource("/images/" + uuid + "/test-image.jpg").getFile().getBytes());

        photosService.uploadPhoto(multipartFile, "title", "description");

        Photo photo = springDataPhotoRepository.findAll().get(0);

        Assertions.assertAll(() -> {
            Assertions.assertEquals("description", photo.getDescription());
            Assertions.assertEquals("title", photo.getTitle());
            Assertions.assertEquals("/images/" + photo.getUuid().toString() + "/thumbnail-test.jpeg", photo.getThumbnailUrl());
            Assertions.assertEquals("/images/" + photo.getUuid()  + "/test.jpeg", photo.getFullPictureUrl());
        });
    }

    @Test
    void getFile() {
        Resource photoFile = photosService.getPhotoFile("f900b4f4-2522-44dd-8b75-c96864d7228b", "test-image.jpg");
        Assertions.assertAll(() -> {
            Assertions.assertEquals("test-image.jpg", photoFile.getFilename());
        });
    }

    @Test
    void getPhoto() {
        String uuid = "f900b4f4-2522-44dd-8b75-c96864d7228b";
        Photo photo = new Photo();
        photo.setTitle("title");
        photo.setUuid(UUID.fromString(uuid));
        photo.setDescription("description");
        photo.setThumbnailUrl("/images/thumbnail-image.jpeg");
        photo.setFullPictureUrl("/images/image.jpeg");

        springDataPhotoRepository.save(photo);

        PhotoItemDTO photoFile = photosService.getPhoto(uuid);

        Assertions.assertAll(() -> {
            Assertions.assertEquals("f900b4f4-2522-44dd-8b75-c96864d7228b", photoFile.getUuid());
            Assertions.assertEquals("description", photoFile.getDescription());
            Assertions.assertEquals("title", photoFile.getTitle());
            Assertions.assertEquals("/images/image.jpeg", photoFile.getFullPictureUrl());
        });
    }

    @Test
    void shouldThrowExceptionWhenFileDoesNotExist() {

        String uuid = UUID.randomUUID().toString();
        Photo photo = new Photo();
        photo.setTitle("title");
        photo.setUuid(UUID.fromString(uuid));
        photo.setDescription("description");
        photo.setThumbnailUrl("/images/not-exist-image.jpeg");
        photo.setFullPictureUrl("/images/not-exist.jpeg");
        springDataPhotoRepository.save(photo);

        Assertions.assertThrows(PhotoFileNotFoundException.class, () -> {
            photosService.deletePhoto(uuid);
        });
    }

    @Test
    void updatePhotoInfo() {
        String uuid = UUID.randomUUID().toString();
        Photo photo = new Photo();
        photo.setTitle("title");
        photo.setUuid(UUID.fromString(uuid));
        photo.setDescription("description");
        photo.setThumbnailUrl("/images/not-exist-image.jpeg");
        photo.setFullPictureUrl("/images/not-exist.jpeg");
        springDataPhotoRepository.save(photo);

        PhotoDTO photoDTO = photosService.updatePhoto(uuid, "updated title", "updated description");

        Assertions.assertAll(() -> {
            Assertions.assertEquals("updated title" , photoDTO.getTitle());
            Assertions.assertEquals("updated description" , photoDTO.getDescription());
        });
    }


    @AfterEach
    void cleanup() {
        springDataPhotoRepository.deleteAll();
    }
}