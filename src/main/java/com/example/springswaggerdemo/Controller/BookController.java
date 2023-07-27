package com.example.springswaggerdemo.Controller;

import com.example.springswaggerdemo.Model.Book;
import com.example.springswaggerdemo.Repository.BookRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "Book")
public class BookController {

    @Autowired
    private  BookRepository bookRepository;



    @GetMapping("/getAllBooks")
    public ResponseEntity<List<Book>> getAllBooks(){
        try {
            List<Book> bookList = new ArrayList<>(bookRepository.findAll());

            if(bookList.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            return new ResponseEntity<>(bookList, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getBookById/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id){
        Optional<Book> bookData = bookRepository.findById(id);

        return bookData.map(book -> new ResponseEntity<>(book, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PostMapping("/addBook")
    public ResponseEntity<Book> addBook(@RequestBody Book book){
    Book bookObj = bookRepository.save(book);
    return new  ResponseEntity<>(bookObj, HttpStatus.OK);
    }

    @PutMapping("/updateBookById/{id}")
    public ResponseEntity<Book> updateBookById(@PathVariable Long id, @RequestBody Book newBookData){
        Optional<Book> oldBookData = bookRepository.findById(id);
//        Optional<Author> author= authorRepository.findById(newBookData.getAuthor().getId());


        if(oldBookData.isPresent()){
            Book updatedBookData =oldBookData.get();
            if(!newBookData.getTitle().isEmpty()) updatedBookData.setTitle(newBookData.getTitle());
//            if(!newBookData.getCustomers().isEmpty()) updatedBookData.setCustomers(newBookData.getCustomers());
//            if(newBookData.getAuthor() != null) updatedBookData.setAuthor(author.get());

            Book book = bookRepository.save(updatedBookData);
            return new ResponseEntity<>(book,HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteBookById/{id}")
    public ResponseEntity<HttpStatus> deleteBookById(@PathVariable Long id){
        Optional<Book> toBeDeletedBookData = bookRepository.findById(id);

        if(toBeDeletedBookData.isPresent()){
            bookRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
