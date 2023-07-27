package com.example.springswaggerdemo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "Authors")
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Author Name", length = 100)
    private String name;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            targetEntity = Book.class
    )
    @JsonIgnore
    private Set<Book> books =new HashSet<>();


    public void addBook(Book book){
        this.books.add(book);
    }
    public void removeBook(Book book){
        this.books.remove(book);
    }

}
