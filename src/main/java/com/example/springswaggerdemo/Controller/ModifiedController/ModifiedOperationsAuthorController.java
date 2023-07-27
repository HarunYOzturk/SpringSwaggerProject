package com.example.springswaggerdemo.Controller.ModifiedController;

import com.example.springswaggerdemo.Model.Author;
import com.example.springswaggerdemo.Model.Book;
import com.example.springswaggerdemo.Repository.AuthorRepository;
import com.example.springswaggerdemo.Repository.BookRepository;
//import com.example.springswaggerdemo.Repository.CustomerRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@Tag(name = "Modified operations For Author")
@RestController
public class ModifiedOperationsAuthorController {

    @Autowired
    private AuthorRepository authorRepository;
//    @Autowired
//    private CustomerRepository customerRepository;
    @Autowired
    private BookRepository bookRepository;



    @PutMapping("/writeBook/{id}")
    public ResponseEntity<Object> writeBook(@PathVariable Long id, @RequestBody Book newBook){
        String msg ="Could not add the book";
        try{
            if(authorRepository.findById(id).isEmpty())
            {
                msg = "Not valid Author ID";
                throw new Exception();
            }
            Author authorObj = authorRepository.findById(id).get();
            authorObj.addBook(newBook);
            newBook.setAuthor(authorObj);
            Book bookObj = bookRepository.save(newBook);
            authorRepository.save(authorObj);
            return new ResponseEntity<>(bookObj, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(msg,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/assignTheAuthor/{authorId}")
    public ResponseEntity<Object> assignTheAuthor(@PathVariable Long authorId, @RequestParam Long bookId){
        String msg ="Could not assigned the author";
        try{
            Optional<Author> author =authorRepository.findById(authorId);
            Optional<Book> book =bookRepository.findById(bookId);

            if(author.isEmpty())
            {
                msg = "Not valid Author ID";
                throw new Exception();
            }
            Author authorObj = author.get();
            if(book.isEmpty())
            {
                msg = "Not valid Book ID";
                throw new Exception();
            }
            Book bookObj = book.get();

            if(Objects.isNull(bookObj.getAuthor())) {
                authorObj.addBook(bookObj);
                bookObj.setAuthor(authorObj);
                authorRepository.save(authorObj);
                bookRepository.save(bookObj);
                return new ResponseEntity<>(bookObj, HttpStatus.OK);
            }
            msg = "The Book has an author already";
            throw new RuntimeException();
        }catch (Exception e){
            return new ResponseEntity<>(msg,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/changeTheAuthor/{authorId}")
    public ResponseEntity<Object> changeTheAuthor(@PathVariable Long authorId, @RequestParam Long bookId){
        String msg ="Could not assigned the author";
        try{
            Optional<Author> author =authorRepository.findById(authorId);
            Optional<Book> book =bookRepository.findById(bookId);
            if(author.isEmpty())
            {
                msg = "Not valid Author ID ";
                throw new Exception();
            }
            Author authorObj = author.get();
            if(book.isEmpty())
            {
                msg = "Not valid Book ID";
                throw new Exception();
            }
            Book bookObj = book.get();

            Author otherAuthorObj =bookObj.getAuthor();
            if(otherAuthorObj == null ) {
                msg ="The Book already doesn't have an author try to assign author instead of changing.";
                throw new RuntimeException();
            }

            otherAuthorObj.removeBook(bookObj);
            bookObj.setAuthor(authorObj);
            bookRepository.save(bookObj);

            authorObj.addBook(bookObj);
            authorRepository.save(otherAuthorObj);
            authorRepository.save(authorObj);


            return new ResponseEntity<>(bookObj, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(msg,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
