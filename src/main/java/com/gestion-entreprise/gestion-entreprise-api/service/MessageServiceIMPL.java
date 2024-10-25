package com.example.springbootoauthjwt.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springbootoauthjwt.dao.ChatRepository;
import com.example.springbootoauthjwt.dao.MeassageRepository;
import com.example.springbootoauthjwt.dao.RoleRepository;
import com.example.springbootoauthjwt.exceptions.ChatException;
import com.example.springbootoauthjwt.exceptions.MessageException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.mappers.Mapper;
import com.example.springbootoauthjwt.model.ChatRoom;
import com.example.springbootoauthjwt.model.Message;
import com.example.springbootoauthjwt.model.User;




@Service
public class MessageServiceIMPL implements MessageService{
    @Autowired
     private  MeassageRepository meassageRepository;
     @Autowired
     private  UserService userService;
     @Autowired
     private  ChatService chatService;

     @Autowired
     private Mapper mapper;

    @Override
    public Message sendMessage(Long userId, Long chatId, String content) throws UserException, ChatException {
       User user=userService.findUserById(userId);
        ChatRoom chat=chatService.findChatById(chatId);

        Message message=new Message();
        message.setChatRoom(chat);
        message.setSender(user);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        
        return message;
    }

    @Override
    public List<Message> getChat_Messages(ChatRoom chatRoom,User user) throws UserException {
         
        if (chatRoom.getUsers().contains(user)) {
        return meassageRepository.findByChatId(chatRoom.getChatId());
    }
    throw new UserException("You are not a member of this chat room");
    }

    @Override
    public void deleteMessage(Long messageId, User reqUser) throws MessageException, UserException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteMessage'");
    }

    
}
