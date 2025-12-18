package com.application.bookstore.service;

import com.application.bookstore.dto.*;
import com.application.bookstore.exception.ValidationException;
import com.application.bookstore.model.Author;
import com.application.bookstore.model.Book;
import com.application.bookstore.repository.AuthorRepository;
import com.application.bookstore.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final AuthorService authorService;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.authorService = authorService;
    }


    //--------------------------------------------------------------
    //------------------- Get All Books ----------------------------
    //--------------------------------------------------------------
    public List<BookDto> getAll() {

        return toDto(bookRepository.findAll());
    }

    //--------------------------------------------------------------
    //------------------- Get Single Book By Id --------------------
    //--------------------------------------------------------------
    public BookDto getById(int id) {


        return bookRepository.findById(id).map(this::toDto).orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));


    }

    //--------------------------------------------------------------
    //------------------- Create New Book --------------------------
    //--------------------------------------------------------------
    public BookDto create(BookRequestDto bookRequestDto) {

        validateBookRequestDto(bookRequestDto);

        Book book = toEntity(bookRequestDto);
        final Book savedBook = bookRepository.save(book);
        return toDto(savedBook);


    }

    //--------------------------------------------------------------
    //------------------- Create New Book With New Author ----------
    //--------------------------------------------------------------
    public BookDto createBookWithNewAuthor(BookWithNewAuthorDto bookWithNewAuthorDto) {

        BookRequestDto bookRequestDto = bookWithNewAuthorDto.getBook();
        AuthorRequestDto authorRequestDto = bookWithNewAuthorDto.getAuthor();

        validateBookRequestDto(bookRequestDto);
        authorService.validateAuthorRequestDto(authorRequestDto);

        Book book = toEntity(bookRequestDto);

        // Check if author with this email already exists
        Author existingAuthor = authorRepository.findByEmail(authorRequestDto.getEmail());


        if (existingAuthor != null) {
            // If author already exists, attach the existing author to the book
            book.getAuthors().add(existingAuthor);

            final Book savedBook = bookRepository.save(book);
            return toDto(savedBook);
        } else {
            // Create new author and attach to book
            Author author = authorService.toEntity(authorRequestDto);

            final Author savedAuthor = authorRepository.save(author);
            book.getAuthors().add(savedAuthor);

            final Book savedBook = bookRepository.save(book);
            return toDto(savedBook);
        }

    }
    //--------------------------------------------------------------
    //------------------- Update Book ------------------------------
    //--------------------------------------------------------------

    //--------------------------------------------------------------
    //------------------- Attach Author With Book ------------------
    //--------------------------------------------------------------
    public BookDto attachAuthor(int bookId, int authorId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found  with id " + bookId));

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found  with id " + authorId));

        book.getAuthors().add(author);
        final Book saveBook = bookRepository.save(book);
        return toDto(saveBook);

    }

    //--------------------------------------------------------------
    //------------------- Delete Book ------------------------------
    //--------------------------------------------------------------
    public void delete(int id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book not found with id " + id);
        }
        bookRepository.deleteById(id);


    }

    //--------------------------------------------------------------
    //------------------- Validate BookRequestDto ------------------
    //--------------------------------------------------------------
    private void validateBookRequestDto(BookRequestDto bookRequestDto) {
        if (bookRequestDto.getTitle() == null) {
            throw new ValidationException("title");
        }
        if (bookRequestDto.getGenre() == null) {
            throw new ValidationException("genre");
        }
        if (bookRequestDto.getPrice() == 0) {
            throw new ValidationException("price");
        }
        if (bookRequestDto.getPrice() < 0) {
            throw new ValidationException("stock", "price should be greater than 0");
        }

        if (bookRequestDto.getStock() < 0) {
            throw new ValidationException("stock", "stock should be greater than 0");
        }
    }


    //--------------------------------------------------------------
    //----------------- Convert Book to BookDto --------------------
    //--------------------------------------------------------------
    private List<BookDto> toDto(List<Book> books) {
        return books.stream().map(this::toDto).toList();
    }

    private BookDto toDto(Book book) {

        if (book == null) {
            return null;
        }

        BookDto result = new BookDto();
        result.setId(book.getId());
        result.setTitle(book.getTitle());
        result.setGenre(book.getGenre());
        result.setPrice(book.getPrice());
        result.setStock(book.getStock());

        final List<AuthorRequestDto> authorRequestDtos = book.getAuthors().stream().map(author -> {
            AuthorRequestDto authorRequestDto = new AuthorRequestDto();
            authorRequestDto.setFirstName(author.getFirstName());
            authorRequestDto.setLastName(author.getLastName());
            authorRequestDto.setEmail(author.getEmail());
            authorRequestDto.setNationality(author.getNationality());

            return authorRequestDto;

        }).toList();


        result.setAuthor(authorRequestDtos);

        return result;
    }


    //--------------------------------------------------------------
    // ------------ convert BookDto to Book ------------------------
    //--------------------------------------------------------------
    private Book toEntity(BookRequestDto bookRequestDto) {
        if (bookRequestDto == null) {
            return null;
        }
        Book book = new Book();
        book.setTitle(bookRequestDto.getTitle());
        book.setGenre(bookRequestDto.getGenre());
        book.setPrice(bookRequestDto.getPrice());
        book.setStock(bookRequestDto.getStock());

        return book;

    }


}