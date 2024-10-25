package com.example.springbootoauthjwt.service;

import java.util.List;

import com.example.springbootoauthjwt.exceptions.ChatException;
import com.example.springbootoauthjwt.exceptions.MessageException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.ChatRoom;
import com.example.springbootoauthjwt.model.Message;
import com.example.springbootoauthjwt.model.User;

public interface MessageService {

    public Message sendMessage(Long userId,Long chatId,String content) throws UserException, ChatException;

    public List<Message> getChat_Messages(ChatRoom chatRoom,User user) throws UserException;

    // public Message findMessageById(Long messageId) throws MessageException;

    public void deleteMessage(Long messageId, User reqUser) throws MessageException, UserException;

}
