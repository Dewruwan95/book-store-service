package com.application.bookstore.service;

import com.application.bookstore.dto.*;
import com.application.bookstore.exception.ValidationException;
import com.application.bookstore.model.Author;
import com.application.bookstore.model.Book;
import com.application.bookstore.repository.AuthorRepository;
import com.application.bookstore.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

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
        logger.info("Fetching all books");
        return toDto(bookRepository.findAll());
    }

    //--------------------------------------------------------------
    //------------------- Get Single Book By Id --------------------
    //--------------------------------------------------------------
    public BookDto getById(int id) {
        logger.info("Fetching book with ID: {}", id);
        return bookRepository.findById(id).map(this::toDto).orElseThrow(() -> {
            logger.warn("Book not found with ID: {}", id);
            return new EntityNotFoundException("Book not found with id " + id);
        });


    }

    //--------------------------------------------------------------
    //------------------- Create New Book --------------------------
    //--------------------------------------------------------------
    public BookDto create(BookRequestDto bookRequestDto) {

        logger.info("Creating new book with title: {}", bookRequestDto.getTitle());

        validateBookRequestDto(bookRequestDto);

        Book book = toEntity(bookRequestDto);
        final Book savedBook = bookRepository.save(book);

        logger.info("Book created successfully with ID: {} and title: {}", savedBook.getId(), savedBook.getTitle());
        return toDto(savedBook);


    }

    //--------------------------------------------------------------
    //------------------- Create New Book With New Author ----------
    //--------------------------------------------------------------
    public BookDto createBookWithNewAuthor(BookWithNewAuthorDto bookWithNewAuthorDto) {

        logger.info("Creating book with new author - Book title: {}, Author email: {}", bookWithNewAuthorDto.getBook().getTitle(), bookWithNewAuthorDto.getAuthor().getEmail());

        BookRequestDto bookRequestDto = bookWithNewAuthorDto.getBook();
        AuthorRequestDto authorRequestDto = bookWithNewAuthorDto.getAuthor();

        validateBookRequestDto(bookRequestDto);
        authorService.validateAuthorRequestDto(authorRequestDto);

        Book book = toEntity(bookRequestDto);

        // Check if author with this email already exists
        Author existingAuthor = authorRepository.findByEmail(authorRequestDto.getEmail());


        if (existingAuthor != null) {
            // If author already exists, attach the existing author to the book
            logger.info("Attaching existing author ID: {} to new book", existingAuthor.getId());
            book.getAuthors().add(existingAuthor);

            final Book savedBook = bookRepository.save(book);
            logger.info("Book created with existing author - Book ID: {}, Author ID: {}", savedBook.getId(), existingAuthor.getId());
            return toDto(savedBook);
        } else {
            // Create new author and attach to book
            logger.info("Creating new author for book");
            Author author = authorService.toEntity(authorRequestDto);

            final Author savedAuthor = authorRepository.save(author);
            book.getAuthors().add(savedAuthor);

            final Book savedBook = bookRepository.save(book);
            logger.info("Book created with new author - Book ID: {}, Author ID: {}", savedBook.getId(), savedAuthor.getId());
            return toDto(savedBook);
        }

    }
    //--------------------------------------------------------------
    //------------------- Update Book ------------------------------
    //--------------------------------------------------------------
    public BookDto update(int id, BookRequestDto bookRequestDto) {
        logger.info("Updating book with ID: {}", id);

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Cannot update: Book not found with ID: {}", id);
                    return new EntityNotFoundException("Book not found with id " + id);
                });

        validateBookRequestDto(bookRequestDto);

        if (bookRequestDto.getTitle() != null) {
            existingBook.setTitle(bookRequestDto.getTitle());
        }
        if (bookRequestDto.getGenre() != null) {
            existingBook.setGenre(bookRequestDto.getGenre());
        }
        if (bookRequestDto.getPrice() != 0) {
            existingBook.setPrice(bookRequestDto.getPrice());
        }
        if (bookRequestDto.getStock() >= 0) {
            existingBook.setStock(bookRequestDto.getStock());
        }

        final Book savedBook = bookRepository.save(existingBook);
        logger.info("Book updated successfully with ID: {}", savedBook.getId());
        return toDto(savedBook);
    }



    //--------------------------------------------------------------
    //------------------- Attach Author With Book ------------------
    //--------------------------------------------------------------
    public BookDto attachAuthor(int bookId, int authorId) {

        logger.info("Attaching author ID: {} to book ID: {}", authorId, bookId);

        Book book = bookRepository.findById(bookId).orElseThrow(() -> {
            logger.warn("Book not found for attachment - Book ID: {}", bookId);
            return new EntityNotFoundException("Book not found  with id " + bookId);
        });

        Author author = authorRepository.findById(authorId).orElseThrow(() -> {
            logger.warn("Author not found for attachment - Author ID: {}", authorId);
            return new EntityNotFoundException("Author not found  with id " + authorId);
        });

        book.getAuthors().add(author);
        final Book saveBook = bookRepository.save(book);

        logger.info("Author ID: {} attached successfully to book ID: {}", authorId, bookId);
        return toDto(saveBook);

    }

    //--------------------------------------------------------------
    //------------------- Delete Book ------------------------------
    //--------------------------------------------------------------
    public void delete(int id) {
        logger.info("Deleting book with ID: {}", id);
        if (!bookRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent book ID: {}", id);
            throw new EntityNotFoundException("Book not found with id " + id);
        }
        bookRepository.deleteById(id);
        logger.info("Book deleted successfully with ID: {}", id);
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