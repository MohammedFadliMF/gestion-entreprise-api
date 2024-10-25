package com.example.springbootoauthjwt.mappers;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springbootoauthjwt.dtos.ChatRoomDTO;
import com.example.springbootoauthjwt.dtos.CompanyDTO;
import com.example.springbootoauthjwt.dtos.CustomerDTO;
import com.example.springbootoauthjwt.dtos.ItemDTO;
import com.example.springbootoauthjwt.dtos.MessageDTO;
import com.example.springbootoauthjwt.dtos.RoleDTO;
import com.example.springbootoauthjwt.dtos.UserDTO;
import com.example.springbootoauthjwt.model.ChatRoom;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Customer;
import com.example.springbootoauthjwt.model.Item;
import com.example.springbootoauthjwt.model.Message;
import com.example.springbootoauthjwt.model.Role;
import com.example.springbootoauthjwt.model.User;

@Service
public class Mapper {
    

    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO=new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
        return  customerDTO;
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer=new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return  customer;
    }

    public UserDTO fromUser(User user){
        UserDTO userDTO=new UserDTO();
        BeanUtils.copyProperties(user,userDTO);
        return userDTO;
    }

    public User fromUserDTO(UserDTO userDTO){
        User user=new User();
        BeanUtils.copyProperties(userDTO,user);
        
        return user;
    }

    public RoleDTO fromRole(Role role){
        RoleDTO roleDTO=new RoleDTO();
        BeanUtils.copyProperties(role,roleDTO);
        roleDTO.setCompanyDTO(fromCompany(role.getCompany()));
        return roleDTO;
    }

    public Role formRoleDTO(RoleDTO roleDTO){
        Role role=new Role();
        BeanUtils.copyProperties(roleDTO,role);
        role.setCompany(formCompanyDTO(roleDTO.getCompanyDTO()));
        return role;
    }
    
    public ItemDTO fromItem(Item item) {
        ItemDTO itemDTO = new ItemDTO();
        BeanUtils.copyProperties(item, itemDTO);
        return itemDTO;
    }

    public Item formItemDTO(ItemDTO itemDTO) {
        Item item = new Item();
        BeanUtils.copyProperties(itemDTO, item);
        // System.out.println("item Unit :"+item.getUnit());
        System.out.println("item Desc :" + item.getDescription());

        return item;
    }
    
    public CompanyDTO fromCompany(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();
        BeanUtils.copyProperties(company, companyDTO);
        return companyDTO;
    }

    public Company formCompanyDTO(CompanyDTO companyDTO) {
        Company company = new Company();
        BeanUtils.copyProperties(companyDTO, company);

        return company;
    }
    
    public ChatRoomDTO fromChatRoom(ChatRoom chatRoom) {
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        BeanUtils.copyProperties(chatRoom, chatRoomDTO);
        return chatRoomDTO;
    }

    public ChatRoom formChatRoomDTO(ChatRoomDTO chatRoomDTO) {
        ChatRoom chatRoom = new ChatRoom();
        BeanUtils.copyProperties(chatRoomDTO, chatRoom);

        return chatRoom;
    }
    
    public MessageDTO fromMessage(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        BeanUtils.copyProperties(message, messageDTO);
        return messageDTO;
    }

    public Message forMessageDTO(MessageDTO messageDTO) {
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO, message);

        return message;
    }

    // public Invoice fromInvoiceDTO(InvoiceDTO accountOperation){
    //     AccountOperationDTO accountOperationDTO=new AccountOperationDTO();
    //     BeanUtils.copyProperties(accountOperation,accountOperationDTO);
    //     return accountOperationDTO;
    // }

}
