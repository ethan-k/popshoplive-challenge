package eskang.popshoplive.controller;

import eskang.popshoplive.controller.dto.PhotoDTO;
import eskang.popshoplive.controller.dto.PhotoItemDTO;
import eskang.popshoplive.controller.dto.PhotoListDTO;
import eskang.popshoplive.service.PhotosService;
import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class PhotosController {

    private final PhotosService photosService;

    public PhotosController(PhotosService photosService) {
        this.photosService = photosService;
    }

    @GetMapping("/photos")
    HttpResponseDTO<List<PhotoListDTO>> photos() {
        return new HttpResponseDTO<>(photosService.getPhotos());
    }

    @PostMapping("/photos")
    @ResponseStatus(HttpStatus.CREATED)
    void uploadPhoto(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("title") String title,
            @RequestParam("description") String description
    ) {
        this.photosService.uploadPhoto(photo, title, description);
    }

    @GetMapping("/images/{uuid}/{photoname:.+}")
    public void serveFile(@PathVariable("uuid") String uuid, @PathVariable("photoname") String photoname, HttpServletResponse response) throws IOException {
        Resource file = photosService.getPhotoFile(uuid, photoname);
        StreamUtils.copy(file.getInputStream(), response.getOutputStream());
    }

    @GetMapping("/photos/{uuid}")
    @ResponseBody
    public HttpResponseDTO<PhotoItemDTO> getImageInfo(@PathVariable String uuid) {
        return new HttpResponseDTO<>(photosService.getPhoto(uuid));
    }

    @DeleteMapping("/photos/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteImage(@PathVariable String uuid) {
        photosService.deletePhoto(uuid);
    }

    @PutMapping("/photos/{uuid}")
    public HttpResponseDTO<PhotoDTO> updatePhoto(
            @PathVariable String uuid,
            @RequestParam("title") String title,
            @RequestParam("description") String description
    ) {
        return new HttpResponseDTO<>(photosService.updatePhoto(uuid, title, description));
    }
}
