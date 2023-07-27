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
@Entity
@Table(name = "Books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Book Title", length = 100)
    private String title;


    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            targetEntity = Customer.class
    )
    @JsonIgnore
    private Set<Customer> customers =new HashSet<>();

//    @JoinColumn(name = "Books Author", length = 100)
    @ManyToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY

    )
    @JsonIgnore
    private Author author;

}
