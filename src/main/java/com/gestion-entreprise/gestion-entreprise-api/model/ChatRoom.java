package com.example.springbootoauthjwt.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;
   
    private String chatName;

    @ManyToOne
    private Company company;
    
    @OneToMany(mappedBy = "chatRoom",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Message> messages=new HashSet<>();

    @ManyToMany()
    private Set<User> users = new HashSet<>();
}
