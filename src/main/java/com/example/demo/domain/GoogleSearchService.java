package com.example.demo.domain;

import java.io.IOException;
import java.util.List;

public interface GoogleSearchService {
    
    List<String> grabSearchPhrase(String aiResponse);
   
    List<String> search(String aiResponse) throws IOException;
}
