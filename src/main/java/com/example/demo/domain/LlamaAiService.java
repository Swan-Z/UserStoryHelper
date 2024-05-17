package com.example.demo.domain;

import org.springframework.ai.document.Document;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface LlamaAiService {

  LlamaResponse generateMessage(String prompt);
  LlamaResponse generateUserStory(String resume);
  LlamaResponse generateSearchPhrase(String resume);
  LlamaResponse generateUserStoryWithSimilaritySearch(String resume, List<Document> userStorySimilar);
}
