package com.example.springbootoauthjwt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbootoauthjwt.dtos.MessageDTO;
import com.example.springbootoauthjwt.exceptions.ChatException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.mappers.Mapper;
import com.example.springbootoauthjwt.model.ChatRoom;
import com.example.springbootoauthjwt.model.Message;
import com.example.springbootoauthjwt.model.User;
import com.example.springbootoauthjwt.service.ChatService;
import com.example.springbootoauthjwt.service.MessageService;
import com.example.springbootoauthjwt.service.UserService;

@RestController
@CrossOrigin("*")

public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private Mapper mapper;

    @Autowired
    private JwtDecoder jwtDecoder;

    @GetMapping("/{chatId}")
    public ResponseEntity<List <MessageDTO>> getChatMessageHandler(@PathVariable Long chatId,@RequestHeader("Authorization") String token) throws UserException, ChatException {
        Jwt jwt = jwtDecoder.decode(token.substring(7));

        User user = userService.findUserByEmail(jwt.getSubject().toString());
        ChatRoom chatRoom=chatService.findChatById(chatId);
        List< MessageDTO> messages = messageService.getChat_Messages(chatRoom, user).stream().map(mapper::fromMessage).toList();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
}
