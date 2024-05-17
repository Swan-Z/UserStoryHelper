package com.example.demo.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.domain.GoogleSearchService;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class GoogleSearchServiceImpl implements GoogleSearchService {
    public final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
    public final int MAX_RESULTS = 2;
    
    @Override
    public List<String> grabSearchPhrase(String aiResponse) {
        List<String> phrases = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(aiResponse);
        phrases.add(jsonObject.getString("Frase 1"));
        phrases.add(jsonObject.getString("Frase 2"));
        phrases.add(jsonObject.getString("Frase 3"));
        //System.out.println(phrases);
        return phrases;
    }
    @Override
    public List<String> search(String aiResponse) throws IOException {
        List<String> phrases = grabSearchPhrase(aiResponse);
        String resume = null;
        List<String> searchResults = new ArrayList<>();
        Elements results = null;
        for (String phrase : phrases) {
            String searchURL = GOOGLE_SEARCH_URL + "?q=" + phrase + "&num=" + MAX_RESULTS;
            Document doc = bypassCookiePage(searchURL);
            //System.out.println(doc); // Print the search results page
            results = doc.select("div.g a"); // General selector for links within search results
            if (results.isEmpty()) {
                results = doc.select("a[href]:has(h3)"); // Links containing an h3 element
            }
            if (results.isEmpty()) {
                results = doc.select("div.yuRUbf a"); // Links within divs with a specific class
            }
            for(Element result : results) {
                String linkHref = result.attr("href");
                String linkText = result.select("h3").text();
                resume = "Text::" + linkText + ", URL::" + linkHref.substring(7, linkHref.indexOf("&"));
                System.out.println(resume);
                searchResults.add(resume);
            }
        }
        return searchResults;
    }

    private Document bypassCookiePage(String searchURL) throws IOException {
        Connection connection = Jsoup.connect(searchURL).userAgent("Mozilla/5.0");
        Document doc = connection.get();
        
        // Find and submit the consent form
        Element consentForm = doc.selectFirst("form[action*=consent]");
        if (consentForm != null) {
            // Manually extract form data
            Map<String, String> formData = new HashMap<>();
            for (Element input : consentForm.select("input[name]")) {
                formData.put(input.attr("name"), input.attr("value"));
            }
            // Submit the form
            Connection.Response response = Jsoup.connect(consentForm.absUrl("action"))
                    .data(formData)
                    .method(Connection.Method.POST)
                    .userAgent("Mozilla/5.0")
                    .execute();
            doc = response.parse();
        }
        return doc;
    }
}