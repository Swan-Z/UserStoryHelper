package com.example.demo.apiRest;

import com.example.demo.infrastructure.JiraApiClient;
import com.example.demo.infrastructure.WeaviateInsertService;
import com.example.demo.domain.WeaviateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.document.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class WeaviateRestController {
    private final WeaviateService weaviateService;
    private final WeaviateInsertService weaviateInsertService;

    private final JiraApiClient jiraApiClient;
    private ExecutorService nonBlockingService = Executors
            .newCachedThreadPool();

    public WeaviateRestController(WeaviateService weaviateService, WeaviateInsertService weaviateInsertService, JiraApiClient jiraApiClient) {
        this.weaviateService = weaviateService;
        this.weaviateInsertService = weaviateInsertService;
        this.jiraApiClient = jiraApiClient;
    }
    @PostMapping("api/v1/vectorstore/initializeData")
    public ResponseEntity<SseEmitter> initializeData() throws IOException, InterruptedException {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        nonBlockingService.execute(() -> {
            try {
                List<Document> documents = jiraApiClient.fetchData();
                for (Document document : documents) {
                    String msg = weaviateService.insertData(document);
                    emitter.send(msg);
                }
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }
    /*  @PostMapping("api/v1/vectorstore/initializeData")
      public ResponseEntity<String> initializeData() throws IOException, InterruptedException {
          List<Document> documents = weaviateInsertService.catchURL();
          String msg = weaviateService.insertData(documents);
          return new ResponseEntity<>(msg, HttpStatus.OK);
      }*/
    @PostMapping("api/v1/vectorstore/insertData")
    public ResponseEntity<SseEmitter> insertData(@RequestParam Document document) throws IOException, InterruptedException {
        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L);
        nonBlockingService.execute(() -> {
            try {

                String msg = weaviateService.insertData(document);
                emitter.send(msg);

            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }

    @GetMapping("api/v1/vectorstore/detectDuplicate")
    public ResponseEntity<String> detectDuplicate(@RequestParam String userStory) { // return the whole document
        try {
            List<Document> weaviateResponse = weaviateService.detectDuplicate(userStory);
            return ResponseEntity.ok(weaviateResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while detecting duplications.");
        }
    }
    @DeleteMapping("api/v1/vectorstore/deleteData")
    public ResponseEntity<String> deleteData(@RequestParam(value = "ids") List<String> uuidList) {
        weaviateService.deleteData(uuidList);
        //TODO: return proper response when the id is not found
        return ResponseEntity.ok("Data deleted successfully");
    }

    @PostMapping("api/v1/vectorstore/updateData")
    public ResponseEntity<String> updateData(@RequestParam String key, @RequestBody Map<String, Object> body) {
        System.out.println("key: " + key);
        ObjectMapper objectMapper = new ObjectMapper();
        Document document = objectMapper.convertValue(body, Document.class);
        weaviateService.updateData(key, document);
        return ResponseEntity.ok("Data updated successfully");
    }
}
        

