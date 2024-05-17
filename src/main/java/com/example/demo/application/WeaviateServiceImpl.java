package com.example.demo.application;

import com.example.demo.infrastructure.WeaviateInsertService;
import com.example.demo.domain.WeaviateService;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.WeaviateVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WeaviateServiceImpl implements WeaviateService {
    private List<Document> documents;
    private final VectorStore vectorStore;
    private final WeaviateInsertService weaviateInsertService;
    @Autowired
    public WeaviateServiceImpl(VectorStore vectorStore, WeaviateInsertService weaviateInsertService) {
        this.vectorStore = vectorStore;
        this.weaviateInsertService = weaviateInsertService;
        
    }
    @Bean
    public VectorStore vectorStore(EmbeddingClient embeddingClient) {
        WeaviateVectorStore.WeaviateVectorStoreConfig config = WeaviateVectorStore.WeaviateVectorStoreConfig.builder()
                .withScheme("http")
                .withHost("localhost:8080")
                .withConsistencyLevel(WeaviateVectorStore.WeaviateVectorStoreConfig.ConsistentLevel.ONE)
                .build();
        return new WeaviateVectorStore(config, embeddingClient);
    }

    @Override
    public List<Document> detectDuplicate(String userStory){
        List<Document> results = vectorStore.similaritySearch(
                SearchRequest
                        .query(userStory)
                        .withTopK(3));
        return results;
    }
    
    @Override
    public void insertData(List<Document> documents){
        try {
            vectorStore.add(documents);
            System.out.println("Data inserted successfully.");
        } catch (Exception e) {
            System.err.println("Error while inserting data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public Optional<Boolean> deleteData(List<String> uuidList){
        vectorStore.delete(uuidList);
        return vectorStore.delete(uuidList);
    }
    
    @Override
    public void updateData(String key, Document document){
        documents = weaviateInsertService.documents;
        for (Document value : documents) {
            if (key.equals(value.getMetadata().get("key"))) {
                String id = value.getId();
                deleteData(List.of(id));
                ArrayList<Document> documentList = new ArrayList<>();
                documentList.add(document);
                insertData(documentList);
                System.out.printf("Data with key '%s' found.%n", key);
            } 
        }
    }
}
