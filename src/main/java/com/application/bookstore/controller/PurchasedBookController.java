package com.application.bookstore.controller;

import com.application.bookstore.dto.AuthorDto;
import com.application.bookstore.dto.PurchasedBookDto;
import com.application.bookstore.service.PurchasedBookService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-store-service/v1/purchases")
@SecurityRequirement(name = "basicAuth")
public class PurchasedBookController {

    private final PurchasedBookService purchaseService;

    public PurchasedBookController(PurchasedBookService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<PurchasedBookDto> purchaseBook(@RequestBody PurchasedBookDto dto) {
        return ResponseEntity.ok(purchaseService.purchaseBook(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchasedBookDto> getPurchase(@PathVariable int id) {
        return ResponseEntity.ok(purchaseService.getPurchaseById(id));
    }

    @GetMapping
    public ResponseEntity<List<PurchasedBookDto>> getAllPurchases() {
        return ResponseEntity.ok(purchaseService.getAllPurchases());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable int id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.noContent().build();
    }
}
