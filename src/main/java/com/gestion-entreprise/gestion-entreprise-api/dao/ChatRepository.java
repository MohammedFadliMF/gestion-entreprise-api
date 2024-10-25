package com.example.springbootoauthjwt.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.springbootoauthjwt.model.ChatRoom;

public interface ChatRepository  extends JpaRepository<ChatRoom,Long>{
    
    @Query("select c from ChatRoom c where c.company.companyId=:companyId")
    public List<ChatRoom> findChatByCompanyId(@Param("companyId") Long chatId);

    Optional <ChatRoom> findByChatId(Long chatId);
    // @Query("select c from Chat c where c.isGroup=false And :user Member of c.users And :reqUser Member of c.users")
    // public Chat findSingleChatByUserIds(@Param("user") User user,@Param("reqUser") User reqUser);

}
