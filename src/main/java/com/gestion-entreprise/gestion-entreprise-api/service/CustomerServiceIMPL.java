package com.example.springbootoauthjwt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.springbootoauthjwt.dao.CompanyRepository;
import com.example.springbootoauthjwt.dao.CustomerRepository;
import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.CustomerException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Customer;
import com.example.springbootoauthjwt.model.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CustomerServiceIMPL implements CustomerService {
    private CustomerRepository customerRepository;
    private CompanyRepository companyRepository;
    private CompanyService companyService;

    @Override
    public Customer createCustomer(User user, Customer customer, Company company)
            throws CompanyException, CustomerException, UserException, RoleException {
        // Optional<Customer> optcustomer = customerRepository.findByEmail(customer.getEmail());

        if (companyService.getAllUsers(company).contains(user)) {

            Customer savedCustomer = new Customer();
            savedCustomer.setBillingAddress(customer.getBillingAddress());
            savedCustomer.setCustomerName(customer.getCustomerName());
            savedCustomer.setEmail(customer.getEmail());
            savedCustomer.setPhone(customer.getPhone());
            savedCustomer.setPrimaryContactname(customer.getPrimaryContactname());
            savedCustomer.setShippingAddress(customer.getShippingAddress());
            savedCustomer = customerRepository.save(savedCustomer);

            company.getCustomers().add(savedCustomer);
            companyRepository.save(company);
            return savedCustomer;
        }
        throw new CustomerException("Ce n'est pas autorisé, vous n'êtes pas membre de l'entreprise");
    }

    @Override
    public Customer findByCustomerId(Long customerId) throws CustomerException {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            return customer.get();
        }
        throw new CustomerException("Customer Not found ");
    }

    @Override
    public Customer updateCustomer(User user, Company company, Customer oldCustomer, Customer newCustomer)
            throws CustomerException, UserException, CompanyException, RoleException {
        // System.out.println("oldCustomer "+oldCustomer.getEmail() );
        // System.out.println("newCustomer " + newCustomer.getEmail());

        if (!oldCustomer.getEmail().equals(newCustomer.getEmail())) {
            throw new CustomerException("Not The Same Customer");
        }
        // System.out.println("company.getCustomers() " + company.getCustomers());

        if (company.getCustomers().contains(oldCustomer) && companyService.getAllUsers(company).contains(user)) {

            oldCustomer.setBillingAddress(newCustomer.getBillingAddress() != null ? newCustomer.getBillingAddress()
                    : oldCustomer.getBillingAddress());
            oldCustomer.setCustomerName(newCustomer.getCustomerName() != null ? newCustomer.getCustomerName()
                    : oldCustomer.getCustomerName());
            oldCustomer.setEmail(newCustomer.getEmail() != null ? newCustomer.getEmail() : oldCustomer.getEmail());
            oldCustomer.setPhone(newCustomer.getPhone() != null ? newCustomer.getPhone() : oldCustomer.getPhone());
            oldCustomer.setPrimaryContactname(
                    newCustomer.getPrimaryContactname() != null ? newCustomer.getPrimaryContactname()
                            : oldCustomer.getPrimaryContactname());
            oldCustomer.setShippingAddress(newCustomer.getShippingAddress() != null ? newCustomer.getShippingAddress()
                    : oldCustomer.getShippingAddress());

            Customer updatedCustomer = customerRepository.save(oldCustomer);
            if (updatedCustomer == null) {
                throw new CustomerException("Failed To update Customer");
            }
            return updatedCustomer;
        }

        throw new CustomerException("Failed To update Customer");
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer findCustomerByEmail(String email) throws CustomerException {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            return customer.get();
        }
        throw new CustomerException("Customer Not Exist With Id: " + email);
    }

    @Override
    public List<Customer> findAllCustomersInCompany(User user, Company company)
            throws CompanyException, CustomerException, UserException, RoleException {
        if (companyService.getAllUsers(company).contains(user)) {
            List<Customer> customers = company.getCustomers();

            if (customers.isEmpty()) {
                throw new CustomerException("Vide");
            }
            return customers;
        }
        throw new UserException("Not Allowed");
    }

    @Override
    public String removeCustomer(User user, Company company, Customer customer)
            throws CustomerException, UserException, CompanyException, RoleException {
        if (!company.getCustomers().contains(customer)) {
            throw new CustomerException("Customer Not Found");
        }

        if (companyService.getAllUsers(company).contains(user)) {
            company.getCustomers().remove(customer);
            companyRepository.save(company);

            customerRepository.deleteById(customer.getCustomerId());

            return "has been deleted customer :" + customer.getCustomerId();
        }
        throw new UserException("you are not allowed ");
    }
}
