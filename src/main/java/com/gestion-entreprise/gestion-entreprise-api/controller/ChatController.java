package com.example.springbootoauthjwt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbootoauthjwt.dtos.ChatRoomDTO;
import com.example.springbootoauthjwt.exceptions.ChatException;
import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.mappers.Mapper;
import com.example.springbootoauthjwt.model.ChatRoom;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.User;
import com.example.springbootoauthjwt.service.ChatService;
import com.example.springbootoauthjwt.service.CompanyService;
import com.example.springbootoauthjwt.service.UserService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@RestController
@RequestMapping("/chats")
public class ChatController {
   
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private Mapper mapper;

    @PostMapping("/create")
    public ResponseEntity<ChatRoom> createChatRoomHandler(@RequestHeader("Authorization") String token,@RequestBody RequestCreateChat request) throws UserException, RoleException, CompanyException, ChatException{
        Jwt jwt = jwtDecoder.decode(token.substring(7));

        User user = userService.findUserByEmail(jwt.getSubject().toString());

        Company optcompany = companyService.findByCompanyId(request.getCompany().getCompanyId());
        ChatRoom chat=chatService.createChat(user, request.getChatName(),optcompany);
        
       return new ResponseEntity<ChatRoom>(chat,HttpStatus.OK);
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<List<ChatRoomDTO>> findAllChatByCompanyIdHandler(@PathVariable Long companyId,
            @RequestHeader("Authorization") String token) throws UserException, CompanyException, RoleException {
        Jwt jwt = jwtDecoder.decode(token.substring(7));

        User user = userService.findUserByEmail(jwt.getSubject().toString());

        List<ChatRoomDTO> chats = chatService.findAllChatByCompanyId(user,companyId).stream()
                .map(mapper::fromChatRoom)
                .toList();
        return new ResponseEntity<List<ChatRoomDTO>>(chats, HttpStatus.OK);
    }
    
    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<ChatRoom> addUserToChatRoomHandler(@PathVariable Long chatId,@PathVariable  Long userId,@RequestHeader("Authorization") String token) throws UserException, ChatException, CompanyException {
        
        ChatRoom chat = chatService.addUserToChatRoom(userId, chatId);
        return new ResponseEntity<ChatRoom>(chat, HttpStatus.OK);
    }
    
    // @PutMapping("/{chatId}/remove/{userId}")
    // public ResponseEntity<Chat> removeUserFromGroupHandler(@PathVariable Integer chatId, @PathVariable Long userId,
    //         @RequestHeader("Authorization") String token) throws UserException, ChatException {
    //     User reqUser = userService.findUserProfile(token);
    //     Chat chat = chatService.removeFromGroup(chatId, userId, reqUser);
    //     return new ResponseEntity<Chat>(chat, HttpStatus.OK);
    // }

    // @PutMapping("/delete/{chatId}")
    // public ResponseEntity<ApiResponse> deleteChatHandler(@PathVariable Integer chatId,
    //         @RequestHeader("Authorization") String token) throws UserException, ChatException {
    //     User reqUser = userService.findUserProfile(token);
    //      chatService.deleteChat(chatId, reqUser.getId());
    //      ApiResponse res=new ApiResponse("Chat is successfully deleted", false);
    //     return new ResponseEntity<>(res, HttpStatus.OK);
    // }


     @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestCreateChat {
        private String chatName;
        private Company company;
        
    }
}
