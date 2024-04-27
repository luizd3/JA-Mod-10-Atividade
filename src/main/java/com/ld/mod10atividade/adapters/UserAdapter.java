package com.ld.mod10atividade.adapters;

import com.google.gson.Gson;
import com.ld.mod10atividade.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserAdapter {

    private Gson gson = new Gson();

    public String userToJson(User user) {
        return gson.toJson(user);
    }
}
