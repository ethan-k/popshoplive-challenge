package eskang.popshoplive.service;

import eskang.popshoplive.PopshopConfiguration;
import eskang.popshoplive.controller.dto.PhotoItemDTO;
import eskang.popshoplive.controller.dto.PhotoListDTO;
import eskang.popshoplive.repository.PhotoRepository;
import eskang.popshoplive.repository.model.Photo;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    public void uploadPhoto(MultipartFile photoFile, String title, String description) {
        Path rootLocation = Paths.get(this.popshopConfiguration.getFileUploadFolderPath());
        try {
            if (photoFile.isEmpty()) {
                throw new PhotoFileException("Failed to save a empty file.");
            }
            String originalFilename = photoFile.getOriginalFilename().toLowerCase();
            String thumbNameFileName = "thumbnail-" + photoFile.getOriginalFilename();
            if (!originalFilename.matches(IMAGE_FiLE_EXTENSION_PATTERN)) {
                throw new PhotoFileException("Given a file is not a image file");
            }

            Path destinationFile = rootLocation.resolve(originalFilename)
                    .normalize();
            Path thumbNameFile = rootLocation.resolve(
                            Paths.get(thumbNameFileName))
                    .normalize();

            try (InputStream inputStream = photoFile.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);

                // For the sake of assignment, we don't do resizing of a thumbnail image
                Files.copy(inputStream, thumbNameFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
            Photo photo = new Photo();
            photo.setDescription(description);
            photo.setTitle(title);
            photo.setThumbnailUrl("/" + popshopConfiguration.getFileUploadFolderName() + "/" + thumbNameFileName);
            photo.setFullPictureUrl("/" + popshopConfiguration.getFileUploadFolderName() + "/" + originalFilename);
            photo.setUuid(UUID.randomUUID());
            photoRepository.save(photo);
        } catch (IOException e) {
            throw new PhotoFileException("Failed to store file.", e);
        }
    }

    public Resource getPhotoFile(String filename) {
        try {
            Path file = Paths.get(this.popshopConfiguration.getFileUploadFolderPath()).resolve(filename);
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
}
