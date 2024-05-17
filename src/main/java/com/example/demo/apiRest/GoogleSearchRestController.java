package com.example.demo.apiRest;

import com.example.demo.domain.GoogleSearchService;
import com.example.demo.domain.LlamaAiService;
import com.example.demo.domain.LlamaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class GoogleSearchRestController {
    
    private final LlamaAiService llamaAiService;
    private final GoogleSearchService googleSearchService;
    @Autowired
    public GoogleSearchRestController(LlamaAiService llamaAiService, GoogleSearchService googleSearchService) {
        this.llamaAiService = llamaAiService;
        this.googleSearchService = googleSearchService;
    }

    @GetMapping("api/v1/google/generateSearchResults")
    public ResponseEntity<List<String>> generateSearchPhrase(@RequestParam String resume) throws IOException {
        final LlamaResponse aiResponse = llamaAiService.generateSearchPhrase(resume);
        final List<String> searchResultsResume = googleSearchService.search(aiResponse.getMessage());
        
        return ResponseEntity.status(HttpStatus.OK).body(searchResultsResume);
    }

    
}
