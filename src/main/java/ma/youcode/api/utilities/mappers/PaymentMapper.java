package ma.youcode.api.utilities.mappers;

import ma.youcode.api.models.Payment;
import ma.youcode.api.payloads.requests.PaymentRequest;
import ma.youcode.api.payloads.responses.PaymentResponse;
import org.mapstruct.Mapper;
import org.starter.utilities.mappers.GenericMapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper extends GenericMapper<Payment , PaymentResponse , PaymentRequest> {
}
