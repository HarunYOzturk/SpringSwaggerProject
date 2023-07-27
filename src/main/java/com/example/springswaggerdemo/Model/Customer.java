package com.example.springswaggerdemo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@Table(name = "Customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @JsonIgnore
    @JsonInclude(value = JsonInclude.Include.CUSTOM)
    private Long id;

    @Column(name = "Customer Name", length = 100)
    private String name;

    @ManyToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "customers"

    )
    @JsonIgnore
    private Set<Book> books =new HashSet<>();


}
