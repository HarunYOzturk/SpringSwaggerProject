package com.example.springswaggerdemo.Controller.ModifiedController;


import com.example.springswaggerdemo.Model.Book;
import com.example.springswaggerdemo.Model.Customer;
import com.example.springswaggerdemo.Repository.BookRepository;
import com.example.springswaggerdemo.Repository.CustomerRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;
import java.util.Set;

@Tag(name = "Modified operations For Customer")
@RestController
@RequestMapping("/api/customers")
public class ModifiedOperationsCustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookRepository bookRepository;

    @PostMapping("/{customerId}/buyBook")
    public ResponseEntity<Object> buyBook(@PathVariable Long customerId, @RequestParam Long bookId) {
        String msg = "Could not add the book to the customer";
        try {
            Customer customer = getCustomerById(customerId);
            Book book = getBookById(bookId);

            customer.getBooks().add(book);
            book.getCustomers().add(customer);

            customerRepository.save(customer);
            bookRepository.save(book);

            return new ResponseEntity<>("Book added to the customer successfully.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{customerId}/books")
    public ResponseEntity<Object> getBooksForCustomer(@PathVariable Long customerId) {
        String msg = "Could not retrieve books for the customer";
        try {

            Customer customer = getCustomerById(customerId);
            Set<Book> books = customer.getBooks();

            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{customerId}/returnBook")
    public ResponseEntity<Object> returnBook(@PathVariable Long customerId, @RequestParam Long bookId) {
        String msg = "Could not return the book";
        try {
            Customer customer = getCustomerById(customerId);
            Book book = getBookById(bookId);

            if (customer.getBooks().contains(book)) {
                customer.getBooks().remove(book);
                book.getCustomers().remove(customer);

                customerRepository.save(customer);
                bookRepository.save(book);

                return new ResponseEntity<>("Book returned successfully.", HttpStatus.OK);
            } else {
                msg = "The customer doesn't have this book";
                throw new RuntimeException();
            }
        } catch (Exception e) {
            return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Customer getCustomerById(Long customerId) throws Exception {
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isEmpty()) {
            throw new Exception("Not valid Customer ID");
        }
        return customerOptional.get();
    }
    private Book getBookById(Long bookId) throws Exception {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new Exception("Not valid Book ID");
        }
        return bookOptional.get();
    }
}
