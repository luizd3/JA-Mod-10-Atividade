package com.ld.mod10atividade.jms;

import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

@Service
public class JmsErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable throwable) {
        System.out.println("Error processing message " + throwable.getMessage());
    }
}
