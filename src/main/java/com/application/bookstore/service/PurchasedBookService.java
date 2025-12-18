package com.application.bookstore.service;

import com.application.bookstore.dto.PurchasedBookDto;
import com.application.bookstore.model.Book;
import com.application.bookstore.model.Customer;
import com.application.bookstore.model.PurchasedBook;
import com.application.bookstore.repository.BookRepository;
import com.application.bookstore.repository.CustomerRepository;
import com.application.bookstore.repository.PurchasedBookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchasedBookService {
    private static final Logger logger = LoggerFactory.getLogger(PurchasedBookService.class);

    private final PurchasedBookRepository purchasedBookRepository;
    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;

    public PurchasedBookService(
            PurchasedBookRepository purchasedBookRepository,
            CustomerRepository customerRepository,
            BookRepository bookRepository) {

        this.purchasedBookRepository = purchasedBookRepository;
        this.customerRepository = customerRepository;
        this.bookRepository = bookRepository;
    }

    //--------------------------------------------------------------
    //------------------- Get All Purchases ------------------------
    //--------------------------------------------------------------
    public List<PurchasedBookDto> getAll() {
        logger.info("Fetching all purchases");
        return toDto(purchasedBookRepository.findAll());
    }

    //--------------------------------------------------------------
    //------------------- Get Single Purchase By Id ----------------
    //--------------------------------------------------------------
    public PurchasedBookDto getById(int id) {
        logger.info("Fetching purchase with ID: {}", id);
        return purchasedBookRepository.findById(id).map(this::toDto).orElseThrow(() -> {
            logger.warn("Purchase not found with ID: {}", id);
            return new EntityNotFoundException("Purchase not found with id " + id);
        });
    }

    //--------------------------------------------------------------
    //------------------- Create New Purchase ----------------------
    //--------------------------------------------------------------
    public PurchasedBookDto create(PurchasedBookDto purchasedBookDto) {
        logger.info("Creating new purchase - Customer ID: {}, Book ID: {}",
                purchasedBookDto.getCustomerId(), purchasedBookDto.getBookId());

        Customer customer = customerRepository.findById(purchasedBookDto.getCustomerId())
                .orElseThrow(() -> {
                    logger.warn("Cannot create purchase: Customer not found with ID: {}", purchasedBookDto.getCustomerId());
                    return new EntityNotFoundException("Customer not found with id " + purchasedBookDto.getCustomerId());
                });

        Book book = bookRepository.findById(purchasedBookDto.getBookId())
                .orElseThrow(() -> {
                    logger.warn("Cannot create purchase: Book not found with ID: {}", purchasedBookDto.getBookId());
                    return new EntityNotFoundException("Book not found with id " + purchasedBookDto.getBookId());
                });

        PurchasedBook purchasedBook = toEntity(purchasedBookDto, customer, book);
        final PurchasedBook savedPurchasedBook = purchasedBookRepository.save(purchasedBook);

        logger.info("Purchase created successfully with ID: {}, Customer: {} {}, Book: {}",
                savedPurchasedBook.getId(),
                customer.getFirstName(), customer.getLastName(),
                book.getTitle());

        return toDto(savedPurchasedBook);
    }

    //--------------------------------------------------------------
    //------------------- Delete Purchase --------------------------
    //--------------------------------------------------------------
    public void delete(int id) {
        logger.info("Deleting purchase with ID: {}", id);

        if (!purchasedBookRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent purchase ID: {}", id);
            throw new EntityNotFoundException("Purchase not found with id " + id);
        }

        purchasedBookRepository.deleteById(id);
        logger.info("Purchase deleted successfully with ID: {}", id);
    }

    //--------------------------------------------------------------
    //------------------- Convert PurchasedBook to PurchasedBookDto
    //--------------------------------------------------------------
    public List<PurchasedBookDto> toDto(List<PurchasedBook> purchasedBooks) {
        return purchasedBooks.stream().map(this::toDto).toList();
    }

    private PurchasedBookDto toDto(PurchasedBook purchasedBook) {
        if (purchasedBook == null) {
            return null;
        }

        PurchasedBookDto result = new PurchasedBookDto();
        result.setPurchaseId(purchasedBook.getId());
        result.setPurchaseDate(purchasedBook.getPurchaseDate());
        result.setCustomerId(purchasedBook.getCustomer().getId());
        result.setBookId(purchasedBook.getBook().getId());

        return result;
    }

    //--------------------------------------------------------------
    // ------------ convert PurchasedBookDto to PurchasedBook ------
    //--------------------------------------------------------------
    private PurchasedBook toEntity(PurchasedBookDto purchasedBookDto, Customer customer, Book book) {
        if (purchasedBookDto == null) {
            return null;
        }

        PurchasedBook result = new PurchasedBook();
        result.setCustomer(customer);
        result.setBook(book);
        result.setPurchaseDate(LocalDate.from(LocalDateTime.now()));

        return result;
    }
}