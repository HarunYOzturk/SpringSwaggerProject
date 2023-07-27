package com.example.springswaggerdemo.Controller.ModifiedController;

import com.example.springswaggerdemo.Model.Author;
import com.example.springswaggerdemo.Model.Book;
import com.example.springswaggerdemo.Model.Customer;
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


//    @Autowired
//    private CustomerRepository customerRepository;


    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public ModifiedOperationsAuthorController(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }



    @PutMapping("/writeBook/{authorId}")
    public ResponseEntity<Object> writeBook(@PathVariable Long authorId, @RequestBody Book newBook){
        String msg ="Could not add the book";
        try{
            Author author = getAuthorById(authorId);
            newBook.setAuthor(author);
            author.addBook(newBook);
            authorRepository.save(author);
            return new ResponseEntity<>(newBook, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(msg,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/assignTheAuthor/{authorId}")
    public ResponseEntity<Object> assignTheAuthor(@PathVariable Long authorId, @RequestParam Long bookId){
        String msg ="Could not assigned the author";
        try{
            Author author = getAuthorById(authorId);
            Book book = getBookById(bookId);

            if(Objects.isNull(book.getAuthor())) {
                author.addBook(book);
                book.setAuthor(author);
                authorRepository.save(author);
                bookRepository.save(book);
                return new ResponseEntity<>(book, HttpStatus.OK);
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

            Author author = getAuthorById(authorId);
            Book book = getBookById(bookId);

            Author otherAuthor =book.getAuthor();
            if(otherAuthor == null ) {
                msg ="The Book doesn't have an author try to assign author instead of changing.";
                throw new RuntimeException();
            }

            otherAuthor.removeBook(book);
            book.setAuthor(author);
            bookRepository.save(book);

            author.addBook(book);
            authorRepository.save(otherAuthor);
            authorRepository.save(author);


            return new ResponseEntity<>(book, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(msg,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Book getBookById(Long bookId) throws Exception {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new Exception("Not valid Author ID");
        }
        return bookOptional.get();
    }
    private Author getAuthorById(Long customerId) throws Exception {
        Optional<Author> authorOptional = authorRepository.findById(customerId);
        if (authorOptional.isEmpty()) {
            throw new Exception("Not valid Customer ID");
        }
        return authorOptional.get();
    }
}
