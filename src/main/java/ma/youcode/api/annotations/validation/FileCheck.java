package ma.youcode.api.annotations.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ma.youcode.api.annotations.validation.impl.FileCheckValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FileCheckValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD , ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileCheck {
    String message() default "Invalid image format.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    long maxSize() default 5;
    String[] allowedTypes() default {"image/jpeg" , "image/png"};
}
