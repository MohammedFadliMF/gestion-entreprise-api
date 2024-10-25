package com.example.springbootoauthjwt.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springbootoauthjwt.dao.ChatRepository;
import com.example.springbootoauthjwt.exceptions.ChatException;
import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.ChatRoom;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.User;


@Service
public class ChatServiceIMPL implements ChatService{
    @Autowired
     private  ChatRepository chatRepository;
     @Autowired
     private  UserService userService;
     @Autowired
     private CompanyService companyService;
     
    @Override
    public ChatRoom createChat(User userReq, String chatName, Company company) throws UserException, RoleException, CompanyException, ChatException {
        
        List<String> chatNames=  chatRepository.findChatByCompanyId(company.getCompanyId()).stream().map(ch->ch.getChatName()).collect(Collectors.toList());

        if (chatNames.contains(chatName)) {
            throw new ChatException("This Chat Already Exists in your Company :"+chatName);
        }
        if (companyService.getAllUsers(company).contains(userReq)) {
            ChatRoom chat = new ChatRoom();
            chat.setChatName(chatName);
            chat.setCompany(company);

            return chatRepository.save(chat);
        }
         throw new UserException("You are Not Allowed !");
    }
    @Override
    public ChatRoom findChatById(Long chatId) throws ChatException {
        Optional<ChatRoom> chat = chatRepository.findById(chatId);
        if (chat.isPresent()) {
            return chat.get();
        }
        throw new ChatException("Chat not found with Id: " + chatId);
    }
    @Override
    public List<ChatRoom> findAllChatByCompanyId(User user,long companyId) throws UserException, CompanyException, RoleException {
        Company company =companyService.findByCompanyId(companyId);
        if (companyService.getAllUsers(company).contains(user)) {
            List<ChatRoom> chats = chatRepository.findChatByCompanyId(company.getCompanyId());
            return chats;
        }
        throw new UserException("You are not allowed !");

    }
    @Override
    public ChatRoom addUserToChatRoom(Long userId, Long chatId) throws UserException, ChatException, CompanyException {
        Optional<ChatRoom> optChat = chatRepository.findByChatId(chatId);
        
        User user = userService.findUserById(userId);

        if (optChat.isPresent()) {
            ChatRoom chat = optChat.get();
            Company company=companyService.findByCompanyId(optChat.get().getCompany().getCompanyId());
            
            if (optChat.get().getCompany().equals(company)) {
                optChat.get().getUsers().add(user);
                return chat;
            } else {
                throw new ChatException("Chat not found in company "+company.getCompanyName());
            }

        }
        throw new ChatException("Chat not found with Id : " + chatId);
    }

    @Override
    public ChatRoom renameChatRoom(Integer chatId, String chatName, User reqUser) throws UserException, ChatException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'renameChtaRoom'");
    }
    @Override
    public ChatRoom removeFromChatRoom(Integer chatId, Long userId, User reqUser) throws UserException, ChatException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFromChatRoom'");
    }
    @Override
    public void deleteChatRoom(Integer chatId, Long userId) throws UserException, ChatException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteChatRoom'");
    }

 
}
