package com.example.springbootoauthjwt.service;

import java.util.List;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.springbootoauthjwt.dao.CompanyRepository;
import com.example.springbootoauthjwt.dao.TaxRepository;
import com.example.springbootoauthjwt.exceptions.CompanyException;
import com.example.springbootoauthjwt.exceptions.RoleException;
import com.example.springbootoauthjwt.exceptions.TaxException;
import com.example.springbootoauthjwt.exceptions.UserException;
import com.example.springbootoauthjwt.model.Company;
import com.example.springbootoauthjwt.model.Tax;
import com.example.springbootoauthjwt.model.User;

import lombok.AllArgsConstructor;
@AllArgsConstructor
@Service
public class TaxServiceIMPL  implements TaxService{
    private TaxRepository taxRepository;
    private CompanyRepository companyRepository;
    private CompanyService companyService;

    @Override
    public Tax createTax(User user, Tax tax, Company company) throws CompanyException, UserException, RoleException, TaxException{
      
        Company optCompany=companyService.findByCompanyId(company.getCompanyId());
         if (companyService.getAllUsers(optCompany).contains(user)) {
          if(company.getTaxes().stream().map(Tax::getName).collect(Collectors.toList()).contains(tax.getName())){
            throw new TaxException("tax Name Already exists");
          }else{
              Tax tax1 =new Tax();
             
              tax1.setName(tax.getName());
              tax1.setDescription(tax.getDescription());
              tax1.setPercent(tax.getPercent());
             
              Tax savedTax=taxRepository.save(tax1);
  
              optCompany.getTaxes().add(tax1);
              companyRepository.save(optCompany);
             return savedTax;
          }
        }
         throw new UserException("you are not allowed !");
        } 
     
    

    @Override
    public Tax updateTax(User user,Company company,Tax oldTax, 
            Tax newTax) throws UserException, TaxException, RoleException, CompanyException  {
    
        if (company.getTaxes().contains(oldTax) && companyService.getAllUsers(company).contains(user)) {
            
            oldTax.setDescription(newTax.getDescription()!=null ? newTax.getDescription():oldTax.getDescription());
            oldTax.setPercent(newTax.getPercent() != null ? newTax.getPercent():oldTax.getPercent());
            oldTax.setName(newTax.getName()!=null? newTax.getName():oldTax.getName());
           
            Tax  updatedTax = taxRepository.save(oldTax);
            if (updatedTax != null) {
                return updatedTax;
            } else {
                throw new TaxException("Failed To update Tax");
            }
        }

        throw new UserException("you are not allowed !");
    }

    
        @Override
        public String removeTax(User user,Company company, Tax tax) 
                throws CompanyException, UserException, RoleException, TaxException  
                {
             if (!company.getTaxes().contains(tax)) {
                throw new TaxException("Tax Not Found");
            }
            
            if ( companyService.getAllUsers(company).contains(user)) {
                company.getTaxes().remove(tax);
                companyRepository.save(company);
                taxRepository.deleteById(tax.getTaxId());
    
               return "has been deleted tax";
        }
        throw new UserException("Not Allowed ");    
        }
            
    @Override
    public List<Tax> findAll() {
        return taxRepository.findAll();
    }

   


    @Override
    public Set<Tax> findAllTaxInCompany(User user,Company company) throws UserException, RoleException, CompanyException, TaxException  {
         if ( companyService.getAllUsers(company).contains(user)) {
             Set<Tax> taxes=company.getTaxes();

             if (taxes.isEmpty()) {
                 throw new TaxException("No Taxes ");
             }
             return taxes;
         }
       throw new UserException("You are Not Allowed");
    }



    @Override
    public Tax findTaxById(Long taxId) throws TaxException {
     Optional<Tax> tax = taxRepository.findById(taxId);
        if (tax.isPresent()) {
            return tax.get();
        }
        throw new TaxException("Tax Not Exist With Id: " + taxId);    }


}
