package com.application.bookstore.repository;

import com.application.bookstore.model.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureMockMvc(addFilters = false)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        Book book1 = new Book();
        book1.setTitle("1984");
        book1.setPrice(12.99);
        book1.setGenre("Dystopian");
        book1.setStock(50);

        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Harry Potter");
        book2.setPrice(15.99);
        book2.setGenre("Fantasy");
        book2.setStock(100);

        bookRepository.save(book2);
    }

    @Test
    void context_loads() {
        Assertions.assertNotNull(bookRepository);
        Assertions.assertNotNull(dataSource);
    }

    @Test
    void should_return_all_books() {
        List<Book> result = bookRepository.findAll();
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(2, result.size());
    }

}