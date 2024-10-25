package com.example.springbootoauthjwt.service;



import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springbootoauthjwt.dao.CompanyRepository;
import com.example.springbootoauthjwt.dao.ItemRepository;
import com.example.springbootoauthjwt.dtos.ItemDTO;
import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.ItemException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.mappers.Mapper;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Item;
import com.example.springbootoauthjwt.model.User;

@Service
public class ItemServiceIMPL  implements ItemService{
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private Mapper mapper;

    @Override
    public Item createItem(User user, Item item, Company company) throws CompanyException, UserException, RoleException {
      
        Company optCompany=companyService.findByCompanyId(company.getCompanyId());
         if (companyService.getAllUsers(optCompany).contains(user)) {
          
            Item savedItem =new Item();
            savedItem.setItemName(item.getItemName());
            savedItem.setPrice(item.getPrice());
            // savedItem.setUnit(item.getUnit());
            savedItem.setDescription(item.getDescription());

            Item item1=itemRepository.save(savedItem);

            optCompany.getItems().add(mapper.fromItem(item1));
           
            companyRepository.save(optCompany);
       return item1;
        }
         throw new UserException("you are not allowed !");
        } 
     
    

    @Override
    public Item updateItem(User user,Company company,Item oldItem, 
            Item newItem) throws UserException, RoleException, CompanyException, ItemException {
    
        if (company.getItems().contains(mapper.fromItem(
                oldItem) ) && companyService.getAllUsers(company).contains(user)) {
            
            oldItem.setDescription(newItem.getDescription()!=null ? newItem.getDescription():oldItem.getDescription());
            oldItem.setItemName(newItem.getItemName() != null ? newItem.getItemName():oldItem.getItemName());
            oldItem.setPrice(newItem.getPrice()!=null? newItem.getPrice():oldItem.getPrice());
            // oldItem.setUnit(newItem.getUnit() != null ? newItem.getUnit() : oldItem.getUnit());
           
            Item  updatedItem = itemRepository.save(oldItem);
            if (updatedItem != null) {
                company.getItems().remove(mapper.fromItem(oldItem) );
                company.getItems().add(mapper.fromItem(updatedItem));
                return updatedItem;
            } else {
                throw new ItemException("Failed To update Item");
            }
        }

        throw new UserException("you are not allowed !");
    }

    
        @Override
        public String removeItem(User user,Company company, Item item) throws ItemException, UserException, RoleException, CompanyException 
                {
                    ItemDTO itemDTO=new ItemDTO();
                    itemDTO=mapper.fromItem(item);
                    System.out.println("itemDTO.getItemId "+ itemDTO.getItemId());
             if (!company.getItems().contains(itemDTO)) {
                throw new ItemException("Item Not Found");
            }
            
            if ( companyService.getAllUsers(company).contains(user)) {
                company.getItems().remove(mapper.fromItem(item));
                companyRepository.save(company);
                
                itemRepository.deleteById(item.getItemId());
    
               return "has been deleted item :";
        }
        throw new UserException("Not Allowed ");    
        }
            
    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

   


    @Override
    public List<Item> findAllItemsInCompany(User user,Company company) throws UserException, RoleException, CompanyException, ItemException {
         if ( companyService.getAllUsers(company).contains(user)) {
             List<Long> ids= company.getItems().stream().map(it->it.getItemId())
                .collect(Collectors.toList());
             Optional<List<Item>> items=itemRepository.findAllItemsByItemIds(ids);
             if (items.isEmpty()) {
                 throw new ItemException("No Items");
             }
             return items.get();
         }
       throw new UserException("Not Allowed");
    }



    @Override
    public Item findItemById(Long itemId) throws ItemException  {
     Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            return item.get();
        }
        throw new ItemException("Item Not Exist With Id: " + itemId);    }


}
