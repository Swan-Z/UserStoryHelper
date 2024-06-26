package com.example.demo.application;

import com.example.demo.domain.AskRequest;
import com.example.demo.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.ai.document.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class AiServiceImpl implements AiService {

    @Autowired
    private ChatClient chatClient;
    private final ModelApiService modelApiService;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public AiServiceImpl(ModelApiService modelApiService) {
        this.modelApiService = modelApiService;
    }
    
    
   /* @Bean
    public ChatClient chatClient() {
        OllamaApi ollamaApi =
                new OllamaApi("http://localhost:11435");
        OllamaChatClient chatClient = new OllamaChatClient(ollamaApi)
                .withModel(String.valueOf(MODEL)) // Set the model
                .withDefaultOptions(OllamaOptions.create()
                        .withModel(OllamaOptions.DEFAULT_MODEL)
                        .withTemperature(0.9f));
        return chatClient;
    }*/

    @Override
    public GPTResponse generateMessage(String promptMessage) {
        AskRequest askRequest = createAskRequestGPT(promptMessage);
        return modelApiService.getDataFromApi(askRequest);
    }
    @Override
    public GPTResponse generateUserStory(String userStory) {
        var prompt = String.format("""
                Como experto en la elaboración de historias de usuario, tu tarea es escribir
                historia de usuario y sus criterios de aceptación utilizando patrones en BDD (Behavior-Driven Development) 
                para proyectos de desarrollo de software.
                Te proporciono el siguiente contexto para la historia de usuario que necesito: %s.
                Basado en este contexto, redacta una historia de usuario con el formato "AS < type of user >, I WANT < some goal > TO < some reason >" 
                y desarrolla distintos tipos de criterios de aceptación siguiendo patrones de BDD.
               """, userStory);

        AskRequest askRequest = createAskRequestGPT(prompt);
        return modelApiService.getDataFromApi(askRequest);
    }
    @Override
    public GPTResponse generateUSWithSimilaritySearch(String resume, List<Document> userStorySimilar) {
        var prompt = String.format("""
                Como experto en la elaboración de historias de usuario, tu tarea es escribir una historia de usuario 
                y sus criterios de aceptación utilizando patrones en BDD (Behavior-Driven Development) para proyectos 
                de desarrollo de software.
                Te proporciono el siguiente contexto para la historia de usuario que necesito: %s y unas historias de usuario similares: %s
                A partir de este contexto, redacta una historia de usuario con el formato "AS < tipo de usuario >, QUIERO < algún objetivo > 
                PARA < alguna razón >" y desarrolla distintos tipos de criterios de aceptación siguiendo patrones de BDD.
               """, resume, userStorySimilar);

        AskRequest askRequest = createAskRequestGPT(prompt);
        return modelApiService.getDataFromApi(askRequest);
    }
    
    @Override
    public GPTResponse generateUSWithSimilarAndGoogleSearch(String resume, List<Document> userStorySimilar, String GoogleSearchResults) {
        var prompt = String.format("""       
                   Como experto en la elaboración de historias de usuario, tu tarea es escribir una historia de usuario
                   y sus criterios de aceptación utilizando patrones en BDD (Behavior-Driven Development) para proyectos de desarrollo de software.
                   Te proporciono el siguiente contexto para la historia de usuario que necesito: %s, unas historias de usuario similares: %s,
                   y información relevante de búsqueda en Google relacionado con esta historia de usuario: %s.               
                   Además, ajustar el formato de la historia de usuario generada como el siguiente ejemplo y asegúrate de incluir referencias a las fuentes de
                   información al final del resultado, incluyendo el título y la URL de cada fuente utilizada:             
                      *AS* a SITDEP user
                        
                        *I WANT* to be able to see the weight of the sales of my department (hierarchical level) that have promotion or not.
                        
                        *TO* to be able to analyze the sales volume of my department (or other hierarchical level) of promo and non-promo sales
                        
                        *ACCEPTANCE CRITERIA:* 
                        |Feature| |
                        
                        -
                        |Scenario|Show the volume represented by sales without promotion compared to total sales|
                        |Given|A user sitdep|
                        |When|navigates to the sales detail screen|
                        |Then|display the KPI of the weight of sales without promotion at a hierarchical level|
                        |And|shows the volume in euros represented by sales without promotion|
                        |And|shows the change in sales volume without promotion compared to A-1.|
                        
                        -
                        |Scenario|Show the volume represented by sales with promotions compared to total sales |
                        |Given|A user sitdep|
                        |When|navigates to the sales detail screen|
                        |Then|display the KPI of the weight of sales with promotion at a hierarchical level|
                        |And|shows the volume in euros represented by sales with promotion|
                        |And|shows the change in sales volume with promotion compared to A-1.|
               """, resume, userStorySimilar, GoogleSearchResults);

        System.out.println(prompt);
        System.out.println(prompt.length());
        AskRequest askRequest = createAskRequestGPT(prompt);
        return modelApiService.getDataFromApi(askRequest);
    }

    @Override
    public GPTResponse generateSearchPhrase(String userStory) {
        var prompt = String.format("""
             Crea tres frases de búsqueda en español basadas en esta historia de usuario %s
             para utilizarlas en Google en busca de información relevante y datos relacionales.
             Las respuestas deben entregarse en formato JSON siguiendo esta estructura:
             {"Frase 1":.., "Frase 2:"..., "Frase 3:"...}. Asegúrate de que las frases de
             búsqueda sean específicas y relevantes para obtener información que pueda mejorar
             la historia de usuario.
              """, userStory);

        AskRequest askRequest = createAskRequestGPT(prompt);
        GPTResponse res = modelApiService.getDataFromApi(askRequest);
        System.out.println(res);
        return res;
    }

    private AskRequest createAskRequestGPT(String query) {
        AskRequest askRequest = new AskRequest();
        askRequest.setQuery(query);
        askRequest.setHistory(new ArrayList<>());
        askRequest.setSystemPrompt("Eres un agente asistente de soporte de inteligencia artificial cuyo objetivo principal es ayudar a otro humano. Eres amigable, conciso y completo.");
        askRequest.setUserPrompt("Ignora el idioma original de la sección de resultados de búsqueda al proporcionar tu respuesta. La respuesta debe estar en el mismo idioma que este último mensaje del usuario");
        //askRequest.setModel("gpt-35-turbo-16k");
        askRequest.setModel("gpt-4-32k");
        askRequest.setTemperature(0.7f);
        return askRequest;
    }

    private AskRequest createAskRequestGemini(String query) {
        AskRequest askRequest = new AskRequest();
        askRequest.setQuery(query);
        askRequest.setHistory(new ArrayList<>());
        askRequest.setSystemPrompt("Eres un agente asistente de soporte de inteligencia artificial cuyo objetivo principal es ayudar a otro humano. Eres amigable, conciso y completo.");
        askRequest.setUserPrompt("Ignora el idioma original de la sección de resultados de búsqueda al proporcionar tu respuesta. La respuesta debe estar en el mismo idioma que este último mensaje del usuario");
        askRequest.setModel("gemini-pro");
        askRequest.setTemperature(0.7f);
        return askRequest;
    }
}

