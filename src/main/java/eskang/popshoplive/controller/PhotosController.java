package eskang.popshoplive.controller;

import eskang.popshoplive.controller.dto.PhotoListDTO;
import eskang.popshoplive.service.PhotosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/photos")
public class PhotosController {

    private final PhotosService photosService;

    public PhotosController(PhotosService photosService) {
        this.photosService = photosService;
    }

    @GetMapping
    HttpResponseDTO<List<PhotoListDTO>> photos() {
        return new HttpResponseDTO<>(photosService.getPhotos());
    }

}
