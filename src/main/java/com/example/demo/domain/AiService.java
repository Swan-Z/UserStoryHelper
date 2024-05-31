package com.example.demo.domain;

import org.springframework.ai.document.Document;

import java.util.List;

public interface AiService {
  GPTResponse generateMessage(String prompt);
  GPTResponse generateUserStory(String resume);
  GPTResponse generateSearchPhrase(String resume);
  GPTResponse generateUSWithSimilaritySearch(String resume, List<Document> userStorySimilar);
  GPTResponse generateUSWithSimilarAndGoogleSearch(String resume, List<Document> userStorySimilar, String GoogleSearchResults);
}
