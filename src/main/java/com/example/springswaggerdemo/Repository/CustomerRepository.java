package com.example.springswaggerdemo.Repository;

import com.example.springswaggerdemo.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
