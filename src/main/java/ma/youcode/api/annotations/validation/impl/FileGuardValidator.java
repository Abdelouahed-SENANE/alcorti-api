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
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Cannot store empty file.").addConstraintViolation();
            return false;
        }
        String contentType = image.getContentType();

        if (!allowedTypes.contains(contentType)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid file type. Allowed types: " + String.join(", ", allowedTypes)).addConstraintViolation();
            return false;
        }

        if (image.getSize() > maxSize) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File size exceeds the maximum limit of " + (maxSize / (1024 * 1024)) + " MB.").addConstraintViolation();
            return false;
        }

        return true;
    }
}
