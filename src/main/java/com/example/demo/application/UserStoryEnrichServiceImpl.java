package com.example.demo.application;

import com.example.demo.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Slf4j
public class UserStoryEnrichServiceImpl implements UserStoryEnrichService {
    private final ChatClient chatClient;
    private final LlamaAiService llamaAiService;
    private final GoogleSearchService googleSearchService;
    private final WeaviateService weaviateService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    public UserStoryEnrichServiceImpl(LlamaAiService llamaAiService, GoogleSearchService googleSearchService, WeaviateService weaviateService, ChatClient chatClient) {
        this.llamaAiService = llamaAiService;
        this.googleSearchService = googleSearchService;
        this.weaviateService = weaviateService;
        this.chatClient = chatClient;
    }

    @Override
    public String combineResults(String resume) throws InterruptedException, ExecutionException {
        List<Document> weaviateResponse = weaviateService.detectDuplicate(resume);
           
        Future<String> userStoryGenerated = this.executorService.submit(() -> {
            log.info("Generating user story for resume: {}", resume);
           // LlamaResponse llamaResponse = llamaAiService.generateUserStory(resume);
            LlamaResponse llamaResponse = llamaAiService.generateUserStoryWithSimilaritySearch(resume, weaviateResponse);
            log.info("Final User story generated: {}", llamaResponse.getMessage());
            return llamaResponse.getMessage();
        });
        Future<String> searchResultsFuture = this.executorService.submit(() -> {
            log.info("Searching in Google for resume: {}", resume);
            LlamaResponse searchPhraseResponse = llamaAiService.generateSearchPhrase(resume);
            List<String> searchResultsResume = googleSearchService.search(searchPhraseResponse.getMessage());
            log.info("Final Search results: {}", searchResultsResume);
            return String.join(", ", searchResultsResume);
        });
        var prompt = """    
                
                Mostrar el resultado de la historia de usuario generada %s y superponer los resultados obtenidos               
                de la búsqueda en Google %s. Por favor, asegúrate de presentar una visualización clara y legible 
                que combine la historia de usuario generada con los resultados de la búsqueda en Google de forma coherente y comprensible.                                                                                                            
              
              """;
        final String llamaMessage = chatClient.call(String.format(prompt, userStoryGenerated.get(), searchResultsFuture.get()));
        return llamaMessage;
    }
}
