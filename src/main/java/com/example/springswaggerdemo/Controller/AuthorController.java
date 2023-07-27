package com.example.springswaggerdemo.Controller;

import com.example.springswaggerdemo.Model.Author;
import com.example.springswaggerdemo.Model.Book;
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
@Tag(name = "Author")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BookRepository bookRepository;


    @GetMapping("/getAllAuthors")
    public ResponseEntity<List<Author>> getAllAuthors(){
        try {
            List<Author> authorList = new ArrayList<>(authorRepository.findAll());
            if(authorList.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(authorList, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAuthorById/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id){
        Optional<Author> authorData = authorRepository.findById(id);

        return authorData.map(author -> new ResponseEntity<>(author, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PostMapping("/addAuthor")
    public ResponseEntity<Author> addAuthor(@RequestBody Author author){
        Author authorObj = authorRepository.save(author);
        return new  ResponseEntity<>(authorObj, HttpStatus.OK);
    }

    @PutMapping("/updateAuthorById/{id}")
    public ResponseEntity<Author> updateAuthorById(@PathVariable Long id, @RequestBody Author newAuthorData){
        Optional<Author> oldAuthorData = authorRepository.findById(id);

        if(oldAuthorData.isPresent()){
            Author updatedAuthorData =oldAuthorData.get();
            if(!newAuthorData.getName().isEmpty()) updatedAuthorData.setName(newAuthorData.getName());
//            if(!newAuthorData.getBooks().isEmpty()) updatedAuthorData.setBooks(newAuthorData.getBooks());

            Author author = authorRepository.save(updatedAuthorData);
            return new ResponseEntity<>(author,HttpStatus.OK);
        }
        //instead not found throw exception
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    @DeleteMapping("/deleteAuthorById/{id}")
    public ResponseEntity<HttpStatus> deleteAuthorById(@PathVariable Long id){
        Optional<Author> toBeDeletedAuthorData = authorRepository.findById(id);

        if(toBeDeletedAuthorData.isPresent()){
            authorRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
