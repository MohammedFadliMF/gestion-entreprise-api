package com.example.springbootoauthjwt.controller;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springbootoauthjwt.dao.RoleRepository;
import com.example.springbootoauthjwt.dao.UserRepository;
import com.example.springbootoauthjwt.dtos.RoleDTO;
import com.example.springbootoauthjwt.dtos.UserDTO;
import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.CustomerException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.mappers.Mapper;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Role;
import com.example.springbootoauthjwt.model.User;
import com.example.springbootoauthjwt.service.CompanyService;
import com.example.springbootoauthjwt.service.MailService;
import com.example.springbootoauthjwt.service.RoleService;
import com.example.springbootoauthjwt.service.UserService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@CrossOrigin("*")

@RequestMapping("/auth")
public class SecurityController {
    // private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
    // private static final SecureRandom random = new SecureRandom();
    // private StringBuilder sb = new StringBuilder(8);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private Mapper mapper;

    @Autowired
    private MailService mailService;
     @Autowired
    private JwtDecoder jwtDecoder;

    // @GetMapping("/profile")
    // public Authentication authentication(Authentication authentication) {
    //     return authentication;
    // }

    @PostMapping("/login")
    public Map<String, String> login(String username, String password) throws RoleException, UserException {
        Authentication authentication = (Authentication) authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        Instant instant = Instant.now();
        String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        ///////////////////
        User user = userService.findUserByEmail(username);
        List<RoleDTO> roles = roleService.findAllRolesByUserId(user.getUserId());

        ///////////////////
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(2, ChronoUnit.HOURS))
                .subject(username)
                .claim("user", mapper.fromUser(user))
                .claim("scope", scope)
                .claim("roles", roles)
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS512).build(),
                jwtClaimsSet);
        String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
        return Map.of("access-token", jwt);
    }

    // @PostMapping("/signup")
    // public ResponseEntity<UserDTO> registerUserHandler(@RequestBody User user) throws UserException {
    //     User createdUser = userService.registerUser(user);
    //     UserDTO userDTO = mapper.fromUser(createdUser);

    //     return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
    // }
    @PostMapping("/signup")
    public Map<String, String> registerUserHandler(@RequestBody User user) throws UserException, RoleException {
        User createdUser = userService.registerUser(user);
        UserDTO userDTO = mapper.fromUser(createdUser);

        // Generate a new JWT token
        Instant instant = Instant.now();
        String scope = createdUser.getRoles().stream()
                .map(Role::getPermition) // Assuming you have a getName method in Role
                .collect(Collectors.joining(" "));

        List<RoleDTO> roles = roleService.findAllRolesByUserId(createdUser.getUserId());

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(2, ChronoUnit.HOURS))
                .subject(createdUser.getEmail())
                .claim("user", mapper.fromUser(createdUser))
                .claim("scope", scope)
                .claim("roles", roles)
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS512).build(),
                jwtClaimsSet);

        String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();

        return Map.of("access-token", jwt);
    }

    

    // @PostMapping("/invite-user/{companyId}")
    // public ResponseEntity<User> InviteUserHandler(@RequestBody invitedUserReq request,
    //         @RequestHeader("Authorization") String token, @PathVariable Long companyId)
    //         throws UserException, CompanyException {

    //         Jwt jwt=jwtDecoder.decode(token.substring(7));
       
    //     User admin=userRepository.findByEmail(jwt.getSubject().toString()).get();
    //   // generate random password
    //     for (int i = 0; i < 8; i++) {
    //         int randomIndex = random.nextInt(CHARACTERS.length());
    //         char randomChar = CHARACTERS.charAt(randomIndex);
    //         sb.append(randomChar);
    //     }
    //     request.getUser().setPassword(sb.toString());
  
    //     User invitedUser = userService.registerUser(request.getUser());
        
    //     Company company = companyService.findByCompanyId(companyId);
        
        
    //     // UserDTO userDTO = mapper.fromUser(createdUser);

    //     Role role = new Role();
    //     role.setPermition(request.getPermission());
    //     invitedUser.getRoles().add(role);
    //     role.setCompany(company);
    //     roleRepository.save(role);

    //     MailStructure mailStructure = new MailStructure();
    //     mailStructure.setMessage(sb.toString());// jwtDecoder.decode(invitedUser.getPassword()).toString()
    //     mailStructure.setSender(company.getCompanyName() + admin.getEmail());
    //     mailStructure.setSubject("Authentication");
    //     mailStructure.setTo(invitedUser.getEmail());

    //     mailService.sendMail(mailStructure);
    //     return new ResponseEntity<User>(invitedUser, HttpStatus.OK);
    // }
   
    @PostMapping("/invite-user/{companyId}")
    public ResponseEntity<String> InviteUserHandler(@RequestBody invitedUserReq request,
            @RequestHeader("Authorization") String token, @PathVariable Long companyId)
            throws UserException, CompanyException, RoleException {
         String message="user not added";
        Jwt jwt = jwtDecoder.decode(token.substring(7));
System.out.println("**************************************************************");

System.out.println(request.getUser());
        User admin = userService.findUserByEmail(jwt.getSubject().toString());
        // User useri=userService.findUserByEmail(request.getUser().getEmail());
       
        Optional<User> invitedU = userRepository.findByEmail(request.getUser().getEmail());
        
        Company company = companyService.findByCompanyId(companyId);
       
        if (invitedU.isPresent()) {  
           if (companyService.getAllUsers(company).contains(invitedU.get())) {
               message="Utilisateur déjà en entreprise";  
           }
        }else{
            Company company2=companyService.inviteUserToCompany(admin, request.getUser(), company, request.getPermission());
             if (company2 != null) {
                message="L'invitation a été envoyée avec succès";
              }
        }

        return new ResponseEntity<String>(message, HttpStatus.OK);
    }

    @GetMapping("/{companyId}/users")
    public ResponseEntity<List<UserDTO>> allUserInCompanyHandler(@PathVariable Long companyId) throws UserException, RoleException, CompanyException {
        Company company=companyService.findByCompanyId(companyId);
        List<UserDTO> users = companyService.getAllUsers(company).stream().map(mapper::fromUser).toList();
        return new ResponseEntity<List<UserDTO>>(users, HttpStatus.OK);
    }

    
    @GetMapping("/invite/response")
    public ResponseEntity<String> handleInvitationResponse(@RequestParam String token) {
        try {
            companyService.handleInvitationResponse(token);
            return ResponseEntity.ok("Invitation accepted and user added to the company.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error handling invitation response: " + e.getMessage());
        }
    }
   
    @PutMapping("/update")
    public ResponseEntity<User> updateUserHandler(@RequestHeader("Authorization") String token, @RequestBody User newUser)throws RoleException, CustomerException, CompanyException, UserException {
        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User oldUser = userService.findUserByEmail(jwt.getSubject().toString());
        User updatedUser = userService.updateUser(newUser,oldUser);
        return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
    }
    


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class invitedUserReq {
        private User user;
        private String permission;
    }
}
