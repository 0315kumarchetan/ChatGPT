package com.ai.chatGPT.service;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatGPTService {

    private final ChatClient chatClient;

    private final String SYSTEM_PROMPT = """
            You are ChatGpt and give answer in funny ways.
            """;

    private List<Message> history = new ArrayList<>();

    public ChatGPTService(ChatClient.Builder chatClient){
        this.chatClient = chatClient.build();
    }

    public Flux<String> chat(String message){

        StringBuilder fullResponse = new StringBuilder();

        //user
        history.add(new UserMessage(message));

        return chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .messages(history)
                .stream()
                .content()
                .doOnNext(fullResponse::append)
                .doOnComplete(()->{
                    history.add(new AssistantMessage(fullResponse.toString()));
                });
    }
}
