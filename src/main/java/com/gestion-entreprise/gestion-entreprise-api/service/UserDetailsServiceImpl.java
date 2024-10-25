package com.example.springbootoauthjwt.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.springbootoauthjwt.configuration.UserDetailsImpl;

import com.example.springbootoauthjwt.dao.UserRepository;
import com.example.springbootoauthjwt.model.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username " + username);

        Optional  <User> user = userRepository.findByEmail(username);
        System.out.println("loadUserByUsername "+user.get());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("No user named " + username);
        } else {
            // System.out.println("*************************************************************");
            // System.out.println("user"+user.get().getUsername());
            // System.out.println(user.get().getPassword());

            return new UserDetailsImpl(user.get());
        }
    }
}
