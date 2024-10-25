package com.example.springbootoauthjwt.controller;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import com.example.springbootoauthjwt.dao.RoleRepository;
import com.example.springbootoauthjwt.dao.UserRepository;
import com.example.springbootoauthjwt.dtos.ItemDTO;
import com.example.springbootoauthjwt.dtos.RoleDTO;
import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.mappers.Mapper;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Expense;
import com.example.springbootoauthjwt.model.Invoice;
import com.example.springbootoauthjwt.model.Role;
import com.example.springbootoauthjwt.model.User;
import com.example.springbootoauthjwt.service.CompanyService;
import com.example.springbootoauthjwt.service.RoleService;
import com.example.springbootoauthjwt.service.UserService;

@RestController

@CrossOrigin("*")
public class CompanyController {
    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Mapper mapper;
    @Autowired
    private JwtEncoder jwtEncoder;

    @GetMapping("/total/{companyId}")
    public Map<String, Double> totalCompanyHandler(
            @RequestHeader("Authorization") String token, @PathVariable Long companyId)
            throws CompanyException, UserException, RoleException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User adminUser = userService.findUserByEmail(jwt.getSubject().toString());
        Company company = companyService.findByCompanyId(companyId);
        
        double totalInvoices = company.getInvoices().stream()
                .mapToDouble(Invoice::getTotal_amount).sum();

        double totalItems = company.getItems().stream()
                .mapToDouble(ItemDTO::getPrice).sum();

        double totalExpenses = company.getExpences().stream()
                .mapToDouble(Expense::getAmount).sum();

        Map<String, Double> totals = new HashMap<>();
        totals.put("totalInvoices", totalInvoices);
        totals.put("totalItems", totalItems);
        totals.put("totalExpenses", totalExpenses);
        return totals;
    }

    @PostMapping("/create-company")
    public Map<String, String> registerCompanyHandler(
            @RequestHeader("Authorization") String token, @RequestBody CompanyRequest request)
            throws CompanyException, UserException, RoleException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User adminUser = userService.findUserByEmail(jwt.getSubject().toString());
        Company createdCompany = companyService.registerCompany(request.getCompany());

        Role role = new Role();
        role.setPermition(request.getPermission());
        adminUser.getRoles().add(role);
        role.setCompany(createdCompany);
        roleRepository.save(role);

        // Generate a new JWT token
        Instant instant = Instant.now();
        String scope = adminUser.getRoles().stream()
                .map(Role::getPermition) // Assuming you have a getName method in Role
                .collect(Collectors.joining(" "));

        List<RoleDTO> roles = roleService.findAllRolesByUserId(adminUser.getUserId());

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(2, ChronoUnit.HOURS))
                .subject(adminUser.getEmail())
                .claim("user", mapper.fromUser(adminUser))
                .claim("scope", scope)
                .claim("roles", roles)
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS512).build(),
                jwtClaimsSet);

        String newJwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();

        return Map.of("access-token", newJwt);
    }

    @PutMapping("/update-company/{companyId}")
    public ResponseEntity<Company> updateCompanyHandler(
            @RequestHeader("Authorization") String token, @RequestBody Company newCompany, @PathVariable Long companyId)
            throws CompanyException, UserException, RoleException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userService.findUserByEmail(jwt.getSubject().toString());

        Company oldCompany = companyService.findByCompanyId(companyId);
        Company updatedCompany = companyService.updateCompany(user, newCompany, oldCompany);
        return new ResponseEntity<Company>(updatedCompany, HttpStatus.OK);
    }

    @PostMapping("/remove-user/{userId}/{companyId}")
    public ResponseEntity<Company> removeUserHandler(@RequestHeader("Authorization") String token,
            @PathVariable Long userId,
            @PathVariable Long companyId) throws RoleException, UserException, CompanyException {

        User user = userService.findUserById(userId);
        Company company = companyService.findByCompanyId(companyId);
        Optional<Role> removedRole = user.getRoles().stream()
                .filter(r -> r.getCompany().equals(company))
                .findFirst();
        if (removedRole.isPresent()) {
            user.getRoles().remove(removedRole);
            // userRepository.save(user);
            // roleRepository.delete(removedRole);
        }
        return new ResponseEntity<Company>(company, HttpStatus.OK);
    }

    @PostMapping("/create-role")
    public ResponseEntity<Role> registerRoleHandler(@RequestBody Role role) throws RoleException {

        Role role1 = roleService.registerRole(role);

        return new ResponseEntity<Role>(role1, HttpStatus.OK);
    }

    @GetMapping("/getuser/{id}")
    public ResponseEntity<User> getUserHandler(@PathVariable Long id) throws UserException {
        User user = userService.findUserById(id);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    public static class CompanyRequest {
        private Company company;
        private String permission;

        // Getters and setters
        public Company getCompany() {
            return company;
        }

        public void setCompany(Company company) {
            this.company = company;
        }

        public String getPermission() {
            return permission;
        }

        public void setPermission(String permission) {
            this.permission = permission;
        }
    }

    // @DeleteMapping("/company/{1,2,3,4,5,6,7,8,9,10,11,12,13,14}")
    // public ResponseEntity<String> deletecompanyHandler(@PathVariable Long id)
    // throws UserException {
    // User user = userService.findUserById(id);
    // return new ResponseEntity<String>("has been delted", HttpStatus.OK);
    // }

}
