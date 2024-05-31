package com.example.demo.domain;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class AskRequest {
    private String query;
    private List<Map<String, String>> history;
    private String systemPrompt;
    private String userPrompt;
    private String model;
    private float temperature;
}