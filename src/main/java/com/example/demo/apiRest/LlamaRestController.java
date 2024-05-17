package com.example.demo.apiRest;
import com.example.demo.domain.LlamaResponse;
import com.example.demo.domain.GoogleSearchService;
import com.example.demo.domain.LlamaAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LlamaRestController {

    private final LlamaAiService llamaAiService;
    
    private final GoogleSearchService googleSearchService;

    @Autowired
    public LlamaRestController(LlamaAiService llamaAiService, GoogleSearchService googleSearchService) {
        this.llamaAiService = llamaAiService;
        this.googleSearchService = googleSearchService;
    }

   @GetMapping("api/v1/ai/generateSearchPhrase")
    public ResponseEntity<String> generate(@RequestParam String resume) {
        final LlamaResponse aiResponse = llamaAiService.generateSearchPhrase(resume);
        return ResponseEntity.status(HttpStatus.OK).body(aiResponse.getMessage());
    }
    @GetMapping("api/v1/ai/generate/userStory")
    public ResponseEntity<LlamaResponse> generateUserStory(@RequestParam String resume) {
        final LlamaResponse aiResponse = llamaAiService.generateUserStory(resume);
        return ResponseEntity.status(HttpStatus.OK).body(aiResponse);
    }
}
