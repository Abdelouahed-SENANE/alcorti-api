//package ma.youcode.api.annotations.validation.impl;
//
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//import ma.youcode.api.annotations.validation.ReceiptRequiredForCash;
//import ma.youcode.api.enums.PaymentMethod;
//import ma.youcode.api.payloads.requests.PaymentRequest;
//
//public class ReceiptRequiredForCashValidator implements ConstraintValidator<ReceiptRequiredForCash, PaymentRequest> {
//
//
//    @Override
//    public boolean isValid(PaymentRequest request, ConstraintValidatorContext context) {
//
//        if (PaymentMethod.CASH.name().equalsIgnoreCase(request.method())) {
//
//            if (request.receipt() == null || request.receipt().isEmpty()) {
//                context.disableDefaultConstraintViolation();
//                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
//                        .addPropertyNode("receipt")
//                        .addConstraintViolation();
//                return false;
//            }
//
//        }
//
//        return true;
//    }
//}
