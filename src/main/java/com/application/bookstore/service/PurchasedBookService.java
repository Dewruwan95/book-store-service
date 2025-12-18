package com.application.bookstore.service;

import com.application.bookstore.dto.PurchasedBookDto;
import com.application.bookstore.model.Book;
import com.application.bookstore.model.Customer;
import com.application.bookstore.model.PurchasedBook;
import com.application.bookstore.repository.BookRepository;
import com.application.bookstore.repository.CustomerRepository;
import com.application.bookstore.repository.PurchasedBookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PurchasedBookService {
    private final PurchasedBookRepository purchaseRepo;
    private final CustomerRepository customerRepo;
    private final BookRepository bookRepo;

    public PurchasedBookService(
            PurchasedBookRepository purchaseRepo,
            CustomerRepository customerRepo,
            BookRepository bookRepo) {

        this.purchaseRepo = purchaseRepo;
        this.customerRepo = customerRepo;
        this.bookRepo = bookRepo;
    }


    public PurchasedBookDto purchaseBook(PurchasedBookDto dto) {
        Customer customer = customerRepo.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Book book = bookRepo.findById(dto.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        PurchasedBook purchase =new PurchasedBook();
        purchase.setCustomer(customer);
        purchase.setBook(book);
        purchase.setPurchaseDate(LocalDate.from(LocalDateTime.now()));


        return toDto(purchaseRepo.save(purchase));
    }


    public PurchasedBookDto getPurchaseById(int id) {
        return purchaseRepo.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Purchase not found"));
    }


    public List<PurchasedBookDto> getAllPurchases() {
        return purchaseRepo.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }


    public void deletePurchase(int id) {
        if (!purchaseRepo.existsById(id)) {
            throw new EntityNotFoundException("Purchase not found with id " + id);
        }
        purchaseRepo.deleteById(id);
    }

    private PurchasedBookDto toDto(PurchasedBook purchase) {
        PurchasedBookDto dto = new PurchasedBookDto();
        dto.setPurchaseId(purchase.getId());
        dto.setPurchaseDate(purchase.getPurchaseDate());
        dto.setCustomerId(purchase.getCustomer().getId());
        dto.setBookId(purchase.getBook().getId());
        return dto;
    }
}