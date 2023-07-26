package com.example.springswaggerdemo.Repository;

import com.example.springswaggerdemo.Model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository  extends JpaRepository<Author,Long> {
}
