package com.finalproject.safepickup.Service;


import tools.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class CongestionService {

    private final RestClient restClient;

    @Value("${HERE_API_KEY}")
    private String apiKey;


    @Value("${here.api.flow-url}")
    private String flowUrl;

    public CongestionService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public JsonNode getTrafficFlow(double lat, double lng, int radius) {

        String circleParam = String.format("circle:%s,%s;r=%d", lat, lng, radius);

        log.info("Calling HERE Traffic API — circle: {}", circleParam);

        JsonNode response = restClient.get()
                .uri(flowUrl, uriBuilder -> uriBuilder
                        .queryParam("in", circleParam)
                        .queryParam("locationReferencing", "olr")
                        .queryParam("apiKey", apiKey)
                        .build()
                )
                .retrieve()
                .body(JsonNode.class);

        log.info("HERE Traffic API response received successfully");

        return response;
    }
}
