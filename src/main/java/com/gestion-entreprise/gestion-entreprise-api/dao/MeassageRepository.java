package com.example.springbootoauthjwt.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.springbootoauthjwt.model.Message;

public interface MeassageRepository extends JpaRepository<Message,Long>{
    
    // @Query("select m from Message m where m.chatRoom.chatId = :chatId order by m.timestamp asc")
    @Query("select m From Message m  where m.chatRoom.chatId=:chatId")
    public List<Message> findByChatId(@Param("chatId") Long chatId);
    
} 
