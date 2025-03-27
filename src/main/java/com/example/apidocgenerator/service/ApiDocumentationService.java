package com.example.apidocgenerator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class ApiDocumentationService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final ApiMetadataExtractorService apiMetadataExtractorService;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public ApiDocumentationService(ApiMetadataExtractorService apiMetadataExtractorService) {
        this.apiMetadataExtractorService = apiMetadataExtractorService;
    }

    public String generateDocumentation(Class<?> controllerClassName, String apiEndpoint, String methodName) {
        Map<String, Object> apiMetaData = apiMetadataExtractorService.extractMetadata(controllerClassName, methodName);

        // Formatting metadata as JSON string
        String metadataJson = apiMetaData.toString().replace("=", ":");

        String requestBody = """
        {
          "model": "gpt-4o-mini",
          "messages": [
            {"role": "system", "content": "You are an expert API documentation generator."},
            {"role": "user", "content": "Generate API documentation for endpoint: %s with the following metadata: %s"}
          ],
          "max_tokens": 200
        }
        """.formatted(apiEndpoint, metadataJson);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(OPENAI_URL, HttpMethod.POST, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            return "Error generating API documentation: " + e.getMessage();
        }
    }
}