package com.example.demo.apiRest;

import com.example.demo.infrastructure.WeaviateInsertService;
import com.example.demo.domain.WeaviateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.document.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class WeaviateRestController {
        
        private final WeaviateService weaviateService;
        private final WeaviateInsertService weaviateInsertService;
    
        public WeaviateRestController(WeaviateService weaviateService, WeaviateInsertService weaviateInsertService) {
            this.weaviateService = weaviateService;
            this.weaviateInsertService = weaviateInsertService;
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
        
        @PostMapping("api/v1/vectorstore/insertData")
        public ResponseEntity<String> insertData() {
            List<Document> documents = weaviateInsertService.documents;
            weaviateService.insertData(documents);
            return ResponseEntity.ok("Data inserted successfully");
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
