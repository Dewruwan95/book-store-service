package com.application.bookstore.controller;

import com.application.bookstore.dto.*;
import com.application.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-store-service/v1/books")
@SecurityRequirement(name = "basicAuth")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    //------------------- Get All Books ------------------------
    @GetMapping
    public ResponseEntity<List<BookDto>> getAll() {
        final List<BookDto> response = bookService.getAll();
        if (response.isEmpty()) {

            return ResponseEntity.noContent().build(); // Better than returning null
        } else {
            return ResponseEntity.ok(response);
        }

    }


    //------------------- Get Single Book By Id ------------------------
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getById(@PathVariable int id) {
        final BookDto response = bookService.getById(id);

        return ResponseEntity.ok(response);

    }

    //------------------- Create New Book ------------------------
    @PostMapping
     @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> save(@RequestBody BookRequestDto bookRequestDto) {
        final BookDto response = bookService.create(bookRequestDto);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    //------------------- Create New Book With New Author ------------------------
    @PostMapping("/with-author")
    public ResponseEntity<BookDto> createBookWithAuthor(@RequestBody BookWithNewAuthorDto book) {

        final BookDto response = bookService.createBookWithNewAuthor(book);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    //------------------- Update Book With Id ------------------------
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> update(@PathVariable int id, @RequestBody BookRequestDto bookRequestDto) {
        BookDto response = bookService.update(id, bookRequestDto);
        return ResponseEntity.ok(response);
    }


    //------------------- Attach Existing Author To Book ------------------------
    @PutMapping("/{bookId}/author/{authorId}")
    public ResponseEntity<BookDto> attachAuthor(@PathVariable int bookId, @PathVariable int authorId) {
        final BookDto response = bookService.attachAuthor(bookId, authorId);

        return ResponseEntity.ok(response);

    }

    //------------------- Delete Author ------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") int id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}