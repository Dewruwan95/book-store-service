package com.application.bookstore.repository;

import com.application.bookstore.model.Book;
import com.application.bookstore.model.Customer;
import com.application.bookstore.model.PurchasedBook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import javax.sql.DataSource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureMockMvc(addFilters = false)
class PurchasedBookRepositoryTest {

    @Autowired
    private PurchasedBookRepository purchasedBookRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        // Create and save a customer first
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@email.com");
        customer.setPhoneNumber("0123456789");
        customer.setAddress("Nugegoda, Colombo");
        Customer savedCustomer = customerRepository.save(customer);

        // Create and save a book first
        Book book = new Book();
        book.setTitle("1984");
        book.setPrice(12.99);
        book.setGenre("Dystopian");
        book.setStock(50);
        Book savedBook = bookRepository.save(book);

        // Create purchased book 1
        PurchasedBook purchasedBook1 = new PurchasedBook();
        purchasedBook1.setCustomer(savedCustomer);
        purchasedBook1.setBook(savedBook);
        purchasedBook1.setPurchaseDate(LocalDate.now());
        purchasedBookRepository.save(purchasedBook1);

        // Create another book
        Book book2 = new Book();
        book2.setTitle("Harry Potter");
        book2.setPrice(15.99);
        book2.setGenre("Fantasy");
        book2.setStock(100);
        Book savedBook2 = bookRepository.save(book2);

        // Create purchased book 2
        PurchasedBook purchasedBook2 = new PurchasedBook();
        purchasedBook2.setCustomer(savedCustomer);
        purchasedBook2.setBook(savedBook2);
        purchasedBook2.setPurchaseDate(LocalDate.now().minusDays(1));
        purchasedBookRepository.save(purchasedBook2);
    }

    @Test
    void context_loads() {
        Assertions.assertNotNull(purchasedBookRepository);
        Assertions.assertNotNull(customerRepository);
        Assertions.assertNotNull(bookRepository);
        Assertions.assertNotNull(dataSource);
    }

    @Test
    void should_return_all_purchased_books() {
        List<PurchasedBook> result = purchasedBookRepository.findAll();
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(2, result.size());
    }
}