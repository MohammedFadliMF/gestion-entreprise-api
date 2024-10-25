package com.example.springbootoauthjwt.controller;


import java.util.List;
import java.util.Set;

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
import com.example.springbootoauthjwt.exceptions.TaxException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Tax;
import com.example.springbootoauthjwt.model.User;
import com.example.springbootoauthjwt.service.CompanyService;
import com.example.springbootoauthjwt.service.TaxService;
import com.example.springbootoauthjwt.service.UserService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@RestController
@RequestMapping("/tax")
@CrossOrigin("*")
public class TaxController {
    @Autowired
    private TaxService taxService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtDecoder jwtDecoder;
 

    @PostMapping("/{companyId}/create-tax")
    public ResponseEntity<Tax> registerTaxHandler(@RequestHeader("Authorization") String token,@RequestBody  Tax tax ,@PathVariable Long companyId) throws CompanyException, UserException, RoleException, CustomerException,
            TaxException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));

        User user = userService.findUserByEmail(jwt.getSubject().toString());

          Company optcompany = companyService.findByCompanyId(companyId);
          Tax createdTax=taxService.createTax(user,tax, optcompany);
      
        return new ResponseEntity<Tax>(createdTax, HttpStatus.CREATED);
    }
    
    @PutMapping("/update/{taxId}/{companyId}")
    public ResponseEntity<Tax> updateInvoiceHandler(@RequestHeader("Authorization") String token,@RequestBody Tax req_tax,@PathVariable Long taxId,@PathVariable Long companyId) throws TaxException, CompanyException, UserException, RoleException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userRepository.findByEmail(jwt.getSubject().toString()).get();
        
        Tax  old_tax=taxService.findTaxById(taxId);
        Company rCompany=companyService.findByCompanyId(companyId);
       
        Tax tax = taxService.updateTax(user,rCompany,old_tax, req_tax);
    
        return new ResponseEntity<Tax>(tax, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{taxId}/{companyId}")
    public ResponseEntity<String> deleteTaxHandler(@RequestHeader("Authorization") String token,
           @PathVariable Long taxId,@PathVariable Long companyId) throws TaxException, CompanyException, UserException, RoleException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userRepository.findByEmail(jwt.getSubject().toString()).get();

        Tax rTax= taxService.findTaxById(taxId);
        Company rCompany = companyService.findByCompanyId(companyId);

        String message = taxService.removeTax(user, rCompany, rTax);

        return new ResponseEntity<String>(message, HttpStatus.OK);
    }
    
    @GetMapping("/{companyId}")
    public ResponseEntity<Set<Tax>> AllTaxInCompanyHandler(@RequestHeader("Authorization") String token,
            @PathVariable Long companyId) throws UserException, RoleException, CompanyException, TaxException{
        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userRepository.findByEmail(jwt.getSubject().toString()).get();
        // System.out.println("companyName "+ company.getCompanyName());
        Company rCompany = companyService.findByCompanyId(companyId);

        Set<Tax> taxes = taxService.findAllTaxInCompany(user, rCompany);

        return new ResponseEntity<Set<Tax>>(taxes, HttpStatus.OK);
    }
    
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestCreateTax {
        private Tax tax;
        private String companyName;
        
    }
    
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestUpdateTax {
        private Tax oldTax;
        private Tax newTax;
        private String companyName;
    }
    
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class RequestDeleteTax {
        private Company company;
        private Tax tax;
    }
}
