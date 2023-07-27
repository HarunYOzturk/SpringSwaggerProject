package com.example.springswaggerdemo.Controller;

import com.example.springswaggerdemo.Model.Book;
import com.example.springswaggerdemo.Repository.AuthorRepository;
import com.example.springswaggerdemo.Repository.BookRepository;
import com.example.springswaggerdemo.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ModifiedOperationsController {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BookRepository bookRepository;



    @PutMapping("/writeBook/{id}")
    public ResponseEntity<Object> writeBook(@PathVariable Long id, @RequestBody Book newBook){
        try{
            newBook.setAuthor(authorRepository.findById(id).get());
            Book bookObj = bookRepository.save(newBook);
            return new ResponseEntity<>(bookObj, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Could not add the book",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
