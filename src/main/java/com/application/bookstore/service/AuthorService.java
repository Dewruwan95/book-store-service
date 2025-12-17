package com.application.bookstore.service;

import com.application.bookstore.dto.*;
import com.application.bookstore.exception.ValidationException;
import com.application.bookstore.model.Author;
import com.application.bookstore.model.Book;
import com.application.bookstore.repository.AuthorRepository;
import com.application.bookstore.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {
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
        return toDto(authorRepository.findAll());

    }

    //--------------------------------------------------------------
    //------------------- Get Single Author By Id-------------------
    //--------------------------------------------------------------
    public AuthorDto getById(int id) {

        return authorRepository.findById(id).map(author -> toDto(author)).orElseThrow(() -> new EntityNotFoundException("Author not found with id " + id));
    }


    //--------------------------------------------------------------
    //------------------- Create New Author ------------------------
    //--------------------------------------------------------------
    public AuthorDto create(AuthorRequestDto authorRequestDto) {

        validateAuthorRequestDto(authorRequestDto);

        Author author = toEntity(authorRequestDto);
        final Author savedAuthor = authorRepository.save(author);
        return toDto(savedAuthor);
    }

    //--------------------------------------------------------------
    //------------------- Create New Author With Book --------------
    //--------------------------------------------------------------
    public AuthorDto createWithBooks(AuthorWithBookRequestDto authorWithBookRequestDto) {

        AuthorRequestDto authorRequestDto = authorWithBookRequestDto.getAuthor();

        validateAuthorRequestDto(authorRequestDto);

        Author author = toEntity(authorRequestDto);


        List<Book> books = new ArrayList<>(bookRepository.findAllById(authorWithBookRequestDto.getBookIds()));

        for (Book book : books) {
            book.getAuthors().add(author);
        }

        author.setBooks(books);

        final Author savedAuthor = authorRepository.save(author);
        return toDto(savedAuthor);


    }

    //--------------------------------------------------------------
    //------------------- Update Author ----------------------------
    //--------------------------------------------------------------
    public AuthorDto update(int id, AuthorDto authorDto) {
        Author existingAuthor = authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Author not found with id " + id));

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

        return toDto(savedAuthor);
    }


    //--------------------------------------------------------------
    //------------------- Delete Author ----------------------------
    //--------------------------------------------------------------
    public void delete(int id) {
        authorRepository.deleteById(id);
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

//        if (authorRequestDto.getNationality() == null) {
//            throw new ValidationException("nationality");
//        }
    }

    //--------------------------------------------------------------
    //----------------- Convert Author to AuthorDto ----------------
    //--------------------------------------------------------------
    public List<AuthorDto> toDto(List<Author> authors) {
        List<AuthorDto> result = authors.stream().map(author -> toDto(author)).toList();

        return result;
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

        final List<BookRequestDto> bookRequestDtos = author.getBooks().stream()
                .map(book -> {
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