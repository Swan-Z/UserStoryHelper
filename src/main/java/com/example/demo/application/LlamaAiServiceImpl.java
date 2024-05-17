package com.example.demo.application;

import com.example.demo.domain.GoogleSearchService;
import com.example.demo.domain.LlamaResponse;
import com.example.demo.domain.LlamaAiService;
import com.example.demo.domain.WeaviateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import org.springframework.ai.document.Document;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static javax.swing.text.html.parser.DTDConstants.MODEL;

@Service
@Slf4j
public class LlamaAiServiceImpl implements LlamaAiService {

    @Autowired
    private ChatClient chatClient;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    
    @Bean
    public ChatClient chatClient() {
        OllamaApi ollamaApi =
                new OllamaApi("http://localhost:11434");
        OllamaChatClient chatClient = new OllamaChatClient(ollamaApi)
                .withModel(String.valueOf(MODEL)) // Set the model
                .withDefaultOptions(OllamaOptions.create()
                        .withModel(OllamaOptions.DEFAULT_MODEL)
                        .withTemperature(0.9f));
        return chatClient;
    }

    @Override
    public LlamaResponse generateMessage(String promptMessage) {
        final String llamaMessage = chatClient.call(promptMessage);
        return new LlamaResponse().setMessage(llamaMessage);
    }
    @Override
    public LlamaResponse generateUserStoryWithSimilaritySearch(String resume, List<Document> userStorySimilar) {
        var prompt = """
                
                Como experto en la elaboración de historias de usuario, tu tarea es escribir una historia de usuario 
                y sus criterios de aceptación utilizando patrones en BDD (Behavior-Driven Development) para proyectos 
                de desarrollo de software.
                Te proporciono el siguiente contexto para la historia de usuario que necesito: %s y unas historias de usuario similares: %s
                A partir de este contexto, redacta una historia de usuario con el formato "AS < tipo de usuario >, QUIERO < algún objetivo > 
                PARA < alguna razón >" y desarrolla distintos tipos de criterios de aceptación siguiendo patrones de BDD.
               
               """;
        final String llamaMessage = chatClient.call(String.format(prompt, resume, userStorySimilar));
        return new LlamaResponse().setMessage(llamaMessage);
    }
    @Override
    public LlamaResponse generateUserStory(String resume) {
       /* var prompt = """
                             
                Como experto en la elaboración de historias de usuario, tu tarea es escribir 
                historias de usuario claras y sus criterios de aceptación utilizando patrones
                en BDD (Behavior-Driven Development) para proyectos de desarrollo de software. 
                Te proporciono el siguiente contexto para la historia de usuario que necesito:%s                
                Basado en este contexto, redacta una historia de usuario detallada y desarrolla 
                distintos tipos de criterios de aceptación siguiendo patrones de BDD. Dame una 
                respuesta con html format como esta plantilla:                    <p><b>Context:</b>&nbsp;<br>
                                                                 Currently we have in the application the possibility to visualize the sale and sales forecast by attribute, the idea of this user story is to be able to see the sale and sales forecast by family in the sale list.</p>
                                                                 
                                                                 <p>&nbsp;</p>
                                                                 
                                                                 <p><b>AS</b> a SITDEP user</p>
                                                                 
                                                                 <p><b>I WANT</b> to be able to filter the 3 main sales of my department&nbsp; by attributes and families.</p>
                                                                 
                                                                 <p><b>TO</b> be able to analyze at the user's desired level</p>
                                                                 
                                                                 <p><b>ACCEPTANCE CRITERIA:</b>&nbsp;</p>
                                                                 <div class="table-wrap">
                                                                 <table class="confluenceTable"><tbody>
                                                                 <tr>
                                                                 <td class="confluenceTd">Feature</td>
                                                                 <td class="confluenceTd">&nbsp;</td>
                                                                 </tr>
                                                                 </tbody></table>
                                                                 </div>
                                                                 
                                                                 
                                                                 <p>-</p>
                                                                 <div class="table-wrap">
                                                                 <table class="confluenceTable"><tbody>
                                                                 <tr>
                                                                 <td class="confluenceTd">Scenario</td>
                                                                 <td class="confluenceTd">Filter top 3 sales&nbsp; by family&nbsp;</td>
                                                                 </tr>
                                                                 <tr>
                                                                 <td class="confluenceTd">Given</td>
                                                                 <td class="confluenceTd">A SITDEP user</td>
                                                                 </tr>
                                                                 <tr>
                                                                 <td class="confluenceTd">When</td>
                                                                 <td class="confluenceTd">Select in the filter "filter by family" in the target screen</td>
                                                                 </tr>
                                                                 <tr>
                                                                 <td class="confluenceTd">Then</td>
                                                                 <td class="confluenceTd">Top 3 sales data by family shown&nbsp;</td>
                                                                 </tr>
                                                                 </tbody></table>
                                                                 </div>
                                                                 
                                                                 
                                                                 <p>-</p>
                                                                 <div class="table-wrap">
                                                                 <table class="confluenceTable"><tbody>
                                                                 <tr>
                                                                 <td class="confluenceTd">Scenario</td>
                                                                 <td class="confluenceTd">Filter top 3 forecast by family&nbsp;</td>
                                                                 </tr>
                                                                 <tr>
                                                                 <td class="confluenceTd">Given</td>
                                                                 <td class="confluenceTd">A SITDEP user</td>
                                                                 </tr>
                                                                 <tr>
                                                                 <td class="confluenceTd">When</td>
                                                                 <td class="confluenceTd">Select in the filter "filter by family" in the target screen</td>
                                                                 </tr>
                                                                 <tr>
                                                                 <td class="confluenceTd">Then</td>
                                                                 <td class="confluenceTd">Top 3 forecast data by family shown&nbsp;</td>
                                                                 </tr>
                                                                 </tbody></table>
                                                                 </div>
                                                                 
            
              """;  */
        var prompt = """
                Como experto en la elaboración de historias de usuario, tu tarea es escribir
                historia de usuario y sus criterios de aceptación utilizando patrones en BDD (Behavior-Driven Development) 
                para proyectos de desarrollo de software.
                Te proporciono el siguiente contexto para la historia de usuario que necesito: %s.
                Basado en este contexto, redacta una historia de usuario con el formato "AS < type of user >, I WANT < some goal > TO < some reason >" 
                y desarrolla distintos tipos de criterios de aceptación siguiendo patrones de BDD.
               
               """;
        final String llamaMessage = chatClient.call(String.format(prompt, resume));
        return new LlamaResponse().setMessage(llamaMessage);
    }
    @Override
    public LlamaResponse generateSearchPhrase(String userStory) {
        var prompt = """
              
                Crea tres frases de búsqueda en español basadas en esta historia de usuario %s
                para utilizarlas en Google en busca de información relevante y datos relacionales. 
                Las respuestas deben entregarse en formato JSON siguiendo esta estructura: 
                {"Frase 1":.., "Frase 2:"..., "Frase 3:"...}. Asegúrate de que las frases 
                de búsqueda sean específicas y relevantes para obtener información que pueda mejorar la 
                historia de usuario.                                             
              
              """;
        final String llamaMessage = chatClient.call(String.format(prompt, userStory));
        return new LlamaResponse().setMessage(llamaMessage);
    }
}

