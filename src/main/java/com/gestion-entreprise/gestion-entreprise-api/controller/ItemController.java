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
import com.example.springbootoauthjwt.exceptions.ItemException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Item;
import com.example.springbootoauthjwt.model.User;
import com.example.springbootoauthjwt.service.CompanyService;
import com.example.springbootoauthjwt.service.ItemService;
import com.example.springbootoauthjwt.service.UserService;


@RestController
@RequestMapping("/item")
@CrossOrigin("*")
public class ItemController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtDecoder jwtDecoder;
 

    @PostMapping("/create-item/{companyId}")
    public ResponseEntity<Item> registerItemHandler(@RequestHeader("Authorization") String token,@RequestBody  Item item ,@PathVariable Long companyId) throws UserException, CompanyException, RoleException, CustomerException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));

        User user = userService.findUserByEmail(jwt.getSubject().toString());

          Company optcompany = companyService.findByCompanyId(companyId);
          Item created_item= itemService.createItem(user,item, optcompany);
      
        return new ResponseEntity<Item>(created_item, HttpStatus.OK);
    }
    
    @PutMapping("/update/{itemId}/{companyId}")
    public ResponseEntity<Item> updateItemHandler(@RequestHeader("Authorization") String token, @RequestBody Item newItem,@PathVariable Long itemId,@PathVariable Long companyId) throws ItemException, CompanyException, UserException, RoleException {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userRepository.findByEmail(jwt.getSubject().toString()).get();
        
        Item  rItem=itemService.findItemById(itemId);
        Company rCompany=companyService.findByCompanyId(companyId);
       
        Item item = itemService.updateItem(user,rCompany,rItem, newItem);
    
        return new ResponseEntity<Item>(item, HttpStatus.OK);
    }
    
    @DeleteMapping("/delete/{itemId}/{companyId}")
    public ResponseEntity<String> deleteItemHandler(@RequestHeader("Authorization") String token, @PathVariable Long itemId ,@PathVariable Long companyId) throws ItemException, UserException, RoleException, CompanyException {
 
        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userRepository.findByEmail(jwt.getSubject().toString()).get();

        Item rItem = itemService.findItemById(itemId);
        Company rCompany = companyService.findByCompanyId(companyId);

        String message = itemService.removeItem(user, rCompany, rItem);

        return new ResponseEntity<String>(message, HttpStatus.OK);
    }
    
    @GetMapping("/all/{companyId}")
    public ResponseEntity<List<Item>> AllItemsInCompanyHandler(@RequestHeader("Authorization") String token,
            @PathVariable Long companyId) throws UserException, RoleException, CompanyException, ItemException
            {

        Jwt jwt = jwtDecoder.decode(token.substring(7));
        User user = userRepository.findByEmail(jwt.getSubject().toString()).get();
        Company rCompany = companyService.findByCompanyId(companyId);
        System.out.println("companyName "+ rCompany.getCompanyName());

        List<Item> items = itemService.findAllItemsInCompany(user, rCompany);

        return new ResponseEntity<List<Item>>(items, HttpStatus.OK);
    }
    
    @GetMapping("/all-items")
    public ResponseEntity<List<Item>> allItems(){
        List <Item> items=itemService.findAll();
        return new ResponseEntity<List<Item>>(items, HttpStatus.OK);
    }
  }
