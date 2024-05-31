package com.example.demo.infrastructure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
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

    public List<Document> catchURL() {
        Unirest.config().connectTimeout(10000);
        List<Document> documents = new ArrayList<>();
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
            documents.add(fetchUserStory(url));
        }
        return documents;
    }
    public Document fetchUserStory(String url) {
        Document document = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
            HttpResponse<String> response = Unirest.get(url)
                    .header("Authorization", "Basic c2hpd2Fuejpac3dzZzIwMDEwNzI3JA==")
                    .header("Cookie", "JSESSIONID=44160E45C5C56B2406E349DACAA9F8B0; atlassian.xsrf.token=ANOG-CKO0-QEKX-36F7_5e300bf04b84b857f794df4955a69b28f6de578d_lin; refresh_token=J3v6Q6tdmpTel3TzoulUaB3D6Kc; _clck=8x5v6f%7C2%7Cfjp%7C0%7C1517; _ga_Q54YPFF83C=GS1.2.1711540370.1.1.1711540486.54.0.0; _ga_MD5P4S1X89=GS1.1.1714637380.1.0.1714637380.0.0.0; has_used_traditional_login=\"true\"; _gid=GA1.2.1529967687.1716885153; _ga=GA1.1.556562481.1708940429; LtpaToken2=zlZ1EayzUpntzwZ1tZhz4OrLSdrXQg3GlcSkXozvg71vQ4VgXWCByhLW0mI1uH/7pQdJLWREcPtR6n2g5pS7YHczAX4OCJpd4DFS376KHAEJYfmslMj97nz8N8q9mwIV+l8Ye3MT7zjjEVNmr2nql30CSeg4hOMZ/NDH8Z30uRGWo56Ro8fBaRe8lEi0ISbSvYmWOIowSgg8ATD1xaFzSk4hINpDmXrvXc2peIWiwdCyb8uyh1xWPgD8JeB9wsY6i8f+EK+j9+6+J5e90qx3MkIJwHjj8E4a3Eh88VNBUtpY9+VD2kCj03u1pDfQdp/zQUQE9Lu/66RpBmStf0CCF9b+a1caIhqD1NDa/NCg6LOb0LoW/fBURMJoieyLBTuiP3g5tS578YvQbLHd7Otzv/60FeIZD0FH65k31ZTVp9e4xCnjexUlRAkExTG28O3vQyJLW79HFp5i0sbvqjhg3RvR4MujPBQ6RM2CyBv9OTlvyvGZQPx0k9W6lpnrQpuDLuwz45LCAVNy9mtSMg9DkO/TPSV7uCLTDs5jct7RqLzSOnIylSfNKzAla7U9GqEEIiBZaZjctaHu4tiG0RGR0yVBv4wk86aqMMgv77+B8JR3wpgPgjPHVVnPt7AL9nd2Ed5GwVkITFeaDqr//1KKP8zyZ/a2EDmWH01Bfxddgs8mRX0Q6tO9PEJfR4sJ5k/a0rC86Ex6zVLkkOrEBSJJ9paXMHx3e808/E5ikUDbxr4=; _ga_C30CVF0FDV=GS1.1.1717054639.55.1.1717055332.0.0.0")
                    .asString();

            if (response.getStatus() == 200) {
                String body = response.getBody();
                JiraResponse jiraResponse = objectMapper.readValue(body, JiraResponse.class);
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
                document = new Document(id, jiraResponse.fields.description(), metadata);
            } else {
                System.out.println("Error: " + response.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return document;
    }
    public record JiraResponse(String key, JiraFields fields ){
        public record JiraFields(JiraProject project, String summary, JiraCreator creator, String created, String updated, String description){
            public record JiraProject(String key){}
            public record JiraCreator(String displayName){}

        }
    }


}

