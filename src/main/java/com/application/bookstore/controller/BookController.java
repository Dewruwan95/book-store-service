package com.application.bookstore.controller;

import com.application.bookstore.dto.BookDto;
import com.application.bookstore.dto.BookWithNewAuthorDto;
import com.application.bookstore.service.BookService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<BookDto>> getAllBooks(){
        final List<BookDto> response = bookService.getAll();
        if (response.isEmpty()) {

            return ResponseEntity.noContent().build(); // Better than returning null
        } else {
            return ResponseEntity.ok(response);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getById(@PathVariable int id){
        final BookDto response = bookService.getById(id);

        return ResponseEntity.ok(response);

    }

    //create book without author

    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookDto> save(@RequestBody BookDto book){
        final BookDto response = bookService.createBook(book);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/with-author")
    public ResponseEntity<BookDto> createBookWithAuthor(@RequestBody BookWithNewAuthorDto book){

        final BookDto response = bookService.createBookWithNewAuthor(book);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //attach existing author to book
    @PutMapping("/{bookId}/author/{authorId}")
    public ResponseEntity<BookDto> attachAuthor(@PathVariable int bookId, @PathVariable int authorId){
        final BookDto response = bookService.attachAuthor(bookId, authorId);

        return ResponseEntity.ok(response);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") int id){
        bookService.deleteOneById(id);
        return ResponseEntity.noContent().build();
    }
}