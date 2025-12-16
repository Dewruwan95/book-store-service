package com.application.bookstore.repository;

import com.application.bookstore.model.PurchasedBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchasedBookRepository extends JpaRepository<PurchasedBook, Integer> {
}
