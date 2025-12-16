package com.application.bookstore.controller;

import com.application.bookstore.dto.AuthorDto;
import com.application.bookstore.dto.AuthorRequestDto;
import com.application.bookstore.dto.AuthorWithBookRequestDto;
import com.application.bookstore.service.AuthorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-store-service/v1/authors")
@SecurityRequirement(name = "basicAuth")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }


    //------------------- Get All Authors ------------------------
        @GetMapping
    public ResponseEntity<List<AuthorDto>> getAll() {
        final List<AuthorDto> response = authorService.getAll();
        if (response.isEmpty()) {

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(response);
        }

    }

    //------------------- Get Single Author By Id ------------------------
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getById(@PathVariable int id) {
        final AuthorDto response = authorService.getById(id);
        return ResponseEntity.ok(response);

    }

    //------------------- Create New Author ------------------------
    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorDto> create(@RequestBody AuthorRequestDto authorRequestDto) {
        final AuthorDto response = authorService.create(authorRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //------------------- Create New Author With Books ------------------------
    @PostMapping("/with-books")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorDto> createWithBooks(@RequestBody AuthorWithBookRequestDto authorWithBookRequestDto) {
        final AuthorDto response = authorService.createWithBooks(authorWithBookRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //------------------- Update Author By Id ------------------------
    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorDto> update(@PathVariable int id, @RequestBody AuthorDto authorDto) {
        final AuthorDto response = authorService.update(id, authorDto);
        return ResponseEntity.ok(response);
    }

    //------------------- Delete Author By Id ------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}