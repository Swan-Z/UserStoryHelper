package com.example.demo.domain;

import org.springframework.ai.document.Document;

import java.util.List;
import java.util.Optional;

public interface WeaviateService {

    List<Document> detectDuplicate(String userStory);

    String insertData(Document document);

    String insertData(List<Document> documents);

    Optional<Boolean> deleteData(List<String> keyList);

    void updateData(String key, Document document);
}
