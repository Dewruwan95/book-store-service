package com.application.bookstore.controller;

import com.application.bookstore.dto.AuthorDto;
import com.application.bookstore.dto.AuthorRequestDto;
import com.application.bookstore.service.AuthorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookstore-service/v1/authors")
@SecurityRequirement(name = "basicAuth")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAll() {
        final List<AuthorDto> response = authorService.getAllAuthors();
        if (response.isEmpty()) {

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(response);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getById(@PathVariable int id) {
        final AuthorDto response = authorService.getAuthorById(id);
        return ResponseEntity.ok(response);

    }


    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorDto> create(@RequestBody AuthorDto authorDto) {
        final AuthorDto response = authorService.createAuthor(authorDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/with-books")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorDto> createWithBooks(@RequestBody AuthorRequestDto dto) {
        final AuthorDto response = authorService.createAuthorWithBooks(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorDto> update(@PathVariable int id, @RequestBody AuthorDto authorDto) {
        final AuthorDto response = authorService.updateAuthor(id, authorDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}