package com.ai.chatGPT.controller;

import com.ai.chatGPT.service.ChatGPTService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    public ChatGPTController(ChatGPTService chatGPTService){
        this.chatGPTService=chatGPTService;
    }


    @PostMapping("/chat")
    public Flux<String> chat(@RequestBody String message){
        return chatGPTService.chat(message);
    }
}
