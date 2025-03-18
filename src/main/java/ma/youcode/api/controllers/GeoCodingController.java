package ma.youcode.api.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import ma.youcode.api.payloads.responses.LocationResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.starter.utilities.dtos.SimpleSuccessDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.starter.utilities.response.Response.simpleSuccess;


@RestController
@RequestMapping("/api/v1/geocoding")
@RequiredArgsConstructor
public class GeoCodingController {

    private static final Logger log = LogManager.getLogger(GeoCodingController.class);
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<SimpleSuccessDTO> getGeocodingAddress(
            @RequestParam String query,
            @RequestParam(defaultValue = "3") int limit,
            @RequestParam(defaultValue = "en") String lang) {

        String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
        final String SEARCH_URL = UriComponentsBuilder.fromHttpUrl(NOMINATIM_URL)
                .queryParam("q", query)
                .queryParam("format", "json")
                .queryParam("accept-language", lang)
                .queryParam("limit", limit)
                .queryParam("countrycodes", "ma")
                .queryParam("viewbox", "-13.5,27.5,-1.0,36.0")
                .queryParam("bounded", "1")
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Alcorti/1.0 (alcorti@gmail.com)");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(SEARCH_URL, HttpMethod.GET, entity, String.class);
        List<LocationResponse> locations = parseLocationResponse(response.getBody());

        return simpleSuccess(HttpStatus.OK.value(), "Retrieve successfully.", locations);
    }


    private List<LocationResponse> parseLocationResponse(String jsonResponse) {
        List<LocationResponse> locations = new ArrayList<>();

        try {
            List<Map<String, Object>> results = objectMapper.readValue(
                    jsonResponse,
                    new TypeReference<>() {
                    }
            );

            for (Map<String, Object> result : results) {
                double lat = Double.parseDouble(result.get("lat").toString());
                double lon = Double.parseDouble(result.get("lon").toString());
                String name = result.get("display_name").toString();

                locations.add(new LocationResponse(lat, lon, name));
            }
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON response: {}", e.getMessage());
        }

        return locations;
    }
}
