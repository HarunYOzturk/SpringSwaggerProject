package com.example.springswaggerdemo.Controller;


import com.example.springswaggerdemo.Model.Customer;
import com.example.springswaggerdemo.Repository.AuthorRepository;
import com.example.springswaggerdemo.Repository.BookRepository;
import com.example.springswaggerdemo.Repository.CustomerRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Customer")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping("/getAllCustomers")
    public ResponseEntity<List<Customer>> getAllCustomers(){
        try {
            List<Customer> customerList = new ArrayList<>(customerRepository.findAll());
            if(customerList.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(customerList, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getCustomerById/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id){
        Optional<Customer> customerData = customerRepository.findById(id);

        return customerData.map(customer -> new ResponseEntity<>(customer, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PostMapping("/addCustomer")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer){
        Customer customerObj = customerRepository.save(customer);
        return new  ResponseEntity<>(customerObj, HttpStatus.OK);
    }


    @PutMapping("/updateCustomerById/{id}")
    public ResponseEntity<Customer> updateCustomerById(@PathVariable Long id, @RequestBody Customer newCustomerData){
        Optional<Customer> oldCustomerData = customerRepository.findById(id);

        if(oldCustomerData.isPresent()){
            Customer updatedCustomerData =oldCustomerData.get();
            if(newCustomerData.getName() != null) updatedCustomerData.setName(newCustomerData.getName());
            if(!newCustomerData.getBooks().isEmpty()) updatedCustomerData.setBooks(newCustomerData.getBooks());

            Customer customer = customerRepository.save(updatedCustomerData);
            return new ResponseEntity<>(customer,HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteCustomerById/{id}")
    public ResponseEntity<HttpStatus> deleteCustomerById(@PathVariable Long id){
        Optional<Customer> toBeDeletedCustomerData = customerRepository.findById(id);

        if(toBeDeletedCustomerData.isPresent()){
            customerRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
