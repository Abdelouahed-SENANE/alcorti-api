package ma.youcode.api.annotations.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ma.youcode.api.annotations.validation.FileGuard;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class FileGuardValidator implements ConstraintValidator<FileGuard, MultipartFile> {
    private long maxSize;
    private Set<String> allowedTypes;

    @Override
    public void initialize(FileGuard constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize() * 1024 * 1024;
        this.allowedTypes = Arrays.stream(constraintAnnotation.allowedTypes()).collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(MultipartFile image, ConstraintValidatorContext context) {

        if (image == null || image.isEmpty()) {
            String errMessage = "File is required!";
            addViolation(context, errMessage);
            return false;
        }
        String contentType = image.getContentType();

        if (!allowedTypes.contains(contentType)) {
            String errMessage = "Invalid file format. Allowed formats: " + String.join(", ", allowedTypes);
            addViolation(context, errMessage);
            return false;
        }

        if (image.getSize() > maxSize) {
            String errMessage = "File size exceeds the maximum allowed size of " + maxSize / (1024 * 1024) + "MB";
            addViolation(context, errMessage);
            return false;
        }

        return true;
    }

    private void addViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
