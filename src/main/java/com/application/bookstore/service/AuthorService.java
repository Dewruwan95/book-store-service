package com.application.bookstore.service;

import com.application.bookstore.dto.*;
import com.application.bookstore.exception.AttributeAlreadyExistsException;
import com.application.bookstore.exception.ValidationException;
import com.application.bookstore.model.Author;
import com.application.bookstore.model.Book;
import com.application.bookstore.repository.AuthorRepository;
import com.application.bookstore.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {

    private static final Logger logger = LoggerFactory.getLogger(AuthorService.class);

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }


    //--------------------------------------------------------------
    //------------------- Get All Authors-- ------------------------
    //--------------------------------------------------------------
    public List<AuthorDto> getAll() {

        logger.info("Fetching all authors");
        return toDto(authorRepository.findAll());

    }

    //--------------------------------------------------------------
    //------------------- Get Single Author By Id-------------------
    //--------------------------------------------------------------
    public AuthorDto getById(int id) {

        logger.info("Fetching author with ID: {}", id);
        return authorRepository.findById(id).map(this::toDto).orElseThrow(() -> {
            logger.warn("Author not found with ID: {}", id);
            return new EntityNotFoundException("Author not found with id " + id);
        });
    }


    //--------------------------------------------------------------
    //------------------- Create New Author ------------------------
    //--------------------------------------------------------------
    public AuthorDto create(AuthorRequestDto authorRequestDto) {

        logger.info("Creating author with email: {}", authorRequestDto.getEmail());

        validateAuthorRequestDto(authorRequestDto);
        validateEmailUniqueness(authorRequestDto.getEmail());

        Author author = toEntity(authorRequestDto);
        final Author savedAuthor = authorRepository.save(author);

        logger.info("Author created with ID: {} and email: {}", savedAuthor.getId(), savedAuthor.getEmail());

        return toDto(savedAuthor);
    }

    //--------------------------------------------------------------
    //------------------- Create New Author With Book --------------
    //--------------------------------------------------------------
    public AuthorDto createWithBooks(AuthorWithBookRequestDto authorWithBookRequestDto) {

        logger.info("Creating author with books - Email: {}, Book IDs: {}", authorWithBookRequestDto.getAuthor().getEmail(), authorWithBookRequestDto.getBookIds().size());

        AuthorRequestDto authorRequestDto = authorWithBookRequestDto.getAuthor();

        validateAuthorRequestDto(authorRequestDto);
        validateEmailUniqueness(authorRequestDto.getEmail());

        Author author = toEntity(authorRequestDto);


        List<Book> books = new ArrayList<>(bookRepository.findAllById(authorWithBookRequestDto.getBookIds()));

        for (Book book : books) {
            book.getAuthors().add(author);
        }

        author.setBooks(books);

        final Author savedAuthor = authorRepository.save(author);
        logger.info("Author created successfully with ID: {} and {} associated books", savedAuthor.getId(), books.size());
        return toDto(savedAuthor);


    }

    //--------------------------------------------------------------
    //------------------- Update Author ----------------------------
    //--------------------------------------------------------------
    public AuthorDto update(int id, AuthorDto authorDto) {

        logger.info("Updating author with ID: {}", id);

        Author existingAuthor = authorRepository.findById(id).orElseThrow(() -> {
            logger.warn("Cannot update: Author not found with ID: {}", id);
            return new EntityNotFoundException("Author not found with id " + id);
        });

        if (authorDto.getFirstName() != null) {
            existingAuthor.setFirstName(authorDto.getFirstName());
        }
        if (authorDto.getLastName() != null) {
            existingAuthor.setLastName(authorDto.getLastName());
        }
        if (authorDto.getEmail() != null) {
            existingAuthor.setEmail(authorDto.getEmail());
        }
        if (authorDto.getNationality() != null) {
            existingAuthor.setNationality(authorDto.getNationality());
        }


        final Author savedAuthor = authorRepository.save(existingAuthor);

        logger.info("Author updated successfully with ID: {}", savedAuthor.getId());
        return toDto(savedAuthor);
    }


    //--------------------------------------------------------------
    //------------------- Delete Author ----------------------------
    //--------------------------------------------------------------
    public void delete(int id) {

        logger.info("Deleting author with ID: {}", id);

        if (!authorRepository.existsById(id)) {

            logger.warn("Attempted to delete non-existent author ID: {}", id);
            throw new EntityNotFoundException("Author not found with id " + id);
        }
        authorRepository.deleteById(id);
        logger.info("Author deleted successfully with ID: {}", id);
    }


    //--------------------------------------------------------------
    //------------------- Validate AuthorRequestDto --------------
    //--------------------------------------------------------------
    public void validateAuthorRequestDto(AuthorRequestDto authorRequestDto) {
        if (authorRequestDto.getFirstName() == null) {
            throw new ValidationException("firstName");
        }

        if (authorRequestDto.getLastName() == null) {
            throw new ValidationException("lastName");
        }

        if (authorRequestDto.getEmail() == null) {
            throw new ValidationException("email");
        }

        if (!authorRequestDto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("email", "Invalid email format");
        }

        if (authorRequestDto.getNationality() == null) {
            throw new ValidationException("nationality");
        }

    }

    //--------------------------------------------------------------
    //------------------- Check Email Uniqueness -------------------
    //--------------------------------------------------------------
    private void validateEmailUniqueness(String email) {
        if (authorRepository.findByEmail(email) != null) {
            throw new AttributeAlreadyExistsException("Author", "email", email);
        }

    }

    //--------------------------------------------------------------
    //----------------- Convert Author to AuthorDto ----------------
    //--------------------------------------------------------------
    public List<AuthorDto> toDto(List<Author> authors) {
        return authors.stream().map(this::toDto).toList();
    }

    private AuthorDto toDto(Author author) {
        if (author == null) {
            return null;
        }

        AuthorDto result = new AuthorDto();
        result.setId(author.getId());
        result.setFirstName(author.getFirstName());
        result.setLastName(author.getLastName());
        result.setEmail(author.getEmail());
        result.setNationality(author.getNationality());

        final List<BookRequestDto> bookRequestDtos = author.getBooks().stream().map(book -> {
            BookRequestDto bookRequestDto = new BookRequestDto();
            bookRequestDto.setTitle(book.getTitle());
            bookRequestDto.setPrice(book.getPrice());
            bookRequestDto.setGenre(book.getGenre());
            bookRequestDto.setStock(book.getStock());
            return bookRequestDto;
        }).toList();
        result.setBookIds(bookRequestDtos);

        return result;
    }


    //--------------------------------------------------------------
    // ------------ convert AuthorDto to Author --------------------
    //--------------------------------------------------------------
    public Author toEntity(AuthorRequestDto authorDto) {
        if (authorDto == null) {

            return null;
        }
        Author author = new Author();
        author.setFirstName(authorDto.getFirstName());
        author.setLastName(authorDto.getLastName());
        author.setEmail(authorDto.getEmail());
        author.setNationality(authorDto.getNationality());

        return author;
    }


}