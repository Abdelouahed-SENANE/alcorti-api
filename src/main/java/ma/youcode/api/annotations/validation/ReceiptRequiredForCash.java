//package ma.youcode.api.annotations.validation;
//
//import jakarta.validation.Constraint;
//import jakarta.validation.Payload;
////import ma.youcode.api.annotations.validation.impl.ReceiptRequiredForCashValidator;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//@Constraint(validatedBy = ReceiptRequiredForCashValidator.class)
//@Target({ElementType.TYPE})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface ReceiptRequiredForCash {
//    String message() default "Receipt is required for cash payment.";
//    Class<?>[] groups() default {};
//    Class<? extends Payload>[] payload() default {};
//}
