package com.example.springbootoauthjwt.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbootoauthjwt.model.Message;

@Controller
// @RestController
public class RealTimeChat {
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // @MessageMapping("/chats")
    @SendTo("/send/message")
    public String sendToUser(@Payload String message){

       simpMessagingTemplate.convertAndSend("/topic", message);
        System.out.println("message  : "+message);
        return message;
    }
}
