package ma.youcode.api.utilities;

import jakarta.annotation.PostConstruct;
import ma.youcode.api.exceptions.FileStorageException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileServiceStorage  {

    private static final Logger log = LogManager.getLogger(FileServiceStorage.class);

    public static Path rootLocation;

    public FileServiceStorage(@Value("${app.storage.directory}") String directory) {
        rootLocation = Paths.get(directory);
    }

    @PostConstruct
    public static void init() {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new FileStorageException("Could not create storage directory", e);
        }
    }

    public static String store(MultipartFile file) {
        try {

            String originalFilename = file.getOriginalFilename();
            String filename = generateFilename(originalFilename);

            Path filePath = rootLocation.resolve(filename);
            file.transferTo(filePath);

            return filename;
        } catch (IOException e) {
            log.error("could not store file {} ", e.getMessage());
            throw new FileStorageException(String.format("Could not store file %s. Please try again!", file.getOriginalFilename()), e);
        }
    }

    private static String generateFilename(String originalFilename) {
        String fileExtension = getFileExtension(originalFilename);
        return UUID.randomUUID() + fileExtension;
    }


    private static String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }

        return filename.substring(filename.lastIndexOf("."));
    }

}
