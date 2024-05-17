package com.example.demo.infrastructure;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ai.document.Document;
import kong.unirest.Unirest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class WeaviateInsertService {
    
    public List<Document> documents;
    public WeaviateInsertService(){
        documents = new ArrayList<>();
        catchURL();
    }
    
    public void catchURL() {
        Unirest.config().connectTimeout(10000);
        List<String> urls = new ArrayList<>(Arrays.asList(
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-1557",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-3381",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-2894",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-4425",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-4424",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-4028",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-4288",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-4360",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-4306",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-1314",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-4178",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-4289",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-4323",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-4296",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-4295",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-4294",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-4227",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-4097",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-4362",
                "https://axinic.central.inditex.grp/jira/rest/api/2/issue/ICPRSITDEP-4357"));
        
        for (String url : urls) { 
            fetchUserStory(url);
        }
    }
    
    public record JiraResponse(String key, JiraFields fields ){
        public record JiraFields(JiraProject project, String summary, JiraCreator creator, String created, String updated, String description){
            public record JiraProject(String key){}
            public record JiraCreator(String displayName){}
            
        }
    }

    public void fetchUserStory(String url) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            HttpResponse<String> response = Unirest.get(url)
                    .header("Authorization", "Basic c2hpd2Fuejpac3dzZzIwMDEwNzI3JA==")
                    .header("Cookie", "JSESSIONID=08D9646AA04078082009FA67DAC55430; atlassian.xsrf.token=ANOG-CKO0-QEKX-36F7_4da4c129634726853c8bcfda03ad203fd3bb9f15_lin")
                    .asString();

            if (response.getStatus() == 200) {
                String body = response.getBody();
                JiraResponse jiraResponse = objectMapper.readValue(body, JiraResponse.class);
                //System.out.println(jiraResponse);
                //Map<String, Object> metadata = objectMapper.convertValue(jiraResponse, new TypeReference<>() {
                //});
                String id = UUID.randomUUID().toString();
                Map<String, Object> metadata = Map.of(
                        "key", jiraResponse.key(),
                        "project", jiraResponse.fields().project(),
                        "summary", jiraResponse.fields().summary(),
                        "creator", jiraResponse.fields().creator(),
                        "created", jiraResponse.fields().created(),
                        "updated", jiraResponse.fields().updated()
                );
                System.out.println(id);
                Document document = new Document(id, jiraResponse.fields.description(), metadata);
                documents.add(document);
            } else {
                System.out.println("Error: " + response.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

