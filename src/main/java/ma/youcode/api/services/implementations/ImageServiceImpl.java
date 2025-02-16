package ma.youcode.api.services.implementations;

import lombok.RequiredArgsConstructor;
import ma.youcode.api.events.OnShipmentDeletedSuccessEvent;
import ma.youcode.api.exceptions.ResourceNotFoundException;
import ma.youcode.api.exceptions.UploadImageException;
import ma.youcode.api.models.Image;
import ma.youcode.api.repositories.ImageRepository;
import ma.youcode.api.services.ImageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private static final Logger log = LogManager.getLogger(ImageServiceImpl.class);
    private @Value("${app.images.path}") String IMAGE_PATH;
    public final ImageRepository imageRepository;

    @Override
    public String uploadImage(MultipartFile image) {

        try {
            Image img = new Image();
            img.setImageName(image.getOriginalFilename());
            img.setImageType(image.getContentType());
            img.setImageData(image.getBytes());
            imageRepository.save(img);
            return IMAGE_PATH + img.getImageId() + extractExtension(image.getOriginalFilename());
        } catch (IOException e) {
            throw new UploadImageException("Failed to upload image." , e);
        }
    }

    @Override
    public Image getImage(String url) {
        UUID id = extractImageId(url);
        return imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found."));
    }

    @Override
    public void delete(String url) {
        UUID uuid = extractImageIdFromUrl(url);
        imageRepository.findById(uuid).ifPresent(imageRepository::delete);
    }

    @EventListener
    public void onShipmentDeletedSuccess(OnShipmentDeletedSuccessEvent event) {
        event.getShipment().getShipmentItems().forEach(item -> {
            delete(item.getImageURL());
        });
    }

    private String extractExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private UUID extractImageId(String url) {
        if (url == null) {
            return null;
        }
        return UUID.fromString(url.substring(0, url.lastIndexOf(".")));
    }

    private UUID extractImageIdFromUrl(String url) {
        if (url == null) {
            return null;
        }
        String[] imageName = url.split("/images/");
        return extractImageId(imageName[1]);
    }

}
