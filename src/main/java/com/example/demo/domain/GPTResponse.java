package com.example.demo.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GPTResponse {

    private String response;
}
