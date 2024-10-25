package com.example.springbootoauthjwt.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import com.example.springbootoauthjwt.dao.CompanyRepository;
import com.example.springbootoauthjwt.dao.RoleRepository;
import com.example.springbootoauthjwt.dao.UserRepository;
import com.example.springbootoauthjwt.dtos.RoleDTO;
import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.mappers.Mapper;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.MailStructure;
import com.example.springbootoauthjwt.model.Role;
import com.example.springbootoauthjwt.model.User;

@Service
public class CompanyServiceIMPL implements CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailService;

    @Autowired
    JwtEncoder jwtEncoder;
    @Autowired
    JwtDecoder jwtDecoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Mapper mapper;
    

    @Override
    public Company registerCompany(Company company) throws CompanyException {
        Optional<Company> isCompanyExist = companyRepository.findByCompanyEmail(company.getCompanyEmail());
      
        if (isCompanyExist.isPresent()) {
            throw new CompanyException("A company associated with this email already exists. Please use a different email address");
        }
        if (company.getCompanyName() == null||company.getCompanyAdress()==null ||company.getCompanyEmail()==null) {
            throw new CompanyException("Please ensure all fields are completed.");
        }

        Company newCompany = new Company();
        newCompany.setCompanyName(company.getCompanyName());
        newCompany.setCompanyAdress(company.getCompanyAdress());
        newCompany.setCompanyEmail(company.getCompanyEmail());
        return companyRepository.save(newCompany);
    }

    @Override
    public List<Company> findCompanysByIds(List<Long> companysIds) throws CompanyException {
        List<Company> companys = companyRepository.findAllCompanyByCompanyIds(companysIds);
        return companys;
    }

    @Override
    public Company updateCompany(User user,Company newCompany, Company oldCompany) throws CompanyException, UserException, RoleException {
       
        if (getAllUsers(oldCompany).contains(user)) {
            oldCompany.setCompanyName(
            newCompany.getCompanyName() != null ? newCompany.getCompanyName() : oldCompany.getCompanyName());

            oldCompany.setCompanyEmail(newCompany.getCompanyEmail()!=null ? newCompany.getCompanyEmail() : newCompany.getCompanyEmail());
            oldCompany.setCompanyAdress(
                    newCompany.getCompanyAdress() != null ? newCompany.getCompanyAdress() : oldCompany.getCompanyAdress());
           
            Company updatedCompany = companyRepository.save(oldCompany);
            if (updatedCompany != null) {
                return updatedCompany;
            } 
        } 
            throw new CompanyException("invalid data for update.");
     
    }

    @Override
    public Role findRoleByName(Company company, String rolename) throws CompanyException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findRoleByName'");
    }

    @Override
    public Company findCompanyByEmail(String companyName) throws CompanyException {
        Optional<Company> company = companyRepository.findByCompanyEmail(companyName);
        if (company.isPresent()) {
            return company.get();
        }
        throw new CompanyException("Company Not Exist With Email : " + companyName);
    }

    @Override
    public Company findByCompanyId(Long companyId) throws CompanyException {
        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isPresent()) {
            return company.get();
        }
        throw new CompanyException("Company Not found ");
    }

    @Override
    public List<User> getAllUsers(Company company) throws UserException, RoleException, CompanyException {

        List<Role> roles = roleService.findAllRolesByCompanyId(company.getCompanyId());
        List<Long> rolesIds = roles.stream().map(r -> r.getRoleId()).collect(Collectors.toList());

        return userService.findAllUsersByRolesIds(rolesIds);
    }

    @Override
    public Company AddUserToCompany(User admin, User user, Company company, String permission)
            throws UserException, CompanyException, RoleException {
        if (!getAllUsers(company).contains(user)) {
            Role role = new Role();
            role.setPermition(permission);
            user.getRoles().add(role);
            role.setCompany(company);
            roleRepository.save(role);

            MailStructure mailStructure = new MailStructure();
            mailStructure.setMessage("you are added to that company ");// jwtDecoder.decode(invitedUser.getPassword()).toString()
            mailStructure.setSender(company.getCompanyName() + admin.getEmail());
            mailStructure.setSubject("Authentication");
            mailStructure.setTo(user.getEmail());

            mailService.sendMailToUser(mailStructure);
            return company;
        }
        throw new UserException("user already exists !");

    }

    @Override
    public Company inviteUserToCompany(User admin, User user, Company company, String permission)
            throws UserException, CompanyException, RoleException {

        // if
        // (getAllUsers(company).contains(userService.findUserByEmail(user.getEmail())))
        // {
        // throw new UserException("User already exists!");
        // }

        // if (userRepository.findByEmail(user.getEmail()).isPresent()) {
        // throw new UserException("User already exists!");
        // }

        // Generate a unique token containing the user's email and company ID
        String token = generateToken(user.getEmail(), user.getUsername(), company.getCompanyId(), permission);

        // Send an invitation email
        MailStructure mailStructure = new MailStructure();
        mailStructure.setMessage("You are invited to join the company " + company.getCompanyName()
                + ". Please click the link to accept the invitation: "
                + "http://localhost:8585/auth/invite/response?token=" + token);
        mailStructure.setSender(company.getCompanyName() + " <" + admin.getEmail() + ">");
        mailStructure.setSubject("Company Invitation");
        mailStructure.setTo(user.getEmail());

        mailService.sendMailToUser(mailStructure);

        return company;
    }

    private String generateToken(String email, String username, Long companyId, String permission) {
        Instant now = Instant.now();

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plus(2, ChronoUnit.HOURS))
                .subject(email)
                .claim("email", email)
                .claim("companyId", companyId)
                .claim("permission",permission)
                .claim("username", username)

                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS512).build(),
                jwtClaimsSet);
        String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();

        return jwt;
    }

    @Override
    public void handleInvitationResponse(String token) throws UserException, CompanyException, RoleException {
        Jwt decodedToken;
        try {
            decodedToken = jwtDecoder.decode(token);
        } catch (JwtException e) {
            throw new UserException("Invalid invitation token!");
        }
        String username = decodedToken.getClaim("username");
        String email = decodedToken.getClaim("email");
        Long companyId = decodedToken.getClaim("companyId");
        String permission = decodedToken.getClaim("permission");

        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            // Create a new user
            user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword("111"); // Ideally, you'd want to prompt the user to set this securely
            // userRepository.save(user);
            user = userService.registerUser(user);
        }

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyException("Company not found"));

        // Check if the user is already in the company
        if (getAllUsers(company).contains(user)) {
            throw new UserException("Utilisateur déjà en entreprise !");
        }

        // Assign the role and add user to company
        Role role = new Role();
        role.setPermition(permission.toString()); // Define your permission
        user.getRoles().add(role);
        role.setCompany(company);
        roleRepository.save(role);

        // Send confirmation email
        MailStructure mailStructure = new MailStructure();
        mailStructure.setMessage("Vous avez été ajouté à l'entreprise" + company.getCompanyName());
        // mailStructure.setSender(company.getCompanyName() + " <" + admin.getEmail() +
        // ">");
        mailStructure.setSender(company.getCompanyName());

        mailStructure.setSubject("Bienvenue dans l'entreprise");
        mailStructure.setTo(user.getEmail());
        mailService.sendMailToUser(mailStructure);
    };


     public Map<String, String> generateTokenString(String username, String password) throws UserException, RoleException{
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

}
