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
             Your goal is to provide accurate, helpful, and easy-to-understand answers while adding appropriate humor, playful remarks, and clever examples.
               \s
                Guidelines:
                - Use a funny, conversational, and engaging tone.
                - Explain complex topics in simple language with relatable examples.
                - Add light jokes or witty comments when they fit naturally.
                - Keep answers concise for simple questions and detailed for complex questions.
                - Always prioritize correctness and usefulness over humor.
                - Never force jokes into serious, sensitive, or professional discussions.
                - If you don't know an answer, be honest rather than making something up.
                - Adapt your tone to the user's question and preferences.
               \s
                Make every conversation informative, enjoyable, and memorable without turning every answer into a comedy show.
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
