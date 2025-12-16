package com.application.bookstore.controller;

import com.application.bookstore.dto.BookDto;
import com.application.bookstore.dto.BookWithNewAuthorDto;
import com.application.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-store-service/v1/books")
@SecurityRequirement(name = "basicAuth")
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.getAll();
    }

    @GetMapping("/{id}")
    public BookDto getById(@PathVariable int id) {
        return bookService.getById(id);
    }

    //create book without author
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public BookDto save(@RequestBody BookDto book) {
        return bookService.createBook(book);
    }

    @PostMapping("/with-author")
    public BookDto createBookWithAuthor(@RequestBody BookWithNewAuthorDto book) {

        return bookService.createBookWithNewAuthor(book);
    }

    //attach existing author to book
    @PutMapping("/{bookId}/author/{authorId}")
    public BookDto attachAuthor(@PathVariable int bookId, @PathVariable int authorId) {
        return bookService.attachAuthor(bookId, authorId);

    }


    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") int id) {
        bookService.deleteOneById(id);
    }


}