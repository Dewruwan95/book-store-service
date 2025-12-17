package com.application.bookstore.controller;

import com.application.bookstore.dto.AuthorDto;
import com.application.bookstore.dto.PurchasedBookDto;
import com.application.bookstore.service.PurchasedBookService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-store-service/v1/purchase")
@SecurityRequirement(name = "basicAuth")
public class PurchasedBookController {

    private final PurchasedBookService purchaseService;

    public PurchasedBookController(PurchasedBookService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<PurchasedBookDto> purchaseBook(@RequestBody PurchasedBookDto dto) {
        PurchasedBookDto created = purchaseService.purchaseBook(dto);

        // Return 201 CREATED with location header
        return ResponseEntity
                .status(HttpStatus.CREATED)  // This returns 201
                .body(created);
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
