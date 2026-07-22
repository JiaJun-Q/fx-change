package com.fx.api.feed;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrchestratorClient {

    private final RestTemplate http;
    private final String orchestratorBaseUrl;

    public OrchestratorClient(RestTemplate http,
                              @Value("${fx.orchestrator.url}") String orchestratorBaseUrl) {
        this.http = http;
        this.orchestratorBaseUrl = trimTrailingSlash(orchestratorBaseUrl);
    }

    public void sendAck(long batchId, String status) {
        http.postForEntity(orchestratorBaseUrl + "/api/feed/ack", new AckMessage(batchId, status), Void.class);
    }

    private String trimTrailingSlash(String url) {
        if (url == null || url.isBlank()) {
            return "http://localhost:8081";
        }
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }
}
