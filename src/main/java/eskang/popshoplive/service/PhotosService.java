package eskang.popshoplive.service;

import eskang.popshoplive.PopshopConfiguration;
import eskang.popshoplive.controller.dto.PhotoDTO;
import eskang.popshoplive.controller.dto.PhotoItemDTO;
import eskang.popshoplive.controller.dto.PhotoListDTO;
import eskang.popshoplive.repository.PhotoRepository;
import eskang.popshoplive.repository.model.Photo;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PhotosService {

    private final PhotoRepository photoRepository;
    private final PopshopConfiguration popshopConfiguration;
    private final static String IMAGE_FiLE_EXTENSION_PATTERN = "(.*/)*.+\\.(png|jpg|gif|bmp|jpeg|PNG|JPG|GIF|BMP|JPEG)$";

    public PhotosService(PhotoRepository photoRepository, PopshopConfiguration popshopConfiguration) {
        this.photoRepository = photoRepository;
        this.popshopConfiguration = popshopConfiguration;
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

    @Transactional
    public void uploadPhoto(MultipartFile photoFile, String title, String description) {
        Path rootLocation = Paths.get(this.popshopConfiguration.getFileUploadFolderPath());
        try {
            if (photoFile.isEmpty()) {
                throw new PhotoFileException("Failed to save a empty file.");
            }
            String originalFilename = photoFile.getOriginalFilename().toLowerCase();
            String thumbNameFileName = "thumbnail-" + originalFilename;
            if (!originalFilename.matches(IMAGE_FiLE_EXTENSION_PATTERN)) {
                throw new PhotoFileException("Given a file is not a image file");
            }
            UUID uuid = UUID.randomUUID();
            Path originalFile = rootLocation.resolve(
                    Paths.get(uuid.toString(), originalFilename))
                    .normalize();
            Path thumbNameFile = rootLocation.resolve(
                            Paths.get(uuid.toString(), thumbNameFileName))
                    .normalize();

            try (InputStream inputStream = photoFile.getInputStream()) {
                originalFile.getParent().toFile().mkdirs();
                Files.copy(inputStream, originalFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }

            try (InputStream inputStream = photoFile.getInputStream()) {
                // For the sake of assignment, we don't do resizing of a thumbnail image
                thumbNameFile.getParent().toFile().mkdirs();
                Files.copy(inputStream, thumbNameFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
            Photo photo = new Photo();
            photo.setDescription(description);
            photo.setTitle(title);
            photo.setOriginalFileName(originalFilename);
            photo.setThumbnailUrl("/" + popshopConfiguration.getFileUploadFolderName() + "/" + uuid + "/" + thumbNameFileName);
            photo.setFullPictureUrl("/" + popshopConfiguration.getFileUploadFolderName() + "/" + uuid + "/" + originalFilename);
            photo.setUuid(uuid);
            photoRepository.save(photo);
        } catch (IOException e) {
            throw new PhotoFileException("Failed to store file.", e);
        }
    }

    public Resource getPhotoFile(String uuid, String filename) {
        try {
            Path file = Paths.get(this.popshopConfiguration.getFileUploadFolderPath()).resolve(Paths.get(uuid, filename));
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new PhotoFileNotFoundException("Could not read photo file: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new PhotoFileNotFoundException("Could not read photo file: " + filename, e);
        }
    }

    public PhotoItemDTO getPhoto(String uuid) {
        Photo photo = photoRepository.getPhotoByUuid(uuid);
        PhotoItemDTO photoItemDTO = new PhotoItemDTO(
                photo.getUuid().toString(),
                photo.getTitle(),
                photo.getDescription(),
                photo.getFullPictureUrl()
        );
        return photoItemDTO;
    }

    @Transactional
    public void deletePhoto(String uuid) {
        Photo photo = photoRepository.getPhotoByUuid(uuid);
        if (photo == null)
            return;
        File originalFile = new File(this.popshopConfiguration.getFileUploadFolderPath() + "/" + uuid + "/" + photo.getOriginalFileName());
        File thumbnailFile = new File(this.popshopConfiguration.getFileUploadFolderPath() + "/" + uuid + "/" +  photo.getThumbnailFileName());
        if (!originalFile.exists() || !thumbnailFile.exists()) {
            throw new PhotoFileNotFoundException("Could not find a photo originalFile: " + uuid);
        }
        originalFile.delete();
        thumbnailFile.delete();

        photoRepository.deleteByUuid(uuid);
    }

    @Transactional
    public PhotoDTO updatePhoto(String uuid, String title, String description) {
        Photo photo = photoRepository.getPhotoByUuid(uuid);
        if (photo == null)
            throw new PhotoFileException("Photo does not exist");

        photo = photoRepository.save(photo);
        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setUuid(photo.getUuid().toString());
        photoDTO.setDescription(description);
        photoDTO.setTitle(title);
        photoDTO.setThumbnailUrl(photo.getThumbnailUrl());
        photoDTO.setFullPictureUrl(photo.getFullPictureUrl());
        return photoDTO;
    }
}
