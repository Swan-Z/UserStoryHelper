package com.example.demo.domain;

import java.util.concurrent.ExecutionException;

public interface UserStoryEnrichService {
    String combineResults(String resume) throws InterruptedException, ExecutionException;
}
