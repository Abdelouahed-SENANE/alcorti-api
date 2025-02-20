package ma.youcode.api.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ma.youcode.api.annotations.validation.FileCheck;
import ma.youcode.api.annotations.validation.ReceiptRequiredForCash;
import ma.youcode.api.enums.PaymentMethod;
import org.springframework.web.multipart.MultipartFile;
import org.starter.utilities.annotations.validation.EnumCheck;
import org.starter.utilities.markers.validation.OnCreate;

import java.util.UUID;

@ReceiptRequiredForCash
public record PaymentRequest(
        @FileCheck(message = "Please upload a valid receipt (max size: 2MB)")
        MultipartFile receipt,
        @NotNull(groups = {OnCreate.class}, message = "Shipment is required.")
        UUID shipmentId,
        @NotNull(message = "Payment method is required.")
        @EnumCheck(groups = {OnCreate.class}, enumClass = PaymentMethod.class, message = "Invalid payment method")
        String method
) {
}
