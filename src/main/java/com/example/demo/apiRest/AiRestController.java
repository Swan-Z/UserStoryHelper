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


@RestController
public class AiRestController {

    private final AiService aiService;
    
    private final GoogleSearchService googleSearchService;

    @Autowired
    public AiRestController(AiService aiService, GoogleSearchService googleSearchService) {
        this.aiService = aiService;
        this.googleSearchService = googleSearchService;
    }

   @GetMapping("api/v1/ai/generateSearchPhrase")
    public ResponseEntity<String> generate(@RequestParam String resume) {
        final GPTResponse aiResponse = aiService.generateSearchPhrase(resume);
        return ResponseEntity.status(HttpStatus.OK).body(aiResponse.getResponse());
    }
    @GetMapping("api/v1/ai/generate/userStory")
    public ResponseEntity<GPTResponse> generateUserStory(@RequestParam String resume) {
        final GPTResponse aiResponse = aiService.generateUserStory(resume);
        return ResponseEntity.status(HttpStatus.OK).body(aiResponse);
    }
}
