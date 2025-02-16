package ma.youcode.api.services;

import ma.youcode.api.models.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage(MultipartFile file);
    Image getImage(String id);
    void delete(String id);
}
