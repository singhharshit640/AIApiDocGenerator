package com.example.apidocgenerator.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApiMetadataExtractorService {

    public Map<String, Object> extractMetadata(Class<?> controllerClass, String methodName) {
        Map<String, Object> metadata = new HashMap<>();
        try {
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    metadata.put("methodName", method.getName());
                    metadata.put("returnType", method.getReturnType().getSimpleName());

                    // Extract HTTP method
                    if (method.isAnnotationPresent(GetMapping.class)) {
                        metadata.put("httpMethod", "GET");
                    } else if (method.isAnnotationPresent(PostMapping.class)) {
                        metadata.put("httpMethod", "POST");
                    } else if (method.isAnnotationPresent(PutMapping.class)) {
                        metadata.put("httpMethod", "PUT");
                    } else if (method.isAnnotationPresent(DeleteMapping.class)) {
                        metadata.put("httpMethod", "DELETE");
                    }

                    // Extract parameters
                    Map<String, String> parameters = new HashMap<>();
                    for (Parameter parameter : method.getParameters()) {
                        String paramName = parameter.getName(); // Can be null if not compiled with `-parameters`
                        if (paramName == null || paramName.startsWith("arg")) {
                            paramName = "param" + parameters.size(); // Assign a default name
                        }
                        parameters.put(paramName, parameter.getType().getSimpleName());
                    }
                    metadata.put("parameters", parameters);
                    break;
                }
            }
        } catch (Exception e) {
            metadata.put("error", "No metadata found for method: " + methodName);
        }
        return metadata;
    }
}