package com.example.demo.infrastructure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class JiraApiClient {
    public List<Document> fetchData() throws IOException, InterruptedException {
        HttpResponse<String> response = sendHttpRequest();
        List<Document> documents = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            if (response.statusCode() == 200) {
                String body = response.body();
                JiraApiClient.JiraResponse jiraResponse = objectMapper.readValue(body, JiraApiClient.JiraResponse.class);
                Integer numTotal = jiraResponse.total();
                for (int i = 0; i < numTotal; i++){
                    String description = jiraResponse.issues().get(i).fields().description();
                    if(description == null){
                        continue;
                    }
                    String id = jiraResponse.issues().get(i).id();
                    String uuidFromString = UUID.nameUUIDFromBytes(id.getBytes(StandardCharsets.UTF_8)).toString();
                    Map<String, Object> metadata = Map.of(
                            "key", jiraResponse.issues().get(i).key(),
                            "project", jiraResponse.issues().get(i).fields().project().key(),
                            "summary", jiraResponse.issues().get(i).fields().summary(),
                            "creator", jiraResponse.issues().get(i).fields().creator().displayName(),
                            "created", jiraResponse.issues().get(i).fields().created(),
                            "updated", jiraResponse.issues().get(i).fields().updated()
                    );
                    System.out.println(uuidFromString);
                    Document document = new Document(description, metadata);
                    documents.add(document);
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("jira_response.json"))) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < documents.size(); i++) {
                        Document document = documents.get(i);
                        builder.append(objectMapper.writeValueAsString(document));
                        if (i < documents.size() - 1) {
                            builder.append(",");
                        }
                        builder.append(System.lineSeparator());
                    }
                    writer.write(builder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Failed to fetch data: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return documents;
    }
    public HttpResponse<String> sendHttpRequest() throws IOException, InterruptedException {
        String username = "shiwanz@inditex.com";
        String apiToken = "ODU4Mjg5NjU3OTY2Ov1UYmTyGJLZpl8q5ykmS/qp6p8z";
        String url = "https://axinic.central.inditex.grp/jira/rest/api/2/search" +
                "?jql=project=ICPRSITDEP%20AND%20issuetype%20=%20Historia" +
                "&startAt=0&fields=key,description,summary,creator,project,updated,created&maxResults=1000";

        String auth = username + ":" + apiToken;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String authHeaderValue = "Basic " + encodedAuth;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", authHeaderValue)
                .header("Cookie", "JSESSIONID=1C53EFDD72C65ED30FB1F9111F75928C; atlassian.xsrf.token=ANOG-CKO0-QEKX-36F7_ca9075628dd899b166de4b9c0758a129c3c42995_lin; _clck=8x5v6f%7C2%7Cfjp%7C0%7C1517; _ga_Q54YPFF83C=GS1.2.1711540370.1.1.1711540486.54.0.0; _ga_MD5P4S1X89=GS1.1.1714637380.1.0.1714637380.0.0.0; has_used_traditional_login=\"true\"; _gid=GA1.2.1529967687.1716885153; LtpaToken2=W3m65TS2wYL5wV5yunKbvs7LPqgREUFCJy0AdbCt0BpGTRl58GR4mGuVj/euYCPzCgW5m8cB4EaIjOFryElwZpvH1CDh/eg1+Xo4opk4pxonKApST9PL9QCY8f+Q/K5w2Hyi3ultZsaXm68+OYEV2LK0yObTBIlto7xOy7mVSXe8GuXM+g0vVqRSwWP9GUZ2lbmkPuurERWCBiCVL+QnlQbh+kDKAy6p8U72b4BMRFGwRHEG9Fff51Ub49Gk7BhVV1unIEZvdKVFjT6o6+NFK7NrqzC7PMiM9Hf86jHvmyE1+ZzHB/Cs7QW//ffTXPOhFj8gJOVJEfbBBpQsFkLBdU4hBuQmwhy0Y2oKdE6kJnjT6Ek3ssVJ8qEWuAKVmnQlsUEEFeRdQWA7dr5EhKvkY94IInsmO2l/6X1udLSCcVIag1T3Yc7vsWeFtIhuW3K3UZldvZgzUPh2nwJyAEKZQto0OtPxVEoDGnCDHumn7kPj0I9RMpmE5rQszDzzrGthY5RHDtrczzMilY7MI/3TUJk4ydQUweId8/vaYMexlMNW3zAKAYA6IfEtzHKBlS6+dMiWEaBSidGfrkeMTQao0t/jpEPkV+Lfi9YjXStlh6u6VdrPEvAEQv1hZfOnySj6Ld3dqM3pV4AJ0ovMGSaPeXyFSodHbljg7UnOyAzSjD+BElsP+aELDas5OUABf1zdTjak7/th7cNmGS59gU28jbpdnFcBa7XTTTeB4ZKjEfc=; _ga_C30CVF0FDV=GS1.1.1717082590.57.1.1717082725.0.0.0; _ga=GA1.1.556562481.1708940429")
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
    public record JiraResponse(Integer total, List<JiraIssues> issues){
        public record JiraIssues(String id, String key, JiraFields fields){
            public record JiraFields( JiraProject project, JiraCreator creator, String created, String updated, String summary, String description){
                public record JiraProject(String key){}
                public record JiraCreator(String displayName){}
            }
        }
    }
}
