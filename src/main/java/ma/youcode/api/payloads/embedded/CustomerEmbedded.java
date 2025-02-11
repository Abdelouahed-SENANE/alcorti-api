package ma.youcode.api.payloads.embedded;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CustomerEmbedded(
        UUID id,
        String cin,
        String firstName,
        String lastName,
        String email,
        String photoURL
) {
}
