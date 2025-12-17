package com.application.bookstore.service;
import com.application.bookstore.dto.BookDto;
import com.application.bookstore.dto.BookRequestDto;
import com.application.bookstore.model.Book;
import com.application.bookstore.repository.AuthorRepository;
import com.application.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@AutoConfigureMockMvc(addFilters = false)
class BookServiceTest {
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private BookService bookService;

    @BeforeEach
    void beforeEachTest() {
        bookRepository = Mockito.mock(BookRepository.class);
        bookService = new BookService(bookRepository, authorRepository);
    }

    @Test
    void context_loads() {
        Assertions.assertNotNull(bookService);
        Assertions.assertNotNull(bookRepository);
    }

    @Test
    void should_return_all_books() {
        Mockito.when(bookRepository.findAll()).thenReturn(dummyBooks());

        List<BookDto> result = bookService.getAll();

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(2, result.size());

        Assertions.assertEquals("1984", result.getFirst().getTitle());
        Assertions.assertEquals(12.99, result.getFirst().getPrice());
        Assertions.assertEquals("Dystopian", result.getFirst().getGenre());
        Assertions.assertEquals(50, result.getFirst().getStock());
    }

    @Test
    void should_create_new_book() {
        BookRequestDto request = new BookRequestDto();
        request.setTitle("1984");
        request.setPrice(12.99);
        request.setGenre("Dystopian");
        request.setStock(50);

        Book savedBook = new Book();
        savedBook.setId(1);
        savedBook.setTitle("1984");
        savedBook.setPrice(12.99);
        savedBook.setGenre("Dystopian");
        savedBook.setStock(50);

        Mockito.when(bookRepository.save(Mockito.any(Book.class))).thenReturn(savedBook);

        BookDto result = bookService.create(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("1984", result.getTitle());
        Assertions.assertEquals(12.99, result.getPrice());
        Assertions.assertEquals("Dystopian", result.getGenre());
        Assertions.assertEquals(50, result.getStock());
    }

    private static List<Book> dummyBooks() {
        Book book1 = new Book();
        book1.setId(1);
        book1.setTitle("1984");
        book1.setPrice(12.99);
        book1.setGenre("Dystopian");
        book1.setStock(50);

        Book book2 = new Book();
        book2.setId(2);
        book2.setTitle("Harry Potter");
        book2.setPrice(15.99);
        book2.setGenre("Fantasy");
        book2.setStock(100);

        return List.of(book1, book2);
    }
}