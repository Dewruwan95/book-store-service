package com.application.bookstore.repository;

import com.application.bookstore.model.Author;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import javax.sql.DataSource;
import java.util.List;


@DataJpaTest
@AutoConfigureMockMvc(addFilters = false)
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        Author author1 = new Author();
        author1.setFirstName("George");
        author1.setLastName("Orwell");
        author1.setEmail("george.orwell@email.com");
        author1.setNationality("British");

        authorRepository.save(author1);

        Author author2 = new Author();
        author2.setFirstName("J.K.");
        author2.setLastName("Rowling");
        author2.setEmail("jk.rowling@email.com");
        author2.setNationality("British");

        authorRepository.save(author2);
    }

    @Test
    void context_loads() {
        Assertions.assertNotNull(authorRepository);
        Assertions.assertNotNull(dataSource);
    }

    @Test
    void should_return_all_authors() {
        List<Author> result = authorRepository.findAll();
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(2, result.size());
    }

}