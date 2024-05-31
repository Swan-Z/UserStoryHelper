package com.example.demo.application;

import com.example.demo.domain.ModelApiService;
import com.example.demo.domain.AskRequest;
import com.example.demo.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@Slf4j
public class UserStoryEnrichServiceImpl implements UserStoryEnrichService {
    private  final ModelApiService modelApiService;
    //private final ChatClient chatClient;
    private final AiService aiService;
    private final GoogleSearchService googleSearchService;
    private final WeaviateService weaviateService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    public UserStoryEnrichServiceImpl(AiService aiService, GoogleSearchService googleSearchService, WeaviateService weaviateService, ModelApiService modelApiService) {
        this.aiService = aiService;
        this.googleSearchService = googleSearchService;
        this.weaviateService = weaviateService;
        this.modelApiService = modelApiService;
       // this.chatClient = chatClient;
    }

    @Override
    public String combineResults(String resume) throws InterruptedException, ExecutionException {
        //similarity search
        Future<List<Document>>  weaviateResponse = this.executorService.submit(() -> {
            return weaviateService.detectDuplicate(resume);
        });
        //search in google
        Future<String> searchResultsFuture = this.executorService.submit(() -> {
            log.info("Searching in Google for resume: {}", resume);
            GPTResponse searchPhraseResponse = aiService.generateSearchPhrase(resume);
            List<String> searchResultsResume = googleSearchService.search(searchPhraseResponse.getResponse());
            log.info("Final Search results: {}", searchResultsResume);
            return String.join(", ", searchResultsResume);
        });
        //generate user story
        GPTResponse aiResponse = aiService.generateUSWithSimilarAndGoogleSearch(resume, weaviateResponse.get(), searchResultsFuture.get());
        String userStoryGenerated = aiResponse.getResponse();
        log.info("Final User story generated: {}", aiResponse.getResponse());
        return userStoryGenerated;
    }

    private AskRequest createAskRequest(String query) {
        AskRequest askRequest = new AskRequest();
        askRequest.setQuery(query);
        askRequest.setHistory(new ArrayList<>());
        askRequest.setSystemPrompt("Eres un agente asistente de soporte de inteligencia artificial cuyo objetivo principal es ayudar a otro humano. Eres amigable, conciso y completo.");
        askRequest.setUserPrompt("Ignora el idioma original de la sección de resultados de búsqueda al proporcionar tu respuesta. La respuesta debe estar en el mismo idioma que este último mensaje del usuario");
        askRequest.setModel("gpt-4-32k");
        askRequest.setTemperature(0.7f);
        return askRequest;
    }
}
