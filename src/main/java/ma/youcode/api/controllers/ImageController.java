package ma.youcode.api.controllers;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.models.Image;
import ma.youcode.api.services.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/images")
@RequiredArgsConstructor

public class ImageController {

    private final ImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> readImageById(@PathVariable String id) {
        Image image = imageService.getImage(id);

        return  ResponseEntity.ok()
                .header("Content-Type", image.getImageType())
                .body(image.getImageData());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable String id) {
        imageService.delete(id);
        return  ResponseEntity.noContent().build();
    }


}
