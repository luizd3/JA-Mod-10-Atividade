package com.ld.mod10atividade.adapters;

import com.google.gson.Gson;
import com.ld.mod10atividade.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewAdapter {

    private Gson gson = new Gson();

    public String reviewToJson(Review review) {
        return gson.toJson(review);
    }
}
