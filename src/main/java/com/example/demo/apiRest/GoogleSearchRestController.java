package com.example.demo.apiRest;

import com.example.demo.domain.GPTResponse;
import com.example.demo.domain.GoogleSearchService;
import com.example.demo.domain.AiService;
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
    
    private final AiService aiService;
    private final GoogleSearchService googleSearchService;
    @Autowired
    public GoogleSearchRestController(AiService aiService, GoogleSearchService googleSearchService) {
        this.aiService = aiService;
        this.googleSearchService = googleSearchService;
    }

    @GetMapping("api/v1/google/generateSearchResults")
    public ResponseEntity<String> generateSearchPhrase(@RequestParam String resume) throws IOException {
        final GPTResponse aiResponse = aiService.generateSearchPhrase(resume);
        final List<String> searchResultsResume = googleSearchService.search(aiResponse.getResponse());
        
        return ResponseEntity.status(HttpStatus.OK).body(searchResultsResume.toString());
    }

    
}
