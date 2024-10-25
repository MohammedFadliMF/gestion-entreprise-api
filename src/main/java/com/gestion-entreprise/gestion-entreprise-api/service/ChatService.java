package com.example.springbootoauthjwt.service;

import java.util.List;

import com.example.springbootoauthjwt.exceptions.ChatException;
import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.ChatRoom;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.User;

public interface ChatService {
    public ChatRoom createChat(User userReq,String chatRoom, Company company) throws UserException, RoleException, CompanyException,
            ChatException;

    public ChatRoom findChatById(Long chatId) throws ChatException;

    public List<ChatRoom> findAllChatByCompanyId(User user,long companyId) throws UserException, CompanyException,RoleException;

    // public Chat createGroup(GroupChatRequest req, User reqUser) throws UserException;

    public ChatRoom addUserToChatRoom(Long userId, Long chatId) throws UserException, ChatException, CompanyException;

    public ChatRoom renameChatRoom(Integer chatId, String chatName, User reqUser) throws UserException, ChatException;

    public ChatRoom removeFromChatRoom(Integer chatId, Long userId, User reqUser) throws UserException, ChatException;

    public void deleteChatRoom(Integer chatId, Long userId) throws UserException, ChatException;
}
