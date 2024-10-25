package com.example.springbootoauthjwt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.springbootoauthjwt.dao.RoleRepository;
import com.example.springbootoauthjwt.dao.UserRepository;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Role;
import com.example.springbootoauthjwt.model.User;

@Service
public class UserServiceIMPL implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User registerUser(User user) throws UserException {

        if (user.getEmail() == null || user.getUsername() == null || user.getPassword() == null) {
            throw new UserException("All fields are required!");
        }
        Optional<User> isEmailExist = userRepository.findByEmail(user.getEmail());

        if (isEmailExist.isPresent()) {
            throw new UserException("Email Is Already Exist");
        }

        // Optional<User> isUsernameExist =
        // userRepository.findByUsername(user.getUsername());
        // if (isUsernameExist.isPresent()) {
        // throw new UserException("Username Is Already Taken");
        // }

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());

        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(newUser);
    }

    @Override
    public User findUserById(Long userId) throws UserException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }
        throw new UserException("User Not Exist With Id: " + userId);

        // Optional<User> optionalUser = userRepository.findById(userId);
        // return optionalUser.orElseThrow(() -> new UserException("User Not Exist With
        // Id: " + userId));

        // User user2=userRepository.findById(userId).orElse(null);
        // if (user2==null) {
        // throw new UserException("User Not Exist With Id: " + userId);
        // }
        // return user2;

    }

    // @Override
    // public User findUserProfile(String token) throws UserException {
    // System.out.println("token : "+token);
    // token=token.substring(7);

    // JwtTokenClaims jwtTokenClaims=jwtTokenProvider.getClaimsFromToken(token);

    // //in JwtGeneratorFilter we added in claims username as email
    // String email=jwtTokenClaims.getUsername();
    // Optional<User> opt=userRepository.findByEmail(email);

    // if (opt.isPresent()) {
    // return opt.get();
    // }
    // throw new UserException("token invalid");

    // }

    @Override
    public User findUserByEmail(String email) throws UserException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        }
        throw new UserException("User does not exist with email: " + email);
    }

    @Override
    public List<User> findUsersByIds(List<Long> usersIds) throws UserException {
        List<User> users = userRepository.findAllUsersByUsersIds(usersIds);
        return users;
    }

    @Override
    public List<User> searchUser(String query) throws UserException {
        List<User> users = userRepository.findByQuery(query);
        if (users.size() == 0) {
            throw new UserException("User Not Found");
        }
        return users;
    }

    @Override
    public User updateUser(User newUser, User oldUser) throws UserException {
        if (newUser.getUserId() == null || !newUser.getUserId().equals(oldUser.getUserId())) {
            throw new UserException("Not The Same Users");
        }
        if (newUser.getUsername() != null && !newUser.getUsername().isBlank()
                && newUser.getEmail() != null && !newUser.getEmail().isBlank()
                && newUser.getPassword() != null && !newUser.getPassword().isBlank()) {
            // oldUser.setUserId(newUser.getUserId());
            oldUser.setUsername(newUser.getUsername());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setPassword(newUser.getPassword());
            // oldUser.setRoles(newUser.getRoles());

            User updatedUser = userRepository.save(oldUser);
            if (updatedUser != null) {
                return updatedUser;
            } else {
                throw new UserException("Failed To update user.");
            }
        } else {
            throw new UserException("invalid data for update.");
        }
    }

    @Override
    public List<User> findAllUsersByRolesIds(List<Long> rolesIds) throws UserException {
        List<User> users = userRepository.findAllUsersByRolesIds(rolesIds);
        if (users.size() != 0) {
            return users;
        }
        throw new UserException("Users not found");
    }

    
    public void removeUserFromCompany(User user, Company company) {

        Optional<Role> removedRole = user.getRoles().stream()
                .filter(r -> r.getCompany().equals(company))
                .findFirst();
        if (removedRole.isPresent()) {
            user.getRoles().remove(removedRole);
            // roleRepository.delete(removedRole);
        }
    }

}
