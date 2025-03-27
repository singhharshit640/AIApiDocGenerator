package com.example.apidocgenerator.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.apidocgenerator.service.ApiDocumentationService;

import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "AI-Powered API Documentation", description = "Generates API documentation using AI")
@RequestMapping("/api")
public class ApiDocumentationController {

    private final ApiDocumentationService apiDocumentationService;

    public ApiDocumentationController(ApiDocumentationService apiDocumentationService) {
        this.apiDocumentationService = apiDocumentationService;
    }

    @GetMapping("/generate-docs")
    @Operation(summary = "Generate API documentation", description = "Uses AI to analyze API endpoints and generate documentation")
    public String generateDocumentation(@RequestParam String apiEndpoint) {
        return apiDocumentationService.generateDocumentation(ApiDocumentationController.class, apiEndpoint,"calculateAddSubMulDiv");
    }

    @GetMapping("/calculate")
    public ResponseEntity<Map<String, Double>> calculateAddSubMulDiv(
            @RequestParam double num1,
            @RequestParam double num2) {

        Map<String, Double> result = new HashMap<>();
        result.put("addition", num1 + num2);
        result.put("subtraction", num1 - num2);
        result.put("multiplication", num1 * num2);
        result.put("division", num2 != 0 ? num1 / num2 : null);

        return ResponseEntity.ok(result);
    }

}