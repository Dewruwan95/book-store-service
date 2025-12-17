package com.application.bookstore.service;

import com.application.bookstore.dto.AuthorDto;
import com.application.bookstore.dto.AuthorRequestDto;
import com.application.bookstore.model.Author;
import com.application.bookstore.repository.AuthorRepository;
import com.application.bookstore.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;

@AutoConfigureMockMvc(addFilters = false)
class AuthorServiceTest {
    private AuthorRepository authorRepository;
    private BookRepository bookRepository;
    private AuthorService authorService;

    @BeforeEach
    void beforeEachTest() {
        authorRepository = Mockito.mock(AuthorRepository.class);
        authorService = new AuthorService(authorRepository, bookRepository);
    }

    @Test
    void context_loads() {
        Assertions.assertNotNull(authorService);
        Assertions.assertNotNull(authorRepository);
    }

    @Test
    void should_return_all_authors() {
        Mockito.when(authorRepository.findAll()).thenReturn(dummyAuthors());

        List<AuthorDto> result = authorService.getAll();

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(2, result.size());

        Assertions.assertEquals("George", result.getFirst().getFirstName());
        Assertions.assertEquals("Orwell", result.getFirst().getLastName());
        Assertions.assertEquals("george.orwell@email.com", result.getFirst().getEmail());
        Assertions.assertEquals("British", result.getFirst().getNationality());
    }

    @Test
    void should_create_new_author() {
        AuthorRequestDto request = new AuthorRequestDto();
        request.setFirstName("George");
        request.setLastName("Orwell");
        request.setEmail("george.orwell@email.com");
        request.setNationality("British");

        Author savedAuthor = new Author();
        savedAuthor.setId(1);
        savedAuthor.setFirstName("George");
        savedAuthor.setLastName("Orwell");
        savedAuthor.setEmail("george.orwell@email.com");
        savedAuthor.setNationality("British");

        Mockito.when(authorRepository.save(Mockito.any(Author.class))).thenReturn(savedAuthor);

        AuthorDto result = authorService.create(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getId());
        Assertions.assertEquals("George", result.getFirstName());
        Assertions.assertEquals("Orwell", result.getLastName());
        Assertions.assertEquals("george.orwell@email.com", result.getEmail());
        Assertions.assertEquals("British", result.getNationality());
    }

    private static List<Author> dummyAuthors() {
        Author author1 = new Author();
        author1.setId(1);
        author1.setFirstName("George");
        author1.setLastName("Orwell");
        author1.setEmail("george.orwell@email.com");
        author1.setNationality("British");

        Author author2 = new Author();
        author2.setId(2);
        author2.setFirstName("J.K.");
        author2.setLastName("Rowling");
        author2.setEmail("jk.rowling@email.com");
        author2.setNationality("British");

        return List.of(author1, author2);
    }

}