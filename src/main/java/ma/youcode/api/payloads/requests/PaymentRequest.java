package ma.youcode.api.payloads.requests;

import jakarta.validation.constraints.NotNull;
import ma.youcode.api.annotations.validation.FileCheck;
//import ma.youcode.api.annotations.validation.ReceiptRequiredForCash;
import ma.youcode.api.enums.PaymentMethod;
import org.springframework.web.multipart.MultipartFile;
import org.starter.utilities.annotations.validation.EnumCheck;
import org.starter.utilities.markers.validation.OnCreate;

import java.util.UUID;

//@ReceiptRequiredForCash
public record   PaymentRequest(
        @NotNull(message = "Shipment id is required.")
        UUID shipmentId
) {
}
