package com.application.bookstore.service;
import com.application.bookstore.dto.PurchasedBookDto;
import com.application.bookstore.model.Book;
import com.application.bookstore.model.Customer;
import com.application.bookstore.model.PurchasedBook;
import com.application.bookstore.repository.BookRepository;
import com.application.bookstore.repository.CustomerRepository;
import com.application.bookstore.repository.PurchasedBookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@AutoConfigureMockMvc(addFilters = false)
class PurchasedBookServiceTest {
    private PurchasedBookRepository purchasedBookRepository;
    private CustomerRepository customerRepository;
    private BookRepository bookRepository;
    private PurchasedBookService purchasedBookService;

    @BeforeEach
    void beforeEachTest() {
        purchasedBookRepository = Mockito.mock(PurchasedBookRepository.class);
        customerRepository = Mockito.mock(CustomerRepository.class);
        bookRepository = Mockito.mock(BookRepository.class);
        purchasedBookService = new PurchasedBookService(purchasedBookRepository, customerRepository, bookRepository);
    }

    @Test
    void context_loads() {
        Assertions.assertNotNull(purchasedBookService);
        Assertions.assertNotNull(purchasedBookRepository);
        Assertions.assertNotNull(customerRepository);
        Assertions.assertNotNull(bookRepository);
    }

    @Test
    void should_return_all_purchased_books() {
        Mockito.when(purchasedBookRepository.findAll()).thenReturn(dummyPurchasedBooks());

        List<PurchasedBookDto> result = purchasedBookService.getAllPurchases();

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(2, result.size());

        Assertions.assertEquals(1, result.getFirst().getPurchaseId());
        Assertions.assertEquals(1, result.getFirst().getCustomerId());
        Assertions.assertEquals(1, result.getFirst().getBookId());
    }

    @Test
    void should_create_new_purchased_book() {
        // Create request - assuming you have a request DTO
        PurchasedBookDto request = new PurchasedBookDto();
        request.setCustomerId(1);
        request.setBookId(1);
        request.setPurchaseDate(LocalDate.now());

        // Mock customer exists
        Customer customer = new Customer();
        customer.setId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        Mockito.when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        // Mock book exists
        Book book = new Book();
        book.setId(1);
        book.setTitle("1984");
        book.setPrice(12.99);
        Mockito.when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        // Mock saved purchased book
        PurchasedBook savedPurchasedBook = new PurchasedBook();
        savedPurchasedBook.setId(1);
        savedPurchasedBook.setCustomer(customer);
        savedPurchasedBook.setBook(book);
        savedPurchasedBook.setPurchaseDate(LocalDate.now());

        Mockito.when(purchasedBookRepository.save(Mockito.any(PurchasedBook.class)))
                .thenReturn(savedPurchasedBook);

        PurchasedBookDto result = purchasedBookService.purchaseBook(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getPurchaseId());
        Assertions.assertEquals(1, result.getCustomerId());
        Assertions.assertEquals(1, result.getBookId());
    }

    private static List<PurchasedBook> dummyPurchasedBooks() {
        // Create customer
        Customer customer = new Customer();
        customer.setId(1);
        customer.setFirstName("John");
        customer.setLastName("Doe");

        // Create book 1
        Book book1 = new Book();
        book1.setId(1);
        book1.setTitle("1984");

        // Create purchased book 1
        PurchasedBook purchased1 = new PurchasedBook();
        purchased1.setId(1);
        purchased1.setCustomer(customer);
        purchased1.setBook(book1);
        purchased1.setPurchaseDate(LocalDate.now());

        // Create book 2
        Book book2 = new Book();
        book2.setId(2);
        book2.setTitle("Harry Potter");

        // Create purchased book 2
        PurchasedBook purchased2 = new PurchasedBook();
        purchased2.setId(2);
        purchased2.setCustomer(customer);
        purchased2.setBook(book2);
        purchased2.setPurchaseDate(LocalDate.now().minusDays(1));

        return List.of(purchased1, purchased2);
    }
}