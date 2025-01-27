package ma.youcode.api.services.implementations;

import ma.youcode.api.services.FileStorageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileServiceStorageImpl implements FileStorageService {
    private static final Logger log = LogManager.getLogger(FileServiceStorageImpl.class);
    @Value("${storage.path}")
    private String STORAGE_DIRECTORY;

    @Override
    @Named("storeFile")
    public String store(MultipartFile file) {
        try {
            Path root = Paths.get(STORAGE_DIRECTORY);
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            String originalFilename =file.getOriginalFilename();
            String fileExtension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID() + fileExtension;

            Path filePath = root.resolve(filename);
            file.transferTo(filePath);
            return filename;
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new RuntimeException(String.format("Could not store file %s. Please try again!", file.getOriginalFilename()), e);
        }
    }
}
