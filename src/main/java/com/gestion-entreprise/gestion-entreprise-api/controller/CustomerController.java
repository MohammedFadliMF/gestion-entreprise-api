package com.example.springbootoauthjwt.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import com.example.springbootoauthjwt.dao.UserRepository;
import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.CustomerException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Customer;
import com.example.springbootoauthjwt.model.User;
import com.example.springbootoauthjwt.request.RequestCreateCustomer;
import com.example.springbootoauthjwt.request.RequestDeleteCustomer;
import com.example.springbootoauthjwt.request.RequestUpdateCustomer;
import com.example.springbootoauthjwt.service.CompanyService;
import com.example.springbootoauthjwt.service.CustomerService;
import com.example.springbootoauthjwt.service.UserService;


@RestController
@RequestMapping("/customer")
@CrossOrigin("*")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtDecoder jwtDecoder;
 

    @PostMapping("/create-customer/{companyId}")
    public ResponseEntity<Customer> registerCustomeryHandler(@RequestHeader("Authorization") String token,@RequestBody  Customer req_customer,@PathVariable Long companyId ) throws CompanyException, CustomerException, UserException, RoleException{

        Jwt jwt = jwtDecoder.decode(token.substring(7));

        User user = userService.findUserByEmail(jwt.getSubject().toString());

          Company optcompany = companyService.findByCompanyId(companyId);
           Customer customer=customerService.createCustomer(user,req_customer, optcompany);
    
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }
    
    @PutMapping("/update/{customerId}/{companyId}")
    public ResponseEntity<Customer> updateCustomerHandler(@RequestHeader("Authorization") String token, @RequestBody Customer newCustomer,@PathVariable Long customerId,
            @PathVariable Long companyId)throws RoleException, CustomerException, CompanyException, UserException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userService.findUserByEmail(jwt.getSubject().toString());
        
        Customer rCustomer=customerService.findByCustomerId(customerId);
        Company rCompany=companyService.findByCompanyId(companyId);
       
        Customer customer = customerService.updateCustomer(user,rCompany,rCustomer, newCustomer);
    
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }
    
    @DeleteMapping("/{customerId}/{companyId}")
    public ResponseEntity<String> deleteCustomerHandler(@RequestHeader("Authorization") String token,@PathVariable Long customerId,@PathVariable Long companyId)
            throws RoleException, CustomerException, CompanyException, UserException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userRepository.findByEmail(jwt.getSubject().toString()).get();

        Customer rCustomer = customerService.findByCustomerId(customerId);
        Company rCompany = companyService.findByCompanyId(companyId);

        String message = customerService.removeCustomer(user, rCompany, rCustomer);

        return new ResponseEntity<String>(message, HttpStatus.OK);
    }
    
    @GetMapping("/customers/{companyId}")
    public ResponseEntity<List<Customer>> AllCustomersInCompanyHandler(@RequestHeader("Authorization") String token,
            @PathVariable Long companyId)
            throws RoleException, CustomerException, CompanyException, UserException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userRepository.findByEmail(jwt.getSubject().toString()).get();
       // System.out.println("companyName "+ company.getCompanyName());
        Company rCompany = companyService.findByCompanyId(companyId);

        List<Customer> customers = customerService.findAllCustomersInCompany(user, rCompany);

        return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
    }
    
    
}
