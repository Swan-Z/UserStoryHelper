package com.example.demo.apiRest;

import com.example.demo.domain.*;
import com.example.demo.infrastructure.WeaviateInsertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class USEnrichRestController {
    @Autowired
    private WeaviateService weaviateService;
    @Autowired
    private WeaviateInsertService weaviateInsertService;
    @Autowired
    private LlamaAiService llamaAiService;
    @Autowired
    private UserStoryEnrichService userStoryEnrichService;

    @GetMapping("api/v1/USHelper/detectDuplications")
    public ResponseEntity<?> detectDuplications(@RequestParam String userStory) {
        try {
            log.info("Detecting duplications for user story: " + userStory);
            List<Document> weaviateResponse = weaviateService.detectDuplicate(userStory);
            List<String> metadata = new ArrayList<>();
            for (Document document : weaviateResponse) {
                metadata.add(document.getMetadata().toString());
            }
            return ResponseEntity.ok(metadata);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while detecting duplications.");
        }
    }

    @GetMapping("api/v1/USHelper/generate")
    public ResponseEntity<?> enrichUserStory(@RequestParam String userStory) {
        try {
            final String aiResponse = userStoryEnrichService.combineResults(userStory);
            return ResponseEntity.ok(aiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while generating message.");
        }
    }

    @GetMapping("api/v1/USHelper/generateUS")
    public ResponseEntity<?> generateUserStory(@RequestParam String userStory) {
        try {
            log.info("Generating for user story: " + userStory);
            final LlamaResponse aiResponse = llamaAiService.generateUserStory(userStory);
            return ResponseEntity.ok(aiResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while generating message.");
        }
    }

}
